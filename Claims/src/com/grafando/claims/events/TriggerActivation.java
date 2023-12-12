package com.grafando.claims.events;
import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class TriggerActivation implements Listener {
    private Player player;
    private final Claimspace claimspace = new Claimspace();
    private final Connection conn;
    private ArrayList<Integer> playerIdListTrust = new ArrayList<>();
    private ArrayList<Integer> playerIdListAccessGrant = new ArrayList<>();

    public TriggerActivation(Connection connection){
        this.conn = connection;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ClickChest){
        if (ClickChest != null && ClickChest.getClickedBlock() != null) {
            player = ClickChest.getPlayer();
            String renterUUID = claimspace.isInClaim(Objects.requireNonNull(ClickChest.getClickedBlock().getLocation().getWorld()).getName(),
                    ClickChest.getClickedBlock().getLocation().getX(), ClickChest.getClickedBlock().getLocation().getZ(), conn, "rentedUUid");
            if (renterUUID == null){
                renterUUID = "";
            }
            if (renterUUID.isEmpty()) {
                if (ClickChest.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (ClickChest.getClickedBlock().getType().equals(Material.BIRCH_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.ACACIA_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.OAK_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.POLISHED_BLACKSTONE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.STONE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.WARPED_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.LEVER)) {
                        if (!claimspace.isInClaim(Objects.requireNonNull(ClickChest.getClickedBlock().getLocation().getWorld()).getName(),
                                ClickChest.getClickedBlock().getLocation().getX(), ClickChest.getClickedBlock().getLocation().getZ(), conn, "uuid").equalsIgnoreCase(player.getUniqueId().toString())
                                && !claimspace.isInClaim(ClickChest.getClickedBlock().getLocation().getWorld().getName(), ClickChest.getClickedBlock().getLocation().getX(),
                                ClickChest.getClickedBlock().getLocation().getZ(), conn, "name").isEmpty()) {
                            int claimId = claimspace.getClaimId(ClickChest.getClickedBlock().getLocation().getWorld().getName(),
                                    ClickChest.getClickedBlock().getLocation().getX(), ClickChest.getClickedBlock().getLocation().getZ(), conn, player.getUniqueId().toString());
                            if (claimId == -1) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes
                                        ('&', "&4>&cThis ground does not belong to you!"));
                            } else {
                                int playerId = claimspace.getPlayerId(player.getUniqueId().toString(), conn);
                                playerIdListTrust.clear();
                                playerIdListAccessGrant.clear();
                                playerIdListTrust = claimspace.getAllGrantedAccessParties(playerIdListTrust, claimId, 0, conn);
                                playerIdListAccessGrant = claimspace.getAllTrustedParties(playerIdListAccessGrant, claimId, 0, conn);
                                boolean cancellationStatus = true;
                                for (int i = 0; i < playerIdListTrust.size(); i++) {
                                    if (playerId == playerIdListTrust.get(i)) {
                                        cancellationStatus = false;
                                    }
                                }
                                for (int i = 0; i < playerIdListAccessGrant.size(); i++) {
                                    if (playerId == playerIdListAccessGrant.get(i)) {
                                        cancellationStatus = false;
                                    }
                                }
                                if (cancellationStatus) {
                                    ClickChest.setCancelled(true);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes
                                            ('&', "&4>&cThis trigger is locked!"));
                                }
                            }
                        }
                    }
                }
            }else{
                if (ClickChest.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!player.getUniqueId().toString().equalsIgnoreCase(renterUUID)) {
                        if (ClickChest.getClickedBlock().getType().equals(Material.BIRCH_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.ACACIA_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.OAK_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.POLISHED_BLACKSTONE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.STONE_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.WARPED_BUTTON) ||
                            ClickChest.getClickedBlock().getType().equals(Material.LEVER)) {
                                ClickChest.setCancelled(true);
                                player.sendMessage(ChatColor.translateAlternateColorCodes
                                        ('&', "&4>&cThis trigger is locked!"));
                        }
                    }
                }
            }
        }
    }
}
