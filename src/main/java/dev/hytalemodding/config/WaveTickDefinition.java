package dev.hytalemodding.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaveTickDefinition {
    private final int delaySeconds;
    private final List<SpawnDefinition> spawns;

    public WaveTickDefinition(int delaySeconds, List<SpawnDefinition> spawns) {
        this.delaySeconds = delaySeconds;
        this.spawns = Collections.unmodifiableList(new ArrayList<>(spawns));
    }

    public int getDelaySeconds() {
        return this.delaySeconds;
    }

    public List<SpawnDefinition> getSpawns() {
        return this.spawns;
    }
}
