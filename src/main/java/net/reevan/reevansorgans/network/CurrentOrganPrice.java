package net.reevan.reevansorgans.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.reevan.reevansorgans.ReevansOrgans;


public record CurrentOrganPrice(int index, int price) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CurrentOrganPrice> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "current_price"));

    public static final StreamCodec<ByteBuf, CurrentOrganPrice> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CurrentOrganPrice::index,
            ByteBufCodecs.VAR_INT,
            CurrentOrganPrice::price,
            CurrentOrganPrice::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
