package net.reevan.reevansorgans.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.reevan.reevansorgans.ReevansOrgans;

import java.io.Serializable;

public record ChangeOrganDemand(int index, int demand) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ChangeOrganDemand> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "change_demand"));

    public static final StreamCodec<ByteBuf, ChangeOrganDemand> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ChangeOrganDemand::index,
            ByteBufCodecs.VAR_INT,
            ChangeOrganDemand::demand,
            ChangeOrganDemand::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
