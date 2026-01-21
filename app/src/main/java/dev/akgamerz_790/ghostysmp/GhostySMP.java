public void startAuraTask() {
    new BukkitRunnable() {
        public void run() {
            for (var entry : new HashMap<>(ghostLocations).entrySet()) {
                Player owner = Bukkit.getPlayer(entry.getKey());
                if (owner == null) continue;
                Location ghostLoc = entry.getValue();
                
                // 🌌 PULSING GHOST ORB
                double pulse = Math.sin(System.currentTimeMillis() * 0.01) * 0.3 + 0.7;
                ghostLoc.getWorld().spawnParticle(Particle.GLOW, ghostLoc, 15, 
                    pulse, pulse, pulse, 0.05);
                ghostLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, ghostLoc, 8, 1, 1, 1, 0.02);
                
                // AURA RINGS
                for (int ring = 0; ring < 3; ring++) {
                    double r = (ring + 1) * 1.5;
                    Location ringLoc = ghostLoc.clone().add(
                        Math.cos(System.currentTimeMillis() * 0.005 + ring) * r,
                        0.5,
                        Math.sin(System.currentTimeMillis() * 0.005 + ring) * r
                    );
                    ghostLoc.getWorld().spawnParticle(Particle.SPIT, ringLoc, 5, 0.2, 0.2, 0.2, 0);
                }
            }
        }
    }.runTaskTimer(GhostySMP.instance, 0L, 3L);
}
