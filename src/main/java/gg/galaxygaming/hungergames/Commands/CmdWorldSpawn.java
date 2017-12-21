package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CmdWorldSpawn extends Cmd {//TODO does necessities already handle this?
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 0)
                return false;
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.setworldspawn")) {
                String pathWorld = "worldS.world";
                String pathX = "worldS.x";
                String pathY = "worldS.y";
                String pathZ = "worldS.z";
                customConfig.set(pathWorld, p.getWorld().getName());
                customConfig.set(pathX, p.getLocation().getBlockX());
                customConfig.set(pathY, p.getLocation().getBlockY());
                customConfig.set(pathZ, p.getLocation().getBlockZ());
                try {
                    customConfig.save(customConfigFile);
                } catch (IOException ignored) {
                }
                p.sendMessage(var.defaultCol() + HGUtils.translate("World spawn set at") + ": " +
                        Integer.toString(p.getLocation().getBlockX()) + ", " + Integer.toString(p.getLocation().getBlockY()) + ", "
                        + Integer.toString(p.getLocation().getBlockZ()));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not set the world spawn for Hunger Games."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You cannot set spawns for the hunger games because you are not an entity, please log in."));
        return true;
    }
}