package MiniCash.miniCashMythicSkill.mechanic;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;

import java.io.File;

public class T extends SkillMechanic implements ITargetedLocationSkill {
    public T(SkillExecutor manager, File file, String line, MythicLineConfig mlc, int interval) {
        super(manager, file, line, mlc, interval);
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation abstractLocation) {
        return null;
    }
}
