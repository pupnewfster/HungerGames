package gg.galaxygaming.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Game {
    Kits kit = new Kits();
    private static String nextMap = "";
    private static boolean voting;
    private static int map1;
    private static int map2;
    private static int map3;
    private ArrayList<String> maps = new ArrayList<>();
    private ArrayList<Integer> mvote = new ArrayList<>();
    private HashMap<String, Integer> votes = new HashMap<>();
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

    public String getNext() {
        if (nextMap.equals("")) {
            if (maps.isEmpty())
                nextMap = null;
            else if (mvote.isEmpty())
                nextMap = maps.get(0);
            else
                nextMap = maps.get(mvote.get(0));
        }
        return nextMap;
    }

    public boolean canVote() {
        return !(maps.isEmpty() || maps.size() == 1);
    }

    private void m() {
        Random r = new Random();
        mvote.clear();
        ArrayList<Integer> mid = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++)
            mid.add(i);
        int temp;
        int max = 3;
        if (maps.size() < 3)
            max = maps.size();
        for (int i = 0; i < max; i++) {
            temp = r.nextInt(mid.size());
            mvote.add(mid.get(temp));
            mid.remove(temp);
        }
    }

    public void initMaps() {
        maps.clear();
        Set<String> temp = customConfig.getKeys(false);
        for (String r : temp)
            if (!r.equalsIgnoreCase("worldS"))
                maps.add(r);
    }

    public String maps() {
        StringBuilder mBuilder = new StringBuilder();
        for (String map : maps)
            mBuilder.append(map).append(", ");
        String m = mBuilder.toString().trim();
        m = m.substring(0, maps.size() - 1).trim();
        return m + '.';
    }

    public void holdVote() {
        if (maps.isEmpty())
            initMaps();
        if (maps.size() == 1)
            return;
        if (!voting)
            m();
        voting = true;
        Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Maps you can vote for are:"));
        for (int i = 0; i < maps.size(); i++) {
            if (i == 3)
                break;
            Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Vote") + ' ' + Integer.toString(i + 1) + ' ' +
                    HGUtils.translate("for map") + ' ' + maps.get(mvote.get(i)) + ' ' + HGUtils.translate("current votes") + ": " + votes(i));
        }
    }

    private String votes(int map) {
        map = map + 1;
        switch (map) {
            case 1:
                return Integer.toString(map1);
            case 2:
                return Integer.toString(map2);
            case 3:
                return Integer.toString(map3);
            default:
                return "";
        }
    }

    public String addVote(String name, int map) {
        int temp = 0;
        if (votes.containsKey(name))
            temp = votes.get(name);
        votes.put(name, map);
        switch (map) {
            case 1:
                map1++;
                break;
            case 2:
                map2++;
                break;
            case 3:
                map3++;
                break;
        }
        switch (temp) {
            case 1:
                map1--;
                break;
            case 2:
                map2--;
                break;
            case 3:
                map3--;
                break;
        }
        if (map1 > map2 && map1 > map3)
            nextMap = maps.get(mvote.get(0));
        else if (map2 > map1 && map2 > map3)
            nextMap = maps.get(mvote.get(1));
        else if (map3 > map1 && map3 > map2)
            nextMap = maps.get(mvote.get(2));
        else
            nextMap = maps.get(mvote.get(0));
        return maps.get(mvote.get(map - 1));
    }

    public void delVote(String name) {
        int temp = 0;
        if (votes.containsKey(name))
            temp = votes.get(name);
        switch (temp) {
            case 1:
                map1--;
                break;
            case 2:
                map2--;
                break;
            case 3:
                map3--;
                break;
        }
        votes.remove(name);
    }

    public void end() {
        Bukkit.unloadWorld(nextMap, false);
        Bukkit.createWorld(new WorldCreator(nextMap));
        nextMap = "";
        kit.clearKits();
        map1 = 0;
        map2 = 0;
        map3 = 0;
        start();
    }

    public void start() {
        initMaps();
        if (maps.isEmpty())
            return;
        HungerGames.getPlayers().gameStart();
        votes.clear();
        voting = false;
        disableSave();
    }

    public boolean voteHappening() {
        return voting;
    }

    public void disableSave() {
        World w;
        for (String map : maps) {
            w = Bukkit.getWorld(map);
            if (w == null) {
                Bukkit.createWorld(new WorldCreator(map));
                w = Bukkit.getWorld(map);
            }
            w.setAutoSave(false);
        }
    }
}