package net.kjentytek303.untransfur.client.screen;
/*
import com.mojang.blaze3d.systems.RenderSystem;
import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class MSCTankScreen extends AbstractContainerScreen<MSCTankMenu> {

	private static final ResourceLocation TEXTURE = ResourceLocation.tryBuild( Untransfur.MODID,"textures/gui/msc_tank_screen.png");

	public MSCTankScreen(MSCTankMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f , 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = ( width - imageWidth ) /2;
		int y = ( height - imageHeight ) /2;

		pGuiGraphics.blit( TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

		//TODO: rederTankContent
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		renderBackground(pGuiGraphics);
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		renderTooltip(pGuiGraphics, pMouseX, pMouseY);
	}
}
*/