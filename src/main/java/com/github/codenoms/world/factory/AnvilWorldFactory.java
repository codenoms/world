package com.github.codenoms.world.factory;

import com.github.codenoms.nbt.NBT;
import com.github.codenoms.nbt.NBTCompound;
import com.github.codenoms.world.BlockLocation;
import com.github.codenoms.world.World;
import com.github.codenoms.world.chunk.provider.BasicChunkProvider;
import com.github.codenoms.world.region.factory.AnvilRegionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

public class AnvilWorldFactory implements WorldFactory
{
    private final File worldsDirectory;

    public AnvilWorldFactory(File worldsDirectory)
    {
        this.worldsDirectory = worldsDirectory;
    }

    @Override
    public World createWorld(String name) throws IOException
    {
        File worldFolder = new File(worldsDirectory, name);
        if(!worldFolder.exists())
            return null;
        File levelDatFile = new File(worldFolder, "level.dat");
        if(!levelDatFile.exists())
            return null;

        NBTCompound levelDat = NBT.readAsGZip(new FileInputStream(levelDatFile)).getCompound("Data");

        World world = new World(name,
                                new BasicChunkProvider<>(
                                        Executors.newFixedThreadPool(4),
                                        new AnvilRegionFactory(new File(worldFolder, "region"))));

        world.setHardcore(levelDat.getByte("hardcore") != 0);
        world.setRaining(levelDat.getByte("raining") == 1);
        world.setThundering(levelDat.getByte("thundering") == 1);
        world.setRemainingRainTicks(levelDat.getInt("rainTime"));

        BlockLocation spawnLocation = new BlockLocation(levelDat.getInt("SpawnX"),
                                                        levelDat.getInt("SpawnY"),
                                                        levelDat.getInt("SpawnZ"));
        world.setSpawnLocation(spawnLocation);

        world.setThunderTime(levelDat.getInt("thunderTime"));
        world.setVersion(levelDat.getInt("version"));
        world.setLastTimePlayed(levelDat.getLong("LastPlayed"));
        world.setTime(levelDat.getLong("Time"));

        return world;
    }
}
