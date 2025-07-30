package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.trackers.DamageBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class MythicMobsListener implements Listener {

    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event)	{
        try {
            switch (event.getMechanicName().toLowerCase()) {
                case "pandeloot", "ploot" -> event.register(new MythicMobsMechanic(event.getConfig()));
            }
        } catch (Exception e) {
            // Handle MythicMobs compatibility issues
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawn(MythicMobSpawnEvent e) {
        try {
            if(e.getEntity() instanceof LivingEntity entity) {
                MythicConfig config = e.getMobType().getConfig();
                boolean shouldTrack = config.getStringList("Rewards").size()>0;
                if(!shouldTrack) shouldTrack = config.getBoolean("Options.ScoreHologram");
                if(!shouldTrack) shouldTrack = config.getBoolean("Options.ScoreMessage");

                if(shouldTrack) new DamageBoard(entity);
            }
        } catch (Exception ex) {
            // Handle MythicMobs compatibility issues
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(MythicMobDeathEvent e) {
        try {
            UUID mob = e.getEntity().getUniqueId();
            if(!DamageBoard.contains(mob)) return;

            MythicConfig config = e.getMobType().getConfig();
            boolean scoreMessage = config.getBoolean("Options.ScoreMessage");
            boolean scoreHologram = config.getBoolean("Options.ScoreHologram");

            List<String> strings = e.getMobType().getConfig().getStringList("Rewards");

            Logger.record();
            DamageBoard damageBoard = DamageBoard.get(mob);
            damageBoard.compileInformation();

            // แสดง hologram เพียงครั้งเดียวสำหรับทุกคน
            boolean hologramDisplayed = false;

            for(UUID uuid : damageBoard.playersAndDamage.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                LootDrop lootDrop = new LootDrop(strings, player, e.getEntity().getLocation())
                        .setDamageBoard(damageBoard)
                        .setSourceEntity(e.getEntity())
                        .build();

                // แสดง hologram เฉพาะครั้งแรกเท่านั้น
                if(scoreHologram && !hologramDisplayed) {
                    lootDrop.displayScoreHolograms();
                    hologramDisplayed = true;
                }
                if(scoreMessage) lootDrop.displayScoreMessage();

                lootDrop.drop();
            }
            Logger.print();

            DamageBoard.remove(mob);
        } catch (Exception ex) {
            // Handle VaultSupport and other MythicMobs compatibility issues
            Logger.userWarning("MythicMobs compatibility error: " + ex.getClass().getSimpleName());
        }
    }

}
