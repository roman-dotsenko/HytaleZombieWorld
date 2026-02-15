package dev.hytalemodding.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassConfig {
    private final String classId;
    private final String name;
    private final String requirementStatId;
    private final int requirementLevel;
    private final List<String> perkIds;

    public ClassConfig(String classId, String name, String requirementStatId, int requirementLevel, List<String> perkIds) {
        this.classId = classId;
        this.name = name;
        this.requirementStatId = requirementStatId;
        this.requirementLevel = requirementLevel;
        this.perkIds = Collections.unmodifiableList(new ArrayList<>(perkIds));
    }

    public String getClassId() {
        return this.classId;
    }

    public String getName() {
        return this.name;
    }

    public String getRequirementStatId() {
        return this.requirementStatId;
    }

    public int getRequirementLevel() {
        return this.requirementLevel;
    }

    public List<String> getPerkIds() {
        return this.perkIds;
    }
}
