package dev.hytalemodding.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaveDefinition {
    private final String id;
    private final String displayName;
    private final int breakDurationSeconds;
    private final List<WaveTickDefinition> ticks;
    private final boolean bossWave;

    public WaveDefinition(
        String id,
        String displayName,
        int breakDurationSeconds,
        List<WaveTickDefinition> ticks,
        boolean bossWave
    ) {
        this.id = id;
        this.displayName = displayName;
        this.breakDurationSeconds = breakDurationSeconds;
        this.ticks = Collections.unmodifiableList(new ArrayList<>(ticks));
        this.bossWave = bossWave;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getBreakDurationSeconds() {
        return this.breakDurationSeconds;
    }

    public List<WaveTickDefinition> getTicks() {
        return this.ticks;
    }

    public boolean isBossWave() {
        return this.bossWave;
    }
}
