package dev.akgamerz_790.ghostysmp;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.core.GhostType;

public class Util {
    public static void spawnGhostParticles(GhostType type, Location initialLoc, Player owner) {
    World world = initialLoc.getWorld();
    if (world == null) return;

    // 🌟 EPIC IMPACT EXPLOSION
    world.spawnParticle(Particle.EXPLOSION_NORMAL, initialLoc, 20, 0.5, 0.5, 0.5, 0.1);
    world.spawnParticle(Particle.FLAME, initialLoc, 50, 1, 1, 1, 0.2);
    world.spawnParticle(Particle.SMOKE_NORMAL, initialLoc, 30, 1, 1, 1, 0.05);
    world.playSound(initialLoc, Sound.ENTITY_WITHER_SHOOT, 1.2f, 0.8f);
    world.playSound(initialLoc, Sound.ENTITY_BLAZE_SHOOT, 0.8f, 1.2f);

    new BukkitRunnable() {
        double angle = 0, height = 0;
        int ticks = 0;
        final int duration = 200; // 10 seconds

        @Override
        public void run() {
            if (!owner.isOnline()) { cancel(); return; }

            Location center = owner.getLocation().clone().add(0, 1.2, 0);
            angle += Math.PI / 16;
            height += 0.05;
            ticks++;

            // 🔥 DOUBLE RING SYSTEM
            double r1 = 2.5, r2 = 3.5;
            Location p1 = center.clone().add(
                Math.cos(angle) * r1, Math.sin(height) * 0.5, Math.sin(angle) * r1
            );
            Location p2 = center.clone().add(
                Math.cos(angle * 2) * r2, Math.sin(height * 1.3) * 0.7, Math.sin(angle * 2) * r2
            );

            // MULTI-LAYER PARTICLES
            world.spawnParticle(Particle.FLAME, p1, 8, 0.1, 0.1, 0.1, 0.05);
            world.spawnParticle(Particle.SMOKE_NORMAL, p1, 5, 0.1, 0.1, 0.1, 0.02);
            world.spawnParticle(Particle.SPIT, p2, 12, 0.15, 0.15, 0.15, 0.08);
            world.spawnParticle(Particle.SCRAPE, center.clone().add(0, 0.5, 0), 3, 1, 1, 1, 0);

            // ✨ HELIX SPIRAL
            for (int i = 0; i < 5; i++) {
                double h = i * 0.3;
                Location helix = center.clone().add(
                    Math.cos(angle + i * Math.PI/2.5) * 1.8,
                    h + Math.sin(height + i),
                    Math.sin(angle + i * Math.PI/2.5) * 1.8
                );
                world.spawnParticle(Particle.GLOW, helix, 2, 0.05, 0.05, 0.05, 0);
            }

            if (ticks >= duration) cancel();
        }
    }.runTaskTimer(GhostySMP.instance, 0L, 1L);
}


    public static void dotDamage(Player target, double hearts, int seconds) {
        new BukkitRunnable() {
            int count = 0;
            public void run() {
                if (!target.isOnline() || target.isDead() || count++ >= seconds) {
                    cancel();
                    return;
                }
                target.damage(hearts);
            }
        }.runTaskTimer(GhostySMP.instance, 0L, 20L);
    }

    public static void lockPlayer(Player p, int seconds) {
        new BukkitRunnable() {
            int count = 0;
            public void run() {
                if (count++ >= seconds * 20) {
                    cancel();
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 10, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 0, false, false));
            }
        }.runTaskTimer(GhostySMP.instance, 0L, 1L);
    }

    public static void giveGodBow(Player p) {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.addEnchant(Enchantment.FLAME, 1, true);
        meta.addEnchant(Enchantment.PUNCH, 2, true);
        meta.addEnchant(Enchantment.MULTISHOT, 1, true);
        bow.setItemMeta(meta);
        p.getInventory().addItem(bow);
        p.sendMessage(ChatColor.GOLD + "God Bow for 15s!");
        new BukkitRunnable() {
            public void run() {
                p.getInventory().remove(bow);
            }
        }.runTaskLater(GhostySMP.instance, 300L);
    }

    public static void tracker(Player owner) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(owner)) {
                owner.teleport(p.getLocation());
                owner.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 2));
                owner.sendMessage(ChatColor.AQUA + "Tracked!");
                return;
            }
        }
    }
}
