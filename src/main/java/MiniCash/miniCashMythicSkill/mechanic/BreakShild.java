package MiniCash.miniCashMythicSkill.mechanic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BreakShild implements ITargetedEntitySkill {

    private final JavaPlugin plugin;

    private final int tick;

    public BreakShild(JavaPlugin plugin,MythicLineConfig config) {

        this.plugin = plugin;
        this.tick = config.getInteger(new String[]{"tick"}, 10);  //クールタイムの時間(tick)
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {

        if (abstractEntity.getBukkitEntity() instanceof Player player) {


            Bukkit.getScheduler().runTask(plugin, () -> {

//                player.sendMessage("breakShildスキル発動！");

                if (player.isHandRaised()) {
                    player.clearActiveItem();
                }

                player.setCooldown(Material.SHIELD, tick);


//                plugin.getLogger().info("breakshild!:" + player.getName() + "..." + player.getCooldown(Material.SHIELD));


            });


            return SkillResult.SUCCESS;
        }
        return SkillResult.CONDITION_FAILED;
    }
}
