package com.pocketserver.api.block;

/**
 * Enum of every single type in the MCPE game.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public enum Material {
    AIR(0),
    STONE(1),
    GRASS(2),
    DIRT(3),
    COBBLESTONE(4),
    WOOD(5),
    SAPLING(6),
    BEDROCK(7),
    WATER(8),
    STATIONARY_WATER(9),
    LAVA(10),
    STATIONARY_LAVA(11),
    SAND(12),
    GRAVEL(13),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    WOOD_LOG(17),
    LEAVES(18);

    private final int id;

    Material(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isItem() {
        return this.id >= 256;
    }

    public boolean isBlock() {
        return !isItem();
    }

    public static Material getMaterial(int id) {
        return values()[id]; // This may stop working. To be determined.
    }
}