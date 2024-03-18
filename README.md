# toNeko Reforged
## 简单的介绍
喵喵喵?

模组的玩法非常的简单啊~ 

首先使用 ```/toneko getNeko``` 向一个玩家发出请求，当玩家接收请求后变成你的猫娘。然后我们要依次将木棍和避雷针或者末地烛放进合成栏，之后你就可以合成出一根撅猫棒。使用撅猫棒攻击你的猫娘可以增加你们的好感度~

模组的玩法就是这么的简单~管你听没听懂，撅猫娘就完啦！

## 简介
这是toNeko的Forge重制版，在Forge中实现了部分与原作类似的功能。

本模组与原作一样为仅服务端模组，仅需要在服务器即可。若客户端安装则可以根据客户端的语言本地化，若仅安装在服务端可以修改配置文件以本地化。
（你也可以向客户端发送汉化材质包来汉化，但应该不会有人需要用这个mod开服务器吧...?）

### 目前的功能
- 可以让玩家成为另一个玩家的猫娘，也可以成为自己的猫娘。
- 可以在玩家的每句话后面添加固定的口癖，类似于 [Pet Phrase](https://www.mcmod.cn/class/7100.html) 模组。
- 可以使用撅猫棒攻击猫娘获得好感度。（目前没有任何作用）
- 较丰富的配置文件和命令。

## 命令
```mcfunction
#查看帮助
toneko help

#获取玩家的全部猫娘
toneko getNeko
#将<player>设为执行者的猫娘或发送请求
toneko getNeko <player>

#获取玩家的全部主人
toneko getOwner
#将<player>设为执行者的主人或发送请求
toneko getOwner <player>

#为执行者移除名称或UUID为<player>的猫娘或发送请求
toneko removeNeko <player>
#为执行者移除名称或UUID为<player>的主人或发送请求
toneko removeOwner <player>

#获取执行者或<player>的口癖
toneko petPhrase [<player>]
#设置<player>的口癖，当执行者是<player>或其主人时方可设置
#当<ignore_english>为true时，在对话中若所有字符的值<=255则不会添加口癖。若不填写则默认为若<phrase>所有字符的值<=255为false，否则为true
#<ignore_after>为检查是否添加口癖时可以忽略口癖前的几个字符
toneko petPhrase <player> <phrase> [<ignore_english>] [<ignore_after>]

#获取执行者与<player>之间的好感经验值
toneko getExp <player>
#设置<player1>与<player2>之间的好感经验值，需要命令权限
toneko setExp <player1> <player2> <value>

#同意<player>发出的请求
toneko accept <player>

#拒绝<player>发出的请求
toneko deny <player>'

#你还可以使用execute来指定执行者喵~
```

## 期望
我希望能让这个模组“更加的模组”，比如不要像插件一样通过命令或者菜单来完成所有的事情，以及我希望为它添加更多的功能和独立的玩法，比如我在里面添加了一个变猫仪式，还有准备添加一些互动类玩法。

目前开发热情不高，没咋碰这个项目了，今天跑回来画个饼。

## 即将到来

- 抚摸及更多玩家间的互动。
- 项圈。我想添加一个存在感很强的项圈，当玩家没有头盔时它会出现在头盔槽位，但这么做似乎需要mixin，但我不想使用mixin因为那会使兼容性变差（此外也是懒得研究mcp）。如果有人知道怎么做的话来支持一下我qwq，需求是这个项圈仅显示在客户端，不会影响发射器和其他模组使用Inventory来获取信息，最好别用mixin喵。
