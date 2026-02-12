package sylvessa.spigot.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CustomWorldLoader {
    public static void copyArenaToServerJar(String arenaName, String customWorldName) throws IOException, URISyntaxException {
        File serverFolder = new File(".");
        File targetFolder = new File(serverFolder, customWorldName);

        copyWorldFromJar(arenaName, targetFolder);
    }

    public static void copyWorldFromJar(String arenaName, File targetFolder) throws IOException, URISyntaxException {
        if(targetFolder.exists()) return;
        targetFolder.mkdirs();

        InputStream in = CustomWorldLoader.class.getResourceAsStream("/arenas/" + arenaName + "/level.dat");
        if(in == null) throw new IOException("Arena not found in jar!");
        try {
            Files.copy(in, new File(targetFolder, "level.dat").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            in.close();
        }

        copyFolderFromJar("/arenas/" + arenaName + "/region", new File(targetFolder, "region"));
    }

    private static void copyFolderFromJar(String jarPath, File targetFolder) throws IOException, URISyntaxException {
        targetFolder.mkdirs();

        File pluginJar = new File(CustomWorldLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        try (JarFile jar = new JarFile(pluginJar)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(jarPath.substring(1))) {
                    File f = new File(targetFolder, entry.getName().substring(jarPath.length()));
                    if (entry.isDirectory()) {
                        f.mkdirs();
                    } else {
                        try (InputStream in = jar.getInputStream(entry)) {
                            Files.copy(in, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
    }
}
