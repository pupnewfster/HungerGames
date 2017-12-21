package gg.galaxygaming.hungergames.Commands;

import gg.galaxygaming.hungergames.Kits;
import gg.galaxygaming.hungergames.Sponsor;
import gg.galaxygaming.hungergames.Stats;
import gg.galaxygaming.hungergames.Variables;
import org.bukkit.command.CommandSender;

public class Cmd {
    Variables var = new Variables();
    Sponsor sp = new Sponsor();
    Kits kit = new Kits();
    Stats s = new Stats();

    public boolean commandUse(CommandSender sender, String[] args) {
        return false;
    }
}