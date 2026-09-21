#!/usr/bin/env python3
"""Convert World of Stone forge/1.20.1 data -> neoforge/1.21.1 data.

Applies the 1.21 datapack changes verified against vanilla 1.21.1 data:
  - directory renames: recipes->recipe, loot_tables->loot_table,
    tags/blocks->tags/block, tags/items->tags/item
  - recipes: result "item"->"id"; string result -> {"id": ...} (stonecutting
    moves top-level count into the result object); forge:mod_loaded condition
    -> neoforge:conditions + neoforge:mod_loaded. Ingredients unchanged.
  - loot: match_tool enchantment predicate wraps into
    predicate.predicates["minecraft:enchantments"] with key "enchantment"->
    "enchantments". apply_bonus/table_bonus/etc unchanged. Skips the single
    copy_nbt file (mimic_speleothem, not registered on neoforge).
  - biome modifiers: forge:add_features -> neoforge:add_features, dir forge->neoforge
Deferred (not converted here): data/forge tags (forge:->c: ore-dictionary mapping).
"""
import json, os, shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parent
SRC = ROOT / "forge" / "src" / "main" / "resources" / "data"
DST = ROOT / "neoforge" / "src" / "main" / "resources" / "data"

stats = {"recipe": 0, "loot": 0, "loot_skipped": 0, "tag": 0, "worldgen": 0,
         "biome_modifier": 0, "other": 0}


def load(p):
    with open(p, "r", encoding="utf-8") as f:
        return json.load(f)


def dump(obj, p):
    p.parent.mkdir(parents=True, exist_ok=True)
    with open(p, "w", encoding="utf-8", newline="\n") as f:
        json.dump(obj, f, indent=2)
        f.write("\n")


def fix_recipe(obj):
    r = obj.get("result")
    if isinstance(r, str):
        newr = {"id": r}
        if "count" in obj:
            newr["count"] = obj.pop("count")
        obj["result"] = newr
    elif isinstance(r, dict) and "item" in r:
        r["id"] = r.pop("item")
    if isinstance(obj.get("results"), list):
        for e in obj["results"]:
            if isinstance(e, dict) and "item" in e:
                e["id"] = e.pop("item")
    if "conditions" in obj:
        conds = obj.pop("conditions")
        for c in conds:
            if isinstance(c, dict) and c.get("type") == "forge:mod_loaded":
                c["type"] = "neoforge:mod_loaded"
        obj["neoforge:conditions"] = conds
    return obj


def fix_loot(node):
    if isinstance(node, dict):
        pred = node.get("predicate")
        if isinstance(pred, dict) and isinstance(pred.get("enchantments"), list):
            lst = pred.pop("enchantments")
            for e in lst:
                if isinstance(e, dict) and "enchantment" in e:
                    e["enchantments"] = e.pop("enchantment")
            pred.setdefault("predicates", {})["minecraft:enchantments"] = lst
        for v in node.values():
            fix_loot(v)
    elif isinstance(node, list):
        for v in node:
            fix_loot(v)
    return node


def convert_tree(rel_src, rel_dst, kind):
    base = SRC / rel_src
    if not base.exists():
        return
    for p in base.rglob("*.json"):
        rel = p.relative_to(base)
        out = DST / rel_dst / rel
        if kind == "recipe":
            dump(fix_recipe(load(p)), out)
            stats["recipe"] += 1
        elif kind == "loot":
            if p.name == "mimic_speleothem.json":
                stats["loot_skipped"] += 1
                continue
            dump(fix_loot(load(p)), out)
            stats["loot"] += 1
        elif kind == "biome_modifier":
            obj = load(p)
            if obj.get("type") == "forge:add_features":
                obj["type"] = "neoforge:add_features"
            dump(obj, out)
            stats["biome_modifier"] += 1
        else:
            out.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(p, out)
            stats[kind] += 1


def main():
    if DST.exists():
        shutil.rmtree(DST)
    convert_tree("worldofstone/recipes", "worldofstone/recipe", "recipe")
    convert_tree("worldofstone/loot_tables", "worldofstone/loot_table", "loot")
    convert_tree("worldofstone/tags/blocks", "worldofstone/tags/block", "tag")
    convert_tree("worldofstone/tags/items", "worldofstone/tags/item", "tag")
    convert_tree("worldofstone/worldgen", "worldofstone/worldgen", "worldgen")
    convert_tree("worldofstone/forge/biome_modifier",
                 "worldofstone/neoforge/biome_modifier", "biome_modifier")
    convert_tree("worldofstone/pe_custom_conversions",
                 "worldofstone/pe_custom_conversions", "other")
    convert_tree("minecraft/tags/blocks", "minecraft/tags/block", "tag")
    convert_tree("minecraft/tags/items", "minecraft/tags/item", "tag")
    print("Conversion complete:", json.dumps(stats, indent=2))


if __name__ == "__main__":
    main()
