package dev.hytalemodding.state;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class SpawnedEnemyData implements Component<EntityStore> {
    public static final BuilderCodec<SpawnedEnemyData> CODEC = BuilderCodec.builder(SpawnedEnemyData.class, SpawnedEnemyData::new)
        .append(new KeyedCodec<>("EnemyType", Codec.STRING), (data, value) -> data.enemyType = value, data -> data.enemyType)
        .add()
        .append(new KeyedCodec<>("WaveId", Codec.STRING), (data, value) -> data.waveId = value, data -> data.waveId)
        .add()
        .append(new KeyedCodec<>("TickIndex", Codec.INTEGER), (data, value) -> data.tickIndex = value, data -> data.tickIndex)
        .add()
        .append(new KeyedCodec<>("SpawnId", Codec.STRING), (data, value) -> data.spawnId = value, data -> data.spawnId)
        .add()
        .build();

    private String enemyType;
    private String waveId;
    private int tickIndex;
    private String spawnId;

    public SpawnedEnemyData() {
        this.enemyType = "";
        this.waveId = "";
        this.tickIndex = 0;
        this.spawnId = "";
    }

    public SpawnedEnemyData(String enemyType, String waveId, int tickIndex, String spawnId) {
        this.enemyType = enemyType;
        this.waveId = waveId;
        this.tickIndex = tickIndex;
        this.spawnId = spawnId;
    }

    public SpawnedEnemyData(SpawnedEnemyData clone) {
        this.enemyType = clone.enemyType;
        this.waveId = clone.waveId;
        this.tickIndex = clone.tickIndex;
        this.spawnId = clone.spawnId;
    }

    public String getEnemyType() {
        return this.enemyType;
    }

    public String getWaveId() {
        return this.waveId;
    }

    public int getTickIndex() {
        return this.tickIndex;
    }

    public String getSpawnId() {
        return this.spawnId;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        return new SpawnedEnemyData(this);
    }
}
