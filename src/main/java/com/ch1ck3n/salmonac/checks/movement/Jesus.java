package com.ch1ck3n.salmonac.checks.movement;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import org.bukkit.event.EventHandler;

public class Jesus extends Check {
    public Jesus(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {

        // Type A
        if( e.getRespawnTick() < 20 || e.isInLiquid() || e.getSetBackTick() < 2 ) {} else {
            if (!e.isServerGround() && !e.isLastServerGround() &&
                    e.isTouchingLiquid() && e.getLilyAround() == 0 &&
                    e.getLastDeltaY() == 0 && e.getDeltaY() == 0) {
                this.setType("(A)");
                this.setVlPerFail(1.0f);
                flag(e.getPlayer(), "ServerGround = " + e.isServerGround() +
                        "\nLastServerGround = " + e.isLastServerGround() +
                        "\nDeltaY = " + e.getDeltaY() +
                        "\nLastDeltaY = " + e.getLastDeltaY());
            }
        }

        // Type B (StableAccel)
        if( e.getRespawnTick() < 20 || e.isOnLadder() || e.isInLiquid() || !e.isTouchingLiquid() || e.isTouchingStair() ) {} else {
            if ( e.getDeltaYAccel() != 0 && e.getLastDeltaYAccel() != 0 && Math.abs(e.getDeltaYAccel()) == Math.abs(e.getLastDeltaYAccel()) ) {
                e.getSalmonPlayer().jesusBBuffer.onTick();
                if (e.getSalmonPlayer().jesusBBuffer.getTick() > 3) {
                    this.setType("(B)");
                    this.setVlPerFail(2.0f);
                    flag(e.getPlayer(), "DeltaYAccel = " + String.format("%.10f", Math.abs(e.getDeltaYAccel())) +
                            "\nLastDeltaYAccel = " + String.format("%.10f", Math.abs(e.getLastDeltaYAccel())));
                }
            }
        }

        // Type C
        if( e.getRespawnTick() < 20 || e.getLilyAround() != 0  || e.isInLiquid() || e.isOnLadder() || e.isTouchingStair() ) {} else {
            if ( e.isLastTouchingLiquid() && e.isLastMathGround() && e.getLastDeltaY() < 0 && e.getDeltaY() > 0 && Math.abs(e.getDeltaY() - e.getLastDeltaY()) > 0.1 ) {
//                e.getCustomPlayer().jesusCBuffer.onTick();
//                if (e.getCustomPlayer().jesusCBuffer.getTick() > 2) {
                    this.setType("(C)");
                    this.setVlPerFail(2.5f);
                    flag(e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) +
                            "\nLastDeltaY = " + String.format("%.10f", e.getLastDeltaY()));
//                }
            }
        }
    }
}
