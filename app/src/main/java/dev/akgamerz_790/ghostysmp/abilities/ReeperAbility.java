package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.utils.Util;

public class ReeperAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        if (target != null && !target.equals(owner)) {
            target.damage(GhostySMP.getReeperDamage());
            Util.dotDamage(target, GhostySMP.getReeperDotDamage(), GhostySMP.getReeperDotDurationSeconds());
        }
    }
}
