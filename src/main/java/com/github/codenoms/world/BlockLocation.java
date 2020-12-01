package com.github.codenoms.world;

public final class BlockLocation
{
    private final int x;
    private final int y;
    private final int z;

    public BlockLocation(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }
}
