package com.ch1ck3n.salmonac.checks.combat;

import com.ch1ck3n.salmonac.SalmonAC;
import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.utils.SalmonPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Blocking extends Check {
    public Blocking(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if( e.getDamager() instanceof Player ) {
            Player player = ((Player)e.getDamager()).getPlayer();

            // Type A
            if( player.isBlocking() ) {
                this.setType("(A)");
                this.setVlPerFail(5.0f);
                flag( player, "Blocking = " + player.isBlocking() +
                        (this.getResponse() == Response.CANCEL ? "\n\nEvent cancelled" : "") );
                if( this.getResponse() == Response.CANCEL ) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
