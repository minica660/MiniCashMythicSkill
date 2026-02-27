package MiniCash.miniCashMythicSkill.mechanic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Stun implements ITargetedEntitySkill {

    private final int time;
    private final double knockback;
    private final double knockbackY;


    public Stun(MythicLineConfig config) {

        this.time = config.getInteger(new String[]{"time", "t"}, 5);    //スタンさせる時間
        this.knockback = config.getInteger(new String[]{"knockback"}, 10);  //ノックバックさせる距離
        this.knockbackY = config.getInteger(new String[]{"knockbacky"}, 10);  //ノックバックさせるY方向の距離

    }


    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity target) {


        if (target.getBukkitEntity() instanceof Player) {

            final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());

            Player player = (Player) target.getBukkitEntity();

            Bukkit.getScheduler().runTask(plugin, () -> {

                //吹っ飛ばし処理
                Vector direction = player.getLocation().getDirection();

                direction.multiply(-1);

                player.setVelocity(new Vector(direction.getX() * knockback, knockbackY, direction.getZ() * knockback));

                Bukkit.getScheduler().runTaskLater(plugin, () -> {


                    Spider spider = player.getWorld().spawn(player.getLocation(), Spider.class, entity -> {
                        entity.setInvisible(true);
                        entity.setInvulnerable(true);
                        entity.setSilent(true);
                        entity.setAI(false);
                    });

                    player.setGameMode(GameMode.SPECTATOR);
                    player.setSpectatorTarget(spider);


                    BukkitTask watchTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                        if (!player.isOnline() || player.getGameMode() != GameMode.SPECTATOR) return;

                        // もし視点がクモから外れていたら、強制的に戻す
                        if (player.getSpectatorTarget() == null || !player.getSpectatorTarget().equals(spider)) {
                            player.setSpectatorTarget(spider);
                        }
                    }, 1L, 1L);


                    // 解除
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {

                        if (player.isOnline()) {
                            player.setSpectatorTarget(null); // 視点解除
                            player.setGameMode(GameMode.SURVIVAL); // サバイバルに戻す
                        }
                        spider.remove();
                        watchTask.cancel();

                    }, time);


                },20L);

            });

            return SkillResult.SUCCESS;

        }


        return SkillResult.CONDITION_FAILED;
    }





}
