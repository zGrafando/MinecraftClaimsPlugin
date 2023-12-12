package com.grafando.claims.events;

import com.grafando.claims.data.Claimspace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.Connection;
import java.time.LocalDateTime;

public class PlayerActivity implements Listener {

    private Player player;
    private Claimspace claimspace = new Claimspace();
    private Connection conn;

    public PlayerActivity(Connection connectionInstance){
         this.conn = connectionInstance;
    }

    @EventHandler
    public void onPlayerMovmentDetection(PlayerMoveEvent Pmve){
        player = Pmve.getPlayer();
        LocalDateTime Instant = LocalDateTime.now();
        claimspace.updateLastPlayerMovmentTimestamp(player, Instant, conn);
        if (claimspace.getcurrentActivityIndicator(player, conn) > 0){
            claimspace.increaseActivityIndicator(player, 0, conn);
        }
        //update displayed name from [AFK] to "normal"
        player.setDisplayName(player.getDisplayName().substring(0, player.getName().length()));
    }
}
