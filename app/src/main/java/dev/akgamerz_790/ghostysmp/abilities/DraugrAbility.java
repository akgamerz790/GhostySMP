package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class DraugrAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        if (target != null && !target.equals(owner)) {
            target.damage(GhostySMP.getDraugrDamage());
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, 1, 0), 100, 0.5, 0.5, 0.5, 0.1);
        }
    }
}