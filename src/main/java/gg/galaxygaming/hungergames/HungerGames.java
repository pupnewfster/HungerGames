package gg.galaxygaming.hungergames;

import gg.galaxygaming.hungergames.Commands.Cmd;
import gg.galaxygaming.hungergames.Commands.CmdHungerGames;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HungerGames extends JavaPlugin {
    private static HungerGames INSTANCE;
    private final File configFile = new File(HungerGames.getInstance().getDataFolder(), "config.yml");
    private ChestRandomizer cr = new ChestRandomizer();
    private Players players = new Players();
    private Game game = new Game();

    @Override
    public void onEnable() {
        INSTANCE = this;
        Initialization init = new Initialization();
        init.initiateFiles();
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        this.game.start();
        getLogger().info(HGUtils.translate("Hunger Games brought to you by") + " Galaxy Gaming " + HGUtils.translate("has been enabled."));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Cmd com = new Cmd();
        if (cmd.getName().equalsIgnoreCase("hungergames"))
            com = new CmdHungerGames();
        return com.commandUse(sender, args);
    }

    @Override
    public void onDisable() {
        this.players.endTimer();
        this.players.sendToWSpawn();
        cr.emptyChests();
        getLogger().info(ChatColor.DARK_RED + HGUtils.translate("Hunger Games disabled."));
    }

    public static HungerGames getInstance() {
        return INSTANCE;
    }

    public static Game getGame() {
        return INSTANCE.game == null ? INSTANCE.game = new Game() : INSTANCE.game;
    }

    public static Players getPlayers() {
        return INSTANCE.players == null ? INSTANCE.players = new Players() : INSTANCE.players;
    }

    public static ChestRandomizer getChestRandomizer() {
        return INSTANCE.cr == null ? INSTANCE.cr = new ChestRandomizer() : INSTANCE.cr;
    }

    /**
     * Gets the config file.
     * @return The config file.
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Gets the config..
     * @return The config.
     */
    public YamlConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getConfigFile());
    }
}