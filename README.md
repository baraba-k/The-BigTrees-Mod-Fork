# The BigTrees Mod (NeoForge 1.21.1 Fork)

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.121-orange.svg)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Version](https://img.shields.io/badge/Version-0.0.1-blueviolet.svg)](https://github.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A modern, highly optimized fork of the classic **BigTrees** mod (originally created by Karob & ThorinStonehelm for Minecraft 1.7.10), fully ported to **Minecraft 1.21.1** using **NeoForge**.

This fork preserves 100% of the original procedural tree algorithms, biome locality mechanics, and multi-block growth configurations while completely modernizing the engine, eliminating world generation lag, and adding quality-of-life enhancements.

---

## ✨ Features

- **9 Procedural Tree Types:**
  - **Great Oak**: Colossal multi-branch oaks with taproots and spherical foliage clusters.
  - **Block Oak**: Dense, massive geometric oaks.
  - **Swamp Oak**: Stilt-rooted canopy oaks adapted to shallow water and wetlands.
  - **Tall Oak**: Towering trunks with curling, dynamic branches.
  - **Big Birch**: Majestic, tall white-bark trees with multi-tier leaf layers.
  - **Big Pine (Spruce)**: Grand evergreen conifers with sprawling branch rings.
  - **Cyprus Tree**: Tall column trunks with dense tops and water-tolerant roots.
  - **Hat Tree**: Mushroom-shaped umbrella canopy with hanging vines.
  - **Dead Tree**: Gnarled, leafless structures suited for deserts and badlands.
- **Natural World Generation:**
  - Procedural biome noise & locality clustering (`#minecraft:is_overworld`).
  - Seamless integration via NeoForge Biome Modifiers.
- **High Performance & Optimization:**
  - Thread-safe world generation with `ConcurrentHashMap` caching.
  - Lazy evaluation of heightmaps to prevent chunk loading lag and stutter.
  - Zero client/server desync.
- **Full Bonemeal & Sapling Support:**
  - All saplings support both **1×1 standalone planting** and **multi-block geometric structures** (2×2, 3×3, 4×4).

---

## 🛠️ Crafting Recipes

All BigTrees saplings can be crafted using a standard 3×3 crafting table:

### 1. Big Oak Sapling (`bt_big_oak_sapling`)
Vertical column of an Oak Sapling on top of 2 Oak Logs:

| Slot | Item |
|:---:|:---|
| **Top** | `minecraft:oak_sapling` |
| **Middle** | `minecraft:oak_log` |
| **Bottom** | `minecraft:oak_log` |

```
[       ] [ Oak Sapling ] [       ]
[       ] [   Oak Log   ] [       ]  ==>  1x Big Oak Sapling
[       ] [   Oak Log   ] [       ]
```

---

### 2. Big Birch Sapling (`bt_big_birch_sapling`)
Vertical column of a Birch Sapling on top of 2 Birch Logs:

| Slot | Item |
|:---:|:---|
| **Top** | `minecraft:birch_sapling` |
| **Middle** | `minecraft:birch_log` |
| **Bottom** | `minecraft:birch_log` |

```
[       ] [ Birch Sapling ] [       ]
[       ] [   Birch Log   ] [       ]  ==>  1x Big Birch Sapling
[       ] [   Birch Log   ] [       ]
```

---

### 3. Big Pine Sapling (`bt_big_pine_sapling`)
Vertical column of a Spruce Sapling on top of 2 Spruce Logs:

| Slot | Item |
|:---:|:---|
| **Top** | `minecraft:spruce_sapling` |
| **Middle** | `minecraft:spruce_log` |
| **Bottom** | `minecraft:spruce_log` |

```
[       ] [ Spruce Sapling ] [       ]
[       ] [   Spruce Log   ] [       ]  ==>  1x Big Pine Sapling
[       ] [   Spruce Log   ] [       ]
```

---

### 4. Cyprus Sapling (`bt_cyprus_sapling`)

| Slot | Item |
|:---:|:---|
| **Row 1** | `Air`, `minecraft:spruce_sapling`, `Air` |
| **Row 2** | `Air`, `minecraft:spruce_log`, `Air` |
| **Row 3** | `minecraft:spruce_log`, `Air`, `minecraft:spruce_log` |

```
[            ] [ Spruce Sapling ] [            ]
[            ] [   Spruce Log   ] [            ]  ==>  1x Cyprus Sapling
[ Spruce Log ] [                ] [ Spruce Log ]
```

---

### 5. Hat Tree Sapling (`bt_hat_tree_sapling`)

| Slot | Item |
|:---:|:---|
| **Row 1** | `Air`, `minecraft:oak_sapling`, `Air` |
| **Row 2** | `minecraft:oak_sapling`, `minecraft:oak_log`, `minecraft:oak_sapling` |
| **Row 3** | `Air`, `Air`, `Air` |

```
[             ] [ Oak Sapling ] [             ]
[ Oak Sapling ] [   Oak Log   ] [ Oak Sapling ]  ==>  1x Hat Tree Sapling
[             ] [             ] [             ]
```

---

### 6. Dead Tree Sapling (`bt_dead_tree_sapling`)

| Slot | Item |
|:---:|:---|
| **Row 1** | `minecraft:dead_bush`, `minecraft:dead_bush`, `minecraft:dead_bush` |
| **Row 2** | `Air`, `minecraft:oak_log`, `Air` |
| **Row 3** | `Air`, `minecraft:oak_log`, `Air` |

```
[ Dead Bush ] [ Dead Bush ] [ Dead Bush ]
[           ] [  Oak Log  ] [           ]  ==>  1x Dead Tree Sapling
[           ] [  Oak Log  ] [           ]
```

---

## 🌲 Tree Growth & Multi-Block Patterns

Every sapling can grow with bonemeal or natural ticks. In addition to single-block growth, planting saplings in specific patterns produces colossal variant trees:

### 1. Big Oak Sapling
* **1×1 Single**: Grows into a **Tall Oak** (spiral curl-branch tree).
* **2×2 Square (4 saplings)**: Grows into a **Block Oak** (dense thick-trunk oak).
  ```
  [X][X]
  [X][X]
  ```
* **4×4 Ring (8 saplings)**: Grows into a **Swamp Oak** (stilted water taproots).
  ```
  .  [X][X]  .
  [X]  .  . [X]
  [X]  .  . [X]
  .  [X][X]  .
  ```
* **4×4 Coin (12 saplings)**: Grows into a **Great Oak** (giant legendary oak).
  ```
  .  [X][X]  .
  [X][X][X][X]
  [X][X][X][X]
  .  [X][X]  .
  ```

### 2. Big Birch Sapling
* **1×1 Single**: Grows into a **Tall Birch**.
* **2×2 Square (4 saplings)**: Grows into a **Giant Birch** with wide 2×2 trunk and multi-layer canopy.

### 3. Big Pine Sapling
* **1×1 Single**: Grows into a **Tall Pine / Spruce**.
* **3×3 Cross (5 saplings)**: Grows into a **Giant Pine / Redwood** (conical canopy layers).
  ```
  .   [X]   .
  [X] [X]  [X]
  .   [X]   .
  ```

### 4. Cyprus Sapling
* **1×1 Single**: Grows into a **Cyprus Tree**.
* **2×2 Square (4 saplings)**: Grows into a **Giant Cyprus Tree** (thick columnar canopy, deep taproots).

### 5. Hat Tree Sapling
* **1×1 Single**: Grows into a **Hat Tree** with vines.
* **4×4 Ring (8 saplings)**: Grows into a **Giant Hat Tree** (expansive dome canopy draped in vines).

### 6. Dead Tree Sapling
* **1×1 Single**: Grows into a **Dead Tree**.
* **2×2 Square (4 saplings)**: Grows into a **Giant Dead Tree** (sprawling dry wood branches).

---

## ⚙️ Configuration

Configurations are automatically generated in the game's `config/bigtrees/` folder:
- `kbigtrees.txt`: Master settings (toggle tree types, dimension blacklists, root generation).
- `treeconfigs/*.cfg`: Detailed parameters for height, branch curvature, leaf radius, and frequency per tree type.
- `biomegroups.cfg`: Biome grouping associations.
- `individualbiomes.cfg`: Population overrides per specific biome.

---

## 🔨 Building From Source

### Prerequisites
- **Java Development Kit (JDK):** Version 21 (Eclipse Temurin 21 recommended)
- Git

### Build Instructions

1. Clone repository:
   ```bash
   git clone https://github.com/your-repo/BigTrees.git
   cd BigTrees
   ```

2. Build the mod:
   - **Windows:**
     ```cmd
     gradlew.bat build
     ```
   - **Linux / macOS:**
     ```bash
     chmod +x gradlew
     ./gradlew build
     ```

3. The built artifact will be generated in:
   `build/libs/BigTrees_1.21.1b_neoforge1060-fork.jar`

### Running in Development Environment
- Start Client: `./gradlew runClient` (or `gradlew.bat runClient`)
- Start Server: `./gradlew runServer` (or `gradlew.bat runServer`)

---

## 📄 License & Credits

- **Original Mod:** Created by **Karob** and **ThorinStonehelm** for Minecraft 1.7.10.
- **Port & Modernization:** Forked and maintained for Minecraft 1.21.1 / NeoForge.
- **License:** Distributed under the [MIT License](LICENSE).
