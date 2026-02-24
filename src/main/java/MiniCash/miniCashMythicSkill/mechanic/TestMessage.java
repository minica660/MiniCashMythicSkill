package MiniCash.miniCashMythicSkill.mechanic;

import MiniCash.miniCashMythicSkill.MiniCashMythicSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class TestMessage implements ITargetedEntitySkill {
    protected final String message;
    protected final int damage;

    public TestMessage(MythicLineConfig config) {
        this.message = config.getString(new String[]{"message", "msg"},"???");
        this.damage = config.getInteger(new String[]{"damage", "d"}, 10);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity target) {
        if (target.getBukkitEntity() instanceof LivingEntity) {
            LivingEntity livingtarget = (LivingEntity) target.getBukkitEntity();
            //メッセージ送信
            target.getBukkitEntity().sendMessage(message);
            //ダメージ
            Bukkit.getScheduler().runTask(MiniCashMythicSkill.getProvidingPlugin(getClass()), () -> {
                livingtarget.damage(damage);
            });
            return SkillResult.SUCCESS;
        }

        //if分でのチェック結果がfalseだった場合
        return SkillResult.CONDITION_FAILED;
    }
}
