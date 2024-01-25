# Spirit Control Mod

* Implements a fighting-game-like Super Attack System for the Official Jingames DBC Server.

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
`/sc` and `/spiritcontrol` are aliases for the prefix for all of these commands, except `//screload`.
* `/sc` - no perm node - Displays data about your current load-out.
* `/sc skills` - no perm node - Displays all of your unlocked skills.
* `/sc equip <slot_name> <skill_id>` - no perm node - Allows the player to change out abilities in their load-out.
* `/sc enable [player]` - `zs.spiritcontrol.enable` - Unlocks Spirit Control for the player.
* `/sc disable [player]` - `zs.spiritcontrol.disable` - Removes Spirit Control from the player.
* `/sc unlock <skill_id> [player]` - `zs.spiritcontrol.unlock` - Unlock an ability.
* `/sc lock <skill_id> [player]` - `zs.spiritcontrol.lock` - Removes an ability.
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