package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import gg.galaxygaming.hungergames.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdForceStop extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Players pl = HungerGames.getPlayers();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.forcestop")) {
                if (pl.allowStart()) {
                    pl.endGame();
                    p.sendMessage(ChatColor.DARK_RED + HGUtils.translate("You stopped the game."));
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + p.getName() + ' ' + HGUtils.translate("stopped the game."));
                } else
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Game is already stopped."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not stop the Hunger Games."));
        } else {
            if (pl.gameGoing()) {
                pl.endGame();
                sender.sendMessage(ChatColor.DARK_RED + HGUtils.translate("You stopped the game."));
                Bukkit.broadcastMessage(ChatColor.DARK_RED + HGUtils.translate("The console stopped the game."));
            } else
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Game is already started."));
        }
        return true;
    }
}