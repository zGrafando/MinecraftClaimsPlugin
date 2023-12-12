package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.sql.Connection;
import java.util.List;

public class BlockExplosion implements Listener {

    List<Block> placedBlock;
    Claimspace claimspace = new Claimspace();
    Connection connection;
    private boolean cancellationStatus;

    public BlockExplosion(Connection conn){
        this.connection = conn;
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent explodingBlock){
        placedBlock = explodingBlock.blockList();
        cancellationStatus = false;
        for (int i = 0; i < placedBlock.size(); i++) {
            if (!claimspace.isInClaim(placedBlock.get(i).getLocation().getWorld().getName(),
                    placedBlock.get(i).getLocation().getX(), placedBlock.get(i).getLocation().getZ(), connection, "name").isEmpty()) {
                cancellationStatus = true;
            }
        }
        if (cancellationStatus) {
            explodingBlock.blockList().clear();
            explodingBlock.setCancelled(true);
        }
    }
}
