package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdStats extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.stats")) {
                if (args.length == 0) {
                    String message = s.get(p.getName());
                    message = parseStats(message);
                    p.sendMessage(var.defaultCol() + message);
                } else {
                    String message = s.get(args[0]);
                    if (message == null) {
                        p.sendMessage(var.errorCol() + HGUtils.translate("Error: Unknown player"));
                        return false;
                    }
                    message = parseStats(message);
                    p.sendMessage(var.defaultCol() + message);
                }
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not view your stats."));
        } else {
            if (args.length != 1)
                return false;
            String message = s.get(args[0]);
            if (message == null) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Unknown player"));
                return false;
            }
            message = parseStats(message);
            sender.sendMessage(ChatColor.YELLOW + message);
        }
        return true;
    }

    private String parseStats(String stats) {//Name, Points, Wins, Kills, Deaths, Games
        String[] info = stats.split(" ");
        String statsNew;
        statsNew = info[0] + ' ' + HGUtils.translate("has") + ' ' + info[1] + ' ' + HGUtils.translate("points, and has won") + ' ' +
                info[2] + ' ' + HGUtils.translate("games, making a total of") + ' ' + info[3] + ' ' + HGUtils.translate("kills. They have also died" +
                ' ' + info[4] + ' ' + HGUtils.translate("times, and have played a total of") + ' ' + info[5] + ' ' + HGUtils.translate("games."));
        return statsNew;
    }
}