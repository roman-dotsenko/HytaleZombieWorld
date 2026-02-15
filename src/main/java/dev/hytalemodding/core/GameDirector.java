package dev.hytalemodding.core;

import com.hypixel.hytale.server.core.HytaleServer;
import dev.hytalemodding.config.ConfigRepository;
import dev.hytalemodding.config.GameConfig;
import dev.hytalemodding.config.WaveDefinition;
import dev.hytalemodding.config.WaveTickDefinition;
import dev.hytalemodding.spawning.SpawnService;
import dev.hytalemodding.state.SpawnedEnemyData;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameDirector {
    private static final int LOOP_INTERVAL_SECONDS = 1;

    private final String worldName;
    private final ConfigRepository configRepository;
    private final SpawnService spawnService;
    private final SpawnTracker spawnTracker;

    private ScheduledFuture<?> loopTask;
    private GamePhase phase = GamePhase.BREAK;
    private int waveIndex;
    private int breakRemainingSeconds;
    private int elapsedSeconds;
    private int nextTickIndex;
    private int lastFiredTickIndex;
    private WaveDefinition activeWave;

    public GameDirector(String worldName, ConfigRepository configRepository, SpawnService spawnService, SpawnTracker spawnTracker) {
        this.worldName = worldName;
        this.configRepository = configRepository;
        this.spawnService = spawnService;
        this.spawnTracker = spawnTracker;
    }

    public synchronized void start() {
        if (this.loopTask != null) {
            return;
        }

        this.waveIndex = 0;
        this.activeWave = getGameConfig().getWaveByIndex(this.waveIndex);
        startBreakPhase();

        this.loopTask = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
            this::tick,
            LOOP_INTERVAL_SECONDS,
            LOOP_INTERVAL_SECONDS,
            TimeUnit.SECONDS
        );
    }

    public synchronized void stop() {
        if (this.loopTask != null) {
            this.loopTask.cancel(false);
            this.loopTask = null;
        }
    }

    public synchronized void forceEndBreak() {
        if (this.phase == GamePhase.BREAK) {
            this.breakRemainingSeconds = 0;
        }
    }

    public synchronized void onEnemyKilled(SpawnedEnemyData data) {
        this.spawnTracker.onEnemyKilled(data.getWaveId(), data.getTickIndex());
    }

    private synchronized void tick() {
        if (this.phase == GamePhase.BREAK) {
            this.breakRemainingSeconds -= LOOP_INTERVAL_SECONDS;
            if (this.breakRemainingSeconds <= 0) {
                startCombatPhase();
            }
            return;
        }

        if (this.phase == GamePhase.COMBAT) {
            this.elapsedSeconds += LOOP_INTERVAL_SECONDS;
            fireScheduledTicks();
            fireForcedTicks();

            if (isWaveComplete()) {
                advanceWave();
            }
        }
    }

    private void fireScheduledTicks() {
        List<WaveTickDefinition> ticks = this.activeWave.getTicks();
        while (this.nextTickIndex < ticks.size()) {
            WaveTickDefinition nextTick = ticks.get(this.nextTickIndex);
            if (this.elapsedSeconds < nextTick.getDelaySeconds()) {
                return;
            }

            fireTick(this.nextTickIndex);
            this.nextTickIndex++;
        }
    }

    private void fireForcedTicks() {
        List<WaveTickDefinition> ticks = this.activeWave.getTicks();
        while (this.lastFiredTickIndex >= 0 && this.nextTickIndex < ticks.size()) {
            if (!this.spawnTracker.isTickCleared(this.activeWave.getId(), this.lastFiredTickIndex)) {
                return;
            }

            WaveTickDefinition nextTick = ticks.get(this.nextTickIndex);
            if (this.elapsedSeconds >= nextTick.getDelaySeconds()) {
                return;
            }

            fireTick(this.nextTickIndex);
            this.nextTickIndex++;
        }
    }

    private void fireTick(int tickIndex) {
        WaveTickDefinition tick = this.activeWave.getTicks().get(tickIndex);
        this.spawnService.spawnTick(this.activeWave.getId(), tickIndex, tick);
        this.lastFiredTickIndex = tickIndex;
    }

    private boolean isWaveComplete() {
        boolean allTicksFired = this.nextTickIndex >= this.activeWave.getTicks().size();
        return allTicksFired && this.spawnTracker.isWaveCleared(this.activeWave.getId());
    }

    private void advanceWave() {
        this.waveIndex += 1;
        this.activeWave = getGameConfig().getWaveByIndex(this.waveIndex);
        startBreakPhase();
    }

    private void startBreakPhase() {
        this.phase = GamePhase.BREAK;
        this.breakRemainingSeconds = this.activeWave.getBreakDurationSeconds();
        this.elapsedSeconds = 0;
        this.nextTickIndex = 0;
        this.lastFiredTickIndex = -1;
        this.spawnTracker.clearWave(this.activeWave.getId());
    }

    private void startCombatPhase() {
        this.phase = GamePhase.COMBAT;
        this.elapsedSeconds = 0;
        this.nextTickIndex = 0;
        this.lastFiredTickIndex = -1;
    }

    private GameConfig getGameConfig() {
        return this.configRepository.getGameConfig(this.worldName);
    }
}
