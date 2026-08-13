package net.kjentytek303.untransfur.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class MSCBusScreen extends AbstractContainerScreen<MSCBusMenu> {
		private static final ResourceLocation TEXTURE = Untransfur.modResource("textures/gui/msc_bus_gui.png");

	public MSCBusScreen(MSCBusMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		//this.imageWidth = 176;
		//this.imageHeight = 145;
	}
	@Override
	protected void init() {
		super.init();
		this.inventoryLabelY = 100000;
		this.titleLabelY = 100000;
	}
	@Override
	protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0,TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		pGuiGraphics.blit(TEXTURE, x, y, 0, 0, 176, 145, 176, 145);
	}

	@Override
	public void render( GuiGraphics gui_graphics, int mouseX, int mouseY, float delta) {
		renderBackground(gui_graphics);
		super.render(gui_graphics, mouseX, mouseY, delta);
		renderTooltip(gui_graphics, mouseX, mouseY);
	}
}
