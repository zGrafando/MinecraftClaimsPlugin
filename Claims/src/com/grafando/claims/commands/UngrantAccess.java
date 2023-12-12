package com.grafando.claims.commands;

import com.grafando.claims.data.Claimspace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.channels.OverlappingFileLockException;
import java.sql.Connection;

public class UngrantAccess implements CommandExecutor {

    private Player player;
    private Claimspace claimspace = new Claimspace();
    private Connection connection;
    private OfflinePlayer offlinePlayer;
    private boolean status;

    public UngrantAccess(Connection connectionInstance){
        this.connection = connectionInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player){
            if (claimspace.isInClaim(player.getLocation().getWorld().getName(), player.getLocation().getX(), player.getLocation().getZ(),
                    connection, "uuid").equalsIgnoreCase(player.getUniqueId().toString())) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player trustedPlayer = Bukkit.getPlayer(args[0]);
                        int claimId = claimspace.getClaimId(player.getLocation().getWorld().toString(),
                                player.getLocation().getX(), player.getLocation().getZ(), connection, player.getUniqueId().toString());
                        if (claimId == -1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes
                                    ('&', "&4>&cThis ground does not belong to you!"));
                        } else {
                            int playerId = claimspace.getPlayerId(trustedPlayer.getUniqueId().toString(), connection);
                            if (claimspace.doesGrantAccessCrossExist(claimId, playerId, connection)) {
                                if (claimspace.deleteGrantAccess(claimId, playerId, connection)) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes
                                            ('&', "&b>&6You have removed access from " + trustedPlayer.getName() + "!"));
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes
                                            ('&', "&4>&eSomething went wrong; Access grant was not deleted"));
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes
                                        ('&', "&4>&eCannot ungrant access from this player -> grant-access does not exist"));
                            }
                        }
                    } else {
                        if (args[0] == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&6Extend this command by player name"));
                        } else {
                            String PlayerAttempt = args[0];
                            if (PlayerAttempt != null || !PlayerAttempt.isEmpty()) {
                                OfflinePlayer[] offlinePlayerList = Bukkit.getOfflinePlayers();
                                for (int i = 0; i < offlinePlayerList.length; i++) {
                                    if (offlinePlayerList[i].getName().equals(PlayerAttempt)) {
                                        offlinePlayer = offlinePlayerList[i];
                                        if (Claimspace.checkPlayerBalanceExistance(offlinePlayer.getUniqueId().toString(), connection)) {
                                            int claimId = claimspace.getClaimId(player.getLocation().getWorld().getName(),
                                                    player.getLocation().getX(), player.getLocation().getZ(), connection, player.getUniqueId().toString());
                                            if (claimId == -1) {
                                                player.sendMessage(ChatColor.translateAlternateColorCodes
                                                        ('&', "&4>&cThis ground does not belong to you!"));
                                            } else {
                                                int playerId = claimspace.getPlayerId(offlinePlayer.getUniqueId().toString(), connection);
                                                if (claimspace.doesGrantAccessCrossExist(claimId, playerId, connection)) {
                                                    if (claimspace.deleteGrantAccess(claimId, playerId, connection)) {
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                                                ('&', "&b>&6You have removed access from " + offlinePlayer.getName() + "!"));
                                                    } else {
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                                                ('&', "&4>&eSomething went wrong; Access grant was not deleted"));
                                                    }
                                                } else {
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes
                                                            ('&', "&4>&eCannot ungrant access from this player -> grant-access does not exist"));
                                                }
                                                status = true;
                                            }
                                        }
                                    }
                                }
                                if (!status) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            "&6This Player seems not to exist"));
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&6No Value found in argument"));
                            }
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&c>&4You must pass a player as command argument"));
                }
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&c>&4This ground does not belong to you"));
            }
        }
        return true;
    }
}
