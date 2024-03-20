package me.lukasabbe.craftevent.commands;

import me.lukasabbe.craftevent.CraftEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        CraftEvent.instance.gameManger.startGame();
        return true;
    }
}
