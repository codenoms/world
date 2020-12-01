package com.github.codenoms.world.factory;

import com.github.codenoms.world.World;

import java.io.IOException;

public interface WorldFactory
{
    World createWorld(String name) throws IOException;
}
