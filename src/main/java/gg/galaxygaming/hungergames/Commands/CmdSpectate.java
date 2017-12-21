package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import gg.galaxygaming.hungergames.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSpectate extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.spectate")) {
                Players pl = HungerGames.getPlayers();
                if (pl.isAlive(p.getName())) {
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: You are already in a game."));
                    return true;
                }
                if (pl.gameGoing()) {
                    if (pl.isSpectating(p.getName())) {
                        if (args.length == 1) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (pl.isAlive(target.getName())) {
                                pl.spectate(p, target);
                                p.sendMessage(ChatColor.WHITE + HGUtils.translate("Now spectating") + ' ' + target.getName() + '.');
                            } else
                                p.sendMessage(var.errorCol() + HGUtils.translate("Error: That player is not playing."));
                        } else {
                            pl.delSpectating(p);
                            p.sendMessage(var.defaultCol() + ChatColor.WHITE + HGUtils.translate("No longer spectating the Hunger Games."));
                            return true;
                        }
                    }
                    pl.addSpectating(p);
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (pl.isAlive(target.getName())) {
                            pl.spectate(p, target);
                            p.sendMessage(ChatColor.WHITE + HGUtils.translate("Now spectating") + ' ' + target.getName() + '.');
                        } else
                            p.sendMessage(var.errorCol() + HGUtils.translate("Error: That player is not playing."));
                    } else
                        p.sendMessage(ChatColor.WHITE + HGUtils.translate("Now spectating the Hunger Games."));
                } else
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: There is no current game."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not spectate the Hunger Games."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You cannot spectate the hunger games, please log in."));
        return true;
    }
}