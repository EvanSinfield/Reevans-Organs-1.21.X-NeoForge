package net.reevan.reevansorgans.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.reevan.reevansorgans.ReevansOrgans;
import net.reevan.reevansorgans.item.ModItems;
import net.reevan.reevansorgans.network.RequestPrice;
import org.slf4j.Logger;

public class MarketScreen extends AbstractContainerScreen<MarketMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/market.png");
    private static final Logger LOGGER = LogUtils.getLogger();  // TODO: remeber to remove this when done

    private ImageButton brainButton;
    private ImageButton heartButton;
    private ImageButton intestinesButton;
    private ImageButton kidneyButton;
    private ImageButton liverButton;
    private ImageButton lungsButton;
    private ImageButton pancreasButton;
    private ImageButton stomachButton;
    private ImageButton thyroidButton;

    private ImageButton buyButton;
    private ImageButton sellButton;

    Item previousItem = null;
    int previousOrgan = -1;
    ResourceLocation organTetxure;
    int selectedOrgan;
    int organPrice = 0;

    public MarketScreen(MarketMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = -45;
        this.titleLabelY = 4;
        this.inventoryLabelX = 1000;
        this.inventoryLabelY = 1000;
        this.imageWidth = 276;
        this.imageHeight = 166;
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.brainButton = this.addRenderableWidget(new ImageButton(x + 5, y + 14, 95, 16,
                generateSprite("brain"), (press) -> {
            this.selectedOrgan = 0;
        }));
        this.heartButton = this.addRenderableWidget(new ImageButton(x + 5, y + 30, 95, 16,
                generateSprite("heart"), (press) -> {
            this.selectedOrgan = 1;
        }));
        this.intestinesButton = this.addRenderableWidget(new ImageButton(x + 5, y + 46, 95, 16,
                generateSprite("intestines"), (press) -> {
            this.selectedOrgan = 2;
        }));
        this.kidneyButton = this.addRenderableWidget(new ImageButton(x + 5, y + 62, 95, 16,
                generateSprite("kidney"), (press) -> {
            this.selectedOrgan = 3;
        }));
        this.liverButton = this.addRenderableWidget(new ImageButton(x + 5, y + 78, 95, 16,
                generateSprite("liver"), (press) -> {
            this.selectedOrgan = 4;
        }));
        this.lungsButton = this.addRenderableWidget(new ImageButton(x + 5, y + 94, 95, 16,
                generateSprite("lungs"), (press) -> {
            this.selectedOrgan = 5;
        }));
        this.pancreasButton = this.addRenderableWidget(new ImageButton(x + 5, y + 110, 95, 16,
                generateSprite("pancreas"), (press) -> {
            this.selectedOrgan = 6;
        }));
        this.stomachButton = this.addRenderableWidget(new ImageButton(x + 5, y + 126, 95, 16,
                generateSprite("stomach"), (press) -> {
            this.selectedOrgan = 7;
        }));
        this.thyroidButton = this.addRenderableWidget(new ImageButton(x + 5, y + 142, 95, 16,
                generateSprite("thyroid"), (press) -> {
            this.selectedOrgan = 8;
        }));

        this.buyButton = this.addRenderableWidget(new ImageButton(x + 110, y + 15, 26, 16,
                generateSprite("buy"), (press) -> {
            LOGGER.debug("Buy button clicked");  // TODO: implement this properly
        }));
        this.sellButton = this.addRenderableWidget(new ImageButton(x + 110, y + 34, 26, 16,
                generateSprite("sell"), (press) -> {
            LOGGER.debug("Sell button clicked");
        }));

        this.sellButton.active = false;
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
        updateSelectedOrgan();
        renderOrganInfo(guiGraphics);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderOrganInfo(GuiGraphics guiGraphics) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        renderOrgan(guiGraphics);

        guiGraphics.drawString(font, Component.literal("Cost: " + this.organPrice), x + 150, y + 48, 0xFFFFFF);
    }

    public void renderOrgan(GuiGraphics guiGraphics) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (this.selectedOrgan != this.previousOrgan) {
            switch (this.selectedOrgan) {
                case (0):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/brain.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (1):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/heart.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (2):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/intestines.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (3):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/kidney.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (4):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/liver.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (5):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/lungs.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (6):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/pancreas.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (7):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/stomach.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (8):
                    this.organTetxure = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/thyroid.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                default:
                    this.organTetxure = null;
            }
        }

        if (organTetxure != null) {
            guiGraphics.blit(organTetxure, x + 150, y + 12, 0, 0, 32, 32, 32, 32);
        }

        this.previousOrgan = this.selectedOrgan;
    }

    private void updateSelectedOrgan() {
        Item organSlot = this.menu.getOrganSlot().getItem();

        if (organSlot != previousItem) {
            this.buyButton.active = false;
            this.sellButton.active = true;

            if (organSlot == ModItems.BRAIN.asItem()) {
                this.selectedOrgan = 0;
            } else if (organSlot == ModItems.HEART.asItem()) {
                this.selectedOrgan = 1;
            } else if (organSlot == ModItems.INTESTINES.asItem()) {
                this.selectedOrgan = 2;
            } else if (organSlot == ModItems.KIDNEY.asItem()) {
                this.selectedOrgan = 3;
            } else if (organSlot == ModItems.LIVER.asItem()) {
                this.selectedOrgan = 4;
            } else if (organSlot == ModItems.LUNGS.asItem()) {
                this.selectedOrgan = 5;
            } else if (organSlot == ModItems.PANCREAS.asItem()) {
                this.selectedOrgan = 6;
            } else if (organSlot == ModItems.STOMACH.asItem()) {
                this.selectedOrgan = 7;
            } else if (organSlot == ModItems.THYROID.asItem()) {
                this.selectedOrgan = 8;
            } else if (organSlot == ItemStack.EMPTY.getItem()) {
                this.buyButton.active = true;
                this.sellButton.active = false;
            } else {
                this.sellButton.active = false;
            }

            previousItem = organSlot;
        }
    }

    private WidgetSprites generateSprite(String name) {
        return new WidgetSprites(
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn"),
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn_inac"),
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn_hl"));
    }

    public void setOrganPrice(int organPrice) {
        this.organPrice = organPrice;
    }
}
