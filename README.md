<font size=7 color=YellowGreen>pokepmp</br></font>
<font size=5>一款为spongeForge1.12.2服务端开发的宝可梦排位插件</br></font>

<font size=7 color=YellowGreen>命令与权限：</font>

<font size=5 color="red">管理员命令:</font></br>
<font size=5 color="red">/pmp reload </font>重新加载配置文件</br>
<font size=5 color="red">/pmp resettop </font>重置排行榜</br>
<font size=5 color="red">/pmp ban 玩家ID </font>禁止某个玩家排位</br>
<font size=5 color="red">/pmp unban 玩家ID </font>解禁玩家排位权限</br>
<font size=5 color="red">/pmp set 玩家ID 积分 </font>设置玩家积分</br>
<font size=5 color="red">/pmp add 玩家ID 积分 </font>为玩家添加积分</br></br>
<font size=5 color="YellowGreen">用户命令：</br></font>
<font size=5 color="YellowGreen">/pmp me </font>查看个人排位信息</br>
<font size=5 color="YellowGreen">/pmp join </font>加入排位列表</br>
<font size=5 color="YellowGreen">/pmp quit </font>退出排位列表</br>
<font size=5 color="YellowGreen">/pmp top </font>查看排位排行榜</br>

<font size=6 color="YellowGreen">Placeholders 占位符</font>

<font size=4 color="blue">%pmp_score%</font> 显示当前玩家积分</br>
<font size=4 color="blue">%pmp_rank%</font> 显示当前玩家段位</br>
<font size=4 color="blue">%pmp_wins%</font> 显示当前玩家胜利数</br>
<font size=4 color="blue">%pmp_losses%</font> 显示当前玩家失败数</br>

<font size=5 color="YellowGreen">获取排名用户占位符：以下为获取第8名写法</font>

<font size=4 color="blue">%pmp_8_name%</font> 显示排行榜第8名玩家ID</br>
<font size=4 color="blue">%pmp_8_score%</font> 显示排行榜第8名玩家积分</br>
<font size=4 color="blue">%pmp_8_rank%</font> 显示排行榜第8名玩家段位</br>
<font size=4 color="blue">%pmp_8_wins%</font> 显示排行榜第8名玩家胜场数</br>
<font size=4 color="blue">%pmp_8_losses%</font> 显示排行榜第8名玩家失败数</br>

<font color=Yellow>配置玩家</font>

对战Ban位设置 {
# 为true时禁止所有神兽 #为false最大允许携带几只神兽参赛
Legendary {
"false"=3
}
# 禁止使用以下特性参战 #请注意特性名称严格遵守大小写
ability=[
test,
test2
]
# 禁止以下宝可梦参战 #请注意宝可梦名称严格遵守大小写
banPokemon=[
test,
test2
]
# 禁止使用以下技能参战 #请注意技能名称严格遵守大小写
move=[
test,
test2
]
}
排位时endbattle命令监听重置 {
# 启用控制台权限执行命令 #设置为 false 排位战斗中不修改此命令
Console=false
# 是否阻止同IP玩家对战
TheIP=true
# 执行命令 #用于排位监听玩家，防止卡界面/卡对战
endbattle="kick %playe% &e防止卡对战界面重新进一下游戏吧"
# 是否传送玩家
stp=true
}
排位规则配置 {
# 启用OU分级规则
OU=false
# 回合时间
Time=20
# 背包规则
bag=true
# 接棒规则
batonpass=false
# 接棒仅1次规则
batonpass1=false
# 喋喋不休规则
chatter=false
# 降雨规则
drizzle=false
# 降雨-悠游自如规则
drizzleswim=false
# 日照规则
drought=false
# 无限战斗规则
endlessbattle=false
# 闪避招式规则
evasion=false
# 一站到底规则
forfeit=false
# 治疗回复
fullHeal=false
# 反转对战规则
inverse=false
# 携带物品规则
item=false
# 最大等级
levelCap=120
# 超级进化规则
mega=false
# 宝可梦数量
numpokemon=1
# 一击必杀规则
ohko=false
# 种类规则
pokemon=true
# 提高到上限
raisetocap=true
# 催眠规则
sleep=true
# 虚张声势规则
swagger=false
}
提示信息配置 {
# 携带的宝可梦全部不合格提示
PokeEmpty="你携带的宝可梦全部不符合排位规则\n&d规则：\n&7%rules%"
# 排行榜信息: 支持 %winne% %loser%
TOP="(§a%i%§r) §d玩家: §r%name% §d积分: §r%score% §d段位: %rank%"
# 排行榜标题信息
TOPtitle="&7&ki&6&ki&5&ki&4&ki&3&ki&2&ki&1&ki§d+ + + + + &e&l段位排行榜 &1&ki&2&ki&3&ki&4&ki&4&ki&5&ki&7&ki"
# 战斗胜利信息
VICTORY="§e玩家 §a%winner% §d在排位中战胜了 §e玩家 §a%loser%"
# 为玩家添加积分提示
addScore="成功给 &a%name% &6增加积分: &a%score%"
# 管理员禁赛玩家时提示
ban="你已将玩家&a %name% &e加入了禁赛列表"
# 管理员禁赛玩家时已经存在提示
ban1="玩家&a %name% &e早已经在禁赛列表了"
# 等待中队伍不符合规则被踢出
check=你携带的宝可梦不符合规则被移出排位列表
# 携带了过多蛋被提示
checkEgg=你不能携带这么多蛋来参赛
# 携带了过多神兽提示
checkLeg="你只能携带 %leg% 只神兽参赛"
# 加入排位时提示信息
join="§a排位 §4// §e玩家 %name% §a加入了排位列表 !\n§a点击 §d接受挑战   &7排队人数： %number%"
# 加入排位时悬浮信息
join1="§e玩家 %name% §6的战绩:  \n§e段位：%rank% \n§7积分:§a%score% §7胜场:§a %winne% §7败场:§a%loser% §7胜率: §a%=%§7%\n§d规则：§7%rules%"
# 已经在排位列表提示信息
join2=你已经在排位列表了
# 失败扣分提示信息
loserMsg="很遗憾本次排位你失败了：积分 -&a%score%"
# 查询个人战绩信息
me="&e排位的个人战绩：\n§r> &7当前段位：%rank%\n§r> &7当前积分：§a%score%\n§r> &7胜场数：§a%winne%\n§r> &7败场数：§a%loser%\n§r> &7胜率：§a%=%%"
# 执行必须由玩家执行的命令不是玩家时提示
notPlayer="&c该命令只能玩家执行"
# 被禁赛玩家提示
onBan="你已被禁赛，请确认是否存在恶意行为"
# 被禁赛玩家提示
onBan1="你已被系统自动禁赛10分钟，等你变强了再来吧"
# 提示玩家对手是谁
pvp="&e对战开始你的对手是&a %name%"
# 退出排位时提示信息
quit=你已经退出排位列表了
# 没有加入退出时提示信息
quit2=你还没有加入排位列表
# 排位即将开始玩家1却在对战中被提出排位提示
quit3="&a由于你处在战斗中已经自动帮你退出了排位列表"
# 重载配置文件提示
reload="&6配置文件已经重新加载"
# 重置排行榜提示
resettop="&e所有玩家排位数据已经重置"
# 设置玩家积分提示
setScore="成功把 &a%name% &6的积分设置为: &a%score%"
# 扣除玩家积分提示提示
takeScore="成功扣除 &a%name% &6的 &a%score% 积分"
# 管理员解除禁赛玩家提示
unban="你已将玩家&a %name% &e解禁了"
# 管理员解除的玩家不在ban位提示
unban1="玩家&a %name% &e还不在禁赛列表"
# 胜利加分提示信息
winneMsg="恭喜你取得了本次排位的胜利，积分 +&a%score%"
# 连胜胜利加分提示信息
winneWinningStreakMsg="§6玩家 §a%winner% §d%s% §6连胜，积分附加 §d%s%"
}
段位配置 {
# 积分=段位
ranks {
"0"="&7[&3青铜&7]"
"10"="&7[&2白银&7]"
"40"="&7[&e黄金&7]"
"100"="&7[&b钻石&7]"
"300"="&7[&d星耀&7]"
"600"="&7[&5王者&7]"
"1000"="&7[&c荣耀&7]"
}
}
胜负积分配置 {
# 连胜加分 #连胜每场连续加分值
WinningStreak=1
# 连胜加分多加分 #连胜最多加分
WinningStreakMax=10
# 默认加分 #当对应段位为空或不存在时所有默认
add=5
# 默认扣分 #当对应段位为空或不存在时所有默认
take=5
}
