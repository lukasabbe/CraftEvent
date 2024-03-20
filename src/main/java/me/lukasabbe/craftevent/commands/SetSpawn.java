package me.lukasabbe.craftevent.commands;

import me.lukasabbe.craftevent.CraftEvent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length > 0){
            if(args.length < 3) return false;
            final int x = Integer.parseInt(args[0]);
            final int y = Integer.parseInt(args[1]);
            final int z = Integer.parseInt(args[2]);
            CraftEvent.instance.getConfig().set("spawn-cords",new int[]{x, y, z});
            CraftEvent.instance.saveConfig();
            commandSender.sendMessage("spawn set to cords");
            return true;
        }

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Console need to specify cords");
            return false;
        }

        Player player = (Player) commandSender;

        Location playerLoc =  player.getLocation();
        final int x = playerLoc.getBlockX();
        final int y = playerLoc.getBlockY();
        final int z = playerLoc.getBlockX();
        CraftEvent.instance.getConfig().set("spawn-cords",new int[]{x, y, z});
        CraftEvent.instance.saveConfig();
        player.sendMessage("New spawn set to current pos");

        return true;
    }
}
