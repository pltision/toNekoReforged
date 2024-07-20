package yee.pltision.tonekoreforged.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import yee.pltision.tonekoreforged.nekostate.common.PetPhrase;

import java.util.UUID;

public class ToNekoCommandEvent extends Event {

    CommandSourceStack commandSourceStack;

    CommandSyntaxException exception=null;

    ToNekoCommandEvent(CommandSourceStack commandSourceStack){
        this.commandSourceStack = commandSourceStack;
    }

    public void setException(CommandSyntaxException exception) {
        this.exception = exception;
    }

    public CommandSyntaxException getException() {
        return exception;
    }

    public CommandSourceStack getCommandSourceStack() {
        return commandSourceStack;
    }

    public static class BiCommandEvent extends ToNekoCommandEvent{
        Player target;

        boolean sendRequest;

        public boolean shouldSendRequest(){
            return sendRequest;
        }
        public void setSendRequest(boolean doSend){
            this.sendRequest=doSend;
        }

        BiCommandEvent(CommandSourceStack sourceStack,Player target,boolean defaultNeedRequest) {
            super(sourceStack);
            this.target=target;
            this.sendRequest =defaultNeedRequest;
        }

        public Player getTarget() {
            return target;
        }
    }
    public static class UUIDBiCommandEvent extends ToNekoCommandEvent{
        UUID target;
        String origin;

        boolean sendRequest;

        public boolean shouldSendRequest(){
            return sendRequest;
        }
        public void setSendRequest(boolean doSend){
            this.sendRequest=doSend;
        }

        UUIDBiCommandEvent(CommandSourceStack sourceStack,UUID target,String origin,boolean defaultNeedRequest) {
            super(sourceStack);
            this.target=target;
            this.sendRequest =defaultNeedRequest;
        }

        public UUID getTarget() {
            return target;
        }

        public String getOrigin() {
            return origin;
        }
    }

    @Cancelable
    public static class AddNekoToExecutorCommandEvent extends BiCommandEvent{

        boolean ignoreDefaultException=false;

        public boolean isIgnoreDefaultException() {
            return ignoreDefaultException;
        }

        public void setIgnoreDefaultException(boolean ignoreDefaultException) {
            this.ignoreDefaultException = ignoreDefaultException;
        }

        public AddNekoToExecutorCommandEvent(CommandSourceStack sourceStack, Player target, boolean defaultNeedRequest) {
            super(sourceStack, target, defaultNeedRequest);
        }
    }

    @Cancelable
    public static class AddOwnerToExecutorCommandEvent extends BiCommandEvent{

        boolean ignoreDefaultException=false;

        public boolean isIgnoreDefaultException() {
            return ignoreDefaultException;
        }

        public void setIgnoreDefaultException(boolean ignoreDefaultException) {
            this.ignoreDefaultException = ignoreDefaultException;
        }

        public AddOwnerToExecutorCommandEvent(CommandSourceStack sourceStack, Player target, boolean defaultNeedRequest) {
            super(sourceStack, target, defaultNeedRequest);
        }
    }

    @Cancelable
    public static class ListNekoOfExecutorCommandEvent extends ToNekoCommandEvent{
        public ListNekoOfExecutorCommandEvent(CommandSourceStack commandSourceStack) {
            super(commandSourceStack);
        }
    }

    @Cancelable
    public static class ListOwnerOfExecutorCommandEvent extends ToNekoCommandEvent{
        public ListOwnerOfExecutorCommandEvent(CommandSourceStack commandSourceStack) {
            super(commandSourceStack);
        }
    }

    @Cancelable
    public static class RemoveNekoToExecutorCommandEvent extends UUIDBiCommandEvent{
        public RemoveNekoToExecutorCommandEvent(CommandSourceStack sourceStack, UUID target,String origin, boolean defaultNeedRequest) {
            super(sourceStack, target, origin, defaultNeedRequest);
        }
    }

    @Cancelable
    public static class RemoveOwnerToExecutorCommandEvent extends UUIDBiCommandEvent{
        public RemoveOwnerToExecutorCommandEvent(CommandSourceStack sourceStack, UUID target,String origin, boolean defaultNeedRequest) {
            super(sourceStack, target, origin, defaultNeedRequest);
        }
    }

    public static class SetPetPhraseEvent extends ToNekoCommandEvent{
        PetPhrase newPhrase=null;
        String phrase;
        boolean ignoreEnglish;
        int ignoreAfter;
        ServerPlayer target;
        public String getPhrase(){
            return phrase;
        }
        public boolean isIgnoreEnglish(){
            return ignoreEnglish;
        }
        boolean ignoreDefaultException=false;

        public boolean isIgnoreDefaultException() {
            return ignoreDefaultException;
        }

        public ServerPlayer getTarget() {
            return target;
        }

        public SetPetPhraseEvent(CommandSourceStack commandSourceStack, ServerPlayer target, String phrase, boolean ignoreEnglish, int ignoreAfter) {
            super(commandSourceStack);
            this.phrase=phrase;
            this.ignoreEnglish =ignoreEnglish;
            this.ignoreAfter=ignoreAfter;
            this.target=target;
        }
    }


}
