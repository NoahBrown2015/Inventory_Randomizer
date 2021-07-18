package me.brownie.inventoryrandomizer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class InventoryRandomizer extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new onClick(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //   Checks if the command sent is this plugin's
        if (label.equalsIgnoreCase("InventoryRandomizer")) {
            //   Handles the console asking for the compass
            if (!(sender instanceof Player)) {
                sender.sendMessage("[Inventory Randomizer] This command is only available in-game!");
                return true;
            }
            //   Handles the reloading of the randomizer gui
            if (args[0].equalsIgnoreCase("reload")) {
                setupGUI();
            }
        }

        return false;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        //   Defines item
        ItemStack item = e.getItem();
        //   Checks if the item is the plugin's
        if (item.getType() != Material.CLOCK || !(item.getItemMeta().getDisplayName().equalsIgnoreCase("Inventory Randomizer"))
                || !(item.getItemMeta().hasLore())) { return; }
        //   Opens randomizer GUI
    }

    public void setupGUI() {
        Inventory inv = Bukkit.createInventory(null, 9, "Inventory Randomizer");
        for (int i = 0; i <= Bukkit.getOnlinePlayers().size(); i++) {

        }
    }

    public ItemStack getItem(Player p) {
        String name = p.getDisplayName().toString();
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName("Inventory Randomizer");
        lore.add("");
        lore.add("Click to open randomizer gui!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void Randomize(Player p) {
        //   Create new list to hold items
        List<ItemStack> list = new ArrayList<ItemStack>();
        //   Add all items in inventory to the list in order
        for (ItemStack items : p.getInventory().getContents()) {
            list.add(items);
        }
        //   Clear the player's inventory
        p.getInventory().clear();
        //   Create a new random
        Random random = new Random();
        //   Shuffle the order of the inventory items in the list
        Collections.shuffle(list);
        //   Add the list items back into the player's inventory
        for (ItemStack item : list) {
            int r = random.nextInt(35);
            if (p.getInventory().getItem(r) != null && p.getInventory().getItem(r).getType() != Material.AIR) return;
                p.getInventory().setItem(r, item);
                list.remove(p.getInventory().getItem(r));
        }
    }


}