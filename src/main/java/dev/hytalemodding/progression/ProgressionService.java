package dev.hytalemodding.progression;

import dev.hytalemodding.config.ClassConfig;
import dev.hytalemodding.config.ConfigRepository;
import dev.hytalemodding.config.StatConfig;
import dev.hytalemodding.state.PlayerClassData;
import dev.hytalemodding.state.PlayerStatsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressionService {
    public static final String STAT_MELEE_ATTACK_POWER = "melee_attack_power";
    public static final String STAT_MELEE_ATTACK_SPEED = "melee_attack_speed";

    private final Map<String, StatConfig> statConfigById = new HashMap<>();
    private final List<ClassConfig> classConfigs;

    public ProgressionService(ConfigRepository configRepository) {
        for (StatConfig config : configRepository.getStatConfigs()) {
            this.statConfigById.put(config.getStatId(), config);
        }
        this.classConfigs = configRepository.getClassConfigs();
    }

    public boolean incrementStat(PlayerStatsData stats, String statId) {
        StatConfig config = this.statConfigById.get(statId);
        if (config == null) {
            return false;
        }

        int current = getStatLevel(stats, statId);
        if (current >= config.getMaxLevel()) {
            return false;
        }

        setStatLevel(stats, statId, current + 1);
        return true;
    }

    public boolean canIncrementStat(PlayerStatsData stats, String statId) {
        StatConfig config = this.statConfigById.get(statId);
        if (config == null) {
            return false;
        }

        int current = getStatLevelInternal(stats, statId);
        return current < config.getMaxLevel();
    }

    public List<ClassConfig> getUnlockableClasses(PlayerStatsData stats) {
        List<ClassConfig> unlockable = new ArrayList<>();
        for (ClassConfig classConfig : this.classConfigs) {
            int level = getStatLevel(stats, classConfig.getRequirementStatId());
            if (level >= classConfig.getRequirementLevel()) {
                unlockable.add(classConfig);
            }
        }

        return unlockable;
    }

    public boolean tryAssignClass(PlayerClassData classData, ClassConfig classConfig) {
        if (classData == null || classConfig == null) {
            return false;
        }

        if (classData.getClassId() != null && !classData.getClassId().isBlank()) {
            return false;
        }

        classData.setClassId(classConfig.getClassId());
        return true;
    }

    public int getStatLevel(PlayerStatsData stats, String statId) {
        return getStatLevelInternal(stats, statId);
    }

    public double getAttackPowerBonus(PlayerStatsData stats) {
        return getIncrementBonus(stats, STAT_MELEE_ATTACK_POWER, true);
    }

    public double getAttackSpeedBonus(PlayerStatsData stats) {
        return getIncrementBonus(stats, STAT_MELEE_ATTACK_SPEED, false);
    }

    private int getStatLevelInternal(PlayerStatsData stats, String statId) {
        if (STAT_MELEE_ATTACK_POWER.equals(statId)) {
            return stats.getMeleeAttackPower();
        }
        if (STAT_MELEE_ATTACK_SPEED.equals(statId)) {
            return stats.getMeleeAttackSpeed();
        }

        return 0;
    }

    private void setStatLevel(PlayerStatsData stats, String statId, int level) {
        if (STAT_MELEE_ATTACK_POWER.equals(statId)) {
            stats.setMeleeAttackPower(level);
        } else if (STAT_MELEE_ATTACK_SPEED.equals(statId)) {
            stats.setMeleeAttackSpeed(level);
        }
    }

    private double getIncrementBonus(PlayerStatsData stats, String statId, boolean forDamage) {
        StatConfig config = this.statConfigById.get(statId);
        if (config == null) {
            return 0.0;
        }

        int level = getStatLevelInternal(stats, statId);
        double total = 0.0;
        for (dev.hytalemodding.config.StatIncrement increment : config.getIncrements()) {
            if (increment.getLevel() <= level) {
                total += forDamage ? increment.getBonusDamage() : increment.getBonusAttackSpeed();
            }
        }

        return total;
    }
}
