package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHelp extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        int maxPages = 5;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("HungerGames.help")) {
                int page = 1;
                if (args.length > 0) {
                    try {
                        page = Integer.parseInt(args[0]);
                    } catch (Exception ignored) {
                    }
                }
                if (page == 0)
                    page = 1;
                if (page > maxPages) {
                    p.sendMessage(var.errorCol() + HGUtils.translate("Error: Please enter a page in between ") + " 1 " +
                            HGUtils.translate("and") + ' ' + Integer.toString(maxPages));
                    return false;
                }
                p.sendMessage(ChatColor.GREEN + HGUtils.translate("Hunger Games Help Page") + ' ' + Integer.toString(page) + ' ' +
                        HGUtils.translate("of") + ' ' + Integer.toString(maxPages));
                sendPage(page, sender);
            } else
                p.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not view the help for Hunger Games."));
        } else {
            int page = 1;
            if (args.length > 0) {
                try {
                    page = Integer.parseInt(args[0]);
                } catch (Exception ignored) {
                }
            }
            if (page == 0)
                page = 1;
            if (page > maxPages) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Please enter a page in between") + " 1 " +
                        HGUtils.translate("and") + ' ' + Integer.toString(maxPages));
                return false;
            }
            sender.sendMessage(ChatColor.GREEN + HGUtils.translate("Hunger Games Help Page") + ' ' +
                    Integer.toString(page) + HGUtils.translate("of") + ' ' + Integer.toString(maxPages));
            sendPage(page, sender);
        }
        return true;
    }

    private void sendPage(int page, CommandSender sender) {
        switch (page) {
            case 1:
                page1(sender);
                break;
            case 2:
                page2(sender);
                break;
            case 3:
                page3(sender);
                break;
            case 4:
                page4(sender);
                break;
            case 5:
                page5(sender);
                break;
        }
    }

    private void page1(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/hg help [page] ~ " + HGUtils.translate("Shows the help page [page] for hunger games."));
        sender.sendMessage(ChatColor.AQUA + "/hg join ~ " + HGUtils.translate("Gets in line for next game."));
        sender.sendMessage(ChatColor.AQUA + "/hg spectate [player] ~ " + HGUtils.translate("Spectates the current game or [player]."));
        sender.sendMessage(ChatColor.AQUA + "/hg leave ~ " + HGUtils.translate("Leaves the current game or if in line, the line."));
        sender.sendMessage(ChatColor.AQUA + "/hg setspawn [number] ~ " + HGUtils.translate("Sets the [number] spawnpoint" +
                "(max set in config.yml bye maxPlayers) 0 is the spectator spawnpoint."));
    }

    private void page2(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/hg info ~ " + HGUtils.translate("Views info about the current round."));
        sender.sendMessage(ChatColor.AQUA + "/hg vote [map number] ~ " + HGUtils.translate("Votes for map [map number]."));
        sender.sendMessage(ChatColor.AQUA + "/hg stats [player] ~ " + HGUtils.translate("Shows the stats of [player]."));
        sender.sendMessage(ChatColor.AQUA + "/hg forcestart ~ " + HGUtils.translate("Forces the game to start."));
        sender.sendMessage(ChatColor.AQUA + "/hg sponsor ~ " + HGUtils.translate("Sponsors yourself."));
    }

    private void page3(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/hg kit [kitname] ~ " + HGUtils.translate("Chooses a kit to use (disabled by default)."));
        sender.sendMessage(ChatColor.AQUA + "/hg setkitprice [kitname] [price] ~ " + HGUtils.translate("Sets the price for a kit."));
        sender.sendMessage(ChatColor.AQUA + "/hg buykit [kitname] ~ " + HGUtils.translate("Will buy the kit if you have enough points."));
        sender.sendMessage(ChatColor.AQUA + "/hg kitprices ~ " + HGUtils.translate("View the prices for the buyable kits."));
        sender.sendMessage(ChatColor.AQUA + "/hg convert [mysql:yml] ~ " + HGUtils.translate("Converts stats from one database to specified one."));
    }

    private void page4(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/hg setworldspawn ~ " + HGUtils.translate("Sets the spawn players will go to when they die or game ends."));
        sender.sendMessage(ChatColor.AQUA + "/hg modify [player] [stats class] [amount] ~ " + HGUtils.translate("Modifies [player]'s [stats class] by [amount]."));
        sender.sendMessage(ChatColor.AQUA + "/hg leaderboard [stats class] [page] ~ " + HGUtils.translate("Leader board of each stat class."));
        sender.sendMessage(ChatColor.AQUA + HGUtils.translate("Stat Classes:") + " deaths, games, kills, points, wins.");
        sender.sendMessage(ChatColor.AQUA + "/hg setcorner [number] ~ " + HGUtils.translate("Sets the corners for the hunger games either 1 or 2."));
    }

    private void page5(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/hg forcestop ~ " + HGUtils.translate("Forces the current game to stop."));
        sender.sendMessage(ChatColor.AQUA + "/hg setchests [world] ~ " + HGUtils.translate("Locates the chest locations for given world."));
        sender.sendMessage(ChatColor.AQUA + HGUtils.translate("Coming soon."));
        sender.sendMessage(ChatColor.AQUA + HGUtils.translate("Coming soon."));
        sender.sendMessage(ChatColor.AQUA + HGUtils.translate("Coming soon."));
    }
}
