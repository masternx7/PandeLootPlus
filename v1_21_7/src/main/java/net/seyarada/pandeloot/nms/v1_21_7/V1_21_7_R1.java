package net.seyarada.pandeloot.nms.v1_21_7;

import net.seyarada.pandeloot.nms.NMSMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class V1_21_7_R1 implements NMSMethods {

    @Override
    public List<Entity> hologram(int duration, Location location, Player player, List<String> text, JavaPlugin plugin) {
        List<Entity> holograms = new ArrayList<>();
        double lY = location.getY() + 1.2;
        
        Collections.reverse(text);
        
        for (String msg : text) {
            if(msg == null) continue;
            if(msg.isEmpty()) {
                lY += 0.22;
                continue;
            }
            lY += 0.22;
            
            // Use ArmorStand for reliable hologram display
            ArmorStand hologram = location.getWorld().spawn(
                new Location(location.getWorld(), location.getX(), lY, location.getZ()), 
                ArmorStand.class
            );
            hologram.setCustomName(msg);
            hologram.setCustomNameVisible(true);
            hologram.setVisible(false);
            hologram.setGravity(false);
            hologram.setMarker(true);
            hologram.setSmall(true);
            
            holograms.add(hologram);
            
            if(duration > 0) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (hologram.isValid()) {
                        hologram.remove();
                    }
                }, duration);
            }
        }
        return holograms;
    }

    @Override
    public void destroyEntity(int toBeDestroyed, Entity player) {
        // Simple implementation - find entity by ID and remove
        for (Entity entity : player.getWorld().getEntities()) {
            if (entity.getEntityId() == toBeDestroyed) {
                entity.remove();
                break;
            }
        }
    }

    @Override
    public void displayToast(Player player, String title, String frame, ItemStack icon) {
        // Simple implementation using chat message
        player.sendMessage("§6[" + title + "]");
    }

    @Override
    public void injectPlayer(Player player) {
        // No implementation needed for compatibility version
    }

    @Override
    public void removePlayer(Player player) {
        // No implementation needed for compatibility version
    }

    @Override
    public void updateHologramPosition(double x, double y, double z, Entity hologram, Player player) {
        if (hologram != null && hologram.isValid()) {
            hologram.teleport(new Location(hologram.getWorld(), x, y, z));
        }
    }

    @Override
    public ItemStack getCustomTextureHead(ItemStack head, String value) {
        if (head.getType() != Material.PLAYER_HEAD) {
            return head;
        }
        
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta == null) return head;
        
        if (value == null) {
            meta.setOwner("PandemoniumHK");
        } else {
            // For compatibility, just set a player name
            meta.setOwner("PandemoniumHK");
        }
        
        head.setItemMeta(meta);
        return head;
    }
}
