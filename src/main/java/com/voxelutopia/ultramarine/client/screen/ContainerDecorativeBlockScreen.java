package com.voxelutopia.ultramarine.client.screen;

import com.voxelutopia.ultramarine.world.block.menu.ContainerDecorativeBlockMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ContainerDecorativeBlockScreen extends AbstractContainerScreen<ContainerDecorativeBlockMenu> implements MenuAccess<ContainerDecorativeBlockMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");
    private final int containerRows;

    public ContainerDecorativeBlockScreen(ContainerDecorativeBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        // Screen#passEvents was removed, therefore screens can no longer pass events.
        // this.passEvents = false;
        this.containerRows = pMenu.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics pGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGraphics);
        super.render(pGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGraphics, float pPartialTick, int pX, int pY) {
        pGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGraphics.blit(CONTAINER_BACKGROUND, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        pGraphics.blit(CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

}
