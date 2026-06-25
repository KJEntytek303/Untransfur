package net.kjentytek303.untransfur;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static net.kjentytek303.untransfur.init.CreativeTabs.CT_TABS_REGISTRY;
import static net.kjentytek303.untransfur.init.InitBlockEntities.BLOCK_ENTITY_REGISTRY;
import static net.kjentytek303.untransfur.init.InitBlocks.BLOCK_REGISTRY;
import static net.kjentytek303.untransfur.init.InitItems.ITEM_REGISTRY;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Untransfur.MODID)
public class Untransfur
{
    public static final String MODID = "untransfur";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Untransfur(FMLJavaModLoadingContext context)
    {
        
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);

        BLOCK_REGISTRY.register(modEventBus);
        ITEM_REGISTRY.register(modEventBus);
        CT_TABS_REGISTRY.register(modEventBus);
        BLOCK_ENTITY_REGISTRY.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {}
    public static ResourceLocation modResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String modResourceStr(String path) {
        return MODID + ":" + path;
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    /*
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    } */
}
