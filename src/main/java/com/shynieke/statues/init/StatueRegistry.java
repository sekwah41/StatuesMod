package com.shynieke.statues.init;

import com.shynieke.statues.Reference;
import com.shynieke.statues.blocks.decorative.BumboStatueBlock;
import com.shynieke.statues.blocks.decorative.DisplayStandBlock;
import com.shynieke.statues.blocks.decorative.EndermiteStatueBlock;
import com.shynieke.statues.blocks.decorative.PebbleBlock;
import com.shynieke.statues.blocks.decorative.SombreroBlock;
import com.shynieke.statues.blocks.decorative.TotemOfUndyingStatueBlock;
import com.shynieke.statues.blocks.statues.AngryBeeStatueBlock;
import com.shynieke.statues.blocks.statues.BabyZombieStatueBlock;
import com.shynieke.statues.blocks.statues.BeeStatueBlock;
import com.shynieke.statues.blocks.statues.BlazeStatueBlock;
import com.shynieke.statues.blocks.statues.CampfireStatueBlock;
import com.shynieke.statues.blocks.statues.ChickenJockeyStatueBlock;
import com.shynieke.statues.blocks.statues.ChickenStatueBlock;
import com.shynieke.statues.blocks.statues.CowStatueBlock;
import com.shynieke.statues.blocks.statues.CreeperStatueBlock;
import com.shynieke.statues.blocks.statues.EndermanStatueBlock;
import com.shynieke.statues.blocks.statues.EvokerStatueBlock;
import com.shynieke.statues.blocks.statues.FloodStatueBlock;
import com.shynieke.statues.blocks.statues.GhastStatueBlock;
import com.shynieke.statues.blocks.statues.GuardianStatueBlock;
import com.shynieke.statues.blocks.statues.HuskStatueBlock;
import com.shynieke.statues.blocks.statues.InfoStatueBlock;
import com.shynieke.statues.blocks.statues.KingCluckStatueBlock;
import com.shynieke.statues.blocks.statues.MagmaStatueBlock;
import com.shynieke.statues.blocks.statues.MooshroomStatueBlock;
import com.shynieke.statues.blocks.statues.PigStatueBlock;
import com.shynieke.statues.blocks.statues.PlayerStatueBlock;
import com.shynieke.statues.blocks.statues.RabbitStatueBlock;
import com.shynieke.statues.blocks.statues.SheepShavenStatueBlock;
import com.shynieke.statues.blocks.statues.SheepStatueBlock;
import com.shynieke.statues.blocks.statues.ShulkerStatueBlock;
import com.shynieke.statues.blocks.statues.SlimeStatueBlock;
import com.shynieke.statues.blocks.statues.SnowGolemStatueBlock;
import com.shynieke.statues.blocks.statues.SpiderStatueBlock;
import com.shynieke.statues.blocks.statues.VillagerStatue;
import com.shynieke.statues.blocks.statues.WastelandStatueBlock;
import com.shynieke.statues.blocks.statues.WitchStatueBlock;
import com.shynieke.statues.blocks.statues.ZombieStatueBlock;
import com.shynieke.statues.blocks.statues.fish.CodStatueBlock;
import com.shynieke.statues.blocks.statues.fish.DolphinStatueBlock;
import com.shynieke.statues.blocks.statues.fish.DrownedStatueBlock;
import com.shynieke.statues.blocks.statues.fish.FishStatueBlock;
import com.shynieke.statues.blocks.statues.fish.PufferfishStatueBlock;
import com.shynieke.statues.blocks.statues.fish.SalmonStatueBlock;
import com.shynieke.statues.blocks.statues.fish.SquidStatueBlock;
import com.shynieke.statues.blocks.statues.fish.TurtleStatueBlock;
import com.shynieke.statues.client.render.PlayerTileInventoryRenderer;
import com.shynieke.statues.items.PlayerCompassItem;
import com.shynieke.statues.items.StatueBlockItem;
import com.shynieke.statues.items.StatueCharredMarshmallow;
import com.shynieke.statues.items.StatueCoreItem;
import com.shynieke.statues.items.StatueGoldenMarshmallow;
import com.shynieke.statues.items.StatueMooshroomSoup;
import com.shynieke.statues.items.StatueTeaItem;
import com.shynieke.statues.items.StatueTransBeeItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

public class StatueRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static ArrayList<Block> STATUES = new ArrayList<>();

    public static final RegistryObject<Block> BABY_ZOMBIE_STATUE = registerStatue("baby_zombie_statue", () -> new BabyZombieStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> ANGRY_BEE_STATUE = registerStatue("angry_bee_statue", () -> new AngryBeeStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> BEE_STATUE = registerBeeStatue("bee_statue", () -> new BeeStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> BLAZE_STATUE = registerStatue("blaze_statue", () -> new BlazeStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> BUMBO_STATUE = registerBlock("bumbo_statue", () -> new BumboStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> CAMPFIRE_STATUE = registerStatue("campfire_statue", () -> new CampfireStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> CHICKEN_JOCKEY_STATUE = registerStatue("chicken_jockey_statue", () -> new ChickenJockeyStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> CHICKEN_STATUE = registerStatue("chicken_statue", () -> new ChickenStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> COD_STATUE = registerStatue("cod_statue", () -> new CodStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> COW_STATUE = registerStatue("cow_statue", () -> new CowStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> CREEPER_STATUE = registerStatue("creeper_statue", () -> new CreeperStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> DISPLAY_STAND = registerBlock("display_stand", () -> new DisplayStandBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> DOLPHIN_STATUE = registerStatue("dolphin_statue", () -> new DolphinStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> DROWNED_STATUE = registerStatue("drowned_statue", () -> new DrownedStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> ENDERMAN_STATUE = registerStatue("enderman_statue", () -> new EndermanStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> ENDERMITE_STATUE = registerStatue("endermite_statue", () -> new EndermiteStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> EVOKER_STATUE = registerStatue("evoker_statue", () -> new EvokerStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> FLOOD_STATUE = registerStatue("flood_statue", () -> new FloodStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> GHAST_STATUE = registerStatue("ghast_statue", () -> new GhastStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> GUARDIAN_STATUE = registerStatue("guardian_statue", () -> new GuardianStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> HUSK_STATUE = registerStatue("husk_statue", () -> new HuskStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> INFO_STATUE = registerStatue("info_statue", () -> new InfoStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> KING_CLUCK_STATUE = registerStatue("king_cluck_statue", () -> new KingCluckStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> MAGMA_STATUE = registerStatue("magma_statue", () -> new MagmaStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> MOOSHROOM_STATUE = registerStatue("mooshroom_statue", () -> new MooshroomStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> PEBBLE = registerBlock("pebble", () -> new PebbleBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> PIG_STATUE = registerStatue("pig_statue", () -> new PigStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> PLAYER_STATUE = registerPlayerStatue("player_statue", () -> new PlayerStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> PUFFERFISH_MEDIUM_STATUE = registerStatue("pufferfish_medium_statue", () -> new PufferfishStatueBlock(blockBuilder(),1), itemBuilder());
    public static final RegistryObject<Block> PUFFERFISH_SMALL_STATUE = registerStatue("pufferfish_small_statue", () -> new PufferfishStatueBlock(blockBuilder(),0), itemBuilder());
    public static final RegistryObject<Block> PUFFERFISH_STATUE = registerStatue("pufferfish_statue", () -> new PufferfishStatueBlock(blockBuilder(),2), itemBuilder());
    public static final RegistryObject<Block> RABBIT_BR_STATUE = registerStatue("rabbit_br_statue", () -> new RabbitStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> RABBIT_BS_STATUE = registerStatue("rabbit_bs_statue", () -> new RabbitStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> RABBIT_BW_STATUE = registerStatue("rabbit_bw_statue", () -> new RabbitStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> RABBIT_GO_STATUE = registerStatue("rabbit_go_statue", () -> new RabbitStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> RABBIT_WH_STATUE = registerStatue("rabbit_wh_statue", () -> new RabbitStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> RABBIT_WS_STATUE = registerStatue("rabbit_ws_statue", () -> new RabbitStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SALMON_STATUE = registerStatue("salmon_statue", () -> new SalmonStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SHEEP_SHAVEN_STATUE = registerStatue("sheep_shaven_statue", () -> new SheepShavenStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_BLACK = registerStatue("sheep_statue_black", () -> new SheepStatueBlock(blockBuilder(), DyeColor.BLACK), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_BLUE = registerStatue("sheep_statue_blue", () -> new SheepStatueBlock(blockBuilder(), DyeColor.BLUE), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_BROWN = registerStatue("sheep_statue_brown", () -> new SheepStatueBlock(blockBuilder(), DyeColor.BROWN), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_CYAN = registerStatue("sheep_statue_cyan", () -> new SheepStatueBlock(blockBuilder(), DyeColor.CYAN), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_GRAY = registerStatue("sheep_statue_gray", () -> new SheepStatueBlock(blockBuilder(), DyeColor.GRAY), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_GREEN = registerStatue("sheep_statue_green", () -> new SheepStatueBlock(blockBuilder(), DyeColor.GREEN), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_LIGHT_BLUE = registerStatue("sheep_statue_light_blue", () -> new SheepStatueBlock(blockBuilder(), DyeColor.LIGHT_BLUE), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_LIGHT_GRAY = registerStatue("sheep_statue_light_gray", () -> new SheepStatueBlock(blockBuilder(), DyeColor.LIGHT_GRAY), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_LIME = registerStatue("sheep_statue_lime", () -> new SheepStatueBlock(blockBuilder(), DyeColor.LIME), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_MAGENTA = registerStatue("sheep_statue_magenta", () -> new SheepStatueBlock(blockBuilder(), DyeColor.MAGENTA), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_ORANGE = registerStatue("sheep_statue_orange", () -> new SheepStatueBlock(blockBuilder(), DyeColor.ORANGE), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_PINK = registerStatue("sheep_statue_pink", () -> new SheepStatueBlock(blockBuilder(), DyeColor.PINK), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_PURPLE = registerStatue("sheep_statue_purple", () -> new SheepStatueBlock(blockBuilder(), DyeColor.PURPLE), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_RED = registerStatue("sheep_statue_red", () -> new SheepStatueBlock(blockBuilder(), DyeColor.RED), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_WHITE = registerStatue("sheep_statue_white", () -> new SheepStatueBlock(blockBuilder(), DyeColor.WHITE), itemBuilder());
    public static final RegistryObject<Block> SHEEP_STATUE_YELLOW = registerStatue("sheep_statue_yellow", () -> new SheepStatueBlock(blockBuilder(), DyeColor.YELLOW), itemBuilder());
    public static final RegistryObject<Block> SHULKER_STATUE = registerStatue("shulker_statue", () -> new ShulkerStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SLIME_STATUE = registerStatue("slime_statue", () -> new SlimeStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SNOW_GOLEM_STATUE = registerStatue("snow_golem_statue", () -> new SnowGolemStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SOMBRERO = registerBlock("sombrero", () -> new SombreroBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SPIDER_STATUE = registerStatue("spider_statue", () -> new SpiderStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> SQUID_STATUE = registerStatue("squid_statue", () -> new SquidStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> TOTEM_OF_UNDYING_STATUE = registerStatue("totem_of_undying_statue", () -> new TotemOfUndyingStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_B = registerStatue("tropical_fish_b", () -> new FishStatueBlock(blockBuilder(), 1), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_BB = registerStatue("tropical_fish_bb", () -> new FishStatueBlock(blockBuilder(), 0), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_BE = registerStatue("tropical_fish_be", () -> new FishStatueBlock(blockBuilder(), 0), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_BM = registerStatue("tropical_fish_bm", () -> new FishStatueBlock(blockBuilder(), 1), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_BMB = registerStatue("tropical_fish_bmb", () -> new FishStatueBlock(blockBuilder(), 1), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_BMS = registerStatue("tropical_fish_bms", () -> new FishStatueBlock(blockBuilder(), 0), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_E = registerStatue("tropical_fish_e", () -> new FishStatueBlock(blockBuilder(), 1), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_ES = registerStatue("tropical_fish_es", () -> new FishStatueBlock(blockBuilder(), 0), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_HB = registerStatue("tropical_fish_hb", () -> new FishStatueBlock(blockBuilder(), 1), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_SB = registerStatue("tropical_fish_sb", () -> new FishStatueBlock(blockBuilder(), 1), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_SD = registerStatue("tropical_fish_sd", () -> new FishStatueBlock(blockBuilder(), 0), itemBuilder());
    public static final RegistryObject<Block> TROPICAL_FISH_SS = registerStatue("tropical_fish_ss", () -> new FishStatueBlock(blockBuilder(), 0), itemBuilder());
    public static final RegistryObject<Block> TURTLE_STATUE = registerStatue("turtle_statue", () -> new TurtleStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> VILLAGER_BR_STATUE = registerStatue("villager_br_statue", () -> new VillagerStatue(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> VILLAGER_GR_STATUE = registerStatue("villager_gr_statue", () -> new VillagerStatue(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> VILLAGER_PU_STATUE = registerStatue("villager_pu_statue", () -> new VillagerStatue(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> VILLAGER_WH_STATUE = registerStatue("villager_wh_statue", () -> new VillagerStatue(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> WASTELAND_STATUE = registerStatue("wasteland_statue", () -> new WastelandStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> WITCH_STATUE = registerStatue("witch_statue", () -> new WitchStatueBlock(blockBuilder()), itemBuilder());
    public static final RegistryObject<Block> ZOMBIE_STATUE = registerStatue("zombie_statue", () -> new ZombieStatueBlock(blockBuilder()), itemBuilder());

    public static final RegistryObject<Item> CUP = ITEMS.register("cup", () -> new Item(itemBuilder().food(StatueFoods.CUP)));
    public static final RegistryObject<Item> MARSHMALLOW = ITEMS.register("marshmallow", () -> new Item(itemBuilder().food(StatueFoods.MARSHMALLOW)));
    public static final RegistryObject<Item> MARSHMALLOW_CHARRED = ITEMS.register("marshmallow_charred", () -> new StatueCharredMarshmallow(itemBuilder()));
    public static final RegistryObject<Item> MARSHMALLOW_COOKED = ITEMS.register("marshmallow_cooked", () -> new Item(itemBuilder().food(StatueFoods.COOKED_MARSHMALLOW)));
    public static final RegistryObject<Item> MARSHMALLOW_GOLDEN = ITEMS.register("marshmallow_golden", () -> new StatueGoldenMarshmallow(itemBuilder(), StatueFoods.GOLDEN_MARSHMALLOW));
    public static final RegistryObject<Item> NUGGET = ITEMS.register("royal_nugget", () -> new Item(itemBuilder().food(StatueFoods.ROYAL_NUGGET)));
    public static final RegistryObject<Item> PLAYER_COMPASS = ITEMS.register("player_compass", () -> new PlayerCompassItem(itemBuilder()));
    public static final RegistryObject<Item> SOUP = ITEMS.register("mooshroom_soup", () -> new StatueMooshroomSoup(itemBuilder(), StatueFoods.SOUP));
    public static final RegistryObject<Item> STATUE_CORE = ITEMS.register("statue_core", () -> new StatueCoreItem(itemBuilder()));
    public static final RegistryObject<Item> TEA = ITEMS.register("tea", () -> new StatueTeaItem(itemBuilder(), StatueFoods.TEA));

    public static <B extends Block> RegistryObject<B> registerStatue(String name, Supplier<? extends B> supplier, Item.Properties properties) {
        RegistryObject<B> block = StatueRegistry.BLOCKS.register(name, supplier);
        STATUES.add(supplier.get());
        ITEMS.register(name, () -> new StatueBlockItem(block.get(), properties));
        return block;
    }

    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<? extends B> supplier, Item.Properties properties) {
        RegistryObject<B> block = StatueRegistry.BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new StatueBlockItem(block.get(), properties));
        return block;
    }

    public static <B extends Block> RegistryObject<B> registerBeeStatue(String name, Supplier<? extends B> supplier, Item.Properties properties) {
        RegistryObject<B> block = StatueRegistry.BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new StatueTransBeeItem(block.get(), properties));
        return block;
    }

    public static <B extends Block> RegistryObject<B> registerPlayerStatue(String name, Supplier<? extends B> supplier, Item.Properties properties) {
        RegistryObject<B> block = StatueRegistry.BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new StatueBlockItem(block.get(), properties.setISTER(() -> PlayerTileInventoryRenderer::new)));
        return block;
    }

    private static Item.Properties itemBuilder()
    {
        return new Item.Properties().group(StatueTabs.STATUES_BLOCKS);
    }

    private static Block.Properties blockBuilder() { return Block.Properties.create(Material.SHULKER); }
}