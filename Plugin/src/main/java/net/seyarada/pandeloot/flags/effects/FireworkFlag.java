package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.utils.ColorUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

@FlagEffect(id="firework", description="Spawns a firework rocket")
public class FireworkFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity entity, ItemDropMeta meta) {
		Location loc = entity.getLocation();

		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
		FireworkMeta fwm = fw.getFireworkMeta();

		int power = meta.getInt("power");
		Color color = ColorUtils.getRGB(meta.getOrDefault("value", "WHITE"));
		boolean flicker = meta.getBoolean("flicker");
		boolean trail = meta.getBoolean("trail");
		boolean instant = meta.getBoolean("instant");
		FireworkEffect.Type type = FireworkEffect.Type.valueOf(meta.getOrDefault("type", "BALL").toUpperCase());

		if(power!=0) fwm.setPower(power);
		fwm.addEffect(FireworkEffect.builder()
				.withColor(color)
				.flicker(flicker)
				.trail(trail)
				.with(type)
				.build());
		fw.setFireworkMeta(fwm);
		if(instant) fw.detonate();
	}
}
