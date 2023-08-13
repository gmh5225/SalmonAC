package com.ch1ck3n.salmonac.checks;

import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import org.bukkit.event.EventHandler;

public class Debugger extends Check {
    public Debugger(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {
        this.setMaxVL(-1.0f);
        /* ----- Jesus Debug Info ----- */
//        e.getPlayer().sendMessage(e.isClientGround() + "/" + e.isLastClientGround() + "/" +
//                e.isServerGround() + "/" + e.isLastServerGround() + "/" +
//                e.isTouchingLiquid() + "/" + (e.getDeltaY() == 0));

//        e.getPlayer().sendMessage(e.isLastTouchingLiquid() +"/"+ e.isLastMathGround()+"/"+e.getLastDeltaY()+"/"+e.getDeltaY()+"/"+e.getTo().getY() );

//        e.getPlayer().sendMessage(e.isSlimeAround() +"/"+e.getLastDeltaY()+"/"+e.getDeltaY() );

//        e.getPlayer().sendMessage(e.isLastCanJump()+"/"+e.isJumping()+"/"+e.isJumpUpwards() );

//        e.getPlayer().sendMessage(String.format("%.3f", e.getDeltaY())+"/"+String.format("%.3f", e.getLastDeltaY())+"/"+
//                        e.isClientGround()+"/"+e.isLastClientGround()+"/"+e.isServerGround()+"/"+e.isLastServerGround());

//                e.getPlayer().sendMessage(e.isFuzzyServerGround()+"/"+e.isFuzzyCollidingHorizontally());

        /* ----- NoFall Debug Info ----- */
//        e.getPlayer().sendMessage(
//                e.getFallDistance() + "/" + e.getPlayer().getFallDistance() + "/" +
//                e.getDeltaY());

//        e.getPlayer().sendMessage(String.format("%3f", e.getDeltaY()) +
//                "/" + String.format("%3f", e.getLastDeltaY()) +
//                "/" + e.isServerGround() +
//                "/" + e.isLastServerGround());

//       e.getPlayer().sendMessage(String.format("%2f",e.getDeltaY())+"/"+String.format("%2f",e.getLastDeltaY())+"/" +
//               e.isClientGround() + "/" + e.isLastClientGround() + "/" +
//               e.isServerGround() + "/" + e.isLastServerGround());

//        e.getPlayer().sendMessage(String.format("%2f",e.getCustomPlayer().getVelocityH())+"/"+String.format("%2f",e.getCustomPlayer().getVelocityY())+"/" +
//                e.getCustomPlayer().getVelocityTick()+"/"+String.format("%3f", e.getDeltaXZ())+"/"+
//                String.format("%3f", e.getDeltaY()) + "/" + String.format("%3f", e.getLastDeltaY()));

//        e.getPlayer().sendMessage(Float.parseFloat(String.format("%6f", e.getDeltaYaw())) - Float.parseFloat(String.format("%6f", e.getLastDeltaPitch())) + "/" +
//                (Float.parseFloat(String.format("%6f", e.getDeltaPitch())) - Float.parseFloat(String.format("%6f", e.getLastDeltaPitch()))));

//        e.getPlayer().sendMessage(String.format("%2f",e.getDeltaYawAccel())+"/"+String.format("%2f",e.getLastDeltaYawAccel()));

//        e.getPlayer().sendMessage(String.format("%2f",e.getDeltaYaw())+"/"+String.format("%2f",e.getLastDeltaYaw()));

        /*e.getPlayer().sendMessage(e.isClientGround() + "/" + e.isLastClientGround() + "/" +
                e.isServerGround() + "/" + e.isLastServerGround() + "/" +
                e.getClientAirTick() + "/" + e.getServerAirTick() + "/" +
                String.format("%3f", e.getDeltaY()));*/
    }
}
