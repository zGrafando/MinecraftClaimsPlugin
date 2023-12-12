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

public class DoorActivation implements Listener {
    private Player player;
    private final Claimspace claimspace = new Claimspace();
    private final Connection conn;
    private ArrayList<Integer> playerIdListTrust = new ArrayList<>();
    private ArrayList<Integer> playerIdListAccessGrant = new ArrayList<>();

    public DoorActivation(Connection connection){
        this.conn = connection;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ClickChest){
        if (ClickChest != null && ClickChest.getClickedBlock() != null) {
            player = ClickChest.getPlayer();
            String renterUUID = claimspace.isInClaim(Objects.requireNonNull(ClickChest.getClickedBlock().getLocation().getWorld()).getName(),
                    ClickChest.getClickedBlock().getLocation().getX(), ClickChest.getClickedBlock().getLocation().getZ(), conn, "rentedUuid");
            if (renterUUID == null){
                renterUUID = "";
            }
            if (renterUUID.isEmpty()) {
                if (ClickChest.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!claimspace.isInClaim(Objects.requireNonNull(ClickChest.getClickedBlock().getLocation().getWorld()).getName(),
                            ClickChest.getClickedBlock().getLocation().getX(), ClickChest.getClickedBlock().getLocation().getZ(), conn, "uuid").equalsIgnoreCase(player.getUniqueId().toString())
                            && !claimspace.isInClaim(ClickChest.getClickedBlock().getLocation().getWorld().getName(), ClickChest.getClickedBlock().getLocation().getX(),
                            ClickChest.getClickedBlock().getLocation().getZ(), conn, "name").isEmpty()) {

                        //Fence Gates

                        if (ClickChest.getClickedBlock().getType().equals(Material.BIRCH_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.ACACIA_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.OAK_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_FENCE_GATE) ||
                                ClickChest.getClickedBlock().getType().equals(Material.WARPED_FENCE_GATE) ||

                                //Trapdoors

                                ClickChest.getClickedBlock().getType().equals(Material.BIRCH_TRAPDOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.ACACIA_TRAPDOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_TRAPDOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_TRAPDOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.OAK_TRAPDOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_TRAPDOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.WARPED_TRAPDOOR) ||

                                //Doors

                                ClickChest.getClickedBlock().getType().equals(Material.BIRCH_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.ACACIA_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.OAK_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_DOOR) ||
                                ClickChest.getClickedBlock().getType().equals(Material.WARPED_DOOR)

                        ) {
                            int claimId = claimspace.getClaimId(ClickChest.getClickedBlock().getLocation().getWorld().getName(),
                                    ClickChest.getClickedBlock().getLocation().getX(), ClickChest.getClickedBlock().getLocation().getZ(), conn, player.getUniqueId().toString());
                            int playerId = claimspace.getPlayerId(player.getUniqueId().toString(), conn);
                            playerIdListTrust.clear();
                            playerIdListAccessGrant.clear();
                            playerIdListTrust = claimspace.getAllGrantedAccessParties(playerIdListTrust, claimId, 0, conn);
                            playerIdListAccessGrant = claimspace.getAllTrustedParties(playerIdListAccessGrant, claimId, 0, conn);
                            boolean cancellationStatus = true;
                            for (Integer integer : playerIdListTrust) {
                                if (playerId == integer) {
                                    cancellationStatus = false;
                                }
                            }
                            for (Integer integer : playerIdListAccessGrant) {
                                if (playerId == integer) {
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
            }else{
                if (ClickChest.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!player.getUniqueId().toString().equalsIgnoreCase(renterUUID)){

                        //Fence Gates

                        if (ClickChest.getClickedBlock().getType().equals(Material.BIRCH_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.ACACIA_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.OAK_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_FENCE_GATE) ||
                        ClickChest.getClickedBlock().getType().equals(Material.WARPED_FENCE_GATE) ||

                        //Trapdoors

                        ClickChest.getClickedBlock().getType().equals(Material.BIRCH_TRAPDOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.ACACIA_TRAPDOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_TRAPDOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_TRAPDOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.OAK_TRAPDOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_TRAPDOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.WARPED_TRAPDOOR) ||

                        //Doors

                        ClickChest.getClickedBlock().getType().equals(Material.BIRCH_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.ACACIA_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.CRIMSON_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.JUNGLE_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.OAK_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.SPRUCE_DOOR) ||
                        ClickChest.getClickedBlock().getType().equals(Material.WARPED_DOOR)

                        ) {
                            ClickChest.setCancelled(true);
                            player.sendMessage(ChatColor.translateAlternateColorCodes
                                    ('&', "&4>&cThis door is locked!"));

                        }
                    }
                }
            }
        }
    }
}
