package dev.hytalemodding.config;

import java.util.List;

public class DefaultConfigFactory {
    public GameConfig createDefaultGameConfig() {
        WaveTickDefinition tick0 = new WaveTickDefinition(0, List.of(
            new SpawnDefinition("Skeleton", 5),
            new SpawnDefinition("Skeleton_Archer", 2)
        ));
        WaveTickDefinition tick1 = new WaveTickDefinition(15, List.of(
            new SpawnDefinition("Skeleton", 7)
        ));

        WaveDefinition wave1 = new WaveDefinition(
            "wave_1",
            "First Stand",
            30,
            List.of(tick0, tick1),
            false
        );

        return new GameConfig(List.of(wave1));
    }

    public List<StatConfig> createDefaultStatConfigs() {
        return List.of(
            new StatConfig(
                "melee_attack_power",
                100,
                1.5,
                10,
                List.of(
                    new StatIncrement(1, 2.0, 0.0),
                    new StatIncrement(5, 5.0, 0.0)
                )
            ),
            new StatConfig(
                "melee_attack_speed",
                110,
                1.4,
                10,
                List.of(
                    new StatIncrement(1, 0.0, 0.05),
                    new StatIncrement(5, 0.0, 0.10)
                )
            )
        );
    }

    public List<ClassConfig> createDefaultClassConfigs() {
        return List.of(
            new ClassConfig("berserker", "Berserker", "melee_attack_power", 5, List.of("adrenaline")),
            new ClassConfig("slayer", "Slayer", "melee_attack_speed", 5, List.of("dash"))
        );
    }

    public List<EnemyRewardConfig> createDefaultEnemyRewards() {
        return List.of(
            new EnemyRewardConfig("Skeleton", 10),
            new EnemyRewardConfig("Skeleton_Archer", 20)
        );
    }
}
