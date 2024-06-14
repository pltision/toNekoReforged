package yee.pltision.tonekoreforged.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

/**
 * 此类存放使用NekoStateApi操控NekoState的事件，若想查看命令相关的另见{@link ToNekoCommandEvent}。
 */
public class NekoStateEvent extends Event {

    public static class PlayerNekoStateEvent extends NekoStateEvent{
        final Player player;

        public PlayerNekoStateEvent(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

    }

    /**
     * 在NekoStateApi尝建为玩家立联系前触发事件。若取消事件原方法会返回false
     */
    @Cancelable
    public static class TryConnectPlayersEvent extends NekoStateEvent{
        final Player neko, owner;

        public Player getNeko() {
            return neko;
        }

        public Player getOwner() {
            return owner;
        }
        public TryConnectPlayersEvent(Player neko, Player owner){
            this.neko = neko;
            this.owner = owner;
        }
    }

    /**
     * 在NekoStateApi尝建为玩家移除猫娘前触发事件。若取消事件原方法会返回false
     */
    @Cancelable
    public static class TryRemoveNekoEvent extends PlayerNekoStateEvent {

        final UUID neko;

        boolean removeNekoStateWhenPlayerHaveNoOwner;

        public boolean doRemoveNekoStateWhenPlayerHaveNoOwner(){
            return removeNekoStateWhenPlayerHaveNoOwner;
        }

        public void setRemoveNekoStateWhenPlayerHaveNoOwner(boolean removeNekoStateWhenPlayerHaveNoOwner) {
            this.removeNekoStateWhenPlayerHaveNoOwner = removeNekoStateWhenPlayerHaveNoOwner;
        }

        public UUID getNeko() {
            return neko;
        }
        public TryRemoveNekoEvent(Player player, UUID neko,boolean removeNekoStateWhenPlayerHaveNoOwner) {
            super(player);
            this.neko = neko;
            this.removeNekoStateWhenPlayerHaveNoOwner=removeNekoStateWhenPlayerHaveNoOwner;
        }

    }

    /**
     * 在NekoStateApi尝建为玩家移除主人前触发事件。若取消事件原方法会返回false
     */
    @Cancelable
    public static class TryRemoveOwnerEvent extends PlayerNekoStateEvent {

        final UUID owner;

        boolean removeNekoStateWhenPlayerHaveNoOwner;

        public boolean doRemoveNekoStateWhenPlayerHaveNoOwner(){
            return removeNekoStateWhenPlayerHaveNoOwner;
        }

        public void setRemoveNekoStateWhenPlayerHaveNoOwner(boolean removeNekoStateWhenPlayerHaveNoOwner) {
            this.removeNekoStateWhenPlayerHaveNoOwner = removeNekoStateWhenPlayerHaveNoOwner;
        }

        public UUID getOwner() {
            return owner;
        }
        public TryRemoveOwnerEvent(Player player, UUID owner,boolean removeNekoStateWhenPlayerHaveNoOwner) {
            super(player);
            this.owner = owner;
            this.removeNekoStateWhenPlayerHaveNoOwner=removeNekoStateWhenPlayerHaveNoOwner;
        }
    }

    /**
     * 在NekoStateApi成功为玩家立联系前触发事件，若本来存在联系则不会触发。
     */
    public static class ConnectedPlayersEvent extends NekoStateEvent{
        final Player neko, owner;

        public Player getNeko() {
            return neko;
        }

        public Player getOwner() {
            return owner;
        }
        public ConnectedPlayersEvent(Player neko, Player owner){
            this.neko = neko;
            this.owner = owner;
        }
    }

    /**
     * 在NekoStateApi尝建为玩家移除猫娘后触发事件。
     */
    public static class RemovedNekoEvent extends PlayerNekoStateEvent {

        final UUID neko;
        final boolean removedNekoState;

        public boolean isRemovedNekoState(){
            return removedNekoState;
        }

        public UUID getNeko() {
            return neko;
        }
        public RemovedNekoEvent(Player player, UUID neko,boolean removedNekoState) {
            super(player);
            this.neko = neko;
            this.removedNekoState=removedNekoState;
        }
    }
    /**
     * 在NekoStateApi尝建为玩家移除猫娘后触发事件。
     */
    public static class RemovedOwnerEvent extends PlayerNekoStateEvent {

        final UUID owner;
        final boolean removedNekoState;

        public boolean isRemovedNekoState(){
            return removedNekoState;
        }

        public UUID getOwner() {
            return owner;
        }
        public RemovedOwnerEvent(Player player, UUID owner,boolean removedNekoState) {
            super(player);
            this.owner = owner;
            this.removedNekoState=removedNekoState;
        }
    }
}
