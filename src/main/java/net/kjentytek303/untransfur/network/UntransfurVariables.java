package net.kjentytek303.untransfur.network;

import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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


public class UntransfurVariables {
	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	public static @Nullable PlayerVariables of( @NotNull Player player ) {
		return player.getCapability(PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
	}

	public static @NotNull PlayerVariables ofOrDefault( @NotNull Player player ) {
		return player.getCapability(PLAYER_VARIABLES_CAPABILITY).resolve().orElseGet(PlayerVariables::new);
	}


	public static class PlayerVariables {
		private double untransfur_progress = 0.0;
		private double flinston_dissolvement = 0.0;

		public double getUntfProgress() { return this.untransfur_progress; }
		public double getFlinstonProgress() { return this.flinston_dissolvement; }
		public void setUntfProgress(double new_progress) { this.untransfur_progress = new_progress; }
		public void setFlinstonDissolvement(double new_dissolvement) { this.flinston_dissolvement = new_dissolvement; }

		public CompoundTag serializeNBT() {
			CompoundTag tag = new CompoundTag();
			tag.putDouble("untransfur_progress", this.untransfur_progress);
			tag.putDouble("flinston_dissolvement", this.flinston_dissolvement);
			return tag;
		}
		public void deserializeNBT(CompoundTag nbt) {
			untransfur_progress = nbt.contains("untransfur_progress") ? nbt.getDouble("untransfur_progress") : untransfur_progress;
			flinston_dissolvement = nbt.contains("flinston_dissolvement") ? nbt.getDouble("flinston_dissolvement") : flinston_dissolvement;
		}

		public void syncPlayerVariables(Entity entity) {
			if( entity instanceof ServerPlayer serverPlayer ) {
				Untransfur.PACKET_HANDLER.send(PacketDistributor.PLAYER.with( ()-> serverPlayer ), new SyncPacket(this));
			}
		}

		public void copyFrom( PlayerVariables present ) {
			if( present == null ) {
				return;
			}
			this.setUntfProgress(present.getUntfProgress());
			this.setFlinstonDissolvement(present.getFlinstonProgress());
		}
	}

	@Mod.EventBusSubscriber
	public static class Provider implements ICapabilitySerializable<CompoundTag> {
		final PlayerVariables PLAYER_VARIABLES = new PlayerVariables();
		final LazyOptional<PlayerVariables> INSTANCE = LazyOptional.of(() -> PLAYER_VARIABLES);

		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event ) {
			if( event.getObject() instanceof Player player && !( player instanceof FakePlayer )) {
				event.addCapability(Untransfur.modResource("player_variables"), new Provider());
			}
		}

		@Override
		public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			return cap == PLAYER_VARIABLES_CAPABILITY ? INSTANCE.cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return PLAYER_VARIABLES.serializeNBT();
		}
		@Override
		public void deserializeNBT(CompoundTag nbt) {
			PLAYER_VARIABLES.deserializeNBT(nbt);
		}
	}

	public static class SyncPacket {
		public PlayerVariables data;

		public SyncPacket( PlayerVariables data ) {
			this.data = data;
		}

		public SyncPacket( FriendlyByteBuf buffer ) {
			this.data = new PlayerVariables();
			this.data.deserializeNBT(buffer.readNbt());
		}

		public void serialize(FriendlyByteBuf buffer) {
			buffer.writeNbt(data.serializeNBT());
		}
	}
}
