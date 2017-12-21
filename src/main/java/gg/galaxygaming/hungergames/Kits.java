package gg.galaxygaming.hungergames;

import gg.galaxygaming.necessities.Economy.Material;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.ArrayList;

public class Kits {
    private static ArrayList<String> classes = new ArrayList<>();
    private static ArrayList<String> alreadyChose = new ArrayList<>();
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "kits.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

    public boolean exists(String kitName) {
        for (String path : customConfig.getKeys(false))
            if (path.equalsIgnoreCase(kitName))
                return true;
        return false;
    }

    public boolean chose(String name) {
        return alreadyChose.contains(name);
    }

    public void clearKits() {
        alreadyChose.clear();
    }

    public void giveKit(Player p, String kit) {
        kit = kit.trim();
        setLists();
        alreadyChose.add(p.getName());
        PlayerInventory inv = p.getInventory();
        String truePath;
        short data;
        for (String path : customConfig.getKeys(true))
            if (!path.equalsIgnoreCase(kit) && path.toUpperCase().startsWith(kit.toUpperCase())) {
                truePath = path.substring(kit.length() + 1, path.length());
                if (truePath.split(":").length > 1)
                    data = (short) Integer.parseInt(truePath.split(":")[1]);
                else
                    data = 0;
                inv.addItem(Material.fromData(Integer.parseInt(truePath.split(":")[0]), data).getBukkitMaterial().toItemStack(customConfig.getInt(kit + '.' + truePath)));
            }
    }

    public void listKits(Player p) {
        setLists();
        StringBuilder tempBuilder = new StringBuilder();
        for (String kit : classes)
            tempBuilder.append(kit).append(", ");
        String temp = tempBuilder.toString().trim();
        temp = temp.substring(0, temp.length() - 1);
        p.sendMessage(ChatColor.WHITE + HGUtils.translate("Available kits") + ": " + temp + '.');
    }

    private void setLists() {
        if (!classes.isEmpty())
            classes.clear();
        classes.addAll(customConfig.getKeys(false));
    }
}