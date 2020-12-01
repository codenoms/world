package com.github.codenoms.world.region.factory;

import com.github.codenoms.world.region.AnvilRegion;

import java.io.File;
import java.io.IOException;

public final class AnvilRegionFactory implements RegionFactory<AnvilRegion>
{
    private final File regionDirectory;

    public AnvilRegionFactory(File regionDirectory)
    {
        this.regionDirectory = regionDirectory;
    }

    @Override
    public AnvilRegion loadRegion(int regionX, int regionZ)
    {
        try
        {
            return new AnvilRegion(new File(regionDirectory, "r." + regionX + "." + regionZ + ".mca"));
        }
        catch(IOException exception)
        {
            return null;
        }
    }
}
