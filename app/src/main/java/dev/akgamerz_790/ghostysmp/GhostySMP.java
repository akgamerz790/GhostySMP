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

    // Config values
    private static int globalCooldownSeconds;
    private static double draugrDamage;
    private static int poltergeistRadius;
    private static int poltergeistHeightUp;
    private static int poltergeistHeightDown;
    private static double poltergeistDestructionChance;
    private static double poltergeistVelocityXMultiplier;
    private static double poltergeistVelocityZMultiplier;
    private static double poltergeistVelocityYBase;
    private static double poltergeistVelocityYRandom;
    private static int anchorsParticleCount;
    private static double anchorsParticleSpread;
    private static int disruptorsReflectDurationTicks;
    private static int disruptorsParticleCount;
    private static double disruptorsParticleSpread;
    private static double shifterTeleportHeight;
    private static int shifterLockDurationSeconds;
    private static int shifterParticleCount;
    private static double shifterParticleSpread;
    private static int infectorParticleCount;
    private static double infectorParticleSpread;
    private static double drainerDamage;
    private static double drainerDotDamage;
    private static int drainerDotDurationSeconds;
    private static int scavengerBowDurationTicks;
    private static int trackerAbsorptionDurationTicks;
    private static int trackerAbsorptionAmplifier;
    private static double reeperDamage;
    private static double reeperDotDamage;
    private static int reeperDotDurationSeconds;
    private static String summonersGuardName;
    private static int summonersParticleCount;
    private static double summonersParticleSpread;
    private static int utilsLockSlownessAmplifier;
    private static int utilsLockLevitationAmplifier;
    private static int utilsDotIntervalTicks;
    private static int utilsGodBowDurationTicks;
    private static int utilsTrackerAbsorptionDurationTicks;
    private static int utilsTrackerAbsorptionAmplifier;
    private static int utilsGhostParticlesExplosionCount;
    private static int utilsGhostParticlesFlameCount;
    private static int utilsGhostParticlesSmokeCount;
    private static double utilsGhostParticlesRingRadius;
    private static int utilsGhostParticlesDurationTicks;
    private static double utilsGhostParticlesRingAngleIncrement;
    private static int auraTaskIntervalTicks;
    private static double auraOrbPulseMin;
    private static double auraOrbPulseAmplitude;
    private static int auraOrbParticleCount;
    private static int auraFlameParticleCount;
    private static int auraRingCount;
    private static double auraRingRadiusMultiplier;
    private static double auraRingAngleSpeed;
    private static int auraSpitParticleCount;

    @Override
public void onEnable() {
    instance = this;

    saveDefaultConfig();
    loadConfig();

    Bukkit.getPluginManager().registerEvents(new GhostManager(), this);

    startAuraTask();
    getLogger().info("GhostySMP enabled!");
}


    @Override
    public void onDisable() {
        getLogger().info("GhostySMP disabled!");
    }

    private void loadConfig() {
        globalCooldownSeconds = getConfig().getInt("global.cooldown_seconds", 17);
        draugrDamage = getConfig().getDouble("abilities.draugr.damage", 14.0);
        poltergeistRadius = getConfig().getInt("abilities.poltergeist.radius", 6);
        poltergeistHeightUp = getConfig().getInt("abilities.poltergeist.height_up", 4);
        poltergeistHeightDown = getConfig().getInt("abilities.poltergeist.height_down", 5);
        poltergeistDestructionChance = getConfig().getDouble("abilities.poltergeist.destruction_chance", 0.85);
        poltergeistVelocityXMultiplier = getConfig().getDouble("abilities.poltergeist.velocity_x_multiplier", 0.2);
        poltergeistVelocityZMultiplier = getConfig().getDouble("abilities.poltergeist.velocity_z_multiplier", 0.3);
        poltergeistVelocityYBase = getConfig().getDouble("abilities.poltergeist.velocity_y_base", 0.9);
        poltergeistVelocityYRandom = getConfig().getDouble("abilities.poltergeist.velocity_y_random", 0.25);
        anchorsParticleCount = getConfig().getInt("abilities.anchors.particle_count", 50);
        anchorsParticleSpread = getConfig().getDouble("abilities.anchors.particle_spread", 3.0);
        disruptorsReflectDurationTicks = getConfig().getInt("abilities.disruptors.reflect_duration_ticks", 300);
        disruptorsParticleCount = getConfig().getInt("abilities.disruptors.particle_count", 100);
        disruptorsParticleSpread = getConfig().getDouble("abilities.disruptors.particle_spread", 2.0);
        shifterTeleportHeight = getConfig().getDouble("abilities.shifter.teleport_height", 50.0);
        shifterLockDurationSeconds = getConfig().getInt("abilities.shifter.lock_duration_seconds", 15);
        shifterParticleCount = getConfig().getInt("abilities.shifter.particle_count", 100);
        shifterParticleSpread = getConfig().getDouble("abilities.shifter.particle_spread", 2.0);
        infectorParticleCount = getConfig().getInt("abilities.infector.particle_count", 80);
        infectorParticleSpread = getConfig().getDouble("abilities.infector.particle_spread", 4.0);
        drainerDamage = getConfig().getDouble("abilities.drainer.damage", 20.0);
        drainerDotDamage = getConfig().getDouble("abilities.drainer.dot_damage", 2.0);
        drainerDotDurationSeconds = getConfig().getInt("abilities.drainer.dot_duration_seconds", 10);
        scavengerBowDurationTicks = getConfig().getInt("abilities.scavenger.bow_duration_ticks", 300);
        trackerAbsorptionDurationTicks = getConfig().getInt("abilities.tracker.absorption_duration_ticks", 200);
        trackerAbsorptionAmplifier = getConfig().getInt("abilities.tracker.absorption_amplifier", 2);
        reeperDamage = getConfig().getDouble("abilities.reeper.damage", 34.0);
        reeperDotDamage = getConfig().getDouble("abilities.reeper.dot_damage", 2.0);
        reeperDotDurationSeconds = getConfig().getInt("abilities.reeper.dot_duration_seconds", 5);
        summonersGuardName = getConfig().getString("abilities.summoners.guard_name", "Guard");
        summonersParticleCount = getConfig().getInt("abilities.summoners.particle_count", 50);
        summonersParticleSpread = getConfig().getDouble("abilities.summoners.particle_spread", 2.0);
        utilsLockSlownessAmplifier = getConfig().getInt("utils.lock_player.slowness_amplifier", 9);
        utilsLockLevitationAmplifier = getConfig().getInt("utils.lock_player.levitation_amplifier", 0);
        utilsDotIntervalTicks = getConfig().getInt("utils.dot_damage.interval_ticks", 20);
        utilsGodBowDurationTicks = getConfig().getInt("utils.god_bow.duration_ticks", 300);
        utilsTrackerAbsorptionDurationTicks = getConfig().getInt("utils.tracker.absorption_duration_ticks", 200);
        utilsTrackerAbsorptionAmplifier = getConfig().getInt("utils.tracker.absorption_amplifier", 2);
        utilsGhostParticlesExplosionCount = getConfig().getInt("utils.ghost_particles.explosion_count", 15);
        utilsGhostParticlesFlameCount = getConfig().getInt("utils.ghost_particles.flame_count", 40);
        utilsGhostParticlesSmokeCount = getConfig().getInt("utils.ghost_particles.smoke_count", 30);
        utilsGhostParticlesRingRadius = getConfig().getDouble("utils.ghost_particles.ring_radius", 2.5);
        utilsGhostParticlesDurationTicks = getConfig().getInt("utils.ghost_particles.duration_ticks", 200);
        utilsGhostParticlesRingAngleIncrement = getConfig().getDouble("utils.ghost_particles.ring_angle_increment", 0.2618);
        auraTaskIntervalTicks = getConfig().getInt("aura.task_interval_ticks", 3);
        auraOrbPulseMin = getConfig().getDouble("aura.orb_pulse_min", 0.7);
        auraOrbPulseAmplitude = getConfig().getDouble("aura.orb_pulse_amplitude", 0.3);
        auraOrbParticleCount = getConfig().getInt("aura.orb_particle_count", 15);
        auraFlameParticleCount = getConfig().getInt("aura.flame_particle_count", 8);
        auraRingCount = getConfig().getInt("aura.ring_count", 3);
        auraRingRadiusMultiplier = getConfig().getDouble("aura.ring_radius_multiplier", 1.5);
        auraRingAngleSpeed = getConfig().getDouble("aura.ring_angle_speed", 0.005);
        auraSpitParticleCount = getConfig().getInt("aura.spit_particle_count", 5);
    }

    // Config accessors
    public static int getGlobalCooldownSeconds() { return globalCooldownSeconds; }
    public static double getDraugrDamage() { return draugrDamage; }
    public static int getPoltergeistRadius() { return poltergeistRadius; }
    public static int getPoltergeistHeightUp() { return poltergeistHeightUp; }
    public static int getPoltergeistHeightDown() { return poltergeistHeightDown; }
    public static double getPoltergeistDestructionChance() { return poltergeistDestructionChance; }
    public static double getPoltergeistVelocityXMultiplier() { return poltergeistVelocityXMultiplier; }
    public static double getPoltergeistVelocityZMultiplier() { return poltergeistVelocityZMultiplier; }
    public static double getPoltergeistVelocityYBase() { return poltergeistVelocityYBase; }
    public static double getPoltergeistVelocityYRandom() { return poltergeistVelocityYRandom; }
    public static int getAnchorsParticleCount() { return anchorsParticleCount; }
    public static double getAnchorsParticleSpread() { return anchorsParticleSpread; }
    public static int getDisruptorsReflectDurationTicks() { return disruptorsReflectDurationTicks; }
    public static int getDisruptorsParticleCount() { return disruptorsParticleCount; }
    public static double getDisruptorsParticleSpread() { return disruptorsParticleSpread; }
    public static double getShifterTeleportHeight() { return shifterTeleportHeight; }
    public static int getShifterLockDurationSeconds() { return shifterLockDurationSeconds; }
    public static int getShifterParticleCount() { return shifterParticleCount; }
    public static double getShifterParticleSpread() { return shifterParticleSpread; }
    public static int getInfectorParticleCount() { return infectorParticleCount; }
    public static double getInfectorParticleSpread() { return infectorParticleSpread; }
    public static double getDrainerDamage() { return drainerDamage; }
    public static double getDrainerDotDamage() { return drainerDotDamage; }
    public static int getDrainerDotDurationSeconds() { return drainerDotDurationSeconds; }
    public static int getScavengerBowDurationTicks() { return scavengerBowDurationTicks; }
    public static int getTrackerAbsorptionDurationTicks() { return trackerAbsorptionDurationTicks; }
    public static int getTrackerAbsorptionAmplifier() { return trackerAbsorptionAmplifier; }
    public static double getReeperDamage() { return reeperDamage; }
    public static double getReeperDotDamage() { return reeperDotDamage; }
    public static int getReeperDotDurationSeconds() { return reeperDotDurationSeconds; }
    public static String getSummonersGuardName() { return summonersGuardName; }
    public static int getSummonersParticleCount() { return summonersParticleCount; }
    public static double getSummonersParticleSpread() { return summonersParticleSpread; }
    public static int getUtilsLockSlownessAmplifier() { return utilsLockSlownessAmplifier; }
    public static int getUtilsLockLevitationAmplifier() { return utilsLockLevitationAmplifier; }
    public static int getUtilsDotIntervalTicks() { return utilsDotIntervalTicks; }
    public static int getUtilsGodBowDurationTicks() { return utilsGodBowDurationTicks; }
    public static int getUtilsTrackerAbsorptionDurationTicks() { return utilsTrackerAbsorptionDurationTicks; }
    public static int getUtilsTrackerAbsorptionAmplifier() { return utilsTrackerAbsorptionAmplifier; }
    public static int getUtilsGhostParticlesExplosionCount() { return utilsGhostParticlesExplosionCount; }
    public static int getUtilsGhostParticlesFlameCount() { return utilsGhostParticlesFlameCount; }
    public static int getUtilsGhostParticlesSmokeCount() { return utilsGhostParticlesSmokeCount; }
    public static double getUtilsGhostParticlesRingRadius() { return utilsGhostParticlesRingRadius; }
    public static int getUtilsGhostParticlesDurationTicks() { return utilsGhostParticlesDurationTicks; }
    public static double getUtilsGhostParticlesRingAngleIncrement() { return utilsGhostParticlesRingAngleIncrement; }
    public static int getAuraTaskIntervalTicks() { return auraTaskIntervalTicks; }
    public static double getAuraOrbPulseMin() { return auraOrbPulseMin; }
    public static double getAuraOrbPulseAmplitude() { return auraOrbPulseAmplitude; }
    public static int getAuraOrbParticleCount() { return auraOrbParticleCount; }
    public static int getAuraFlameParticleCount() { return auraFlameParticleCount; }
    public static int getAuraRingCount() { return auraRingCount; }
    public static double getAuraRingRadiusMultiplier() { return auraRingRadiusMultiplier; }
    public static double getAuraRingAngleSpeed() { return auraRingAngleSpeed; }
    public static int getAuraSpitParticleCount() { return auraSpitParticleCount; }

    // Optional: command handler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ghostaura")) {
            if (sender instanceof Player player) {
                ghostLocations.put(player.getUniqueId(), player.getLocation());
                player.sendMessage("Bhosdike ghost aura tere location pe enable ho gaya! Ab dikha sabko ki tu hai kon.");
                return true;
            }
            sender.sendMessage("Sala players hi use kar sakte hain, tu console hai kya?");
            return true;
        } else if (command.getName().equalsIgnoreCase("ghost")) {
            return dev.akgamerz_790.ghostysmp.abilities.AbilityHandler.handleCommand(sender, args);
        } else if (command.getName().equalsIgnoreCase("ghostreload")) {
            if (sender.hasPermission("ghostsmp.reload")) {
                reloadConfig();
                loadConfig();
                sender.sendMessage("GhostySMP config reload ho gaya, ab naya drama shuru kar!");
                return true;
            } else {
                sender.sendMessage("Tere paas permission nahi hai reload karne ki, gandu! Jaake permission maang.");
                return true;
            }
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
                    double pulse = Math.sin(System.currentTimeMillis() * 0.01) * getAuraOrbPulseAmplitude() + getAuraOrbPulseMin();
                    ghostLoc.getWorld().spawnParticle(Particle.GLOW, ghostLoc, getAuraOrbParticleCount(),
                            pulse, pulse, pulse, 0.05);
                    ghostLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, ghostLoc, getAuraFlameParticleCount(), 1, 1, 1, 0.02);

                    // Aura rings
                    for (int ring = 0; ring < getAuraRingCount(); ring++) {
                        double radius = (ring + 1) * getAuraRingRadiusMultiplier();
                        Location ringLoc = ghostLoc.clone().add(
                                Math.cos(System.currentTimeMillis() * getAuraRingAngleSpeed() + ring) * radius,
                                0.5,
                                Math.sin(System.currentTimeMillis() * getAuraRingAngleSpeed() + ring) * radius
                        );
                        ghostLoc.getWorld().spawnParticle(Particle.SPIT, ringLoc, getAuraSpitParticleCount(), 0.2, 0.2, 0.2, 0);
                    }
                }
            }
        }.runTaskTimer(instance, 0L, getAuraTaskIntervalTicks());
    }

    // Optional: utility to register a ghost location
    public void addGhostLocation(UUID playerUUID, Location location) {
        ghostLocations.put(playerUUID, location);
    }

    public void removeGhostLocation(UUID playerUUID) {
        ghostLocations.remove(playerUUID);
    }
}
