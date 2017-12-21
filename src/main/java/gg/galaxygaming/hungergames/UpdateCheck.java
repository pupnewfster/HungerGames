package gg.galaxygaming.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateCheck {
    private static boolean update;
    private static String newVersion = "";
    private static String currentVersion = "";

    public void tellOp(Player p) {
        if (update)
            p.sendMessage(ChatColor.WHITE + HGUtils.translate("Update found") + ": v" + currentVersion + ' ' +
                    HGUtils.translate("is outdated please update to") + " v" + newVersion);
        else
            p.sendMessage(ChatColor.GREEN + HGUtils.translate("Hunger Games is up to date."));
    }

    public void checkForUpdate() {
        currentVersion = Bukkit.getPluginManager().getPlugin("Hunger Games").getDescription().getVersion();
        try {
            URL url = new URL("http://crossge.com/version");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            newVersion = response.toString();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + HGUtils.translate("Not connected to the internet"));
            return;
        }
        int major = Integer.parseInt(newVersion.split("\\.")[0]);
        int minor = Integer.parseInt(newVersion.split("\\.")[1]);
        int bug = Integer.parseInt(newVersion.split("\\.")[2]);
        int curMajor = Integer.parseInt(currentVersion.split("\\.")[0]);
        int curMinor = Integer.parseInt(currentVersion.split("\\.")[1]);
        int curBug = Integer.parseInt(currentVersion.split("\\.")[2]);
        if (major > curMajor || minor > curMinor || bug > curBug)
            update = true;
        if (update)
            Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + HGUtils.translate("Update found") + ": v" + currentVersion +
                    ' ' + HGUtils.translate("is outdated please update to") + " v" + newVersion);
        else
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + HGUtils.translate("Hunger Games is up to date."));
    }
}