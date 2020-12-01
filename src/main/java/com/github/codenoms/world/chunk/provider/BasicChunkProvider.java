package com.github.codenoms.world.chunk.provider;

import com.github.codenoms.nbt.NBTCompound;
import com.github.codenoms.nbt.NBTString;
import com.github.codenoms.util.NamespacedKey;
import com.github.codenoms.world.chunk.Chunk;
import com.github.codenoms.world.chunk.ChunkSection;
import com.github.codenoms.world.chunk.PaletteEntry;
import com.github.codenoms.world.region.Region;
import com.github.codenoms.world.region.factory.RegionFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BasicChunkProvider<T extends Region> implements ChunkProvider
{
    private static final class RegionLocation
    {
        private int regionFileX;
        private int regionFileZ;

        public RegionLocation(int chunkX, int chunkZ)
        {
            this.regionFileX = chunkX >> 5;
            this.regionFileZ = chunkZ >> 5;
        }

        @Override
        public boolean equals(Object o)
        {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            RegionLocation location = (RegionLocation) o;
            return regionFileX == location.regionFileX &&
                   regionFileZ == location.regionFileZ;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(regionFileX, regionFileZ);
        }
    }

    private final ExecutorService        ioPool;
    private final RegionFactory<T>       regionFactory;
    private final Map<RegionLocation, T> openRegionFiles = new HashMap<>();

    public BasicChunkProvider(ExecutorService ioPool, RegionFactory<T> regionFactory)
    {
        this.ioPool        = ioPool;
        this.regionFactory = regionFactory;
    }

    @Override
    public Future<Chunk> getChunkAt(int chunkX, int chunkZ)
    {
        Region region = openRegionFiles.computeIfAbsent(new RegionLocation(chunkX, chunkZ),
                                                        (location) -> regionFactory.loadRegion(location.regionFileX, location.regionFileZ));

        if(region == null)
            return CompletableFuture.completedFuture(null);

        return ioPool.submit(() ->
        {
            try
            {
                NBTCompound compound = region.readChunkNBT(chunkX & 0b11111, chunkZ & 0b11111);

                List<ChunkSection> sections = compound.getCompound("Level")
                        .getList("Sections")
                        .stream()
                        .map((section) -> (NBTCompound) section)
                        .filter((section) -> section.getByte("Y") >= 0 && section.getByte("Y") < 16 && section.hasElement("Palette"))
                        .map((section) ->
                {
                    List<PaletteEntry> palette = new ArrayList<>();
                    section.getList("Palette")
                           .stream()
                           .map((element) -> (NBTCompound) element)
                           .forEach((element) ->
                           {
                               Map<String, String> propertyMap = new HashMap<>();
                               if(element.hasElement("Properties"))
                                   element.getCompound("Properties")
                                          .getElements()
                                          .stream()
                                          .map((property) -> (NBTString) property)
                                          .forEach((property) -> propertyMap.put(property.getName(), property.getValue()));

                               palette.add(new PaletteEntry(new NamespacedKey(element.getString("Name")), propertyMap));
                           });

                    int bits = (int) Math.ceil(Math.log(palette.size()) / Math.log(2));
                    if(bits < 4)
                        bits = 4;

                    long[] longs = section.getLongArray("BlockStates");

                    BitSet bitSet = BitSet.valueOf(longs);

                    int[][][] blocks = new int[16][16][16];

                    int position = 0;
                    for(int y = 0; y < 16; y++)
                        for(int j = 0; j < 16; j++)
                            for(int k = 0; k < 16; k++)
                            {
                                long[] blockState = bitSet.get(position, position += bits).toLongArray();
                                if(blockState.length != 0)
                                    blocks[y][j][k] = (int) blockState[0];
                            }

                    return new ChunkSection(chunkX, chunkZ, section.getByte("Y"), palette, blocks);
                }).collect(Collectors.toList());

                return new Chunk(chunkX, chunkZ, sections);
            }
            catch(IOException e)
            {
                return null;
            }
        });
    }

    @Override
    public void close() throws InterruptedException
    {
        ioPool.shutdown();
        ioPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);

        for(Region file : openRegionFiles.values())
            try
            {
                file.close();
            }
            catch(IOException exception)
            {
                exception.printStackTrace();
            }
    }
}
