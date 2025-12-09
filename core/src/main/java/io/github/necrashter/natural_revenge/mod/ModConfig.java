package io.github.necrashter.natural_revenge.mod;

/**
 * Configuration class that holds all mod toggle states.
 * This is a singleton that can be accessed from anywhere in the game.
 */
public class ModConfig {
    private static ModConfig instance;

    // Mod toggles
    public boolean bunnyhop = false;
    public boolean airStrafe = false;
    public boolean thirdPerson = false;
    public boolean rapidFire = false;
    public boolean infiniteAmmo = false;
    public boolean oneHitKill = false;

    // Third person camera settings
    public float thirdPersonDistance = 5.0f;

    // Bunnyhop settings
    public float bunnyhopBoost = 1.3f;

    // Rapid fire multiplier
    public float rapidFireMultiplier = 5.0f;

    private ModConfig() {}

    public static ModConfig getInstance() {
        if (instance == null) {
            instance = new ModConfig();
        }
        return instance;
    }

    public static ModConfig get() {
        return getInstance();
    }

    /**
     * Reset all mods to disabled state
     */
    public void resetAll() {
        bunnyhop = false;
        airStrafe = false;
        thirdPerson = false;
        rapidFire = false;
        infiniteAmmo = false;
        oneHitKill = false;
    }
}
