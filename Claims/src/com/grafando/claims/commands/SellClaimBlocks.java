package com.grafando.claims.commands;

import com.grafando.claims.data.Claimlogs;
import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SellClaimBlocks implements CommandExecutor{

    private Player player;
    private Claimspace claimspace = new Claimspace();
    private Connection connection;
    private Claimlogs claimlogs = new Claimlogs();

    public SellClaimBlocks(Connection connectionInstance){
        this.connection = connectionInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player){
            if (args.length == 1){
                int intendedClaim = Integer.parseInt(args[0]);
                if (claimspace.getClaimblocks(player, connection) >= intendedClaim){
                    double intendedMoney = intendedClaim * claimspace.getClaimblockRatio(connection);
                    if (claimspace.getMoneyAmountRemaining(connection) > intendedMoney){
                        claimspace.updateClaimblocks(player, intendedClaim, connection);
                        claimspace.updateBalance(player, (int) intendedMoney, "plus", connection);
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&a>&6Your balance increased by &2&l"+intendedMoney));
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&a>&6 You now have &2&l"+claimspace.getBalance(player, connection)+"&6 deposited in your account!"));
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        String message = player.getUniqueId().toString()+";"+intendedClaim+";"+intendedMoney+";"+timeStamp+"  |:| -->"
                                +player.getName()+"("+player.getUniqueId().toString()+") has sold "+intendedClaim
                                +" claimblocks for the monetary amount of "+intendedMoney+" at: "+timeStamp;
                        claimlogs.writeClaimFile(message);
                    }else{
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&&4>&eThe inflation is at a maximum; sry :("));
                    }
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&c>&4You do not have enough claimblocks to sell"));
                }
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&&b>&6You have &b&l"+claimspace.getClaimblocks(player, connection)+"&6 claimblocks"));
            }
        }
        return true;
    }
}

