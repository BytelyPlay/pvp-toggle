package org.bytelyplay.pvptoggle.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.bytelyplay.pvptoggle.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@EventBusSubscriber(modid = Main.MODID)
public class PVPManager {
    // TODO: Make a singleton don't make everything static lord this is terrible code.
    private static final ExecutorService VIRTUAL_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Path SAVE_FOLDER = Path.of("./config/pvptoggle");
    private static final Path SAVE_PATH = Path.of(SAVE_FOLDER + "/pvptogglestates.json");

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(PVPManager.class);
    // ArrayList is used to make it take less space to save.
    private static ArrayList<UUID> allowPVP = new ArrayList<>();
    public static void setPVP(UUID uuid, boolean state) {
        if (!state) {
            allowPVP.remove(uuid);
            return;
        }
        if (allowPVP.contains(uuid)) return;
        allowPVP.add(uuid);
    }
    public static boolean getPVP(UUID uuid) {
        return allowPVP.contains(uuid);
    }
    public static CompletableFuture<Void> saveAll() {
        return CompletableFuture.runAsync(() -> {
            ReentrantReadWriteLock.WriteLock lock = readWriteLock.writeLock();
            lock.lock();
            try {
                if (!Files.exists(SAVE_FOLDER)) Files.createDirectories(SAVE_FOLDER);

                FileOutputStream stream = new FileOutputStream(SAVE_PATH.toString());
                stream.write(mapper.writeValueAsBytes(allowPVP));

                stream.close();
            } catch (JsonProcessingException e) {
                log.error("Something went wrong saving the data of the players.", e);
            } catch (IOException e) {
                log.error("Something went wrong saving the data to the file.", e);
            }
            lock.unlock();
        }, VIRTUAL_EXECUTOR);
    }
    public static CompletableFuture<Void> readAll() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (Files.exists(SAVE_PATH)) {
                    String data = Files.readString(SAVE_PATH);
                    allowPVP = mapper.readValue(data, new TypeReference<>() {});
                } else {
                    log.warn("SAVE_PATH doesn't exist if this is a first run this is normal.");
                }
            } catch (IOException e) {
                log.error("Something went wrong reading the file.", e);
            }
        }, VIRTUAL_EXECUTOR);
    }
    @SubscribeEvent
    public static void onTick(ServerTickEvent.Post event) {
        // based off overworld also i'm scared to close it because it might remove the dimension from memory
        log.debug(String.valueOf(event.getServer().overworld().getGameTime() % 600));
        if (event.getServer().overworld().getGameTime() % 600 == 0) {
            saveAll();
        }
    }
    @SubscribeEvent
    public static void onServerShuttingDownEvent(ServerStoppingEvent event) {
        saveAll();

        VIRTUAL_EXECUTOR.shutdown();
    }
}
