package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import gg.galaxygaming.hungergames.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdLeave extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.leave")) {
                Players pl = HungerGames.getPlayers();
                int spot = pl.posInQueue(p.getName());
                if (spot == 0) {//In game not in line
                    p.setHealth(0);
                    p.sendMessage(ChatColor.WHITE + HGUtils.translate("You left the Hunger Games."));
                    Bukkit.broadcastMessage(ChatColor.GRAY + p.getName() + ' ' + HGUtils.translate("left the current hunger games."));
                    return true;
                }
                pl.removeFromQueue(p.getName());
                p.sendMessage(ChatColor.DARK_RED + HGUtils.translate("Removed from the line for joining the next game."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not leave the Hunger Games."));//wait wtf
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You cannot leave the hunger games, please log in."));
        return true;
    }
}