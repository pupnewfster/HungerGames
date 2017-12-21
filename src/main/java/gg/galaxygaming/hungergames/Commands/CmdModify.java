package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdModify extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length != 3)
            return false;
        if (sender instanceof Player && !sender.hasPermission("HungerGames.modify")) {
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not modify a players stats for the Hunger Games."));
            return true;
        }
        String targetName = args[0];
        if (s.get(targetName) != null) {
            int temp;
            try {
                temp = Integer.parseInt(args[2]);
            } catch (Exception e) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Must enter a number to modify by."));
                return false;
            }
            if (args[1].equalsIgnoreCase("deaths")) {
                s.addDeath(targetName, temp);
                sender.sendMessage(var.defaultCol() + HGUtils.translate("Added") + ' ' + Integer.toString(temp) +
                        ' ' + HGUtils.translate("deaths to") + ' ' + targetName + '.');
            } else if (args[1].equalsIgnoreCase("games")) {
                s.addGame(targetName, temp);
                sender.sendMessage(var.defaultCol() + HGUtils.translate("Added") + ' ' + Integer.toString(temp) +
                        ' ' + HGUtils.translate("games to") + ' ' + targetName + '.');
            } else if (args[1].equalsIgnoreCase("kills")) {
                s.addKill(targetName, temp);
                sender.sendMessage(var.defaultCol() + HGUtils.translate("Added") + ' ' + Integer.toString(temp) +
                        ' ' + HGUtils.translate("kills to") + ' ' + targetName + '.');
            } else if (args[1].equalsIgnoreCase("points")) {
                s.addPoints(targetName, temp);
                sender.sendMessage(var.defaultCol() + HGUtils.translate("Added") + ' ' + Integer.toString(temp) +
                        ' ' + HGUtils.translate("points to") + ' ' + targetName + '.');
            } else if (args[1].equalsIgnoreCase("wins")) {
                s.addWin(targetName, temp);
                sender.sendMessage(var.defaultCol() + HGUtils.translate("Added") + ' ' + Integer.toString(temp) +
                        ' ' + HGUtils.translate("wins to") + ' ' + targetName + '.');
            } else
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Unknown stat section."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Player does not exist."));
        return true;
    }
}