package dev.neddslayer.sharedhealth.components;

// import net.minecraft.nbt.NbtCompound;
// import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import java.util.HashMap;
import java.util.Map;

public class SharedHealthComponent implements IHealthComponent {

    private final Map<String, Float> teamHealth = new HashMap<>();

    Scoreboard scoreboard;
    MinecraftServer server;

    public SharedHealthComponent(Scoreboard scoreboard, MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    @Override
    public float getHealth(String team) {
        return teamHealth.getOrDefault(team, 20.0f);
    }

    @Override
    public void setHealth(String team, float health) {
        teamHealth.put(team, health);
    }

    @Override
    public void readData(ReadView readView) {

    }

    @Override
    public void writeData(WriteView writeView) {

    }
}
