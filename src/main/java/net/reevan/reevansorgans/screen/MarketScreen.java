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
import net.reevan.reevansorgans.network.packets.BuySellOrgan;
import net.reevan.reevansorgans.network.packets.RequestCredits;
import net.reevan.reevansorgans.network.packets.RequestPrice;
import org.slf4j.Logger;

import java.util.Random;

public class MarketScreen extends AbstractContainerScreen<MarketMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/market.png");
    private static final Logger LOGGER = LogUtils.getLogger();  // TODO: remeber to remove this when done
    public static final Random random = new Random();

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
    ResourceLocation organTexture;
    ResourceLocation organDesc;
    int selectedOrgan;
    int organPrice = 0;
    boolean organInSlot = false;
    int credits = 0;

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
                generateButtonSprite("brain"), (press) -> {
            this.selectedOrgan = 0;
        }));
        this.heartButton = this.addRenderableWidget(new ImageButton(x + 5, y + 30, 95, 16,
                generateButtonSprite("heart"), (press) -> {
            this.selectedOrgan = 1;
        }));
        this.intestinesButton = this.addRenderableWidget(new ImageButton(x + 5, y + 46, 95, 16,
                generateButtonSprite("intestines"), (press) -> {
            this.selectedOrgan = 2;
        }));
        this.kidneyButton = this.addRenderableWidget(new ImageButton(x + 5, y + 62, 95, 16,
                generateButtonSprite("kidney"), (press) -> {
            this.selectedOrgan = 3;
        }));
        this.liverButton = this.addRenderableWidget(new ImageButton(x + 5, y + 78, 95, 16,
                generateButtonSprite("liver"), (press) -> {
            this.selectedOrgan = 4;
        }));
        this.lungsButton = this.addRenderableWidget(new ImageButton(x + 5, y + 94, 95, 16,
                generateButtonSprite("lungs"), (press) -> {
            this.selectedOrgan = 5;
        }));
        this.pancreasButton = this.addRenderableWidget(new ImageButton(x + 5, y + 110, 95, 16,
                generateButtonSprite("pancreas"), (press) -> {
            this.selectedOrgan = 6;
        }));
        this.stomachButton = this.addRenderableWidget(new ImageButton(x + 5, y + 126, 95, 16,
                generateButtonSprite("stomach"), (press) -> {
            this.selectedOrgan = 7;
        }));
        this.thyroidButton = this.addRenderableWidget(new ImageButton(x + 5, y + 142, 95, 16,
                generateButtonSprite("thyroid"), (press) -> {
            this.selectedOrgan = 8;
        }));

        this.buyButton = this.addRenderableWidget(new ImageButton(x + 110, y + 15, 26, 16,
                generateButtonSprite("buy"), (press) -> {
            buyOrgan(this.selectedOrgan);
        }));
        this.sellButton = this.addRenderableWidget(new ImageButton(x + 110, y + 34, 26, 16,
                generateButtonSprite("sell"), (press) -> {
            sellOrgan();
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
        PacketDistributor.sendToServer(RequestCredits.INSTANCE);
        Component estimateText;

        renderOrgan(guiGraphics);

        guiGraphics.drawString(font, Component.literal("Credits: " + credits), x + 150, y + 49, 0xFFFFFF);
        guiGraphics.drawString(font, Component.literal("Cost: " + this.organPrice), x + 150, y + 58, 0xFFFFFF);

        if (organInSlot) {
            ItemStack organ = this.menu.getOrganSlot();

            int estimateNum = Math.round(((100 - organ.getDamageValue()) / 100f) * organPrice);

            estimateText = Component.literal("Aprox. value: " + estimateNum);
        } else {
            estimateText = Component.literal("Aprox. value: N/A");
        }

        guiGraphics.drawString(font, estimateText, x + 150, y + 67, 0xFFFFFF);
    }

    public void renderOrgan(GuiGraphics guiGraphics) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (this.selectedOrgan != this.previousOrgan) {
            switch (this.selectedOrgan) {
                case (0):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/brain.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/brain_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (1):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/heart.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/heart_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (2):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/intestines.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/intestines_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (3):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/kidney.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/kidney_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (4):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/liver.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/liver_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (5):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/lungs.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/lungs_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (6):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/pancreas.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/pancreas_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (7):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/stomach.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/stomach_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (8):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/thyroid.png");
                    this.organDesc = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/gui/sprites/thyroid_desc.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                default:
                    this.organTexture = null;
                    this.organDesc = null;
            }
        }

        if (organTexture != null) {
            guiGraphics.blit(organTexture, x + 150, y + 12, 0, 0, 32, 32, 32, 32);
//            guiGraphics.blit(organDesc, x, y, 0, 0, 512, 256, 512, 256);
        }

        this.previousOrgan = this.selectedOrgan;
    }

    private void updateSelectedOrgan() {
        Item organSlot = this.menu.getOrganSlot().getItem();

        if (organSlot != previousItem) {
            this.buyButton.active = false;
            this.sellButton.active = true;
            this.organInSlot = true;

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
                this.organInSlot = false;
            } else {
                this.sellButton.active = false;
                this.organInSlot = false;
            }

            previousItem = organSlot;
        }
    }

    private WidgetSprites generateButtonSprite(String name) {
        return new WidgetSprites(
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn"),
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn_inac"),
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn_hl"));
    }

    public void setOrganPrice(int organPrice) {
        this.organPrice = organPrice;
    }

    public void buyOrgan(int index) {
        LOGGER.debug("Buy pressed");

        PacketDistributor.sendToServer(RequestCredits.INSTANCE);

        LOGGER.debug("credits: {}", credits);

        if (credits > organPrice) {
            LOGGER.debug("sufficient credits");

            if (this.menu.getOrganSlot() == ItemStack.EMPTY) {
                LOGGER.debug("Slot empty");

                PacketDistributor.sendToServer(new BuySellOrgan(this.selectedOrgan, 1));

                LOGGER.debug("demand packet sent");
            }
        }
    }

    public void sellOrgan() {
        LOGGER.debug("Sell function started");

        ItemStack organ = this.menu.getOrganSlot();
        boolean sellAprove = false;
        boolean priceCorrect = true;

        if (organ.is(ModItems.BRAIN)) {
            if (selectedOrgan != 0) {
                this.selectedOrgan = 0;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.HEART)) {
            if (selectedOrgan != 1) {
                this.selectedOrgan = 1;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.INTESTINES)) {
            if (selectedOrgan != 2) {
                this.selectedOrgan = 2;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.KIDNEY)) {
            if (selectedOrgan != 3) {
                this.selectedOrgan = 3;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.LIVER)) {
            if (selectedOrgan != 4) {
                this.selectedOrgan = 4;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.LUNGS)) {
            if (selectedOrgan != 5) {
                this.selectedOrgan = 5;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.PANCREAS)) {
            if (selectedOrgan != 6) {
                this.selectedOrgan = 6;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.STOMACH)) {
            if (selectedOrgan != 7) {
                this.selectedOrgan = 7;
                priceCorrect = false;
            }
            sellAprove = true;
        } else if (organ.is(ModItems.THYROID)) {
            if (selectedOrgan != 8) {
                this.selectedOrgan = 8;
                priceCorrect = false;
            }
            sellAprove = true;
        }

        LOGGER.debug("sell approve: {}", sellAprove);
        LOGGER.debug("priceCorrect: {}", priceCorrect);

        if (sellAprove) {
            PacketDistributor.sendToServer(RequestCredits.INSTANCE);

            if (!priceCorrect) {
                int oldPrice = this.organPrice;
                boolean wait = true;
                PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));

                LOGGER.debug("old price {}", oldPrice);
                LOGGER.debug("credits: {}", credits);

                while (wait) {
                    if (oldPrice != this.organPrice) {
                        wait = false;  // XXX: this will freeze game until packets have been received and updated
                    }
                }

                LOGGER.debug("wait over");
            }

            PacketDistributor.sendToServer(new BuySellOrgan(this.selectedOrgan, -1));
        }
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
