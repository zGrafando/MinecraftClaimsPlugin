package com.grafando.claims.commands;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;

public class ResetClaimInstance implements CommandExecutor{

    Player player;
    Claimspace claimspace = new Claimspace();
    ResultSet rs;
    Connection connection;

    public ResetClaimInstance(Connection connectionInstance){
        this.connection = connectionInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player){
            if (claimspace.isClaimographySet(player, connection)){
                claimspace.deleteClaimography(player, connection);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&c>&4There are no Claimography records to reset"));
            }

        }
        return true;
    }
}
