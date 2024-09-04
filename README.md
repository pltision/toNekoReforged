# toNeko Reforged
## 简单的介绍
喵喵喵?

模组的玩法非常的简单啊~ 

首先使用 ```/toneko getNeko``` 向一个玩家发出请求，当玩家接收请求后变成你的猫娘。然后我们要依次将木棍和避雷针或者末地烛放进合成栏，之后你就可以合成出一根撅猫棒。使用撅猫棒攻击你的猫娘可以增加你们的好感度（好感度目前意义不明）~

模组的玩法就是这么的简单~ 管你听没听懂，撅猫娘就完啦！

## 简介
这是toNeko的Forge重制版，在Forge中实现了部分与原作类似的功能，但目前以准备加入更多的原创内容。

## 目前的功能

### 变猫部分

还原了toNeko旧版变猫娘的内容，可以部分修改聊天信息等。

- 让玩家成为另一个玩家的猫娘，也可以成为自己的猫娘（可配置）。
- 在玩家的每句话后面添加固定的口癖，类似于 [Pet Phrase](https://www.mcmod.cn/class/7100.html) 模组。
- 使用撅猫棒攻击猫娘获得好感度（虽然目前没有任何作用）。
- 较丰富的配置文件和命令。

#### 命令
<details>  
<summary>展开命令内容</summary>  

```mcfunction none
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
toneko deny <player>

#你还可以使用execute来指定执行者喵~
```

</details>

### 装饰品
- 一个尾巴和耳朵饰品，可被染色。
- 我还想加入衣物之类的东西，不过感觉没那建模水平喵。

### 项圈
模组添加一个类有功能性的项圈，目前来看这是个创新点。
- 可以用皮革和铁锭做一个项圈，打开它可以放入饰品，计划制作更多种类的项圈和支持染色。
- 目前项圈的饰品只有一个铃铛，它的功能是让走动时可以发出铃声（自我感觉不太吵）。仍可能对其加入更多功能，如吸引敌对生物，发光效果等...
- 预计加入更多饰品，如传送器，可以将玩家传送到另一个玩家旁。
- 正在尝试实现让拴绳可以对（带有项圈的）玩家使用。
