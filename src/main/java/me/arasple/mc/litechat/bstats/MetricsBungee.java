package me.arasple.mc.litechat.bstats;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

/**
 * bStats collects some data for plugin authors.
 * Check out https://bStats.org/ to learn more about bStats!
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MetricsBungee {

    static {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            final String defaultPackage = new String(new byte[]{'o', 'r', 'g', '.', 'b', 's', 't', 'a', 't', 's', '.', 'b', 'u', 'n', 'g', 'e', 'e', 'c', 'o', 'r', 'd'});
            final String examplePackage = new String(new byte[]{'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e'});
            if (MetricsBungee.class.getPackage().getName().equals(defaultPackage) || MetricsBungee.class.getPackage().getName().equals(examplePackage)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }

    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bungeecord";
    private final Plugin plugin;
    private boolean enabled;
    private String serverUUID;
    private boolean logFailedRequests = false;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static final List<Object> knownMetricsInstances = new ArrayList<>();

    public MetricsBungee(Plugin plugin) {
        this.plugin = plugin;
        try {
            loadConfig();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load bStats config!", e);
            return;
        }
        if (!enabled) {
            return;
        }
        Class<?> usedMetricsClass = getFirstBStatsClass();
        if (usedMetricsClass == null) {
            return;
        }
        if (usedMetricsClass == getClass()) {
            linkMetrics(this);
            startSubmitting();
        } else {
            try {
                usedMetricsClass.getMethod("linkMetrics", Object.class).invoke(null, this);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                if (logFailedRequests) {
                    plugin.getLogger().log(Level.WARNING, "Failed to link to first metrics class " + usedMetricsClass.getName() + "!", e);
                }
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static void linkMetrics(Object metrics) {
        knownMetricsInstances.add(metrics);
    }

    public JsonObject getPluginData() {
        JsonObject data = new JsonObject();
        String pluginName = plugin.getDescription().getName();
        String pluginVersion = plugin.getDescription().getVersion();
        data.addProperty("pluginName", pluginName);
        data.addProperty("pluginVersion", pluginVersion);
        JsonArray customCharts = new JsonArray();
        data.add("customCharts", customCharts);
        return data;
    }

    private void startSubmitting() {
        plugin.getProxy().getScheduler().schedule(plugin, this::submitData, 2, 30, TimeUnit.MINUTES);
    }

    private JsonObject getServerData() {
        int playerAmount = plugin.getProxy().getOnlineCount();
        playerAmount = Math.min(playerAmount, 500);
        int onlineMode = plugin.getProxy().getConfig().isOnlineMode() ? 1 : 0;
        String bungeecordVersion = plugin.getProxy().getVersion();
        int managedServers = plugin.getProxy().getServers().size();
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();
        JsonObject data = new JsonObject();
        data.addProperty("serverUUID", serverUUID);
        data.addProperty("playerAmount", playerAmount);
        data.addProperty("managedServers", managedServers);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("bungeecordVersion", bungeecordVersion);
        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);
        return data;
    }

    private void submitData() {
        final JsonObject data = getServerData();
        final JsonArray pluginData = new JsonArray();
        for (Object metrics : knownMetricsInstances) {
            try {
                Object plugin = metrics.getClass().getMethod("getPluginData").invoke(metrics);
                if (plugin instanceof JsonObject) {
                    pluginData.add((JsonObject) plugin);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        data.add("plugins", pluginData);
        try {
            sendData(plugin, data);
        } catch (Exception e) {
            if (logFailedRequests) {
                plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats!", e);
            }
        }
    }

    private void loadConfig() throws IOException {
        Path configPath = plugin.getDataFolder().toPath().getParent().resolve("bStats");
        configPath.toFile().mkdirs();
        File configFile = new File(configPath.toFile(), "config.yml");
        if (!configFile.exists()) {
            writeFile(configFile, "#bStats collects some data for plugin authors like how many servers are using their plugins.", "#To honor their work, you should not disable it.", "#This has nearly no effect on the server performance!", "#Check out https://bStats.org/ to learn more :)", "enabled: true", "serverUuid: \"" + UUID.randomUUID().toString() + "\"", "logFailedRequests: false", "logSentData: false", "logResponseStatusText: false");
        }
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        enabled = configuration.getBoolean("enabled", true);
        serverUUID = configuration.getString("serverUuid");
        logFailedRequests = configuration.getBoolean("logFailedRequests", false);
        logSentData = configuration.getBoolean("logSentData", false);
        logResponseStatusText = configuration.getBoolean("logResponseStatusText", false);
    }

    private Class<?> getFirstBStatsClass() {
        Path configPath = plugin.getDataFolder().toPath().getParent().resolve("bStats");
        configPath.toFile().mkdirs();
        File tempFile = new File(configPath.toFile(), "temp.txt");
        try {
            String className = readFile(tempFile);
            if (className != null) {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException ignored) {
                }
            }
            writeFile(tempFile, getClass().getName());
            return getClass();
        } catch (IOException e) {
            if (logFailedRequests) {
                plugin.getLogger().log(Level.WARNING, "Failed to get first bStats class!", e);
            }
            return null;
        }
    }

    private String readFile(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            return bufferedReader.readLine();
        }
    }

    private void writeFile(File file, String... lines) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter fileWriter = new FileWriter(file); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }

    private static void sendData(Plugin plugin, JsonObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + data.toString());
        }
        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();
        byte[] compressedData = compress(data.toString());
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(compressedData);
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }
        bufferedReader.close();
        if (logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + builder.toString());
        }
    }

    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.close();
        return outputStream.toByteArray();
    }

}