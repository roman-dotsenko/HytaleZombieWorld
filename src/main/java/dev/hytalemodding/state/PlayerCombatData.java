package dev.hytalemodding.state;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerCombatData implements Component<EntityStore> {
    public static final BuilderCodec<PlayerCombatData> CODEC = BuilderCodec.builder(PlayerCombatData.class, PlayerCombatData::new)
        .append(new KeyedCodec<>("LastAttackMillis", Codec.LONG), (data, value) -> data.lastAttackMillis = value, data -> data.lastAttackMillis)
        .add()
        .build();

    private long lastAttackMillis;

    public PlayerCombatData() {
        this.lastAttackMillis = 0L;
    }

    public PlayerCombatData(PlayerCombatData clone) {
        this.lastAttackMillis = clone.lastAttackMillis;
    }

    public long getLastAttackMillis() {
        return this.lastAttackMillis;
    }

    public void setLastAttackMillis(long lastAttackMillis) {
        this.lastAttackMillis = lastAttackMillis;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        return new PlayerCombatData(this);
    }
}
