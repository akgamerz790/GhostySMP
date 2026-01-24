package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class AnchorsAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        Location loc = owner.getLocation();
        World world = loc.getWorld();
        if (world == null) return;

        for (Entity en : loc.getWorld().getNearbyEntities(loc, 9, 9, 9)) {
            if (!(en instanceof Player p) || p.getUniqueId().equals(owner.getUniqueId())) continue;

            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 9));
            p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 128));
            p.sendMessage(ChatColor.RED + "Ender pearl nahi allowed bhai, ruk ja chutiye!");
        }

        world.spawnParticle(Particle.CLOUD, loc,
            GhostySMP.getAnchorsParticleCount(),
            GhostySMP.getAnchorsParticleSpread(),
            GhostySMP.getAnchorsParticleSpread(),
            GhostySMP.getAnchorsParticleSpread(),
            0.1);
    }
}