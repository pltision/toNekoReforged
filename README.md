# toNeko Reforged
## 简单的介绍
喵喵喵?

模组的玩法非常的简单啊~ 

首先使用 ```/toneko getNeko``` 向一个玩家发出请求，当玩家接收请求后变成你的猫娘。然后我们要依次将木棍和避雷针或者末地烛放进合成栏，之后你就可以合成出一根撅猫棒。使用撅猫棒攻击你的猫娘可以增加你们的好感度~

模组的玩法就是这么的简单~管你听没听懂，撅猫娘就完啦！

## 简介
### 目前的功能
- 可以让玩家成为另一个玩家的猫娘，也可以成为自己的猫娘。
- 可以在玩家的每句话后面添加固定的口癖，类似于 [Pet Phrase](https://www.mcmod.cn/class/7100.html) 模组。
- 可以使用撅猫棒攻击猫娘获得好感度。（目前没有任何作用）
- 较丰富的配置文件和命令。

## 命令
```mcfunction none
#获取玩家的全部猫娘
/toneko getNeko
#将<player>设为执行者的猫娘或发送请求
/toneko getNeko <player>

#获取玩家的全部主人
/toneko getOwner
#将<player>设为执行者的主人或发送请求
/toneko getOwner <player>

#为执行者移除名称或UUID为<player>的猫娘或发送请求
/toneko removeNeko <player>
#为执行者移除名称或UUID为<player>的主人或发送请求
/toneko removeOwner <player>

#获取执行者或<player>的口癖
/toneko petPhrase [<player>]
#设置<player>的口癖，当执行者是<player>或其主人时方可设置
#当<ignore_english>为true时，在对话中若所有字符的值<=255则不会添加口癖。若不填写则默认为若<phrase>所有字符的值<=255为false，否则为true
#<ignore_after>为检查是否添加口癖时可以忽略口癖前的几个字符
/toneko petPhrase <player> <phrase> [<ignore_english>] [<ignore_after>]

#获取执行者与<player>之间的好感经验值
/toneko getExp <player>
#设置<player1>与<player2>之间的好感经验值，需要命令权限
/toneko setExp <player1> <player2> <value>

#同意<player>发出的请求
/toneko accept <player>

#拒绝<player>发出的请求
/toneko deny <player>
```
