package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class SummonersAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        Location loc = owner.getLocation();
        IronGolem g1 = (IronGolem) owner.getWorld().spawnEntity(loc.clone().add(-1, 0, 0), EntityType.IRON_GOLEM);
        IronGolem g2 = (IronGolem) owner.getWorld().spawnEntity(loc.clone().add(2, 0, 0), EntityType.IRON_GOLEM);
        g1.setCustomName(ChatColor.GREEN + GhostySMP.getSummonersGuardName());
        g2.setCustomName(ChatColor.GREEN + GhostySMP.getSummonersGuardName());
        owner.getWorld().spawnParticle(Particle.HEART, loc,
            GhostySMP.getSummonersParticleCount(),
            GhostySMP.getSummonersParticleSpread(),
            1, GhostySMP.getSummonersParticleSpread(), 0);
    }
}
