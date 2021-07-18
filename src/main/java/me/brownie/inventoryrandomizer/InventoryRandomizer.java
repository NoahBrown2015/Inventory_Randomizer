package me.brownie.inventoryrandomizer;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class InventoryRandomizer extends JavaPlugin implements Listener {

    public Events events;
    public Utils util;
    public Menu menu;

    Inventory inv = Bukkit.createInventory(null, 18, "Inventory Randomizer");
    List<Collection> OnlinePlayers = new ArrayList<>();
    OfflinePlayer CurrentCompassUser;

    @Override
    public void onEnable() {
        OnlinePlayers.add(Bukkit.getOnlinePlayers());
        menu.createGui();
        this.getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //   Checks if the command sent is this plugin's
        if (label.equalsIgnoreCase("InventoryRandomizer")) {
            //   Handles the console asking for the compass
            if (!(sender instanceof Player)) {
                sender.sendMessage("[Inventory Randomizer] This command is only available in-game!");
                return true;
            }
            //   Define sender as a player
            Player p = (Player) sender;
            //   Handles the reloading of the randomizer gui
            if (args[0].equalsIgnoreCase("reload")) {
                //   Checks for permission
                if (!util.permCheck(p,"reload")) return true;
                //   Adds all current players to the GUI
                menu.addPlayers(true,CurrentCompassUser);
                //   Informs command sender the reload was successful
                p.sendMessage(ChatColor.GOLD + "[Inventory Randomizer] Plugin successfully reloaded!");
                //   Logs that the plugin has successfully been reloaded
                this.getLogger().info("Inventory Randomizer has been");
                return true;
            }
            //   Handles the acquiring of the randomizer item
            if (args[0].equalsIgnoreCase("get")) {
                //   Checks for permission
                if (!util.permCheck(p, "get")) return true;
                //   Checks for an open slot in the player's inventory
                Inventory pInv = p.getInventory();
                for (ItemStack item: pInv.getContents()) {
                    if (item == null || item.getType() == Material.AIR) {
                        //   If found, give the player the item and inform them
                        p.getInventory().addItem(getItem());
                        p.sendMessage(ChatColor.GOLD + "[Inventory Randomizer] You have been given an inventory randomizer");
                        return true;
                    } else {
                        //   If not found, get the player's location and drop the item there and inform them
                        Location loc = p.getLocation();
                        World world = loc.getWorld();
                        world.dropItemNaturally(loc, getItem());
                        p.sendMessage(ChatColor.GOLD + "[Inventory Randomizer] An inventory randomizer has been left on the ground nearby");
                    }
                }
                return true;
            }
            //   If the args don't match get or reload, inform the player the specified parameters are invalid
            p.sendMessage(ChatColor.RED + "[Inventory Randomizer] That command does not exist!");
        }
        return true;
    }

    public ItemStack getItem() {
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

    //   Randomizes the order of a player's inventory
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