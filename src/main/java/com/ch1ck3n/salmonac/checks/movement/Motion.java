package com.ch1ck3n.salmonac.checks.movement;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import com.ch1ck3n.salmonac.utils.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

public class Motion extends Check {
    public Motion(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {
        if( !e.getPlayer().getWorld().isChunkLoaded(e.getPlayer().getWorld().getChunkAt(e.getPlayer().getLocation())) )
            return;
        if( e.getPlayer().getGameMode() == GameMode.CREATIVE )
            return;

        // Type A (AirJump)
        // Disable if player just placed a block 6 ticks ago
        if( e.getRespawnTick() < 20 || /*e.isCollidingVerticallyUp() ||*/ e.getPlaceBlockTick() < 6 ) {} else {
            if( e.isJumpUpwards() && !e.isServerGround() && !e.isLastServerGround() ){
                this.setType("(A)");
                this.setVlPerFail(2.0f);
                flag( e.getPlayer() ,"JumpUpwards = " + e.isJumpUpwards() +
                        "\nServerGround = " + e.isServerGround() +
                        "\nLastServerGround = " + e.isLastServerGround() );
            }
//            if( !e.isLastCanJump() && (e.isJumping() && !e.isServerGround())&& e.isJumpUpwards() ) {
//                this.setType("(Tick)");
//                this.setVlPerFail(2.0f);
//                flag( e.getPlayer() ,"CanJump = " + e.isLastCanJump() +
//                        "\nJumping = " + e.isJumping() +
//                        "\nJumpUpwards = " + e.isJumpUpwards() );
//            }
        }

        // Type B (Stable)
        // Disable when player is onLadder, touchingClimbable, touchingLiquid, touchingSlab or touchingStair
        if( e.getRespawnTick() < 20 || e.isOnLadder() || e.isTouchingClimable() || e.isTouchingLiquid() || e.isTouchingSlab() || e.isTouchingStair() ) {} else {
            if (e.getDeltaY() != 0 && e.getLastDeltaY() != 0 && e.getDeltaY() == e.getLastDeltaY()) {
                this.setType("(B)");
                this.setVlPerFail(1.0f);
                flag( e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) +
                        "\nLastDeltaY = " + String.format("%.10f", e.getLastDeltaY()) );
            }
        }

        // Type C (FastFall)
        if( e.getRespawnTick() < 20 || e.getDamageTick() < 20 ) {} else {
            if (e.getDeltaY() < -3.920005) {
                this.setType("(C)");
                this.setVlPerFail(5.0f);
                flag( e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) );
            }
        }

        // Type D (Jump)
        if( e.getRespawnTick() < 20 ||
                e.isCollidingVerticallyUp() || e.getCollidingVerticallyUpTick() < 2 || e.getDamageTick() < 2 ||
                e.getDeltaY() <= 0 || e.isOnLadder() || e.getServerAirTick() != 1 ||
                e.getSetBackTick() < 2 || e.getSlimeTick() < 20 || e.getWebTick() == 0 ) {} else {
            if ( !e.isServerGround() ) {
                double prediction = 0.41999998688697815;
                if ( e.getPlayer().hasPotionEffect(PotionEffectType.JUMP) ) {
                    prediction += PlayerUtil.getAmplifier(e.getPlayer(), PotionEffectType.JUMP) * 0.1f;
                }
                if( e.isTouchingWater() || e.isTouchingLava() ) {
                    prediction = 0.03999999910593033D;
                    if( e.getDeltaXZ() != 0  )
                        prediction -= 0.02D;
                    else if( e.getDeltaXZ() == 0 )
                        prediction += 0.02D;
                }
                if (Math.abs(e.getDeltaY() - prediction) > 0.001) {
                    this.setType("(D)");
                    this.setVlPerFail(2.0f);
                    flag( e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) +
                            "\nPrediction = " + String.format("%.10f", prediction) );
                }
            }
        }

        // Type E (Prediction)
        if( e.getRespawnTick() < 20 || e.getDamageTick() < 2 || e.isOnLadder() ||
                e.getPlaceBlockTick() < 6 || e.getSetBackTick() < 2 || e.getSlimeTick() == 0 ||
                e.isTouchingLiquid() || e.getWebTick() == 0 || e.isFuzzyServerGround() ) {} else {
            if ( !e.isCollidingVerticallyUp() && !e.isServerGround() && e.getServerAirTick() > 6 ) {
                double prediction = (e.getLastDeltaY() - 0.08D) * 0.9800000190734863D;
                if (Math.abs(e.getDeltaY() - prediction) > 0.001) {
                    e.getSalmonPlayer().motionEBuffer.onTick();
                    if (e.getSalmonPlayer().motionEBuffer.getTick() > 2) {
                        this.setType("(E)");
                        this.setVlPerFail(1.0f);
                        flag( e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) +
                                "\nPrediction = " + String.format("%.10f", prediction) );
                    }
                }
            }
//            if ( e.isCollidingHorizontally() && !e.isServerGround() ) {
//                double prediction = (e.getLastDeltaY() - 0.08D) * 0.9800000190734863D;
//                e.getPlayer().sendMessage(prediction+"");
//                if (Math.abs(e.getDeltaY() - prediction) > 0.001) {
//                    e.getSalmonPlayer().motionIBuffer.onTick();
//                    if (e.getSalmonPlayer().motionIBuffer.getTick() > 2) {
//                    this.setType("(E)");
//                    this.setVlPerFail(1.5f);
//                    flag( e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) +
//                            "\nPrediction = " + String.format("%.10f", prediction) );
//                    }
//                }
//            }
        }

        // Type F (Invalid)
        if( e.getRespawnTick() < 20 || e.getDamageTick() < 2 || e.isOnLadder() ||
                e.getPlaceBlockTick() < 6 || e.getServerAirTick() < 6 + PlayerUtil.getAmplifier(e.getPlayer(), PotionEffectType.JUMP) ||
                e.getSetBackTick() < 2 || e.isTouchingLiquid() || e.getWebTick() == 0 || e.isFuzzyServerGround() ) {} else {
            if ( e.getFallDistance() != 0 && e.getLastDeltaY() < 0 && e.getDeltaY() > 0 && e.getSalmonPlayer().getLastVelocityY() == 0 ) {
                this.setType("(F)");
                this.setVlPerFail(3.0f);
                flag( e.getPlayer(), "DeltaY = " + String.format("%.10f", e.getDeltaY()) +
                        "\nVelocityY = " + String.format("%.10f", e.getSalmonPlayer().getLastVelocityY()) );
            }
        }
//
        // Type G (StableAccel)
        if( e.getRespawnTick() < 20 || e.isOnLadder() || e.getTeleportTick() < 20 ||
                e.isTouchingClimable() || e.isTouchingLiquid() || e.isTouchingSlab() || e.isTouchingStair() ) {} else {
            if ( e.getDeltaYAccel() != 0 && e.getLastDeltaYAccel() != 0 && Math.abs(e.getDeltaYAccel()) == Math.abs(e.getLastDeltaYAccel()) ) {
                this.setType("(G)");
                this.setVlPerFail(1.0f);
                flag( e.getPlayer(), "DeltaYAccel = " + String.format("%.10f", Math.abs(e.getDeltaYAccel())) +
                        "\nLastDeltaYAccel = " + String.format("%.10f", Math.abs(e.getLastDeltaYAccel())) );
            }
        }
//
        // Type H (Spider - Collide)
        if( e.getRespawnTick() < 20 || e.getDamageTick() < 2 || e.isOnLadder() ||
                (e.getServerAirTick() < 6 + PlayerUtil.getAmplifier(e.getPlayer(), PotionEffectType.JUMP)) || e.isTouchingLiquid() ) {} else {
            if (e.isCollidingHorizontally() && !e.isLastServerGround() && e.getDeltaY() > 0) {
                this.setType("(H)");
                this.setVlPerFail(2.0f);
                flag(e.getPlayer(), "CollidingHorizontally = " + e.isCollidingHorizontally() +
                        "\nServerGround = " + e.isServerGround() +
                        "\nDeltaY = " + String.format("%.10f", e.getDeltaY()));
            }
        }
    }
}
