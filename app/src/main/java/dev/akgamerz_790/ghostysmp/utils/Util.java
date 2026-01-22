package dev.akgamerz_790.ghostysmp.utils;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.core.GhostAbilityType;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public final class Util {

    private Util() {} // no instantiation

    /* ===================== PARTICLES ===================== */

    public static void spawnGhostParticles(GhostAbilityType type, Location loc, Player owner) {
        World world = loc.getWorld();
        if (world == null || owner == null) return;

        world.spawnParticle(Particle.EXPLOSION, loc, 15, 0.4, 0.4, 0.4, 0.05);
        world.spawnParticle(Particle.FLAME, loc, 40, 1, 1, 1, 0.1);
        world.spawnParticle(Particle.SMOKE, loc, 30, 1, 1, 1, 0.03);

        world.playSound(loc, Sound.ENTITY_WITHER_SHOOT, 1.1f, 0.8f);
        world.playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 0.9f, 1.2f);

        new BukkitRunnable() {
            double angle = 0;
            int ticks = 0;

            @Override
            public void run() {
                if (!owner.isOnline()) {
                    cancel();
                    return;
                }

                Location center = owner.getLocation().clone().add(0, 1.2, 0);
                angle += Math.PI / 12;

                Location ring = center.clone().add(
                        Math.cos(angle) * 2.5,
                        Math.sin(angle) * 0.5,
                        Math.sin(angle) * 2.5
                );

                world.spawnParticle(Particle.END_ROD, ring, 3, 0.05, 0.05, 0.05, 0);
                world.spawnParticle(Particle.FLAME, ring, 2, 0.05, 0.05, 0.05, 0);

                if (++ticks >= 200) cancel(); // 10s
            }
        }.runTaskTimer(GhostySMP.instance, 0L, 1L);
    }

    /* ===================== DAMAGE ===================== */

    public static void dotDamage(Player target, double damage, int seconds) {
        if (target == null) return;

        new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                if (!target.isOnline() || target.isDead() || time++ >= seconds) {
                    cancel();
                    return;
                }
                target.damage(damage);
            }
        }.runTaskTimer(GhostySMP.instance, 0L, 20L);
    }

    /* ===================== CONTROL ===================== */

    public static void lockPlayer(Player p, int seconds) {
        if (p == null) return;

        p.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                seconds * 20,
                9,
                false,
                false
        ));

        p.addPotionEffect(new PotionEffect(
                PotionEffectType.LEVITATION,
                seconds * 20,
                0,
                false,
                false
        ));
    }

    /* ===================== ITEMS ===================== */

    public static void giveGodBow(Player p) {
        if (p == null) return;

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Chutiya Bow");
            meta.addEnchant(Enchantment.FLAME, 1, true);
            meta.addEnchant(Enchantment.PUNCH, 2, true);
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            bow.setItemMeta(meta);
        }

        p.getInventory().addItem(bow);
        p.sendMessage(ChatColor.GOLD + "Chutiya Bow mila re! 15 seconds ke liye god ban gaya, ab maro sabko!");

        new BukkitRunnable() {
            @Override
            public void run() {
                p.getInventory().remove(bow);
            }
        }.runTaskLater(GhostySMP.instance, 300L);
    }

    /* ===================== TRACKER ===================== */

    public static void tracker(Player owner) {
        if (owner == null) return;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(owner)) {
                owner.teleport(p.getLocation());
                owner.addPotionEffect(new PotionEffect(
                        PotionEffectType.ABSORPTION,
                        200,
                        2
                ));
                owner.sendMessage(ChatColor.AQUA + "Target track ho gaya, ab jaake maaro chutiye!");
                return;
            }
        }
    }
}
