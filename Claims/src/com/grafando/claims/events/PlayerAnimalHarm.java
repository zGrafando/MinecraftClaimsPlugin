package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.sql.Connection;
import java.util.ArrayList;

public class PlayerAnimalHarm implements Listener {
    Claimspace claimspace = new Claimspace();
    Connection connection;
    private boolean cancellationStatus;
    public PlayerAnimalHarm(Connection conn) {
        this.connection = conn;
    }
    private ArrayList<Integer> playerIdListTrust = new ArrayList<>();
    private ArrayList<Integer> playerIdListAccessGrant = new ArrayList<>();


    @EventHandler
    public void onPlayerAttackAnimal(EntityDamageByEntityEvent PlayerHarmAnimal) {
        cancellationStatus = false;
        if (!claimspace.isInClaim(PlayerHarmAnimal.getEntity().getLocation().getWorld().getName(),
                PlayerHarmAnimal.getEntity().getLocation().getX(), PlayerHarmAnimal.getEntity().getLocation().getZ(),
                connection, "name").isEmpty()) {
            if (PlayerHarmAnimal.getDamager().getType().equals(EntityType.PLAYER)) {
                Player player = (Player) PlayerHarmAnimal.getDamager();
                if (!claimspace.isInClaim(PlayerHarmAnimal.getEntity().getLocation().getWorld().getName(),
                        PlayerHarmAnimal.getEntity().getLocation().getX(), PlayerHarmAnimal.getEntity().getLocation().getZ(),
                        connection, "uuid").equals(PlayerHarmAnimal.getDamager().getUniqueId().toString())) {
                    if (PlayerHarmAnimal.getEntity().getType().equals(EntityType.ARMOR_STAND)||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.CAT) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.COW) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.PIG) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.SHEEP) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.CHICKEN) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.DONKEY) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.DOLPHIN) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.HORSE) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.ITEM_FRAME) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.LLAMA) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.PANDA) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.PARROT) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.OCELOT) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.MUSHROOM_COW) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.POLAR_BEAR) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.RABBIT) ||
                        PlayerHarmAnimal.getEntity().getType().equals(EntityType.VILLAGER)){
                            cancellationStatus = true;
                            int claimId = claimspace.getClaimId(PlayerHarmAnimal.getEntity().getLocation().getWorld().getName(),
                                    PlayerHarmAnimal.getEntity().getLocation().getX(), PlayerHarmAnimal.getEntity().getLocation().getZ(), connection, player.getUniqueId().toString());
                            int playerId = claimspace.getPlayerId(player.getUniqueId().toString(), connection);
                            playerIdListTrust.clear();
                            playerIdListAccessGrant.clear();
                            playerIdListTrust = claimspace.getAllGrantedAccessParties(playerIdListTrust, claimId, 0, connection);
                            playerIdListAccessGrant = claimspace.getAllTrustedParties(playerIdListAccessGrant, claimId, 0, connection);
                            boolean cancellationStatus = true;
                            for (Integer integer : playerIdListTrust) {
                                if (playerId == integer) {
                                    cancellationStatus = false;
                                }
                            }
                            for (Integer integer : playerIdListAccessGrant) {
                                if (playerId == integer) {
                                    cancellationStatus = false;
                                }
                            }
                            if (cancellationStatus) {
                                PlayerHarmAnimal.setCancelled(true);
                                PlayerHarmAnimal.getEntity().sendMessage(ChatColor.translateAlternateColorCodes
                                        ('&', "&4>&cYou cannot kill animals that do not belong to you!"));
                            }
                    }
                }
            }else{
                if (PlayerHarmAnimal.getDamager().getType().equals(EntityType.CREEPER) ||
                        PlayerHarmAnimal.getDamager().getType().equals(EntityType.BLAZE) ||
                        PlayerHarmAnimal.getDamager().getType().equals(EntityType.WOLF) ||
                        PlayerHarmAnimal.getDamager().getType().equals(EntityType.FIREWORK) ||
                        PlayerHarmAnimal.getDamager().getType().equals(EntityType.FIREBALL)){
                    cancellationStatus = true;
                }
            }
        }
        if (cancellationStatus) {
            PlayerHarmAnimal.setCancelled(true);
            PlayerHarmAnimal.getEntity().sendMessage(ChatColor.translateAlternateColorCodes
                    ('&', "&4>&cYou cannot kill animals that do not belong to you!"));
        }
    }
}
