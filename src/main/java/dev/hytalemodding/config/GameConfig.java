package dev.hytalemodding.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameConfig {
    private final List<WaveDefinition> waves;

    public GameConfig(List<WaveDefinition> waves) {
        this.waves = Collections.unmodifiableList(new ArrayList<>(waves));
    }

    public List<WaveDefinition> getWaves() {
        return this.waves;
    }

    public WaveDefinition getWaveByIndex(int index) {
        if (this.waves.isEmpty()) {
            throw new IllegalStateException("No waves configured.");
        }

        if (index < this.waves.size()) {
            return this.waves.get(index);
        }

        return this.waves.get(this.waves.size() - 1);
    }
}
