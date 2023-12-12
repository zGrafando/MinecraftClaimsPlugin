package com.grafando.claims.utils;

import com.grafando.claims.Claims;
import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class DisplayBlocks {

    private final ArrayList<Block> blockList = new ArrayList<>();
    private final ArrayList<Material> materialList = new ArrayList<>();

    public void displayBlocks(Block prevBlock, Block clickedBlock, Claims instance){
        getBlocksForDisplay(prevBlock, clickedBlock);               //Dissilusion here
        Block cornerBot = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
        Block cornerTop = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
        for (Block tempBlock : blockList) {
            materialList.add(tempBlock.getType());
            if (tempBlock.getX() == clickedBlock.getX() && tempBlock.getZ() == clickedBlock.getZ() ||
                    tempBlock.getX() == prevBlock.getX() && tempBlock.getZ() == prevBlock.getZ() ||
                    tempBlock.getX() == cornerBot.getX() && tempBlock.getZ() == cornerBot.getZ() ||
                    tempBlock.getX() == cornerTop.getX() && tempBlock.getZ() == cornerTop.getZ()) {
                if (tempBlock.getBiome().equals(Biome.SNOWY_BEACH) || tempBlock.getBiome().equals(Biome.SNOWY_MOUNTAINS) ||
                        tempBlock.getBiome().equals(Biome.SNOWY_TAIGA) || tempBlock.getBiome().equals(Biome.SNOWY_TAIGA_HILLS) ||
                        tempBlock.getBiome().equals(Biome.SNOWY_TAIGA_MOUNTAINS) || tempBlock.getBiome().equals(Biome.SNOWY_TUNDRA) ||
                        tempBlock.getBiome().equals(Biome.ICE_SPIKES)) {
                    tempBlock.getLocation().getBlock().setType(Material.OBSIDIAN);
                } else {
                    tempBlock.getLocation().getBlock().setType(Material.PACKED_ICE);
                }
            } else {
                if (tempBlock.getBiome().equals(Biome.SNOWY_BEACH) || tempBlock.getBiome().equals(Biome.SNOWY_MOUNTAINS) ||
                        tempBlock.getBiome().equals(Biome.SNOWY_TAIGA) || tempBlock.getBiome().equals(Biome.SNOWY_TAIGA_HILLS) ||
                        tempBlock.getBiome().equals(Biome.SNOWY_TAIGA_MOUNTAINS) || tempBlock.getBiome().equals(Biome.SNOWY_TUNDRA) ||
                        tempBlock.getBiome().equals(Biome.ICE_SPIKES)) {
                    tempBlock.getLocation().getBlock().setType(Material.COAL_BLOCK);
                } else {
                    tempBlock.getLocation().getBlock().setType(Material.SNOW_BLOCK);
                }
            }
        }
        instance.runDelayedTask(blockList, materialList);
    }

    public void getBlocksForDisplay(Block prevBlock, Block clickedBlock){
        Block clickedBlockNextX; Block clickedBlockNextZ; Block prevBlockNextX; Block prevBlockNextZ;
        Block cornerBlockBot; Block cornerBlockBotNextX; Block cornerBlockBotNextZ;
        Block cornerBlockTop; Block cornerBlockTopNextX; Block cornerBlockTopNextZ;
        if (prevBlock.getX() < clickedBlock.getX()){
            if (prevBlock.getZ() < clickedBlock.getZ()){
                clickedBlockNextX = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX()-1, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                clickedBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), clickedBlock.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextX = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX()+1, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), prevBlock.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBot = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX()+1, cornerBlockBot.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX(), cornerBlockBot.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTop = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX()-1, cornerBlockTop.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX(),cornerBlockTop.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                for (int i = prevBlock.getX()+4; i < clickedBlock.getX()-4; i = i+8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(i, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(i, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
                for (int i = prevBlock.getZ()+4; i < clickedBlock.getZ()-4; i = i+8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
            }else{
                clickedBlockNextX = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX()-1, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                clickedBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), clickedBlock.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextX = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX()+1, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), prevBlock.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBot = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX()+1, cornerBlockBot.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX(), cornerBlockBot.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTop = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX()-1, cornerBlockTop.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX(), cornerBlockTop.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                for (int i = prevBlock.getX()+4; i < clickedBlock.getX()-4; i = i+8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(i, prevBlock.getZ());
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(i, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
                for (int i = prevBlock.getZ()-4; i > clickedBlock.getZ()+4; i = i-8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
            }
        }else{
            if (prevBlock.getZ() < clickedBlock.getZ()) {
                clickedBlockNextX = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX()+1, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                clickedBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), clickedBlock.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextX = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX()-1, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), prevBlock.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBot = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX()+1, cornerBlockBot.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX(), cornerBlockBot.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTop = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX()-1, cornerBlockTop.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX(), cornerBlockTop.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                for (int i = prevBlock.getX()-4; i > clickedBlock.getX()+4; i = i-8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(i, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(i, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
                for (int i = prevBlock.getZ()+4; i < clickedBlock.getZ()-4; i = i+8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
            }else{
                clickedBlockNextX = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX()+1, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                clickedBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), clickedBlock.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextX = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX()-1, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                prevBlockNextZ = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), prevBlock.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBot = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX()+1, cornerBlockBot.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockBotNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockBot.getX(), cornerBlockBot.getZ()-1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTop = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextX = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX()-1, cornerBlockTop.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                cornerBlockTopNextZ = clickedBlock.getWorld().getHighestBlockAt(cornerBlockTop.getX(), cornerBlockTop.getZ()+1, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                for (int i = prevBlock.getX()-4; i > clickedBlock.getX()+4; i = i-8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(i, prevBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(i, clickedBlock.getZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
                for (int i = prevBlock.getZ()-4; i > clickedBlock.getZ()+4; i = i-8){
                    Block tempBlockOnPrev = clickedBlock.getWorld().getHighestBlockAt(prevBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnPrev);
                    Block tempBlockOnClick = clickedBlock.getWorld().getHighestBlockAt(clickedBlock.getX(), i, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                    blockList.add(tempBlockOnClick);
                }
            }
        }
        blockList.add(clickedBlock); blockList.add(clickedBlockNextX); blockList.add(clickedBlockNextZ);
        blockList.add(prevBlock); blockList.add(prevBlockNextX); blockList.add(prevBlockNextZ);
        blockList.add(cornerBlockBot); blockList.add(cornerBlockBotNextX); blockList.add(cornerBlockBotNextZ); blockList.add(cornerBlockTop);
        blockList.add(cornerBlockTopNextX); blockList.add(cornerBlockTopNextZ);
    }
}
