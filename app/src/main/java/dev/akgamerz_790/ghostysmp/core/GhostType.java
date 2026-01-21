package dev.akgamerz_790.ghostysmp;

import org.bukkit.NamespacedKey;

public enum GhostType {

    DRAUGR("Draugr"),
    POLTERGEIST("Poltergeist"),
    ANCHORS("Anchors"),
    DISRUPTORS("Disruptors"),
    SHIFTER("Shifter"),
    INFECTOR("Infector"),
    SCAVENGER("Scavenger"),
    TRACKER("Tracker"),
    REEPER("Reeper"),
    SUMMONERS("Summoners"),
    DRAINER("Drainer");

    private static final int GLOBAL_COOLDOWN_SECONDS = 2;

    public final String display;
    public final int cooldownSeconds;

    public static final NamespacedKey TYPE_KEY =
            new NamespacedKey("ghostysmp", "type");

    GhostType(String display) {
        this.display = display;
        this.cooldownSeconds = GLOBAL_COOLDOWN_SECONDS;
    }
}
