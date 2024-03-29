package me.lukasabbe.craftevent;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sk89q.worldedit.WorldEditException;
import me.lukasabbe.craftevent.data.BiomeData;
import me.lukasabbe.craftevent.data.ItemData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GameManger {

    public boolean isGameOn = false;

    private final List<CraftEventPlayer> onlinePlayers = new ArrayList<>();
    private final List<PlayerBox> playerBoxes = new ArrayList<>();
    public void startGame(){
        if(isGameOn) return;
        isGameOn = true;
        List<CraftEventPlayer> players = onlinePlayers
                .stream()
                .filter(player -> !player.isSpectating())
                .collect(Collectors.toList());
        if(!conditions(2)) return; // Change to player base

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


        InputStream jsonInput = CraftEvent.instance.getResource("item_dependencies.json");
        assert jsonInput != null;
        Reader reader = new InputStreamReader(jsonInput, StandardCharsets.UTF_8);
        Type typeToken = new TypeToken<ArrayList<ItemData>>(){}.getType();
        List<ItemData> itemData = new Gson().fromJson(reader, typeToken);
        List<ItemData> randomizedItems = new ArrayList<>();
        Random rnd = new Random();

        int amountOfItems = CraftEvent.instance.getConfig().getInt("craft-amount-items");
        for(int i = 0; i < amountOfItems; i++){
            randomizedItems.add(itemData.get(rnd.nextInt(itemData.size()-1)));
        }
        randomizedItems = getNeededBiomes(randomizedItems);
        List<ItemData> finalRandomizedItems = randomizedItems;
        playerBoxes.forEach(box -> {
            try {
                box.instantiateBox(finalRandomizedItems);
                players.get(0).player.teleport(box.getPlayer1Pos());
                players.get(1).player.teleport(box.getPlayer2Pos());
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        });

        //check if box has succeeded with task in PlayerBox

        // Add listener here that get triggers when a box wins.

        /*
            other: Turn of block destroying outside of boxes
                   Turn of add auto play
                   add friend command/ pair command

         */

    }

    private List<ItemData> getNeededBiomes(List<ItemData> items){
        List<ItemData> withBiomes = new ArrayList<>();
        InputStream jsonInput = CraftEvent.instance.getResource("biomes.json");
        assert jsonInput != null;
        Reader reader = new InputStreamReader(jsonInput, StandardCharsets.UTF_8);
        Type typeToken = new TypeToken<ArrayList<BiomeData>>(){}.getType();
        List<BiomeData> biomes = new Gson().fromJson(reader, typeToken);

        Random rnd = new Random();

        for(ItemData item : items){
            List<BiomeData> selectedBiomes = new ArrayList<>();
            item.biomes.forEach(biome -> {
                Optional<BiomeData> biomeDataO = biomes
                        .stream()
                        .filter(t -> t.name.equals(biome))
                        .findFirst();
                assert biomeDataO.isPresent();
                BiomeData biomeData = biomeDataO.get();
                selectedBiomes.add(biomeData);
            });
            int biomesLeft = selectedBiomes.size() - 6;

            if(biomesLeft >= 0){
                for(int i = 0; i < biomesLeft; i++){
                    selectedBiomes.add(biomes.get(rnd.nextInt(biomes.size()-1)));
                }
            }

            item.biomeDatas = selectedBiomes;
            withBiomes.add(item);
        }
        return withBiomes;
    }

    private boolean conditions(int count){
        return count >= 2;
    }

    public void addNewPlayer(CraftEventPlayer player){
        onlinePlayers.add(player);
    }

    public void removePlayer(Player player){
        onlinePlayers.removeIf(eventPlayer -> eventPlayer.player.equals(player));
    }

}
