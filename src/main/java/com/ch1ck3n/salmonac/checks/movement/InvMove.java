package com.ch1ck3n.salmonac.checks.movement;

import com.ch1ck3n.salmonac.SalmonAC;
import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.utils.SalmonPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvMove extends Check {
    public InvMove(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        SalmonPlayer salmonPlayer = SalmonAC.getInstance().getPlayerManager().getPlayer( (Player)e.getWhoClicked() );

        // Type A
        // Click gui while sprinting
        if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
            if (salmonPlayer.getPlayer().isSprinting()) {
                this.setType("(A)");
                this.setVlPerFail(1.0f);
                flag(salmonPlayer.getPlayer(), "Sprinting = " + salmonPlayer.getPlayer().isSprinting() +
                        (this.getResponse() == Response.CANCEL ? "\n\nEvent is cancelled" : ""));
                if (this.getResponse() == Response.CANCEL) {
                    e.setCancelled(true);
                }
            }
        }

        // Type B
        // Click gui while sneaking
        if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
            if (salmonPlayer.getPlayer().isSneaking()) {
                this.setType("(B)");
                this.setVlPerFail(1.0f);
                flag(salmonPlayer.getPlayer(), "Sneaking = " + salmonPlayer.getPlayer().isSneaking() +
                        (this.getResponse() == Response.CANCEL ? "\n\nEvent is cancelled" : ""));
                if (this.getResponse() == Response.CANCEL) {
                    e.setCancelled(true);
                }
            }
        }

        // Type C
        // Click gui while attacking
        if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
            if (salmonPlayer.getAttackTick() == 0) {
                this.setType("(C)");
                this.setVlPerFail(2.0f);
                flag(salmonPlayer.getPlayer(), "AttackTick = " + salmonPlayer.getAttackTick() +
                        (this.getResponse() == Response.CANCEL ? "\n\nEvent is cancelled" : ""));
                if (this.getResponse() == Response.CANCEL) {
                    e.setCancelled(true);
                }
            }
        }

        // Type D
        // Click gui while rotating
        if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
            if (salmonPlayer.getDeltaYaw() != 0 || salmonPlayer.getDeltaPitch() != 0) {
                salmonPlayer.invMoveDBuffer.onTick();
                if (salmonPlayer.invMoveDBuffer.getTick() > 1) {
                    this.setType("(D)");
                    this.setVlPerFail(1.0f);
                    flag(salmonPlayer.getPlayer(), "DeltaYaw = " + salmonPlayer.getDeltaYaw() +
                            "\nDeltaPitch = " + salmonPlayer.getDeltaPitch() +
                            (this.getResponse() == Response.CANCEL ? "\n\nEvent is cancelled" : ""));
                    if (this.getResponse() == Response.CANCEL) {
                        e.setCancelled(true);
                    }
                }
            }
        }

        // Type E
        // Click gui while placing block
        if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
            if (salmonPlayer.getPlaceBlockTick() == 0) {
                this.setType("(E)");
                this.setVlPerFail(2.0f);
                flag(salmonPlayer.getPlayer(), "PlaceBlockTick = " + salmonPlayer.getPlaceBlockTick() +
                        (this.getResponse() == Response.CANCEL ? "\n\nEvent is cancelled" : ""));
                if (this.getResponse() == Response.CANCEL) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if ( e.getDamager() instanceof Player ) {
            SalmonPlayer salmonPlayer = SalmonAC.getInstance().getPlayerManager().getPlayer( ((Player)e.getDamager()) );

            if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
                if (salmonPlayer.getGuiClickedTick() == 0) {
                    if (this.getResponse() == Response.CANCEL) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        SalmonPlayer salmonPlayer = SalmonAC.getInstance().getPlayerManager().getPlayer( e.getPlayer() );

        if( salmonPlayer.getPlayer().getGameMode() == GameMode.CREATIVE ) {} else {
            if (salmonPlayer.getGuiClickedTick() == 0) {
                if (this.getResponse() == Response.CANCEL) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
