package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CmdRemove extends Cmd {
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    private File customConfigFileChest = new File(HungerGames.getInstance().getDataFolder(), "chestlocs.yml");
    private YamlConfiguration customConfigChest = YamlConfiguration.loadConfiguration(customConfigFileChest);

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length != 2)//TODO error message
            return false;
        if (args[0].equalsIgnoreCase("arena")) {
            boolean isPlayer = sender instanceof Player;
            if (isPlayer && !sender.hasPermission("HungerGames.removeArena")) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not remove arenas from the Hunger Games."));
                return true;
            }
            customConfig.set(args[1], null);
            customConfigChest.set(args[1], null);
            try {
                customConfig.save(customConfigFile);
            } catch (IOException ignored) {
            }
            sender.sendMessage(var.defaultCol() + HGUtils.translate("Arena removed: ") + ' ' + args[1]);
            HungerGames.getGame().initMaps();
        }
        //TODO say what didnt work
        return true;
    }
}