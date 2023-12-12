package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerBreakOnClaim implements Listener {

    private Player player;
    private Block brokenBlock;
    private final Claimspace claimspace = new Claimspace();
    private ArrayList<Integer> playerIdListTrust = new ArrayList<>();
    private ArrayList<Integer> playerIdListAccess = new ArrayList<>();
    private Connection connection;

    public PlayerBreakOnClaim(Connection connectonInstance){
        this.connection = connectonInstance;
    }

    @EventHandler
    public void onPlayerMine(BlockBreakEvent e){
        player = e.getPlayer();
        brokenBlock = e.getBlock();
        if (!claimspace.isInClaim(Objects.requireNonNull(brokenBlock.getLocation().getWorld()).getName(), brokenBlock.getLocation().getX(),
                brokenBlock.getLocation().getZ(), connection, "uuid").equalsIgnoreCase(player.getUniqueId().toString()) && !claimspace.isInClaim(brokenBlock.getLocation().getWorld().getName(),
                brokenBlock.getLocation().getX(), brokenBlock.getLocation().getZ(), connection, "name").isEmpty()){
            int claimId = claimspace.getClaimId(brokenBlock.getLocation().getWorld().getName(),
                    brokenBlock.getLocation().getX(), brokenBlock.getLocation().getZ(), connection, player.getUniqueId().toString());
            if (claimId == -1){
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&4>&cThis ground does not belong to you!"));
            }else {
                int playerId = claimspace.getPlayerId(player.getUniqueId().toString(), connection);
                playerIdListTrust.clear();
                playerIdListAccess.clear();
                playerIdListTrust = claimspace.getAllTrustedParties(playerIdListTrust, claimId, 0, connection);
                playerIdListAccess = claimspace.getAllGrantedAccessParties(playerIdListAccess, claimId, 0, connection);
                boolean cancellationStatus = true;
                for (int i = 0; i < playerIdListTrust.size(); i++) {
                    if (playerId == playerIdListTrust.get(i)) {
                        cancellationStatus = false;
                    }
                }
                for (int i = 0; i < playerIdListAccess.size(); i++) {
                    if (playerId == playerIdListAccess.get(i)) {
                        if (brokenBlock.getType().equals(Material.WHEAT_SEEDS) ||
                                brokenBlock.getType().equals(Material.PUMPKIN_SEEDS) ||
                                brokenBlock.getType().equals(Material.MELON_SEEDS) ||
                                brokenBlock.getType().equals(Material.BEETROOT_SEEDS) ||
                                brokenBlock.getType().equals(Material.MELON_STEM) ||
                                brokenBlock.getType().equals(Material.MELON) ||
                                brokenBlock.getType().equals(Material.PUMPKIN_STEM) ||
                                brokenBlock.getType().equals(Material.PUMPKIN) ||
                                brokenBlock.getType().equals(Material.BEETROOT) ||
                                brokenBlock.getType().equals(Material.SUGAR_CANE) ||
                                brokenBlock.getType().equals(Material.WHEAT) ||
                                brokenBlock.getType().equals(Material.CARROT) ||
                                brokenBlock.getType().equals(Material.POTATO)) {
                            cancellationStatus = false;
                        }
                    }
                }
                for (int i = 0; i < playerIdListTrust.size(); i++){
                    if (playerId == playerIdListTrust.get(i)){
                        cancellationStatus = false;
                    }
                }
                if (claimspace.isInClaim(player.getLocation().getWorld().getName(), player.getLocation().getX(), player.getLocation().getZ(), connection, "rentedUUID") != null) {
                    String compUUID = claimspace.isInClaim(player.getLocation().getWorld().getName(), player.getLocation().getX(), player.getLocation().getZ(), connection, "rentedUUID");
                    if (compUUID.equalsIgnoreCase(player.getUniqueId().toString())) {
                        if (brokenBlock.getType().equals(Material.SPRUCE_SIGN) ||
                                brokenBlock.getType().equals(Material.SPRUCE_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.ACACIA_SIGN) ||
                                brokenBlock.getType().equals(Material.ACACIA_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.BIRCH_SIGN) ||
                                brokenBlock.getType().equals(Material.BIRCH_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.CRIMSON_SIGN) ||
                                brokenBlock.getType().equals(Material.CRIMSON_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.DARK_OAK_SIGN) ||
                                brokenBlock.getType().equals(Material.DARK_OAK_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.JUNGLE_SIGN) ||
                                brokenBlock.getType().equals(Material.JUNGLE_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.OAK_SIGN) ||
                                brokenBlock.getType().equals(Material.OAK_WALL_SIGN) ||
                                brokenBlock.getType().equals(Material.WARPED_SIGN) ||
                                brokenBlock.getType().equals(Material.WARPED_WALL_SIGN)) {
                            cancellationStatus = false;
                        }
                    }
                }
                if (cancellationStatus) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&4>&cYou cannot build in a claimed area!"));
                }
            }
        }
    }
}
