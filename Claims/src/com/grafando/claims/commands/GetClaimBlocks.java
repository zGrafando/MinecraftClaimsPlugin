package com.grafando.claims.commands;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class GetClaimBlocks implements CommandExecutor {

    Player player;
    Claimspace claimspace = new Claimspace();
    Connection connection;

    public GetClaimBlocks(Connection connectionInstance){
        this.connection = connectionInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player) {
            if (args.length == 1) {
                int intendedClaim = Integer.parseInt(args[0]);
                double intendedMoney = intendedClaim * (claimspace.getClaimblockRatio(connection) + 0.5);
                if (claimspace.getBalance(player, connection) > intendedMoney) {
                    claimspace.updateBalance(player, (int) intendedMoney, "minus", connection);
                    if (claimspace.increaseClaimblocks(player, intendedClaim, connection)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&b>&6Your claimblocks have been increased by &b&l" + intendedClaim));
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&b>&6You now have &b&l" + claimspace.getClaimblocks(player, connection) + "&6 claimblocks!"));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&4>&eSomething went wrong; no claimblocks were given"));
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&c>&4You do not have sufficent funds to buy the claimblocks you wish"));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&b>&6You have &b&l" + claimspace.getClaimblocks(player, connection) + "&6 claimblocks"));
            }
        }
        return true;
    }
}
