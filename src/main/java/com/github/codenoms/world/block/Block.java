package com.github.codenoms.world.block;

import com.github.codenoms.world.BlockLocation;
import com.github.codenoms.world.chunk.PaletteEntry;

public class Block
{
    private final BlockLocation location;

    private PaletteEntry type;

    public Block(BlockLocation location, PaletteEntry type)
    {
        this.location = location;
        this.type = type;
    }

    public BlockLocation getLocation()
    {
        return location;
    }

    public PaletteEntry getType()
    {
        return type;
    }

    public void setType(PaletteEntry type)
    {
        this.type = type;
    }
}
