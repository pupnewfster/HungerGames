package gg.galaxygaming.hungergames;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Initialization {//TODO replace a bunch of the below files with the map data structure and then some with mysql tables
    Stats s = new Stats();
    UpdateCheck up = new UpdateCheck();
    private File customConfigFileSQL = new File(HungerGames.getInstance().getDataFolder(), "sql.yml");
    private File customConfigFileChest = new File(HungerGames.getInstance().getDataFolder(), "chests.yml");
    private File customConfigFileSponsor = new File(HungerGames.getInstance().getDataFolder(), "sponsors.yml");
    private File customConfigFileKits = new File(HungerGames.getInstance().getDataFolder(), "kits.yml");
    private File customConfigFileBreakable = new File(HungerGames.getInstance().getDataFolder(), "breakable.yml");
    private File customConfigFilePlaceable = new File(HungerGames.getInstance().getDataFolder(), "placeable.yml");
    private File customConfigFileCommands = new File(HungerGames.getInstance().getDataFolder(), "commands.yml");

    public void initiateFiles() {
        if (!HungerGames.getInstance().getDataFolder().exists()) {
            if (!HungerGames.getInstance().getDataFolder().mkdir())
                return; //Output error message if failed
        }
        createYaml();
        HungerGames.getGame().initMaps();
        s.connect();
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        if (config.getBoolean("checkForUpdates"))//TODO remove/comment out
            up.checkForUpdate();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void addYML(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
    }

    public void createYaml() {//TODO switch to using Necessities utils to addyml
        addYML(new File(HungerGames.getInstance().getDataFolder(), "chestlocs.yml"));
        addYML(new File(HungerGames.getInstance().getDataFolder(), "spawns.yml"));
        addYML(new File(HungerGames.getInstance().getDataFolder(), "kitprices.yml"));
        addYML(new File(HungerGames.getInstance().getDataFolder(), "stats.yml"));

        if (!HungerGames.getInstance().getConfigFile().exists()) {
            try {
                HungerGames.getInstance().getConfigFile().createNewFile();
                YamlConfiguration config = HungerGames.getInstance().getConfig();
                config.set("minPerChest", 3);
                config.set("maxPerChest", 7);
                config.set("itemsPerSponsor", 3);
                config.set("useKits", false);
                config.set("useMySQL", false);
                config.set("maxPlayers", 24);
                config.set("placeBlocks", false);
                config.set("safeTime", 15);
                config.set("tpCoolDown", 30);
                config.set("minPlayers", 2);
                config.set("playersDeathMatch", 3);
                config.set("deathTime", 60);
                config.set("updateMOTD", false);
                config.set("language", "en");
                config.set("votingTime", 180);
                config.set("messageFrequency", 30);
                config.set("checkForUpdates", true);
                config.set("showScore", true);
                config.set("spectateOnDeath", false);
                config.set("showJoin", true);
                config.set("kickOnEnd", false);
                config.save(HungerGames.getInstance().getConfigFile());
            } catch (IOException ignored) {
            }
        } else {
            YamlConfiguration config = HungerGames.getInstance().getConfig();
            try {
                if (!config.contains("minPerChest"))
                    config.set("minPerChest", 3);
                if (!config.contains("maxPerChest"))
                    config.set("maxPerChest", 7);
                if (!config.contains("itemsPerSponsor"))
                    config.set("itemsPerSponsor", 3);
                if (!config.contains("useKits"))
                    config.set("useKits", false);
                if (!config.contains("useMySQL"))
                    config.set("useMySQL", false);
                if (!config.contains("maxPlayers"))
                    config.set("maxPlayers", 24);
                if (!config.contains("placeBlocks"))
                    config.set("placeBlocks", false);
                if (!config.contains("safeTime"))
                    config.set("safeTime", 15);
                if (!config.contains("tpCoolDown"))
                    config.set("tpCoolDown", 30);
                if (!config.contains("minPlayers"))
                    config.set("minPlayers", 2);
                if (!config.contains("deathTime"))
                    config.set("deathTime", 60);
                if (!config.contains("playersDeathMatch"))
                    config.set("playersDeathMatch", 3);
                if (!config.contains("updateMOTD"))
                    config.set("updateMOTD", false);
                if (!config.contains("language"))
                    config.set("language", "en");
                if (!config.contains("votingTime"))
                    config.set("votingTime", 180);
                if (!config.contains("messageFrequency"))
                    config.set("messageFrequency", 30);
                if (!config.contains("checkForUpdates"))
                    config.set("checkForUpdates", true);
                if (!config.contains("showScore"))
                    config.set("showScore", true);
                if (!config.contains("spectateOnDeath"))
                    config.set("spectateOnDeath", false);
                if (!config.contains("showJoin"))
                    config.set("showJoin", true);
                if (!config.contains("kickOnEnd"))
                    config.set("kickOnEnd", false);
                config.save(HungerGames.getInstance().getConfigFile());
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFileCommands.exists()) {
            try {
                customConfigFileCommands.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFileBreakable);
                customConfig.set("hungergames", true);
                customConfig.set("survivalgames", true);
                customConfig.set("hg", true);
                customConfig.set("sg", true);
                customConfig.save(customConfigFileCommands);
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFileBreakable.exists()) {
            try {
                customConfigFileBreakable.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFileBreakable);
                customConfig.set("18", true);
                customConfig.set("51", true);
                customConfig.save(customConfigFileBreakable);
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFilePlaceable.exists()) {
            try {
                customConfigFilePlaceable.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFilePlaceable);
                customConfig.set("18", true);
                customConfig.set("259", true);
                customConfig.save(customConfigFilePlaceable);
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFileSQL.exists()) {
            try {//hostname, port, dbName, username, password
                customConfigFileSQL.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFileSQL);
                customConfig.set("hostname", "localhost");
                customConfig.set("port", "3306");
                customConfig.set("dbName", "HungerGames");
                customConfig.set("username", "username");
                customConfig.set("password", "password");
                customConfig.save(customConfigFileSQL);
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFileKits.exists()) {
            try {
                customConfigFileKits.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFileKits);
                customConfig.set("Tribute.345", 1);//compass
                customConfig.set("Tribute.346", 1);//fishing rod
                customConfig.set("Tribute.272", 1);//stone sword
                customConfig.set("Tribute.297", 2);//bread
                customConfig.set("Survivor.271", 1);//wood axe
                customConfig.set("Survivor.261", 1);//bow
                customConfig.set("Survivor.262", 20);//arrow
                customConfig.set("Survivor.363", 3);//raw steak
                customConfig.save(customConfigFileKits);
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFileChest.exists()) {
            try {
                customConfigFileChest.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFileChest);
                customConfig.set("39", 0.86);//brown mushroom
                customConfig.set("40", 0.86);//red mushroom
                customConfig.set("258", 0.08);//iron axe
                customConfig.set("259", 0.25);//flint and steel
                customConfig.set("260", 4.71);//apple
                customConfig.set("261", 0.86);//bow
                customConfig.set("262", 0.86);//arrow
                customConfig.set("263", 0.86);//coal
                customConfig.set("264", 0.04);//diamond
                customConfig.set("265", 0.43);//iron ingot
                customConfig.set("266", 2.57);//gold ingot
                customConfig.set("267", 0.16);//iron sword
                customConfig.set("268", 0.86);//wood sword
                customConfig.set("271", 0.86);//wood axe
                customConfig.set("272", 0.08);//stone sword
                customConfig.set("275", 0.08);//stone axe
                customConfig.set("280", 5.14);//stick
                customConfig.set("281", 0.86);//bowl
                customConfig.set("282", 0.86);//stew
                customConfig.set("283", 0.08);//gold sword
                customConfig.set("286", 0.08);//gold axe
                customConfig.set("287", 0.43);//string
                customConfig.set("288", 0.86);//feather
                customConfig.set("296", 2.06);//wheat
                customConfig.set("297", 0.68);//bread
                customConfig.set("298", 0.68);//leather hat
                customConfig.set("299", 0.43);//leather chest
                customConfig.set("300", 0.43);//leather pants
                customConfig.set("301", 0.86);//leather boots
                customConfig.set("302", 0.08);//chain hat
                customConfig.set("303", 0.04);//chain chest
                customConfig.set("304", 0.04);//chain pants
                customConfig.set("305", 0.08);//chain boots
                customConfig.set("306", 0.08);//iron hat
                customConfig.set("309", 0.08);//iron boots
                customConfig.set("314", 0.08);//gold hat
                customConfig.set("315", 0.04);//gold chest
                customConfig.set("316", 0.04);//gold pants
                customConfig.set("317", 0.08);//gold boots
                customConfig.set("318", 0.86);//flint
                customConfig.set("319", 3.00);//raw pork
                customConfig.set("320", 0.43);//cooked pork
                customConfig.set("322", 0.08);//gold apple
                customConfig.set("332", 2.57);//snowball
                customConfig.set("333", 0.43);//boat
                customConfig.set("334", 5.14);//leather
                customConfig.set("344", 7.71);//egg
                customConfig.set("345", 6.00);//compass
                customConfig.set("346", 0.43);//fishing rod
                customConfig.set("347", 6.00);//clock
                customConfig.set("349", 5.14);//raw fish
                customConfig.set("350", 0.68);//cooked fish
                customConfig.set("357", 6.00);//cookie
                customConfig.set("359", 2.57);//shears
                customConfig.set("360", 6.00);//melon
                customConfig.set("363", 3.00);//raw steak
                customConfig.set("364", 0.43);//steak
                customConfig.set("365", 3.85);//raw chicken
                customConfig.set("366", 0.68);//chicken
                customConfig.set("368", 0.34);//ender pearl
                customConfig.set("391", 4.71);//carrot
                customConfig.set("392", 7.71);//potato
                customConfig.set("393", 0.68);//cooked potato
                customConfig.set("395", 6.00);//map
                customConfig.set("396", 0.08);//gold carrot
                customConfig.set("400", 0.43);//pie
                customConfig.save(customConfigFileChest);
            } catch (IOException ignored) {
            }
        }
        if (!customConfigFileSponsor.exists()) {
            try {
                customConfigFileSponsor.createNewFile();
                YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFileSponsor);
                customConfig.set("258", 0.08);//iron axe
                customConfig.set("259", 0.25);//flint and steel
                customConfig.set("260", 4.71);//apple
                customConfig.set("261", 0.86);//bow
                customConfig.set("262", 0.86);//arrow
                customConfig.set("263", 0.86);//coal
                customConfig.set("264", 0.06);//diamond
                customConfig.set("265", 0.43);//iron ingot
                customConfig.set("266", 2.57);//gold ingot
                customConfig.set("267", 0.16);//iron sword
                customConfig.set("268", 0.86);//wood sword
                customConfig.set("271", 0.86);//wood axe
                customConfig.set("272", 0.08);//stone sword
                customConfig.set("275", 0.08);//stone axe
                customConfig.set("276", 0.15);//diamond sword
                customConfig.set("279", 0.15);//diamond axe
                customConfig.set("280", 5.14);//stick
                customConfig.set("282", 0.86);//stew
                customConfig.set("283", 0.18);//gold sword
                customConfig.set("286", 0.18);//gold axe
                customConfig.set("297", 0.68);//bread
                customConfig.set("298", 0.68);//leather hat
                customConfig.set("299", 0.43);//leather chest
                customConfig.set("300", 0.43);//leather pants
                customConfig.set("301", 0.86);//leather boots
                customConfig.set("302", 0.18);//chain hat
                customConfig.set("303", 0.14);//chain chest
                customConfig.set("304", 0.14);//chain pants
                customConfig.set("305", 0.18);//chain boots
                customConfig.set("306", 0.18);//iron hat
                customConfig.set("307", 0.14);//iron chest
                customConfig.set("308", 0.14);//iron pants
                customConfig.set("309", 0.18);//iron boots
                customConfig.set("310", 0.14);//diamond hat
                customConfig.set("311", 0.12);//diamond chest
                customConfig.set("312", 0.12);//diamond pants
                customConfig.set("313", 0.14);//diamond boots
                customConfig.set("314", 0.18);//gold hat
                customConfig.set("315", 0.14);//gold chest
                customConfig.set("316", 0.14);//gold pants
                customConfig.set("317", 0.18);//gold boots
                customConfig.set("319", 3.00);//raw pork
                customConfig.set("320", 0.43);//cooked pork
                customConfig.set("322", 0.08);//gold apple
                customConfig.set("333", 0.43);//boat
                customConfig.set("345", 6.00);//compass
                customConfig.set("346", 0.43);//fishing rod
                customConfig.set("347", 6.00);//clock
                customConfig.set("349", 5.14);//raw fish
                customConfig.set("350", 0.68);//cooked fish
                customConfig.set("357", 6.00);//cookie
                customConfig.set("359", 2.57);//shears
                customConfig.set("360", 6.00);//melon
                customConfig.set("363", 3.00);//raw steak
                customConfig.set("364", 0.43);//steak
                customConfig.set("365", 3.85);//raw chicken
                customConfig.set("366", 0.68);//chicken
                customConfig.set("368", 0.34);//ender pearl
                customConfig.set("391", 4.71);//carrot
                customConfig.set("392", 7.71);//potato
                customConfig.set("393", 0.68);//cooked potato
                customConfig.set("395", 6.00);//map
                customConfig.set("396", 0.08);//gold carrot
                customConfig.set("400", 0.43);//pie
                customConfig.save(customConfigFileSponsor);
            } catch (Exception ignored) {
            }
        }
    }
}