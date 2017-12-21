package gg.galaxygaming.hungergames;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Players {//TODO replace spectating with spectator gamemode
    //TODO refactor or rename class
    Sponsor spons = new Sponsor();
    Stats s = new Stats();
    private File customConfigFile = new File(HungerGames.getInstance().getDataFolder(), "spawns.yml");
    private YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    private ArrayList<String> spectating = new ArrayList<>();
    private ArrayList<String> sponsored = new ArrayList<>();
    private ArrayList<String> origalive = new ArrayList<>();
    private ArrayList<String> queued = new ArrayList<>();
    private ArrayList<String> alive = new ArrayList<>();
    private ArrayList<String> dead = new ArrayList<>();
    private boolean alreadySponsor;
    private boolean deathStarted;
    private boolean gameStarted;
    private boolean invincible = true;
    private boolean moveDeny = true;
    private boolean death;
    private String motd = "Voting";
    private String worldName = "";//TODO: Figure out why is this never set?
    private int xCornMin;
    private int xCornMax;
    private int zCornMin;
    private int zCornMax;
    private int tptime;
    private int stime;
    private int dtime;
    private int time;
    private int xmin;
    private int xmax;
    private int zmin;
    private static int zmax;
    //TODO remove timers and use bukkit runnables/scheduled tasks
    private Timer invinc = new Timer();
    private Timer noMove = new Timer();
    private Timer count = new Timer();
    private Timer t = new Timer();

    private BukkitRunnable day = new BukkitRunnable() {
        @Override
        public void run() {
            HungerGames.getPlayers().refillChests();
        }
    };

    public boolean alreadySponsored(String name) {
        return sponsored.contains(name);
    }

    public String getMotd() {
        return motd;
    }

    public void sendToWSpawn() {
        Player p;
        for (String anAlive : alive) {
            p = Bukkit.getPlayer(anAlive);
            p.setFoodLevel(20);
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFlying(false);
            p.setCanPickupItems(true);
            PlayerInventory inv = p.getInventory();
            for (int j = 0; j < 36; j++)
                inv.setItem(j, new ItemStack(Material.AIR));
            inv.setBoots(new ItemStack(Material.AIR));
            inv.setLeggings(new ItemStack(Material.AIR));
            inv.setChestplate(new ItemStack(Material.AIR));
            inv.setHelmet(new ItemStack(Material.AIR));
            p.setExp(-p.getExp());
            p.getActivePotionEffects().clear();
            p.teleport(spawnLoc(), TeleportCause.PLUGIN);
        }
        for (String aSpectating : spectating) {
            p = Bukkit.getPlayer(aSpectating);
            p.setFoodLevel(20);
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFlying(false);
            p.setCanPickupItems(true);
            PlayerInventory inv = p.getInventory();
            for (int j = 0; j < 36; j++)
                inv.setItem(j, new ItemStack(Material.AIR));
            inv.setBoots(new ItemStack(Material.AIR));
            inv.setLeggings(new ItemStack(Material.AIR));
            inv.setChestplate(new ItemStack(Material.AIR));
            inv.setHelmet(new ItemStack(Material.AIR));
            p.setExp(-p.getExp());
            p.getActivePotionEffects().clear();
            p.teleport(spawnLoc(), TeleportCause.PLUGIN);
        }
    }

    public void endTimer() {
        t.cancel();
        t.purge();
        t = new Timer();
        count.cancel();
        count.purge();
        count = new Timer();
        this.day.cancel();
        invinc.cancel();
        invinc.purge();
        invinc = new Timer();
        noMove.cancel();
        noMove.purge();
        noMove = new Timer();
    }

    public void addSponsored(String name) {
        sponsored.add(name);
    }

    public String leftAlive() {
        StringBuilder tempBuilder = new StringBuilder();
        for (String anAlive : alive)
            tempBuilder.append(anAlive).append(", ");
        String temp = tempBuilder.toString().trim();
        temp = temp.substring(0, temp.length() - 2);
        return temp + '.';
    }

    public void startSponsor() {
        Bukkit.broadcastMessage(ChatColor.GREEN + HGUtils.translate("The players left now recieve sponsorships."));
        for (String anAlive : alive)
            spons.giveItems(Bukkit.getPlayer(anAlive));
    }

    public void escaping(Player p) {
        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();
        if (x >= xmax || x <= xmin || z >= zmax || z <= zmin) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + p.getName() + ' ' + HGUtils.translate("is trying to escape the death match."));
            Player temp;
            for (int i = 0; i < alive.size(); i++) {
                temp = Bukkit.getPlayer(alive.get(i));
                temp.teleport(loc(i + 1), TeleportCause.PLUGIN);
            }
        }
    }

    public void startDeath() {
        death = true;
        String world = HungerGames.getGame().getNext();
        String pathx;
        String pathz;
        int tempxmin = 30000000;
        int tempxmax = -30000000;
        int tempzmin = 30000000;
        int tempzmax = -30000000;
        int x;
        int z;
        int maxPlayers = HungerGames.getInstance().getConfig().getInt("maxPlayers");
        for (int i = 1; i < maxPlayers; i++) {
            pathx = world + ".s" + Integer.toString(i) + ".x";
            pathz = world + ".s" + Integer.toString(i) + ".z";
            if (customConfig.get(pathz) != null && customConfig.get(pathx) != null) {
                x = customConfig.getInt(pathx);
                z = customConfig.getInt(pathz);
                if (z > tempzmax)
                    tempzmax = z;
                else if (z < tempzmin)
                    tempzmin = z;
                if (x > tempxmax)
                    tempxmax = x;
                else if (x < tempxmin)
                    tempxmin = x;
            }
        }
        tempzmax = tempzmax + 5;
        tempzmin = tempzmin - 5;
        tempxmax = tempxmax + 5;
        tempxmin = tempxmin - 5;
        zmax = tempzmax;
        zmin = tempzmin;
        xmax = tempxmax;
        xmin = tempxmin;
        Bukkit.broadcastMessage(ChatColor.DARK_RED + HGUtils.translate("Death match started."));
        motd = HGUtils.translate("Deathmatch");
        Player temp;
        for (int i = 0; i < alive.size(); i++) {
            temp = Bukkit.getPlayer(alive.get(i));
            temp.teleport(loc(i + 1), TeleportCause.PLUGIN);
        }
    }

    public boolean deathMatch() {
        return alive.size() <= HungerGames.getInstance().getConfig().getInt("playersDeathMatch");
    }

    public boolean sponsorStart() {
        if ((origalive.size() / 5 >= alive.size() || alive.size() == HungerGames.getInstance().getConfig().getInt("playersDeathMatch") + 1) && !alreadySponsor) {
            alreadySponsor = true;
            return true;
        }
        return false;
    }

    public boolean gameGoing() {
        return alive.size() > 1;
    }

    public boolean allowStart() {
        return gameStarted;
    }

    public boolean safeTime() {
        return invincible;
    }

    public boolean isAlive(String name) {
        return alive.contains(name);
    }

    public boolean isSpectating(String name) {
        return spectating.contains(name);
    }

    public void spectate(Player p, Player target) {
        p.teleport(target, TeleportCause.PLUGIN);
    }

    public void addSpectating(Player p) {
        String world = HungerGames.getGame().getNext();
        String pathX = world + ".s0.x";
        String pathY = world + ".s0.y";
        String pathZ = world + ".s0.z";
        p.setGameMode(GameMode.SPECTATOR);
        spectating.add(p.getName());
        p.teleport(new Location(Bukkit.getWorld(world), customConfig.getInt(pathX), customConfig.getInt(pathY), customConfig.getInt(pathZ)), TeleportCause.PLUGIN);
        p.getActivePotionEffects().clear();
    }

    public boolean deathstarted() {
        return death;
    }

    public void escapingArena(Player p) {
        String world = HungerGames.getGame().getNext();
        if (!worldName.equalsIgnoreCase(world)) {
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
            xCornMax = x1;
            xCornMin = x2;
            zCornMax = z1;
            zCornMin = z2;
        }
        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();
        if (x >= xCornMax) {
            p.sendMessage(ChatColor.RED + HGUtils.translate("You may not leave the arena."));
            Location l = new Location(p.getWorld(), x - 1, p.getLocation().getBlockY(), z);
            p.teleport(l, TeleportCause.PLUGIN);
        } else if (x <= xCornMin) {
            p.sendMessage(ChatColor.RED + HGUtils.translate("You may not leave the arena."));
            Location l = new Location(p.getWorld(), x + 1, p.getLocation().getBlockY(), z);
            p.teleport(l, TeleportCause.PLUGIN);
        } else if (z >= zCornMax) {
            p.sendMessage(ChatColor.RED + HGUtils.translate("You may not leave the arena."));
            Location l = new Location(p.getWorld(), x, p.getLocation().getBlockY(), z - 1);
            p.teleport(l, TeleportCause.PLUGIN);
        } else if (z <= zCornMin) {
            p.sendMessage(ChatColor.RED + HGUtils.translate("You may not leave the arena."));
            Location l = new Location(p.getWorld(), x, p.getLocation().getBlockY(), z + 1);
            p.teleport(l, TeleportCause.PLUGIN);
        }
    }

    public void delSpectating(Player p) {
        p.setGameMode(GameMode.SURVIVAL);
        PlayerInventory inv = p.getInventory();//TODO is setting this stuff needed
        for (int j = 0; j < 36; j++)
            inv.setItem(j, new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setHelmet(new ItemStack(Material.AIR));
        p.setExp(-p.getExp());
        p.getActivePotionEffects().clear();//TODO: can they have potion effects?
        p.teleport(spawnLoc(), TeleportCause.PLUGIN);
    }

    public void addDead(String name) {
        alive.remove(name);
        dead.add(name);
        Player p = Bukkit.getPlayer(name);
        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);
        p.setCanPickupItems(true);
        PlayerInventory inv = p.getInventory();
        for (int j = 0; j < 36; j++)
            inv.setItem(j, new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setHelmet(new ItemStack(Material.AIR));
        p.setExp(-p.getExp());
        p.getActivePotionEffects().clear();
        p.teleport(spawnLoc(), TeleportCause.PLUGIN);
    }

    private String playerList(List<String> players) {
        if (players.isEmpty())
            return "none";
        StringBuilder sb = new StringBuilder();
        for (String pl : players)
            sb.append(pl).append(", ");
        String temp = sb.toString().trim();
        return temp.substring(0, temp.length() - 1) + '.';
    }

    public String deceased() {//dead
        return playerList(dead);
    }

    public String breathing() {//alive
        return playerList(alive);
    }

    public String amount() {
        try {
            return Integer.toString(alive.size()) + ' ' + HGUtils.translate("alive out of") + ' ' +
                    Integer.toString(alive.size() + dead.size()) + ' ' + HGUtils.translate("total.");
        } catch (Exception e) {
            return HGUtils.translate("Game not started");
        }
    }

    public boolean onePlayerLeft() {
        return alive.size() == 1 || alive.size() == 0;
    }

    public String winner() {
        s.addWin(alive.get(0), 1);
        s.addPoints(alive.get(0), 20);
        Player p = Bukkit.getPlayer(alive.get(0));
        p.setFoodLevel(20);
        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        p.setFlying(false);
        p.setCanPickupItems(true);
        PlayerInventory inv = p.getInventory();
        for (int j = 0; j < 36; j++)
            inv.setItem(j, new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setHelmet(new ItemStack(Material.AIR));
        p.setExp(-p.getExp());
        p.getActivePotionEffects().clear();
        p.teleport(spawnLoc(), TeleportCause.PLUGIN);
        return alive.get(0);
    }

    public void endGame() {
        motd = "Game over";
        sendToWSpawn();
        alive.clear();
        origalive.clear();
        dead.clear();
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SURVIVAL);
            if (config.getBoolean("kickOnEnd"))
                p.kickPlayer("Game Over.");
        }
        death = false;
        alreadySponsor = false;
        deathStarted = false;
        gameStarted = false;
        HungerGames.getChestRandomizer().emptyChests();
        spectating.clear();
        sponsored.clear();
        HungerGames.getGame().end();
    }

    public void addToQueue(String name) {
        queued.add(name);
    }

    public void removeFromQueue(String name) {
        queued.remove(name);
    }

    public boolean queueFull() {
        return queued.size() == HungerGames.getInstance().getConfig().getInt("maxPlayers");
    }

    public int posInQueue(String name) {
        return queued.indexOf(name) + 1;
    }

    public String district(String name) {
        for (int i = 0; i < origalive.size(); i++)
            if (origalive.get(i).equals(name)) {
                int temp = i + 1;
                if (temp + 1 > 12)
                    temp = temp - 11;
                return Integer.toString(temp);
            }
        return null;
    }

    public void gameStart() {
        motd = HGUtils.translate("Voting");
        alive.clear();
        origalive.clear();
        dead.clear();
        spectating.clear();
        gameStarted = true;
        invincible = true;
        moveDeny = true;
        vote();
    }

    public Location pSpawnPoint(Player p) {
        return loc(alive.indexOf(p.getName()) + 1);
    }

    public void checkPlayers() {
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        if (queued.size() < config.getInt("minPlayers")) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + HGUtils.translate("Not enough players to start, need at least") + ' ' +
                    Integer.toString(config.getInt("minPlayers")) + ". " + HGUtils.translate("Restarting countdown."));
            vote();
        } else
            finishGameStart();
    }

    public boolean denyMoving() {
        return moveDeny;
    }

    private void finishGameStart() {
        Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Game will now start."));
        ChestRandomizer cr = HungerGames.getChestRandomizer();
        cr.emptyChests();
        cr.randomizeChests();
        for (String aQueued : queued) {
            alive.add(aQueued);
            origalive.add(aQueued);
        }
        queued.clear();
        joinGame();
        motd = HungerGames.getGame().getNext() + ' ' + HGUtils.translate("Pre-game");
        tpCool();
    }

    public void finishGameStart2() {
        motd = HungerGames.getGame().getNext() + ' ' + HGUtils.translate("Game in progress");
        Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Players may now move."));
        moveDeny = false;
        safeTimer();
        if (deathMatch())
            deathCountdown();
        Bukkit.getWorld(HungerGames.getGame().getNext()).setTime(70584000);//70584000: sunrise. 70620000: evening
        day.runTaskLaterAsynchronously(HungerGames.getInstance(), 660 * 20);//11 minutes, Why is this at 11 minutes?
    }

    public void refillChests() {
        HungerGames.getChestRandomizer().randomizeChests();
        Bukkit.broadcastMessage(ChatColor.GREEN + HGUtils.translate("The chests have been refilled."));
    }

    private void joinGame() {
        Player temp;
        for (int i = 0; i < alive.size(); i++) {
            temp = Bukkit.getPlayer(alive.get(i));
            temp.setGameMode(GameMode.SURVIVAL);
            temp.setFoodLevel(20);
            temp.setHealth(temp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            temp.setFlying(false);
            temp.teleport(loc(i + 1), TeleportCause.PLUGIN);
            temp.getActivePotionEffects().clear();
            PlayerInventory inv = temp.getInventory();
            for (int j = 0; j < 36; j++)
                inv.setItem(j, new ItemStack(Material.AIR));
            inv.setBoots(new ItemStack(Material.AIR));
            inv.setLeggings(new ItemStack(Material.AIR));
            inv.setChestplate(new ItemStack(Material.AIR));
            inv.setHelmet(new ItemStack(Material.AIR));
            temp.setExp(-temp.getExp());
            s.addGame(alive.get(i), 1);
        }
    }

    private Location loc(int number) {
        String world = HungerGames.getGame().getNext();
        String pathX = world + ".s" + Integer.toString(number) + ".x";
        String pathY = world + ".s" + Integer.toString(number) + ".y";
        String pathZ = world + ".s" + Integer.toString(number) + ".z";
        return new Location(Bukkit.getWorld(world), customConfig.getInt(pathX), customConfig.getInt(pathY), customConfig.getInt(pathZ));
    }

    public Location spawnLoc() {
        String world = customConfig.getString("worldS.world");
        String pathX = "worldS.x";
        String pathY = "worldS.y";
        String pathZ = "worldS.z";
        return new Location(Bukkit.getWorld(world), customConfig.getInt(pathX), customConfig.getInt(pathY), customConfig.getInt(pathZ));
    }

    public void safeTimer() {//Why not just print it every messageFrequency..?
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        if (stime == 0)
            stime = config.getInt("safeTime");
        int freq = config.getInt("messageFrequency");


        BukkitRunnable invinc = new BukkitRunnable() {
            @Override
            public void run() {
                if (freq >= stime) {//TODO should this be stime <= 0 instead
                    Bukkit.broadcastMessage(HGUtils.translate("Players are no longer invincible."));
                    invincible = false;
                    cancel();
                } else {
                    Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Players are now invincible for") + ' ' + getTime(stime));
                    stime -= freq;
                }
            }
        };
        invinc.runTaskTimerAsynchronously(Necessities.getInstance(), 0, freq * 20);


        /*Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Players are now invincible for") + ' ' + getTime(stime));
        if (freq >= stime) {
            int temp = stime;
            stime = 0;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().safeEnd();
                }
            };
            try {
                invinc.schedule(timerTask, temp * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            stime -= freq;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().safeTimer();
                }
            };
            try {
                invinc.schedule(timerTask, freq * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public void tpCool() {
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        if (tptime == 0)
            tptime = config.getInt("tpCoolDown");
        int freq = config.getInt("messageFrequency");
        Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Players may move in") + ' ' + getTime(tptime));
        if (tptime == 1 || tptime == 0) {
            tptime = 0;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().finishGameStart2();
                }
            };
            try {
                noMove.schedule(timerTask, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (tptime > 1 && tptime <= 10) {
            tptime -= 1;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().tpCool();
                }
            };
            try {
                noMove.schedule(timerTask, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (freq >= tptime) {
            int temp = tptime - 10;
            tptime = 10;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().tpCool();
                }
            };
            try {
                noMove.schedule(timerTask, temp * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (tptime - freq >= 10) {

            tptime -= freq;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().tpCool();
                }
            };
            try {
                noMove.schedule(timerTask, freq * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.broadcastMessage("If this shows up there is an error.");
        }
    }

    public void deathCountdown() {
        if (!deathStarted) {
            YamlConfiguration config = HungerGames.getInstance().getConfig();
            if (dtime == 0)
                dtime = config.getInt("deathTime");
            int freq = config.getInt("messageFrequency");
            Bukkit.broadcastMessage(ChatColor.DARK_RED + HGUtils.translate("Death match will start in") + ' ' + getTime(dtime));
            if (dtime == 1 || dtime == 0) {
                dtime = 0;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        HungerGames.getPlayers().startDeath();
                    }
                };
                try {
                    t.schedule(timerTask, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (dtime > 1 && dtime <= 10) {
                dtime -= 1;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        HungerGames.getPlayers().deathCountdown();
                    }
                };
                try {
                    t.schedule(timerTask, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (freq >= dtime) {
                int temp = dtime - 10;
                dtime = 10;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        HungerGames.getPlayers().deathCountdown();
                    }
                };
                try {
                    t.schedule(timerTask, temp * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (dtime - freq >= 10) {
                dtime -= freq;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        HungerGames.getPlayers().deathCountdown();
                    }
                };
                try {
                    t.schedule(timerTask, freq * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Bukkit.broadcastMessage("If this shows up there is an error.");
            }
        }
    }

    public void vote() {
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        if (time == 0)
            time = config.getInt("votingTime");
        int freq = config.getInt("messageFrequency");
        if (config.getBoolean("showJoin")) {
            Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Game will start in") + ' ' + getTime(time) + ' ' +
                    HGUtils.translate("please use /hg join to join."));
        } else
            Bukkit.broadcastMessage(ChatColor.WHITE + HGUtils.translate("Game will start in") + ' ' + getTime(time));
        HungerGames.getGame().holdVote();
        if (freq >= time) {
            int temp = time;
            time = 0;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().checkPlayers();
                }
            };
            try {
                count.schedule(timerTask, temp * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            time -= freq;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HungerGames.getPlayers().vote();
                }
            };
            try {
                count.schedule(timerTask, freq * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getTime(int time) {
        int seconds = time % 60;
        int minutes = time / 60;
        String message = "";
        if (minutes == 1)
            message = "1 " + HGUtils.translate("minute");
        else if (minutes > 1)
            message = Integer.toString(minutes) + ' ' + HGUtils.translate("minutes");
        if (seconds == 1)
            message += " 1 " + HGUtils.translate("second");
        else if (seconds > 1)
            message += ' ' + Integer.toString(seconds) + ' ' + HGUtils.translate("seconds");
        message = message.trim();
        return message;
    }
}