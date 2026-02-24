package MiniCash.miniCashMythicSkill;

import MiniCash.miniCashMythicSkill.mechanic.RandomLightningMechanic;
import MiniCash.miniCashMythicSkill.mechanic.TestMessage;
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
            event.register(new RandomLightningMechanic(event.getConfig()));
        }
    }


}
