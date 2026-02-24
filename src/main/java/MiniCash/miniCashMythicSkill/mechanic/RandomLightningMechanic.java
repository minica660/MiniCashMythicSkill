package MiniCash.miniCashMythicSkill.mechanic;

import MiniCash.miniCashMythicSkill.MiniCashMythicSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLightningMechanic implements ITargetedLocationSkill {
    private final int amount;
    private final int radius;
    private final int delay;
    private final int time;
    private final int damage;

    public RandomLightningMechanic(MythicLineConfig config) {

        this.amount = config.getInteger(new String[]{"amount", "am"}, 10);  // 何か所行うか
        this.radius = config.getInteger(new String[]{"radius", "r"}, 10);   //どのくらいの範囲ランダム
        this.delay = config.getInteger(new String[]{"delay", "dl"}, 10);    // 雷までの遅延時間
        this.time = config.getInteger(new String[]{"time", "t"}, 10);       // 燃焼時間
        this.damage = config.getInteger(new String[]{"damage", "dm"}, 10);  //ダメージ量
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation target) {
        World world = Bukkit.getWorld(target.getWorld().getName());
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());

        if (world == null){
            return SkillResult.CONDITION_FAILED;
        }

        Random random = new Random();
        AbstractEntity caster = skillMetadata.getCaster().getEntity();
        // スキル実行者の座標
        AbstractLocation casterLoc = caster.getLocation();

        for (int i = 0; i < amount; i++) {

            //座標確定
            double x = casterLoc.getX() + ((Math.random() * 2 - 1) * radius);
            double y = casterLoc.getY() + ((Math.random() * 2 - 1) * radius);
            double z = casterLoc.getZ() + ((Math.random() * 2 - 1) * radius);

            Location location = new Location(world, x, y, z);
            Bukkit.getScheduler().runTask(MiniCashMythicSkill.getProvidingPlugin(getClass()), () -> {

                Color whiteColor = Color.WHITE;

                world.spawnParticle(Particle.FLASH, location, 1, 0.0, 0.0, 0.0, 0.0, whiteColor);
               new org.bukkit.scheduler.BukkitRunnable() {
                    @Override
                    public void run() {
                        //雷召喚(偽の雷)
                        world.strikeLightningEffect(location);

                        // 周囲へのダメージ
                        world.getNearbyEntities(location, 0.5, 2, 0.5).forEach(entity -> {
                            if (entity instanceof LivingEntity) {
                                LivingEntity livingEntity = (LivingEntity) entity;
                                //ダメージ
                                livingEntity.damage(damage);
                                //燃焼
                                livingEntity.setFireTicks(time);
                            }
                        });
                    }
                }.runTaskLater(plugin, delay);
            });
        }

        return SkillResult.SUCCESS;
    }
}
