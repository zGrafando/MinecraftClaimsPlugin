package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import java.sql.Connection;
import java.util.List;

public class LightningStrike implements Listener{
    Claimspace claimspace = new Claimspace();
    Connection connection;
    private boolean cancellationStatus;

    public LightningStrike(Connection conn) {
        this.connection = conn;
    }

    @EventHandler
    public void onBlockExplode(BlockIgniteEvent LightningStrike) {
        cancellationStatus = false;
        if (LightningStrike.getIgnitingBlock() != null) {
            if (!claimspace.isInClaim(LightningStrike.getIgnitingBlock().getLocation().getWorld().getName(),
                    LightningStrike.getIgnitingBlock().getLocation().getX(), LightningStrike.getIgnitingBlock().getLocation().getZ(),
                    connection, "name").isEmpty()) {
                if (LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.LIGHTNING)
                        || LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL)
                        || LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.EXPLOSION)
                        || LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.SPREAD)
                        || LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.ARROW)
                        || LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.FIREBALL)
                        || LightningStrike.equals(BlockIgniteEvent.IgniteCause.LAVA)) {
                    cancellationStatus = true;
                }
                if (LightningStrike.getPlayer() == null) {

                } else {
                    if (!claimspace.isInClaim(LightningStrike.getIgnitingBlock().getLocation().getWorld().getName(),
                            LightningStrike.getIgnitingBlock().getLocation().getX(), LightningStrike.getIgnitingBlock().getLocation().getZ(),
                            connection, "uuid").equalsIgnoreCase(LightningStrike.getPlayer().getUniqueId().toString())) {
                        if (LightningStrike.getCause().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL)) {
                            cancellationStatus = true;
                        }
                    }
                }
            }
        }
        if (cancellationStatus) {
            LightningStrike.setCancelled(true);
        }
    }
}
