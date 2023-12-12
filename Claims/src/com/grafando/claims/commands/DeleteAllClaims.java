package com.grafando.claims.commands;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class DeleteAllClaims implements CommandExecutor {

    private Player player;
    private Claimspace claimspace = new Claimspace();
    private int claimblockTotal;
    private int failedDeletions;
    private int CheckerId;
    private Connection connection;

    public DeleteAllClaims(Connection connection){
        this.claimblockTotal = 0;
        this.failedDeletions = 0;
        this.connection = connection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        player = (Player) sender;
        if (sender instanceof Player) {
            if (claimspace.doesClaimExist(player, connection) > 0) {
                CheckerId = claimspace.doesClaimExist(player, connection);
                while (0 < CheckerId) {
                    int tempId = claimspace.doesClaimExist(player, connection);
                    int tempX1Value = (int)claimspace.getX1FromClaims(tempId, connection);
                    int tempX2Value = (int)claimspace.getX2FromClaims(tempId, connection);
                    int tempZ1Value = (int)claimspace.getZ1FromClaims(tempId, connection);
                    int tempZ2Value = (int)claimspace.getZ2FromClaims(tempId, connection);
                    if(claimspace.deleteSpecificClaimById(tempId, connection)){
                        int tempSumm;
                        if (tempX1Value > tempX2Value) {
                            int diffInX = tempX1Value - tempX2Value;
                            int diffInZ;
                            if (tempZ1Value > tempZ2Value) {
                                diffInZ = tempZ1Value - tempZ2Value;
                            }else{
                                diffInZ = tempZ2Value - tempZ1Value;
                            }
                            tempSumm = (int) ((diffInX * diffInZ) * 0.95);

                        }else{
                            int diffInX = tempX2Value - tempX1Value;
                            int diffInZ;
                            if (tempZ1Value > tempZ2Value) {
                                diffInZ = tempZ1Value - tempZ2Value;
                            }else{
                                diffInZ = tempZ2Value - tempZ1Value;
                            }
                            tempSumm = (int) ((diffInX * diffInZ) * 0.95);
                        }
                        if (claimspace.increaseClaimblocks(player, tempSumm, connection)) {
                            this.setClaimblockTotal(tempSumm);
                        }
                    }else{
                        this.setFailedDeletions(1);
                    }
                    CheckerId = claimspace.doesClaimExist(player, connection);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&5>&6Your Claims were deleted; &b&l" + claimblockTotal + " &6claimblocks were restored to your account ->" +
                                " &e&lyou &6now have &b&l" + claimspace.getClaimblocks(player, connection) + "&6 claimblocks!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&5>&3" + failedDeletions + " &6Deletions have failed;"));
            }else {
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&c>&4Found no claims to delete"));
            }
        }
        return true;
    }

    public void setClaimblockTotal(int claimblockTotal){
        this.claimblockTotal = this.claimblockTotal + claimblockTotal;
    }

    public void setFailedDeletions(int failedDeletions){
        this.failedDeletions = this.failedDeletions + failedDeletions;
    }
}

