package com.ch1ck3n.salmonac;

import com.ch1ck3n.salmonac.checks.Check;
import com.ch1ck3n.salmonac.checks.Debugger;
import com.ch1ck3n.salmonac.checks.combat.*;
import com.ch1ck3n.salmonac.checks.movement.*;
import com.ch1ck3n.salmonac.checks.player.GroundSpoof;
import com.ch1ck3n.salmonac.checks.world.Timer;
import com.ch1ck3n.salmonac.commands.SalmonACCommand;
import com.ch1ck3n.salmonac.events.*;
import com.ch1ck3n.salmonac.managers.CheckManager;
import com.ch1ck3n.salmonac.managers.PlayerManager;
import com.ch1ck3n.salmonac.utils.PlayerUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class SalmonAC extends JavaPlugin implements Listener {

    public Executor packetThread;
    private String prefix = "";
    public String getPrefix(){
        return prefix;
    }
    private boolean broadcast = true;
    public boolean shouldBroadcast() {
        return broadcast;
    }
    public void setBroadcast(boolean b) {
        broadcast = b;
    }
    private boolean debugMode = false;
    public boolean isDebugMode() {
        return debugMode;
    }
    public void setDebugMode(boolean b) {
        debugMode = b;
    }
    private static SalmonAC instance;
    public static SalmonAC getInstance(){
        return instance;
    }
    private CheckManager checkManager;
    public CheckManager getCheckManager(){
        return checkManager;
    }
    private PlayerManager playerManager;
    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    @Override
    public void onEnable() {
        /* ----- SalmonAC Setup ----- */
        instance = this;
        prefix = "§8[§cSalmonAC§8]§r";

        getServer().getPluginManager().registerEvents(this,this);
        getServer().getPluginManager().registerEvents(new BlockEventListener(),this);
        getServer().getPluginManager().registerEvents(new EntityEventListener(),this);
        getServer().getPluginManager().registerEvents(new InventoryEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(),this);

        packetThread = Executors.newSingleThreadExecutor();
        checkManager = new CheckManager();
        playerManager = new PlayerManager();

        PlayerUtil.weaponDamageListSetup();

        /* ----- Check Setup ----- */
        checkManager.addCheck( new Debugger("Debugger", Check.Response.NONE, Check.Punishment.NONE,"This is a debugger") );

        //Combat
        checkManager.addCheck(new Aim("Aim", Check.Response.NONE, Check.Punishment.KICK, "Invalid rotations"));
        checkManager.addCheck( new Blocking("Blocking", Check.Response.NONE, Check.Punishment.KICK, "Attack while blocking") );
        checkManager.addCheck( new Critical("Critical", Check.Response.NONE, Check.Punishment.KICK, "Modify packet to do critical") );
        checkManager.addCheck( new Reach("Reach", Check.Response.NONE, Check.Punishment.KICK, "Modify reach distance to hit further") );
        checkManager.addCheck( new Swing("Swing", Check.Response.NONE, Check.Punishment.KICK, "Cancel packet to not to swing") );
        checkManager.addCheck( new Velocity("Velocity", Check.Response.NONE, Check.Punishment.KICK, "Cancel packet to get no knockback") );

        // Movement
        checkManager.addCheck( new InvMove("InvMove", Check.Response.NONE, Check.Punishment.KICK, "Click gui while interacting world") );
        checkManager.addCheck( new Jesus("Jesus", Check.Response.NONE, Check.Punishment.KICK, "Walk on liquid") );
        checkManager.addCheck( new Motion("Motion", Check.Response.NONE, Check.Punishment.KICK, "Modify motion Y to jump higher or lower") );
        checkManager.addCheck( new Speed("Speed", Check.Response.NONE, Check.Punishment.KICK, "Motion motion X & Z to move faster") );
        checkManager.addCheck( new Sprinting("Sprinting", Check.Response.NONE,Check.Punishment.KICK, "Impossible sprinting") );
        checkManager.addCheck( new Step("Step", Check.Response.NONE, Check.Punishment.KICK, "Modify step height to step higher") );

        // Player
        checkManager.addCheck( new GroundSpoof("GroundSpoof", Check.Response.NONE, Check.Punishment.KICK, "Modify packet to spoof ground") );

        // World
        checkManager.addCheck( new Timer("Timer",Check.Response.NONE,Check.Punishment.KICK,"Modify timer speed to move faster") );

        checkManager.setupChecks();

        /* ----- Player Register ----- */
        for( Player p : Bukkit.getOnlinePlayers() ){
            playerManager.registerPlayer( p );
        }


        /* ----- Command Register ----- */
        getCommand("salmon").setExecutor( new SalmonACCommand() );

        /* ----- ProtocolLib Setup ----- */
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                packetThread.execute(() ->{
                    if (event.getPacketType() == PacketType.Play.Client.POSITION || event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
                        Player player = event.getPlayer();
                        double x = event.getPacket().getDoubles().read(0);
                        double y = event.getPacket().getDoubles().read(1);
                        double z = event.getPacket().getDoubles().read(2);
                        float yaw = event.getPacket().getFloat().read(0);
                        float pitch = event.getPacket().getFloat().read(1);
                        SalmonMoveEvent customEvent = new SalmonMoveEvent(player,new Location(player.getWorld(),x,y,z,yaw,pitch));
                        getServer().getPluginManager().callEvent( customEvent );
                    }
                });
            }
            @Override
            public void onPacketSending(PacketEvent event) {

            }
        });
    }
}
