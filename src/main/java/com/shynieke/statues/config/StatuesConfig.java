package com.shynieke.statues.config;

import com.shynieke.statues.Statues;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.EnumValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class StatuesConfig {

	public enum EnumDeathSource {
		ALL,
		PLAYER,
		PLAYER_FAKEPLAYER
	}

	public static class Common {

		public final DoubleValue statueDropChance;
		public final EnumValue<EnumDeathSource> statueKillSource;

		public final BooleanValue playerDropsStatue;
		public final EnumValue<EnumDeathSource> playerStatueKillSource;
		public final DoubleValue playerStatueDropChance;
		public final BooleanValue playerCompass;

		public final BooleanValue statueBatSpawning;
		public final BooleanValue ancientCityLoot;
		public final DoubleValue ancientCityLootChance;
		public final IntValue statueCooldown;
		public final IntValue statueMinCooldown;
		public final IntValue statueSpeedUpgrade;

		public final BooleanValue requiresPower;
		public final BooleanValue powerDrain;
		public final IntValue itemPowerUsage;
		public final IntValue killPowerUsage;
		public final IntValue summonPowerUsage;
		public final IntValue despawnPowerUsage;

		//Lucky Players
		public final ConfigValue<List<? extends String>> lucky_players;

		//Messages
		public final ConfigValue<List<? extends String>> info_messages;

		Common(ModConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.translation("statues.configuration.general.title")
					.push("general");

			statueBatSpawning = builder
					.comment("Setting this to false disables the statue bat from spawning naturally (Default: true)")
					.define("statueBatSpawning", true);

			ancientCityLoot = builder
					.comment("When enabled this will allow Ancient City loot to contain statues (Default: true)")
					.define("ancientCityLoot", true);

			ancientCityLootChance = builder
					.comment("The chance of a statue appearing in ancient City loot (Default: 0.1)")
					.defineInRange("ancientCityLootChance", 0.1, 0, 1.0);

			statueCooldown = builder
					.comment("The amount of ticks before an upgraded statue becomes interact-able (Default: 200)")
					.defineInRange("statueCooldown", 200, 1, Integer.MAX_VALUE);

			statueMinCooldown = builder
					.comment("The minimum cooldown in ticks a statue can have after the speed has been subtracted (Default: 10)")
					.defineInRange("statuesMinCooldown", 10, 1, Integer.MAX_VALUE);

			statueSpeedUpgrade = builder
					.comment("The amount of ticks subtracted from the statue cooldown per level of Speed applied (Default: 20)")
					.defineInRange("statueSpeedUpgrade", 20, 1, Integer.MAX_VALUE);

			builder.pop();

			//Statue drop settings
			builder.comment("Statue drop Settings")
					.translation("statues.configuration.drops.title")
					.push("drops");

			statueDropChance = builder
					.comment("The drop chance of statues when statue drops is true (Default: 0.01)")
					.defineInRange("statueDropChance", 0.01, 0, 1.0);

			statueKillSource = builder
					.comment("Source of death that determines how the Statues drop (Default: \"PLAYER\") [player statue has it's own config option]",
							"ALL = All sources",
							"PLAYER = Player only",
							"PLAYER_FAKEPLAYER = Players / Fake players only")
					.defineEnum("statueKillSource", EnumDeathSource.PLAYER);

			builder.pop();

			//Player settings
			builder.comment("Player Statue Settings")
					.translation("statues.configuration.player.title")
					.push("player");

			playerDropsStatue = builder
					.comment("When true players will drop Player Statues when killed (Default: true)")
					.define("playerDropsStatue", true);

			playerStatueKillSource = builder
					.comment("Source of death that determines how the Player Statue drops (Default: \"PLAYER\") [player statue has it's own config option]",
							"ALL = All sources",
							"PLAYER = Player only",
							"PLAYER_FAKEPLAYER = Players / Fake players only")
					.defineEnum("playerDropsKillSource", EnumDeathSource.PLAYER);

			playerStatueDropChance = builder
					.comment("The drop chance of player statues when playerDropsStatue is enabled (Default: 1.0)")
					.defineInRange("statueDropChance", 1.0, 0, 1.0);

			playerCompass = builder
					.comment("When true Statues will add a player compass (Default: true)")
					.define("playerCompass", true);

			builder.pop();

			//Info messages
			builder.comment("Info messages")
					.translation("statues.configuration.info_messages.title")
					.push("info_messages");

			String[] messages = new String[]
					{
							"Statues is still in beta not all mobs have a statue yet",
							"Chickens are not royal, prove me wrong by placing the right statue on a royal looking block",
							"Undead stop burning in water, would a statue burn on a block that looks like water?",
							"Fun Fact: I was supposed to be a christmas special item, but plans got changed and now I am here",
							"Pigs love mud how will they react on a more sandy block",
							"Did you know chicken jockeys exist even in statues? go ahead try it out place a baby zombie statue on a chicken statue",
							"Huge thanks to Xisuma for helping create his statue",
							"Gone Fishing",
							"1-3 tall they seem, prickly is a bit mean, Give them a hat and see their true means",
							"Right click a player statue with a comparator to emit redstone when that player is online",
							"Right click a player statue with a compass to bound the compass to the last known location of the player",
							"A player and a creeper next to a pool of lave, throw in a shiny rock",
							"Rename a bee 'Trans Bee' to show support for trans people",
							"Rename a bee to a tropical name to find its name-sake",
							"Using a Statue Core on a player statue will make them grow, and will add more features to it",
							"The bat has returned, find them in caves near you, or in your local hellish dimension",
							"Check the pockets of your local wondering traders",
							"Looking for a player skin? Just rename the right statue"
					};

			info_messages = builder
					.comment("Adding lines / removing lines specifies what the informative statue can say")
					.defineListAllowEmpty(List.of("info_messages"), () -> List.of(messages), () -> "", o -> (o instanceof String));

			builder.pop();

			// Lucky Players
			builder.comment("Lucky players")
					.translation("statues.configuration.lucky_players.title")
					.push("lucky_players");

			String[] luckyPlayers = new String[]
					{
							"iskall85"
					};

			lucky_players = builder
					.comment("Adding usernames will make these users have less luck with getting statues")
					.defineListAllowEmpty(List.of("lucky_players"), () -> List.of(luckyPlayers), () -> "", o -> (o instanceof String));

			builder.pop();

			// Power settings
			builder.comment("Power Settings")
					.translation("statues.configuration.power_settings.title")
					.push("power_settings");

			requiresPower = builder
					.comment("When true statues will require power to function when upgraded (Default: false)")
					.define("requiresPower", false);

			powerDrain = builder
					.comment("If statues should drain power over time (Default: true)")
					.define("powerDrain", true);

			itemPowerUsage = builder
					.comment("The power used to generate an item (200)")
					.defineInRange("itemPowerUsage", 200, 0, Integer.MAX_VALUE);

			killPowerUsage = builder
					.comment("The power used to kill a mob (500)")
					.defineInRange("killPowerUsage", 500, 0, Integer.MAX_VALUE);

			summonPowerUsage = builder
					.comment("The power used to summon a mob (500)")
					.defineInRange("spawnPowerUsage", 100, 0, Integer.MAX_VALUE);

			despawnPowerUsage = builder
					.comment("The power used to despawn a mob (250)")
					.defineInRange("despawnPowerUsage", 250, 0, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	public static class Client {
		public final BooleanValue allowScrolling;

		Client(ModConfigSpec.Builder builder) {
			builder.comment("Client settings")
					.translation("statues.configuration.client.title")
					.push("client");

			allowScrolling = builder
					.comment("Allow scrolling to increase / decrease an angle value in the posing screen")
					.define("allowScrolling", true);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}


	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Statues.LOGGER.debug("Loaded Statues' config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Statues.LOGGER.warn("Statues' config just got changed on the file system!");
	}
}
