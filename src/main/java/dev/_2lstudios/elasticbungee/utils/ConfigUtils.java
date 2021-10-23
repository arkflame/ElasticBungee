package dev._2lstudios.elasticbungee.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigUtils {
    public static Configuration getConfig(final File file) throws IOException {
        saveDefault(file.getParentFile(), file.getName());

        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }

    public static void saveDefault(final File targetDir, final String fileName) {
        if (!targetDir.exists())
            targetDir.mkdir();

        File file = new File(targetDir, fileName);

        if (!file.exists()) {
            try (InputStream in = ConfigUtils.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveConfig(final Configuration configuration, final File file) throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
    }
}
