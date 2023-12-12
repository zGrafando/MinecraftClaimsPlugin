package com.grafando.claims;

import com.grafando.claims.commands.*;
import com.grafando.claims.data.Claimlogs;
import com.grafando.claims.data.Claimspace;
import com.grafando.claims.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class Claims extends JavaPlugin {

    private Claimspace claimspace = new Claimspace();
    private final Connection connectionActivity = claimspace.getConnection();
    private final Connection connectionDeleteAll = claimspace.getConnection();
    private final Connection regularQueries = claimspace.getConnection();
    private final Connection claimProtection = claimspace.getConnection();
    private Claimlogs claimlogs = new Claimlogs();

    @Override
    public void onEnable() {
        super.onEnable();
        Objects.requireNonNull(this.getCommand("getclaimblocks")).setExecutor(new GetClaimBlocks(regularQueries));
        Objects.requireNonNull(this.getCommand("sellclaimblocks")).setExecutor(new SellClaimBlocks(regularQueries));
        Objects.requireNonNull(this.getCommand("deletethisclaim")).setExecutor(new DeleteThisClaim(connectionDeleteAll));
        Objects.requireNonNull(this.getCommand("deleteallclaims")).setExecutor(new DeleteAllClaims(connectionDeleteAll));
        Objects.requireNonNull(this.getCommand("resetclaiminstance")).setExecutor(new ResetClaimInstance(regularQueries));
        Objects.requireNonNull(this.getCommand("getaccesslist")).setExecutor(new GetAccessList(claimProtection));
        Objects.requireNonNull(this.getCommand("gettrustlist")).setExecutor(new GetTrustList(claimProtection));
        Objects.requireNonNull(this.getCommand("trust")).setExecutor(new Trust(claimProtection));
        Objects.requireNonNull(this.getCommand("revoketrust")).setExecutor(new Untrust(claimProtection));
        Objects.requireNonNull(this.getCommand("grantaccess")).setExecutor(new GrantAccess(claimProtection));
        Objects.requireNonNull(this.getCommand("revokeaccess")).setExecutor(new UngrantAccess(claimProtection));
        getServer().getPluginManager().registerEvents(new ClaimCreation(this, regularQueries), this);
        getServer().getPluginManager().registerEvents(new OwnerCheck(this, regularQueries), this);
        getServer().getPluginManager().registerEvents(new PlayerActivity(connectionActivity), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakOnClaim(claimProtection), this);
        getServer().getPluginManager().registerEvents(new PlayerBuildOnClaim(claimProtection), this);
        getServer().getPluginManager().registerEvents(new TriggerActivation(claimProtection), this);
        getServer().getPluginManager().registerEvents(new BlockExplosion(claimProtection), this);
        getServer().getPluginManager().registerEvents(new LightningStrike(claimProtection), this);
        getServer().getPluginManager().registerEvents(new Burn(claimProtection), this);
        getServer().getPluginManager().registerEvents(new AnimalHarm(claimProtection), this);
        getServer().getPluginManager().registerEvents(new PlayerAnimalHarm(claimProtection), this);
        getServer().getPluginManager().registerEvents(new DoorActivation(claimProtection), this);
        claimlogs.createDirectory();
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Claims]:"+ChatColor.AQUA+" Plugin is enabled!");
        BukkitRunnable afkCheckerAndClaimblockEmitter = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player tempPlayer : Bukkit.getServer().getOnlinePlayers()) {
                    claimspace.increaseClaimblocks(tempPlayer, 4, connectionDeleteAll);
                    LocalDateTime now = LocalDateTime.now();
                    if (now.minusMinutes(5).isAfter(claimspace.getLastPlayerMovementTimestamp(tempPlayer, connectionActivity))) {
                        claimspace.increaseActivityIndicator(tempPlayer, 1, connectionActivity);
                        if (claimspace.getcurrentActivityIndicator(tempPlayer, connectionActivity) >= 3) {
                            tempPlayer.kickPlayer("Your Activity Indicators deemed you afk and have instructed Agent Smith to disconnect you");
                        } else if (claimspace.getcurrentActivityIndicator(tempPlayer, connectionActivity) >= 1) {
                            tempPlayer.setDisplayName(tempPlayer.getDisplayName().concat(ChatColor.translateAlternateColorCodes
                                    ('&', "&8&l[AFK]")));
                        }
                    }
                }
            }
        };
        afkCheckerAndClaimblockEmitter.runTaskTimer(this, 6000, 6000);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            connectionActivity.close();
            connectionDeleteAll.close();
            regularQueries.close();
            claimProtection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Claims]:"+ChatColor.AQUA+" Plugin is disabled!");
    }

    public void runDelayedTask(ArrayList<Block> blockList, ArrayList<Material> materialList){
        BukkitRunnable BlockTypeReturner = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < blockList.size(); i++) {
                    blockList.get(i).getLocation().getBlock().setType(materialList.get(i));
                }
            }
        };
        BlockTypeReturner.runTaskLater(this, 60);
    }
}

