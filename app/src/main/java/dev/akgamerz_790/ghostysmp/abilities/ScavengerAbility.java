package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.utils.Util;

public class ScavengerAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        Util.giveGodBow(owner);
    }
}
