package me.lukasabbe.craftevent;

import com.sk89q.worldedit.WorldEditException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameManger {

    public boolean isGameOn = false;

    private final List<CraftEventPlayer> onlinePlayers = new ArrayList<>();
    private final List<PlayerBox> playerBoxes = new ArrayList<>();
    public void startGame(){
        isGameOn = true;
        List<CraftEventPlayer> players = onlinePlayers
                .stream()
                .filter(player -> !player.isSpectating())
                .collect(Collectors.toList());
        if(!condtions(2)) return; // Change to player base

        List<Integer> cords = CraftEvent.instance.getConfig().getIntegerList("playerBox");
        final int x = cords.get(0);
        final int y = cords.get(1);
        final int z = cords.get(2);
        final int distanceBetweenBoxes = 15;
        int counter = 0;
        for(CraftEventPlayer player : players){
            final CraftEventPlayer teamPlayer = player.getTeamPlayer();
            final Location locationOfBox = new Location(player.player.getWorld(), x, y - 20, z - distanceBetweenBoxes * counter);
            PlayerBox playerBox;
            if(teamPlayer != null){
                playerBox = new PlayerBox(player, teamPlayer, locationOfBox);
            }else{
                playerBox = new PlayerBox(player, player, locationOfBox); // Add custom matchmaking
            }
            playerBoxes.add(playerBox);

            counter++;
        }

        playerBoxes.forEach(box -> {
            try {
                box.instantiateBox();
                players.get(0).player.teleport(box.getPlayer1Pos());
                players.get(1).player.teleport(box.getPlayer2Pos());
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        });



    }

    public boolean condtions(int count){
        return count >= 2;
    }

    public void addNewPlayer(CraftEventPlayer player){
        onlinePlayers.add(player);
    }

    public void removePlayer(Player player){
        onlinePlayers.removeIf(eventPlayer -> eventPlayer.player.equals(player));
    }

}
