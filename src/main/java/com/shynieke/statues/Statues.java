package com.shynieke.statues;

import com.shynieke.statues.client.ClientHandler;
import com.shynieke.statues.config.StatuesConfig;
import com.shynieke.statues.handlers.DropHandler;
import com.shynieke.statues.handlers.FishHandler;
import com.shynieke.statues.handlers.SpecialHandler;
import com.shynieke.statues.handlers.TraderHandler;
import com.shynieke.statues.init.StatueBlockEntities;
import com.shynieke.statues.init.StatueEntities;
import com.shynieke.statues.init.StatueRegistry;
import com.shynieke.statues.init.StatueSerializers;
import com.shynieke.statues.init.StatueSounds;
import com.shynieke.statues.items.CustomSpawnEggItem;
import com.shynieke.statues.packets.StatuesNetworking;
import com.shynieke.statues.recipes.StatueLootList;
import com.shynieke.statues.tiles.PlayerBlockEntity;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class Statues {
	public static final Logger LOGGER = LogManager.getLogger();

	public Statues() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StatuesConfig.commonSpec);
		eventBus.register(StatuesConfig.class);

		eventBus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);

		StatueRegistry.ENTITIES.register(eventBus);
		StatueRegistry.BLOCKS.register(eventBus);
		StatueRegistry.ITEMS.register(eventBus);
		StatueBlockEntities.BLOCK_ENTITIES.register(eventBus);
		StatueSounds.SOUND_EVENTS.register(eventBus);

		eventBus.addListener(StatueEntities::registerEntityAttributes);

//		if (ModList.get().isLoaded("curios")) {
//			eventBus.addListener(CuriosCompat::sendImc);
//		}

//		MinecraftForge.EVENT_BUS.register(new InventoryHandler());
		MinecraftForge.EVENT_BUS.register(new FishHandler());
		MinecraftForge.EVENT_BUS.register(new TraderHandler());
		MinecraftForge.EVENT_BUS.register(new DropHandler());
		MinecraftForge.EVENT_BUS.register(new SpecialHandler()); //Used for the Etho Statue

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::doClientStuff);
			eventBus.addListener(ClientHandler::registerBlockColors);
		});

	}

	private void setup(final FMLCommonSetupEvent event) {
		StatueLootList.initializeStatueLoot();
		EntityDataSerializers.registerSerializer(StatueSerializers.OPTIONAL_GAME_PROFILE);
		StatueEntities.setupEntities();

		StatuesNetworking.init();

		event.enqueueWork(() -> {
				for(RegistryObject<Item> registryObject : StatueRegistry.ITEMS.getEntries()) {
					if(registryObject.get() instanceof CustomSpawnEggItem) {
						CustomSpawnEggItem spawnEgg = (CustomSpawnEggItem) registryObject.get();
						SpawnEggItem.BY_ID.put(spawnEgg.entityType.get(), spawnEgg);
					}
				}
			}
		);
	}

	public void serverAboutToStart(final FMLServerAboutToStartEvent event) {
		MinecraftServer server = event.getServer();
		PlayerBlockEntity.setProfileCache(server.getProfileCache());
		PlayerBlockEntity.setSessionService(server.getSessionService());
		PlayerBlockEntity.setMainThreadExecutor(event.getServer());
	}
}
