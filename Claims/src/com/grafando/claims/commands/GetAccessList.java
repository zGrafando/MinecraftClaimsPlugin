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
import java.util.ArrayList;

public class GetAccessList implements CommandExecutor{
    private Player player;
    private Claimspace claimspace = new Claimspace();
    private Connection connection;
    private ArrayList<Integer> TrustIdList = new ArrayList<>();

    public GetAccessList(Connection connectionInstance){
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
                TrustIdList = claimspace.getAllGrantedAccessParties(TrustIdList, claimId, 0, connection);
                if (TrustIdList.isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&4>&7&lNo one&f has access to this claim"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&b&lPlayers with access:"));
                    for (int i = 0; i < TrustIdList.size(); i++) {
                        String PlayerUUID = claimspace.getPlayerById(TrustIdList.get(i), connection);
                        player.sendMessage(ChatColor.translateAlternateColorCodes
                                ('&', "&b>&f -&7 " + PlayerUUID));
                    }
                }
            }
        }
        return true;
    }
}
