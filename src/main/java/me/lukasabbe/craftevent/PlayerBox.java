package me.lukasabbe.craftevent;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.lukasabbe.craftevent.data.ItemData;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerBox implements GameStateListener {
    private final Location locationOfBox;
    private final World world;

    private final List<CraftEventPlayer> players = new ArrayList<>();

    private List<ItemData> randomizedItems = new ArrayList<>();

    private final List<Location> biomeLocationsPlayer1;
    private final List<Location> biomeLocationsPlayer2;

    public PlayerBox(CraftEventPlayer player1, CraftEventPlayer player2, Location locationOfBox){
        players.add(player1);
        players.add(player2);
        this.locationOfBox = locationOfBox;
        org.bukkit.World playerWorld = player1.player.getWorld();
        this.world = BukkitAdapter.adapt(playerWorld);
        biomeLocationsPlayer1 = Arrays.asList( // Add local cords to biome placement
                new Location(playerWorld,0,0,0),
                new Location(playerWorld,0,0,0),
                new Location(playerWorld,0,0,0)
        );
        biomeLocationsPlayer2 = Arrays.asList(
                new Location(playerWorld,0,0,0),
                new Location(playerWorld,0,0,0),
                new Location(playerWorld,0,0,0)
        );
    }
    public void instantiateBox(List<ItemData> randomizedItems) throws WorldEditException {
        this.randomizedItems = randomizedItems;
        List<Integer> cords = CraftEvent.instance.getConfig().getIntegerList("playerBox");
        final BlockVector3 min = BlockVector3.at(cords.get(0), cords.get(1),cords.get(2));
        final BlockVector3 max = BlockVector3.at(cords.get(3), cords.get(4),cords.get(5));
        CuboidRegion region = new CuboidRegion(world,min, max);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try(EditSession editSession = WorldEdit.getInstance().newEditSession(world)){
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );

            Operations.complete(forwardExtentCopy);

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(locationOfBox.getBlockX(), locationOfBox.getY(), locationOfBox.getBlockZ()))
                    .build();

            Operations.complete(operation);

            editSession.setBlock(BukkitAdapter.asBlockVector(locationOfBox), BlockTypes.DIAMOND_BLOCK.getDefaultState());

            Operations.complete(editSession.commit());

        }
    }

    public Location getPlayer1Pos(){
        return locationOfBox.clone().add(4,1,4);
    }
    public Location getPlayer2Pos(){
        return locationOfBox.clone().add(10,1,4);
    }

    public Location getBiomeLocationPlayer1(int pos){
        return biomeLocationsPlayer1.get(pos);
    }
    public Location getBiomeLocationPlayer2(int pos){
        return biomeLocationsPlayer2.get(pos);
    }

    public int biomeLocationSize(){
        return biomeLocationsPlayer2.size();
    }

    @Override
    public void startGame() {

    }
}
