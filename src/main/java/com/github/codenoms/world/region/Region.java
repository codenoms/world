package com.github.codenoms.world.region;

import com.github.codenoms.nbt.NBTCompound;

import java.io.IOException;

public interface Region
{
    NBTCompound readChunkNBT(int relChunkX, int relChunkZ) throws IOException;

    void close() throws IOException;
}
