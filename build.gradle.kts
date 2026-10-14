plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

val minecraft = stonecutter.current.version

version = "${mod.version}+$minecraft"
base {
    archivesName.set("${mod.id}-common")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.project.prop("loom.platform")
})

repositories {
    maven("https://maven.architectury.dev/")
    maven("https://maven.blamejared.com")
    maven("https://cursemaven.com") { content { includeGroup("curse.maven") } }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")

    mappings(loom.officialMojangMappings())

    modImplementation("dev.architectury:architectury:${mod.dep("architectury_api")}")
    modCompileOnly("mezz.jei:jei-$minecraft-common-api:${mod.dep("jei")}")
    compileOnly("curse.maven:just-enough-resources-jer-240630:${mod.dep("jer")}")
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}
