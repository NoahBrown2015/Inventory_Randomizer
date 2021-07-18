package me.brownie.inventoryrandomizer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    private InventoryRandomizer main;
    public void Utils(InventoryRandomizer Main) {
        this.main = Main;
    }

    //   Checks for permissions
    public boolean permCheck(Player p, String perm) {
        perm = "inventoryrandomizer." + perm;
        //   Checks if a player has permission to use a specific command
        if (p.hasPermission(perm) || p.hasPermission("inventoryrandomizer.*") || p.hasPermission("*") || p.isOp()) return true;
        //   If not, it informs the player who tried running sed command
        p.sendMessage(ChatColor.RED + "[Inventory Randomizer] You do not have permission to use that command!");
        return false;
    }
}
