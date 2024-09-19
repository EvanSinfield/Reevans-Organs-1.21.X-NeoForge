package net.reevan.reevansorgans.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.reevan.reevansorgans.ReevansOrgans;

public record BuySellOrgan(int index, int demand) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BuySellOrgan> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "change_demand"));

    public static final StreamCodec<ByteBuf, BuySellOrgan> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            BuySellOrgan::index,
            ByteBufCodecs.VAR_INT,
            BuySellOrgan::demand,
            BuySellOrgan::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
