package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.HGUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class CmdLeaderboards extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length == 0 || args.length > 2)
            return false;
        if (sender instanceof Player && !sender.hasPermission("HungerGames.leaderboards")) {
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: You may not view the leaderboards for the Hunger Games."));
            return true;
        }
        String type;
        if (args[0].equalsIgnoreCase("points"))
            type = "points";
        else if (args[0].equalsIgnoreCase("wins"))
            type = "wins";
        else if (args[0].equalsIgnoreCase("kills"))
            type = "kills";
        else if (args[0].equalsIgnoreCase("deaths"))
            type = "deaths";
        else if (args[0].equalsIgnoreCase("games"))
            type = "games";
        else {
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Unknown stat type."));
            return false;
        }
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Page entered not an integer."));
                return false;
            }
        }
        if (page == 0)
            page = 1;
        ArrayList<String> leader = s.leaderboards();
        ArrayList<String> t = getType(leader, type);
        int size = pages(t.size());
        if (page > size) {
            sender.sendMessage(var.errorCol() + HGUtils.translate("Error: Max pages are") + ' ' + Integer.toString(size) + '.');
            return true;
        }
        sender.sendMessage(ChatColor.WHITE + HGUtils.translate("Page") + ' ' + Integer.toString(page) +
                ' ' + HGUtils.translate("of") + ' ' + Integer.toString(size) + ' ' + HGUtils.translate("for stat") + ' ' + type + '.');
        page = page - 1;
        for (int i = 0; i < 8; i++) {
            if (t.size() == i + 8 * page)
                break;
            sender.sendMessage(var.defaultCol() + Integer.toString(i + 1 + 8 * page) + ' ' + t.get(i + 8 * page));
        }
        return true;
    }

    private ArrayList<String> getType(ArrayList<String> leaders, String type) {
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> unSorted = new ArrayList<>();
        String name;
        for (String leader : leaders) {
            name = leader.split(" ")[0];
            if (type.equalsIgnoreCase("points"))
                unSorted.add(name + ' ' + leader.split(" ")[1]);
            else if (type.equalsIgnoreCase("wins"))
                unSorted.add(name + ' ' + leader.split(" ")[2]);
            else if (type.equalsIgnoreCase("kills"))
                unSorted.add(name + ' ' + leader.split(" ")[3]);
            else if (type.equalsIgnoreCase("deaths"))
                unSorted.add(name + ' ' + leader.split(" ")[4]);
            else if (type.equalsIgnoreCase("games"))
                unSorted.add(name + ' ' + leader.split(" ")[5]);
        }
        Collections.sort(unSorted);
        String obj;
        for (int i = 0; i < unSorted.size(); i++) {
            obj = getObj(unSorted, i);
            if (obj != null)
                temp.add(obj);
        }
        return temp;
    }

    private String getObj(ArrayList<String> unSorted, int time) {
        ArrayList<Integer> leadsort = new ArrayList<>();
        for (String anUnSorted : unSorted)
            leadsort.add(Integer.parseInt(anUnSorted.split(" ")[1]));
        Collections.sort(leadsort);
        Collections.reverse(leadsort);
        if (unSorted.size() < time + 1)
            return null;
        int occurrence = 1;
        for (int i = 0; i < time; i++)
            if (leadsort.get(i).equals(leadsort.get(time)))
                occurrence++;
        int leadSpot = leadCords(Integer.toString(leadsort.get(time)), occurrence, unSorted);
        if (leadSpot == -1)
            return null;
        return unSorted.get(leadSpot);
    }

    private int leadCords(String amount, int occurrence, ArrayList<String> sorting) {
        int counter = 1;
        for (int i = 0; i < sorting.size(); i++) {
            if (sorting.get(i).split(" ")[1].equalsIgnoreCase(amount)) {
                if (counter == occurrence)
                    return i;
                counter++;
            }
        }
        return -1;
    }

    private int pages(int size) {
        int rounder = 0;
        if (size % 8 != 0)
            rounder = 1;
        return size / 8 + rounder;
    }
}