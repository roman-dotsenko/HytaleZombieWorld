package dev.hytalemodding.state;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class WaveStateData implements Component<EntityStore> {
    public static final BuilderCodec<WaveStateData> CODEC = BuilderCodec.builder(WaveStateData.class, WaveStateData::new)
        .append(new KeyedCodec<>("WaveIndex", Codec.INTEGER), (data, value) -> data.waveIndex = value, data -> data.waveIndex)
        .add()
        .append(new KeyedCodec<>("Phase", Codec.STRING), (data, value) -> data.phase = value, data -> data.phase)
        .add()
        .append(new KeyedCodec<>("BreakRemaining", Codec.INTEGER), (data, value) -> data.breakRemainingSeconds = value, data -> data.breakRemainingSeconds)
        .add()
        .append(new KeyedCodec<>("ElapsedSeconds", Codec.INTEGER), (data, value) -> data.elapsedSeconds = value, data -> data.elapsedSeconds)
        .add()
        .append(new KeyedCodec<>("ActiveTickIndex", Codec.INTEGER), (data, value) -> data.activeTickIndex = value, data -> data.activeTickIndex)
        .add()
        .build();

    private int waveIndex;
    private String phase;
    private int breakRemainingSeconds;
    private int elapsedSeconds;
    private int activeTickIndex;

    public WaveStateData() {
        this.waveIndex = 0;
        this.phase = "BREAK";
        this.breakRemainingSeconds = 0;
        this.elapsedSeconds = 0;
        this.activeTickIndex = 0;
    }

    public WaveStateData(WaveStateData clone) {
        this.waveIndex = clone.waveIndex;
        this.phase = clone.phase;
        this.breakRemainingSeconds = clone.breakRemainingSeconds;
        this.elapsedSeconds = clone.elapsedSeconds;
        this.activeTickIndex = clone.activeTickIndex;
    }

    public int getWaveIndex() {
        return this.waveIndex;
    }

    public void setWaveIndex(int waveIndex) {
        this.waveIndex = waveIndex;
    }

    public String getPhase() {
        return this.phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public int getBreakRemainingSeconds() {
        return this.breakRemainingSeconds;
    }

    public void setBreakRemainingSeconds(int breakRemainingSeconds) {
        this.breakRemainingSeconds = breakRemainingSeconds;
    }

    public int getElapsedSeconds() {
        return this.elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public int getActiveTickIndex() {
        return this.activeTickIndex;
    }

    public void setActiveTickIndex(int activeTickIndex) {
        this.activeTickIndex = activeTickIndex;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        return new WaveStateData(this);
    }
}
