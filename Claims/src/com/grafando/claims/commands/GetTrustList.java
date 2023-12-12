package com.grafando.claims.commands;

import com.grafando.claims.data.Claimlogs;
import com.grafando.claims.data.Claimspace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetTrustList implements CommandExecutor {

    private Player player;
    private Claimspace claimspace = new Claimspace();
    private Connection connection;
    private Claimlogs claimlogs = new Claimlogs();
    private ArrayList<Integer> TrustIdList = new ArrayList<>();

    public GetTrustList(Connection connectionInstance){
        this.connection = connectionInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player){
            int claimId = claimspace.getClaimId(player.getLocation().getWorld().getName(),
                    player.getLocation().getX(), player.getLocation().getZ(), connection, player.getUniqueId().toString());
            if (claimId == -1){
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&4>&cThis ground does not belong to you!"));
            }else {
                TrustIdList.clear();
                TrustIdList = claimspace.getAllTrustedParties(TrustIdList, claimId, 0, connection);
                if (TrustIdList.isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&4>&7&lNo one &fis tusted to this claim"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&b&lPlayers trusted:"));
                    for (int i = 0; i < TrustIdList.size(); i++) {
                        String PlayerName = claimspace.getPlayerById(TrustIdList.get(i), connection);
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&b>&f -&7 " + PlayerName));
                    }
                }
            }
        }
        return true;
    }
}
