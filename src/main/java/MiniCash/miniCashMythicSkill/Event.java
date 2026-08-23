package MiniCash.miniCashMythicSkill;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Event implements Listener {

    private static final Set<UUID> stunedPlayers = new HashSet<>();

//    @EventHandler
//    public void onSpectate(PlayerTeleportEvent event) {
//
//        if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
//            event.setCancelled(true);
//        }
//    }




    @EventHandler
    public void onStopSpectate(PlayerStopSpectatingEntityEvent event) {

        if(stunedPlayers.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }

    }



    public static void addStunedPlayer(UUID uuid) {
        stunedPlayers.add(uuid);
    }

    public static void removeStunedPlayer(UUID uuid) {
        stunedPlayers.remove(uuid);
    }





}
