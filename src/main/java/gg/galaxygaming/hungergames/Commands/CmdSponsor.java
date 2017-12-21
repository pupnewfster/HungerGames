package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import gg.galaxygaming.hungergames.Players;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSponsor extends Cmd {//TODO rework how sponsoring works in terms of command -> money to name instead of just free for self

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.sponsor")) {
                if (args.length != 0)
                    return false;
                Players pl = HungerGames.getPlayers();
                if (pl.gameGoing()) {
                    if (pl.alreadySponsored(p.getName())) {
                        p.sendMessage(var.errorCol() + HGUtils.translate("Error: You already sponsored yourself this round."));
                        return true;
                    }
                    sp.giveItems(p);
                    pl.addSponsored(p.getName());
                    p.sendMessage(ChatColor.GREEN + HGUtils.translate("You sponsored yourself."));
                } else
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Game not going."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not view the help for Hunger Games."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not sponsor yourself."));
        return true;
    }
}