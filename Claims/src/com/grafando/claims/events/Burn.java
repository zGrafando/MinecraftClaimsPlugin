package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import java.sql.Connection;
import java.util.List;

public class Burn implements Listener {
    Claimspace claimspace = new Claimspace();
    Connection connection;
    private boolean cancellationStatus;
    public Burn(Connection conn) {
        this.connection = conn;
    }


    @EventHandler
    public void onBlockBurn(BlockBurnEvent BlockBurn) {
        cancellationStatus = false;
        if (!claimspace.isInClaim(BlockBurn.getBlock().getLocation().getWorld().getName(),
                BlockBurn.getBlock().getLocation().getX(), BlockBurn.getBlock().getLocation().getZ(),
                connection, "name").isEmpty()) {
            cancellationStatus = true;
        }
        if (cancellationStatus) {
            BlockBurn.setCancelled(true);
        }
    }
}
