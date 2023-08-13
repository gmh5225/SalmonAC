package com.ch1ck3n.salmonac.checks.combat;

import com.ch1ck3n.salmonac.SalmonAC;
import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.utils.SalmonPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Swing extends Check {
    public Swing(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player player = ((Player) e.getDamager()).getPlayer();
            SalmonPlayer salmonPlayer = SalmonAC.getInstance().getPlayerManager().getPlayer(player);

            // Type A
            if( salmonPlayer.getSwingTick() > 2){
                this.setType("(A)");
                this.setVlPerFail(5.0f);
                flag( player, "SwingTick = " + salmonPlayer.getSwingTick() );
                if( this.getResponse() == Response.CANCEL ) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
