package com.ch1ck3n.salmonac.checks.player;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import com.ch1ck3n.salmonac.utils.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

public class GroundSpoof extends Check {
    public GroundSpoof(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {
        if( e.getPlayer().getGameMode() == GameMode.CREATIVE ) return;

        // Type A
        // ClientGround but not ServerGround, DeltaY != 0
        if( e.getRespawnTick() < 40 || e.isBoatAround() ||
                (e.isFuzzyServerGround() && !e.isFuzzyCollidingHorizontally()) ||
                e.getPlaceBlockTick() < 6 || e.getServerAirTick() < 6 + PlayerUtil.getAmplifier(e.getPlayer(), PotionEffectType.JUMP) ||
                e.getSetBackTick() < 2 || e.getSlimeTick() < 20 ) {} else {
            if ( e.getDeltaY() != 0.0D && e.getLastDeltaY() != 0.0D &&
                    !e.isLastServerGround() && !e.isServerGround() && e.isClientGround() && e.isLastClientGround() ) {
                this.setType("(A)");
                this.setVlPerFail(1.0f);
                flag(e.getPlayer(), "ClientGround = " + e.isClientGround() +
                        "\nLastClientGround = " + e.isLastClientGround() +
                        "\nServerGround = " + e.isServerGround() +
                        "\nLastServerGround = " + e.isLastServerGround() +
                        "\nDeltaY = " + String.format("%.10f", e.getDeltaY()) +
                        "\nLastDeltaY = " + String.format("%.10f", e.getLastDeltaY()) +
                        (this.getResponse() == Response.FIX ? "\n\nFix FallDistance to " + String.format("%.10f", e.getFallDistance()) : "") );
                if ( this.getResponse() == Response.FIX ) {
                    e.getPlayer().setFallDistance((float) e.getFallDistance());
                }
            }
        }

        // Type B
        // ClientGround but not ServerGround, DeltaY = 0
        if( e.getRespawnTick() < 40 || e.isBoatAround() || e.getLilyAround() > 0 ||
                e.getSetBackTick() < 2 || e.getSlimeTick() < 20  ) {} else {
            if ( e.getDeltaY() == 0.0D && e.getLastDeltaY() == 0.0D &&
                    !e.isLastServerGround() && !e.isServerGround() && e.isClientGround() && e.isLastClientGround() ) {
                this.setType("(B)");
                this.setVlPerFail(1.0f);
                flag(e.getPlayer(), "ClientGround = " + e.isClientGround() +
                        "\nLastClientGround = " + e.isLastClientGround() +
                        "\nServerGround = " + e.isServerGround() +
                        "\nLastServerGround = " + e.isLastServerGround() +
                        "\nDeltaY = " + String.format("%.10f", e.getDeltaY()) +
                        "\nLastDeltaY = " + String.format("%.10f", e.getLastDeltaY()) );
            }
        }

        // Type C
        // ServerGround but not ClientGround, DeltaY = 0
        if( e.getRespawnTick() < 40 || !e.isFuzzyServerGround() ||
                e.isPistonAround() || e.getSlimeTick() < 20 ||
                e.getTeleportTick() < 20 || e.isTouchingStair() ) {} else {
            if ( e.getDeltaY() == 0 && e.getLastDeltaY() == 0 && e.isServerGround() && !e.isClientGround() ) {
                this.setType("(C)");
                this.setVlPerFail(1.0f);
                flag(e.getPlayer(), "ClientGround = " + e.isClientGround() +
                        "\nServerGround = " + e.isServerGround() +
                        "\nDeltaY = " + String.format("%.10f", e.getDeltaY()) +
                        "\nLastDeltaY = " + String.format("%.10f", e.getLastDeltaY()) +
                        (this.getResponse() == Response.FIX ? "\n\nFix FallDistance to " + String.format("%.10f", e.getFallDistance()) : ""));
                if ( this.getResponse() == Response.FIX ) {
                    if (e.getPlayer().getFallDistance() - 3 > 0) e.getPlayer().damage(e.getPlayer().getFallDistance() - 3);
                    e.getPlayer().setFallDistance((float) e.getFallDistance());
                }
            }
        }

        // Type D
        // Spoof Ground in air with C03
        if( e.getRespawnTick() < 40 || e.getDeltaY() >= 0 || e.getPlaceBlockTick() < 6 ||
                (e.isFuzzyServerGround() && !e.isCollidingHorizontally()) || e.isTouchingLiquid() ) {} else {
            if ( Math.abs(e.getFallDistance() - e.getPlayer().getFallDistance()) > 1 &&
                    e.getFallDistance() / 2 > e.getPlayer().getFallDistance() ) {
                if ( e.getFallDistance() - e.getLastFallDamage() > 0 ) {
                    this.setType("(D)");
                    this.setVlPerFail(4.0f);
                    flag(e.getPlayer(), "ServerFallDistance = " + String.format("%.10f", e.getFallDistance()) +
                            "\nClientFallDistance = " + String.format("%.10f", e.getPlayer().getFallDistance()) +
                            (this.getResponse() == Response.FIX ? "\n\nFix FallDistance to " + String.format("%.10f", e.getFallDistance() - e.getLastFallDamage()) : ""));
                    if ( this.getResponse() == Response.FIX ) {
                        e.getPlayer().setFallDistance((float) (e.getFallDistance()));
                        e.setLastFallDamage(0);
                    }
                } else {
                    if ( e.getFallDistance() != 0 ) {
                        this.setType("(D)");
                        this.setVlPerFail(4.0f);
                        flag(e.getPlayer(), "ServerFallDistance = " + String.format("%.10f", e.getFallDistance()) +
                                "\nClientFallDistance = " + String.format("%.10f", e.getPlayer().getFallDistance()));
                    }
                }
            }
        }

        // Type E
        // My Flux NoFall ==
        if( e.getRespawnTick() < 40 || e.getSlimeTick() < 20 ||
                (e.isFuzzyServerGround() && !e.isFuzzyCollidingHorizontally()) || e.isTouchingLiquid() ) {} else {
            if ( e.getFallDistance() > 1 && e.getPlayer().getFallDistance() == 0 && !e.isServerGround() && !e.isJumping() ) {
                this.setType("(E)");
                this.setVlPerFail(5.0f);
                flag( e.getPlayer(), "ServerFallDistance = " + String.format("%.10f", e.getFallDistance()) +
                        "\nClientFallDistance = " + String.format("%.10f", e.getPlayer().getFallDistance()) +
                        "\nJumping = " + e.isJumping() +
                        "\nServerGround = " + e.isServerGround() );
                if ( this.getResponse() == Response.FIX ) {
                    e.getPlayer().setFallDistance((float) (e.getFallDistance()));
                }
            }
        }

//        // Type F
//        // Baguar NoFall codes
//        if( e.getRespawnTick() < 40 || e.getPlaceBlockTick() < 6 || e.getSlimeTick() == 0 ||
//                e.isSuperFuzzyServerGround() || e.isTouchingLiquid() ) {} else {
//            if ( e.isClientGround() != e.isServerGround() || e.isClientGround() != (e.getDeltaY() > 0) ) {
//                this.setType("(F)");
//                this.setVlPerFail(5.0f);
//                e.getCustomPlayer().groundSpoofFBuffer.onTick();
//                if( e.getCustomPlayer().groundSpoofFBuffer.getTick() > 5 ) {
//                    flag(e.getPlayer(), "ClientGround = " + e.isClientGround() +
//                            "\nServerGround = " + e.isServerGround() +
//                            "\nDeltaY > 0 = " + (e.getDeltaY() > 0) +
//                            "\n\n<Baguar> LEMAFO");
//                }
//            }else {
//                e.getCustomPlayer().groundSpoofFBuffer.reduceTick();
//            }
//        }

//        // Type G
//        if( e.getRespawnTick() < 40 || e.getSalmonPlayer().getVelocityY() != 0 ||
//                e.getSlimeTick() < 20 || (e.isSuperFuzzyServerGround() && e.isCollidingHorizontally()) || e.isTouchingLiquid() ) {} else {
//            if ( e.getFallDistance() != 0 && e.getPlayer().getFallDistance() == 0 && !e.isServerGround() ) {
//                this.setType("(G)");
//                this.setVlPerFail(5.0f);
//                flag( e.getPlayer(), "ServerFallDistance = " + String.format("%.10f", e.getFallDistance()) +
//                        "\nClientFallDistance = " + String.format("%.10f", e.getPlayer().getFallDistance()) +
//                        "\nServerGround = " + e.isServerGround() );
//            }
//        }
    }
}
