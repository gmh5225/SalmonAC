package com.ch1ck3n.salmonac.checks.movement;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import org.bukkit.event.EventHandler;

public class Step extends Check {
    public Step(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {

        // Type A
        if( e.getRespawnTick() < 20 || e.getSetBackTick() < 2 || e.getTeleportTick() < 20 || e.isInBlock() ) {} else {
            if (e.isServerGround() && e.isLastServerGround() && e.getDeltaY() > 0.6) {
                this.setType("(A)");
                this.setVlPerFail(3.0f);
                flag(e.getPlayer(), "StepHeight = " + e.getDeltaY() +
                        "\nServerGround = " + e.isServerGround() +
                        "\nLastServerGround = " + e.isLastServerGround());
            }
        }

        // Type B
        if( e.getRespawnTick() < 20 || e.getSetBackTick() < 2 || e.getTeleportTick() < 20 || e.isInBlock() ||
                e.isTouchingClimable() || e.isTouchingLiquid() || e.isTouchingSlab() || e.isTouchingStair()) {} else {
            if (e.isClientGround() && e.isLastClientGround() && e.isServerGround() && e.getDeltaY() > 0D && e.getDeltaY() % 0.125D != 0 ) {
                this.setType("(B)");
                this.setVlPerFail(2.0f);
                flag(e.getPlayer(), "StepHeight = " + e.getDeltaY() +
                        "\nClientGround = " + e.isClientGround() +
                        "\nLastClientGround = " + e.isLastClientGround());
            }
        }
    }
}
