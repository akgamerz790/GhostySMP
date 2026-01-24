package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.utils.Util;

public class ShifterAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        if (target != null && !target.equals(owner)) {
            target.teleport(target.getLocation().add(0, GhostySMP.getShifterTeleportHeight(), 0));
            Util.lockPlayer(target, GhostySMP.getShifterLockDurationSeconds());
            target.getWorld().spawnParticle(Particle.CLOUD, target.getLocation(),
                GhostySMP.getShifterParticleCount(),
                GhostySMP.getShifterParticleSpread(),
                GhostySMP.getShifterParticleSpread(),
                GhostySMP.getShifterParticleSpread(), 0.1);
        }
    }
}
