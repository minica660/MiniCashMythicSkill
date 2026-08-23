package MiniCash.miniCashMythicSkill.mechanic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LinkPlayerMechanic implements ITargetedEntitySkill {

    private final JavaPlugin plugin;
    private final double searchRadius;
    private final double maxDistance;
    private final int durationTicks;
    private final int intervalTicks;
    private final int damage;

    public LinkPlayerMechanic(JavaPlugin plugin , MythicLineConfig config) {

        this.plugin = plugin;

        this.searchRadius = config.getDouble(new String[]{"radius", "r"}, 20.0);
        this.maxDistance = config.getDouble(new String[]{"maxdistance", "md"}, 5.0);
        this.durationTicks = config.getInteger(new String[]{"duration", "d"}, 200);
        this.intervalTicks = config.getInteger(new String[]{"interval", "i"}, 5);
        this.damage = config.getInteger(new String[]{"damage"}, 10);

    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {


        Bukkit.getScheduler().runTask(plugin, () -> {

            Entity caster = skillMetadata.getCaster().getEntity().getBukkitEntity();

            List<Player> nearbyPlayers = new ArrayList<>();

            for (Entity entity : caster.getNearbyEntities(searchRadius, searchRadius, searchRadius)) {
                if (entity instanceof Player player) {
                    nearbyPlayers.add(player);
                }
            }

            if (nearbyPlayers.size() < 2) {
                caster.sendMessage("対象のプレイヤーが2人揃っていません");
                return;
            }

            Player target1 = nearbyPlayers.get(0);
            Player target2 = nearbyPlayers.get(1);


            new BukkitRunnable() {
                int elapsed = 0;

                @Override
                public void run() {

                    if (elapsed >= durationTicks || !target1.isOnline() || !target2.isOnline() || target1.isDead() || target2.isDead()) {
                        this.cancel();
                        return;
                    }

                    Location location1 = target1.getLocation().add(0, 1.0, 0);
                    Location location2 = target2.getLocation().add(0, 1.0, 0);

                    // 2人間の距離を計算
                    double distance = location1.distance(location2);

                    // パーティクルで線のように結ぶ
                    drawParticleLine(location1, location2);

                    // プレイヤー同士の距離が指定した距離を超えていたらダメージを与える
                    if (distance > maxDistance) {
                        target1.damage(damage, caster);
                        target2.damage(damage, caster);
                    }

                    elapsed += intervalTicks;
                }
            }.runTaskTimer(plugin, 0L, intervalTicks);


        });

        return SkillResult.SUCCESS;
    }


    // 2点間を結ぶ
    private void drawParticleLine(Location point1, Location point2) {
        double distance = point1.distance(point2);
        Vector direction = point2.toVector().subtract(point1.toVector()).normalize();

        for (double d = 0; d < distance; d += 0.5) {
            Location point = point1.clone().add(direction.clone().multiply(d));
            point.getWorld().spawnParticle(Particle.FLAME, point, 1, 0, 0, 0, 0);
        }
    }
}
