package net.kjentytek303.untransfur.network;

import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class LivingEntityVariables {
	public static final Capability<LivingEntityVariables.Vars> LIVING_ENTITY_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	public static @Nullable LivingEntityVariables.Vars of(@NotNull LivingEntity entity ) {
		return entity.getCapability(LIVING_ENTITY_VARIABLES_CAPABILITY).resolve().orElse(null);
	}

	public static @NotNull LivingEntityVariables.Vars ofOrDefault(@NotNull LivingEntity entity ) {
		return entity.getCapability(LIVING_ENTITY_VARIABLES_CAPABILITY).resolve().orElseGet(LivingEntityVariables.Vars::new);
	}


	@Mod.EventBusSubscriber
	public static class Provider implements ICapabilitySerializable<CompoundTag> {
		final LivingEntityVariables.Vars LIVING_ENTITY_VARIABLES = new LivingEntityVariables.Vars();
		final LazyOptional<LivingEntityVariables.Vars> INSTANCE = LazyOptional.of(() -> LIVING_ENTITY_VARIABLES);

		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event ) {
			if( event.getObject() instanceof LivingEntity entity ) {
				event.addCapability(Untransfur.modResource("living_entity_variables"), new LivingEntityVariables.Provider());
				if( event.getObject() instanceof Player player && !( player instanceof FakePlayer)) {
					event.addCapability(Untransfur.modResource("player_variables"), new UntransfurVariables.Provider());
				}
			}
		}

		@Override
		public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			return cap == LIVING_ENTITY_VARIABLES_CAPABILITY ? INSTANCE.cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return LIVING_ENTITY_VARIABLES.serializeNBT();
		}
		@Override
		public void deserializeNBT(CompoundTag nbt) {
			LIVING_ENTITY_VARIABLES.deserializeNBT(nbt);
		}
	}

	public static class Vars {
		private double dissolvement = 0.0;

		public double getDissolvement() { return this.dissolvement; }
		public void setDissolvement (double new_dissolvement) { this.dissolvement = new_dissolvement; }

		public CompoundTag serializeNBT() {
			CompoundTag tag = new CompoundTag();
			tag.putDouble("flinston_dissolvement", this.dissolvement);
			return tag;
		}
		public void deserializeNBT(CompoundTag nbt) {
			dissolvement = nbt.contains("flinston_dissolvement") ? nbt.getDouble("flinston_dissolvement") : dissolvement;
		}

		public void syncPlayerVariables(LivingEntity entity) {
			if( entity instanceof ServerPlayer serverPlayer ) {
				Untransfur.PACKET_HANDLER.send(PacketDistributor.PLAYER.with( ()-> serverPlayer ), new LivingEntityVariables.SyncPacket(this));
			}
		}

		public void copyFrom( LivingEntityVariables.Vars present ) {
			if( present == null ) {
				return;
			}
			this.setDissolvement(present.getDissolvement());
		}
	}

	public static class SyncPacket {
		public LivingEntityVariables.Vars data;

		public SyncPacket( LivingEntityVariables.Vars data ) {
			this.data = data;
		}

		public SyncPacket( FriendlyByteBuf buffer ) {
			this.data = new LivingEntityVariables.Vars();
			this.data.deserializeNBT(buffer.readNbt());
		}

		public void serialize(FriendlyByteBuf buffer) {
			buffer.writeNbt(data.serializeNBT());
		}
	}
}
