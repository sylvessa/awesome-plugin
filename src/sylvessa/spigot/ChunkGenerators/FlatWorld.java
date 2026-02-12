package sylvessa.spigot.ChunkGenerators;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class FlatWorld extends ChunkGenerator {

    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
        byte[][] sections = new byte[8][];

        for (int section = 0; section < 4; section++) {
            sections[section] = new byte[4096];
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                set(sections, x, 0, z, Material.BEDROCK);
                for (int y = 1; y <= 49; y++) {
                    set(sections, x, y, z, Material.DIRT);
                }
                set(sections, x, 50, z, Material.GRASS);
            }
        }

        return sections;
    }

    private void set(byte[][] sections, int x, int y, int z, Material mat) {
        int section = y >> 4;
        int index = (y & 15) << 8 | z << 4 | x;
        sections[section][index] = (byte) mat.getId();
    }
}
