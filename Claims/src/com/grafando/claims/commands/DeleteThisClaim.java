package com.grafando.claims.commands;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Objects;

public class DeleteThisClaim implements  CommandExecutor{

    Player player;
    Claimspace claimspace = new Claimspace();
    int claimblockTotal;
    Connection connection;

    public DeleteThisClaim(Connection ConnectionInstance){
        this.claimblockTotal = 0;
        this.connection = ConnectionInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player){
            if (!claimspace.isInClaim(Objects.requireNonNull(player.getLocation().getWorld()).getName(),
                    player.getLocation().getX(), player.getLocation().getZ(), connection, "name").isEmpty()){
                if (claimspace.isInClaim(Objects.requireNonNull(player.getLocation().getWorld()).getName(),
                        player.getLocation().getX(), player.getLocation().getZ(), connection, "name").equalsIgnoreCase(player.getName())){
                    int value = claimspace.calculateClaimblocksReturned(player.getLocation().getWorld().getName(),
                            player.getLocation().getX(), player.getLocation().getZ(), player, connection);
                    claimspace.increaseClaimblocks(player, value, connection);
                    if (claimspace.resetSpecificClaim(player, player.getLocation().getWorld().getName(),
                            player.getLocation().getX(), player.getLocation().getZ(), connection)){
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&5>&6Your Claim was deleted; &b&l"+value+" &6claimblocks were restored to your account ->" +
                                        " you now have &b&l"+claimspace.getClaimblocks(player, connection)+" &6claimblocks!"));
                    }else{
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&c>&4Something went wrong; claim was not deleted"));
                    }
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&4>&cThis is not your claim to delete"));
                }
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&4>&eYou are not standing on a claim"));
            }

        }
        return true;
    }
}
