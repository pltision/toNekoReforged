package yee.pltision.tonekoreforged.nekostate.object;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.nekostate.command.CommandExceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class NekoRequest {
    /**
     * key为owner，value为neko
     */
    public static final Map<UUID,NekoRequest> REQUESTS = new HashMap<>();

    public UUID sendTo;
    public int requestTick;
    public int requestClod;
    public WhenAccept whenAccept;
    public boolean denied=false;

    @SubscribeEvent
    public static void serverStarting(ServerStartedEvent event){
        REQUESTS.clear();
    }

    public NekoRequest(UUID sendTo,int requestTick,int requestClod,WhenAccept whenAccept){
        this.sendTo=sendTo;
        this.requestTick=requestTick;
        this.requestClod=requestClod;
        this.whenAccept=whenAccept;
    }

    public static boolean tryAccept(CommandSourceStack source, ServerPlayer sender, ServerPlayer accept) throws CommandSyntaxException{
        NekoRequest request=REQUESTS.get(sender.getUUID());
        if(request==null) return false;
        if(request.denied)return false;
        if(request.requestTick+ Config.maxAcceptTime<source.getServer().getTickCount()) return false;
        if(!request.sendTo.equals(accept.getUUID()))return false;
        request.whenAccept.accept(source,sender,accept);
        REQUESTS.remove(sender.getUUID());
        return true;
    }

    public static boolean trySend(MinecraftServer server, ServerPlayer sender, ServerPlayer to,WhenAccept whenAccept){
        NekoRequest request=REQUESTS.get(sender.getUUID());
        if(request==null) {
            REQUESTS.put(sender.getUUID(),new NekoRequest(to.getUUID(), server.getTickCount(), Config.maxAcceptTime,whenAccept));
        }
        else {
            if(request.requestTick+ request.requestClod>server.getTickCount()) return false;
            request.denied=false;
            request.requestTick=server.getTickCount();
            request.whenAccept=whenAccept;
            request.sendTo=to.getUUID();
        }
        return true;
    }

    public static void trySendAndReturn(CommandSourceStack source, ServerPlayer sender, ServerPlayer to, WhenAccept whenAccept, MutableComponent info)throws CommandSyntaxException{
        if(trySend(source.getServer(),sender,to,whenAccept)){
            to.sendSystemMessage(Component.empty().append(info).append(" ")
                    .append(Lang.ACCEPT_REQUEST_BUTTON.component()
                            .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/toneko accept "+sender.getName().getString()))
                                    .withColor(TextColor.parseColor("#00ff00")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Lang.REQUEST_COMMAND_INFO.component("/toneko accept "+sender.getName().getString())))
                            ))
                    .append(" ")
                    .append(Lang.DENY_REQUEST_BUTTON.component()
                            .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/toneko deny "+sender.getName().getString()))
                                    .withColor(TextColor.parseColor("#ff0000")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Lang.REQUEST_COMMAND_INFO.component("/toneko deny "+sender.getName().getString())))
                            ))
            );
            source.sendSuccess(() -> Lang.SEND_REQUEST_INFO.component(to.getName()), false);
        }
        else{
            throw CommandExceptions.SEND_REQUEST_COOLING.create();
        }

    }

    public static boolean deny(MinecraftServer server,ServerPlayer sender,ServerPlayer deny){
        NekoRequest request=REQUESTS.get(sender.getUUID());
        if(request==null)return false;
        if(!request.sendTo.equals(deny.getUUID()))return false;
        if(request.denied)return false;
        if(request.requestTick+ request.requestClod<=server.getTickCount()) return false;
        request.denied=true;
        request.requestClod=Integer.min(20*60*20, request.requestClod*2);
        return true;
    }


    @FunctionalInterface
    public interface WhenAccept{
        void accept(CommandSourceStack source,ServerPlayer sender,ServerPlayer accept)throws CommandSyntaxException;
    }

}
