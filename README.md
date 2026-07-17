<div align="center">

<img src="https://raw.githubusercontent.com/Reijin2312/BCLib-New-Dawn/master/bclib_banner.png" alt="BCLib: New Dawn" width="100%">

# BCLib: New Dawn

**An independently maintained continuation of BCLib for modern Minecraft versions**

[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1422283?logo=curseforge\&label=CurseForge%20Downloads\&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge)
[![GitHub Issues](https://img.shields.io/github/issues/Reijin2312/BCLib-New-Dawn?logo=github)](https://github.com/Reijin2312/BCLib-New-Dawn/issues)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1%20%7C%201.21.11%20%7C%2026.1.x-62B47A)](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge/files)
[![Loaders](https://img.shields.io/badge/Loaders-NeoForge%20%7C%20Fabric-5C6BC0)](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge)
[![Code License](https://img.shields.io/badge/Code-MIT-blue.svg)](LICENSE)

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge)
·
[Downloads](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge/files)
·
[Source](https://github.com/Reijin2312/BCLib-New-Dawn)
·
[Issues](https://github.com/Reijin2312/BCLib-New-Dawn/issues)
·
[Discord](https://discord.gg/BHxhJSn5uR)

</div>

---

## About

**BCLib: New Dawn** is an independently maintained, unofficial continuation of BCLib for Fabric and NeoForge.

BCLib is a shared library used by BetterX mods and other compatible projects. It provides reusable APIs, utilities, configuration systems, rendering features, world-generation helpers, mathematical libraries, material builders, and compatibility functionality.

The Fabric edition continues maintenance of the original Fabric codebase with bug fixes and compatibility improvements.

The NeoForge edition ports the same core functionality to NeoForge with the loader-specific changes required for compatibility.

> [!NOTE]
> BCLib is primarily a developer-facing library. It does not add significant standalone gameplay content and normally needs to be installed only when another mod lists it as a required dependency.

> [!IMPORTANT]
> BCLib: New Dawn is not maintained or endorsed by the original BCLib developers or the BetterX Team.
>
> Please do not report New Dawn-specific issues to the upstream BCLib repository or the official BetterX support channels. Use the [New Dawn issue tracker](https://github.com/Reijin2312/BCLib-New-Dawn/issues) instead.

---

## Supported versions

| Minecraft version | Mod loader | Current version | Source branch                                                                      |
| ----------------- | ---------- | --------------: | ---------------------------------------------------------------------------------- |
| 1.21.1            | NeoForge   |       `21.0.22` | [`master`](https://github.com/Reijin2312/BCLib-New-Dawn/tree/master)               |
| 1.21.1            | Fabric     |       `21.0.15` | [`fabric-1.21.1`](https://github.com/Reijin2312/BCLib-New-Dawn/tree/fabric-1.21.1) |
| 1.21.11           | NeoForge   |       `21.11.2` | [`port/1.21.11`](https://github.com/Reijin2312/BCLib-New-Dawn/tree/port/1.21.11)   |
| 26.1–26.1.2       | NeoForge   |        `26.1.2` | [`26.1`](https://github.com/Reijin2312/BCLib-New-Dawn/tree/26.1)                   |

Always check the [CurseForge files page](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge/files) and download the correct file for your Minecraft version and mod loader.

---

## Features

### Rendering

* Emissive block textures using the `_e` suffix
* Resource-pack support for emissive textures
* Procedural block and item models
* Custom block-rendering interfaces
* Custom block and item color providers
* Configurable transparent and translucent render layers

### APIs

* Mod integration API
* Structure and feature registration helpers
* World-data migration and fixer utilities
* Block-name migration support
* Bonemeal and plant-growth APIs
* Biome wrappers and custom biome data
* Custom fog-density support
* Runtime tag utilities
* Block and item registration helpers
* Data-generation utilities

### Mathematical and generation libraries

* Signed Distance Function implementations
* SDF primitives and operations
* SDF-based feature generation
* Block post-processing
* Spline creation and manipulation
* Voronoi noise
* OpenSimplex noise
* Common mathematical utilities
* Weighted lists and weighted trees
* Custom surface-building helpers

### Materials and content utilities

* Shared material builders
* Wood and stone block-set generation
* Automatic block and item model generation
* Automatic recipes and tags
* Pattern-based block and item assets
* Predefined common Minecraft block types
* Utility classes for generating large content sets

### Configuration

* JSON-based configuration system
* Hierarchical configuration files
* Multiple configuration entry types
* Saving only values changed by the user
* Client configuration integration
* Mod Menu integration on Fabric

### Additional utilities

* Translation-template generation
* Block manipulation helpers
* Recipe registration utilities
* Runtime integration helpers
* Client and server initialization interfaces
* Common functionality shared by BetterX mods

---

## New Dawn improvements

BCLib: New Dawn aims to preserve the behavior and APIs of the original project while providing continued maintenance and support for modern mod loaders.

Current goals and improvements include:

* continued maintenance of the Fabric edition;
* support for modern NeoForge versions;
* support for newer Minecraft releases;
* shared fixes across supported loaders;
* loader-specific compatibility changes;
* integration with WorldWeaver: New Dawn;
* integration with WunderLib: New Dawn;
* improved compatibility between New Dawn ecosystem projects;
* consistent functionality across supported mod loaders;
* maintenance of APIs required by BetterEnd: New Dawn and BetterNether: New Dawn.

---

## Download and installation

Download BCLib: New Dawn from the official CurseForge page:

### [Download from CurseForge](https://www.curseforge.com/minecraft/mc-mods/bclib-neoforge/files)

1. Install a supported Minecraft version.
2. Install the appropriate NeoForge or Fabric loader.
3. Download the BCLib: New Dawn file matching your loader and Minecraft version.
4. Install the required dependencies shown on the selected CurseForge file page.
5. Place all downloaded `.jar` files in your Minecraft `mods` directory.
6. Launch the game.

Make sure the Minecraft version, mod loader, BCLib version, and dependency versions all match.

BCLib usually needs to be installed only when another mod lists it as a required dependency.

---

## Required dependencies

Requirements vary between Minecraft versions and mod loaders. Always check the dependency list of the selected CurseForge file.

### NeoForge

* [WorldWeaver: New Dawn](https://www.curseforge.com/minecraft/mc-mods/worldweaver-neoforge)
* [WunderLib: New Dawn](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge)

### Fabric

* [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
* [WorldWeaver: New Dawn](https://www.curseforge.com/minecraft/mc-mods/worldweaver-neoforge)
* [WunderLib: New Dawn](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge)

### Optional on Fabric

* [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)

---

## Using BCLib in another mod

Add the BetterX Maven repository to your Gradle configuration:

```groovy
repositories {
    maven {
        url = "https://maven.ambertation.de/releases"
    }
}
```

Define the BCLib version in `gradle.properties`:

```properties
bclib_version=21.0.22
```

Use the BCLib version matching your Minecraft version and mod loader.

### NeoForge

Add BCLib to your dependencies:

```groovy
dependencies {
    implementation "org.betterx:bclib:${bclib_version}"
}
```

Add BCLib to `META-INF/neoforge.mods.toml`:

```toml
[[dependencies.your_mod_id]]
modId="bclib"
mandatory=true
versionRange="[21.0,)"
ordering="NONE"
side="BOTH"
```

Replace `your_mod_id` with your mod ID and adjust the version range for the BCLib release against which your project was built.

### Fabric

Add BCLib to your dependencies:

```groovy
dependencies {
    modImplementation "org.betterx:bclib:${bclib_version}"
}
```

Add BCLib to the `depends` section of `fabric.mod.json`:

```json
{
  "depends": {
    "bclib": "21.0.x"
  }
}
```

Also declare compatible versions of WorldWeaver, WunderLib, Fabric API, Minecraft, Java, and Fabric Loader where required.

---

## Development setup

Clone the repository:

```bash
git clone https://github.com/Reijin2312/BCLib-New-Dawn.git
cd BCLib-New-Dawn
```

Select the branch for the version and loader you want to work on:

```bash
# NeoForge 1.21.1
git checkout master

# Fabric 1.21.1
git checkout fabric-1.21.1

# NeoForge 1.21.11
git checkout port/1.21.11

# NeoForge 26.1.x
git checkout 26.1
```

Import the project into IntelliJ IDEA or another Gradle-compatible IDE.

Use the Java version required by the selected Minecraft version and branch.

---

## Local New Dawn dependencies

BCLib can use local versions of WorldWeaver and WunderLib during development.

A local development directory can look like this:

```text
projects/
├── BCLib-New-Dawn/
├── WorldWeaver-New-Dawn/
└── WunderLib-New-Dawn/
```

Local dependency inclusion is controlled through the Gradle settings and helper scripts included in the repository.

When local dependency sources are unavailable or disabled, the project uses the configured binary dependencies instead.

---

## Building

Run:

```bash
./gradlew build
```

On Windows:

```bat
gradlew.bat build
```

The compiled mod files will be available in:

```text
build/libs
```

---

## API stability

BCLib is under active development.

Developers should be aware that:

* APIs may change between major Minecraft versions;
* compatibility cannot be assumed across different Minecraft versions;
* Fabric and NeoForge implementations may require loader-specific code;
* experimental APIs may receive breaking changes;
* development or beta releases should be tested before use in production modpacks;
* mods should declare an explicit compatible BCLib version range.

---

## Reporting issues

Before opening an issue:

1. Make sure you are using the latest available BCLib version.
2. Verify that you downloaded the correct file for your Minecraft version and mod loader.
3. Check that WorldWeaver, WunderLib, and Fabric API are installed where required.
4. Confirm that all dependency versions match your BCLib version.
5. Test without unrelated mods when possible.
6. Include the latest log or crash report.
7. Include a complete mod list.
8. Specify whether you are using Fabric or NeoForge.
9. Provide clear steps to reproduce the problem.

Report bugs through the official New Dawn issue tracker:

### [Open an issue](https://github.com/Reijin2312/BCLib-New-Dawn/issues)

Do not report New Dawn-specific bugs to the original BCLib developers or the BetterX Team.

---

## Contributing

Contributions are welcome.

You can help by:

* reporting and reproducing bugs;
* submitting fixes;
* improving Fabric or NeoForge compatibility;
* improving API consistency;
* improving WorldWeaver and WunderLib integration;
* testing new Minecraft versions;
* improving performance;
* adding documentation;
* updating translations;
* improving developer examples.

Please clearly explain your changes and test them before submitting a pull request.

---

## Maintainer

BCLib: New Dawn is maintained by **Raijin**.

* [CurseForge profile: Raijin2312](https://www.curseforge.com/members/raijin2312/projects)
* [GitHub profile: Reijin2312](https://github.com/Reijin2312)
* [New Dawn Discord](https://discord.gg/BHxhJSn5uR)

---

## Credits and attribution

* BCLib was originally created by **Paulevs** and its contributors.
* The project was subsequently developed and maintained by **Quiqueck**, **Bulldog83**, and other BetterX contributors.
* The upstream BCLib source is maintained at [`quiqueck/BCLib`](https://github.com/quiqueck/BCLib).
* The original BCLib project is available on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bclib).
* Original artwork and the majority of the original code belong to their respective developers and the BetterX Team.
* BCLib: New Dawn is an independent, unofficial continuation maintained by Raijin.
* Special thanks to everyone who reports issues, contributes fixes, improves compatibility, writes documentation, and tests releases.

---

## License

The project source code is distributed under the [MIT License](LICENSE).

Copyright:

* © 2020 paulevsGitch
* © 2026 Raijin

Selected project assets are distributed under the terms described in [`LICENSE.ASSETS`](LICENSE.ASSETS).

Those assets use the **CC BY-NC-SA 4.0** license and require attribution to **Team BetterX**.

See both license files before modifying or redistributing the project or its assets.
