package com.grafando.claims.events;

import com.grafando.claims.Claims;
import com.grafando.claims.data.Claimspace;
import com.grafando.claims.utils.DisplayBlocks;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class ClaimCreation implements Listener{

    private Claimspace claimspace = new Claimspace();
    private Claims plugin;
    private Block clickedBlock;
    private Block prevBlock;
    private Player player;
    private final ArrayList<Block> blockList = new ArrayList<>();
    private final ArrayList<Material> materialList = new ArrayList<>();
    private DisplayBlocks displayBlocks = new DisplayBlocks();
    private Connection connection;

    public ClaimCreation(Claims instance, Connection connectionInstance){
        this.plugin = instance;
        this.connection = connectionInstance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ClickBlock) {
        clickedBlock = ClickBlock.getClickedBlock();
        player = ClickBlock.getPlayer();
        if (ClickBlock.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_AXE)) {
                String claimOwner = claimspace.isInClaim(Objects.requireNonNull(clickedBlock.getLocation().getWorld()).getName(),
                        clickedBlock.getLocation().getX(), clickedBlock.getLocation().getZ(), connection, "name");
                if (claimOwner.isEmpty()) {
                    if (claimspace.isClaimographySet(player, connection)) {
                        if (getWorldFromString(claimspace.getWorldFromClaimography(player.getUniqueId().toString(), connection)).getName().equals(
                                Objects.requireNonNull(clickedBlock.getLocation().getWorld()).getName())) {
                            prevBlock = clickedBlock.getWorld().getBlockAt((int) claimspace.getXFromClaimography(player.getUniqueId().toString(), connection),
                                    (int) claimspace.getYFromClaimography(player.getUniqueId().toString(), connection), (int) claimspace.getZFromClaimography(player.getUniqueId().toString(), connection));
                            int calculatedArea = calculateArea(prevBlock, clickedBlock);
                            player.sendMessage(ChatColor.translateAlternateColorCodes
                                    ('&', "&eClaimblocks calculated: &b&n" + calculatedArea));
                            if (calculatedArea >= 64) {
                                if (claimspace.updateClaimblocks(player, calculatedArea, connection)) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes
                                            ('&', "&2Claimblocks subtracted"));
                                    boolean insertionStatus = claimspace.insertClaim(Objects.requireNonNull(prevBlock.getLocation().getWorld()).getName(), prevBlock.getLocation().getX(),
                                            prevBlock.getLocation().getY(), prevBlock.getLocation().getZ(), clickedBlock.getLocation().getWorld().getName(),
                                            clickedBlock.getLocation().getX(), clickedBlock.getLocation().getY(), clickedBlock.getLocation().getZ(),
                                            "", player.getName(), player, connection);
                                    if (insertionStatus) {
                                        displayBlocks.displayBlocks(prevBlock, clickedBlock, this.plugin);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                                ('&', "&6&l> Claim created!"));
                                        claimspace.deleteClaimography(player, connection);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                                ('&', "&6&l> Claim Instance reset, you now have "+claimspace.getClaimblocks(player, connection)+" claim blocks left"));
                                    } else {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                                ('&', "&4 Something went wrong; claim was not created"));
                                    }
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes
                                            ('&', "&4 You do not have enough claim blocks to claim this area"));
                                }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes
                                    ('&', "&4 The input Claim-corners appear to be too close together; the claim must be at least 8x8"));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&4 Invalid World!! -> Contact Admin immediately"));
                    }
                } else {

                        if (!claimspace.isClaimographySet(player, connection)) {
                            claimspace.registerClaimography(Objects.requireNonNull(clickedBlock.getLocation().getWorld()).getName(),
                                    clickedBlock.getLocation().getX(), clickedBlock.getLocation().getY(), clickedBlock.getLocation().getZ(), player.getUniqueId().toString(), connection);
                            Material tempmat = clickedBlock.getLocation().getBlock().getType();
                            if (clickedBlock.getBiome().equals(Biome.SNOWY_BEACH) || clickedBlock.getBiome().equals(Biome.SNOWY_MOUNTAINS) ||
                                    clickedBlock.getBiome().equals(Biome.SNOWY_TAIGA) || clickedBlock.getBiome().equals(Biome.SNOWY_TAIGA_HILLS) ||
                                    clickedBlock.getBiome().equals(Biome.SNOWY_TAIGA_MOUNTAINS) || clickedBlock.getBiome().equals(Biome.SNOWY_TUNDRA) ||
                                    clickedBlock.getBiome().equals(Biome.ICE_SPIKES)) {
                                clickedBlock.getLocation().getBlock().setType(Material.COAL_BLOCK);
                            } else {
                                clickedBlock.getLocation().getBlock().setType(Material.SNOW_BLOCK);
                            }
                            blockList.add(clickedBlock);
                            materialList.add(tempmat);
                            plugin.runDelayedTask(blockList, materialList);
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes
                                    ('&', "&c>&4An instance is already set somewhere else"));
                        }
                }
                }else{

                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&4 Claim overlaps with an existing one"));
            }

            }
        }
    }

    public int calculateArea(Block block1, Block block2){
        int differenceOnXAxis;
        int differenceOnZAxis;
        if (block1.getX() > block2.getX()){
            differenceOnXAxis = block1.getX() - block2.getX();
        }else{
            differenceOnXAxis = block2.getX() - block1.getX();
        }
        if (block1.getZ() > block2.getZ()){
            differenceOnZAxis = block1.getZ() - block2.getZ();
        }else{
            differenceOnZAxis = block2.getZ() - block1.getZ();
        }
        return differenceOnXAxis * differenceOnZAxis;
    }

    public World getWorldFromString(String worldName){
        WorldCreator creator = new WorldCreator("world");
        World w = creator.createWorld();
        for (World w1 : Bukkit.getServer().getWorlds()) {
            if (worldName.trim().equalsIgnoreCase(w1.getName())) {
                w = w1;
            }
        }
        return w;
    }
}
