package MiniCash.miniCashMythicSkill;

import MiniCash.miniCashMythicSkill.mechanic.*;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniCashMythicSkill extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("MiniCashMythicSkill 起動完了!");

        getServer().getPluginManager().registerEvents(new Event(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event) {
        if (event.getMechanicName().equalsIgnoreCase("mtestmessage")) {
            event.register(new TestMessage(event.getConfig()));
        }
        if (event.getMechanicName().equalsIgnoreCase("randomLightning")) {
            event.register(new RandomLightning(this,event.getConfig()));
        }
        if (event.getMechanicName().equalsIgnoreCase("mstun")) {
            event.register(new Stun(this,event.getConfig()));
        }
        if (event.getMechanicName().equalsIgnoreCase("linkplayers")) {
            event.register(new LinkPlayerMechanic(this, event.getConfig()));
        }
        if (event.getMechanicName().equalsIgnoreCase("breakshild")) {
            event.register(new BreakShild(this,event.getConfig()));
        }
    }


}
