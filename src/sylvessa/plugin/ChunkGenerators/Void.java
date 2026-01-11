package sylvessa.plugin.ChunkGenerators;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class Void extends ChunkGenerator {

    private final byte[] airChunk = new byte[16 * 128 * 16]; // all zeros = air

    @Override
    public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
        byte[] chunk = new byte[airChunk.length];
        System.arraycopy(airChunk, 0, chunk, 0, airChunk.length);
        return chunk;
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        world.setSpawnLocation(0, 128, 0);
        return true;
    }
}

