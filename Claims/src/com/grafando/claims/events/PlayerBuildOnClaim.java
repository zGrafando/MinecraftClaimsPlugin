package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerBuildOnClaim implements Listener{

    private Player player;
    private Block placedBlock;
    private Claimspace claimspace = new Claimspace();
    private Connection connection;
    private boolean cancellationStatus;
    private ArrayList<Integer> playerIdListTrust = new ArrayList<>();
    private ArrayList<Integer> playerIdListAccess = new ArrayList<>();

    public PlayerBuildOnClaim(Connection connectonInstance){
        this.connection = connectonInstance;
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e){
        player = e.getPlayer();
        placedBlock = e.getBlock();
        if (!claimspace.isInClaim(Objects.requireNonNull(placedBlock.getLocation().getWorld()).getName(), placedBlock.getLocation().getX(),
                placedBlock.getLocation().getZ(), connection, "uuid").equalsIgnoreCase(player.getUniqueId().toString()) && !claimspace.isInClaim(placedBlock.getLocation().getWorld().getName(),
                placedBlock.getLocation().getX(), placedBlock.getLocation().getZ(), connection, "name").isEmpty()){
            int claimId = claimspace.getClaimId(placedBlock.getLocation().getWorld().getName(),
                    placedBlock.getLocation().getX(), placedBlock.getLocation().getZ(), connection, player.getUniqueId().toString());
            if (claimId == -1){
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&4>&cThis ground does not belong to you!"));
            }else{
                int playerId = claimspace.getPlayerId(player.getUniqueId().toString(), connection);
                playerIdListAccess.clear();
                playerIdListTrust.clear();
                playerIdListTrust = claimspace.getAllTrustedParties(playerIdListTrust, claimId, 0, connection);
                playerIdListAccess = claimspace.getAllTrustedParties(playerIdListAccess, claimId, 0, connection);
                cancellationStatus = true;
                for (int i = 0; i < playerIdListAccess.size(); i++){
                    if (playerId == playerIdListAccess.get(i)){
                        if (placedBlock.getType().equals(Material.WHEAT_SEEDS) ||
                                placedBlock.getType().equals(Material.PUMPKIN_SEEDS) ||
                                placedBlock.getType().equals(Material.MELON_SEEDS) ||
                                placedBlock.getType().equals(Material.BEETROOT_SEEDS) ||
                                placedBlock.getType().equals(Material.MELON_STEM) ||
                                placedBlock.getType().equals(Material.MELON) ||
                                placedBlock.getType().equals(Material.PUMPKIN_STEM) ||
                                placedBlock.getType().equals(Material.PUMPKIN) ||
                                placedBlock.getType().equals(Material.BEETROOT) ||
                                placedBlock.getType().equals(Material.SUGAR_CANE) ||
                                placedBlock.getType().equals(Material.WHEAT) ||
                                placedBlock.getType().equals(Material.CARROT) ||
                                placedBlock.getType().equals(Material.POTATO)) {
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
                        if (placedBlock.getType().equals(Material.SPRUCE_SIGN) ||
                                placedBlock.getType().equals(Material.SPRUCE_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.ACACIA_SIGN) ||
                                placedBlock.getType().equals(Material.ACACIA_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.BIRCH_SIGN) ||
                                placedBlock.getType().equals(Material.BIRCH_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.CRIMSON_SIGN) ||
                                placedBlock.getType().equals(Material.CRIMSON_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.DARK_OAK_SIGN) ||
                                placedBlock.getType().equals(Material.DARK_OAK_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.JUNGLE_SIGN) ||
                                placedBlock.getType().equals(Material.JUNGLE_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.OAK_SIGN) ||
                                placedBlock.getType().equals(Material.OAK_WALL_SIGN) ||
                                placedBlock.getType().equals(Material.WARPED_SIGN) ||
                                placedBlock.getType().equals(Material.WARPED_WALL_SIGN)) {
                            cancellationStatus = false;
                        }
                    }
                }
            }
            if (cancellationStatus) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&cYou cannot build in a claimed area!"));
            }
        }
    }
}
