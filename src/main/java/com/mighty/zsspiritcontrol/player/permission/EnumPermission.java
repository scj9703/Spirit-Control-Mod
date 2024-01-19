package com.mighty.zsspiritcontrol.player.permission;

import com.mighty.zsspiritcontrol.SpiritControl;

public enum EnumPermission {
    SPIRITCONTROL_ENABLE(new BukkitWrapper.Permission("zs.spiritcontrol.enable", true)),
    SPIRITCONTROL_DISABLE(new BukkitWrapper.Permission("zs.spiritcontrol.disable", true)),
    SPIRITCONTROL_TOGGLE(new BukkitWrapper.Permission("zs.spiritcontrol.toggle", true)),
    SPIRITCONTROL_CHECK(new BukkitWrapper.Permission("zs.spiritcontrol.disable", true)),
    SPIRITCONTROL_UNLOCK(new BukkitWrapper.Permission("zs.spiritcontrol.unlock", true)),
    SPIRITCONTROL_LOCK(new BukkitWrapper.Permission("zs.spiritcontrol.lock", true)),
    SPIRITCONTROL_RELOAD(new BukkitWrapper.Permission("zs.spiritcontrol.reloadconfig", true));

    public final BukkitWrapper.Permission permNode;
    EnumPermission(BukkitWrapper.Permission permission) {
        this.permNode = permission;
    }
    public static void init(){
        SpiritControl.INSTANCE.LOGGER.info("Attempting to create permissions");
    }
}
