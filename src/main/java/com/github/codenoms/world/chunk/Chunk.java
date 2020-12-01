package com.github.codenoms.world.chunk;

import com.github.codenoms.util.NamespacedKey;
import com.github.codenoms.world.block.Block;

import java.util.Collections;
import java.util.List;

public class Chunk
{
    private final int x;
    private final int z;
    private final ChunkSection[] sections;

    public Chunk(int x, int z, List<ChunkSection> sections)
    {
        this.x = x;
        this.z = z;

        this.sections = new ChunkSection[16];
        for(ChunkSection section : sections)
            this.sections[section.getSectionIndex()] = section;

        for(int i = 0; i < this.sections.length; i++)
            if(this.sections[i] == null)
                this.sections[i] = new ChunkSection(x,
                                                    z,
                                                    i,
                                                    Collections.singletonList(new PaletteEntry(new NamespacedKey("minecraft:air"))),
                                                    new int[16][16][16]);
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }

    public Block getBlock(int x, int y, int z)
    {
        if(y < 0 || y > 255)
            return null;
        return sections[y / sections.length].getBlock(x & 0b1111, y & 0b1111, z & 0b1111);
    }
}
