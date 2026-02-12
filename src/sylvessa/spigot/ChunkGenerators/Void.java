package sylvessa.spigot.ChunkGenerators;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class Void extends ChunkGenerator {

    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
        return new byte[8][];
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        world.setSpawnLocation(5192, 128, 5192);
        return true;
    }
}
