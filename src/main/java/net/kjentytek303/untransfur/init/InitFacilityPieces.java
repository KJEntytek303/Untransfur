package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.ltxprogrammer.changed.data.RegistryElementPredicate;
import net.ltxprogrammer.changed.init.ChangedFacilityZones;
import net.ltxprogrammer.changed.world.features.structures.LootTables;
import net.ltxprogrammer.changed.world.features.structures.facility.ConfiguredFacilityPiece;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollectionBuilder;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.ltxprogrammer.changed.world.features.structures.facility.GatherFacilityPiecesEvent;
import net.ltxprogrammer.changed.world.features.structures.facility.types.RoomType;
import net.minecraft.util.random.Weight;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.Set;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitFacilityPieces {
	@SubscribeEvent
	public static void RegisterFacilityPieces(GatherFacilityPiecesEvent event) {
		if(event.getPieceType() instanceof RoomType) {
			FacilityPieceCollectionBuilder builder = event.getBuilder();

			builder.register( new ConfiguredFacilityPiece(
					new FacilityRoomPiece(Untransfur.modResource("facility/msc_room"), Optional.of(LootTables.HIGH_TIER_LAB)),
					Weight.of(13),
					0,
					1,
					Set.of(ChangedFacilityZones.GENERAL_ZONE.get()),
					RegistryElementPredicate.forAll(ForgeRegistries.BIOMES)
				).setName(Untransfur.modResource("facility/msc_room"))
			);
		}
	}
}
