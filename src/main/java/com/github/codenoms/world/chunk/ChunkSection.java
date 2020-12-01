package com.github.codenoms.world.chunk;

import com.github.codenoms.world.BlockLocation;
import com.github.codenoms.world.block.Block;

import java.util.List;

public class ChunkSection
{
    private final int sectionIndex;
    private final List<PaletteEntry> palette;
    private final Block[][][] blocks;

    public ChunkSection(int chunkX, int chunkZ, int sectionIndex, List<PaletteEntry> palette, int[][][] blockPaletteIndices)
    {
        this.sectionIndex = sectionIndex;
        this.palette      = palette;

        this.blocks = new Block[16][16][16];
        int chunkStartX = chunkX * 16,
            chunkStartY = sectionIndex * 16,
            chunkStartZ = chunkZ * 16;
        for(int y = 0; y < 16; y++)
            for(int z = 0; z < 16; z++)
                for(int x = 0; x < 16; x++)
                    blocks[y][z][x] = new Block(new BlockLocation(chunkStartX + x, chunkStartY + y, chunkStartZ + z), palette.get(blockPaletteIndices[y][z][x]));
    }

    public int getSectionIndex()
    {
        return sectionIndex;
    }

    public List<PaletteEntry> getPalette()
    {
        return palette;
    }

    public Block getBlock(int x, int y, int z)
    {
        return blocks[y][z][x];
    }
}
