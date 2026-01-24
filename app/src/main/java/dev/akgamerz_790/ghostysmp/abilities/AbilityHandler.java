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
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.HashMap;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.core.GhostAbilityType;
import dev.akgamerz_790.ghostysmp.core.GhostManager;
import dev.akgamerz_790.ghostysmp.utils.Util;

public class AbilityHandler implements org.bukkit.event.Listener {
    private static final Random random = new Random();

    // Reflect cooldown tracker
    public static final HashMap<UUID, Long> reflectUntil = new HashMap<>();

    private static final Map<GhostAbilityType, Ability> abilities = new HashMap<>();

    static {
        abilities.put(GhostAbilityType.DRAUGR, new DraugrAbility());
        abilities.put(GhostAbilityType.POLTERGEIST, new PoltergeistAbility());
        abilities.put(GhostAbilityType.ANCHORS, new AnchorsAbility());
        abilities.put(GhostAbilityType.DISRUPTORS, new DisruptorsAbility());
        abilities.put(GhostAbilityType.SHIFTER, new ShifterAbility());
        abilities.put(GhostAbilityType.INFECTOR, new InfectorAbility());
        abilities.put(GhostAbilityType.DRAINER, new DrainerAbility());
        abilities.put(GhostAbilityType.SCAVENGER, new ScavengerAbility());
        abilities.put(GhostAbilityType.TRACKER, new TrackerAbility());
        abilities.put(GhostAbilityType.REEPER, new ReeperAbility());
        abilities.put(GhostAbilityType.SUMMONERS, new SummonersAbility());
    }

    public static boolean handleCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) return false;
        if (args.length == 0) {
            p.sendMessage(ChatColor.GOLD + "Available ghosts chutiyapa: draugr, poltergeist, anchors, disruptors, shifter, infector, drainer, scavenger, tracker, reeper, summoners");
            return true;
        }
        try {
            GhostAbilityType type = GhostAbilityType.valueOf(args[0].toUpperCase());
            p.getInventory().addItem(createGhostItem(type));
            p.sendMessage(ChatColor.GREEN + "Bhosdike " + type.display + " Ghost mila! Jaise teri maa ne diya ho.");
            return true;
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "Bhosdike galat naam! Available ghosts: draugr, poltergeist, anchors, disruptors, shifter, infector, drainer, scavenger, tracker, reeper, summoners");
            return true;
        }
    }

    public static ItemStack createGhostItem(GhostAbilityType type) {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + type.display + " Ghost");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click karo aur ghost ko summon karo!", ChatColor.RED + "Cooldown: " + type.cooldownSeconds + "s (warna ruk jaoge chutiye)"));
        meta.getPersistentDataContainer().set(GhostAbilityType.TYPE_KEY(), PersistentDataType.STRING, type.name());
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static void activateAbility(GhostAbilityType type, Player owner, Player target) {
        Ability ability = abilities.get(type);
        if (ability != null) {
            ability.activate(owner, target);
        } else {
            // Fallback to old switch for abilities not yet separated
            switch (type) {
                case DISRUPTORS -> {
                    reflectUntil.put(owner.getUniqueId(), System.currentTimeMillis() + GhostySMP.getDisruptorsReflectDurationTicks());
                    owner.getWorld().spawnParticle(Particle.FLAME, owner.getLocation(),
                        GhostySMP.getDisruptorsParticleCount(),
                        GhostySMP.getDisruptorsParticleSpread(),
                        GhostySMP.getDisruptorsParticleSpread(),
                        GhostySMP.getDisruptorsParticleSpread(), 0.1);
                }
                case SHIFTER -> {
                    if (target != null) {
                        target.teleport(target.getLocation().add(0, GhostySMP.getShifterTeleportHeight(), 0));
                        Util.lockPlayer(target, GhostySMP.getShifterLockDurationSeconds());
                        target.getWorld().spawnParticle(Particle.CLOUD, target.getLocation(),
                            GhostySMP.getShifterParticleCount(),
                            GhostySMP.getShifterParticleSpread(),
                            GhostySMP.getShifterParticleSpread(),
                            GhostySMP.getShifterParticleSpread(), 0.1);
                    }
                }
                case INFECTOR -> owner.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, owner.getLocation(),
                    GhostySMP.getInfectorParticleCount(),
                    GhostySMP.getInfectorParticleSpread(),
                    GhostySMP.getInfectorParticleSpread(),
                    GhostySMP.getInfectorParticleSpread(), 0.1);
                case DRAINER -> {
                    if (target != null) {
                        target.damage(GhostySMP.getDrainerDamage());
                        Util.dotDamage(target, GhostySMP.getDrainerDotDamage(), GhostySMP.getDrainerDotDurationSeconds());
                    }
                }
                case SCAVENGER -> Util.giveGodBow(owner);
                case REEPER -> {
                    if (target != null) {
                        target.damage(GhostySMP.getReeperDamage());
                        Util.dotDamage(target, GhostySMP.getReeperDotDamage(), GhostySMP.getReeperDotDurationSeconds());
                    }
                }
                case SUMMONERS -> {
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
        }

        owner.sendMessage(ChatColor.GREEN + "Chutiya " + type.display + " chal gaya! Ab maro ya marao.");
        owner.playSound(owner.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
    }

    private static void showPlayerUI(Player player) {
        player.sendMessage(ChatColor.GOLD + "Online chutiyas:");
        for (Player p : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.WHITE + "• " + p.getName() +
                (p.getGameMode() == GameMode.CREATIVE ? ChatColor.GRAY + " (Creative mode mein bhosdike attitude)" : ""));
        }
    }
}
