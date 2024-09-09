## Description
Complete rewrite of toNeko that can turns the player into a catgirl in Forge or NeoForge, and add a bit of fun to the game.

Now the mod is not enough a rewrite, I also want to add something different in it.

## Feature & Feature Features

### To Neko

Restores toNeko of changing into a neko.

- Let a player to one's neko, you also can be your neko (configurable).
- Add a pet phrase to end of player's conversation, like [Pet Phrase](https://www.mcmod.cn/class/7100.html).
- Use cat stick to hit player can get exp of them (but exp can do nothing now).
- A rite that can change someone to your neko.
- A lot of configuration and commands。

#### Commands
<details>  
<summary>Open command descriptions</summary>  

```mcfunction none
#Get help
toneko help

#Get your all nekos
toneko getNeko
#Set <player> to your neko or send request
toneko getNeko <player>

#Get your all owners
toneko getOwner
#Set <player> to your owner or send request
toneko getOwner <player>

#Remove a neko with a name or UUID with <player> or send request
toneko removeNeko <player>
#Remove a owner with a name or UUID with <player> or send request
toneko removeOwner <player>

#Get the pet phrase of the executor or <player>
toneko petPhrase [<player>]

#Set the pet phrase for <player>
#When <ignore_english> is true, if all character in message <=255 will not append pet phrase to message, false for default
#Ignore <ignore_after> character to the pet phrase after
toneko petPhrase <player> <phrase> [<ignore_english>] [<ignore_after>]

#Clear pet phrase for <player>
toneko petPhrase <player> ""

#Accept request from <player>
toneko accept <player>

#Deny request from <player>
toneko deny <player>

#Check is enable neko rite and see how to use
toneko nekoRite
```

</details>

### Baubles
- Tail and ears, they can be dye.
- I also want to make some clothing, but I think modeling them is too hard for me, nah.

Baubles can use on armor equipment slots, also they can use in Curios.

### Collar
I am adding a functional collar, maybe that's the differential.
- You can craft it whit some leather and iron, use can open a menu to add some bauble in it.
- Collar has its own slot, put your mouse on head slot then you can see it. But if you installed curios you can put it in a collar equip in Curios. Whether to use necklace in default Curios or add a new collar equip, I am considering for that.
- There's only a bell for bauble in collar now. It can make some ringtone when you're walking (not really noisy, I think). Maybe can add more feature in it or make more bell, like draw enemies' attention or glowing effect.
- I will add more bauble for collar, like a teleporter can teleport a player that equipping it to another player.
- Try to let lead can use to player (that wearing collar (or not) ).

### Rob Shear
Rob Shear is an enchantment that use to shears.

It can shear armor or collar on player. It can use with neko's owner (or not) or a dispenser block, all are configurable.

Maybe it can use on Binding Curse? I am considering for that. Btw, use dispenser to tricks is so fun, nya!
