package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import gg.galaxygaming.hungergames.HungerGames;
import gg.galaxygaming.hungergames.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdKit extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.kit")) {
                if (HungerGames.getInstance().getConfig().getBoolean("useKits")) {
                    Players pl = HungerGames.getPlayers();
                    if (pl.gameGoing()) {
                        if (pl.isAlive(p.getName())) {
                            if (args.length == 0)
                                kit.listKits(p);
                            else {
                                if (!kit.chose(p.getName())) {
                                    if (kit.exists(args[0])) {
                                        kit.giveKit(p, args[0]);
                                        p.sendMessage(var.defaultCol() + HGUtils.translate("You chose the kit") + ' ' + args[0] + '.');
                                    } else
                                        p.sendMessage(var.errorCol() + HGUtils.translate("Error: That kit does not exist."));
                                } else
                                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Already chose a kit."));
                            }
                        } else
                            p.sendMessage(var.errorCol() + HGUtils.translate("Error: You are not in the game."));
                    } else
                        p.sendMessage(var.errorCol() + HGUtils.translate("Error: Game not going."));
                } else
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: This server has kits disabled."));
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: you may not use kits for the Hunger Games."));
        } else
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not use any kits."));
        return true;
    }
}