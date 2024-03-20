package me.lukasabbe.craftevent.tab_complet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompletSetSpawn implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> tab = new ArrayList<>();
        if(args.length == 0){
            return tab;
        }
        if(args.length == 1){
            tab.add("x");
        }else if(args.length == 2){
            tab.add("y");
        }else{
            tab.add("z");
        }
        return tab;
    }
}
