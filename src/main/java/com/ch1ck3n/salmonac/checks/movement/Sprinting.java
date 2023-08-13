package com.ch1ck3n.salmonac.checks.movement;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import org.bukkit.event.EventHandler;

public class Sprinting extends Check {
    public Sprinting(String name, Check.Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {

        // Type A
        if( e.getPlayer().isBlocking() && e.getPlayer().isSprinting() ) {
            this.setType("(A)");
            this.setVlPerFail(2.0f);
            flag(e.getPlayer(), "Blocking = " + e.getPlayer().isBlocking() +
                    "\nSprinting = " + e.getPlayer().isSprinting());
        }

        // Type B
        if( e.getHungryTick() < 2 ) {} else {
            if (e.getPlayer().getFoodLevel() <= 6 && e.getPlayer().isSprinting()) {
                this.setType("(B)");
                this.setVlPerFail(2.0f);
                flag(e.getPlayer(), "FoodLevel = " + e.getPlayer().getFoodLevel() +
                        "\nSprinting = " + e.getPlayer().isSprinting());
            }
        }

        // Type C
        if( e.getPlayer().isSneaking() && e.getPlayer().isSprinting() ) {
            this.setType("(C)");
            this.setVlPerFail(4.0f);
            flag(e.getPlayer(), "Sneaking = " + e.getPlayer().isSneaking() +
                    "\nSprinting = " + e.getPlayer().isSprinting());
        }
    }
}
