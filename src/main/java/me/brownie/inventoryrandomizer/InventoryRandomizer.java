package me.brownie.inventoryrandomizer;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class InventoryRandomizer extends JavaPlugin implements Listener {

    Inventory inv = Bukkit.createInventory(null, 18, "Inventory Randomizer");
    List<Collection> OnlinePlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        OnlinePlayers.add(Bukkit.getOnlinePlayers());
        createGui();
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
                addPlayers(OnlinePlayers);
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
            p.sendMessage(ChatColor.RED + "[Inventory Randomizer] That command does not exist!");
        }
        return false;
    }

    //   Adds a player to the GUI onJoin
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Thing(e.getPlayer());
    }

    //   Checks if a player has an open slot in their inventory
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
        //   Checks if a player has permission to use a specific command
        if (p.hasPermission(perm) || p.hasPermission("inventoryrandomizer.*") || p.hasPermission("*")) return true;
        //   If not, it informs the player who tried running sed command
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

    //   Adds all players (in the list given) to the gui
    @Deprecated
    public void addPlayers() {
        if (OnlinePlayers.size() >= 18) {
            Bukkit.getLogger().info("[Inventory Randomizer] There are too many players online!");
            return;
        }
        //   Get the current state of the gui
        Inventory inventory = inv;
        //   Clear the copy made just above
        inventory.clear();
        //   Add all players currently online to the gui
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
        //   Re-add close button
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
        addPlayers();
    }

    /*
    so this one is hard to explain, it will only be used when the command is sent in-game
    but not when the plugin is initially being started up, that way the plugin can run
    ven if no players are on the server.
    */
    public void Thing(Player p) {
        List<Collection> OnlinePlayers = new ArrayList<>();
        OnlinePlayers.add(Bukkit.getOnlinePlayers());
        if (OnlinePlayers.contains(Bukkit.getPlayer(p.getName()))) {
            OnlinePlayers.remove(Bukkit.getPlayer(p.getName()));
        }
        addPlayers(OnlinePlayers);
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