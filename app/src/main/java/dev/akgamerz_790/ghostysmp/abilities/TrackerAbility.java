package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.utils.Util;

public class TrackerAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        Util.tracker(owner);
        owner.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,
            GhostySMP.getTrackerAbsorptionDurationTicks(),
            GhostySMP.getTrackerAbsorptionAmplifier()));
        owner.sendMessage("Online chutiyas:");
        for (Player p : owner.getServer().getOnlinePlayers()) {
            owner.sendMessage("• " + p.getName() +
                (p.getGameMode() == org.bukkit.GameMode.CREATIVE ? " (Creative mode mein bhosdike attitude)" : ""));
        }
    }
}