package yee.pltision.tonekoreforged.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import yee.pltision.tonekoreforged.ToNeko;

public class NekoNetworks {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ToNeko.location("set_collar"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register(){
        int i=0;
        INSTANCE.registerMessage(i++, CCollarStateChangePacket.class, CCollarStateChangePacket::encode,CCollarStateChangePacket::new, CCollarStateChangePacket::handle);
        INSTANCE.registerMessage(i++, SSetCollarSlotPacket.class, SSetCollarSlotPacket::encode,SSetCollarSlotPacket::new, SSetCollarSlotPacket::handle);
        INSTANCE.registerMessage(i/*++*/, SSetCollarSlotCreativePacket.class, SSetCollarSlotCreativePacket::encode,SSetCollarSlotCreativePacket::new, SSetCollarSlotCreativePacket::handle);
    }
}
