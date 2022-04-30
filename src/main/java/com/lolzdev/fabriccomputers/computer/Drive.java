package com.lolzdev.fabriccomputers.computer;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

public class Drive {
    public ArrayList<Integer> content;
    public UUID uuid;
    private FileWriter writer;

    public Drive(int size, UUID id) {
        this.content = new ArrayList<>();

        Path path = FabricLoader.getInstance().getGameDir().resolve("fabriccomputers");
        File dir = new File(path.toUri());
        if (!dir.exists()) {
            dir.mkdir();
        }

        this.uuid = id;

        Path drivePath = path.resolve("fib.bin");
        File file = new File(drivePath.toUri());

        if (file.exists()) {
            try {
                byte[] data = Files.readAllBytes(drivePath);
                for (byte b : data) {
                    System.out.println("Loaded: " + ((int) b & 0xff));
                    content.add((int) b & 0xff);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
