package me.brownie.inventoryrandomizer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Menu {

    private InventoryRandomizer main;
    public void Menu(InventoryRandomizer Main) {
        this.main = Main;
    }

    //   Creates the randomizer GUI
    public void createGui() {
        //   Create inventory
        Inventory inv = Bukkit.createInventory(null,18, "Select A Player!");
        //   Create close button
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        lore.add(ChatColor.LIGHT_PURPLE + "Click here to close this menu");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(19,item);
        //   Add current online players
        Player owner = (Player) Bukkit.getPlayer(main.getConfig().getString("Owner"));
        addPlayers(false,owner);
    }

    //   Adds all players to the gui
    @Deprecated
    public void addPlayers(boolean command, OfflinePlayer p) {
        if (main.OnlinePlayers.size() >= 18) {
            Bukkit.getLogger().info("[Inventory Randomizer] There are too many players online!");
            return;
        }
        //   Checks if its the initial gui setup or if someone is using a command
        if (!command) {
            main.OnlinePlayers.remove(p);
        }
        //   Get the current state of the gui
        Inventory inventory = main.inv;
        //   Clear the copy made just above
        inventory.clear();
        //   Add all players currently online to the gui
        for (int i = 0; i <= main.OnlinePlayers.size(); i++) {
            Player tempP = (Player) main.OnlinePlayers.get(i);
            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setOwningPlayer(tempP);
            meta.setDisplayName(tempP.getDisplayName());
            lore.add(ChatColor.DARK_PURPLE + "Click to randomize this player's inventory!");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i,item);
        }
        //   Re-add close button
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        lore.add(ChatColor.LIGHT_PURPLE + "Click to close the menu");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(18,item);
        main.inv = inventory;
        //   Re-add the removed player to the OnlinePlayers list for the future
        Collection pReTyped = (Collection) p;
        main.OnlinePlayers.add(pReTyped);
    }
}
