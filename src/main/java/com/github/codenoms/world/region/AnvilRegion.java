package com.github.codenoms.world.region;

import com.github.codenoms.nbt.NBT;
import com.github.codenoms.nbt.NBTCompound;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class AnvilRegion implements Region
{
    private final File                   file;
    private final RandomAccessFile       fileAccess;
    private final ReentrantReadWriteLock locker;
    private final int[][]                chunkLocations = new int[32][32];


    public AnvilRegion(File file) throws IOException
    {
        this.file       = file;
        this.fileAccess = new RandomAccessFile(file, "r");
        this.locker     = new ReentrantReadWriteLock(false);
        readLocations();
    }

    private void readLocations() throws IOException
    {
        DataInputStream stream = new DataInputStream(new FileInputStream(file));
        for(int x = 0; x < 32; x++)
            for(int z = 0; z < 32; z++)
                chunkLocations[x][z] = stream.readInt();
        stream.close();
    }

    private int getFileOffset(int relChunkX, int relChunkZ)
    {
        return (chunkLocations[relChunkX][relChunkZ] >>> 8) * 4096;
    }

    private int getFileLength(int relChunkX, int relChunkZ)
    {
        return (chunkLocations[relChunkX][relChunkZ] & 0xFF) * 4096;
    }

    @Override
    public NBTCompound readChunkNBT(int relChunkX, int relChunkZ) throws IOException
    {
        int offset = getFileOffset(relChunkX, relChunkZ);
        if(offset == 0)
            return null;
        int length = getFileLength(relChunkX, relChunkZ);

        Lock lock = locker.readLock();
        lock.lock();

        byte[] bytes = new byte[length];
        try
        {
            fileAccess.seek(offset);
            fileAccess.read(bytes);
        }
        finally
        {
            lock.unlock();
        }

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bytes));

        // read actual length of NBT data
        byte[] realBytes = new byte[stream.readInt()];
        System.arraycopy(bytes, 5, realBytes, 0, realBytes.length);

        NBTCompound chunk;
        switch(stream.readByte())
        {
            case 1:
                chunk = NBT.readAsGZip(new ByteArrayInputStream(realBytes));
                break;
            case 2:
                chunk = NBT.readAsZLib(new ByteArrayInputStream(realBytes));
                break;
            case 3:
                chunk = NBT.read(new ByteArrayInputStream(realBytes));
                break;
            default:
                throw new IllegalArgumentException("bad compression type");
        }

        return chunk;
    }

    @Override
    public void close() throws IOException
    {
        fileAccess.close();
    }
}
