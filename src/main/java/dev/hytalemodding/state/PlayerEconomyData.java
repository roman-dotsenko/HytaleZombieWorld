package dev.hytalemodding.state;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerEconomyData implements Component<EntityStore> {
    public static final BuilderCodec<PlayerEconomyData> CODEC = BuilderCodec.builder(PlayerEconomyData.class, PlayerEconomyData::new)
        .append(new KeyedCodec<>("Coins", Codec.INTEGER), (data, value) -> data.coins = value, data -> data.coins)
        .add()
        .append(new KeyedCodec<>("TotalEarned", Codec.INTEGER), (data, value) -> data.totalEarned = value, data -> data.totalEarned)
        .add()
        .build();

    private int coins;
    private int totalEarned;

    public PlayerEconomyData() {
        this.coins = 0;
        this.totalEarned = 0;
    }

    public PlayerEconomyData(PlayerEconomyData clone) {
        this.coins = clone.coins;
        this.totalEarned = clone.totalEarned;
    }

    public int getCoins() {
        return this.coins;
    }

    public void addCoins(int amount) {
        if (amount <= 0) {
            return;
        }
        this.coins += amount;
        this.totalEarned += amount;
    }

    public boolean spendCoins(int amount) {
        if (amount <= 0 || this.coins < amount) {
            return false;
        }
        this.coins -= amount;
        return true;
    }

    public int getTotalEarned() {
        return this.totalEarned;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        return new PlayerEconomyData(this);
    }
}
