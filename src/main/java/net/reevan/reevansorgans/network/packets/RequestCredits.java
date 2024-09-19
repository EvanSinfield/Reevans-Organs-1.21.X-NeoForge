package net.reevan.reevansorgans.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.reevan.reevansorgans.ReevansOrgans;

public class RequestCredits implements CustomPacketPayload {
    public static final RequestCredits INSTANCE = new RequestCredits();

    public static final Type<RequestCredits> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ReevansOrgans.MODID, "request_credits"));

public static final StreamCodec<ByteBuf, RequestCredits> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private RequestCredits() {

    }

    public void handle(final IPayloadContext ctx) {
        int credits = ctx.player().getPersistentData().getInt("credits");

        PacketDistributor.sendToPlayer((ServerPlayer) ctx.player(), new SendCredits(credits));
    }
}
