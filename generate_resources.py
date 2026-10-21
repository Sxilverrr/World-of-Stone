import json
import os
import shutil
import urllib.request

from PIL import Image

ROOT = os.path.dirname(os.path.abspath(__file__))
COMMON_RES = os.path.join(ROOT, "src", "main", "resources")
RES = os.path.join(ROOT, "forge", "src", "main", "resources")
SRC_TEX = os.path.join(COMMON_RES, "assets", "worldofstone", "textures", "block")
SRC_OVERLAY_DIR = os.path.join(SRC_TEX, "overlays", "overgrown")
SRC_ITEM_TEX = os.path.join(COMMON_RES, "assets", "worldofstone", "textures", "item")
VANILLA_CACHE = os.path.join(ROOT, ".vanilla_textures")
VANILLA_BASE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.20.1/assets/minecraft/textures/block"

DST_ASSETS = os.path.join(COMMON_RES, "assets", "worldofstone")
DST_DATA = os.path.join(RES, "data", "worldofstone")
DST_MC_TAGS = os.path.join(RES, "data", "minecraft", "tags", "blocks")
DST_MC_ITEM_TAGS = os.path.join(RES, "data", "minecraft", "tags", "items")
DST_FORGE_TAGS = os.path.join(RES, "data", "forge", "tags", "blocks")
DST_FORGE_ITEM_TAGS = os.path.join(RES, "data", "forge", "tags", "items")
DST_BLOCK_TEX = os.path.join(DST_ASSETS, "textures", "block")
DST_ITEM_TEX = os.path.join(DST_ASSETS, "textures", "item")

PICKAXE_BLOCKS = []
SHOVEL_BLOCKS = []
AXE_BLOCKS = []
HOE_BLOCKS = []
LOGS_THAT_BURN_BLOCKS = []
WITHER_IMMUNE_BLOCKS = []
WALL_BLOCKS = []
STAIR_BLOCKS = []
SLAB_BLOCKS = []
BUTTON_BLOCKS = []
SAND_BLOCKS = []
GRAVEL_BLOCKS = []
SPELEOTHEM_BLOCKS = []
SMOOTH_STONE_BLOCKS = []
SMOOTH_STONE_SLABS = []
VANILLA_HOST_ORE_BLOCKS = []
ORE_BLOCKS_BY_TYPE = {}

VANILLA_ORE_HOSTS = ["granite", "diorite", "andesite", "tuff", "netherrack", "blackstone", "basalt", "end_stone", "obsidian"]
HOST_TEXTURE_OVERRIDES = {"basalt": "basalt_side"}
IGNEOUS = ["adamellite", "pyroxenite", "rhyolite", "tonalite", "gabbro", "latite", "komatiite", "dacite"]
VARIANT_TEXTURE_SOURCE = {}
METAMORPHIC = ["gneiss", "eclogite", "marble", "quartzite", "blueschist", "greenschist", "soapstone", "migmatite"]
SEDIMENTARY = ["limestone", "chalk", "shale", "siltstone", "lignite", "dolomite", "greywacke", "chert"]
ALL_VARIANTS = IGNEOUS + METAMORPHIC + SEDIMENTARY

FOSSILS = ["ammonite_fossil", "shell_fossil", "rib_fossil", "skull_fossil", "bone_fossil"]

ORES = [
    {"name": "coal_ore", "vanilla": "coal_ore", "drop": "minecraft:coal", "smelt_result": "minecraft:coal", "smelt_xp": 0.1, "fortune": "ore_drops", "min": 1, "max": 1, "needs": "wood", "rate": "singular"},
    {"name": "iron_ore", "vanilla": "iron_ore", "drop": "minecraft:raw_iron", "smelt_result": "minecraft:iron_ingot", "smelt_xp": 0.7, "fortune": "ore_drops", "min": 1, "max": 1, "needs": "stone", "rate": "singular"},
    {"name": "gold_ore", "vanilla": "gold_ore", "drop": "minecraft:raw_gold", "smelt_result": "minecraft:gold_ingot", "smelt_xp": 1.0, "fortune": "ore_drops", "min": 1, "max": 1, "needs": "iron", "rate": "singular"},
    {"name": "diamond_ore", "vanilla": "diamond_ore", "drop": "minecraft:diamond", "smelt_result": "minecraft:diamond", "smelt_xp": 1.0, "fortune": "ore_drops", "min": 1, "max": 1, "needs": "iron", "rate": "singular"},
    {"name": "emerald_ore", "vanilla": "emerald_ore", "drop": "minecraft:emerald", "smelt_result": "minecraft:emerald", "smelt_xp": 1.0, "fortune": "ore_drops", "min": 1, "max": 1, "needs": "iron", "rate": "singular"},
    {"name": "redstone_ore", "vanilla": "redstone_ore", "drop": "minecraft:redstone", "smelt_result": "minecraft:redstone", "smelt_xp": 0.7, "fortune": "uniform", "min": 4, "max": 5, "needs": "iron", "rate": "dense"},
    {"name": "lapis_ore", "vanilla": "lapis_ore", "drop": "minecraft:lapis_lazuli", "smelt_result": "minecraft:lapis_lazuli", "smelt_xp": 0.2, "fortune": "uniform", "min": 4, "max": 9, "needs": "stone", "rate": "dense"},
    {"name": "copper_ore", "vanilla": "copper_ore", "drop": "minecraft:raw_copper", "smelt_result": "minecraft:copper_ingot", "smelt_xp": 0.7, "fortune": "uniform", "min": 2, "max": 5, "needs": "stone", "rate": "dense"},
]
FOSSIL_TEXTURE_SOURCE = {
    "ammonite_fossil": "ammonite",
    "shell_fossil": "shell",
    "rib_fossil": "rib",
    "skull_fossil": "skull",
    "bone_fossil": "bone",
}

LANG = {}

MOD_COMPAT_PREFIXES = ("worldofstone:quark_", "worldofstone:undergarden_",
                       "worldofstone:create_", "worldofstone:betterend_",
                       "worldofstone:galosphere_", "worldofstone:bwg_",
                       "worldofstone:twilightforest_", "worldofstone:aether_",
                       "worldofstone:blue_skies_", "worldofstone:spelunkery_",
                       "worldofstone:iceandfire_", "worldofstone:mysticalagriculture_",
                       "worldofstone:biomesoplenty_",
                       "worldofstone:forbidden_arcanus_", "worldofstone:alexscaves_",
                       "worldofstone:ars_nouveau_", "worldofstone:cataclysm_",
                       "worldofstone:twigs_", "worldofstone:architects_palette_",
                       "worldofstone:outer_end_", "worldofstone:botania_",
                       "worldofstone:ad_astra_", "worldofstone:deep_aether_",
                       "worldofstone:caverns_and_chasms_", "worldofstone:atmospheric_",
                       "worldofstone:endergetic_", "worldofstone:wilder_wilds_",
                       "worldofstone:regions_unexplored_", "worldofstone:born_in_chaos_v1_",
                       "worldofstone:naturalist_", "worldofstone:yungscavebiomes_",
                       "worldofstone:natures_spirit_", "worldofstone:netherexp_",
                       "worldofstone:deeperdarker_", "worldofstone:the_deep_void_",
                       "worldofstone:defiled_lands_preborn_")


def tag_entry(block_id):
    if isinstance(block_id, str) and block_id.startswith(MOD_COMPAT_PREFIXES):
        return {"id": block_id, "required": False}
    return block_id


def safe_tag_values(ids):
    return [tag_entry(i) for i in ids]


def write_json(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2)


def map_src_name(name):
    for new, old in VARIANT_TEXTURE_SOURCE.items():
        if name == new:
            return old
        if name.startswith(new + "_"):
            return old + name[len(new):]
    return name


def safe_copyfile(src, dst):
    if os.path.abspath(src) == os.path.abspath(dst):
        return
    shutil.copyfile(src, dst)


def copy_texture(src_name, dst_name, src_dir=None):
    actual_src_name = map_src_name(src_name)
    src = os.path.join(src_dir or SRC_TEX, actual_src_name + ".png")
    os.makedirs(DST_BLOCK_TEX, exist_ok=True)
    dst = os.path.join(DST_BLOCK_TEX, dst_name + ".png")
    if os.path.exists(src):
        safe_copyfile(src, dst)
        return True
    return False


def composite_overlay(base_name, overlay_path, dst_name, tint=None, base_path_override=None):
    if base_path_override is not None:
        base_path = base_path_override
    else:
        actual_base = map_src_name(base_name)
        base_path = os.path.join(SRC_TEX, actual_base + ".png")
        if not os.path.exists(base_path):
            base_path = os.path.join(SRC_TEX, base_name + ".png")
    if not os.path.exists(base_path) or not os.path.exists(overlay_path):
        return False
    base = Image.open(base_path).convert("RGBA")
    overlay = Image.open(overlay_path).convert("RGBA")
    if overlay.size != base.size:
        overlay = overlay.resize(base.size, Image.NEAREST)
    if tint is not None:
        pixels = overlay.load()
        for y in range(overlay.size[1]):
            for x in range(overlay.size[0]):
                r, g, b, a = pixels[x, y]
                pixels[x, y] = (
                    int(r * tint[0] / 255),
                    int(g * tint[1] / 255),
                    int(b * tint[2] / 255),
                    a
                )
    combined = Image.alpha_composite(base, overlay)
    os.makedirs(DST_BLOCK_TEX, exist_ok=True)
    combined.save(os.path.join(DST_BLOCK_TEX, dst_name + ".png"))
    return True


def generate_synthetic_grass_top(path):
    overlay_path = os.path.join(SRC_OVERLAY_DIR, "overgrown_stone_side_overlay.png")
    if not os.path.exists(overlay_path):
        return
    overlay = Image.open(overlay_path).convert("RGBA")
    op = overlay.load()
    img = Image.new("RGBA", (16, 16))
    np_pixels = img.load()
    for y in range(16):
        for x in range(16):
            sy = y % 5
            r, g, b, _ = op[x, sy]
            np_pixels[x, y] = (r, g, b, 255)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)


def generate_stone_block_texture(path, base_color, dark_spots=8, light_spots=6):
    import random
    random.seed(hash(path) & 0xFFFF)
    img = Image.new("RGBA", (16, 16))
    pixels = img.load()
    for y in range(16):
        for x in range(16):
            jitter = random.randint(-25, 20)
            r = max(0, min(255, base_color[0] + jitter))
            g = max(0, min(255, base_color[1] + jitter))
            b = max(0, min(255, base_color[2] + jitter))
            pixels[x, y] = (r, g, b, 255)
    for _ in range(dark_spots):
        cx = random.randint(0, 15)
        cy = random.randint(0, 15)
        size = random.choice([1, 2])
        shade = (max(0, base_color[0] - 50), max(0, base_color[1] - 50), max(0, base_color[2] - 50))
        for dx in range(-size, size + 1):
            for dy in range(-size, size + 1):
                if abs(dx) + abs(dy) <= size:
                    px = (cx + dx) % 16
                    py = (cy + dy) % 16
                    pixels[px, py] = (shade[0], shade[1], shade[2], 255)
    for _ in range(light_spots):
        cx = random.randint(0, 15)
        cy = random.randint(0, 15)
        shade = (min(255, base_color[0] + 30), min(255, base_color[1] + 30), min(255, base_color[2] + 30))
        pixels[cx, cy] = (shade[0], shade[1], shade[2], 255)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)


def generate_modern_mossy_overlay(path):
    import random
    random.seed(2024)
    img = Image.new("RGBA", (16, 16))
    pixels = img.load()
    for y in range(16):
        for x in range(16):
            pixels[x, y] = (0, 0, 0, 0)
    moss_colors = [(56, 99, 38), (66, 117, 45), (77, 130, 50), (45, 88, 32), (88, 140, 56)]
    cluster_count = 14
    for _ in range(cluster_count):
        cx = random.randint(0, 15)
        cy = random.randint(0, 15)
        radius = random.choice([1, 1, 2, 2, 3])
        density = random.uniform(0.55, 0.85)
        for dx in range(-radius, radius + 1):
            for dy in range(-radius, radius + 1):
                if dx * dx + dy * dy > radius * radius:
                    continue
                if random.random() > density:
                    continue
                px = (cx + dx) % 16
                py = (cy + dy) % 16
                color = random.choice(moss_colors)
                alpha = random.randint(180, 240)
                pixels[px, py] = (color[0], color[1], color[2], alpha)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)


def fetch_vanilla(name):
    os.makedirs(VANILLA_CACHE, exist_ok=True)
    cached = os.path.join(VANILLA_CACHE, name + ".png")
    if os.path.exists(cached):
        return cached
    url = f"{VANILLA_BASE_URL}/{name}.png"
    try:
        with urllib.request.urlopen(url, timeout=15) as resp:
            data = resp.read()
        with open(cached, "wb") as f:
            f.write(data)
        return cached
    except Exception as e:
        print(f"  warn: failed to fetch {url}: {e}")
        return None


def make_variant_ore_texture(variant, vanilla_ore_name, dst_name, host_texture_path=None):
    variant_stone_path = host_texture_path if host_texture_path else os.path.join(DST_BLOCK_TEX, variant + ".png")
    vanilla_ore_path = fetch_vanilla(vanilla_ore_name)
    vanilla_stone_path = fetch_vanilla("stone")
    if not (os.path.exists(variant_stone_path) and vanilla_ore_path and vanilla_stone_path):
        return False
    variant_stone = Image.open(variant_stone_path).convert("RGBA")
    vanilla_ore = Image.open(vanilla_ore_path).convert("RGBA")
    vanilla_stone = Image.open(vanilla_stone_path).convert("RGBA")
    if variant_stone.size != vanilla_ore.size:
        variant_stone = variant_stone.resize(vanilla_ore.size, Image.NEAREST)
    if vanilla_stone.size != vanilla_ore.size:
        vanilla_stone = vanilla_stone.resize(vanilla_ore.size, Image.NEAREST)

    out = Image.new("RGBA", vanilla_ore.size)
    vp = variant_stone.load()
    op = vanilla_ore.load()
    sp = vanilla_stone.load()
    out_pixels = out.load()

    total_b = 0
    count_b = 0
    for sy in range(vanilla_stone.size[1]):
        for sx in range(vanilla_stone.size[0]):
            sr, sg, sb, _ = sp[sx, sy]
            total_b += (sr + sg + sb) // 3
            count_b += 1
    vstone_avg = max(1, total_b // count_b)

    for y in range(vanilla_ore.size[1]):
        for x in range(vanilla_ore.size[0]):
            ore_r, ore_g, ore_b, _ = op[x, y]
            vr, vg, vb, _ = vp[x, y]

            max_c = max(ore_r, ore_g, ore_b)
            min_c = min(ore_r, ore_g, ore_b)
            saturation = max_c - min_c
            ore_brightness = (ore_r + ore_g + ore_b) // 3

            sat_alpha = max(0.0, min(1.0, saturation / 25.0))
            coal_alpha = max(0.0, min(1.0, (vstone_avg - ore_brightness - 27) / 80.0))
            alpha = max(sat_alpha, coal_alpha)

            out_r = int(alpha * ore_r + (1 - alpha) * vr)
            out_g = int(alpha * ore_g + (1 - alpha) * vg)
            out_b = int(alpha * ore_b + (1 - alpha) * vb)

            out_pixels[x, y] = (
                max(0, min(255, out_r)),
                max(0, min(255, out_g)),
                max(0, min(255, out_b)),
                255
            )

    out.save(os.path.join(DST_BLOCK_TEX, dst_name + ".png"))
    return True


def ore_loot(name, ore):
    silk_branch = {
        "type": "minecraft:item",
        "conditions": [
            {
                "condition": "minecraft:match_tool",
                "predicate": {
                    "enchantments": [
                        {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                    ]
                }
            }
        ],
        "name": f"worldofstone:{name}"
    }
    if ore["fortune"] == "ore_drops":
        drop_branch = {
            "type": "minecraft:item",
            "name": ore["drop"],
            "functions": [
                {"function": "minecraft:apply_bonus", "enchantment": "minecraft:fortune", "formula": "minecraft:ore_drops"},
                {"function": "minecraft:explosion_decay"}
            ]
        }
    else:
        drop_branch = {
            "type": "minecraft:item",
            "name": ore["drop"],
            "functions": [
                {"function": "minecraft:set_count", "count": {"type": "minecraft:uniform", "min": ore["min"], "max": ore["max"]}},
                {"function": "minecraft:apply_bonus", "enchantment": "minecraft:fortune", "formula": "minecraft:uniform_bonus_count", "parameters": {"bonusMultiplier": 1}},
                {"function": "minecraft:explosion_decay"}
            ]
        }
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {"type": "minecraft:alternatives", "children": [silk_branch, drop_branch]}
                ],
                "conditions": [{"condition": "minecraft:survives_explosion"}]
            }
        ]
    }


NEEDS_STONE_TOOL = []
NEEDS_IRON_TOOL = []
NEEDS_DIAMOND_TOOL = []


def write_ore_block(name, variant, ore, host_texture_path=None, tier_override=None):
    make_variant_ore_texture(variant, ore["vanilla"], name, host_texture_path=host_texture_path)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), cube_all_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), cube_all_model(name))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), ore_loot(name, ore))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    needs = tier_override if tier_override else ore["needs"]
    if needs == "stone":
        NEEDS_STONE_TOOL.append(name)
    elif needs == "iron":
        NEEDS_IRON_TOOL.append(name)
    elif needs == "diamond":
        NEEDS_DIAMOND_TOOL.append(name)
    ore_type = ore["name"].replace("_ore", "")
    ORE_BLOCKS_BY_TYPE.setdefault(ore_type, []).append(name)
    if "smelt_result" in ore:
        ore_id = f"worldofstone:{name}"
        xp = ore.get("smelt_xp", 0.1)
        write_recipe(f"{name}_from_smelting",
                     smelting_recipe(ore["smelt_result"], ore_id, xp, 200))
        write_recipe(f"{name}_from_blasting",
                     blasting_recipe(ore["smelt_result"], ore_id, xp, 100))


def extract_moss_overlay(dst_path):
    return extract_moss_diff("cobblestone", "mossy_cobblestone", dst_path)


def extract_moss_diff(base_name, mossy_name, dst_path):
    base_path = fetch_vanilla(base_name)
    mossy_path = fetch_vanilla(mossy_name)
    if not base_path or not mossy_path:
        return False
    base = Image.open(base_path).convert("RGBA")
    mossy = Image.open(mossy_path).convert("RGBA")
    if base.size != mossy.size:
        return False
    out = Image.new("RGBA", mossy.size)
    bp = base.load()
    mp = mossy.load()
    op = out.load()
    for y in range(mossy.size[1]):
        for x in range(mossy.size[0]):
            cr, cg, cb, _ = bp[x, y]
            mr, mg, mb, _ = mp[x, y]
            diff = abs(cr - mr) + abs(cg - mg) + abs(cb - mb)
            green_dominance = mg - max(mr, mb)
            if diff > 25 and green_dominance > 5:
                op[x, y] = (mr, mg, mb, 255)
            else:
                op[x, y] = (0, 0, 0, 0)
    os.makedirs(os.path.dirname(dst_path), exist_ok=True)
    out.save(dst_path)
    return True


def tint_vanilla_texture(vanilla_path, target_avg, dst_path):
    if not vanilla_path or not os.path.exists(vanilla_path):
        return False
    vanilla = Image.open(vanilla_path).convert("RGBA")
    vanilla_avg = average_color_of(vanilla_path)
    if vanilla_avg == (255, 255, 255):
        return False
    tint_r = target_avg[0] / max(1, vanilla_avg[0])
    tint_g = target_avg[1] / max(1, vanilla_avg[1])
    tint_b = target_avg[2] / max(1, vanilla_avg[2])
    out = Image.new("RGBA", vanilla.size)
    pin = vanilla.load()
    pout = out.load()
    for y in range(vanilla.size[1]):
        for x in range(vanilla.size[0]):
            r, g, b, a = pin[x, y]
            r2 = max(0, min(255, int(r * tint_r)))
            g2 = max(0, min(255, int(g * tint_g)))
            b2 = max(0, min(255, int(b * tint_b)))
            pout[x, y] = (r2, g2, b2, a)
    os.makedirs(os.path.dirname(dst_path), exist_ok=True)
    out.save(dst_path)
    return True


def modernize_from_ub(ub_src_name, vanilla_name, dst_name):
    ub_src_path = os.path.join(SRC_TEX, map_src_name(ub_src_name) + ".png")
    if not os.path.exists(ub_src_path):
        return False
    target_avg = average_color_of(ub_src_path)
    vanilla_path = fetch_vanilla(vanilla_name)
    if not vanilla_path:
        safe_copyfile(ub_src_path, os.path.join(DST_BLOCK_TEX, dst_name + ".png"))
        return False
    return tint_vanilla_texture(vanilla_path, target_avg,
                                 os.path.join(DST_BLOCK_TEX, dst_name + ".png"))


def write_infested_block(name, host_name, host_display, haunting=True):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"),
               {"variants": {"": {"model": f"worldofstone:block/{host_name}"}}})
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{host_name}"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"),
               {
                   "type": "minecraft:block",
                   "pools": [{
                       "rolls": 1.0,
                       "bonus_rolls": 0.0,
                       "entries": [{
                           "type": "minecraft:item",
                           "name": f"worldofstone:{host_name}",
                           "conditions": [{
                               "condition": "minecraft:match_tool",
                               "predicate": {"enchantments": [{"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}]}
                           }]
                       }]
                   }]
               })
    LANG[f"block.worldofstone.{name}"] = "Infested " + host_display
    PICKAXE_BLOCKS.append(name)
    if haunting:
        write_recipe(f"{name}_from_haunting", haunting_recipe(f"worldofstone:{name}", f"worldofstone:{host_name}"))


def write_cracked_brick_texture(variant):
    base_vanilla = fetch_vanilla("stone_bricks")
    cracked_vanilla = fetch_vanilla("cracked_stone_bricks")
    variant_brick = os.path.join(DST_BLOCK_TEX, variant + "_bricks.png")
    dst = os.path.join(DST_BLOCK_TEX, "cracked_" + variant + "_bricks.png")
    if not base_vanilla or not cracked_vanilla or not os.path.exists(variant_brick):
        return False
    base = Image.open(base_vanilla).convert("RGBA")
    cracked = Image.open(cracked_vanilla).convert("RGBA")
    variant_img = Image.open(variant_brick).convert("RGBA")
    if base.size != cracked.size:
        return False
    if variant_img.size != base.size:
        variant_img = variant_img.resize(base.size, Image.NEAREST)
    out = Image.new("RGBA", variant_img.size)
    bp = base.load()
    cp = cracked.load()
    vp = variant_img.load()
    op = out.load()
    for y in range(variant_img.size[1]):
        for x in range(variant_img.size[0]):
            br, bg, bb, _ = bp[x, y]
            cr, cg, cb, _ = cp[x, y]
            vr, vg, vb, va = vp[x, y]
            base_lum = max(1, (br + bg + bb) / 3)
            cracked_lum = (cr + cg + cb) / 3
            ratio = cracked_lum / base_lum
            if ratio < 0.85:
                op[x, y] = (
                    max(0, min(255, int(vr * ratio))),
                    max(0, min(255, int(vg * ratio))),
                    max(0, min(255, int(vb * ratio))),
                    va,
                )
            else:
                op[x, y] = (vr, vg, vb, va)
    os.makedirs(os.path.dirname(dst), exist_ok=True)
    out.save(dst)
    return True


def composite_with_local_overlay(base_path, overlay_path, dst_path):
    if not os.path.exists(base_path) or not os.path.exists(overlay_path):
        return False
    base = Image.open(base_path).convert("RGBA")
    overlay = Image.open(overlay_path).convert("RGBA")
    if overlay.size != base.size:
        overlay = overlay.resize(base.size, Image.NEAREST)
    combined = Image.alpha_composite(base, overlay)
    os.makedirs(os.path.dirname(dst_path), exist_ok=True)
    combined.save(dst_path)
    return True


def recolor_with_variant_palette(vanilla_path, variant_stone_path, dst_path, softness=0.0):
    if not vanilla_path or not os.path.exists(vanilla_path) or not os.path.exists(variant_stone_path):
        return False
    variant_avg = average_color_of(variant_stone_path)
    vanilla_avg = average_color_of(vanilla_path)
    if vanilla_avg == (255, 255, 255):
        return False
    vanilla_lum = 0.299 * vanilla_avg[0] + 0.587 * vanilla_avg[1] + 0.114 * vanilla_avg[2]
    if vanilla_lum < 1:
        return False
    contrast = max(0.0, 1.0 - softness)
    vanilla = Image.open(vanilla_path).convert("RGBA")
    pin = vanilla.load()
    out = Image.new("RGBA", vanilla.size)
    pout = out.load()
    for y in range(vanilla.size[1]):
        for x in range(vanilla.size[0]):
            r, g, b, a = pin[x, y]
            if a == 0:
                pout[x, y] = (0, 0, 0, 0)
                continue
            pixel_lum = 0.299 * r + 0.587 * g + 0.114 * b
            ratio = pixel_lum / vanilla_lum
            if contrast < 1.0:
                ratio = 1.0 + (ratio - 1.0) * contrast
            pout[x, y] = (
                max(0, min(255, int(variant_avg[0] * ratio))),
                max(0, min(255, int(variant_avg[1] * ratio))),
                max(0, min(255, int(variant_avg[2] * ratio))),
                a,
            )
    os.makedirs(os.path.dirname(dst_path), exist_ok=True)
    out.save(dst_path)
    return True


def make_smooth_stone_texture(variant, dst_name):
    variant_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    vanilla_smooth = fetch_vanilla("smooth_stone")
    dst_path = os.path.join(DST_BLOCK_TEX, dst_name + ".png")
    if recolor_with_variant_palette(vanilla_smooth, variant_path, dst_path):
        return True
    if os.path.exists(variant_path):
        from PIL import ImageFilter
        img = Image.open(variant_path).convert("RGBA")
        img.filter(ImageFilter.GaussianBlur(radius=1.8)).save(dst_path)
        return True
    return False


def average_color_of(path):
    if not os.path.exists(path):
        return (255, 255, 255)
    img = Image.open(path).convert("RGBA")
    pixels = img.load()
    r_sum = g_sum = b_sum = count = 0
    for y in range(img.size[1]):
        for x in range(img.size[0]):
            r, g, b, a = pixels[x, y]
            if a == 0:
                continue
            r_sum += r
            g_sum += g
            b_sum += b
            count += 1
    if count == 0:
        return (255, 255, 255)
    return (r_sum // count, g_sum // count, b_sum // count)


SAND_TINTS = {}


def generate_synthetic_snow_top(path):
    import random
    random.seed(7)
    img = Image.new("RGBA", (16, 16))
    pixels = img.load()
    for y in range(16):
        for x in range(16):
            jitter = random.randint(-8, 0)
            v = 250 + jitter
            pixels[x, y] = (v, v, v, 255)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)


def cube_all_blockstate(name):
    return {"variants": {"": {"model": f"worldofstone:block/{name}"}}}


def y_rotated_blockstate(name):
    model = f"worldofstone:block/{name}"
    return {"variants": {"": [{"model": model}] + [{"model": model, "y": y} for y in (90, 180, 270)]}}


def cube_all_blockstate_rot(name):
    model = f"worldofstone:block/{name}"
    variants = []
    for x in (0, 90, 180, 270):
        for y in (0, 90, 180, 270):
            entry = {"model": model}
            if x:
                entry["x"] = x
            if y:
                entry["y"] = y
            variants.append(entry)
    return {"variants": {"": variants}}


def cube_all_model(texture_name):
    return {
        "parent": "minecraft:block/cube_all",
        "textures": {"all": f"worldofstone:block/{texture_name}"}
    }


def cube_bottom_top_model(side, top, bottom):
    return {
        "parent": "minecraft:block/cube_bottom_top",
        "textures": {
            "side": f"worldofstone:block/{side}",
            "top": f"worldofstone:block/{top}",
            "bottom": f"worldofstone:block/{bottom}"
        }
    }


def overgrown_model_with_tint(stone_tex):
    return {
        "parent": "minecraft:block/block",
        "textures": {
            "particle": f"worldofstone:block/{stone_tex}",
            "stone": f"worldofstone:block/{stone_tex}",
            "grass_top": "minecraft:block/grass_block_top",
            "grass_overlay": "worldofstone:block/ub_grass_side_overlay"
        },
        "elements": [
            {
                "from": [0, 0, 0],
                "to": [16, 16, 16],
                "faces": {
                    "down": {"uv": [0, 0, 16, 16], "texture": "#stone", "cullface": "down"},
                    "up": {"uv": [0, 0, 16, 16], "texture": "#grass_top", "cullface": "up", "tintindex": 0},
                    "north": {"uv": [0, 0, 16, 16], "texture": "#stone", "cullface": "north"},
                    "south": {"uv": [0, 0, 16, 16], "texture": "#stone", "cullface": "south"},
                    "west": {"uv": [0, 0, 16, 16], "texture": "#stone", "cullface": "west"},
                    "east": {"uv": [0, 0, 16, 16], "texture": "#stone", "cullface": "east"}
                }
            },
            {
                "from": [0, 0, 0],
                "to": [16, 16, 16],
                "faces": {
                    "north": {"uv": [0, 0, 16, 16], "texture": "#grass_overlay", "cullface": "north", "tintindex": 0},
                    "south": {"uv": [0, 0, 16, 16], "texture": "#grass_overlay", "cullface": "south", "tintindex": 0},
                    "west": {"uv": [0, 0, 16, 16], "texture": "#grass_overlay", "cullface": "west", "tintindex": 0},
                    "east": {"uv": [0, 0, 16, 16], "texture": "#grass_overlay", "cullface": "east", "tintindex": 0}
                }
            }
        ]
    }


def overgrown_snowed_model(stone_tex, snowed_side_tex):
    return {
        "parent": "minecraft:block/block",
        "textures": {
            "particle": f"worldofstone:block/{stone_tex}",
            "stone": f"worldofstone:block/{stone_tex}",
            "snow": "minecraft:block/snow",
            "snow_side": f"worldofstone:block/{snowed_side_tex}"
        },
        "elements": [
            {
                "from": [0, 0, 0],
                "to": [16, 16, 16],
                "faces": {
                    "down": {"uv": [0, 0, 16, 16], "texture": "#stone", "cullface": "down"},
                    "up": {"uv": [0, 0, 16, 16], "texture": "#snow", "cullface": "up"},
                    "north": {"uv": [0, 0, 16, 16], "texture": "#snow_side", "cullface": "north"},
                    "south": {"uv": [0, 0, 16, 16], "texture": "#snow_side", "cullface": "south"},
                    "west": {"uv": [0, 0, 16, 16], "texture": "#snow_side", "cullface": "west"},
                    "east": {"uv": [0, 0, 16, 16], "texture": "#snow_side", "cullface": "east"}
                }
            }
        ]
    }


def speleothem_model(texture_ref, from_xz, to_xz):
    return {
        "parent": "minecraft:block/block",
        "textures": {
            "particle": texture_ref,
            "all": texture_ref
        },
        "elements": [
            {
                "from": [from_xz, 0, from_xz],
                "to": [to_xz, 16, to_xz],
                "faces": {
                    "down": {"texture": "#all", "uv": [from_xz, from_xz, to_xz, to_xz], "tintindex": 0},
                    "up": {"texture": "#all", "uv": [from_xz, from_xz, to_xz, to_xz], "tintindex": 0},
                    "north": {"texture": "#all", "uv": [from_xz, 0, to_xz, 16], "tintindex": 0},
                    "south": {"texture": "#all", "uv": [from_xz, 0, to_xz, 16], "tintindex": 0},
                    "east": {"texture": "#all", "uv": [from_xz, 0, to_xz, 16], "tintindex": 0},
                    "west": {"texture": "#all", "uv": [from_xz, 0, to_xz, 16], "tintindex": 0}
                }
            }
        ]
    }


def speleothem_blockstate(name):
    small = f"worldofstone:block/{name}_small"
    medium = f"worldofstone:block/{name}_medium"
    large = f"worldofstone:block/{name}_large"
    return {
        "variants": {
            "size=small,tip_direction=up": {"model": small},
            "size=medium,tip_direction=up": {"model": medium},
            "size=large,tip_direction=up": {"model": large},
            "size=small,tip_direction=down": {"model": small, "x": 180},
            "size=medium,tip_direction=down": {"model": medium, "x": 180},
            "size=large,tip_direction=down": {"model": large, "x": 180}
        }
    }


def button_blockstate(name):
    base = f"worldofstone:block/{name}"
    pressed = f"worldofstone:block/{name}_pressed"
    return {
        "variants": {
            "face=floor,facing=east,powered=false": {"model": base, "y": 90},
            "face=floor,facing=west,powered=false": {"model": base, "y": 270},
            "face=floor,facing=south,powered=false": {"model": base, "y": 180},
            "face=floor,facing=north,powered=false": {"model": base},
            "face=floor,facing=east,powered=true": {"model": pressed, "y": 90},
            "face=floor,facing=west,powered=true": {"model": pressed, "y": 270},
            "face=floor,facing=south,powered=true": {"model": pressed, "y": 180},
            "face=floor,facing=north,powered=true": {"model": pressed},
            "face=wall,facing=east,powered=false": {"model": base, "uvlock": True, "x": 90, "y": 90},
            "face=wall,facing=west,powered=false": {"model": base, "uvlock": True, "x": 90, "y": 270},
            "face=wall,facing=south,powered=false": {"model": base, "uvlock": True, "x": 90, "y": 180},
            "face=wall,facing=north,powered=false": {"model": base, "uvlock": True, "x": 90},
            "face=wall,facing=east,powered=true": {"model": pressed, "uvlock": True, "x": 90, "y": 90},
            "face=wall,facing=west,powered=true": {"model": pressed, "uvlock": True, "x": 90, "y": 270},
            "face=wall,facing=south,powered=true": {"model": pressed, "uvlock": True, "x": 90, "y": 180},
            "face=wall,facing=north,powered=true": {"model": pressed, "uvlock": True, "x": 90},
            "face=ceiling,facing=east,powered=false": {"model": base, "x": 180, "y": 270},
            "face=ceiling,facing=west,powered=false": {"model": base, "x": 180, "y": 90},
            "face=ceiling,facing=south,powered=false": {"model": base, "x": 180},
            "face=ceiling,facing=north,powered=false": {"model": base, "x": 180, "y": 180},
            "face=ceiling,facing=east,powered=true": {"model": pressed, "x": 180, "y": 270},
            "face=ceiling,facing=west,powered=true": {"model": pressed, "x": 180, "y": 90},
            "face=ceiling,facing=south,powered=true": {"model": pressed, "x": 180},
            "face=ceiling,facing=north,powered=true": {"model": pressed, "x": 180, "y": 180}
        }
    }


def button_models(texture):
    base = {"textures": {"texture": f"worldofstone:block/{texture}"}}
    return (
        {"parent": "minecraft:block/button", **base},
        {"parent": "minecraft:block/button_pressed", **base},
        {"parent": "minecraft:block/button_inventory", **base},
    )


def item_model_block(name):
    return {"parent": f"worldofstone:block/{name}"}


def simple_loot(name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {"type": "minecraft:item", "name": f"worldofstone:{name}"}
                ],
                "conditions": [
                    {"condition": "minecraft:survives_explosion"}
                ]
            }
        ]
    }


def clay_loot(name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {
                        "type": "minecraft:alternatives",
                        "children": [
                            {
                                "type": "minecraft:item",
                                "conditions": [
                                    {
                                        "condition": "minecraft:match_tool",
                                        "predicate": {
                                            "enchantments": [
                                                {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                                            ]
                                        }
                                    }
                                ],
                                "name": f"worldofstone:{name}"
                            },
                            {
                                "type": "minecraft:item",
                                "name": "minecraft:clay_ball",
                                "functions": [
                                    {"function": "minecraft:set_count", "count": 4.0, "add": False},
                                    {"function": "minecraft:explosion_decay"}
                                ]
                            }
                        ]
                    }
                ],
                "conditions": [{"condition": "minecraft:survives_explosion"}]
            }
        ]
    }


def shaped_recipe(result, count, pattern, key_item):
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": pattern,
        "key": {"#": {"item": key_item}},
        "result": {"item": result, "count": count}
    }


def shapeless_recipe(result, count, ingredients):
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [{"item": i} for i in ingredients],
        "result": {"item": result, "count": count}
    }


def stairs_craft(result, ing):
    return shaped_recipe(result, 4, ["#  ", "## ", "###"], ing)


def slab_craft(result, ing):
    return shaped_recipe(result, 6, ["###"], ing)


def wall_craft(result, ing):
    return shaped_recipe(result, 6, ["###", "###"], ing)


def button_craft(result, ing):
    return shapeless_recipe(result, 1, [ing])


def bricks_craft(result, ing):
    return shaped_recipe(result, 4, ["##", "##"], ing)


def chiseled_from_slab(result, slab):
    return shaped_recipe(result, 1, ["#", "#"], slab)


def stonecutter_recipe(result, ing, count=1):
    return {
        "type": "minecraft:stonecutting",
        "ingredient": {"item": ing},
        "result": result,
        "count": count
    }


def smelting_recipe(result, ing, exp=0.1, time=200):
    return {
        "type": "minecraft:smelting",
        "ingredient": {"item": ing},
        "result": result,
        "experience": exp,
        "cookingtime": time
    }


def blasting_recipe(result, ing, exp=0.1, time=100):
    return {
        "type": "minecraft:blasting",
        "ingredient": {"item": ing},
        "result": result,
        "experience": exp,
        "cookingtime": time
    }


def haunting_recipe(result, ing):
    return {
        "type": "create:haunting",
        "conditions": [{"type": "forge:mod_loaded", "modid": "create"}],
        "ingredients": [{"item": ing}],
        "results": [{"item": result, "count": 1}]
    }


def write_recipe(name, recipe):
    write_json(os.path.join(DST_DATA, "recipes", f"{name}.json"), recipe)


def write_stone_variant_recipes():
    cobble_brick_variants = IGNEOUS + METAMORPHIC + SEDIMENTARY

    for v in cobble_brick_variants:
        ns = "worldofstone:"
        stone = ns + v
        cobble = ns + v + "_cobblestone"
        cobble_stairs = ns + v + "_cobblestone_stairs"
        cobble_slab = ns + v + "_cobblestone_slab"
        cobble_wall = ns + v + "_cobblestone_wall"
        m_cobble = ns + "mossy_" + v + "_cobblestone"
        m_cobble_stairs = ns + "mossy_" + v + "_cobblestone_stairs"
        m_cobble_slab = ns + "mossy_" + v + "_cobblestone_slab"
        m_cobble_wall = ns + "mossy_" + v + "_cobblestone_wall"
        bricks = ns + v + "_bricks"
        brick_stairs = ns + v + "_brick_stairs"
        brick_slab = ns + v + "_brick_slab"
        brick_wall = ns + v + "_brick_wall"
        m_bricks = ns + "mossy_" + v + "_bricks"
        m_brick_stairs = ns + "mossy_" + v + "_brick_stairs"
        m_brick_slab = ns + "mossy_" + v + "_brick_slab"
        m_brick_wall = ns + "mossy_" + v + "_brick_wall"

        write_recipe(f"{v}_from_smelting", smelting_recipe(stone, cobble))
        write_recipe(f"{v}_bricks_from_crafting", bricks_craft(bricks, stone))
        write_recipe(f"{v}_cobblestone_stairs_from_crafting", stairs_craft(cobble_stairs, cobble))
        write_recipe(f"{v}_cobblestone_slab_from_crafting", slab_craft(cobble_slab, cobble))
        write_recipe(f"{v}_cobblestone_wall_from_crafting", wall_craft(cobble_wall, cobble))
        write_recipe(f"mossy_{v}_cobblestone_from_vine", shapeless_recipe(m_cobble, 1, [cobble, "minecraft:vine"]))
        write_recipe(f"mossy_{v}_cobblestone_from_moss_block", shapeless_recipe(m_cobble, 1, [cobble, "minecraft:moss_block"]))
        write_recipe(f"mossy_{v}_cobblestone_stairs_from_crafting", stairs_craft(m_cobble_stairs, m_cobble))
        write_recipe(f"mossy_{v}_cobblestone_slab_from_crafting", slab_craft(m_cobble_slab, m_cobble))
        write_recipe(f"mossy_{v}_cobblestone_wall_from_crafting", wall_craft(m_cobble_wall, m_cobble))
        write_recipe(f"{v}_brick_stairs_from_crafting", stairs_craft(brick_stairs, bricks))
        write_recipe(f"{v}_brick_slab_from_crafting", slab_craft(brick_slab, bricks))
        write_recipe(f"{v}_brick_wall_from_crafting", wall_craft(brick_wall, bricks))
        write_recipe(f"mossy_{v}_bricks_from_vine", shapeless_recipe(m_bricks, 1, [bricks, "minecraft:vine"]))
        write_recipe(f"mossy_{v}_bricks_from_moss_block", shapeless_recipe(m_bricks, 1, [bricks, "minecraft:moss_block"]))
        write_recipe(f"mossy_{v}_brick_stairs_from_crafting", stairs_craft(m_brick_stairs, m_bricks))
        write_recipe(f"mossy_{v}_brick_slab_from_crafting", slab_craft(m_brick_slab, m_bricks))
        write_recipe(f"mossy_{v}_brick_wall_from_crafting", wall_craft(m_brick_wall, m_bricks))

        write_recipe(f"{v}_bricks_from_stonecutting", stonecutter_recipe(bricks, stone))
        write_recipe(f"{v}_brick_stairs_from_stonecutting_stone", stonecutter_recipe(brick_stairs, stone))
        write_recipe(f"{v}_brick_slab_from_stonecutting_stone", stonecutter_recipe(brick_slab, stone, 2))
        write_recipe(f"{v}_brick_wall_from_stonecutting_stone", stonecutter_recipe(brick_wall, stone))
        write_recipe(f"{v}_cobblestone_stairs_from_stonecutting", stonecutter_recipe(cobble_stairs, cobble))
        write_recipe(f"{v}_cobblestone_slab_from_stonecutting", stonecutter_recipe(cobble_slab, cobble, 2))
        write_recipe(f"{v}_cobblestone_wall_from_stonecutting", stonecutter_recipe(cobble_wall, cobble))
        write_recipe(f"{v}_brick_stairs_from_stonecutting_bricks", stonecutter_recipe(brick_stairs, bricks))
        write_recipe(f"{v}_brick_slab_from_stonecutting_bricks", stonecutter_recipe(brick_slab, bricks, 2))
        write_recipe(f"{v}_brick_wall_from_stonecutting_bricks", stonecutter_recipe(brick_wall, bricks))
        write_recipe(f"mossy_{v}_cobblestone_stairs_from_stonecutting", stonecutter_recipe(m_cobble_stairs, m_cobble))
        write_recipe(f"mossy_{v}_cobblestone_slab_from_stonecutting", stonecutter_recipe(m_cobble_slab, m_cobble, 2))
        write_recipe(f"mossy_{v}_cobblestone_wall_from_stonecutting", stonecutter_recipe(m_cobble_wall, m_cobble))
        write_recipe(f"mossy_{v}_brick_stairs_from_stonecutting", stonecutter_recipe(m_brick_stairs, m_bricks))
        write_recipe(f"mossy_{v}_brick_slab_from_stonecutting", stonecutter_recipe(m_brick_slab, m_bricks, 2))
        write_recipe(f"mossy_{v}_brick_wall_from_stonecutting", stonecutter_recipe(m_brick_wall, m_bricks))

    for v in ALL_VARIANTS:
        ns = "worldofstone:"
        sand = ns + v + "_sand"
        sandstone = ns + v + "_sandstone"
        sandstone_stairs = ns + v + "_sandstone_stairs"
        sandstone_slab = ns + v + "_sandstone_slab"
        sandstone_wall = ns + v + "_sandstone_wall"
        chiseled = ns + "chiseled_" + v + "_sandstone"
        cut = ns + "cut_" + v + "_sandstone"
        cut_slab = ns + "cut_" + v + "_sandstone_slab"
        smooth = ns + "smooth_" + v + "_sandstone"
        smooth_stairs = ns + "smooth_" + v + "_sandstone_stairs"
        smooth_slab = ns + "smooth_" + v + "_sandstone_slab"

        write_recipe(f"{v}_sandstone_from_crafting", shaped_recipe(sandstone, 1, ["##", "##"], sand))
        write_recipe(f"{v}_sandstone_stairs_from_crafting", stairs_craft(sandstone_stairs, sandstone))
        write_recipe(f"{v}_sandstone_slab_from_crafting", slab_craft(sandstone_slab, sandstone))
        write_recipe(f"{v}_sandstone_wall_from_crafting", wall_craft(sandstone_wall, sandstone))
        write_recipe(f"chiseled_{v}_sandstone_from_crafting", chiseled_from_slab(chiseled, sandstone_slab))
        write_recipe(f"cut_{v}_sandstone_from_crafting", shaped_recipe(cut, 4, ["##", "##"], sandstone))
        write_recipe(f"cut_{v}_sandstone_slab_from_crafting", slab_craft(cut_slab, cut))
        write_recipe(f"smooth_{v}_sandstone_from_smelting", smelting_recipe(smooth, sandstone, 0.1, 200))
        write_recipe(f"smooth_{v}_sandstone_stairs_from_crafting", stairs_craft(smooth_stairs, smooth))
        write_recipe(f"smooth_{v}_sandstone_slab_from_crafting", slab_craft(smooth_slab, smooth))

        write_recipe(f"{v}_sandstone_stairs_from_stonecutting", stonecutter_recipe(sandstone_stairs, sandstone))
        write_recipe(f"{v}_sandstone_slab_from_stonecutting", stonecutter_recipe(sandstone_slab, sandstone, 2))
        write_recipe(f"{v}_sandstone_wall_from_stonecutting", stonecutter_recipe(sandstone_wall, sandstone))
        write_recipe(f"chiseled_{v}_sandstone_from_stonecutting", stonecutter_recipe(chiseled, sandstone))
        write_recipe(f"cut_{v}_sandstone_from_stonecutting", stonecutter_recipe(cut, sandstone))
        write_recipe(f"cut_{v}_sandstone_slab_from_stonecutting_sandstone", stonecutter_recipe(cut_slab, sandstone, 2))
        write_recipe(f"cut_{v}_sandstone_slab_from_stonecutting_cut", stonecutter_recipe(cut_slab, cut, 2))
        write_recipe(f"smooth_{v}_sandstone_stairs_from_stonecutting", stonecutter_recipe(smooth_stairs, smooth))
        write_recipe(f"smooth_{v}_sandstone_slab_from_stonecutting", stonecutter_recipe(smooth_slab, smooth, 2))


def cobble_drops_loot(stone_name, cobble_name, fossil_chance=None):
    pools = [
        {
            "rolls": 1.0,
            "bonus_rolls": 0.0,
            "entries": [
                {
                    "type": "minecraft:alternatives",
                    "children": [
                        {
                            "type": "minecraft:item",
                            "conditions": [
                                {
                                    "condition": "minecraft:match_tool",
                                    "predicate": {
                                        "enchantments": [
                                            {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                                        ]
                                    }
                                }
                            ],
                            "name": f"worldofstone:{stone_name}"
                        },
                        {"type": "minecraft:item", "name": f"worldofstone:{cobble_name}"}
                    ]
                }
            ],
            "conditions": [{"condition": "minecraft:survives_explosion"}]
        }
    ]
    if fossil_chance:
        pools.append({
            "rolls": 1.0,
            "bonus_rolls": 0.0,
            "conditions": [
                {"condition": "minecraft:random_chance", "chance": fossil_chance},
                {
                    "condition": "minecraft:inverted",
                    "term": {
                        "condition": "minecraft:match_tool",
                        "predicate": {
                            "enchantments": [
                                {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                            ]
                        }
                    }
                }
            ],
            "entries": [
                {"type": "minecraft:item", "weight": 3, "name": "worldofstone:bone_fossil"},
                {"type": "minecraft:item", "weight": 2, "name": "worldofstone:rib_fossil"},
                {"type": "minecraft:item", "weight": 2, "name": "worldofstone:shell_fossil"},
                {"type": "minecraft:item", "weight": 1, "name": "worldofstone:ammonite_fossil"},
                {"type": "minecraft:item", "weight": 1, "name": "worldofstone:skull_fossil"}
            ]
        })
    return {"type": "minecraft:block", "pools": pools}


def lignite_drops_loot(stone_name, drop_item):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {
                        "type": "minecraft:alternatives",
                        "children": [
                            {
                                "type": "minecraft:item",
                                "conditions": [
                                    {
                                        "condition": "minecraft:match_tool",
                                        "predicate": {
                                            "enchantments": [
                                                {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                                            ]
                                        }
                                    }
                                ],
                                "name": f"worldofstone:{stone_name}"
                            },
                            {"type": "minecraft:item", "name": drop_item}
                        ]
                    }
                ],
                "conditions": [{"condition": "minecraft:survives_explosion"}]
            }
        ]
    }


def fossil_chance_loot(stone_name, fossil_chance=0.05):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {"type": "minecraft:item", "name": f"worldofstone:{stone_name}"}
                ],
                "conditions": [{"condition": "minecraft:survives_explosion"}]
            },
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "conditions": [
                    {"condition": "minecraft:random_chance", "chance": fossil_chance},
                    {
                        "condition": "minecraft:inverted",
                        "term": {
                            "condition": "minecraft:match_tool",
                            "predicate": {
                                "enchantments": [
                                    {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                                ]
                            }
                        }
                    }
                ],
                "entries": [
                    {"type": "minecraft:item", "weight": 3, "name": "worldofstone:bone_fossil"},
                    {"type": "minecraft:item", "weight": 2, "name": "worldofstone:rib_fossil"},
                    {"type": "minecraft:item", "weight": 2, "name": "worldofstone:shell_fossil"},
                    {"type": "minecraft:item", "weight": 1, "name": "worldofstone:ammonite_fossil"},
                    {"type": "minecraft:item", "weight": 1, "name": "worldofstone:skull_fossil"}
                ]
            }
        ]
    }


def gravel_loot(name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {
                        "type": "minecraft:alternatives",
                        "children": [
                            {
                                "type": "minecraft:item",
                                "conditions": [
                                    {
                                        "condition": "minecraft:match_tool",
                                        "predicate": {
                                            "enchantments": [
                                                {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                                            ]
                                        }
                                    }
                                ],
                                "name": f"worldofstone:{name}"
                            },
                            {
                                "type": "minecraft:alternatives",
                                "children": [
                                    {
                                        "type": "minecraft:item",
                                        "conditions": [
                                            {
                                                "condition": "minecraft:table_bonus",
                                                "enchantment": "minecraft:fortune",
                                                "chances": [0.1, 0.14285715, 0.25, 1.0]
                                            }
                                        ],
                                        "name": "minecraft:flint"
                                    },
                                    {
                                        "type": "minecraft:item",
                                        "name": f"worldofstone:{name}"
                                    }
                                ]
                            }
                        ]
                    }
                ],
                "conditions": [{"condition": "minecraft:survives_explosion"}]
            }
        ]
    }


def slab_loot(name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "functions": [
                            {
                                "function": "minecraft:set_count",
                                "conditions": [
                                    {
                                        "condition": "minecraft:block_state_property",
                                        "block": f"worldofstone:{name}",
                                        "properties": {"type": "double"}
                                    }
                                ],
                                "count": 2.0,
                                "add": False
                            },
                            {"function": "minecraft:explosion_decay"}
                        ],
                        "name": f"worldofstone:{name}"
                    }
                ]
            }
        ]
    }


def stairs_blockstate(parent_block_name):
    inner = f"worldofstone:block/{parent_block_name}_stairs_inner"
    outer = f"worldofstone:block/{parent_block_name}_stairs_outer"
    straight = f"worldofstone:block/{parent_block_name}_stairs"
    return {
        "variants": {
            "facing=east,half=bottom,shape=straight": {"model": straight},
            "facing=west,half=bottom,shape=straight": {"model": straight, "y": 180, "uvlock": True},
            "facing=south,half=bottom,shape=straight": {"model": straight, "y": 90, "uvlock": True},
            "facing=north,half=bottom,shape=straight": {"model": straight, "y": 270, "uvlock": True},
            "facing=east,half=bottom,shape=outer_right": {"model": outer},
            "facing=west,half=bottom,shape=outer_right": {"model": outer, "y": 180, "uvlock": True},
            "facing=south,half=bottom,shape=outer_right": {"model": outer, "y": 90, "uvlock": True},
            "facing=north,half=bottom,shape=outer_right": {"model": outer, "y": 270, "uvlock": True},
            "facing=east,half=bottom,shape=outer_left": {"model": outer, "y": 270, "uvlock": True},
            "facing=west,half=bottom,shape=outer_left": {"model": outer, "y": 90, "uvlock": True},
            "facing=south,half=bottom,shape=outer_left": {"model": outer},
            "facing=north,half=bottom,shape=outer_left": {"model": outer, "y": 180, "uvlock": True},
            "facing=east,half=bottom,shape=inner_right": {"model": inner},
            "facing=west,half=bottom,shape=inner_right": {"model": inner, "y": 180, "uvlock": True},
            "facing=south,half=bottom,shape=inner_right": {"model": inner, "y": 90, "uvlock": True},
            "facing=north,half=bottom,shape=inner_right": {"model": inner, "y": 270, "uvlock": True},
            "facing=east,half=bottom,shape=inner_left": {"model": inner, "y": 270, "uvlock": True},
            "facing=west,half=bottom,shape=inner_left": {"model": inner, "y": 90, "uvlock": True},
            "facing=south,half=bottom,shape=inner_left": {"model": inner},
            "facing=north,half=bottom,shape=inner_left": {"model": inner, "y": 180, "uvlock": True},
            "facing=east,half=top,shape=straight": {"model": straight, "x": 180, "uvlock": True},
            "facing=west,half=top,shape=straight": {"model": straight, "x": 180, "y": 180, "uvlock": True},
            "facing=south,half=top,shape=straight": {"model": straight, "x": 180, "y": 90, "uvlock": True},
            "facing=north,half=top,shape=straight": {"model": straight, "x": 180, "y": 270, "uvlock": True},
            "facing=east,half=top,shape=outer_right": {"model": outer, "x": 180, "y": 90, "uvlock": True},
            "facing=west,half=top,shape=outer_right": {"model": outer, "x": 180, "y": 270, "uvlock": True},
            "facing=south,half=top,shape=outer_right": {"model": outer, "x": 180, "y": 180, "uvlock": True},
            "facing=north,half=top,shape=outer_right": {"model": outer, "x": 180, "uvlock": True},
            "facing=east,half=top,shape=outer_left": {"model": outer, "x": 180, "uvlock": True},
            "facing=west,half=top,shape=outer_left": {"model": outer, "x": 180, "y": 180, "uvlock": True},
            "facing=south,half=top,shape=outer_left": {"model": outer, "x": 180, "y": 90, "uvlock": True},
            "facing=north,half=top,shape=outer_left": {"model": outer, "x": 180, "y": 270, "uvlock": True},
            "facing=east,half=top,shape=inner_right": {"model": inner, "x": 180, "y": 90, "uvlock": True},
            "facing=west,half=top,shape=inner_right": {"model": inner, "x": 180, "y": 270, "uvlock": True},
            "facing=south,half=top,shape=inner_right": {"model": inner, "x": 180, "y": 180, "uvlock": True},
            "facing=north,half=top,shape=inner_right": {"model": inner, "x": 180, "uvlock": True},
            "facing=east,half=top,shape=inner_left": {"model": inner, "x": 180, "uvlock": True},
            "facing=west,half=top,shape=inner_left": {"model": inner, "x": 180, "y": 180, "uvlock": True},
            "facing=south,half=top,shape=inner_left": {"model": inner, "x": 180, "y": 90, "uvlock": True},
            "facing=north,half=top,shape=inner_left": {"model": inner, "x": 180, "y": 270, "uvlock": True}
        }
    }


def stairs_models(texture_name):
    base = {"textures": {"bottom": f"worldofstone:block/{texture_name}",
                         "top": f"worldofstone:block/{texture_name}",
                         "side": f"worldofstone:block/{texture_name}"}}
    straight = {"parent": "minecraft:block/stairs", **base}
    inner = {"parent": "minecraft:block/inner_stairs", **base}
    outer = {"parent": "minecraft:block/outer_stairs", **base}
    return straight, inner, outer


def stairs_models_pillared(side_tex, top_tex, bottom_tex):
    base = {"textures": {"bottom": f"worldofstone:block/{bottom_tex}",
                         "top": f"worldofstone:block/{top_tex}",
                         "side": f"worldofstone:block/{side_tex}"}}
    straight = {"parent": "minecraft:block/stairs", **base}
    inner = {"parent": "minecraft:block/inner_stairs", **base}
    outer = {"parent": "minecraft:block/outer_stairs", **base}
    return straight, inner, outer


def slab_models_pillared(side_tex, top_tex, bottom_tex):
    base = {"textures": {"bottom": f"worldofstone:block/{bottom_tex}",
                         "top": f"worldofstone:block/{top_tex}",
                         "side": f"worldofstone:block/{side_tex}"}}
    bot = {"parent": "minecraft:block/slab", **base}
    top = {"parent": "minecraft:block/slab_top", **base}
    return bot, top


def slab_blockstate(name, base_block_texture):
    bot = f"worldofstone:block/{name}"
    top = f"worldofstone:block/{name}_top"
    full = f"worldofstone:block/{base_block_texture}"
    return {
        "variants": {
            "type=bottom": {"model": bot},
            "type=top": {"model": top},
            "type=double": {"model": full}
        }
    }


def slab_models(texture):
    base = {"textures": {"bottom": f"worldofstone:block/{texture}",
                         "top": f"worldofstone:block/{texture}",
                         "side": f"worldofstone:block/{texture}"}}
    bot = {"parent": "minecraft:block/slab", **base}
    top = {"parent": "minecraft:block/slab_top", **base}
    return bot, top


def wall_blockstate(name):
    post = f"worldofstone:block/{name}_post"
    side = f"worldofstone:block/{name}_side"
    side_tall = f"worldofstone:block/{name}_side_tall"
    return {
        "multipart": [
            {"when": {"up": "true"}, "apply": {"model": post}},
            {"when": {"north": "low"}, "apply": {"model": side, "uvlock": True}},
            {"when": {"east": "low"}, "apply": {"model": side, "y": 90, "uvlock": True}},
            {"when": {"south": "low"}, "apply": {"model": side, "y": 180, "uvlock": True}},
            {"when": {"west": "low"}, "apply": {"model": side, "y": 270, "uvlock": True}},
            {"when": {"north": "tall"}, "apply": {"model": side_tall, "uvlock": True}},
            {"when": {"east": "tall"}, "apply": {"model": side_tall, "y": 90, "uvlock": True}},
            {"when": {"south": "tall"}, "apply": {"model": side_tall, "y": 180, "uvlock": True}},
            {"when": {"west": "tall"}, "apply": {"model": side_tall, "y": 270, "uvlock": True}}
        ]
    }


def wall_models(texture):
    base = {"textures": {"wall": f"worldofstone:block/{texture}"}}
    post = {"parent": "minecraft:block/template_wall_post", **base}
    side = {"parent": "minecraft:block/template_wall_side", **base}
    side_tall = {"parent": "minecraft:block/template_wall_side_tall", **base}
    inv = {"parent": "minecraft:block/wall_inventory", **base}
    return post, side, side_tall, inv


def lang_for(name):
    return name.replace("_", " ").title()


def register_lang_block(name, display=None):
    LANG[f"block.worldofstone.{name}"] = display if display else lang_for(name)


def register_lang_item(name, display=None):
    LANG[f"item.worldofstone.{name}"] = display if display else lang_for(name)


def write_basic_block(name, texture_name, tool="pickaxe", random_rot=False):
    blockstate = cube_all_blockstate_rot(name) if random_rot else cube_all_blockstate(name)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), blockstate)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), cube_all_model(texture_name))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, tool)


def write_pillared_block(name, side_tex, top_tex, bottom_tex, tool="pickaxe"):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), cube_all_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"),
               cube_bottom_top_model(side_tex, top_tex, bottom_tex))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, tool)


def write_overgrown_block(name, stone_tex):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), y_rotated_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"),
               overgrown_model_with_tint(stone_tex))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")


def write_overgrown_snowed(name, stone_tex, snowed_side_tex):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), cube_all_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"),
               overgrown_snowed_model(stone_tex, snowed_side_tex))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")


def track_tool(name, tool):
    if tool == "pickaxe":
        PICKAXE_BLOCKS.append(name)
    elif tool == "shovel":
        SHOVEL_BLOCKS.append(name)
    elif tool == "axe":
        AXE_BLOCKS.append(name)
    elif tool == "hoe":
        HOE_BLOCKS.append(name)


def silk_only_loot(name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1.0,
                "bonus_rolls": 0.0,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "name": f"worldofstone:{name}",
                        "conditions": [
                            {
                                "condition": "minecraft:match_tool",
                                "predicate": {
                                    "enchantments": [
                                        {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}
                                    ]
                                }
                            }
                        ]
                    }
                ]
            }
        ]
    }


def write_speleothem_block(name, texture_ref, display=None, unbreakable=False, tier=None, tool="pickaxe", drop_self=False):
    SPELEOTHEM_BLOCKS.append(name)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), speleothem_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_small.json"), speleothem_model(texture_ref, 7, 9))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_medium.json"), speleothem_model(texture_ref, 6, 10))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_large.json"), speleothem_model(texture_ref, 4, 12))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{name}_medium"})
    if unbreakable:
        write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"),
                   {"type": "minecraft:block", "pools": []})
    else:
        write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), silk_only_loot(name))
        if tool is not None:
            track_tool(name, tool)
            if tier == "stone":
                NEEDS_STONE_TOOL.append(name)
            elif tier == "iron":
                NEEDS_IRON_TOOL.append(name)
            elif tier == "diamond":
                NEEDS_DIAMOND_TOOL.append(name)
    register_lang_block(name, display)


def write_button(name, texture):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), button_blockstate(name))
    base, pressed, inv = button_models(texture)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), base)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_pressed.json"), pressed)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_inventory.json"), inv)
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{name}_inventory"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    BUTTON_BLOCKS.append(name)


def write_stairs(name, texture, parent_for_blockstate_models):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), stairs_blockstate(parent_for_blockstate_models))
    s, i, o = stairs_models(texture)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{parent_for_blockstate_models}_stairs.json"), s)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{parent_for_blockstate_models}_stairs_inner.json"), i)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{parent_for_blockstate_models}_stairs_outer.json"), o)
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{parent_for_blockstate_models}_stairs"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    STAIR_BLOCKS.append(name)


def write_pillared_stairs(name, side_tex, top_tex, bottom_tex, model_base):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), stairs_blockstate(model_base))
    s, i, o = stairs_models_pillared(side_tex, top_tex, bottom_tex)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{model_base}_stairs.json"), s)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{model_base}_stairs_inner.json"), i)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{model_base}_stairs_outer.json"), o)
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{model_base}_stairs"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    STAIR_BLOCKS.append(name)


def write_pillared_slab(name, side_tex, top_tex, bottom_tex, base_full_block):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), slab_blockstate(name, base_full_block))
    bot, top = slab_models_pillared(side_tex, top_tex, bottom_tex)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), bot)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_top.json"), top)
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{name}"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), slab_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    SLAB_BLOCKS.append(name)


def write_slab(name, texture, base_full_block):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), slab_blockstate(name, base_full_block))
    bot, top = slab_models(texture)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), bot)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_top.json"), top)
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{name}"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), slab_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    SLAB_BLOCKS.append(name)


def write_wall(name, texture):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), wall_blockstate(name))
    post, side, tall, inv = wall_models(texture)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_post.json"), post)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_side.json"), side)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_side_tall.json"), tall)
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_inventory.json"), inv)
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"),
               {"parent": f"worldofstone:block/{name}_inventory"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")
    WALL_BLOCKS.append(name)


def write_simple_item(name, texture):
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"worldofstone:item/{texture}"}
    })
    register_lang_item(name)


def write_sandstone_set(variant):
    side = f"{variant}_sandstone_normal"
    top = f"{variant}_sandstone_top"
    bottom = f"{variant}_sandstone_bottom"
    chiseled_tex = f"chiseled_{variant}_sandstone"
    cut_tex = f"cut_{variant}_sandstone"
    variant_stone_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    sandstone_recolor_pairs = [
        ("sandstone", side),
        ("sandstone_top", top),
        ("sandstone_bottom", bottom),
        ("chiseled_sandstone", chiseled_tex),
        ("cut_sandstone", cut_tex),
    ]
    for vanilla_name, dst in sandstone_recolor_pairs:
        vanilla_path = fetch_vanilla(vanilla_name)
        dst_path = os.path.join(DST_BLOCK_TEX, dst + ".png")
        if not recolor_with_variant_palette(vanilla_path, variant_stone_path, dst_path):
            ub_src = {"sandstone": side.replace(f"{variant}_", f"{variant}_"),
                      "sandstone_top": f"{variant}_sandstone_top",
                      "sandstone_bottom": f"{variant}_sandstone_bottom",
                      "chiseled_sandstone": f"{variant}_sandstone_chiseled",
                      "cut_sandstone": f"{variant}_sandstone_smooth"}.get(vanilla_name)
            if ub_src:
                modernize_from_ub(ub_src, vanilla_name, dst)

    write_pillared_block(f"{variant}_sandstone", side, top, bottom)
    write_pillared_stairs(f"{variant}_sandstone_stairs", side, top, bottom, f"{variant}_sandstone")
    write_pillared_slab(f"{variant}_sandstone_slab", side, top, bottom, f"{variant}_sandstone")
    write_wall(f"{variant}_sandstone_wall", side)

    write_basic_block(f"chiseled_{variant}_sandstone", chiseled_tex)

    write_pillared_block(f"cut_{variant}_sandstone", cut_tex, top, bottom)
    write_pillared_slab(f"cut_{variant}_sandstone_slab", cut_tex, top, bottom, f"cut_{variant}_sandstone")

    write_basic_block(f"smooth_{variant}_sandstone", top)
    write_stairs(f"smooth_{variant}_sandstone_stairs", top, f"smooth_{variant}_sandstone")
    write_slab(f"smooth_{variant}_sandstone_slab", top, f"smooth_{variant}_sandstone")


VANILLA_SAND_AVG = (220, 208, 160)


def compute_tint(variant_avg):
    return (
        min(255, int(variant_avg[0] * 255 / VANILLA_SAND_AVG[0])),
        min(255, int(variant_avg[1] * 255 / VANILLA_SAND_AVG[1])),
        min(255, int(variant_avg[2] * 255 / VANILLA_SAND_AVG[2]))
    )


def write_sand(variant):
    name = f"{variant}_sand"
    variant_stone_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    vanilla_sand = fetch_vanilla("sand")
    dst_path = os.path.join(DST_BLOCK_TEX, name + ".png")
    if not recolor_with_variant_palette(vanilla_sand, variant_stone_path, dst_path):
        src_tex_path = os.path.join(SRC_TEX, map_src_name(name) + ".png")
        if os.path.exists(src_tex_path):
            safe_copyfile(src_tex_path, dst_path)
    SAND_BLOCKS.append(name)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), cube_all_blockstate_rot(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), cube_all_model(name))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "shovel")


def write_glass(variant):
    name = f"{variant}_glass"
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), cube_all_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"),
               {"parent": "minecraft:block/leaves", "textures": {"all": "minecraft:block/white_stained_glass"}})
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), silk_only_loot(name))
    write_recipe(f"{name}_from_smelting", smelting_recipe(f"worldofstone:{name}", f"worldofstone:{variant}_sand"))
    register_lang_block(name)
    pane = f"{name}_pane"
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{pane}.json"), glass_pane_blockstate())
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{pane}.json"),
               {"parent": "minecraft:item/generated", "textures": {"layer0": "minecraft:block/white_stained_glass"}})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{pane}.json"), silk_only_loot(pane))
    write_recipe(f"{pane}_from_crafting", shaped_recipe(f"worldofstone:{pane}", 16, ["###", "###"], f"worldofstone:{name}"))
    register_lang_block(pane)


def glass_pane_blockstate():
    parts = [{"apply": {"model": "worldofstone:block/strata_glass_pane_post"}}]
    for side, value, part, y in (("north", "true", "side", 0), ("east", "true", "side", 90),
                                 ("south", "true", "side_alt", 0), ("west", "true", "side_alt", 90),
                                 ("north", "false", "noside", 0), ("east", "false", "noside_alt", 0),
                                 ("south", "false", "noside_alt", 90), ("west", "false", "noside", 270)):
        apply = {"model": f"worldofstone:block/strata_glass_pane_{part}"}
        if y:
            apply["y"] = y
        parts.append({"apply": apply, "when": {side: value}})
    return {"multipart": parts}


def write_glass_pane_models():
    parts = {
        "post": ([7, 0, 7], [9, 16, 9], {"down": ("edge", [7, 7, 9, 9]), "up": ("edge", [7, 7, 9, 9])}),
        "side": ([7, 0, 0], [9, 16, 7], {"down": ("edge", [7, 0, 9, 7]), "up": ("edge", [7, 0, 9, 7]), "north": ("edge", [7, 0, 9, 16]),
                                         "west": ("pane", [16, 0, 9, 16]), "east": ("pane", [9, 0, 16, 16])}),
        "side_alt": ([7, 0, 9], [9, 16, 16], {"down": ("edge", [7, 0, 9, 7]), "up": ("edge", [7, 0, 9, 7]), "south": ("edge", [7, 0, 9, 16]),
                                              "west": ("pane", [7, 0, 0, 16]), "east": ("pane", [0, 0, 7, 16])}),
        "noside": ([7, 0, 7], [9, 16, 9], {"north": ("pane", [9, 0, 7, 16])}),
        "noside_alt": ([7, 0, 7], [9, 16, 9], {"east": ("pane", [7, 0, 9, 16])}),
    }
    for part, (frm, to, faces) in parts.items():
        out = {}
        for face, (tex, uv) in faces.items():
            out[face] = {"uv": uv, "texture": f"#{tex}", "tintindex": 0}
            if part.startswith("side") and face in ("north", "south"):
                out[face]["cullface"] = face
        write_json(os.path.join(DST_ASSETS, "models", "block", f"strata_glass_pane_{part}.json"), {
            "ambientocclusion": False,
            "textures": {"particle": "minecraft:block/white_stained_glass", "pane": "minecraft:block/white_stained_glass",
                         "edge": "minecraft:block/white_stained_glass_pane_top"},
            "elements": [{"from": frm, "to": to, "faces": out}]
        })


def write_gravel(variant):
    tex = f"{variant}_gravel"
    variant_stone_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    vanilla_gravel = fetch_vanilla("gravel")
    dst_path = os.path.join(DST_BLOCK_TEX, tex + ".png")
    if not recolor_with_variant_palette(vanilla_gravel, variant_stone_path, dst_path, softness=0.4):
        modernize_from_ub(tex, "gravel", tex)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{tex}.json"), cube_all_blockstate_rot(tex))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{tex}.json"), cube_all_model(tex))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{tex}.json"), item_model_block(tex))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{tex}.json"), gravel_loot(tex))
    register_lang_block(tex)
    track_tool(tex, "shovel")
    GRAVEL_BLOCKS.append(tex)


def write_clay(variant):
    tex = f"{variant}_clay"
    variant_stone_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    vanilla_clay = fetch_vanilla("clay")
    dst_path = os.path.join(DST_BLOCK_TEX, tex + ".png")
    if not recolor_with_variant_palette(vanilla_clay, variant_stone_path, dst_path):
        modernize_from_ub(tex, "clay", tex)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{tex}.json"), cube_all_blockstate(tex))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{tex}.json"), cube_all_model(tex))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{tex}.json"), item_model_block(tex))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{tex}.json"), clay_loot(tex))
    register_lang_block(tex)
    track_tool(tex, "shovel")


def write_button_set(variant):
    name = f"{variant}_button"
    texture = variant
    write_button(name, texture)
    write_recipe(f"{name}_from_crafting", button_craft(f"worldofstone:{name}", f"worldofstone:{variant}"))


def write_speleothem_set(variant):
    name = f"{variant}_speleothem"
    write_speleothem_block(name, f"worldofstone:block/{variant}")


def write_sandstone_speleothem_set(variant):
    name = f"{variant}_sandstone_speleothem"
    write_speleothem_block(name, f"worldofstone:block/{variant}_sandstone_normal")


def write_smooth_stone_set(variant):
    smooth_name = f"smooth_{variant}"
    smooth_slab_name = f"smooth_{variant}_slab"
    make_smooth_stone_texture(variant, smooth_name)
    write_basic_block(smooth_name, smooth_name)
    write_slab(smooth_slab_name, smooth_name, smooth_name)
    SMOOTH_STONE_BLOCKS.append(smooth_name)
    SMOOTH_STONE_SLABS.append(smooth_slab_name)
    ns = "worldofstone:"
    write_recipe(f"smooth_{variant}_from_smelting",
                 smelting_recipe(ns + smooth_name, ns + variant, 0.1, 200))
    write_recipe(f"smooth_{variant}_slab_from_crafting",
                 slab_craft(ns + smooth_slab_name, ns + smooth_name))
    write_recipe(f"smooth_{variant}_from_stonecutting",
                 stonecutter_recipe(ns + smooth_name, ns + variant))
    write_recipe(f"smooth_{variant}_slab_from_stonecutting_stone",
                 stonecutter_recipe(ns + smooth_slab_name, ns + variant, 2))
    write_recipe(f"smooth_{variant}_slab_from_stonecutting_smooth",
                 stonecutter_recipe(ns + smooth_slab_name, ns + smooth_name, 2))


def pillar_blockstate(name):
    return {
        "variants": {
            "axis=y": {"model": f"worldofstone:block/{name}"},
            "axis=x": {"model": f"worldofstone:block/{name}_horizontal", "x": 90, "y": 90},
            "axis=z": {"model": f"worldofstone:block/{name}_horizontal", "x": 90}
        }
    }


def cube_column_model(side_tex, end_tex):
    return {
        "parent": "minecraft:block/cube_column",
        "textures": {
            "side": f"worldofstone:block/{side_tex}",
            "end": f"worldofstone:block/{end_tex}"
        }
    }


def cube_column_horizontal_model(side_tex, end_tex):
    return {
        "parent": "minecraft:block/cube_column_horizontal",
        "textures": {
            "side": f"worldofstone:block/{side_tex}",
            "end": f"worldofstone:block/{end_tex}"
        }
    }


def write_pillar(name, side_tex, end_tex):
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{name}.json"), pillar_blockstate(name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}.json"), cube_column_model(side_tex, end_tex))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{name}_horizontal.json"), cube_column_horizontal_model(side_tex, end_tex))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{name}.json"), item_model_block(name))
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{name}.json"), simple_loot(name))
    register_lang_block(name)
    track_tool(name, "pickaxe")


POLISHED_BLOCKS = []
POLISHED_STAIRS = []
POLISHED_SLABS = []
POLISHED_WALLS = []
TILE_BLOCKS = []
TILE_STAIRS = []
TILE_SLABS = []
TILE_WALLS = []
CRACKED_TILE_BLOCKS = []
PILLAR_BLOCKS = []


def make_polished_texture(variant, dst_name):
    vanilla = fetch_vanilla("polished_andesite")
    variant_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    dst = os.path.join(DST_BLOCK_TEX, dst_name + ".png")
    if vanilla and os.path.exists(variant_path):
        if recolor_with_variant_palette(vanilla, variant_path, dst, softness=0.3):
            return True
    if os.path.exists(variant_path):
        safe_copyfile(variant_path, dst)
        return True
    return False


def make_tile_texture(variant, dst_name):
    vanilla = fetch_vanilla("deepslate_tiles")
    variant_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    dst = os.path.join(DST_BLOCK_TEX, dst_name + ".png")
    if vanilla and os.path.exists(variant_path):
        if recolor_with_variant_palette(vanilla, variant_path, dst, softness=0.3):
            return True
    if os.path.exists(variant_path):
        safe_copyfile(variant_path, dst)
        return True
    return False


def make_cracked_tile_texture(variant, tile_name, cracked_name):
    base_vanilla = fetch_vanilla("deepslate_tiles")
    cracked_vanilla = fetch_vanilla("cracked_deepslate_tiles")
    tile_path = os.path.join(DST_BLOCK_TEX, tile_name + ".png")
    dst = os.path.join(DST_BLOCK_TEX, cracked_name + ".png")
    if not base_vanilla or not cracked_vanilla or not os.path.exists(tile_path):
        if os.path.exists(tile_path):
            safe_copyfile(tile_path, dst)
            return True
        return False
    base = Image.open(base_vanilla).convert("RGBA")
    cracked = Image.open(cracked_vanilla).convert("RGBA")
    tile_img = Image.open(tile_path).convert("RGBA")
    if base.size != cracked.size:
        safe_copyfile(tile_path, dst)
        return True
    if tile_img.size != base.size:
        tile_img = tile_img.resize(base.size, Image.NEAREST)
    out = Image.new("RGBA", tile_img.size)
    bp, cp, tp, op = base.load(), cracked.load(), tile_img.load(), out.load()
    for y in range(tile_img.size[1]):
        for x in range(tile_img.size[0]):
            br, bg, bb, _ = bp[x, y]
            cr, cg, cb, _ = cp[x, y]
            tr, tg, tb, ta = tp[x, y]
            base_lum = max(1, (br + bg + bb) / 3)
            cracked_lum = (cr + cg + cb) / 3
            ratio = cracked_lum / base_lum
            if ratio < 0.85:
                op[x, y] = (
                    max(0, min(255, int(tr * ratio))),
                    max(0, min(255, int(tg * ratio))),
                    max(0, min(255, int(tb * ratio))),
                    ta,
                )
            else:
                op[x, y] = (tr, tg, tb, ta)
    out.save(dst)
    return True


def make_pillar_textures(variant, side_name, top_name):
    side_vanilla = fetch_vanilla("quartz_pillar")
    top_vanilla = fetch_vanilla("quartz_pillar_top")
    variant_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
    side_dst = os.path.join(DST_BLOCK_TEX, side_name + ".png")
    top_dst = os.path.join(DST_BLOCK_TEX, top_name + ".png")
    if side_vanilla and os.path.exists(variant_path):
        recolor_with_variant_palette(side_vanilla, variant_path, side_dst, softness=0.3)
    elif os.path.exists(variant_path):
        safe_copyfile(variant_path, side_dst)
    if top_vanilla and os.path.exists(variant_path):
        recolor_with_variant_palette(top_vanilla, variant_path, top_dst, softness=0.3)
    elif os.path.exists(variant_path):
        safe_copyfile(variant_path, top_dst)


def write_polished_set(variant):
    name = f"polished_{variant}"
    stairs_name = f"polished_{variant}_stairs"
    slab_name = f"polished_{variant}_slab"
    wall_name = f"polished_{variant}_wall"
    make_polished_texture(variant, name)
    write_basic_block(name, name)
    write_stairs(stairs_name, name, name)
    write_slab(slab_name, name, name)
    write_wall(wall_name, name)
    POLISHED_BLOCKS.append(name)
    POLISHED_STAIRS.append(stairs_name)
    POLISHED_SLABS.append(slab_name)
    POLISHED_WALLS.append(wall_name)
    ns = "worldofstone:"
    write_recipe(f"{name}_from_crafting", bricks_craft(ns + name, ns + variant))
    write_recipe(f"{stairs_name}_from_crafting", stairs_craft(ns + stairs_name, ns + name))
    write_recipe(f"{slab_name}_from_crafting", slab_craft(ns + slab_name, ns + name))
    write_recipe(f"{wall_name}_from_crafting", wall_craft(ns + wall_name, ns + name))
    write_recipe(f"{name}_from_stonecutting", stonecutter_recipe(ns + name, ns + variant))
    write_recipe(f"{stairs_name}_from_stonecutting_stone", stonecutter_recipe(ns + stairs_name, ns + variant))
    write_recipe(f"{stairs_name}_from_stonecutting_polished", stonecutter_recipe(ns + stairs_name, ns + name))
    write_recipe(f"{slab_name}_from_stonecutting_stone", stonecutter_recipe(ns + slab_name, ns + variant, 2))
    write_recipe(f"{slab_name}_from_stonecutting_polished", stonecutter_recipe(ns + slab_name, ns + name, 2))
    write_recipe(f"{wall_name}_from_stonecutting_stone", stonecutter_recipe(ns + wall_name, ns + variant))
    write_recipe(f"{wall_name}_from_stonecutting_polished", stonecutter_recipe(ns + wall_name, ns + name))


def write_tile_set(variant):
    tile_name = f"{variant}_tiles"
    cracked_name = f"cracked_{variant}_tiles"
    stairs_name = f"{variant}_tile_stairs"
    slab_name = f"{variant}_tile_slab"
    wall_name = f"{variant}_tile_wall"
    make_tile_texture(variant, tile_name)
    make_cracked_tile_texture(variant, tile_name, cracked_name)
    write_basic_block(tile_name, tile_name)
    write_basic_block(cracked_name, cracked_name)
    write_stairs(stairs_name, tile_name, tile_name)
    write_slab(slab_name, tile_name, tile_name)
    write_wall(wall_name, tile_name)
    TILE_BLOCKS.append(tile_name)
    TILE_STAIRS.append(stairs_name)
    TILE_SLABS.append(slab_name)
    TILE_WALLS.append(wall_name)
    CRACKED_TILE_BLOCKS.append(cracked_name)
    ns = "worldofstone:"
    write_recipe(f"{tile_name}_from_crafting", bricks_craft(ns + tile_name, ns + f"{variant}_bricks"))
    write_recipe(f"{stairs_name}_from_crafting", stairs_craft(ns + stairs_name, ns + tile_name))
    write_recipe(f"{slab_name}_from_crafting", slab_craft(ns + slab_name, ns + tile_name))
    write_recipe(f"{wall_name}_from_crafting", wall_craft(ns + wall_name, ns + tile_name))
    write_recipe(f"{cracked_name}_from_smelting", smelting_recipe(ns + cracked_name, ns + tile_name, 0.1, 200))
    write_recipe(f"{tile_name}_from_stonecutting", stonecutter_recipe(ns + tile_name, ns + variant))
    write_recipe(f"{tile_name}_from_stonecutting_polished", stonecutter_recipe(ns + tile_name, ns + f"polished_{variant}"))
    write_recipe(f"{stairs_name}_from_stonecutting_stone", stonecutter_recipe(ns + stairs_name, ns + variant))
    write_recipe(f"{stairs_name}_from_stonecutting_tile", stonecutter_recipe(ns + stairs_name, ns + tile_name))
    write_recipe(f"{slab_name}_from_stonecutting_stone", stonecutter_recipe(ns + slab_name, ns + variant, 2))
    write_recipe(f"{slab_name}_from_stonecutting_tile", stonecutter_recipe(ns + slab_name, ns + tile_name, 2))
    write_recipe(f"{wall_name}_from_stonecutting_stone", stonecutter_recipe(ns + wall_name, ns + variant))
    write_recipe(f"{wall_name}_from_stonecutting_tile", stonecutter_recipe(ns + wall_name, ns + tile_name))


def write_pillar_set(variant):
    name = f"{variant}_pillar"
    side_name = name
    top_name = f"{variant}_pillar_top"
    make_pillar_textures(variant, side_name, top_name)
    write_pillar(name, side_name, top_name)
    PILLAR_BLOCKS.append(name)
    ns = "worldofstone:"
    write_recipe(f"{name}_from_crafting", shaped_recipe(ns + name, 2, ["#", "#"], ns + variant))
    write_recipe(f"{name}_from_stonecutting", stonecutter_recipe(ns + name, ns + variant))


VANILLA_SPELEOTHEMS = [
    ("stone_speleothem", "minecraft:block/stone", "Stone Speleothem"),
    ("granite_speleothem", "minecraft:block/granite", "Granite Speleothem"),
    ("diorite_speleothem", "minecraft:block/diorite", "Diorite Speleothem"),
    ("andesite_speleothem", "minecraft:block/andesite", "Andesite Speleothem"),
    ("deepslate_speleothem", "minecraft:block/deepslate", "Deepslate Speleothem"),
    ("tuff_speleothem", "minecraft:block/tuff", "Tuff Speleothem"),
    ("calcite_speleothem", "minecraft:block/calcite", "Calcite Speleothem"),
    ("dripstone_speleothem", "minecraft:block/dripstone_block", "Dripstone Speleothem"),
    ("netherrack_speleothem", "minecraft:block/netherrack", "Netherrack Speleothem"),
    ("basalt_speleothem", "minecraft:block/basalt_side", "Basalt Speleothem"),
    ("blackstone_speleothem", "minecraft:block/blackstone", "Blackstone Speleothem"),
    ("end_stone_speleothem", "minecraft:block/end_stone", "End Stone Speleothem"),
    ("sandstone_speleothem", "minecraft:block/sandstone", "Sandstone Speleothem"),
    ("red_sandstone_speleothem", "minecraft:block/red_sandstone", "Red Sandstone Speleothem"),
    ("smooth_basalt_speleothem", "minecraft:block/smooth_basalt", "Smooth Basalt Speleothem"),
]

QUARK_SPELEOTHEMS = [
    ("quark_limestone_speleothem", "quark:block/limestone", "Limestone Speleothem"),
    ("quark_shale_speleothem", "quark:block/shale", "Shale Speleothem"),
    ("quark_myalite_speleothem", "quark:block/myalite", "Myalite Speleothem"),
    ("quark_permafrost_speleothem", "quark:block/permafrost", "Permafrost Speleothem"),
    ("quark_jasper_speleothem", "quark:block/jasper", "Jasper Speleothem"),
    ("quark_dusky_myalite_speleothem", "quark:block/dusky_myalite", "Dusky Myalite Speleothem"),
]

UNDERGARDEN_SPELEOTHEMS = [
    ("undergarden_depthrock_speleothem", "undergarden:block/depthrock", "Depthrock Speleothem"),
    ("undergarden_shiverstone_speleothem", "undergarden:block/shiverstone", "Shiverstone Speleothem"),
]

CREATE_SPELEOTHEMS = [
    ("create_limestone_speleothem", "create:block/palettes/stone_types/limestone", "Limestone Speleothem"),
    ("create_scoria_speleothem", "create:block/palettes/stone_types/scoria", "Scoria Speleothem"),
    ("create_scorchia_speleothem", "create:block/palettes/stone_types/scorchia", "Scorchia Speleothem"),
    ("create_asurine_speleothem", "create:block/palettes/stone_types/natural/asurine_0", "Asurine Speleothem"),
    ("create_ochrum_speleothem", "create:block/palettes/stone_types/natural/ochrum_0", "Ochrum Speleothem"),
    ("create_veridium_speleothem", "create:block/palettes/stone_types/natural/veridium_0", "Veridium Speleothem"),
    ("create_crimsite_speleothem", "create:block/palettes/stone_types/natural/crimsite_0", "Crimsite Speleothem"),
]

BETTEREND_SPELEOTHEMS = [
    ("betterend_flavolite_speleothem", "betterend:block/flavolite", "Flavolite Speleothem"),
    ("betterend_violecite_speleothem", "betterend:block/violecite", "Violecite Speleothem"),
    ("betterend_virid_jadestone_speleothem", "betterend:block/virid_jadestone", "Virid Jadestone Speleothem"),
    ("betterend_azure_jadestone_speleothem", "betterend:block/azure_jadestone", "Azure Jadestone Speleothem"),
    ("betterend_sandy_jadestone_speleothem", "betterend:block/sandy_jadestone", "Sandy Jadestone Speleothem"),
    ("betterend_sulphuric_rock_speleothem", "betterend:block/sulphuric_rock", "Sulphuric Rock Speleothem"),
    ("betterend_umbralith_speleothem", "betterend:block/umbralith", "Umbralith Speleothem"),
    ("betterend_brimstone_speleothem", "betterend:block/brimstone", "Brimstone Speleothem"),
]

GALOSPHERE_SPELEOTHEMS = [
    ("galosphere_allurite_speleothem", "galosphere:block/allurite_block", "Allurite Speleothem"),
    ("galosphere_lumiere_speleothem", "galosphere:block/lumiere_block", "Lumiere Speleothem"),
]

BWG_SPELEOTHEMS = [
    ("bwg_dacite_speleothem", "biomeswevegone:block/dacite", "Dacite Speleothem"),
    ("bwg_white_dacite_speleothem", "biomeswevegone:block/white_dacite", "White Dacite Speleothem"),
    ("bwg_red_rock_speleothem", "biomeswevegone:block/red_rock", "Red Rock Speleothem"),
    ("bwg_black_sandstone_speleothem", "biomeswevegone:block/black_sandstone", "Black Sandstone Speleothem"),
    ("bwg_white_sandstone_speleothem", "biomeswevegone:block/white_sandstone", "White Sandstone Speleothem"),
    ("bwg_blue_sandstone_speleothem", "biomeswevegone:block/blue_sandstone", "Blue Sandstone Speleothem"),
    ("bwg_purple_sandstone_speleothem", "biomeswevegone:block/purple_sandstone", "Purple Sandstone Speleothem"),
    ("bwg_pink_sandstone_speleothem", "biomeswevegone:block/pink_sandstone", "Pink Sandstone Speleothem"),
    ("bwg_windswept_sandstone_speleothem", "biomeswevegone:block/windswept_sandstone", "Windswept Sandstone Speleothem"),
]

TWILIGHTFOREST_SPELEOTHEMS = [
    ("twilightforest_mazestone_speleothem", "twilightforest:block/mazestone", "Mazestone Speleothem"),
    ("twilightforest_deadrock_speleothem", "twilightforest:block/deadrock", "Deadrock Speleothem"),
    ("twilightforest_trollsteinn_speleothem", "twilightforest:block/trollsteinn_light", "Trollsteinn Speleothem"),
]

AETHER_SPELEOTHEMS = [
    ("aether_holystone_speleothem", "aether:block/natural/holystone", "Holystone Speleothem"),
    ("aether_aerogel_speleothem", "aether:block/construction/aerogel", "Aerogel Speleothem"),
]

BLUE_SKIES_SPELEOTHEMS = [
    ("blue_skies_lunar_stone_speleothem", "blue_skies:block/stone/lunar_stone", "Lunar Stone Speleothem"),
    ("blue_skies_turquoise_stone_speleothem", "blue_skies:block/stone/turquoise_stone", "Turquoise Stone Speleothem"),
    ("blue_skies_midnight_sandstone_speleothem", "blue_skies:block/stone/midnight_sandstone", "Midnight Sandstone Speleothem"),
    ("blue_skies_crystal_sandstone_speleothem", "blue_skies:block/stone/crystal_sandstone", "Crystal Sandstone Speleothem"),
]

SPELUNKERY_SPELEOTHEMS = [
    ("spelunkery_rock_salt_block_speleothem", "spelunkery:block/rock_salt_block", "Rock Salt Speleothem"),
    ("spelunkery_nephrite_speleothem", "spelunkery:block/nephrite", "Nephrite Speleothem"),
]

ICEANDFIRE_SPELEOTHEMS = [
    ("iceandfire_dread_stone_speleothem", "iceandfire:block/dread_stone", "Dread Stone Speleothem"),
]

MYSTICALAGRICULTURE_SPELEOTHEMS = [
    ("mysticalagriculture_soulstone_speleothem", "mysticalagriculture:block/soulstone", "Soulstone Speleothem"),
]

BIOMESOPLENTY_SPELEOTHEMS = [
    ("biomesoplenty_white_sandstone_speleothem", "biomesoplenty:block/white_sandstone", "White Sandstone Speleothem"),
    ("biomesoplenty_orange_sandstone_speleothem", "biomesoplenty:block/orange_sandstone", "Orange Sandstone Speleothem"),
    ("biomesoplenty_black_sandstone_speleothem", "biomesoplenty:block/black_sandstone", "Black Sandstone Speleothem"),
    ("biomesoplenty_brimstone_speleothem", "biomesoplenty:block/brimstone", "Brimstone Speleothem"),
]

FORBIDDEN_ARCANUS_SPELEOTHEMS = [
    ("forbidden_arcanus_darkstone_speleothem", "forbidden_arcanus:block/darkstone", "Darkstone Speleothem"),
    ("forbidden_arcanus_soulless_sandstone_speleothem", "forbidden_arcanus:block/soulless_sandstone", "Soulless Sandstone Speleothem"),
]

ALEXSCAVES_SPELEOTHEMS = [
    ("alexscaves_galena_speleothem", "alexscaves:block/galena", "Galena Speleothem"),
    ("alexscaves_limestone_speleothem", "alexscaves:block/limestone", "Limestone Speleothem"),
    ("alexscaves_radrock_speleothem", "alexscaves:block/radrock", "Radrock Speleothem"),
    ("alexscaves_abyssmarine_speleothem", "alexscaves:block/abyssmarine", "Abyssmarine Speleothem"),
    ("alexscaves_guanostone_speleothem", "alexscaves:block/guanostone", "Guanostone Speleothem"),
    ("alexscaves_coprolith_speleothem", "alexscaves:block/coprolith", "Coprolith Speleothem"),
    ("alexscaves_gingerbread_block_speleothem", "alexscaves:block/gingerbread_block", "Gingerbread Speleothem"),
]

ARS_NOUVEAU_SPELEOTHEMS = [
    ("ars_nouveau_sourcestone_speleothem", "ars_nouveau:block/sourcestone", "Sourcestone Speleothem"),
]

CATACLYSM_SPELEOTHEMS = [
    ("cataclysm_azure_seastone_speleothem", "cataclysm:block/azure_seastone", "Azure Seastone Speleothem"),
]

TWIGS_SPELEOTHEMS = [
    ("twigs_schist_speleothem", "twigs:block/schist", "Schist Speleothem"),
    ("twigs_rhyolite_speleothem", "twigs:block/rhyolite_side", "Rhyolite Speleothem"),
    ("twigs_bloodstone_speleothem", "twigs:block/bloodstone", "Bloodstone Speleothem"),
]

ARCHITECTS_PALETTE_SPELEOTHEMS = [
    ("architects_palette_abyssaline_speleothem", "architects_palette:block/abyssaline_side", "Abyssaline Speleothem"),
    ("architects_palette_myonite_speleothem", "architects_palette:block/myonite", "Myonite Speleothem"),
    ("architects_palette_hadaline_speleothem", "architects_palette:block/hadaline", "Hadaline Speleothem"),
    ("architects_palette_esoterrack_speleothem", "architects_palette:block/esoterrack", "Esoterrack Speleothem"),
    ("architects_palette_onyx_speleothem", "architects_palette:block/onyx", "Onyx Speleothem"),
    ("architects_palette_wardstone_speleothem", "architects_palette:block/wardstone", "Wardstone Speleothem"),
    ("architects_palette_moonshale_speleothem", "architects_palette:block/moonshale", "Moonshale Speleothem"),
    ("architects_palette_nebulite_speleothem", "architects_palette:block/nebulite", "Nebulite Speleothem"),
]

OUTER_END_SPELEOTHEMS = [
    ("outer_end_violite_speleothem", "outer_end:block/violite", "Violite Speleothem"),
    ("outer_end_stromatolite_speleothem", "outer_end:block/stromatolite", "Stromatolite Speleothem"),
    ("outer_end_halite_speleothem", "outer_end:block/halite", "Halite Speleothem"),
    ("outer_end_ancient_stone_speleothem", "outer_end:block/ancient_ice_cap_top", "Ancient Stone Speleothem"),
]

BOTANIA_SPELEOTHEMS = [
    ("botania_livingrock_speleothem", "botania:block/livingrock", "Livingrock Speleothem"),
    ("botania_shimmerrock_speleothem", "botania:block/shimmerrock", "Shimmerrock Speleothem"),
    ("botania_metamorphic_forest_stone_speleothem", "botania:block/metamorphic_forest_stone", "Fuchsite Speleothem"),
    ("botania_metamorphic_plains_stone_speleothem", "botania:block/metamorphic_plains_stone", "Talc Speleothem"),
    ("botania_metamorphic_mountain_stone_speleothem", "botania:block/metamorphic_mountain_stone", "Gneiss Speleothem"),
    ("botania_metamorphic_fungal_stone_speleothem", "botania:block/metamorphic_fungal_stone", "Mycelite Speleothem"),
    ("botania_metamorphic_swamp_stone_speleothem", "botania:block/metamorphic_swamp_stone", "Cataclasite Speleothem"),
    ("botania_metamorphic_desert_stone_speleothem", "botania:block/metamorphic_desert_stone", "Solite Speleothem"),
    ("botania_metamorphic_taiga_stone_speleothem", "botania:block/metamorphic_taiga_stone", "Lunite Speleothem"),
    ("botania_metamorphic_mesa_stone_speleothem", "botania:block/metamorphic_mesa_stone", "Rosy Talc Speleothem"),
]

AD_ASTRA_SPELEOTHEMS = [
    ("ad_astra_sky_stone_speleothem", "ad_astra:block/sky_stone", "Sky Stone Speleothem"),
    ("ad_astra_moon_stone_speleothem", "ad_astra:block/moon_stone", "Moon Stone Speleothem"),
    ("ad_astra_moon_deepslate_speleothem", "ad_astra:block/moon_deepslate", "Moon Deepslate Speleothem"),
    ("ad_astra_mars_stone_speleothem", "ad_astra:block/mars_stone", "Mars Stone Speleothem"),
    ("ad_astra_venus_stone_speleothem", "ad_astra:block/venus_stone", "Venus Stone Speleothem"),
    ("ad_astra_venus_sandstone_speleothem", "ad_astra:block/venus_sandstone", "Venus Sandstone Speleothem"),
    ("ad_astra_mercury_stone_speleothem", "ad_astra:block/mercury_stone", "Mercury Stone Speleothem"),
    ("ad_astra_glacio_stone_speleothem", "ad_astra:block/glacio_stone", "Glacio Stone Speleothem"),
    ("ad_astra_permafrost_speleothem", "ad_astra:block/permafrost", "Permafrost Speleothem"),
]

DEEP_AETHER_SPELEOTHEMS = [
    ("deep_aether_aseterite_speleothem", "deep_aether:block/aseterite", "Aseterite Speleothem"),
    ("deep_aether_raw_clorite_speleothem", "deep_aether:block/raw_clorite", "Raw Clorite Speleothem"),
]

CAVERNS_AND_CHASMS_SPELEOTHEMS = [
    ("caverns_and_chasms_sugilite_speleothem", "caverns_and_chasms:block/sugilite", "Sugilite Speleothem"),
    ("caverns_and_chasms_cylindrite_speleothem", "caverns_and_chasms:block/cylindrite", "Cylindrite Speleothem"),
    ("caverns_and_chasms_rhyolite_speleothem", "caverns_and_chasms:block/rhyolite", "Rhyolite Speleothem"),
]

ATMOSPHERIC_SPELEOTHEMS = [
    ("atmospheric_ivory_travertine_speleothem", "atmospheric:block/ivory_travertine_side", "Ivory Travertine Speleothem"),
    ("atmospheric_peach_travertine_speleothem", "atmospheric:block/peach_travertine_side", "Peach Travertine Speleothem"),
    ("atmospheric_persimmon_travertine_speleothem", "atmospheric:block/persimmon_travertine_side", "Persimmon Travertine Speleothem"),
    ("atmospheric_saffron_travertine_speleothem", "atmospheric:block/saffron_travertine_side", "Saffron Travertine Speleothem"),
    ("atmospheric_dolerite_speleothem", "atmospheric:block/dolerite", "Dolerite Speleothem"),
    ("atmospheric_arid_sandstone_speleothem", "atmospheric:block/arid_sandstone", "Arid Sandstone Speleothem"),
    ("atmospheric_red_arid_sandstone_speleothem", "atmospheric:block/red_arid_sandstone", "Red Arid Sandstone Speleothem"),
]

ENDERGETIC_SPELEOTHEMS = [
    ("endergetic_eumus_speleothem", "endergetic:block/eumus", "Eumus Speleothem"),
]

WILDER_WILDS_SPELEOTHEMS = [
    ("wilder_wilds_lavenderhardenedclay_speleothem", "wilder_wilds:block/lavender_harded_clay", "Lavender Terracotta Speleothem"),
    ("wilder_wilds_coral_hardened_clay_speleothem", "wilder_wilds:block/coralterracotta", "Coral Terracotta Speleothem"),
    ("wilder_wilds_cream_hardened_clay_speleothem", "wilder_wilds:block/creamterracotta", "Cream Terracotta Speleothem"),
]

REGIONS_UNEXPLORED_SPELEOTHEMS = [
    ("regions_unexplored_chalk_speleothem", "regions_unexplored:block/chalk", "Chalk Speleothem"),
    ("regions_unexplored_argillite_speleothem", "regions_unexplored:block/argillite", "Argillite Speleothem"),
    ("regions_unexplored_mossy_stone_speleothem", "regions_unexplored:block/mossy_stone", "Mossy Stone Speleothem"),
]

BORN_IN_CHAOS_V1_SPELEOTHEMS = [
    ("born_in_chaos_v1_black_argillite_speleothem", "born_in_chaos_v1:block/tiemnyikirpich6", "Black Argillite Speleothem"),
]

NATURALIST_SPELEOTHEMS = [
    ("naturalist_shellstone_speleothem", "naturalist:block/shellstone", "Shellstone Speleothem"),
]

YUNGSCAVEBIOMES_SPELEOTHEMS = [
    ("yungscavebiomes_ancient_sandstone_speleothem", "yungscavebiomes:block/ancient_sandstone", "Ancient Sandstone Speleothem"),
]

NATURES_SPIRIT_SPELEOTHEMS = [
    ("natures_spirit_travertine_speleothem", "natures_spirit:block/travertine", "Travertine Speleothem"),
    ("natures_spirit_chert_speleothem", "natures_spirit:block/chert", "Chert Speleothem"),
    ("natures_spirit_pink_sandstone_speleothem", "natures_spirit:block/pink_sandstone", "Pink Sandstone Speleothem"),
]

NETHEREXP_SPELEOTHEMS = [
    ("netherexp_soul_slate_speleothem", "netherexp:block/soul_slate", "Soul Slate Speleothem"),
    ("netherexp_pale_soul_slate_speleothem", "netherexp:block/pale_soul_slate", "Pale Soul Slate Speleothem"),
    ("netherexp_black_ice_speleothem", "netherexp:block/black_ice", "Black Ice Speleothem"),
]

DEEPERDARKER_SPELEOTHEMS = [
    ("deeperdarker_sculk_stone_speleothem", "deeperdarker:block/sculk_stone", "Sculk Stone Speleothem"),
    ("deeperdarker_gloomslate_speleothem", "deeperdarker:block/gloomslate", "Gloomslate Speleothem"),
]

THE_DEEP_VOID_SPELEOTHEMS = [
    ("the_deep_void_ancient_deepslate_speleothem", "the_deep_void:block/ancient_deepslate", "Ancient Deepslate Speleothem"),
    ("the_deep_void_primordial_stone_speleothem", "the_deep_void:block/primordial_rock", "Primordial Stone Speleothem"),
    ("the_deep_void_solid_void_block_speleothem", "the_deep_void:block/solid_void_block", "Solid Void Speleothem"),
    ("the_deep_void_monolithic_stone_speleothem", "the_deep_void:block/monolith_stone", "Monolithic Stone Speleothem"),
]

DEFILED_LANDS_PREBORN_SPELEOTHEMS = [
    ("defiled_lands_preborn_defiled_stone_speleothem", "defiled_lands_preborn:block/defiled_stone", "Defiled Stone Speleothem"),
]

DECORATIVE_SPELEOTHEMS = [
    ("ice_speleothem", "minecraft:block/ice", "Ice Speleothem", False, None, "pickaxe"),
    ("packed_ice_speleothem", "minecraft:block/packed_ice", "Packed Ice Speleothem", False, None, "pickaxe"),
    ("blue_ice_speleothem", "minecraft:block/blue_ice", "Blue Ice Speleothem", False, None, "pickaxe"),
    ("obsidian_speleothem", "minecraft:block/obsidian", "Obsidian Speleothem", False, "diamond", "pickaxe"),
    ("crying_obsidian_speleothem", "minecraft:block/crying_obsidian", "Crying Obsidian Speleothem", False, "diamond", "pickaxe"),
    ("bone_speleothem", "minecraft:block/bone_block_side", "Bone Speleothem", False, None, "pickaxe"),
    ("raw_iron_speleothem", "minecraft:block/raw_iron_block", "Raw Iron Speleothem", False, "stone", "pickaxe"),
    ("raw_copper_speleothem", "minecraft:block/raw_copper_block", "Raw Copper Speleothem", False, "stone", "pickaxe"),
    ("raw_gold_speleothem", "minecraft:block/raw_gold_block", "Raw Gold Speleothem", False, "iron", "pickaxe"),
    ("glowstone_speleothem", "minecraft:block/glowstone", "Glowstone Speleothem", False, None, "pickaxe"),
    ("magma_speleothem", "minecraft:block/magma", "Magma Speleothem", False, None, "pickaxe"),
    ("amethyst_speleothem", "minecraft:block/amethyst_block", "Amethyst Speleothem", False, None, "pickaxe"),
    ("purpur_speleothem", "minecraft:block/purpur_block", "Purpur Speleothem", False, None, "pickaxe"),
    ("prismarine_speleothem", "minecraft:block/prismarine", "Prismarine Speleothem", False, None, "pickaxe"),
    ("prismarine_bricks_speleothem", "minecraft:block/prismarine_bricks", "Prismarine Bricks Speleothem", False, None, "pickaxe"),
    ("dark_prismarine_speleothem", "minecraft:block/dark_prismarine", "Dark Prismarine Speleothem", False, None, "pickaxe"),
    ("quartz_speleothem", "minecraft:block/quartz_block_side", "Quartz Speleothem", False, None, "pickaxe"),
    ("coal_speleothem", "minecraft:block/coal_block", "Coal Speleothem", False, None, "pickaxe"),
    ("copper_speleothem", "minecraft:block/copper_block", "Copper Speleothem", False, "stone", "pickaxe"),
    ("exposed_copper_speleothem", "minecraft:block/exposed_copper", "Exposed Copper Speleothem", False, "stone", "pickaxe"),
    ("weathered_copper_speleothem", "minecraft:block/weathered_copper", "Weathered Copper Speleothem", False, "stone", "pickaxe"),
    ("oxidized_copper_speleothem", "minecraft:block/oxidized_copper", "Oxidized Copper Speleothem", False, "stone", "pickaxe"),
    ("iron_speleothem", "minecraft:block/iron_block", "Iron Speleothem", False, "stone", "pickaxe"),
    ("gold_speleothem", "minecraft:block/gold_block", "Gold Speleothem", False, "iron", "pickaxe"),
    ("redstone_speleothem", "minecraft:block/redstone_block", "Redstone Speleothem", False, None, "pickaxe"),
    ("emerald_speleothem", "minecraft:block/emerald_block", "Emerald Speleothem", False, "iron", "pickaxe"),
    ("lapis_speleothem", "minecraft:block/lapis_block", "Lapis Lazuli Speleothem", False, "stone", "pickaxe"),
    ("diamond_speleothem", "minecraft:block/diamond_block", "Diamond Speleothem", False, "iron", "pickaxe"),
    ("netherite_speleothem", "minecraft:block/netherite_block", "Netherite Speleothem", False, "diamond", "pickaxe"),
    ("dirt_speleothem", "minecraft:block/dirt", "Dirt Speleothem", False, None, "shovel"),
    ("snow_speleothem", "minecraft:block/snow", "Snow Speleothem", False, None, "shovel"),
    ("mud_speleothem", "minecraft:block/mud", "Mud Speleothem", False, None, "shovel"),
    ("packed_mud_speleothem", "minecraft:block/packed_mud", "Packed Mud Speleothem", False, None, "pickaxe"),
    ("tnt_speleothem", "minecraft:block/tnt_side", "TNT Speleothem", False, None, None),
    ("end_portal_frame_speleothem", "minecraft:block/end_portal_frame_top", "End Portal Frame Speleothem", True, None, "pickaxe"),
    ("bedrock_speleothem", "minecraft:block/bedrock", "Bedrock Speleothem", True, None, "pickaxe"),
    ("glass_speleothem", "minecraft:block/glass", "Glass Speleothem", False, None, None),
    ("tinted_glass_speleothem", "minecraft:block/tinted_glass", "Tinted Glass Speleothem", False, None, None),
    ("white_stained_glass_speleothem", "minecraft:block/white_stained_glass", "White Stained Glass Speleothem", False, None, None),
    ("orange_stained_glass_speleothem", "minecraft:block/orange_stained_glass", "Orange Stained Glass Speleothem", False, None, None),
    ("magenta_stained_glass_speleothem", "minecraft:block/magenta_stained_glass", "Magenta Stained Glass Speleothem", False, None, None),
    ("light_blue_stained_glass_speleothem", "minecraft:block/light_blue_stained_glass", "Light Blue Stained Glass Speleothem", False, None, None),
    ("yellow_stained_glass_speleothem", "minecraft:block/yellow_stained_glass", "Yellow Stained Glass Speleothem", False, None, None),
    ("lime_stained_glass_speleothem", "minecraft:block/lime_stained_glass", "Lime Stained Glass Speleothem", False, None, None),
    ("pink_stained_glass_speleothem", "minecraft:block/pink_stained_glass", "Pink Stained Glass Speleothem", False, None, None),
    ("gray_stained_glass_speleothem", "minecraft:block/gray_stained_glass", "Gray Stained Glass Speleothem", False, None, None),
    ("light_gray_stained_glass_speleothem", "minecraft:block/light_gray_stained_glass", "Light Gray Stained Glass Speleothem", False, None, None),
    ("cyan_stained_glass_speleothem", "minecraft:block/cyan_stained_glass", "Cyan Stained Glass Speleothem", False, None, None),
    ("purple_stained_glass_speleothem", "minecraft:block/purple_stained_glass", "Purple Stained Glass Speleothem", False, None, None),
    ("blue_stained_glass_speleothem", "minecraft:block/blue_stained_glass", "Blue Stained Glass Speleothem", False, None, None),
    ("brown_stained_glass_speleothem", "minecraft:block/brown_stained_glass", "Brown Stained Glass Speleothem", False, None, None),
    ("green_stained_glass_speleothem", "minecraft:block/green_stained_glass", "Green Stained Glass Speleothem", False, None, None),
    ("red_stained_glass_speleothem", "minecraft:block/red_stained_glass", "Red Stained Glass Speleothem", False, None, None),
    ("black_stained_glass_speleothem", "minecraft:block/black_stained_glass", "Black Stained Glass Speleothem", False, None, None),
    ("tube_coral_speleothem", "minecraft:block/tube_coral_block", "Tube Coral Speleothem", False, None, "pickaxe"),
    ("brain_coral_speleothem", "minecraft:block/brain_coral_block", "Brain Coral Speleothem", False, None, "pickaxe"),
    ("bubble_coral_speleothem", "minecraft:block/bubble_coral_block", "Bubble Coral Speleothem", False, None, "pickaxe"),
    ("fire_coral_speleothem", "minecraft:block/fire_coral_block", "Fire Coral Speleothem", False, None, "pickaxe"),
    ("horn_coral_speleothem", "minecraft:block/horn_coral_block", "Horn Coral Speleothem", False, None, "pickaxe"),
    ("sponge_speleothem", "minecraft:block/sponge", "Sponge Speleothem", False, None, "hoe"),
    ("slime_speleothem", "minecraft:block/slime_block", "Slime Speleothem", False, None, None),
    ("honey_speleothem", "minecraft:block/honey_block_top", "Honey Speleothem", False, None, None),
    ("moss_speleothem", "minecraft:block/moss_block", "Moss Speleothem", False, None, "hoe"),
    ("sea_lantern_speleothem", "minecraft:block/sea_lantern", "Sea Lantern Speleothem", False, None, None),
    ("white_wool_speleothem", "minecraft:block/white_wool", "White Wool Speleothem", False, None, None),
    ("orange_wool_speleothem", "minecraft:block/orange_wool", "Orange Wool Speleothem", False, None, None),
    ("magenta_wool_speleothem", "minecraft:block/magenta_wool", "Magenta Wool Speleothem", False, None, None),
    ("light_blue_wool_speleothem", "minecraft:block/light_blue_wool", "Light Blue Wool Speleothem", False, None, None),
    ("yellow_wool_speleothem", "minecraft:block/yellow_wool", "Yellow Wool Speleothem", False, None, None),
    ("lime_wool_speleothem", "minecraft:block/lime_wool", "Lime Wool Speleothem", False, None, None),
    ("pink_wool_speleothem", "minecraft:block/pink_wool", "Pink Wool Speleothem", False, None, None),
    ("gray_wool_speleothem", "minecraft:block/gray_wool", "Gray Wool Speleothem", False, None, None),
    ("light_gray_wool_speleothem", "minecraft:block/light_gray_wool", "Light Gray Wool Speleothem", False, None, None),
    ("cyan_wool_speleothem", "minecraft:block/cyan_wool", "Cyan Wool Speleothem", False, None, None),
    ("purple_wool_speleothem", "minecraft:block/purple_wool", "Purple Wool Speleothem", False, None, None),
    ("blue_wool_speleothem", "minecraft:block/blue_wool", "Blue Wool Speleothem", False, None, None),
    ("brown_wool_speleothem", "minecraft:block/brown_wool", "Brown Wool Speleothem", False, None, None),
    ("green_wool_speleothem", "minecraft:block/green_wool", "Green Wool Speleothem", False, None, None),
    ("red_wool_speleothem", "minecraft:block/red_wool", "Red Wool Speleothem", False, None, None),
    ("black_wool_speleothem", "minecraft:block/black_wool", "Black Wool Speleothem", False, None, None),
    ("white_concrete_speleothem", "minecraft:block/white_concrete", "White Concrete Speleothem", False, None, "pickaxe"),
    ("orange_concrete_speleothem", "minecraft:block/orange_concrete", "Orange Concrete Speleothem", False, None, "pickaxe"),
    ("magenta_concrete_speleothem", "minecraft:block/magenta_concrete", "Magenta Concrete Speleothem", False, None, "pickaxe"),
    ("light_blue_concrete_speleothem", "minecraft:block/light_blue_concrete", "Light Blue Concrete Speleothem", False, None, "pickaxe"),
    ("yellow_concrete_speleothem", "minecraft:block/yellow_concrete", "Yellow Concrete Speleothem", False, None, "pickaxe"),
    ("lime_concrete_speleothem", "minecraft:block/lime_concrete", "Lime Concrete Speleothem", False, None, "pickaxe"),
    ("pink_concrete_speleothem", "minecraft:block/pink_concrete", "Pink Concrete Speleothem", False, None, "pickaxe"),
    ("gray_concrete_speleothem", "minecraft:block/gray_concrete", "Gray Concrete Speleothem", False, None, "pickaxe"),
    ("light_gray_concrete_speleothem", "minecraft:block/light_gray_concrete", "Light Gray Concrete Speleothem", False, None, "pickaxe"),
    ("cyan_concrete_speleothem", "minecraft:block/cyan_concrete", "Cyan Concrete Speleothem", False, None, "pickaxe"),
    ("purple_concrete_speleothem", "minecraft:block/purple_concrete", "Purple Concrete Speleothem", False, None, "pickaxe"),
    ("blue_concrete_speleothem", "minecraft:block/blue_concrete", "Blue Concrete Speleothem", False, None, "pickaxe"),
    ("brown_concrete_speleothem", "minecraft:block/brown_concrete", "Brown Concrete Speleothem", False, None, "pickaxe"),
    ("green_concrete_speleothem", "minecraft:block/green_concrete", "Green Concrete Speleothem", False, None, "pickaxe"),
    ("red_concrete_speleothem", "minecraft:block/red_concrete", "Red Concrete Speleothem", False, None, "pickaxe"),
    ("black_concrete_speleothem", "minecraft:block/black_concrete", "Black Concrete Speleothem", False, None, "pickaxe"),
    ("white_glazed_terracotta_speleothem", "minecraft:block/white_glazed_terracotta", "White Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("orange_glazed_terracotta_speleothem", "minecraft:block/orange_glazed_terracotta", "Orange Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("magenta_glazed_terracotta_speleothem", "minecraft:block/magenta_glazed_terracotta", "Magenta Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("light_blue_glazed_terracotta_speleothem", "minecraft:block/light_blue_glazed_terracotta", "Light Blue Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("yellow_glazed_terracotta_speleothem", "minecraft:block/yellow_glazed_terracotta", "Yellow Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("lime_glazed_terracotta_speleothem", "minecraft:block/lime_glazed_terracotta", "Lime Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("pink_glazed_terracotta_speleothem", "minecraft:block/pink_glazed_terracotta", "Pink Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("gray_glazed_terracotta_speleothem", "minecraft:block/gray_glazed_terracotta", "Gray Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("light_gray_glazed_terracotta_speleothem", "minecraft:block/light_gray_glazed_terracotta", "Light Gray Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("cyan_glazed_terracotta_speleothem", "minecraft:block/cyan_glazed_terracotta", "Cyan Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("purple_glazed_terracotta_speleothem", "minecraft:block/purple_glazed_terracotta", "Purple Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("blue_glazed_terracotta_speleothem", "minecraft:block/blue_glazed_terracotta", "Blue Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("brown_glazed_terracotta_speleothem", "minecraft:block/brown_glazed_terracotta", "Brown Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("green_glazed_terracotta_speleothem", "minecraft:block/green_glazed_terracotta", "Green Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("red_glazed_terracotta_speleothem", "minecraft:block/red_glazed_terracotta", "Red Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("black_glazed_terracotta_speleothem", "minecraft:block/black_glazed_terracotta", "Black Glazed Terracotta Speleothem", False, None, "pickaxe"),
    ("honeycomb_speleothem", "minecraft:block/honeycomb_block", "Honeycomb Speleothem", False, None, None),
    ("bricks_speleothem", "minecraft:block/bricks", "Bricks Speleothem", False, None, "pickaxe"),
    ("hay_bale_speleothem", "minecraft:block/hay_block_side", "Hay Bale Speleothem", False, None, "hoe"),
    ("sculk_speleothem", "minecraft:block/sculk", "Sculk Speleothem", False, None, "hoe"),
    ("sculk_catalyst_speleothem", "minecraft:block/sculk_catalyst_side", "Sculk Catalyst Speleothem", False, None, "hoe"),
    ("clay_speleothem", "minecraft:block/clay", "Clay Speleothem", False, None, "shovel"),
    ("podzol_speleothem", "minecraft:block/podzol_side", "Podzol Speleothem", False, None, "shovel"),
    ("mycelium_speleothem", "minecraft:block/mycelium_side", "Mycelium Speleothem", False, None, "shovel"),
    ("coarse_dirt_speleothem", "minecraft:block/coarse_dirt", "Coarse Dirt Speleothem", False, None, "shovel"),
    ("rooted_dirt_speleothem", "minecraft:block/rooted_dirt", "Rooted Dirt Speleothem", False, None, "shovel"),
    ("soul_sand_speleothem", "minecraft:block/soul_sand", "Soul Sand Speleothem", False, None, "shovel"),
    ("soul_soil_speleothem", "minecraft:block/soul_soil", "Soul Soil Speleothem", False, None, "shovel"),
    ("grass_speleothem", "minecraft:block/grass_block_side", "Grass Speleothem", False, None, "shovel"),
    ("brown_mushroom_speleothem", "minecraft:block/brown_mushroom_block", "Brown Mushroom Speleothem", False, None, "axe"),
    ("red_mushroom_speleothem", "minecraft:block/red_mushroom_block", "Red Mushroom Speleothem", False, None, "axe"),
    ("nether_wart_speleothem", "minecraft:block/nether_wart_block", "Nether Wart Speleothem", False, None, "hoe"),
    ("warped_wart_speleothem", "minecraft:block/warped_wart_block", "Warped Wart Speleothem", False, None, "hoe"),
    ("shroomlight_speleothem", "minecraft:block/shroomlight", "Shroomlight Speleothem", False, None, "hoe"),
    ("ochre_froglight_speleothem", "minecraft:block/ochre_froglight_side", "Ochre Froglight Speleothem", False, None, None),
    ("verdant_froglight_speleothem", "minecraft:block/verdant_froglight_side", "Verdant Froglight Speleothem", False, None, None),
    ("pearlescent_froglight_speleothem", "minecraft:block/pearlescent_froglight_side", "Pearlescent Froglight Speleothem", False, None, None),
    ("dead_tube_coral_speleothem", "minecraft:block/dead_tube_coral_block", "Dead Tube Coral Speleothem", False, None, "pickaxe"),
    ("dead_brain_coral_speleothem", "minecraft:block/dead_brain_coral_block", "Dead Brain Coral Speleothem", False, None, "pickaxe"),
    ("dead_bubble_coral_speleothem", "minecraft:block/dead_bubble_coral_block", "Dead Bubble Coral Speleothem", False, None, "pickaxe"),
    ("dead_fire_coral_speleothem", "minecraft:block/dead_fire_coral_block", "Dead Fire Coral Speleothem", False, None, "pickaxe"),
    ("dead_horn_coral_speleothem", "minecraft:block/dead_horn_coral_block", "Dead Horn Coral Speleothem", False, None, "pickaxe"),
    ("melon_speleothem", "minecraft:block/melon_side", "Melon Speleothem", False, None, "axe"),
    ("pumpkin_speleothem", "minecraft:block/pumpkin_side", "Pumpkin Speleothem", False, None, "axe"),
    ("warped_nylium_speleothem", "minecraft:block/warped_nylium_side", "Warped Nylium Speleothem", False, None, "pickaxe"),
    ("crimson_nylium_speleothem", "minecraft:block/crimson_nylium_side", "Crimson Nylium Speleothem", False, None, "pickaxe"),
    ("oak_log_speleothem", "minecraft:block/oak_log", "Oak Log Speleothem", False, None, None),
    ("stripped_oak_log_speleothem", "minecraft:block/stripped_oak_log", "Stripped Oak Log Speleothem", False, None, None),
    ("oak_planks_speleothem", "minecraft:block/oak_planks", "Oak Planks Speleothem", False, None, None),
    ("spruce_log_speleothem", "minecraft:block/spruce_log", "Spruce Log Speleothem", False, None, None),
    ("stripped_spruce_log_speleothem", "minecraft:block/stripped_spruce_log", "Stripped Spruce Log Speleothem", False, None, None),
    ("spruce_planks_speleothem", "minecraft:block/spruce_planks", "Spruce Planks Speleothem", False, None, None),
    ("birch_log_speleothem", "minecraft:block/birch_log", "Birch Log Speleothem", False, None, None),
    ("stripped_birch_log_speleothem", "minecraft:block/stripped_birch_log", "Stripped Birch Log Speleothem", False, None, None),
    ("birch_planks_speleothem", "minecraft:block/birch_planks", "Birch Planks Speleothem", False, None, None),
    ("jungle_log_speleothem", "minecraft:block/jungle_log", "Jungle Log Speleothem", False, None, None),
    ("stripped_jungle_log_speleothem", "minecraft:block/stripped_jungle_log", "Stripped Jungle Log Speleothem", False, None, None),
    ("jungle_planks_speleothem", "minecraft:block/jungle_planks", "Jungle Planks Speleothem", False, None, None),
    ("acacia_log_speleothem", "minecraft:block/acacia_log", "Acacia Log Speleothem", False, None, None),
    ("stripped_acacia_log_speleothem", "minecraft:block/stripped_acacia_log", "Stripped Acacia Log Speleothem", False, None, None),
    ("acacia_planks_speleothem", "minecraft:block/acacia_planks", "Acacia Planks Speleothem", False, None, None),
    ("dark_oak_log_speleothem", "minecraft:block/dark_oak_log", "Dark Oak Log Speleothem", False, None, None),
    ("stripped_dark_oak_log_speleothem", "minecraft:block/stripped_dark_oak_log", "Stripped Dark Oak Log Speleothem", False, None, None),
    ("dark_oak_planks_speleothem", "minecraft:block/dark_oak_planks", "Dark Oak Planks Speleothem", False, None, None),
    ("mangrove_log_speleothem", "minecraft:block/mangrove_log", "Mangrove Log Speleothem", False, None, None),
    ("stripped_mangrove_log_speleothem", "minecraft:block/stripped_mangrove_log", "Stripped Mangrove Log Speleothem", False, None, None),
    ("mangrove_planks_speleothem", "minecraft:block/mangrove_planks", "Mangrove Planks Speleothem", False, None, None),
    ("cherry_log_speleothem", "minecraft:block/cherry_log", "Cherry Log Speleothem", False, None, None),
    ("stripped_cherry_log_speleothem", "minecraft:block/stripped_cherry_log", "Stripped Cherry Log Speleothem", False, None, None),
    ("cherry_planks_speleothem", "minecraft:block/cherry_planks", "Cherry Planks Speleothem", False, None, None),
    ("crimson_stem_speleothem", "minecraft:block/crimson_stem", "Crimson Stem Speleothem", False, None, None),
    ("stripped_crimson_stem_speleothem", "minecraft:block/stripped_crimson_stem", "Stripped Crimson Stem Speleothem", False, None, None),
    ("crimson_planks_speleothem", "minecraft:block/crimson_planks", "Crimson Planks Speleothem", False, None, None),
    ("warped_stem_speleothem", "minecraft:block/warped_stem", "Warped Stem Speleothem", False, None, None),
    ("stripped_warped_stem_speleothem", "minecraft:block/stripped_warped_stem", "Stripped Warped Stem Speleothem", False, None, None),
    ("warped_planks_speleothem", "minecraft:block/warped_planks", "Warped Planks Speleothem", False, None, None),
    ("bamboo_speleothem", "minecraft:block/bamboo_block", "Bamboo Speleothem", False, None, None),
    ("stripped_bamboo_speleothem", "minecraft:block/stripped_bamboo_block", "Stripped Bamboo Speleothem", False, None, None),
    ("bamboo_planks_speleothem", "minecraft:block/bamboo_planks", "Bamboo Planks Speleothem", False, None, None),
    ("bamboo_mosaic_speleothem", "minecraft:block/bamboo_mosaic", "Bamboo Mosaic Speleothem", False, None, "axe"),
    ("oak_leaves_speleothem", "minecraft:block/oak_leaves", "Oak Leaves Speleothem", False, None, "hoe"),
    ("spruce_leaves_speleothem", "minecraft:block/spruce_leaves", "Spruce Leaves Speleothem", False, None, "hoe"),
    ("birch_leaves_speleothem", "minecraft:block/birch_leaves", "Birch Leaves Speleothem", False, None, "hoe"),
    ("jungle_leaves_speleothem", "minecraft:block/jungle_leaves", "Jungle Leaves Speleothem", False, None, "hoe"),
    ("acacia_leaves_speleothem", "minecraft:block/acacia_leaves", "Acacia Leaves Speleothem", False, None, "hoe"),
    ("dark_oak_leaves_speleothem", "minecraft:block/dark_oak_leaves", "Dark Oak Leaves Speleothem", False, None, "hoe"),
    ("mangrove_leaves_speleothem", "minecraft:block/mangrove_leaves", "Mangrove Leaves Speleothem", False, None, "hoe"),
    ("cherry_leaves_speleothem", "minecraft:block/cherry_leaves", "Cherry Leaves Speleothem", False, None, "hoe"),
    ("azalea_leaves_speleothem", "minecraft:block/azalea_leaves", "Azalea Leaves Speleothem", False, None, "hoe"),
    ("flowering_azalea_leaves_speleothem", "minecraft:block/flowering_azalea_leaves", "Flowering Azalea Leaves Speleothem", False, None, "hoe"),
]

DROP_SELF_NAMES = {
    "tnt_speleothem",
    "tinted_glass_speleothem",
    "sponge_speleothem",
    "slime_speleothem",
    "honey_speleothem",
    "moss_speleothem",
    "white_wool_speleothem", "orange_wool_speleothem", "magenta_wool_speleothem", "light_blue_wool_speleothem",
    "yellow_wool_speleothem", "lime_wool_speleothem", "pink_wool_speleothem", "gray_wool_speleothem",
    "light_gray_wool_speleothem", "cyan_wool_speleothem", "purple_wool_speleothem", "blue_wool_speleothem",
    "brown_wool_speleothem", "green_wool_speleothem", "red_wool_speleothem", "black_wool_speleothem",
    "white_concrete_speleothem", "orange_concrete_speleothem", "magenta_concrete_speleothem", "light_blue_concrete_speleothem",
    "yellow_concrete_speleothem", "lime_concrete_speleothem", "pink_concrete_speleothem", "gray_concrete_speleothem",
    "light_gray_concrete_speleothem", "cyan_concrete_speleothem", "purple_concrete_speleothem", "blue_concrete_speleothem",
    "brown_concrete_speleothem", "green_concrete_speleothem", "red_concrete_speleothem", "black_concrete_speleothem",
    "white_glazed_terracotta_speleothem", "orange_glazed_terracotta_speleothem", "magenta_glazed_terracotta_speleothem",
    "light_blue_glazed_terracotta_speleothem", "yellow_glazed_terracotta_speleothem", "lime_glazed_terracotta_speleothem",
    "pink_glazed_terracotta_speleothem", "gray_glazed_terracotta_speleothem", "light_gray_glazed_terracotta_speleothem",
    "cyan_glazed_terracotta_speleothem", "purple_glazed_terracotta_speleothem", "blue_glazed_terracotta_speleothem",
    "brown_glazed_terracotta_speleothem", "green_glazed_terracotta_speleothem", "red_glazed_terracotta_speleothem",
    "black_glazed_terracotta_speleothem",
    "honeycomb_speleothem",
    "hay_bale_speleothem",
    "clay_speleothem", "podzol_speleothem", "mycelium_speleothem", "coarse_dirt_speleothem",
    "rooted_dirt_speleothem", "soul_sand_speleothem", "soul_soil_speleothem", "grass_speleothem",
    "brown_mushroom_speleothem", "red_mushroom_speleothem", "nether_wart_speleothem",
    "warped_wart_speleothem", "shroomlight_speleothem",
    "ochre_froglight_speleothem", "verdant_froglight_speleothem", "pearlescent_froglight_speleothem",
    "melon_speleothem", "pumpkin_speleothem",
    "warped_nylium_speleothem", "crimson_nylium_speleothem",
    "oak_log_speleothem", "stripped_oak_log_speleothem", "oak_planks_speleothem",
    "spruce_log_speleothem", "stripped_spruce_log_speleothem", "spruce_planks_speleothem",
    "birch_log_speleothem", "stripped_birch_log_speleothem", "birch_planks_speleothem",
    "jungle_log_speleothem", "stripped_jungle_log_speleothem", "jungle_planks_speleothem",
    "acacia_log_speleothem", "stripped_acacia_log_speleothem", "acacia_planks_speleothem",
    "dark_oak_log_speleothem", "stripped_dark_oak_log_speleothem", "dark_oak_planks_speleothem",
    "mangrove_log_speleothem", "stripped_mangrove_log_speleothem", "mangrove_planks_speleothem",
    "cherry_log_speleothem", "stripped_cherry_log_speleothem", "cherry_planks_speleothem",
    "crimson_stem_speleothem", "stripped_crimson_stem_speleothem", "crimson_planks_speleothem",
    "warped_stem_speleothem", "stripped_warped_stem_speleothem", "warped_planks_speleothem",
    "bamboo_speleothem", "stripped_bamboo_speleothem", "bamboo_planks_speleothem", "bamboo_mosaic_speleothem",
    "oak_leaves_speleothem", "spruce_leaves_speleothem", "birch_leaves_speleothem", "jungle_leaves_speleothem",
    "acacia_leaves_speleothem", "dark_oak_leaves_speleothem", "mangrove_leaves_speleothem", "cherry_leaves_speleothem",
    "azalea_leaves_speleothem", "flowering_azalea_leaves_speleothem",
}

TERRACOTTA_SPELEOTHEMS = [
    ("terracotta_speleothem", "minecraft:block/terracotta", "Terracotta Speleothem"),
    ("white_terracotta_speleothem", "minecraft:block/white_terracotta", "White Terracotta Speleothem"),
    ("orange_terracotta_speleothem", "minecraft:block/orange_terracotta", "Orange Terracotta Speleothem"),
    ("yellow_terracotta_speleothem", "minecraft:block/yellow_terracotta", "Yellow Terracotta Speleothem"),
    ("light_gray_terracotta_speleothem", "minecraft:block/light_gray_terracotta", "Light Gray Terracotta Speleothem"),
    ("red_terracotta_speleothem", "minecraft:block/red_terracotta", "Red Terracotta Speleothem"),
    ("brown_terracotta_speleothem", "minecraft:block/brown_terracotta", "Brown Terracotta Speleothem"),
    ("magenta_terracotta_speleothem", "minecraft:block/magenta_terracotta", "Magenta Terracotta Speleothem"),
    ("light_blue_terracotta_speleothem", "minecraft:block/light_blue_terracotta", "Light Blue Terracotta Speleothem"),
    ("lime_terracotta_speleothem", "minecraft:block/lime_terracotta", "Lime Terracotta Speleothem"),
    ("pink_terracotta_speleothem", "minecraft:block/pink_terracotta", "Pink Terracotta Speleothem"),
    ("gray_terracotta_speleothem", "minecraft:block/gray_terracotta", "Gray Terracotta Speleothem"),
    ("cyan_terracotta_speleothem", "minecraft:block/cyan_terracotta", "Cyan Terracotta Speleothem"),
    ("purple_terracotta_speleothem", "minecraft:block/purple_terracotta", "Purple Terracotta Speleothem"),
    ("blue_terracotta_speleothem", "minecraft:block/blue_terracotta", "Blue Terracotta Speleothem"),
    ("green_terracotta_speleothem", "minecraft:block/green_terracotta", "Green Terracotta Speleothem"),
    ("black_terracotta_speleothem", "minecraft:block/black_terracotta", "Black Terracotta Speleothem"),
]


def write_overgrown_set(variant):
    snowed_overlay = os.path.join(SRC_OVERLAY_DIR, "overgrown_stone_snowed_side_overlay.png")
    snowed_side = f"snowed_{variant}_side"
    composite_overlay(variant, snowed_overlay, snowed_side)
    write_overgrown_block(f"overgrown_{variant}", variant)
    write_overgrown_snowed(f"snowed_{variant}", variant, snowed_side)
    write_overgrown_recipes(variant, f"worldofstone:{variant}")


def write_overgrown_recipes(variant, base_item):
    write_recipe(f"snowed_{variant}", shapeless_recipe(f"worldofstone:snowed_{variant}", 1, [base_item, "minecraft:snow_block"]))
    write_recipe(f"overgrown_{variant}", shapeless_recipe(f"worldofstone:overgrown_{variant}", 1, [base_item, "minecraft:moss_block"]))
    write_recipe(f"snowed_{variant}_smelt", smelting_recipe(base_item, f"worldofstone:snowed_{variant}"))
    write_recipe(f"overgrown_{variant}_smelt", smelting_recipe(base_item, f"worldofstone:overgrown_{variant}"))


def write_overgrown_vanilla_set(host):
    host_actual = HOST_TEXTURE_OVERRIDES.get(host, host)
    host_path = fetch_vanilla(host_actual)
    if host_path is None:
        return
    os.makedirs(DST_BLOCK_TEX, exist_ok=True)
    safe_copyfile(host_path, os.path.join(DST_BLOCK_TEX, f"{host}.png"))
    snowed_overlay = os.path.join(SRC_OVERLAY_DIR, "overgrown_stone_snowed_side_overlay.png")
    snowed_side = f"snowed_{host}_side"
    composite_overlay(host, snowed_overlay, snowed_side, base_path_override=host_path)
    write_overgrown_block(f"overgrown_{host}", host)
    write_overgrown_snowed(f"snowed_{host}", host, snowed_side)
    write_overgrown_recipes(host, f"minecraft:{host}")


def write_wos_tag(path_segment, values):
    payload = {"replace": False, "values": safe_tag_values(values)}
    write_json(os.path.join(DST_DATA, "tags", "blocks", f"{path_segment}.json"), payload)
    write_json(os.path.join(DST_DATA, "tags", "items", f"{path_segment}.json"), payload)


def write_world_of_stone_tags():
    speleothem_ids = [f"worldofstone:{n}" for n in SPELEOTHEM_BLOCKS]
    write_wos_tag("speleothem", speleothem_ids)

    smooth_stone_ids = [f"worldofstone:{n}" for n in SMOOTH_STONE_BLOCKS]
    write_wos_tag("smooth_stones", smooth_stone_ids)

    cobble_brick_variants = set(IGNEOUS + METAMORPHIC + SEDIMENTARY)

    def variant_subcategories(variant):
        subs = {}
        ore_ids = [f"worldofstone:{variant}_{ore['name']}" for ore in ORES]
        subs["ores"] = ore_ids
        sandstone_ids = [
            f"worldofstone:{variant}_sandstone",
            f"worldofstone:cut_{variant}_sandstone",
            f"worldofstone:chiseled_{variant}_sandstone",
            f"worldofstone:smooth_{variant}_sandstone",
            f"worldofstone:{variant}_sandstone_stairs",
            f"worldofstone:{variant}_sandstone_slab",
            f"worldofstone:{variant}_sandstone_wall",
            f"worldofstone:cut_{variant}_sandstone_slab",
            f"worldofstone:smooth_{variant}_sandstone_stairs",
            f"worldofstone:smooth_{variant}_sandstone_slab",
        ]
        subs["sandstone"] = sandstone_ids
        if variant in cobble_brick_variants:
            cobble_ids = [
                f"worldofstone:{variant}_cobblestone",
                f"worldofstone:{variant}_cobblestone_stairs",
                f"worldofstone:{variant}_cobblestone_slab",
                f"worldofstone:{variant}_cobblestone_wall",
            ]
            subs["cobblestone"] = cobble_ids
            mossy_cobble_ids = [
                f"worldofstone:mossy_{variant}_cobblestone",
                f"worldofstone:mossy_{variant}_cobblestone_stairs",
                f"worldofstone:mossy_{variant}_cobblestone_slab",
                f"worldofstone:mossy_{variant}_cobblestone_wall",
            ]
            subs["mossy_cobblestone"] = mossy_cobble_ids
            brick_ids = [
                f"worldofstone:{variant}_bricks",
                f"worldofstone:{variant}_brick_stairs",
                f"worldofstone:{variant}_brick_slab",
                f"worldofstone:{variant}_brick_wall",
            ]
            subs["bricks"] = brick_ids
            mossy_brick_ids = [
                f"worldofstone:mossy_{variant}_bricks",
                f"worldofstone:mossy_{variant}_brick_stairs",
                f"worldofstone:mossy_{variant}_brick_slab",
                f"worldofstone:mossy_{variant}_brick_wall",
            ]
            subs["mossy_bricks"] = mossy_brick_ids
        subs["smooth_stone"] = [
            f"worldofstone:smooth_{variant}",
            f"worldofstone:smooth_{variant}_slab",
        ]
        subs["overgrown"] = [
            f"worldofstone:overgrown_{variant}",
            f"worldofstone:snowed_{variant}",
        ]
        subs["polished"] = [
            f"worldofstone:polished_{variant}",
            f"worldofstone:polished_{variant}_stairs",
            f"worldofstone:polished_{variant}_slab",
            f"worldofstone:polished_{variant}_wall",
        ]
        subs["tiles"] = [
            f"worldofstone:{variant}_tiles",
            f"worldofstone:cracked_{variant}_tiles",
            f"worldofstone:{variant}_tile_stairs",
            f"worldofstone:{variant}_tile_slab",
            f"worldofstone:{variant}_tile_wall",
        ]
        subs["pillar"] = [
            f"worldofstone:{variant}_pillar",
        ]
        subs["speleothem"] = [
            f"worldofstone:{variant}_speleothem",
            f"worldofstone:{variant}_sandstone_speleothem",
        ]
        return subs

    for variant in ALL_VARIANTS:
        subs = variant_subcategories(variant)
        for sub_name, ids in subs.items():
            write_wos_tag(f"{variant}/{sub_name}", ids)
        umbrella = [f"worldofstone:{variant}"]
        umbrella += [f"#worldofstone:{variant}/{sub_name}" for sub_name in subs.keys()]
        umbrella += [f"worldofstone:{name}" for name in (
            f"cracked_{variant}_bricks", f"chiseled_{variant}_bricks",
            f"infested_{variant}", f"infested_{variant}_cobblestone", f"infested_{variant}_bricks",
            f"infested_mossy_{variant}_bricks", f"infested_cracked_{variant}_bricks", f"infested_chiseled_{variant}_bricks",
            f"{variant}_button", f"{variant}_sand", f"{variant}_gravel", f"{variant}_clay", f"{variant}_glass", f"{variant}_glass_pane")]
        write_wos_tag(variant, umbrella)

    igneous_refs = [f"#worldofstone:{v}" for v in IGNEOUS]
    metamorphic_refs = [f"#worldofstone:{v}" for v in METAMORPHIC]
    sedimentary_refs = [f"#worldofstone:{v}" for v in SEDIMENTARY]
    write_wos_tag("igneous", igneous_refs)
    write_wos_tag("metamorphic", metamorphic_refs)
    write_wos_tag("sedimentary", sedimentary_refs)

    for host in ["stone", "granite", "diorite", "andesite", "tuff"]:
        overgrown_ids = [f"worldofstone:overgrown_{host}", f"worldofstone:snowed_{host}"]
        write_wos_tag(f"{host}/overgrown", overgrown_ids)
        write_wos_tag(host, [f"minecraft:{host}", f"#worldofstone:{host}/overgrown"])


def main():
    moss_dst = os.path.join(DST_BLOCK_TEX, "mossy_overlay.png")
    moss_extracted = extract_moss_overlay(moss_dst)
    if not moss_extracted:
        src_mossy = os.path.join(SRC_TEX, "mossy_overlay.png")
        if os.path.exists(src_mossy):
            safe_copyfile(src_mossy, moss_dst)

    brick_moss_dst = os.path.join(DST_BLOCK_TEX, "brick_mossy_overlay.png")
    brick_moss_extracted = extract_moss_diff("stone_bricks", "mossy_stone_bricks", brick_moss_dst)

    for variant in IGNEOUS + METAMORPHIC + SEDIMENTARY:
        copy_texture(variant, variant)
        variant_stone_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
        cobble_dst = os.path.join(DST_BLOCK_TEX, variant + "_cobblestone.png")
        bricks_dst = os.path.join(DST_BLOCK_TEX, variant + "_bricks.png")
        vanilla_cobble = fetch_vanilla("cobblestone")
        if not recolor_with_variant_palette(vanilla_cobble, variant_stone_path, cobble_dst, softness=0.3):
            modernize_from_ub(variant + "_cobble", "cobblestone", variant + "_cobblestone")
        vanilla_brick = fetch_vanilla("stone_bricks")
        if not recolor_with_variant_palette(vanilla_brick, variant_stone_path, bricks_dst):
            copy_texture(variant + "_brick", variant + "_bricks")
        if moss_extracted and os.path.exists(cobble_dst):
            composite_with_local_overlay(cobble_dst, moss_dst, os.path.join(DST_BLOCK_TEX, "mossy_" + variant + "_cobblestone.png"))
        else:
            composite_overlay(variant + "_cobble", os.path.join(SRC_TEX, "mossy_overlay.png"), "mossy_" + variant + "_cobblestone", tint=(70, 130, 50))
        if brick_moss_extracted and os.path.exists(bricks_dst):
            composite_with_local_overlay(bricks_dst, brick_moss_dst, os.path.join(DST_BLOCK_TEX, "mossy_" + variant + "_bricks.png"))
        elif moss_extracted and os.path.exists(bricks_dst):
            composite_with_local_overlay(bricks_dst, moss_dst, os.path.join(DST_BLOCK_TEX, "mossy_" + variant + "_bricks.png"))
        else:
            composite_overlay(variant + "_brick", os.path.join(SRC_TEX, "mossy_overlay.png"), "mossy_" + variant + "_bricks", tint=(70, 130, 50))
        write_basic_block(variant, variant, random_rot=True)
        write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{variant}.json"),
                   cobble_drops_loot(variant, variant + "_cobblestone"))
        write_basic_block(variant + "_cobblestone", variant + "_cobblestone")
        write_basic_block(variant + "_bricks", variant + "_bricks")
        write_basic_block("mossy_" + variant + "_cobblestone", "mossy_" + variant + "_cobblestone")
        write_basic_block("mossy_" + variant + "_bricks", "mossy_" + variant + "_bricks")
        cracked_name = "cracked_" + variant + "_bricks"
        write_cracked_brick_texture(variant)
        write_basic_block(cracked_name, cracked_name)
        write_recipe(cracked_name + "_from_smelting",
                     smelting_recipe("worldofstone:" + cracked_name,
                                     "worldofstone:" + variant + "_bricks", 0.1, 200))
        chiseled_name = "chiseled_" + variant + "_bricks"
        chiseled_dst = os.path.join(DST_BLOCK_TEX, chiseled_name + ".png")
        variant_stone_path = os.path.join(DST_BLOCK_TEX, variant + ".png")
        vanilla_chiseled = fetch_vanilla("chiseled_stone_bricks")
        recolor_with_variant_palette(vanilla_chiseled, variant_stone_path, chiseled_dst)
        write_basic_block(chiseled_name, chiseled_name)
        write_recipe(chiseled_name + "_from_stonecutting",
                     stonecutter_recipe("worldofstone:" + chiseled_name,
                                        "worldofstone:" + variant + "_bricks"))
        write_recipe(chiseled_name,
                     shaped_recipe("worldofstone:" + chiseled_name, 1, ["#", "#"],
                                   "worldofstone:" + variant + "_brick_slab"))
        write_infested_block(f"infested_{variant}", variant, lang_for(variant))
        write_infested_block(f"infested_{variant}_cobblestone", f"{variant}_cobblestone", lang_for(f"{variant}_cobblestone"), haunting=False)
        write_infested_block(f"infested_{variant}_bricks", f"{variant}_bricks", lang_for(f"{variant}_bricks"))
        write_infested_block(f"infested_mossy_{variant}_bricks", f"mossy_{variant}_bricks", lang_for(f"mossy_{variant}_bricks"))
        write_infested_block(f"infested_cracked_{variant}_bricks", f"cracked_{variant}_bricks", lang_for(f"cracked_{variant}_bricks"))
        write_infested_block(f"infested_chiseled_{variant}_bricks", f"chiseled_{variant}_bricks", lang_for(f"chiseled_{variant}_bricks"))
        write_stairs(variant + "_cobblestone_stairs", variant + "_cobblestone", variant + "_cobblestone")
        write_slab(variant + "_cobblestone_slab", variant + "_cobblestone", variant + "_cobblestone")
        write_wall(variant + "_cobblestone_wall", variant + "_cobblestone")
        write_stairs("mossy_" + variant + "_cobblestone_stairs", "mossy_" + variant + "_cobblestone", "mossy_" + variant + "_cobblestone")
        write_slab("mossy_" + variant + "_cobblestone_slab", "mossy_" + variant + "_cobblestone", "mossy_" + variant + "_cobblestone")
        write_wall("mossy_" + variant + "_cobblestone_wall", "mossy_" + variant + "_cobblestone")
        write_stairs(variant + "_brick_stairs", variant + "_bricks", variant + "_brick")
        write_slab(variant + "_brick_slab", variant + "_bricks", variant + "_bricks")
        write_wall(variant + "_brick_wall", variant + "_bricks")
        write_stairs("mossy_" + variant + "_brick_stairs", "mossy_" + variant + "_bricks", "mossy_" + variant + "_brick")
        write_slab("mossy_" + variant + "_brick_slab", "mossy_" + variant + "_bricks", "mossy_" + variant + "_bricks")
        write_wall("mossy_" + variant + "_brick_wall", "mossy_" + variant + "_bricks")

    fossil_drop_variants = {"chalk", "limestone", "dolomite", "shale"}
    for variant in SEDIMENTARY:
        if variant == "lignite":
            write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{variant}.json"),
                       lignite_drops_loot(variant, "worldofstone:lignite_coal"))
        elif variant in fossil_drop_variants:
            write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{variant}.json"),
                       cobble_drops_loot(variant, variant + "_cobblestone", fossil_chance=0.05))

    write_glass_pane_models()
    for variant in ALL_VARIANTS:
        write_sand(variant)
        write_glass(variant)
        write_sandstone_set(variant)
        write_gravel(variant)
        write_clay(variant)
        write_button_set(variant)
        write_speleothem_set(variant)
        write_sandstone_speleothem_set(variant)
        write_overgrown_set(variant)
        write_smooth_stone_set(variant)
        write_polished_set(variant)
        write_tile_set(variant)
        write_pillar_set(variant)

    for variant in ALL_VARIANTS:
        for ore in ORES:
            ore_name = f"{variant}_{ore['name']}"
            write_ore_block(ore_name, variant, ore)

    for host in ["stone", "granite", "diorite", "andesite", "tuff"]:
        write_overgrown_vanilla_set(host)

    for host in VANILLA_ORE_HOSTS:
        host_path = fetch_vanilla(HOST_TEXTURE_OVERRIDES.get(host, host))
        for ore in ORES:
            ore_name = f"{host}_{ore['name']}"
            write_ore_block(ore_name, host, ore, host_texture_path=host_path,
                            tier_override="diamond" if host == "obsidian" else None)
            VANILLA_HOST_ORE_BLOCKS.append(ore_name)
            host_display = host.replace("_", " ").title()
            ore_display = ore["name"].replace("_", " ").title()
            LANG[f"block.worldofstone.{ore_name}"] = f"{host_display} {ore_display}"

    for name, tex_ref, display in VANILLA_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in QUARK_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in UNDERGARDEN_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in CREATE_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in BETTEREND_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in GALOSPHERE_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in BWG_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in TWILIGHTFOREST_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in AETHER_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in BLUE_SKIES_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in SPELUNKERY_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in ICEANDFIRE_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in MYSTICALAGRICULTURE_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in BIOMESOPLENTY_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in FORBIDDEN_ARCANUS_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in ALEXSCAVES_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in ARS_NOUVEAU_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in CATACLYSM_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in TWIGS_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in ARCHITECTS_PALETTE_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in OUTER_END_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in BOTANIA_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in AD_ASTRA_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in DEEP_AETHER_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in CAVERNS_AND_CHASMS_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in ATMOSPHERIC_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in ENDERGETIC_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in WILDER_WILDS_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in REGIONS_UNEXPLORED_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in BORN_IN_CHAOS_V1_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in NATURALIST_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in YUNGSCAVEBIOMES_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in NATURES_SPIRIT_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in NETHEREXP_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in DEEPERDARKER_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in THE_DEEP_VOID_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in DEFILED_LANDS_PREBORN_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    for name, tex_ref, display in TERRACOTTA_SPELEOTHEMS:
        write_speleothem_block(name, tex_ref, display)
    wood_speleothem_keywords = ("_log_", "_stem_", "_planks_", "_bamboo_", "bamboo_", "bamboo_mosaic_")
    log_burn_keywords = ("_log_speleothem", "stripped_oak", "stripped_spruce", "stripped_birch",
                         "stripped_jungle", "stripped_acacia", "stripped_dark_oak",
                         "stripped_mangrove", "stripped_cherry")
    for name, tex_ref, display, unbreakable, tier, tool in DECORATIVE_SPELEOTHEMS:
        effective_tool = tool
        if any(kw in name for kw in ("_log_speleothem", "_stem_speleothem", "_planks_speleothem",
                                     "bamboo_speleothem", "stripped_bamboo_speleothem",
                                     "bamboo_mosaic_speleothem")):
            effective_tool = "axe"
        write_speleothem_block(name, tex_ref, display, unbreakable=unbreakable, tier=tier, tool=effective_tool, drop_self=(name in DROP_SELF_NAMES))
        if "_log_speleothem" in name:
            LOGS_THAT_BURN_BLOCKS.append(name)
        if name in ("bedrock_speleothem", "end_portal_frame_speleothem"):
            WITHER_IMMUNE_BLOCKS.append(name)

    dev_name = "dev_speleothem"
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{dev_name}.json"), speleothem_blockstate(dev_name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{dev_name}_small.json"), speleothem_model(f"worldofstone:block/{dev_name}", 7, 9))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{dev_name}_medium.json"), speleothem_model(f"worldofstone:block/{dev_name}", 6, 10))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{dev_name}_large.json"), speleothem_model(f"worldofstone:block/{dev_name}", 4, 12))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{dev_name}.json"),
               {"parent": f"worldofstone:block/{dev_name}_medium"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{dev_name}.json"), silk_only_loot(dev_name))
    PICKAXE_BLOCKS.append(dev_name)
    LANG[f"block.worldofstone.{dev_name}"] = "Dev Speleothem"

    mimic_name = "mimic_speleothem"
    mimic_tex_path = os.path.join(DST_BLOCK_TEX, mimic_name + ".png")
    vanilla_stone = fetch_vanilla("stone")
    if vanilla_stone:
        gray = Image.open(vanilla_stone).convert("RGBA")
        gpix = gray.load()
        for y in range(gray.size[1]):
            for x in range(gray.size[0]):
                r, g, b, a = gpix[x, y]
                lum = int(0.299 * r + 0.587 * g + 0.114 * b)
                gpix[x, y] = (lum, lum, lum, a)
        os.makedirs(os.path.dirname(mimic_tex_path), exist_ok=True)
        gray.save(mimic_tex_path)
    else:
        flat = Image.new("RGBA", (16, 16), (200, 200, 200, 255))
        flat.save(mimic_tex_path)
    write_json(os.path.join(DST_ASSETS, "blockstates", f"{mimic_name}.json"), speleothem_blockstate(mimic_name))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{mimic_name}_small.json"), speleothem_model(f"worldofstone:block/{mimic_name}", 7, 9))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{mimic_name}_medium.json"), speleothem_model(f"worldofstone:block/{mimic_name}", 6, 10))
    write_json(os.path.join(DST_ASSETS, "models", "block", f"{mimic_name}_large.json"), speleothem_model(f"worldofstone:block/{mimic_name}", 4, 12))
    write_json(os.path.join(DST_ASSETS, "models", "item", f"{mimic_name}.json"),
               {"parent": f"worldofstone:block/{mimic_name}_medium"})
    write_json(os.path.join(DST_DATA, "loot_tables", "blocks", f"{mimic_name}.json"), {
        "type": "minecraft:block",
        "pools": [{
            "rolls": 1.0,
            "bonus_rolls": 0.0,
            "entries": [{
                "type": "minecraft:item",
                "name": f"worldofstone:{mimic_name}",
                "functions": [{
                    "function": "minecraft:copy_nbt",
                    "source": "block_entity",
                    "ops": [{"source": "Source", "target": "BlockEntityTag.Source", "op": "replace"}]
                }],
                "conditions": [{
                    "condition": "minecraft:match_tool",
                    "predicate": {"enchantments": [{"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}]}
                }]
            }]
        }]
    })
    PICKAXE_BLOCKS.append(mimic_name)
    LANG[f"block.worldofstone.{mimic_name}"] = "Mimic Speleothem"

    src_side_overlay = os.path.join(SRC_OVERLAY_DIR, "overgrown_stone_side_overlay.png")
    if os.path.exists(src_side_overlay):
        safe_copyfile(src_side_overlay, os.path.join(DST_BLOCK_TEX, "ub_grass_side_overlay.png"))

    generate_synthetic_grass_top(os.path.join(DST_BLOCK_TEX, "ub_grass_top.png"))
    generate_synthetic_snow_top(os.path.join(DST_BLOCK_TEX, "ub_snow_top.png"))

    os.makedirs(DST_ITEM_TEX, exist_ok=True)
    for fossil in FOSSILS:
        src_name = FOSSIL_TEXTURE_SOURCE.get(fossil, fossil)
        src = os.path.join(SRC_ITEM_TEX, f"{src_name}.png")
        if os.path.exists(src):
            safe_copyfile(src, os.path.join(DST_ITEM_TEX, f"{fossil}.png"))
        write_simple_item(fossil, fossil)

    src_lc = os.path.join(SRC_ITEM_TEX, "lignite_coal.png")
    if os.path.exists(src_lc):
        safe_copyfile(src_lc, os.path.join(DST_ITEM_TEX, "lignite_coal.png"))
    write_simple_item("lignite_coal", "lignite_coal")

    LANG["itemGroup.worldofstone"] = "World of Stone"
    write_json(os.path.join(DST_ASSETS, "lang", "en_us.json"), LANG)

    write_json(os.path.join(DST_MC_TAGS, "mineable", "pickaxe.json"), {
        "replace": False,
        "values": safe_tag_values([f"worldofstone:{n}" for n in PICKAXE_BLOCKS])
    })
    write_json(os.path.join(DST_MC_TAGS, "mineable", "shovel.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in SHOVEL_BLOCKS]
    })
    write_json(os.path.join(DST_MC_TAGS, "mineable", "axe.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in AXE_BLOCKS]
    })
    write_json(os.path.join(DST_MC_TAGS, "mineable", "hoe.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in HOE_BLOCKS]
    })
    write_json(os.path.join(DST_MC_TAGS, "logs_that_burn.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in LOGS_THAT_BURN_BLOCKS]
    })
    write_json(os.path.join(DST_MC_TAGS, "wither_immune.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in WITHER_IMMUNE_BLOCKS]
    })
    write_json(os.path.join(DST_MC_TAGS, "walls.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in WALL_BLOCKS]
    })
    write_json(os.path.join(DST_MC_TAGS, "needs_stone_tool.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in NEEDS_STONE_TOOL]
    })
    write_json(os.path.join(DST_MC_TAGS, "needs_iron_tool.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in NEEDS_IRON_TOOL]
    })
    write_json(os.path.join(DST_MC_TAGS, "needs_diamond_tool.json"), {
        "replace": False,
        "values": [f"worldofstone:{n}" for n in NEEDS_DIAMOND_TOOL]
    })

    cobble_brick = IGNEOUS + METAMORPHIC + SEDIMENTARY
    cobblestones = [f"worldofstone:{v}_cobblestone" for v in cobble_brick]
    cobblestones += [f"worldofstone:mossy_{v}_cobblestone" for v in cobble_brick]
    stones = [f"worldofstone:{v}" for v in ALL_VARIANTS]
    sandstones = [f"worldofstone:{v}_sandstone" for v in ALL_VARIANTS]
    sandstones += [f"worldofstone:cut_{v}_sandstone" for v in ALL_VARIANTS]
    sandstones += [f"worldofstone:chiseled_{v}_sandstone" for v in ALL_VARIANTS]
    sandstones += [f"worldofstone:smooth_{v}_sandstone" for v in ALL_VARIANTS]

    write_json(os.path.join(DST_MC_ITEM_TAGS, "stone_tool_materials.json"), {
        "replace": False,
        "values": cobblestones
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "stone_crafting_materials.json"), {
        "replace": False,
        "values": cobblestones
    })
    write_json(os.path.join(DST_MC_TAGS, "base_stone_overworld.json"), {
        "replace": False,
        "values": stones
    })
    write_json(os.path.join(DST_FORGE_TAGS, "stones.json"), {
        "replace": False,
        "values": stones
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "stones.json"), {
        "replace": False,
        "values": stones
    })
    write_json(os.path.join(DST_FORGE_TAGS, "cobblestone.json"), {
        "replace": False,
        "values": cobblestones
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "cobblestone.json"), {
        "replace": False,
        "values": cobblestones
    })
    write_json(os.path.join(DST_FORGE_TAGS, "sandstone.json"), {
        "replace": False,
        "values": sandstones
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "sandstone.json"), {
        "replace": False,
        "values": sandstones
    })

    sand_ids = [f"worldofstone:{n}" for n in SAND_BLOCKS]
    gravel_ids = [f"worldofstone:{n}" for n in GRAVEL_BLOCKS]
    stair_ids = [f"worldofstone:{n}" for n in STAIR_BLOCKS]
    slab_ids = [f"worldofstone:{n}" for n in SLAB_BLOCKS]
    button_ids = [f"worldofstone:{n}" for n in BUTTON_BLOCKS]
    wall_ids = [f"worldofstone:{n}" for n in WALL_BLOCKS]
    brick_ids = [f"worldofstone:{v}_bricks" for v in IGNEOUS + METAMORPHIC + SEDIMENTARY]
    brick_ids += [f"worldofstone:mossy_{v}_bricks" for v in IGNEOUS + METAMORPHIC + SEDIMENTARY]
    all_ore_ids = []
    for ids in ORE_BLOCKS_BY_TYPE.values():
        all_ore_ids.extend(f"worldofstone:{n}" for n in ids)

    write_recipe("glass_from_strata_sand", {
        "type": "minecraft:smelting",
        "ingredient": [{"item": i} for i in sand_ids],
        "result": "minecraft:glass",
        "experience": 0.1,
        "cookingtime": 200
    })
    glass_ids = [f"worldofstone:{v}_glass" for v in ALL_VARIANTS]
    pane_ids = [f"worldofstone:{v}_glass_pane" for v in ALL_VARIANTS]
    write_wos_tag("strata_glass", glass_ids)
    for tag_dir in (DST_FORGE_TAGS, DST_FORGE_ITEM_TAGS):
        for path, ids in (("glass", glass_ids), ("glass/silica", glass_ids), ("glass_panes", pane_ids)):
            write_json(os.path.join(tag_dir, f"{path}.json"), {"replace": False, "values": ids})
    for result, pattern, key, count in (
            ("beacon", ["GGG", "GSG", "OOO"], {"O": "minecraft:obsidian", "S": "minecraft:nether_star"}, 1),
            ("daylight_detector", ["GGG", "QQQ", "WWW"], {"Q": "minecraft:quartz", "W": "#minecraft:wooden_slabs"}, 1),
            ("end_crystal", ["GGG", "GEG", "GTG"], {"E": "minecraft:ender_eye", "T": "minecraft:ghast_tear"}, 1),
            ("glass_bottle", ["G G", " G "], {}, 3),
            ("tinted_glass", [" S ", "SGS", " S "], {"S": "minecraft:amethyst_shard"}, 2)):
        keys = {"G": {"tag": "worldofstone:strata_glass"}}
        keys.update({k: {"tag": v[1:]} if v.startswith("#") else {"item": v} for k, v in key.items()})
        write_recipe(f"{result}_from_strata_glass", {
            "type": "minecraft:crafting_shaped",
            "pattern": pattern,
            "key": keys,
            "result": {"item": f"minecraft:{result}", "count": count}
        })
    write_json(os.path.join(DST_MC_TAGS, "sand.json"), {
        "replace": False,
        "values": sand_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "sand.json"), {
        "replace": False,
        "values": sand_ids
    })
    write_json(os.path.join(DST_MC_TAGS, "stairs.json"), {
        "replace": False,
        "values": stair_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "stairs.json"), {
        "replace": False,
        "values": stair_ids
    })
    write_json(os.path.join(DST_MC_TAGS, "slabs.json"), {
        "replace": False,
        "values": slab_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "slabs.json"), {
        "replace": False,
        "values": slab_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "walls.json"), {
        "replace": False,
        "values": wall_ids
    })
    write_json(os.path.join(DST_MC_TAGS, "buttons.json"), {
        "replace": False,
        "values": button_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "buttons.json"), {
        "replace": False,
        "values": button_ids
    })
    write_json(os.path.join(DST_MC_TAGS, "stone_buttons.json"), {
        "replace": False,
        "values": button_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "stone_buttons.json"), {
        "replace": False,
        "values": button_ids
    })
    write_json(os.path.join(DST_MC_TAGS, "stone_bricks.json"), {
        "replace": False,
        "values": brick_ids
    })
    write_json(os.path.join(DST_MC_ITEM_TAGS, "stone_bricks.json"), {
        "replace": False,
        "values": brick_ids
    })

    write_json(os.path.join(DST_FORGE_TAGS, "gravel.json"), {
        "replace": False,
        "values": gravel_ids
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "gravel.json"), {
        "replace": False,
        "values": gravel_ids
    })
    write_json(os.path.join(DST_FORGE_TAGS, "sand.json"), {
        "replace": False,
        "values": sand_ids
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "sand.json"), {
        "replace": False,
        "values": sand_ids
    })
    write_json(os.path.join(DST_FORGE_TAGS, "ores.json"), {
        "replace": False,
        "values": all_ore_ids
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "ores.json"), {
        "replace": False,
        "values": all_ore_ids
    })
    for ore_type, ids in ORE_BLOCKS_BY_TYPE.items():
        ids_full = [f"worldofstone:{n}" for n in ids]
        write_json(os.path.join(DST_FORGE_TAGS, "ores", f"{ore_type}.json"), {
            "replace": False,
            "values": ids_full
        })
        write_json(os.path.join(DST_FORGE_ITEM_TAGS, "ores", f"{ore_type}.json"), {
            "replace": False,
            "values": ids_full
        })
        write_json(os.path.join(DST_MC_TAGS, f"{ore_type}_ores.json"), {
            "replace": False,
            "values": ids_full
        })
        write_json(os.path.join(DST_MC_ITEM_TAGS, f"{ore_type}_ores.json"), {
            "replace": False,
            "values": ids_full
        })

    rate_buckets = {}
    for ore in ORES:
        rate = ore.get("rate")
        if not rate:
            continue
        ore_type = ore["name"].replace("_ore", "")
        rate_buckets.setdefault(rate, []).extend(
            f"worldofstone:{n}" for n in ORE_BLOCKS_BY_TYPE.get(ore_type, [])
        )
    for rate, ids in rate_buckets.items():
        write_json(os.path.join(DST_FORGE_TAGS, "ore_rates", f"{rate}.json"), {
            "replace": False,
            "values": ids
        })
        write_json(os.path.join(DST_FORGE_ITEM_TAGS, "ore_rates", f"{rate}.json"), {
            "replace": False,
            "values": ids
        })

    write_json(os.path.join(DST_FORGE_TAGS, "ores_in_ground", "stone.json"), {
        "replace": False,
        "values": all_ore_ids
    })
    write_json(os.path.join(DST_FORGE_ITEM_TAGS, "ores_in_ground", "stone.json"), {
        "replace": False,
        "values": all_ore_ids
    })

    for fossil in FOSSILS:
        write_json(os.path.join(DST_DATA, "recipes", f"{fossil}_to_bone_meal.json"), {
            "type": "minecraft:crafting_shapeless",
            "ingredients": [{"item": f"worldofstone:{fossil}"}],
            "result": {"item": "minecraft:bone_meal", "count": 4}
        })

    write_stone_variant_recipes()

    write_world_of_stone_tags()

    write_recipe("blast_furnace_from_worldofstone_smooth", {
        "type": "minecraft:crafting_shaped",
        "key": {
            "I": {"item": "minecraft:iron_ingot"},
            "X": {"tag": "worldofstone:smooth_stones"},
            "#": {"item": "minecraft:furnace"}
        },
        "pattern": [
            "III",
            "X#X",
            "XXX"
        ],
        "result": {"item": "minecraft:blast_furnace"}
    })

    tints = {}
    for v in ALL_VARIANTS:
        with Image.open(os.path.join(DST_BLOCK_TEX, f"{v}_sand.png")) as sand:
            r, g, b = sand.convert("RGB").resize((1, 1), Image.BOX).getpixel((0, 0))
        tints[f"{v}_glass"] = tints[f"{v}_glass_pane"] = {"r": r, "g": g, "b": b}
    write_json(os.path.join(COMMON_RES, "data", "worldofstone", "sand_tints.json"), tints)

    print(f"Generated {len(LANG)} language entries, {len(PICKAXE_BLOCKS)} pickaxe + {len(SHOVEL_BLOCKS)} shovel tagged blocks, {len(SPELEOTHEM_BLOCKS)} speleothems, {len(VANILLA_HOST_ORE_BLOCKS)} vanilla-host ores, {len(SMOOTH_STONE_BLOCKS)} smooth stones")


if __name__ == "__main__":
    main()
