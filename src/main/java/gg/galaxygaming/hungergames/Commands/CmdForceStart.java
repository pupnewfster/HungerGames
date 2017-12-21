package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdForceStart extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.forcestart")) {
                if (!HungerGames.getPlayers().allowStart()) {
                    HungerGames.getGame().start();
                    p.sendMessage(ChatColor.AQUA + HGUtils.translate("You started the game."));
                    Bukkit.broadcastMessage(ChatColor.AQUA + p.getName() + ' ' + HGUtils.translate("started the game."));
                } else
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Game is already started."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not start the Hunger Games."));
        } else {
            if (HungerGames.getPlayers().gameGoing()) {
                HungerGames.getGame().start();
                sender.sendMessage(ChatColor.AQUA + HGUtils.translate("You started the game."));
                Bukkit.broadcastMessage(ChatColor.AQUA + HGUtils.translate("The console started the game."));
            } else
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Game is already started."));
        }
        return true;
    }
}