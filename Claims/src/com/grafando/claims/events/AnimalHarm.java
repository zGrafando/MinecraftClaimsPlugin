package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.sql.Connection;

public class AnimalHarm implements Listener {
    Claimspace claimspace = new Claimspace();
    Connection connection;
    private boolean cancellationStatus;
    public AnimalHarm(Connection conn) {
        this.connection = conn;
    }


    @EventHandler
    public void onAnimalHarm(EntityDamageEvent AnimalHarm) {
        cancellationStatus = false;
        if (AnimalHarm.getEntityType().equals(EntityType.COW) ||
            AnimalHarm.getEntityType().equals(EntityType.POLAR_BEAR) ||
            AnimalHarm.getEntityType().equals(EntityType.VILLAGER) ||
            AnimalHarm.getEntityType().equals(EntityType.RABBIT) ||
            AnimalHarm.getEntityType().equals(EntityType.MUSHROOM_COW) ||
            AnimalHarm.getEntityType().equals(EntityType.PARROT) ||
            AnimalHarm.getEntityType().equals(EntityType.LLAMA) ||
            AnimalHarm.getEntityType().equals(EntityType.CHICKEN) ||
            AnimalHarm.getEntityType().equals(EntityType.SHEEP) ||
            AnimalHarm.getEntityType().equals(EntityType.PIG) ||
            AnimalHarm.getEntityType().equals(EntityType.HORSE) ||
            AnimalHarm.getEntityType().equals(EntityType.SNOWMAN) ||
            AnimalHarm.getEntityType().equals(EntityType.CAT) ||
            AnimalHarm.getEntityType().equals(EntityType.WOLF)) {
            if (!claimspace.isInClaim(AnimalHarm.getEntity().getLocation().getWorld().getName(),
                    AnimalHarm.getEntity().getLocation().getX(), AnimalHarm.getEntity().getLocation().getZ(),
                    connection, "name").isEmpty()) {
                if (AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.DRYOUT) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.FALL) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.FIRE) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.LAVA) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.MAGIC) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.POISON) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) ||
                        AnimalHarm.getCause().equals(EntityDamageEvent.DamageCause.THORNS)) {
                    cancellationStatus = true;
                }
            }
        }
        if (cancellationStatus) {
            AnimalHarm.setCancelled(true);
        }
    }
}
