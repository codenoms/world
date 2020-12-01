package com.github.codenoms.world.chunk;

import com.github.codenoms.util.NamespacedKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PaletteEntry
{
    private final NamespacedKey       key;
    private final Map<String, String> properties;

    public PaletteEntry(NamespacedKey key)
    {
        this(key, new HashMap<>());
    }

    public PaletteEntry(NamespacedKey key, Map<String, String> properties)
    {
        this.key = key;
        this.properties = properties;
    }

    @Override
    public String toString()
    {
        return key.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        PaletteEntry that = (PaletteEntry) o;
        return Objects.equals(key, that.key) &&
               Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(key, properties);
    }
}
