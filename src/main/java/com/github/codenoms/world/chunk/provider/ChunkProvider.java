package com.github.codenoms.world.chunk.provider;

import com.github.codenoms.world.chunk.Chunk;

import java.io.IOException;
import java.util.concurrent.Future;

public interface ChunkProvider
{
    Future<Chunk> getChunkAt(int chunkX, int chunkZ);

    void close() throws IOException, InterruptedException;
}
