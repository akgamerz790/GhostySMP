package dev.akgamerz_790.ghostysmp;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;
import org.bukkit.util.*;
import java.util.*;

public class GhostManager implements Listener {
    public static Map<UUID, Map<GhostType, Long>> cooldowns = new HashMap<>();
    public static Map<UUID, Location> ghostLocations = new HashMap<>();
    public static Map<UUID, Long> reflectUntil = new HashMap<>();
    public static Map<UUID, Boolean> anchorLocked = new HashMap<>();
    public static Map<UUID, Double> infectorDamage = new HashMap<>();

    public GhostManager() {
        startAuraTask();
    }

    public void startAuraTask() {
        new BukkitRunnable() {
            public void run() {
                long now = System.currentTimeMillis();
                for (var entry : new HashMap<>(ghostLocations).entrySet()) {
                    Player owner = Bukkit.getPlayer(entry.getKey());
                    if (owner == null) continue;
                    Location loc = entry.getValue();
                    if (loc.getWorld() == null) continue;
                    
                    // AOE Effects
                    for (Entity en : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
                        if (!(en instanceof Player p) || p.getUniqueId().equals(owner.getUniqueId())) continue;
                        
                        double dist = loc.distance(p.getLocation());
                        
                        // ANCHORS: 9 blocks - 80% slow + no jump + no pearl
                        if (dist <= 9) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 9));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 128)); 
                            anchorLocked.put(p.getUniqueId(), true);
                        }
                        
                        // INFECTOR: 7 blocks - poison until 9 hearts
                        if (dist <= 7) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 3));
                            infectorDamage.compute(p.getUniqueId(), (k, v) -> 
                                v == null ? 4.0 : Math.min(v + 2.0, 18.0));
                        }
                    }
                    
                    loc.getWorld().spawnParticle(Particle.FLAME, loc, 2, 0.5, 0.5, 0.5, 0.02);
                }
                
                reflectUntil.entrySet().removeIf(e -> e.getValue() < now);
            }
        }.runTaskTimer(GhostySMP.instance, 0L, 5L);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.hasItem() || p.hasCooldown(e.getItem().getType())) return;
        ItemMeta m = e.getItem().getItemMeta();
        if (m == null || !m.getPersistentDataContainer().has(GhostType.TYPE_KEY, PersistentDataType.STRING)) return;
        
        GhostType type = GhostType.valueOf(m.getPersistentDataContainer().get(GhostType.TYPE_KEY, PersistentDataType.STRING));
        UUID id = p.getUniqueId();
        cooldowns.computeIfAbsent(id, k -> new HashMap<>());
        long now = System.currentTimeMillis();
        if (cooldowns.get(id).getOrDefault(type, 0L) > now) {
            long remain = (cooldowns.get(id).get(type) - now) / 1000;
            p.sendMessage(ChatColor.RED + "Cooldown: " + remain + "s!");
            return;
        }
        
        Location loc = p.getLocation().add(2, 0, 2);
        ghostLocations.put(id, loc);
        Util.spawnGhostParticles(type, loc, p);
        
        // FIXED: Raytrace ENTITIES (players)
        RayTraceResult hit = p.rayTraceEntities(50);  // Remove second param
        Player target = null;
        if (hit != null && hit.getHitEntity() instanceof Player t && !t.equals(p)) {
            target = (Player) hit.getHitEntity();
        }
        
        AbilityManager.activateAbility(type, p, target);
        cooldowns.get(id).put(type, now + type.cooldownSeconds * 1000L);
        p.setCooldown(Material.BLAZE_ROD, type.cooldownSeconds * 20);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player a && e.getEntity() instanceof Player o) {
            Long endTime = reflectUntil.get(o.getUniqueId());
            if (endTime != null && endTime > System.currentTimeMillis()) {
                double dmg = e.getDamage();
                a.damage(dmg * 0.9);
                e.setDamage(dmg * 0.1);
            }
        }
    }

    @EventHandler
    public void onPearl(ProjectileLaunchEvent e) {
        if (e.getEntity().getType() == EntityType.ENDER_PEARL) {
            if (e.getEntity().getShooter() instanceof Player s && anchorLocked.getOrDefault(s.getUniqueId(), false)) {
                e.setCancelled(true);
                s.sendMessage(ChatColor.RED + "No enderpearl!");
                anchorLocked.remove(s.getUniqueId());
            }
        }
    }
}
