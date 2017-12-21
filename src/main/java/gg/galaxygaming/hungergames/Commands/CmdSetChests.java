package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetChests extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length != 1) {
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: Must give world name."));
                return false;
            }
            if (p.hasPermission("HungerGames.setchests")) {
                String world = args[0];
                if (Bukkit.getWorld(world) == null) {
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Must give a valid world name."));
                    return false;
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + HGUtils.translate("Chests for") + ' ' + world + ' ' +
                        HGUtils.translate("are being located expect a little lag."));
                HungerGames.getChestRandomizer().chests(world);
                Bukkit.broadcastMessage(ChatColor.GREEN + HGUtils.translate("Chests for") + ' ' + world + ' ' + HGUtils.translate("have been located."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not set the chest locations for Hunger Games."));
        } else {
            if (args.length != 1) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Must give world name."));
                return false;
            }
            String world = args[0];
            if (Bukkit.getWorld(world) == null) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Must give a valid world name."));
                return false;
            }
            Bukkit.broadcastMessage(ChatColor.GREEN + HGUtils.translate("Chests for") + ' ' + world + ' ' +
                    HGUtils.translate("are being located expect a little lag."));
            HungerGames.getChestRandomizer().chests(world);
            Bukkit.broadcastMessage(ChatColor.GREEN + HGUtils.translate("Chests for") + ' ' + world + ' ' + HGUtils.translate("have been located."));
        }
        return true;
    }
}