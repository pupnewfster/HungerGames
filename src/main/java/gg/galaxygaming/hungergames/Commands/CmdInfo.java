package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import gg.galaxygaming.hungergames.Players;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdInfo extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("HungerGames.info")) {
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not view the info for the current round."));
            return true;
        }
        sender.sendMessage(var.defaultCol() + HGUtils.translate("Map") + ": " + HungerGames.getGame().getNext());
        Players pl = HungerGames.getPlayers();
        sender.sendMessage(ChatColor.GREEN + pl.amount());
        sender.sendMessage(ChatColor.WHITE + HGUtils.translate("Alive") + ": " + pl.breathing());
        sender.sendMessage(ChatColor.DARK_RED + HGUtils.translate("Dead") + ": " + pl.deceased());
        return true;
    }
}