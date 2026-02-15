package dev.hytalemodding.config;

public class SpawnDefinition {
    private final String mobId;
    private final int count;

    public SpawnDefinition(String mobId, int count) {
        this.mobId = mobId;
        this.count = count;
    }

    public String getMobId() {
        return this.mobId;
    }

    public int getCount() {
        return this.count;
    }
}
