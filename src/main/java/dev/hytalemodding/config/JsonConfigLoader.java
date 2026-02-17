package dev.hytalemodding.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonConfigLoader {
    private final Gson gson = new Gson();
    private final String basePath;

    public JsonConfigLoader(String basePath) {
        this.basePath = basePath.endsWith("/") ? basePath : basePath + "/";
    }

    public GameConfig loadGameConfig() {
        WaveConfigFile file = loadResource("waves.json", WaveConfigFile.class);
        if (file == null || file.waves == null) {
            return null;
        }

        List<WaveDefinition> waves = new ArrayList<>();
        for (WaveEntry entry : file.waves) {
            waves.add(toWaveDefinition(entry));
        }

        return new GameConfig(waves);
    }

    public List<StatConfig> loadStatConfigs() {
        StatConfigFile file = loadResource("stats.json", StatConfigFile.class);
        if (file == null || file.stats == null) {
            return null;
        }

        List<StatConfig> stats = new ArrayList<>();
        for (StatEntry entry : file.stats) {
            List<StatIncrement> increments = new ArrayList<>();
            if (entry.increments != null) {
                for (StatIncrementEntry inc : entry.increments) {
                    increments.add(new StatIncrement(inc.level, inc.bonusDamage, inc.bonusAttackSpeed));
                }
            }
            stats.add(new StatConfig(entry.statId, entry.baseCost, entry.costMultiplier, entry.maxLevel, increments));
        }

        return stats;
    }

    public List<ClassConfig> loadClassConfigs() {
        ClassConfigFile file = loadResource("classes.json", ClassConfigFile.class);
        if (file == null || file.classes == null) {
            return null;
        }

        List<ClassConfig> classes = new ArrayList<>();
        for (ClassEntry entry : file.classes) {
            String requirementStat = entry.requirements != null ? entry.requirements.statId : "";
            int requirementLevel = entry.requirements != null ? entry.requirements.minLevel : 0;
            List<String> perkIds = entry.perks != null ? entry.perksToIds() : Collections.emptyList();
            classes.add(new ClassConfig(entry.classId, entry.name, requirementStat, requirementLevel, perkIds));
        }

        return classes;
    }

    public List<EnemyRewardConfig> loadEnemyRewards() {
        RewardConfigFile file = loadResource("rewards.json", RewardConfigFile.class);
        if (file == null || file.rewards == null) {
            return null;
        }

        List<EnemyRewardConfig> rewards = new ArrayList<>();
        for (RewardEntry entry : file.rewards) {
            rewards.add(new EnemyRewardConfig(entry.mobId, entry.coins));
        }

        return rewards;
    }

    private WaveDefinition toWaveDefinition(WaveEntry entry) {
        String id = resolveWaveId(entry.waveId);
        List<WaveTickDefinition> ticks = new ArrayList<>();
        if (entry.ticks != null) {
            for (WaveTickEntry tick : entry.ticks) {
                ticks.add(toWaveTickDefinition(tick));
            }
        }

        return new WaveDefinition(
            id,
            entry.displayName,
            entry.breakDuration,
            ticks,
            entry.bossWave
        );
    }

    private WaveTickDefinition toWaveTickDefinition(WaveTickEntry entry) {
        List<SpawnDefinition> spawns = new ArrayList<>();
        if (entry.spawns != null) {
            for (SpawnEntry spawn : entry.spawns) {
                spawns.add(new SpawnDefinition(spawn.mobId, spawn.count));
            }
        }

        return new WaveTickDefinition(entry.delayFromWaveStart, spawns);
    }

    private <T> T loadResource(String filename, Class<T> type) {
        String path = this.basePath + filename;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                return null;
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                return this.gson.fromJson(reader, type);
            }
        } catch (IOException | JsonParseException ex) {
            return null;
        }
    }

    private String resolveWaveId(JsonElement waveIdElement) {
        if (waveIdElement == null || waveIdElement.isJsonNull()) {
            return "wave_0";
        }

        if (waveIdElement.isJsonPrimitive()) {
            if (waveIdElement.getAsJsonPrimitive().isNumber()) {
                return "wave_" + waveIdElement.getAsInt();
            }

            String value = waveIdElement.getAsString();
            if (!value.isBlank()) {
                return value;
            }
        }

        return "wave_0";
    }

    private static class WaveConfigFile {
        private List<WaveEntry> waves;
    }

    private static class WaveEntry {
        @SerializedName("wave_id")
        private JsonElement waveId;
        @SerializedName("display_name")
        private String displayName;
        @SerializedName("break_duration")
        private int breakDuration;
        private List<WaveTickEntry> ticks;
        @SerializedName("is_boss_wave")
        private boolean bossWave;
    }

    private static class WaveTickEntry {
        @SerializedName("delay_from_wave_start")
        private int delayFromWaveStart;
        private List<SpawnEntry> spawns;
    }

    private static class SpawnEntry {
        @SerializedName("mob_id")
        private String mobId;
        private int count;
    }

    private static class StatConfigFile {
        private List<StatEntry> stats;
    }

    private static class StatEntry {
        @SerializedName("stat_id")
        private String statId;
        @SerializedName("base_cost")
        private int baseCost;
        @SerializedName("cost_multiplier")
        private double costMultiplier;
        @SerializedName("max_level")
        private int maxLevel;
        private List<StatIncrementEntry> increments;
    }

    private static class StatIncrementEntry {
        private int level;
        @SerializedName("bonus_damage")
        private double bonusDamage;
        @SerializedName("bonus_attack_speed")
        private double bonusAttackSpeed;
    }

    private static class ClassConfigFile {
        private List<ClassEntry> classes;
    }

    private static class ClassEntry {
        @SerializedName("class_id")
        private String classId;
        private String name;
        private RequirementEntry requirements;
        private List<PerkEntry> perks;

        private List<String> perksToIds() {
            List<String> ids = new ArrayList<>();
            for (PerkEntry perk : this.perks) {
                if (perk.id != null && !perk.id.isBlank()) {
                    ids.add(perk.id);
                }
            }
            return ids;
        }
    }

    private static class RequirementEntry {
        @SerializedName("stat_id")
        private String statId;
        @SerializedName("min_level")
        private int minLevel;
    }

    private static class PerkEntry {
        private String id;
    }

    private static class RewardConfigFile {
        private List<RewardEntry> rewards;
    }

    private static class RewardEntry {
        @SerializedName("mob_id")
        private String mobId;
        private int coins;
    }
}
