package com.shynieke.statues.compat.patchouli;

import net.minecraft.world.entity.player.Player;

public class PatchouliCompat {
	public static void convertBook(Player playerIn) {
//		Item guideBook = BuiltInRegistries.ITEM.get(new ResourceLocation("patchouli", "guide_book"));
//		if (guideBook != null) {
//			playerIn.getMainHandItem().shrink(1);
//			ItemStack patchouliBook = new ItemStack(guideBook);
//			CompoundTag tag = patchouliBook.getOrCreateTag();
//			tag.putString("patchouli:book", "statues:statues");
//			Level level = playerIn.level();
//			if (!playerIn.addItem(patchouliBook)) {
//				level.addFreshEntity(new ItemEntity(level, playerIn.getX(), playerIn.getY(), playerIn.getZ(), patchouliBook));
//			}
//			level.playSound(null, playerIn.blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.MASTER, 0.5F, 1.0F);
//		}
	}
}
