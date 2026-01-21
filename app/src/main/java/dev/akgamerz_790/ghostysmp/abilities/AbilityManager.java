package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.*;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.HashMap;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.core.GhostType;
import dev.akgamerz_790.ghostysmp.core.GhostManager;
import dev.akgamerz_790.ghostysmp.utils.Util;

public class AbilityManager implements org.bukkit.event.Listener {
    private static final Random random = new Random();

    // Reflect cooldown tracker
    public static final HashMap<UUID, Long> reflectUntil = new HashMap<>();

    public static boolean handleCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p) || args.length == 0) return false;
        try {
            GhostType type = GhostType.valueOf(args[0].toUpperCase());
            p.getInventory().addItem(createGhostItem(type));
            p.sendMessage(ChatColor.GREEN + "✨ " + type.display + " Ghost given!");
            return true;
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "Ghosts: draugr poltergeist anchors disruptors shifter infector drainer scavenger tracker reeper summoners");
            return true;
        }
    }

    public static ItemStack createGhostItem(GhostType type) {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + type.display + " Ghost");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to summon!", ChatColor.RED + "Cooldown: " + type.cooldownSeconds + "s"));
        meta.getPersistentDataContainer().set(GhostType.TYPE_KEY(), PersistentDataType.STRING, type.name());
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static void activateAbility(GhostType type, Player owner, Player target) {
        switch (type) {
            case DRAUGR -> {
                if (target != null) {
                    target.damage(14); // 7 hearts
                    target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0,1,0), 100, 0.5, 0.5, 0.5, 0.1);
                }
            }
            case POLTERGEIST -> {
    Location loc = owner.getLocation();
    World world = loc.getWorld();
    if (world == null) return;

    Random random = new Random();

    int radius = 6;          // tight area
    int heightUp = 4;        // only a few blocks above
    int heightDown = 5;      // a bit below

    for (int y = -heightDown; y <= heightUp; y++) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                // spherical filter for a smooth dome
                double dist = Math.sqrt(x*x + y*y + z*z);
                if (dist > radius) continue;
                if (random.nextDouble() > 0.85) continue; // dense but not ALL

                Location blockLoc = loc.clone().add(x, y, z);
                Block block = blockLoc.getBlock();
                Material mat = block.getType();

                if (!mat.isSolid() || mat == Material.BEDROCK) continue;

                FallingBlock fb = world.spawnFallingBlock(
                        blockLoc,
                        block.getBlockData()
                );

                // Keep original block intact
                fb.setDropItem(false);
                fb.setHurtEntities(false);

                // small upward + outward force
                double vx = (x + (random.nextDouble() - 0.5)) * 0.2;
                double vz = (z + (random.nextDouble() - 0.5)) * 0.3;
                double vy = 8 + random.nextDouble() * 0.1; // gentle lift

                fb.setVelocity(new Vector(vx, vy, vz));
            }
        }
    }

    // ✨ particles to make it look magical / chaotic
    world.spawnParticle(Particle.CLOUD, loc, 60, 2, 1, 2, 0.05);
    world.spawnParticle(Particle.CLOUD, loc, 80, 1.5, 1, 1.5, 0.02);

    world.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 0.8f);
    world.playSound(loc, Sound.ENTITY_WARDEN_HURT, 1f, 1f);
}



            case ANCHORS -> owner.getWorld().spawnParticle(Particle.CLOUD, owner.getLocation(), 50, 3, 3, 3, 0.1);
            case DISRUPTORS -> {
                reflectUntil.put(owner.getUniqueId(), System.currentTimeMillis() + 15000);
                owner.getWorld().spawnParticle(Particle.FLAME, owner.getLocation(), 100, 2, 2, 2, 0.1);
            }
            case SHIFTER -> {
                if (target != null) {
                    target.teleport(target.getLocation().add(0,50,0));
                    Util.lockPlayer(target, 15);
                    target.getWorld().spawnParticle(Particle.CLOUD, target.getLocation(), 100, 2, 2, 2, 0.1);
                }
            }
            case INFECTOR -> owner.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, owner.getLocation(), 80, 4, 4, 4, 0.1);
            case DRAINER -> {
                if (target != null) {
                    target.damage(20); // 10 hearts
                    Util.dotDamage(target, 2, 10);
                }
            }
            case SCAVENGER -> Util.giveGodBow(owner);
            case TRACKER -> {
                Util.tracker(owner);
                owner.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 2));
                showPlayerUI(owner);
            }
            case REEPER -> {
                if (target != null) {
                    target.damage(34); // 17 hearts
                    Util.dotDamage(target, 2, 5);
                }
            }
            case SUMMONERS -> {
                Location loc = owner.getLocation();
                IronGolem g1 = (IronGolem) owner.getWorld().spawnEntity(loc.clone().add(-1, 0, 0), EntityType.IRON_GOLEM);
                IronGolem g2 = (IronGolem) owner.getWorld().spawnEntity(loc.clone().add(2, 0, 0), EntityType.IRON_GOLEM);
                g1.setCustomName(ChatColor.GREEN + "Guard");
                g2.setCustomName(ChatColor.GREEN + "Guard");
                owner.getWorld().spawnParticle(Particle.HEART, loc, 50, 2, 1, 2, 0);
            }
        }

        owner.sendMessage(ChatColor.GREEN + "⚡ " + type.display + " activated!");
        owner.playSound(owner.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
    }

    private static void showPlayerUI(Player player) {
        player.sendMessage(ChatColor.GOLD + "👁️ Online Players:");
        for (Player p : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.WHITE + "• " + p.getName() + 
                (p.getGameMode() == GameMode.CREATIVE ? ChatColor.GRAY + " (Creative)" : ""));
        }
    }
}
