package net.reevan.reevansorgans.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.reevan.reevansorgans.ReevansOrgans;
import net.reevan.reevansorgans.credits.PlayerCredits;
import net.reevan.reevansorgans.item.ModItems;
import net.reevan.reevansorgans.network.RequestPrice;
import org.apache.logging.log4j.core.jmx.Server;
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
    int selectedOrgan;
    int organPrice = 0;
    boolean organInSlot = false;

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
        Component estimateText;

        renderOrgan(guiGraphics);

        guiGraphics.drawString(font, Component.literal("Cost: " + this.organPrice), x + 150, y + 58, 0xFFFFFF);

        if (organInSlot) {
            ItemStack organ = this.menu.getOrganSlot();

            int estimateNum = Math.round(((100 - organ.getDamageValue()) / 100f) * organPrice);

            estimateText = Component.literal("Aprox. value: " + estimateNum);
        } else {
            estimateText = Component.literal("Aprox. value: N/A");
        }

        guiGraphics.drawString(font, estimateText, x + 150, y + 67, 0xFFFFFF);
        guiGraphics.drawString(font, getOrganDesc(this.selectedOrgan), x + 185, y + 12, 0xFFFFFF);
    }

    public String getOrganDesc(int index) {
        switch (index) {
            case 0:
                return "One of the rarer organs, one of these fuckers is in you right now.";
            case 1:
                return "The fact that you're currently selling one is ironic. 4 chambers of pure money.";
            case 2:
                return "Big or small, its still the same shit tube. Fluctuates in price wildly.";
            case 3:
                return "Deals with piss for you. Quite tasty in a pinch and.";
            case 4:
                return "Helps keep you alive after 15 shots of tequila. Or doctor pepper. Pick your poison.";
            case 5:
                return "Big wet meat balloons. We do not accept smokers or asbestos, sorry for any inconvenience.";
            case 6:
                return "The insulin is probably worth more than the pancreas, but money is money.";
            case 7:
                return "Deals with all the shit you eat. 20kg of Ben and Jerrys does de-value this.";
            case 8:
                return "F tier organ. Really susceptible to cancer for some reason.";

            default: return "BAd N/A";
        }
    }

    public void renderOrgan(GuiGraphics guiGraphics) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (this.selectedOrgan != this.previousOrgan) {
            switch (this.selectedOrgan) {
                case (0):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/brain.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (1):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/heart.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (2):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/intestines.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (3):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/kidney.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (4):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/liver.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (5):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/lungs.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (6):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/pancreas.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (7):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/stomach.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                case (8):
                    this.organTexture = ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "textures/item/thyroid.png");
                    PacketDistributor.sendToServer(new RequestPrice(this.selectedOrgan));
                    break;
                default:
                    this.organTexture = null;
            }
        }

        if (organTexture != null) {
            guiGraphics.blit(organTexture, x + 150, y + 12, 0, 0, 32, 32, 32, 32);
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

    private WidgetSprites generateSprite(String name) {
        return new WidgetSprites(
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn"),
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn_inac"),
                ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, name+"_btn_hl"));
    }

    public void setOrganPrice(int organPrice) {
        this.organPrice = organPrice;
    }

    public void buyOrgan(int index) {
        int credits = this.minecraft.player.getPersistentData().getInt("credits");

        if (credits > organPrice) {
            if (this.menu.getOrganSlot() != ItemStack.EMPTY) {
                ItemStack organ;

                this.minecraft.player.getPersistentData().putInt("credits", credits - organPrice);

                switch (this.selectedOrgan) {
                    case 0:
                        organ = ModItems.BRAIN.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 1:
                        organ = ModItems.HEART.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 2:
                        organ = ModItems.INTESTINES.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 3:
                        organ = ModItems.KIDNEY.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 4:
                        organ = ModItems.LIVER.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 5:
                        organ = ModItems.LUNGS.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 6:
                        organ = ModItems.PANCREAS.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 7:
                        organ = ModItems.STOMACH.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    case 8:
                        organ = ModItems.THYROID.toStack();
                        organ.setDamageValue(random.nextInt());
                        break;
                    default:
                        organ = ItemStack.EMPTY;
                }

                this.menu.setOrganSlot(organ);
            }
        }
    }
}
