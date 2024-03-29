package me.lukasabbe.craftevent;

import me.lukasabbe.craftevent.commands.SetSpawn;
import me.lukasabbe.craftevent.commands.StartGameCommand;
import me.lukasabbe.craftevent.events.onEventPlayer;
import me.lukasabbe.craftevent.tab_complet.TabCompletSetSpawn;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class CraftEvent extends JavaPlugin {

    public static CraftEvent instance;
    public GameManger gameManger;

    @Override
    public void onEnable() {
        instance = this;
        gameManger = new GameManger();
        createConfig();
        registerAllCommands();
        registerAllEvents();
    }

    private void createConfig(){
        FileConfiguration config = getConfig();
        config.addDefault("spawn-cords", new int[]{0, 100, 0});
        config.addDefault("biome-n", new int[]{0 , 0 , 0});
        config.addDefault("biome-e", new int[]{-9 , 0 , 4});
        config.addDefault("biome-s", new int[]{-21 , 0 , 2});
        config.addDefault("biome-w", new int[]{-26 , 0 , -3});
        config.addDefault("playerBox", new int[]{-7, 64, -15, 7, 60, -23});
        config.addDefault("craft-amount-items", 5);
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void registerAllCommands(){
        final PluginCommand setSpawnCommand = this.getCommand("setspawn");
        if(setSpawnCommand == null) return;
        setSpawnCommand.setTabCompleter(new TabCompletSetSpawn());
        setSpawnCommand.setExecutor(new SetSpawn());

        final PluginCommand startCommand = this.getCommand("start");
        if(startCommand == null) return;
        startCommand.setExecutor(new StartGameCommand());
    }

    private void registerAllEvents(){
        getServer().getPluginManager().registerEvents(new onEventPlayer(),this);
    }
}


/*
Becons
cake
 */
