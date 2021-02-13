// Plugin originally by HandsChrift however his plugin is the worst plugin I've ever seen so I remade it. https://read-my-man.ga/F1dlq6OUeA.png
package me.moomoo.worldstats;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {
    public double size;
    public int offlinePlayers;

    @Override
    public void onEnable() {
        getLogger().info("WorldStats enabled");
        saveDefaultConfig();
        if (getConfig().get("time") == null) {
            getConfig().set("time", System.currentTimeMillis());
            saveConfig();
        }
        getCommand("worldstats").setExecutor(new Commands(this));
        // Check every 1 hour.
        Bukkit.getScheduler().runTaskTimer(this, () -> new Thread(() -> {
            size = list() / 1048576.0D / 1000.0D;
            offlinePlayers = Bukkit.getOfflinePlayers().length;
            getLogger().info("[WorldStats] Finished checking file size: " + size);
        }).start(), 0L, getConfig().getLong("filesizeupdate_in_ticks")); // 72000

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Expansions(this).register();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("WorldStats disabled");
    }

    private long list() {
        long l = 0L;
        for (String s : getConfig().getStringList("Worlds")) {
            for (File file : Objects.requireNonNull(new File(s).listFiles())) {
                if (file.isFile()) {
                    l += file.length();
                }
            }
        }
        return l;
    }
}
