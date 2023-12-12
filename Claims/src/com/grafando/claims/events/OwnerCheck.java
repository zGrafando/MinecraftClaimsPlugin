package com.grafando.claims.events;

import com.grafando.claims.Claims;
import com.grafando.claims.data.Claimspace;
import com.grafando.claims.utils.DisplayBlocks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.Connection;

public class OwnerCheck implements Listener {

    private Claimspace claimspace = new Claimspace();
    private Claims plugin;
    private Block clickedBlock;
    private Player player;
    private Connection connection;
    private DisplayBlocks displayBlocks = new DisplayBlocks();

    public OwnerCheck(Claims instance, Connection connectionInstance){
        this.connection = connectionInstance;
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ClickBlock){
        clickedBlock = ClickBlock.getClickedBlock();
        player = ClickBlock.getPlayer();
        if (ClickBlock.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(player.getInventory().getItemInMainHand().getType().equals(Material.STICK)){
                String OwnerName = claimspace.isInClaim(clickedBlock.getLocation().getWorld().getName(),
                        clickedBlock.getLocation().getX(), clickedBlock.getLocation().getZ(), connection, "name");
                String Claimowner = claimspace.isInClaim(clickedBlock.getLocation().getWorld().getName(),
                        clickedBlock.getLocation().getX(), clickedBlock.getLocation().getZ(), connection, "ClaimownerUUID");
                String Renter = claimspace.isInClaim(clickedBlock.getLocation().getWorld().getName(), clickedBlock.getX(), clickedBlock.getZ(), connection, "rentedUUID");
                if (Renter.isEmpty()) {
                    if (!Claimowner.isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&5>&8This claim is owned by &6&l" + OwnerName));
                        int claimId = claimspace.getClaimId(clickedBlock.getWorld().getName(), clickedBlock.getX(), clickedBlock.getZ(), connection, Claimowner);
                        Block prevBlock = clickedBlock.getWorld().getHighestBlockAt((int) claimspace.getX1FromClaims(claimId, connection),
                                (int) claimspace.getZ1FromClaims(claimId, connection), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                        Block currentBlock = clickedBlock.getWorld().getHighestBlockAt((int) claimspace.getX2FromClaims(claimId, connection),
                                (int) claimspace.getZ2FromClaims(claimId, connection), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                        displayBlocks.displayBlocks(prevBlock, currentBlock, this.plugin);
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&4>&8This claim is owned by &7&lno one"));
                    }
                }else{
                    String renter = claimspace.getPlayerByUUID(Renter, connection);
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&5>&8This claim is rented by &6&l" + renter));
                    int claimId = claimspace.getClaimId(clickedBlock.getWorld().getName(), clickedBlock.getX(), clickedBlock.getZ(), connection, Claimowner);
                    Block prevBlock = clickedBlock.getWorld().getHighestBlockAt((int) claimspace.getX1FromClaims(claimId, connection),
                            (int) claimspace.getZ1FromClaims(claimId, connection), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    Block currentBlock = clickedBlock.getWorld().getHighestBlockAt((int) claimspace.getX2FromClaims(claimId, connection),
                            (int) claimspace.getZ2FromClaims(claimId, connection), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    displayBlocks.displayBlocks(prevBlock, currentBlock, this.plugin);
                }
            }
        }
    }
}
