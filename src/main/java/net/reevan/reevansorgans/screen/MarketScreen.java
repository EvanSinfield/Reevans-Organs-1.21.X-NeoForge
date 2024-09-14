package net.reevan.reevansorgans.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.reevan.reevansorgans.ReevansOrgans;
import net.reevan.reevansorgans.item.ModItems;
import org.slf4j.Logger;

public class MarketScreen extends AbstractContainerScreen<MarketMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/market.png");
    private static final Logger LOGGER = LogUtils.getLogger();

    private ImageButton brainButton;

    Item previous = null;
    ResourceLocation organTetxure;
    int selectedOrgan;

    public MarketScreen(MarketMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = -45;  // FIXME: put in right place
        this.titleLabelY = 4;
        this.inventoryLabelX = 1000;
        this.inventoryLabelY = 1000;
        this.imageWidth = 276;
        this.imageHeight = 166;

        LOGGER.debug("width: {}  height: {}", this.width, this.height);
        LOGGER.debug("image-width: {}  image-height: {}", this.imageWidth, this.imageHeight);

        this.brainButton = this.addRenderableWidget(new ImageButton(5, 14, 95, 16,
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/brain"), (press) -> {
            this.selectedOrgan = 0;
        }));  // FIXME: it wants a sprite isntead of resource location
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, 276, 166, 512, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderOrgan(guiGraphics);
    }

    public void renderOrgan(GuiGraphics guiGraphics) {
        Item organSlot = this.menu.getOrganSlot().getItem();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (organSlot != previous) {
            if (organSlot == ModItems.BRAIN.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/brain.png");
            } else if (organSlot == ModItems.HEART.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/heart.png");
            } else if (organSlot == ModItems.INTESTINES.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/intestines.png");
            } else if (organSlot == ModItems.KIDNEY.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/kidney.png");
            } else if (organSlot == ModItems.LIVER.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/liver.png");
            } else if (organSlot == ModItems.LUNGS.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/lungs.png");
            } else if (organSlot == ModItems.PANCREAS.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/pancreas.png");
            } else if (organSlot == ModItems.STOMACH.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/stomach.png");
            } else if (organSlot == ModItems.THYROID.asItem()) {
                organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/thyroid.png");
            } else {
                organTetxure = null;
            }

            LOGGER.debug(organSlot.toString());

            previous = organSlot;
        }

        if (organTetxure != null) {

            LOGGER.debug(organTetxure.toString());

            guiGraphics.blit(organTetxure, x + 150, y + 12, 0, 0, 32, 32, 32, 32);
        }
    }

    public void renderButtons(GuiGraphics guiGraphics) {

    }

    public void setSelectedOrgan(int selectedOrgan) {
        this.selectedOrgan = selectedOrgan;
    }
}
