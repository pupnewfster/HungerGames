package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CmdSetSpawn extends Cmd {
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1)
                return false;
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.setspawn")) {
                int number;
                try {
                    number = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    return false;
                }
                int maxSpawns = HungerGames.getInstance().getConfig().getInt("maxPlayers");
                if (number > maxSpawns || number < 0) {
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Max spawns are") +
                            ' ' + Integer.toString(maxSpawns) + ' ' + HGUtils.translate("with the 0 being the spectator spawn"));
                    return false;
                }
                String pathX = p.getWorld().getName() + ".s" + Integer.toString(number) + ".x";
                String pathY = p.getWorld().getName() + ".s" + Integer.toString(number) + ".y";
                String pathZ = p.getWorld().getName() + ".s" + Integer.toString(number) + ".z";
                customConfig.set(pathX, p.getLocation().getBlockX());
                customConfig.set(pathY, p.getLocation().getBlockY());
                customConfig.set(pathZ, p.getLocation().getBlockZ());
                try {
                    customConfig.save(customConfigFile);
                } catch (IOException ignored) {
                }
                p.sendMessage(var.defaultCol() + HGUtils.translate("Spawn") + ' ' + Integer.toString(number) + ' ' + HGUtils.translate("set at") + ": " +
                        Integer.toString(p.getLocation().getBlockX()) + ", " + Integer.toString(p.getLocation().getBlockY()) + ", "
                        + Integer.toString(p.getLocation().getBlockZ()));
                HungerGames.getGame().initMaps();
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not set the spawnpoints for Hunger Games."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You cannot set spawns for the hunger games because you are not an entity, please log in."));
        return true;
    }
}