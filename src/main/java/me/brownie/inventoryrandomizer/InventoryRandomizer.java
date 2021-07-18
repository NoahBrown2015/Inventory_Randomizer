package me.brownie.inventoryrandomizer;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class InventoryRandomizer extends JavaPlugin implements Listener {

    Inventory inv = Bukkit.createInventory(null, 18, "Inventory Randomizer");

    @Override
    public void onEnable() {
        setupGUI();
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
                if (!permCheck(p,"reload")) return true;
                setupGUI();
                p.sendMessage(ChatColor.GOLD + "[Inventory Randomizer] Plugin successfully reloaded!");
                this.getLogger().info("Inventory Randomizer has been");
                return true;
            }
            if (args[0].equalsIgnoreCase("get")) {
                if (!permCheck(p, "get")) return true;
                if (hasAvaliableSlot(p)) {
                    p.getInventory().addItem(getItem(p));
                    p.sendMessage(ChatColor.GOLD + "[Inventory Randomizer] You have been given an inventory randomizer");
                } else {
                    Location loc = p.getLocation();
                    World world = loc.getWorld();
                    p.sendMessage(ChatColor.GOLD + "[Inventory Randomizer] An inventory randomizer has been left on the ground nearby");
                }
                return true;
            }
            p.sendMessage(ChatColor.RED + "That command does not exist!");
        }
        return false;
    }

    public boolean hasAvaliableSlot(Player p){
        Inventory inv = p.getInventory();
        for (ItemStack item: inv.getContents()) {
            if(item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public boolean permCheck(Player p, String perm) {
        perm = "inventoryrandomizer." + perm;
        if (p.hasPermission(perm) || p.hasPermission("inventoryrandomizer.*") || p.hasPermission("*")) return true;
        p.sendMessage(ChatColor.RED + "[Inventory Randomizer] You do not have permission to use that command!");
        return false;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        //   Defines item
        ItemStack item = e.getItem();
        //   Checks if the item is the plugin's
        if (item.getType() != Material.CLOCK || !(item.getItemMeta().getDisplayName().equalsIgnoreCase("Inventory Randomizer"))
                || !(item.getItemMeta().hasLore())) return;
        //   Opens randomizer GUI
        e.getPlayer().openInventory(inv);
    }

    @Deprecated
    public void setupGUI() {
        Inventory inventory = inv;
        inventory.clear();
        List<Collection> OnlinePlayers = new ArrayList<>();
        OnlinePlayers.add(Bukkit.getOnlinePlayers());
        for (int i = 0; i <= OnlinePlayers.size(); i++) {
            Player p = (Player) OnlinePlayers.get(i);
            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setOwningPlayer(p);
            lore.add(ChatColor.DARK_PURPLE + "Click to randomize this player's inventory!");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i,item);
        }
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        lore.add(ChatColor.LIGHT_PURPLE + "Click to close the menu");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(19,item);
        inv = inventory;
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