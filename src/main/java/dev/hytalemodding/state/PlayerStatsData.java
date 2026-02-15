package dev.hytalemodding.state;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerStatsData implements Component<EntityStore> {
    public static final BuilderCodec<PlayerStatsData> CODEC = BuilderCodec.builder(PlayerStatsData.class, PlayerStatsData::new)
        .append(new KeyedCodec<>("MeleeAttackPower", Codec.INTEGER), (data, value) -> data.meleeAttackPower = value, data -> data.meleeAttackPower)
        .add()
        .append(new KeyedCodec<>("MeleeAttackSpeed", Codec.INTEGER), (data, value) -> data.meleeAttackSpeed = value, data -> data.meleeAttackSpeed)
        .add()
        .append(new KeyedCodec<>("RangedVelocity", Codec.INTEGER), (data, value) -> data.rangedVelocity = value, data -> data.rangedVelocity)
        .add()
        .append(new KeyedCodec<>("RangedReload", Codec.INTEGER), (data, value) -> data.rangedReload = value, data -> data.rangedReload)
        .add()
        .build();

    private int meleeAttackPower;
    private int meleeAttackSpeed;
    private int rangedVelocity;
    private int rangedReload;

    public PlayerStatsData() {
        this.meleeAttackPower = 0;
        this.meleeAttackSpeed = 0;
        this.rangedVelocity = 0;
        this.rangedReload = 0;
    }

    public PlayerStatsData(PlayerStatsData clone) {
        this.meleeAttackPower = clone.meleeAttackPower;
        this.meleeAttackSpeed = clone.meleeAttackSpeed;
        this.rangedVelocity = clone.rangedVelocity;
        this.rangedReload = clone.rangedReload;
    }

    public int getMeleeAttackPower() {
        return this.meleeAttackPower;
    }

    public void setMeleeAttackPower(int meleeAttackPower) {
        this.meleeAttackPower = meleeAttackPower;
    }

    public int getMeleeAttackSpeed() {
        return this.meleeAttackSpeed;
    }

    public void setMeleeAttackSpeed(int meleeAttackSpeed) {
        this.meleeAttackSpeed = meleeAttackSpeed;
    }

    public int getRangedVelocity() {
        return this.rangedVelocity;
    }

    public void setRangedVelocity(int rangedVelocity) {
        this.rangedVelocity = rangedVelocity;
    }

    public int getRangedReload() {
        return this.rangedReload;
    }

    public void setRangedReload(int rangedReload) {
        this.rangedReload = rangedReload;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        return new PlayerStatsData(this);
    }
}
