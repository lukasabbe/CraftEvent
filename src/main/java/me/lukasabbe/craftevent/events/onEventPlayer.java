package me.lukasabbe.craftevent.events;

import me.lukasabbe.craftevent.CraftEvent;
import me.lukasabbe.craftevent.CraftEventPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class onEventPlayer implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        CraftEventPlayer player = new CraftEventPlayer(playerJoinEvent.getPlayer());
        CraftEvent.instance.gameManger.addNewPlayer(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent){
        CraftEvent.instance.gameManger.removePlayer(playerQuitEvent.getPlayer());
    }
}
