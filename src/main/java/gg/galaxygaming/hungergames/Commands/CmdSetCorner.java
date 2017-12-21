package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CmdSetCorner extends Cmd {//TODO replace this with necessities left and right position info
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1)
                return false;
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.setcorner")) {
                int number;
                try {
                    number = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    return false;
                }
                if (number > 2 || number < 1) {
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Only 2 corners are needed."));
                    return false;
                }
                String pathX = p.getWorld().getName() + ".corner" + Integer.toString(number) + ".x";
                String pathY = p.getWorld().getName() + ".corner" + Integer.toString(number) + ".y";
                String pathZ = p.getWorld().getName() + ".corner" + Integer.toString(number) + ".z";
                customConfig.set(pathX, p.getLocation().getBlockX());
                customConfig.set(pathY, p.getLocation().getBlockY());
                customConfig.set(pathZ, p.getLocation().getBlockZ());
                try {
                    customConfig.save(customConfigFile);
                } catch (IOException ignored) {
                }
                p.sendMessage(var.defaultCol() + HGUtils.translate("Corner") + ' ' + Integer.toString(number) + ' ' + HGUtils.translate("set at") + ": " +
                        Integer.toString(p.getLocation().getBlockX()) + ", " + Integer.toString(p.getLocation().getBlockY()) + ", "
                        + Integer.toString(p.getLocation().getBlockZ()));
                HungerGames.getGame().initMaps();
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not set the corners for Hunger Games."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You cannot set corners for the hunger games because you are not an entity, please log in."));
        return true;
    }
}