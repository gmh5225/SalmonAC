package com.ch1ck3n.salmonac.checks.movement;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;

public class Speed extends Check {
    public Speed(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {
        if( e.getPlayer().getGameMode() == GameMode.CREATIVE )
            return;


        // Type A (Stable)
        if( e.getRespawnTick() < 20 || e.getCollidingHorizontallyTick() < 2 || e.getCollidingVerticallyUpTick() == 0 ||
                e.isOnLadder() || e.getServerAirTick() < 2 || e.getSetBackTick() < 2 ||
                e.isServerGround() || e.isTouchingLiquid() || e.getWebTick() == 0 ) {} else {
            if (e.getDeltaXZ() != 0 && e.getLastDeltaXZ() != 0 && e.getLastDeltaXZ() == e.getDeltaXZ()) {
                this.setType("(A)");
                this.setVlPerFail(1.0f);
                flag(e.getPlayer(), "DeltaXZ = " + e.getDeltaXZ() + "\nLastDeltaXZ = " + e.getLastDeltaXZ());
            }
        }

        // Type B (Prediction)
        if( e.getRespawnTick() < 20 || e.isFuzzyCollidingHorizontally() || e.getCollidingHorizontallyTick() < 2 ||
                e.getDamageTick() < 2 || e.isOnLadder() || e.getSetBackTick() < 3 || e.isFuzzyServerGround() ||
                e.getServerAirTick() < 2 || e.isTouchingLiquid() || e.getWebTick() == 0 ) {} else {
            float f4 = 0.91f;
            if (e.isServerGround()) {
                if (e.getIceTick() == 0) f4 *= 0.98f;
                else if (e.getSlimeTick() == 0) f4 *= 0.8f;
                else f4 *= 0.6f;
            }
            double prediction = e.getLastDeltaXZ() * f4 + 0.026D;
            if (e.getDeltaXZ() - prediction > 0.001) {
                e.getSalmonPlayer().speedBBuffer.onTick();
                if ( e.getSalmonPlayer().speedBBuffer.getTick() > 1 ) {
                    this.setType("(B)");
                    this.setVlPerFail(2.0f);
                    flag(e.getPlayer(), "DeltaXZ = " + e.getDeltaXZ() +
                            "\nPrediction = " + prediction +
                            "\nSprinting = " + e.getPlayer().isSprinting());
                }
            }
        }

        // Type C (Invalid)
        if( e.getRespawnTick() < 20 || e.getTeleportTick() < 20 || e.getDamageTick() < 20 ) {} else {
            if ( e.getDeltaXZ() > 10 ) {
                this.setType("(C)");
                this.setVlPerFail(Float.parseFloat(String.format("%.1f", 1.0f * Math.round(e.getDeltaXZ()) / 5)));
                flag( e.getPlayer(), "DeltaXZ = " + e.getDeltaXZ() );
            }
        }
    }
}
