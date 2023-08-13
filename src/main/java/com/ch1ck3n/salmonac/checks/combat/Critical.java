package com.ch1ck3n.salmonac.checks.combat;

import com.ch1ck3n.salmonac.SalmonAC;
import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.utils.SalmonPlayer;
import com.ch1ck3n.salmonac.utils.PlayerUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Critical extends Check {
    public Critical(String name, Check.Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player){
            Player player = ((Player)e.getDamager()).getPlayer();
            SalmonPlayer salmonPlayer = SalmonAC.getInstance().getPlayerManager().getPlayer(player);

            double expectedDamage = (PlayerUtil.getWeaponDamage(player.getItemInHand().getType()) + (PlayerUtil.getEnchantmentLevel(player, Enchantment.DAMAGE_ALL) * 1.25));

            // Type A
            // Crits with 0 FallDistance
            if( salmonPlayer.getRespawnTick() < 20 || salmonPlayer.getItemChangeTick() < 1 ) {} else {
                if ( salmonPlayer.getFallDistance() == 0 && e.getDamage() != expectedDamage ) {
                    salmonPlayer.criticalABuffer.onTick();
                    if(salmonPlayer.criticalABuffer.getTick() > 1) {
                        this.setType("(A)");
                        this.setVlPerFail(2.0f);
                        flag(player, "ExpectedDamage = " + expectedDamage +
                                "\nActualDamage = " + e.getDamage() +
                                "\nFallDistance = " + String.format("%.10f", salmonPlayer.getFallDistance()) +
                                "\nServerGround = " + salmonPlayer.isServerGround() +
                                "\nClientGround = " + salmonPlayer.isClientGround() +
                                "\nItemInHand = " + player.getItemInHand().getType() +
                                (this.getResponse() == Response.FIX ? "\n\nFix damage to " + expectedDamage : "") +
                                (this.getResponse() == Response.CANCEL ? "\n\nEvent cancelled" : ""));
                        if (this.getResponse() == Response.FIX) {
                            e.setDamage(expectedDamage);
                        } else if (this.getResponse() == Response.CANCEL) {
                            e.setCancelled(true);
                        }
                    }
                }
            }

            // Type B
            // Crits with FallDistance but Different GroundState
            if( salmonPlayer.getRespawnTick() < 20 || salmonPlayer.isTouchingStair() ) {} else {
                if( salmonPlayer.getFallDistance() != 0 && !salmonPlayer.isServerGround() &&
                        salmonPlayer.isClientGround() && e.getDamage() != expectedDamage){
                    this.setType("(B)");
                    this.setVlPerFail(2.0f);
                    flag( player,"ExpectedDamage = " + expectedDamage +
                            "\nActualDamage = " + e.getDamage() +
                            "\nFallDistance = " + String.format("%.10f", salmonPlayer.getFallDistance()) +
                            "\nServerGround = " + salmonPlayer.isServerGround() +
                            "\nClientGround = " + salmonPlayer.isClientGround() +
                            "\nItemInHand = " + player.getItemInHand().getType() +
                            (this.getResponse() == Response.FIX ? "\n\nFix damage to " + expectedDamage: "") +
                            (this.getResponse() == Response.CANCEL ? "\n\nEvent cancelled" : ""));
                    if( this.getResponse() == Response.FIX ) {
                        e.setDamage( expectedDamage );
                    }else if( this.getResponse() == Response.CANCEL ) {
                        e.setCancelled(true);
                    }
                }
            }

            // Type C
            // Crits with bad GroundState
            if( salmonPlayer.getRespawnTick() < 20 || salmonPlayer.lastDeltaY == 0  ) {} else {
                if( salmonPlayer.getFallDistance() != 0 && salmonPlayer.isServerGround() && salmonPlayer.isClientGround() ){
                    salmonPlayer.criticalCBuffer.onTick();
                    if(salmonPlayer.criticalCBuffer.getTick() > 1){
                        this.setType("(C)");
                        this.setVlPerFail(1.0f);
                        flag( player,"ExpectedDamage = " + expectedDamage +
                                "\nActualDamage = " + e.getDamage() +
                                "\nFallDistance = " + String.format("%.10f", salmonPlayer.getFallDistance()) +
                                "\nServerGround = " + salmonPlayer.isServerGround() +
                                "\nClientGround = " + salmonPlayer.isClientGround() +
                                "\nItemInHand = " + player.getItemInHand().getType() +
                                (this.getResponse() == Response.CANCEL ? "\n\nEvent cancelled" : ""));
                        if( this.getResponse() == Response.CANCEL ) {
                            e.setCancelled(true);
                        }
                    }
                }else {
                    salmonPlayer.criticalCBuffer.reduceTick();
                }
            }
        }
    }
}
