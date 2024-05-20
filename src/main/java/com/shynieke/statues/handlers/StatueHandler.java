package com.shynieke.statues.handlers;

import com.shynieke.statues.datacomponent.StatueStats;
import com.shynieke.statues.fakeplayer.StatueFakePlayer;
import com.shynieke.statues.items.StatueBlockItem;
import com.shynieke.statues.registry.StatueDataComponents;
import com.shynieke.statues.storage.StatueSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class StatueHandler {
	@SubscribeEvent
	public void onKill(LivingDeathEvent event) {
		final LivingEntity target = event.getEntity();
		Entity source = event.getSource().getEntity();
		if (source instanceof Player player) {
			Inventory inventory = player.getInventory();
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				ItemStack stack = inventory.getItem(i);
				if (stack.getCount() == 1 && stack.getItem() instanceof StatueBlockItem statue &&
						upgraded(stack) && statue.matchesEntity(target)) {
					increaseKillCounter(stack);
					break;
				}
			}
		}
	}

	private boolean upgraded(ItemStack stack) {
		return stack.getOrDefault(StatueDataComponents.UPGRADED, false);
	}

	private void increaseKillCounter(ItemStack stack) {
		StatueStats stats = stack.getOrDefault(StatueDataComponents.STATS, StatueStats.empty());
		stats.setKillCount(stats.killCount() + 1);
		int level = getLevel(stats.killCount());
		if (stats.level() != level) {
			stats.setLevel(level);
			stats.setUpgradeSlots(stats.upgradeSlots() + 1);
		}
		stack.set(StatueDataComponents.STATS, stats);
	}

	public int getLevel(int killedMobs) {
		if (killedMobs > 0) {
			int level = (int) Math.floor(killedMobs / 10.0D);
			if (level <= 16) {
				return level;
			}
			return 16;
		}
		return 0;
	}

	@SubscribeEvent
	public void onLivingSpawnEvent(FinalizeSpawnEvent event) {
		MobSpawnType spawnReason = event.getSpawnType();
		if (spawnReason == MobSpawnType.NATURAL || spawnReason == MobSpawnType.REINFORCEMENT || spawnReason == MobSpawnType.EVENT) {
			Mob mob = event.getEntity();
			if (StatueSavedData.get().isDespawnerNearby(mob.level().dimension(), mob.blockPosition(), 32)) {
//				Statues.LOGGER.info(mob.blockPosition() + " Spawn cancelled by a Despawner upgraded Statue " + mob.getType());
				event.setSpawnCancelled(true);
			}
		}
	}


	@SubscribeEvent
	public void onLevelUnload(final LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			StatueFakePlayer.unload(serverLevel);
		}
	}
}
