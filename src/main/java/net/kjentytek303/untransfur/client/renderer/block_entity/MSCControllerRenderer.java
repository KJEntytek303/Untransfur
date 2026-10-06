package net.kjentytek303.untransfur.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import cpw.mods.modlauncher.TransformerHolder;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
		Vec3 bottom_left = TransformHorizontalDirectionVec3(Vec3.atLowerCornerWithOffset( new Vec3i(0,0,0), 0.5, 0.5, 0.5), pBlockEntity.getBlockState().getValue(FACING).getOpposite(), -1.5, 0.5, 0 );
		Vec3 top_right = TransformHorizontalDirectionVec3(Vec3.atLowerCornerWithOffset( new Vec3i(0,0,0), 0.5, 0.5, 0.5), pBlockEntity.getBlockState().getValue(FACING).getOpposite(), 1.5, 6.5, 0 );
		int alpha = tint_color >> 24 & 0xFF;
		alpha = alpha * 3 / 4;
		tint_color = tint_color & (0xFFFFFF);
		tint_color |= alpha << 24;

		drawQuad(builder, pPoseStack, (float)bottom_left.x(), (float)bottom_left.y(), (float)bottom_left.z(), (float)top_right.x(), (float)top_right.y(), (float)top_right.z(), sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), 0x9F, tint_color );

	}

	public static void drawVertex( VertexConsumer builder, PoseStack pose_stack, float x, float y, float z, float u, float v, int packed_light, int color) {
		builder.vertex(pose_stack.last().pose(), x, y, z)
			.color(color)
			.uv(u, v)
			.uv2(packed_light)
			.normal(-4,-4,-4)
			.endVertex();
	}

	public static void drawQuad( VertexConsumer builder, PoseStack pose_stack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float u1, float v0, float v1, int packed_light, int color) {
		pose_stack.pushPose();
		drawVertex(builder, pose_stack, x0, y0, z0, u0, v0, packed_light, color);
		drawVertex(builder, pose_stack, x0, y1, z1, u0, v1, packed_light, color);
		drawVertex(builder, pose_stack, x1, y1, z1, u1, v1, packed_light, color);
		drawVertex(builder, pose_stack, x1, y0, z0, u1, v0, packed_light, color);
		pose_stack.popPose();
	}
}
