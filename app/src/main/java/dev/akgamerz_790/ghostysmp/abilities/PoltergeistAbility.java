package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class PoltergeistAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        Location loc = owner.getLocation();
        World world = loc.getWorld();
        if (world == null) return;

        int radius = GhostySMP.getPoltergeistRadius();
        int heightUp = GhostySMP.getPoltergeistHeightUp();
        int heightDown = GhostySMP.getPoltergeistHeightDown();
        double destructionChance = GhostySMP.getPoltergeistDestructionChance();
        double velocityXMultiplier = GhostySMP.getPoltergeistVelocityXMultiplier();
        double velocityZMultiplier = GhostySMP.getPoltergeistVelocityZMultiplier();
        double velocityYBase = GhostySMP.getPoltergeistVelocityYBase();
        double velocityYRandom = GhostySMP.getPoltergeistVelocityYRandom();

        for (int y = -heightDown; y <= heightUp; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    double dist = Math.sqrt(x * x + y * y + z * z);
                    if (dist > radius) continue;
                    if (Math.random() > destructionChance) continue;

                    Location blockLoc = loc.clone().add(x, y, z);
                    Block block = blockLoc.getBlock();
                    Material mat = block.getType();

                    if (!mat.isSolid() || mat == Material.BEDROCK) continue;

                    // Spawn falling block entity (visual + physics)
                    FallingBlock fb = world.spawnFallingBlock(blockLoc, block.getBlockData());

                    fb.setDropItem(false);
                    fb.setHurtEntities(false);

                    // CRITICAL: Actually remove the terrain block to create crater
                    block.setType(Material.AIR, false);

                    // DO NOT setCancelDrop(true) - let them re-land naturally

                    // Fixed velocities (was way too high before)
                    double vx = (x + (Math.random() - 0.5)) * velocityXMultiplier;
                    double vz = (z + (Math.random() - 0.5)) * velocityZMultiplier;
                    double vy = velocityYBase + Math.random() * velocityYRandom;

                    fb.setVelocity(new Vector(vx, vy, vz));
                }
            }
        }

        // Particles and sounds unchanged
        world.spawnParticle(Particle.CLOUD, loc, 60, 2, 1, 2, 0.05);
        world.spawnParticle(Particle.CLOUD, loc, 80, 1.5, 1, 1.5, 0.02);

        world.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 0.8f);
        world.playSound(loc, Sound.ENTITY_WARDEN_HURT, 1f, 1f);
    }
}