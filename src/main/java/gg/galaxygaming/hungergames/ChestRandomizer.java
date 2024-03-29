package gg.galaxygaming.hungergames;

import gg.galaxygaming.necessities.Economy.Material;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestRandomizer {
    private static File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private static YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    private static File customConfigFileChest = new File(HungerGames.getInstance().getDataFolder(), "chests.yml");
    private static YamlConfiguration customConfigChest = YamlConfiguration.loadConfiguration(customConfigFileChest);
    private static File customConfFileLocs = new File(HungerGames.getInstance().getDataFolder(), "chestlocs.yml");
    private static YamlConfiguration customConfLocs = YamlConfiguration.loadConfiguration(customConfFileLocs);
    private static ArrayList<Integer> blockIds = new ArrayList<>();
    private static ArrayList<Double> percentChance = new ArrayList<>();
    private static ArrayList<Short> damageValue = new ArrayList<>();
    private static ArrayList<Integer> chestSpots = new ArrayList<>();

    public int chestIdLocation() {
        Random r = new Random();
        int temp = r.nextInt(1000);//Gives a number to find the random.
        double totalPercent = 0;
        for (int x = 0; x < blockIds.size(); x++)//Runs through 5 times, 5 items.
        {
            totalPercent = totalPercent + percentChance.get(x);//Calculates for total percentage of all items up to the current item.
            if (temp < 10 * totalPercent && temp > 10 * totalPercent - 10 * percentChance.get(x))
                return x;
        }
        return -1;
    }

    public void randomizeChests() {
        setLists();
        String world = HungerGames.getGame().getNext();
        if (world == null)
            return; //No chest locations set yet
        World w = Bukkit.getWorld(world);
        int chestNum = 0;
        int x, y, z, chestAmount, loc, mat;
        short data;
        for (String path : customConfLocs.getKeys(true)) {
            if (path.toUpperCase().startsWith(world.toUpperCase())) {
                x = customConfLocs.getInt(world + ".c" + Integer.toString(chestNum) + ".x");
                y = customConfLocs.getInt(world + ".c" + Integer.toString(chestNum) + ".y");
                z = customConfLocs.getInt(world + ".c" + Integer.toString(chestNum) + ".z");
                Block b = w.getBlockAt(x, y, z);
                if (b.getState() instanceof Chest) {
                    resetSpots();
                    Chest c = (Chest) b.getState();
                    Inventory inv = c.getBlockInventory();
                    if (inv != null)
                        for (int i = 0; i < 27; i++)
                            inv.setItem(i, Material.AIR.getBukkitMaterial().toItemStack(1));
                    chestAmount = items();
                    if (inv != null)
                        for (int i = 0; i < chestAmount; i++) {
                            loc = chestIdLocation();
                            mat = 0;
                            data = 0;
                            if (loc != -1) {
                                mat = blockIds.get(loc);
                                data = damageValue.get(loc);
                            }
                            inv.setItem(chestLoc(), Material.fromData(mat, data).getBukkitMaterial().toItemStack(1));
                        }
                }
                chestNum++;
            }
        }
    }

    public void chests(String world) {
        int x1 = customConfig.getInt(world + ".corner1.x");
        int y1 = customConfig.getInt(world + ".corner1.y");
        int z1 = customConfig.getInt(world + ".corner1.z");
        int x2 = customConfig.getInt(world + ".corner2.x");
        int y2 = customConfig.getInt(world + ".corner2.y");
        int z2 = customConfig.getInt(world + ".corner2.z");
        int temp;
        if (x1 < x2) {
            temp = x2;
            x2 = x1;
            x1 = temp;
        }
        if (y1 < y2) {
            temp = y2;
            y2 = y1;
            y1 = temp;
        }
        if (z1 < z2) {
            temp = z2;
            z2 = z1;
            z1 = temp;
        }
        World w = Bukkit.getWorld(world);
        customConfLocs.set(world, null);
        int chestNum = 0;
        for (int x = x2; x <= x1; x++)
            for (int y = y2; y <= y1; y++)
                for (int z = z2; z <= z1; z++) {
                    Block b = w.getBlockAt(x, y, z);
                    if (b.getState() instanceof Chest) {
                        customConfLocs.set(world + ".c" + Integer.toString(chestNum) + ".x", x);
                        customConfLocs.set(world + ".c" + Integer.toString(chestNum) + ".y", y);
                        customConfLocs.set(world + ".c" + Integer.toString(chestNum) + ".z", z);
                        chestNum++;
                    }
                }
        try {
            customConfLocs.save(customConfFileLocs);
        } catch (IOException ignored) {
        }
    }

    public void emptyChests() {
        setLists();
        String world = HungerGames.getGame().getNext();
        if (world == null)
            return; //No chest locations set yet
        int x1 = customConfig.getInt(world + ".corner1.x");
        int y1 = customConfig.getInt(world + ".corner1.y");
        int z1 = customConfig.getInt(world + ".corner1.z");
        int x2 = customConfig.getInt(world + ".corner2.x");
        int y2 = customConfig.getInt(world + ".corner2.y");
        int z2 = customConfig.getInt(world + ".corner2.z");
        int temp;
        if (x1 < x2) {
            temp = x2;
            x2 = x1;
            x1 = temp;
        }
        if (y1 < y2) {
            temp = y2;
            y2 = y1;
            y1 = temp;
        }
        if (z1 < z2) {
            temp = z2;
            z2 = z1;
            z1 = temp;
        }
        World w = Bukkit.getWorld(world);
        if (w == null) {
            Bukkit.createWorld(new WorldCreator(world));
            w = Bukkit.getWorld(world);
        }
        int chestNum = 0;
        int x, y, z;
        for (String path : customConfLocs.getKeys(true)) {
            if (path.toUpperCase().startsWith(world.toUpperCase())) {
                x = customConfLocs.getInt(world + ".c" + Integer.toString(chestNum) + ".x");
                y = customConfLocs.getInt(world + ".c" + Integer.toString(chestNum) + ".y");
                z = customConfLocs.getInt(world + ".c" + Integer.toString(chestNum) + ".z");
                Block b = w.getBlockAt(x, y, z);
                if (b.getState() instanceof Chest) {
                    Chest c = (Chest) b.getState();
                    Inventory inv = c.getBlockInventory();
                    if (inv != null)
                        for (int i = 0; i < 27; i++)
                            inv.setItem(i, new ItemStack(org.bukkit.Material.AIR, 1));
                }
                chestNum++;
            }
        }
        List<Entity> entList = w.getEntities();
        for (Entity current : entList)
            if (current instanceof Item && current.getLocation().getBlockX() >= x2 && current.getLocation().getBlockX() <= x1
                    && current.getLocation().getBlockZ() >= z2 && current.getLocation().getBlockZ() <= z1)
                current.remove();
    }

    private void resetSpots() {
        chestSpots.clear();
        for (int i = 0; i < 27; i++)
            chestSpots.add(i);
    }

    private int chestLoc() {
        Random r = new Random();
        int temp = r.nextInt(chestSpots.size());
        int ret = chestSpots.get(temp);
        chestSpots.remove(temp);
        return ret;
    }

    private int items() {
        Random r = new Random();//TODO not have multiple randoms created for no reason
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        int min = config.getInt("minPerChest");
        int max = config.getInt("maxPerChest");
        if (min >= max)
            return max;
        int temp = r.nextInt(max) + 1;
        while (min > temp)
            temp = r.nextInt(max) + 1;
        return temp;
    }

    private void balance() {
        double total = 0;
        for (double d : percentChance)
            total += d;
        if (total == 0)
            return;
        for (int i = 0; i < percentChance.size(); i++)
            percentChance.set(i, percentChance.get(i) * 100 / total);
        int i = 0;
        for (String path : customConfigChest.getKeys(false)) {
            if (i >= percentChance.size())
                break;
            customConfigChest.set(path, percentChance.get(i));
            i++;
        }
        try {
            customConfigChest.save(customConfigFileChest);
        } catch (IOException ignored) {
        }
    }

    private void setLists() {
        blockIds.clear();
        percentChance.clear();
        short data;
        for (String path : customConfigChest.getKeys(false)) {
            if (path.split(":").length > 1) {
                blockIds.add(Integer.parseInt(path.split(":")[0]));
                data = (short) Integer.parseInt(path.split(":")[1]);
            } else {
                blockIds.add(Integer.parseInt(path));
                data = 0;
            }
            damageValue.add(data);
            percentChance.add(customConfigChest.getDouble(path));
        }
        balance();
    }
}