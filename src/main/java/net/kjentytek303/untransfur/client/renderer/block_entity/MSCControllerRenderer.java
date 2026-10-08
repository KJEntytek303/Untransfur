package net.kjentytek303.untransfur.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import cpw.mods.modlauncher.TransformerHolder;
import net.kjentytek303.untransfur.block.MSCControllerBlock;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitBlocks;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import static net.kjentytek303.untransfur.block.MSCControllerBlock.FACING;
import static net.kjentytek303.untransfur.util.BlockUtilities.TransformHorizontalDirectionVec3;
import static net.minecraft.world.level.material.Fluids.WATER;

@OnlyIn(Dist.CLIENT)
public class MSCControllerRenderer implements BlockEntityRenderer<MSCControllerBlockEntity> {

	public final BlockEntityRendererProvider.Context ctx;
	public MSCControllerRenderer(BlockEntityRendererProvider.Context context) {
		ctx = context;
	}

	@Override
	public void render(@NotNull MSCControllerBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
			   @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

		//if(pBlockEntity.getFluidYHeight() == 0 || pBlockEntity.getLevel() == null) {
		//	return;
		//}

		IClientFluidTypeExtensions fluid_type = IClientFluidTypeExtensions.of(WATER);

		ResourceLocation still_texture = fluid_type.getStillTexture();
		if( still_texture == null ) {
			return;
		}

		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(still_texture);
		int tint_color = fluid_type.getTintColor();

		Vec3 controller_center = pBlockEntity.getBlockPos().getCenter();
		VertexConsumer builder = pBuffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(WATER.defaultFluidState()));
		Vec3 bottom_left = new Vec3( -1.5, 2, 1.5);
		Vec3 top_right = new Vec3( -1.5, 7.5, -1.5);
		int alpha = tint_color >> 24 & 0xFF;
		alpha = alpha * 3 / 4;
		tint_color = tint_color & (0xFFFFFF);
		tint_color |= alpha << 24;

		if(!pBlockEntity.getBlockState().is(InitBlocks.MSC_CONTROLLER.get())) {
			return;
		};
		if( pBlockEntity.isDrained()) {
			return;
		}
		float fluid_level = pBlockEntity.getFluidLevel();

		//Front
		pPoseStack.pushPose();
		rotateToBentity(pPoseStack, pBlockEntity);
		drawQuad(builder, pPoseStack, 1f, 1, 0.5f, -2f, getFluidYHeight(fluid_level), 0.5f, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), pPackedLight, tint_color);
		pPoseStack.popPose();

		pPoseStack.pushPose();
		pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
		pPoseStack.translate(-1, 0, -1);
		rotateToBentity(pPoseStack, pBlockEntity);
		drawQuad(builder, pPoseStack, 1f, 1, 0.5f, -2f, getFluidYHeight(fluid_level), 0.5f, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), pPackedLight, tint_color);
		pPoseStack.popPose();

		//Top
		if(!pBlockEntity.isFilled() && !pBlockEntity.isDrained()) {
			pPoseStack.pushPose();
			rotateToBentity(pPoseStack, pBlockEntity);
			pPoseStack.translate(-0.5, 0, -2);
			drawQuad(builder, pPoseStack, 1.5f, getFluidYHeight(fluid_level), -1f, -1.5f, getFluidYHeight(fluid_level), 2.5f, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), pPackedLight, tint_color);
			pPoseStack.popPose();

			pPoseStack.pushPose();
			rotateToBentity(pPoseStack, pBlockEntity);
			pPoseStack.mulPose(Axis.XP.rotationDegrees(180));
			pPoseStack.translate(-0.5, 0, 0);
			drawQuad(builder, pPoseStack, 1.5f, -getFluidYHeight(fluid_level), -0.5f, -1.5f, -getFluidYHeight(fluid_level),  3f, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), pPackedLight, tint_color);
			pPoseStack.popPose();
		}
	}

	public static float getFluidYHeight(float fluid_level) {
		return fluid_level * 7;
	}

	public static void drawVertex( VertexConsumer builder, PoseStack pose_stack, float x, float y, float z, float u, float v, int packed_light, int color) {
		builder.vertex(pose_stack.last().pose(), x, y, z)
			.color(color)
			.uv(u, v)
			.uv2(packed_light)
			.normal(1,0,0)
			.endVertex();
	}

	public static void drawQuad( VertexConsumer builder, PoseStack pose_stack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float u1, float v0, float v1, int packed_light, int color) {
		drawVertex(builder, pose_stack, x0, y0, z0, u0, v0, packed_light, color);
		drawVertex(builder, pose_stack, x0, y1, z1, u0, v1, packed_light, color);
		drawVertex(builder, pose_stack, x1, y1, z1, u1, v1, packed_light, color);
		drawVertex(builder, pose_stack, x1, y0, z0, u1, v0, packed_light, color);
	}

	public static void rotateToBentity( PoseStack pose_stack, MSCControllerBlockEntity bentity ) {
		Direction facing = bentity.getBlockState().getValue(FACING);
		switch (facing) {
			case NORTH -> {
				pose_stack.mulPose(Axis.YP.rotationDegrees(180));
				pose_stack.translate(0, 0, -1);
			}

			case WEST -> {
				pose_stack.mulPose(Axis.YN.rotationDegrees(90));
				pose_stack.translate(1, 0, -1);
			}
			case EAST -> pose_stack.mulPose(Axis.YP.rotationDegrees(90));
			default -> { //South
				pose_stack.mulPose(Axis.YP.rotationDegrees(0));
				pose_stack.translate(1, 0, 0);
			}
		}
	}
}
