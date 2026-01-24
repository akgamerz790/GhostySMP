package dev.akgamerz_790.ghostysmp.core;

import org.bukkit.NamespacedKey;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public enum GhostAbilityType {

    DRAUGR("Draugr"),
    POLTERGEIST("Poltergeist"),
    ANCHORS("Anchors"),
    DISRUPTORS("Disruptors"),
    SHIFTER("Shifter"),
    INFECTOR("Infector"),
    DRAINER("Drainer"),
    SCAVENGER("Scavenger"),
    TRACKER("Tracker"),
    REEPER("Reeper"),
    SUMMONERS("Summoners");

    private static final int GLOBAL_COOLDOWN_SECONDS = 17;

    public final String display;
    public final int cooldownSeconds;

    public static NamespacedKey TYPE_KEY() {
    return new NamespacedKey(GhostySMP.instance, "type");
}


    GhostAbilityType(String display) {
        this.display = display;
        this.cooldownSeconds = GLOBAL_COOLDOWN_SECONDS;
    }
}
