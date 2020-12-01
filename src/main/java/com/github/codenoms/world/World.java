package com.github.codenoms.world;

import com.github.codenoms.world.chunk.provider.ChunkProvider;

public class World
{
    private final String name;

    private ChunkProvider chunkProvider;

    private boolean isHardcore;
    private boolean isRaining;
    private boolean isThundering;
    // TODO gametype
    private int remainingRainTicks;
    private BlockLocation spawnLocation;
    private int thunderTime;
    private int version;
    private long lastTimePlayed;
    private long seed;
    private long sizeOnDisk;
    private long time;

    public World(String name, ChunkProvider chunkProvider)
    {
        this.name = name;
        this.chunkProvider = chunkProvider;
    }

    public ChunkProvider getChunkProvider()
    {
        return chunkProvider;
    }

    public void setChunkProvider(ChunkProvider chunkProvider)
    {
        this.chunkProvider = chunkProvider;
    }

    public boolean isHardcore()
    {
        return isHardcore;
    }

    public void setHardcore(boolean hardcore)
    {
        isHardcore = hardcore;
    }

    public boolean isRaining()
    {
        return isRaining;
    }

    public void setRaining(boolean raining)
    {
        isRaining = raining;
    }

    public boolean isThundering()
    {
        return isThundering;
    }

    public void setThundering(boolean thundering)
    {
        isRaining = true;
        isThundering = thundering;
    }

    public int getRemainingRainTicks()
    {
        return remainingRainTicks;
    }

    public void setRemainingRainTicks(int remainingRainTicks)
    {
        this.remainingRainTicks = remainingRainTicks;
    }

    public BlockLocation getSpawnLocation()
    {
        return spawnLocation;
    }

    public void setSpawnLocation(BlockLocation spawnLocation)
    {
        this.spawnLocation = spawnLocation;
    }

    public int getThunderTime()
    {
        return thunderTime;
    }

    public void setThunderTime(int thunderTime)
    {
        this.thunderTime = thunderTime;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public long getLastTimePlayed()
    {
        return lastTimePlayed;
    }

    public void setLastTimePlayed(long lastTimePlayed)
    {
        this.lastTimePlayed = lastTimePlayed;
    }

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
    }

    public long getSizeOnDisk()
    {
        return sizeOnDisk;
    }

    public void setSizeOnDisk(long sizeOnDisk)
    {
        this.sizeOnDisk = sizeOnDisk;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public String getName()
    {
        return name;
    }
}
