package com.ch1ck3n.salmonac.checks.world;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.events.SalmonMoveEvent;
import org.bukkit.event.EventHandler;

public class Timer extends Check {
    public Timer(String name, Response response, Punishment punishment, String description) {
        super(name, response, punishment, description);
    }

    @EventHandler
    public void onMove(SalmonMoveEvent e) {

        // Type Fast
        if( e.getRespawnTick() < 40 || e.getSetBackTick() < 2 ) {} else {
            if (e.getDeltaXZ() == 0) {
                e.getSalmonPlayer().timerABuffer.reduceTick();
            }
            if (50 - (e.getPacketTime() - e.getLastPacketTime()) > 5) {
                e.getSalmonPlayer().timerABuffer.onTick();
                if (e.getSalmonPlayer().timerABuffer.getTick() > 10) {
                    this.setType("(Fast)");
                    this.setVlPerFail(1.0f);
                    flag(e.getPlayer(), "Speed = " + e.getSalmonPlayer().timerSpeed.getTick() / 20f);
                }
            } else {
                e.getSalmonPlayer().timerABuffer.reduceTick();
            }
        }

        // Type Slow
        if( e.getRespawnTick() < 40 || e.getSetBackTick() < 2 ) {} else {
            if (e.getDeltaXZ() == 0) {
                e.getSalmonPlayer().timerBBuffer.reduceTick();
            }
            if (50 - (e.getPacketTime() - e.getLastPacketTime()) < -5) {
                e.getSalmonPlayer().timerBBuffer.onTick();
                if (e.getSalmonPlayer().timerBBuffer.getTick() > 10) {
                    this.setType("(Slow)");
                    this.setVlPerFail(1.0f);
                    flag(e.getPlayer(), "Speed = " + e.getSalmonPlayer().timerSpeed.getTick() / 20f);
                }
            } else {
                e.getSalmonPlayer().timerBBuffer.reduceTick();
            }
        }

        // Type Average
        if( e.getRespawnTick() < 40 || e.getTeleportTick() < 20 ) {} else {
            if ( e.getDeltaXZ() == 0 || !e.getPlayer().isSprinting() ) {
                e.getSalmonPlayer().timerCSampleList.clear();
                e.getSalmonPlayer().timerCBuffer.reduceTick();
            }
            e.getSalmonPlayer().timerCSampleList.add((e.getPacketTime() - e.getLastPacketTime()));
            if ( e.getSalmonPlayer().timerCSampleList.isEnough() ) {
                if ( Math.abs(Math.round(e.getSalmonPlayer().timerCSampleList.getAverage()) - 50) > 2 ) {
                    e.getSalmonPlayer().timerCBuffer.onTick();
                    if ( e.getSalmonPlayer().timerCBuffer.getTick() > 7 ) {
                        this.setType("(Average)");
                        this.setVlPerFail(1.0f);
                        flag(e.getPlayer(), "Speed = " + (50f / e.getSalmonPlayer().timerCSampleList.getAverage()) +
                                "\nAverage = " + e.getSalmonPlayer().timerCSampleList.getAverage() +
                                "\nʕ•ᴥ•ʔ");
                    }
                } else {
                    e.getSalmonPlayer().timerCBuffer.reduceTick();
                }
            }
        }
    }
}
