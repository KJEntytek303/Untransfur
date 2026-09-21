package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block.MSCControllerBlock;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static net.kjentytek303.untransfur.block.MSCControllerBlock.FACING;


public class CaptureEntityProgram extends MSCScheduledCommand {

	public static final ResourceLocation ID = Untransfur.modResource("capture_entity");
	public CaptureEntityProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		if( !(bentity.getBlockState().getBlock() instanceof MSCControllerBlock msc) || bentity.getLevel() == null ) {
			return false;
		}

		BlockState msc_controller = bentity.getBlockState();
		BlockPos pos = bentity.getBlockPos();

		BlockPos left_bottom_back = BlockUtilities.TransformHorizontalDirection(pos, msc_controller.getValue(FACING), -1, 0, -3);
		BlockPos right_top_front = BlockUtilities.TransformHorizontalDirection(pos, msc_controller.getValue(FACING), 1, 7, -2);
		AABB detection_box = new AABB( left_bottom_back, right_top_front);

		var entities = bentity.getLevel().getEntitiesOfClass(LivingEntity.class, detection_box);
		var iterator = entities.iterator();
		while (iterator.hasNext()) {
			var entity = iterator.next();
			AABB entity_box = entity.getBoundingBox();

			if (! (	detection_box.contains(entity_box.minX, entity_box.minY, entity_box.minZ) &&
				detection_box.contains(entity_box.maxX, entity_box.maxY, entity_box.maxZ) )
			) { iterator.remove(); }
		}
		return entities.isEmpty(); // if empty - continue, else return;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = bentity.isOpen() && bentity.isDrained();
		if(!ret) {
			bentity.failure_chance += 0.0015;
			bentity.markUpdated();
		}
		return ret;
	}
}
