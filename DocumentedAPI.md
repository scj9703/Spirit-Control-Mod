# Spirit Control Scripting API

### Example Usage: var SCPlayer = SPCApi.getPlayer(event.player);

### SCPlayer getPlayer(IPlayer player)
* Returns an instance of the Player's player-sided SC Data.

---
# SCPlayer

### void toggleIsArmed()
* Toggles the Player's "Armed" setting. Does not support Boolean arguments, it is like a binary on/off switch. 

### boolean isArmed()
* Whether the Player is armed to use SC.

### void setCooldown(double seconds)
* Sets the Player's Spirit Control Cooldown.

### boolean isOnCooldown()

### double getCooldown()
* Returns the Player's Spirit Control Cooldown in seconds.
* Errata: If the player is off cooldown, returns 'negative seconds' instead of 0.

### boolean canUseAttack()
* Whether the Player can use their currently selected Attack. (I.e., Super Attack 1 if selected)

### Attack getCurrentSelectedAttack()

### Ability getAbilityFromSlot(String slotName)
* Accepted inputs "SUPER1", "SUPER2", "ULTIMATE", "PASSIVE", otherwise returns null.

### void setCurrentAttackSlot(int slot)
* Force selects an attack slot. Valid inputs 0-2.

### void setUnlockedSpiritControl(boolean shouldUnlock)
* Force Unlocks/Locks Spirit Control. Use with caution: Locking Spirit Control erases the player's unlocked ability data.

### boolean hasUnlockedSpiritControl()
* Whether the Player has unlocked Spirit Control.

### Set<Attack> getUnlockedSuperAttacks()

### Set<Attack> getUnlockedUltimates()

### Set<PassiveAbility> getUnlockedPassives()

### double getMaxBaseSpirit()
* Returns maximum Spirit Capacity BEFORE modifiers (i.e., Passives)

### double getMaxSpirit()
* Returns maximum Spirit Capacity WITH modifiers (i.e., Passives)

### double getSpirit()
* Returns current Spirit Gauge value.

### void setSpirit(double spirit)
* Sets Spirit Gauge.

### String drawSpiritGauge()
* Returns a text-based representation of the Player's Spirit Gauge.

### boolean isFatigued()
* Returns whether the Player is DBC Fatigued.

### boolean isChargingDBC()
* Is the Player holding C to charge?

---
# Ability

### String getDescription()
* Returns Ability description.

### String getName()
* Returns Ability name.
---
# Attack 
### Extends Ability

### byte getType()
Returns the DBC Attack type.

### byte getColor()
Returns the color.

### int getSpeed()
Returns the speed.

### boolean isEffect()
Returns whether there is an AoE effect.

### double getDmgModifier()
Returns the damage modifier.

### double getCostModifier()
Returns the cost modifier.

### double getCooldown()
Returns the cooldown.

### double getCasttime()
Returns the cast time.

### boolean isUltimate()
Returns whether it is ultimate.

### double getFatigue()
Returns the fatigue.

---
# PassiveAbility
### Extends Ability

### boolean canPlayerUsePassive(SCPlayer player)
* Whether the Player meets the requirements for their Passive.
