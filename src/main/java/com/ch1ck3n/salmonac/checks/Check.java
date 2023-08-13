package com.ch1ck3n.salmonac.checks;

import com.ch1ck3n.salmonac.SalmonAC;
import com.ch1ck3n.salmonac.utils.SalmonPlayer;
import com.ch1ck3n.salmonac.managers.PlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Check implements Listener {

    String description;
    public String getDesc(){
        return description;
    }
    float maxVL;
    public void setMaxVL(float f){
        maxVL = f;
    }
    String name;
    public String getCheckName(){
        return name;
    }
    Punishment punishment;
    Response response;
    public Response getResponse() {
        return response;
    }
    String type = "";
    public String getType(){
        return type;
    }
    public void setType(String s){
        type = s;
    }
    float vlPerFail = 1.0f;
    public void setVlPerFail(float f){
        vlPerFail = f;
    }

    public enum Response {
        NONE, CANCEL, FIX, SETBACK_BUKKIT, SETBACK_SPIGOT;
    }

    public enum Punishment {
        NONE, KICK, BAN;
    }

    public Check(String name, Response response, Punishment punishment, String description){
        this.description = description;
        this.maxVL = 10.0f;
        this.name = name;
        this.punishment = punishment;
        this.response = response;
        this.vlPerFail = 1.0f;
    }

//    public void consoleFlag(Player p){
//        PlayerManager playerManager = SalmonAC.getInstance().getPlayerManager();
//        SalmonPlayer violator = playerManager.getPlayer( p );
//        float vl = violator.onFailed( String.valueOf(name) );
//        String message = SalmonAC.getInstance().getPrefix() + "§e " + p.getName() + "§7 failed §e" + name + " §8[§7x" + vl + "§8]";
//        Bukkit.getConsoleSender().sendMessage(message);
//    }

    public void flag(Player p){
        send(p,"NONE");
    }

    public void flag(Player p, String info) {
        send(p,info);
        if ( response == Response.SETBACK_BUKKIT ) {
            (SalmonAC.getInstance().getPlayerManager().getPlayer(p)).setBack = true;
        }else if ( response == Response.SETBACK_SPIGOT ) {
            p.teleport((SalmonAC.getInstance().getPlayerManager().getPlayer(p)).getLastLocation());
        }
    }

    public void flagNoSetBack(Player p, String info) {
        send(p,info);
    }

    private void send(Player p,String info){
        PlayerManager playerManager = SalmonAC.getInstance().getPlayerManager();
        SalmonPlayer violator = playerManager.getPlayer( p );
        float vl = violator.onFailed( String.valueOf(name), vlPerFail );
        String message = SalmonAC.getInstance().getPrefix() + " §e" + p.getName() + "§7 failed §e" + name + type + " §8[§7x§f" + String.format("%.1f", vl) + "§8]";
        TextComponent textComponent = new TextComponent( message );
        textComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT ,
                new ComponentBuilder(
                        "§7Description: \n§r§f" + description +
                                "\n\n§7Information: \n§r§f" + info +
                                "\n\n§7Type: §f" + type +
                                "\n§7CheckVL: §f" + String.format("%.1f", vl) + "/" + maxVL + " §c(+" + vlPerFail + ")" +
                                "\n§7Click To Teleport" ).create() ) );
        textComponent.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + p.getName() ) );
        for( Player player : Bukkit.getOnlinePlayers() ){
            if( playerManager.getPlayer( player ).getVerboseStatus() ){
                player.spigot().sendMessage( textComponent );
            }
        }
        if( vl >= maxVL && vl > 0 ){
            violator.resetCheckVL(name);
            Bukkit.getScheduler().runTask( SalmonAC.getInstance(), new Runnable() {
                @Override
                public void run() {
                    SalmonAC instance = SalmonAC.getInstance();
                    String reason = instance.getPrefix() + " §4Unfair Advantage";
                    if ( punishment == Punishment.KICK ) {
                        String message = instance.getPrefix() + " " + p.getName() + "§7 is §c kicked §7for §4Unfair Advantage";
                        if( !instance.isDebugMode() ) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + p.getName() + " " + reason);
                            broadcast(instance, message);
                        }else{
                            p.sendMessage(instance.getPrefix() + "§7 You are §ckicked §7for §e" + name );
                        }
                    }else if ( punishment == Punishment.BAN ) {
                        String message = instance.getPrefix() + " " + p.getName() + "§7 is §cbanned §7for §4Unfair Advantage";
                        if( !instance.isDebugMode() ) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + p.getName() + " " + reason);
                            broadcast(instance, message);
                        }else{
                            p.sendMessage(instance.getPrefix() + "§7 You are §cbanned §7for §e" + name );
                        }
                    }
                }
            });
        }
    }

    public void broadcast(SalmonAC instance, String s) {
        if( instance.shouldBroadcast() ) {
            Bukkit.getConsoleSender().sendMessage(s);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(new TextComponent(s));
            }
        }
    }
}
