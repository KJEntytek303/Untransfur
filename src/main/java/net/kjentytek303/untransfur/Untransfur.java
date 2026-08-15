package net.kjentytek303.untransfur;

import com.mojang.logging.LogUtils;
import net.kjentytek303.untransfur.client.screen.MSCBusScreen;
import net.kjentytek303.untransfur.config.ServerCfg;
import net.kjentytek303.untransfur.init.InitMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import net.minecraftforge.network.NetworkRegistry;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.kjentytek303.untransfur.init.CreativeTabs.CT_TABS_REGISTRY;
import static net.kjentytek303.untransfur.init.InitBlockEntities.BLOCK_ENTITY_REGISTRY;
import static net.kjentytek303.untransfur.init.InitBlocks.BLOCK_REGISTRY;
import static net.kjentytek303.untransfur.init.InitItems.ITEM_REGISTRY;
import static net.kjentytek303.untransfur.init.InitMenus.MENU_REGISTRY;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Untransfur.MODID)
public class Untransfur
{
    public static final String MODID = "untransfur";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(Untransfur.modResource("network"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals );
    private static int message_id = 0;

    public Untransfur(FMLJavaModLoadingContext context)
    {
        
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);

        BLOCK_REGISTRY.register(modEventBus);
        ITEM_REGISTRY.register(modEventBus);
        CT_TABS_REGISTRY.register(modEventBus);
        BLOCK_ENTITY_REGISTRY.register(modEventBus);
        MENU_REGISTRY.register(modEventBus);

        context.registerConfig(ModConfig.Type.SERVER, ServerCfg.SPEC, "untransfur-server.toml");
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(message_id, messageType, encoder, decoder, messageConsumer);
        message_id++;
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer, NetworkDirection direction) {
        PACKET_HANDLER.registerMessage(message_id, messageType, encoder, decoder, messageConsumer, Optional.of(direction));
        message_id++;
    }

    public static ResourceLocation modResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String modResourceStr(String path) {
        return MODID + ":" + path;
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(InitMenus.MSC_BUS_MENU.get(), MSCBusScreen::new);
        }
    }
}
