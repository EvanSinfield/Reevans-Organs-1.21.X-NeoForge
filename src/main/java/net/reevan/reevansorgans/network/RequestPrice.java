package net.reevan.reevansorgans.network;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.reevan.reevansorgans.ReevansOrgans;
import org.slf4j.Logger;


public record RequestPrice(int index) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestPrice> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "request_price"));

    public static final StreamCodec<ByteBuf, RequestPrice> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            RequestPrice::index,
            RequestPrice::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
