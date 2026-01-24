package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;
import dev.akgamerz_790.ghostysmp.utils.Util;

public class DrainerAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        if (target != null && !target.equals(owner)) {
            target.damage(GhostySMP.getDrainerDamage());
            Util.dotDamage(target, GhostySMP.getDrainerDotDamage(), GhostySMP.getDrainerDotDurationSeconds());
        }
    }
}
