package net.kjentytek303.untransfur.registries;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;

import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

/*
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class UntransfurRegistries<T> implements Registry<T> {
	private UntransfurRegistries() {}

	private static final Logger LOGGER = LogManager.getLogger(UntransfurRegistries.class);
	public static final HashMap <ResourceKey<Registry<?>>, Supplier<IForgeRegistry<?>>> REGISTRY_HOLDERS = new HashMap<>();

	public static IForgeRegistry<?> getRegistry( ResourceKey<Registry<?>> key ) {
		if (REGISTRY_HOLDERS.isEmpty())
			throw new IllegalStateException("Cannot access registries before creation");
		return REGISTRY_HOLDERS.get(key).get();
	}
	@SubscribeEvent
	public static void onCreateRegistries( NewRegistryEvent event ) {
		createRegistry(event, regKey("msc_commands"), null, null);
	}

	private static <T> void createRegistry(
		NewRegistryEvent event, ResourceKey<? extends Registry<T>> key,
		@Nullable Consumer<RegistryBuilder<T>> additionalBuilder,
		@Nullable Consumer<IForgeRegistry<T>> onFill
	) {
		var builder = RegistryBuilder.<T>of(key.location()).setMaxID(Integer.MAX_VALUE -1);
		if (additionalBuilder != null)
			additionalBuilder.accept(builder);
		Supplier<IForgeRegistry<T>> holder = event.create(builder, onFill);
		REGISTRY_HOLDERS.put((ResourceKey)key, () -> (ForgeRegistry<?>)holder.get());
		LOGGER.info("Created registry {}", key);
	}

	public static <T> ResourceKey<Registry<T>> regKey(String name ) {
		return ResourceKey.createRegistryKey(Untransfur.modResource(name));
	}
}*/
