package dev.akgamerz_790.ghostysmp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.akgamerz_790.ghostysmp.core.GhostManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GhostySMP extends JavaPlugin {

    public static GhostySMP instance;

    // Stores ghost locations for aura particles
    private final Map<UUID, Location> ghostLocations = new HashMap<>();

    @Override
public void onEnable() {
    instance = this;

    Bukkit.getPluginManager().registerEvents(new GhostManager(), this);

    startAuraTask();
    getLogger().info("GhostySMP enabled!");
}


    @Override
    public void onDisable() {
        getLogger().info("GhostySMP disabled!");
    }

    // Optional: command handler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ghostaura")) {
            if (sender instanceof Player player) {
                ghostLocations.put(player.getUniqueId(), player.getLocation());
                player.sendMessage("Ghost aura enabled at your location!");
                return true;
            }
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        return false;
    }

    // Starts recurring aura particle effect for ghosts
    public void startAuraTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Location> entry : new HashMap<>(ghostLocations).entrySet()) {
                    Player owner = Bukkit.getPlayer(entry.getKey());
                    if (owner == null) continue;

                    Location ghostLoc = entry.getValue();

                    // Pulsing ghost orb
                    double pulse = Math.sin(System.currentTimeMillis() * 0.01) * 0.3 + 0.7;
                    ghostLoc.getWorld().spawnParticle(Particle.GLOW, ghostLoc, 15, 
                            pulse, pulse, pulse, 0.05);
                    ghostLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, ghostLoc, 8, 1, 1, 1, 0.02);

                    // Aura rings
                    for (int ring = 0; ring < 3; ring++) {
                        double radius = (ring + 1) * 1.5;
                        Location ringLoc = ghostLoc.clone().add(
                                Math.cos(System.currentTimeMillis() * 0.005 + ring) * radius,
                                0.5,
                                Math.sin(System.currentTimeMillis() * 0.005 + ring) * radius
                        );
                        ghostLoc.getWorld().spawnParticle(Particle.SPIT, ringLoc, 5, 0.2, 0.2, 0.2, 0);
                    }
                }
            }
        }.runTaskTimer(instance, 0L, 3L); // runs every 3 ticks
    }

    // Optional: utility to register a ghost location
    public void addGhostLocation(UUID playerUUID, Location location) {
        ghostLocations.put(playerUUID, location);
    }

    public void removeGhostLocation(UUID playerUUID) {
        ghostLocations.remove(playerUUID);
    }
}
