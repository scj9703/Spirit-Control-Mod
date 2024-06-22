# Spirit Control Mod

* Implements a fighting-game-like Super Attack System for the Official Jingames 1.7.10 Modded Minecraft Server.

* Trello of requirements / progress: https://trello.com/b/xCMUKqSn/spirit-control-mod
* Overview Document: https://docs.google.com/document/d/1VTNgK3i6tIVS1_goBPZD1eRq2rAlfA95jTqlj8ZZHjk/edit?usp=sharing

This project uses Mixins, so if you're in a mod environment add these to your program arguments to run:
```
--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin spiritcontrol.mixins.json
```

Building
--------
To build, you simply run `./gradlew build` in your terminal. <br>
The results are going to be in `./build/libs/`.

Commands and permissions
------------------------
`/spc` and `/spiritcontrol` are aliases for the prefix for all of these commands, except `//screload`. 
<br><br>

THE DEFAULT PERMISSION NODES ARE: 
* `com.mighty.spiritcontrol.command.spiritcontrol.Command_SpiritControl` - /spc
* `com.mighty.spiritcontrol.command.configreload.Command_SCReload` - /screload
<br><br>
These are required to even run commands in SC on Crucible/Cauldron servers, however since SC implements its own permission system you can just add `com.mighty.spiritcontrol.*` to all players and then the custom perm nodes for other commands individually to correct ranks
<br>
<br>

CUSTOM PERM NODES:
* `/spc` - no perm node - Displays data about your current load-out.
* `/spc skills` - no perm node - Displays all of your unlocked skills.
* `/spc equip <slot_name> <skill_id>` - no perm node - Allows the player to change out abilities in their load-out.
* `/spc enable [player]` - `zs.spiritcontrol.enable` - Unlocks Spirit Control for the player.
* `/spc disable [player]` - `zs.spiritcontrol.disable` - Removes Spirit Control from the player.
* `/spc unlock <skill_id> [player]` - `zs.spiritcontrol.unlock` - Unlock an ability.
* `/spc lock <skill_id> [player]` - `zs.spiritcontrol.lock` - Removes an ability.
* `//screload` - `zs.spiritcontrol.reloadconfig` - Reloads configs and abilities from `./config/spirit_control`

Configs
-------
Configs are located in `./config/spirit_control/`.
<br><br>
You can overwrite default ability stats by creating new abilities in their respective config files using the same ID as a default ability. <br><br>
You cannot change default ability types. (Passives can't become ultimates or supers, etc.) <br>
Default ability IDs: `KiAttack`, `EnergyWave`, `VirtuousSpirit`

### Acceptable race names:
```json lines
human
saiyan
half_saiyan
namekian
arcosian
majin
```

### Acceptable passive fill method names:
```json lines
damage_taken
damage_dealt
passive
```
<br>

### Acceptable attack names:
```json lines
wave
blast
disk
spiral
big_blast
barrage
shield
explosion
```

### Acceptable attack types:
```json lines
alignment_based
white
blue
purple
red
black
green
yellow
orange
pink
magenta
light_pink
cyan
dark_cyan
light_cyan
dark_gray
gray
dark_blue
light_blue
dark_purple
light_purple
dark_red
light_red
dark_green
lime
dark_yellow
light_yellow
gold
light_orange
dark_brown
light_brown
```
