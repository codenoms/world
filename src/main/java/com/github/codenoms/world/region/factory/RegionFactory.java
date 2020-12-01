package com.github.codenoms.world.region.factory;

import com.github.codenoms.world.region.Region;

public interface RegionFactory<T extends Region>
{
    T loadRegion(int regionX, int regionZ);
}
