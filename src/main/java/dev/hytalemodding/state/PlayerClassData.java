package dev.hytalemodding.state;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerClassData implements Component<EntityStore> {
    public static final BuilderCodec<PlayerClassData> CODEC = BuilderCodec.builder(PlayerClassData.class, PlayerClassData::new)
        .append(new KeyedCodec<>("ClassId", Codec.STRING), (data, value) -> data.classId = value, data -> data.classId)
        .add()
        .append(new KeyedCodec<>("PathId", Codec.STRING), (data, value) -> data.pathId = value, data -> data.pathId)
        .add()
        .build();

    private String classId;
    private String pathId;

    public PlayerClassData() {
        this.classId = "";
        this.pathId = "";
    }

    public PlayerClassData(PlayerClassData clone) {
        this.classId = clone.classId;
        this.pathId = clone.pathId;
    }

    public String getClassId() {
        return this.classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getPathId() {
        return this.pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        return new PlayerClassData(this);
    }
}
