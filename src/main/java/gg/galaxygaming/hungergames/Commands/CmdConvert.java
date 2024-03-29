package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class CmdConvert extends Cmd {//TODO Remove as only will support mysql
    private File customConfigFileStats = new File(HungerGames.getInstance().getDataFolder(), "stats.yml");
    private YamlConfiguration customConfigStats = YamlConfiguration.loadConfiguration(customConfigFileStats);

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length != 1)
            return false;
        if (sender instanceof Player && !sender.hasPermission("HungerGames.convert")) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.convert")) {
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not convert the database for the Hunger Games."));
                return true;
            }
            if (args[0].equalsIgnoreCase("mysql") || args[0].equalsIgnoreCase("sql")) {//converts to mysql
                for (String name : customConfigStats.getKeys(false)) {
                    String stats = s.get(name);
                    s.addPoints(name, -Integer.parseInt(stats.split(" ")[1]));
                    s.addWin(name, -Integer.parseInt(stats.split(" ")[2]));
                    s.addKill(name, -Integer.parseInt(stats.split(" ")[3]));
                    s.addDeath(name, -Integer.parseInt(stats.split(" ")[4]));
                    s.addGame(name, -Integer.parseInt(stats.split(" ")[5]));

                    s.addPoints(name, customConfigStats.getInt(name + ".points"));
                    s.addWin(name, customConfigStats.getInt(name + ".wins"));
                    s.addKill(name, customConfigStats.getInt(name + ".kills"));
                    s.addDeath(name, customConfigStats.getInt(name + ".deaths"));
                    s.addGame(name, customConfigStats.getInt(name + ".games"));
                }
            } else if (args[0].equalsIgnoreCase("yml")) {
                s.convertToYML();
            } else
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Illegal database."));
        }
        return true;
    }
}