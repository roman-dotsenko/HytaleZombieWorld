package dev.hytalemodding.core;

import java.util.HashMap;
import java.util.Map;

public class SpawnTracker {
    private final Map<TickKey, Integer> activeByTick = new HashMap<>();

    public synchronized void registerSpawn(String waveId, int tickIndex, int count) {
        if (count <= 0) {
            return;
        }

        TickKey key = new TickKey(waveId, tickIndex);
        this.activeByTick.merge(key, count, Integer::sum);
    }

    public synchronized void onEnemyKilled(String waveId, int tickIndex) {
        TickKey key = new TickKey(waveId, tickIndex);
        Integer remaining = this.activeByTick.get(key);
        if (remaining == null) {
            return;
        }

        int updated = remaining - 1;
        if (updated <= 0) {
            this.activeByTick.remove(key);
        } else {
            this.activeByTick.put(key, updated);
        }
    }

    public synchronized boolean isTickCleared(String waveId, int tickIndex) {
        return !this.activeByTick.containsKey(new TickKey(waveId, tickIndex));
    }

    public synchronized boolean isWaveCleared(String waveId) {
        for (TickKey key : this.activeByTick.keySet()) {
            if (key.waveId.equals(waveId)) {
                return false;
            }
        }

        return true;
    }

    public synchronized void clearWave(String waveId) {
        this.activeByTick.keySet().removeIf(key -> key.waveId.equals(waveId));
    }

    private static class TickKey {
        private final String waveId;
        private final int tickIndex;

        private TickKey(String waveId, int tickIndex) {
            this.waveId = waveId;
            this.tickIndex = tickIndex;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof TickKey)) {
                return false;
            }

            TickKey that = (TickKey) other;
            return this.tickIndex == that.tickIndex && this.waveId.equals(that.waveId);
        }

        @Override
        public int hashCode() {
            int result = this.waveId.hashCode();
            result = 31 * result + this.tickIndex;
            return result;
        }
    }
}
