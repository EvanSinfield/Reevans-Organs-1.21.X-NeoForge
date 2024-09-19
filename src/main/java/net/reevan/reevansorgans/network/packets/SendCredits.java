package net.reevan.reevansorgans.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.reevan.reevansorgans.ReevansOrgans;

public record SendCredits(int credits) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendCredits> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "send_credits"));

    public static final StreamCodec<ByteBuf, SendCredits> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SendCredits::credits,
            SendCredits::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
