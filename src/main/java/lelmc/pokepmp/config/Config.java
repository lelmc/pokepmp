package lelmc.pokepmp.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Config {

    @Setting("段位配置")
    public ranksConfig ranks;

    @Setting("提示信息配置")
    public Messages messages;

    @Setting("排位规则配置")
    public rulesConfig rulesConfig;

    @Setting("对战Ban位设置")
    public BanConfig BanConfig;

    @Setting("胜负积分配置")
    public ScoreConfig ScoreConfig;

    @Setting("排位时endbattle命令监听重置")
    public command comm;

    public Config() {
        ranks = new ranksConfig();
        messages = new Messages();
        rulesConfig = new rulesConfig();
        BanConfig = new BanConfig();
        ScoreConfig = new ScoreConfig();
        comm = new command();
    }

    @ConfigSerializable
    public static class command {
        @Setting(comment = "启用控制台权限执行命令 #设置为 false 排位战斗中不修改此命令")
        public boolean Console = false;

        @Setting(comment = "执行命令 #用于排位监听玩家，防止卡界面/卡对战")
        public String endbattle = "kick %playe% &e防止卡对战界面重新进一下游戏吧";

        @Setting(comment = "是否传送玩家")
        public boolean stp = true;

        @Setting(comment = "是否阻止同IP玩家对战")
        public boolean TheIP = true;
    }

    @ConfigSerializable
    public static class ranksConfig {
        @Setting(comment = "积分=段位")
        public Map<String, String> ranks = new HashMap<String, String>() {{
            put("0", "&7[&3青铜&7]");
            put("10", "&7[&2白银&7]");
            put("40", "&7[&e黄金&7]");
            put("100", "&7[&b钻石&7]");
            put("300", "&7[&d星耀&7]");
            put("600", "&7[&5王者&7]");
            put("1000", "&7[&c荣耀&7]");
        }};
    }

    @ConfigSerializable
    public static class ScoreConfig {
        @Setting(comment = "默认加分 #当对应段位为空或不存在时所有默认")
        public int add = 5;

        @Setting(comment = "默认扣分 #当对应段位为空或不存在时所有默认")
        public int take = 5;

        @Setting(comment = "连胜加分 #连胜每场连续加分值")
        public int WinningStreak = 1;
        @Setting(comment = "连胜加分多加分 #连胜最多加分")
        public int WinningStreakMax = 10;

        /*@Setting(comment = "对应段位 加积分 #必须和自己配置的段位保持一致")
        public Map<String, Integer> addScore = new HashMap<String, Integer>() {{
            put("&7[&3青铜&7]", 5);
            put("&7[&e白银&7]", 5);
            put("&7[&e黄金&7]", 5);
            put("&7[&b钻石&7]", 5);
            put("&7[&d星耀&7]", 5);
            put("&7[&5王者&7]", 5);
            put("&7[&c荣耀&7]", 5);
        }};
        @Setting(comment = "对应段位 减积分 #必须和自己配置的段位保持一致")
        public Map<String, Integer> takeScore = new HashMap<String, Integer>() {{
            put("&7[&3青铜&7]", 5);
            put("&7[&e白银&7]", 5);
            put("&7[&e黄金&7]", 5);
            put("&7[&b钻石&7]", 5);
            put("&7[&d星耀&7]", 5);
            put("&7[&5王者&7]", 5);
            put("&7[&c荣耀&7]", 5);
        }};*/
    }

    @ConfigSerializable
    public static class BanConfig {
        @Setting(comment = "为true时禁止所有神兽 #为false最大允许携带几只神兽参赛")
        public Map<Boolean, Integer> Legendary = new HashMap<Boolean, Integer>() {{
            put(false, 3);
        }};

        @Setting(comment = "禁止以下宝可梦参战 #请注意宝可梦名称严格遵守大小写")
        public List<String> banPokemon = new ArrayList<String>() {{
            add("test");
            add("test2");
        }};

        @Setting(comment = "禁止使用以下技能参战 #请注意技能名称严格遵守大小写")
        public List<String> move = new ArrayList<String>() {{
            add("test");
            add("test2");
        }};

        @Setting(comment = "禁止使用以下特性参战 #请注意特性名称严格遵守大小写")
        public List<String> ability = new ArrayList<String>() {{
            add("test");
            add("test2");
        }};
    }

    @ConfigSerializable
    public static class rulesConfig {
        @Setting(comment = "启用OU分级规则")
        public boolean OU = true;

        @Setting(comment = "接棒规则")
        public boolean batonpass = false;

        @Setting(comment = "虚张声势规则")
        public boolean swagger = false;

        @Setting(comment = "接棒仅1次规则")
        public boolean batonpass1 = false;

        @Setting(comment = "降雨规则")
        public boolean drizzle = false;

        @Setting(comment = "降雨-悠游自如规则")
        public boolean drizzleswim = false;

        @Setting(comment = "日照规则")
        public boolean drought = false;

        @Setting(comment = "无限战斗规则")
        public boolean endlessbattle = false;

        @Setting(comment = "携带物品规则")
        public boolean item = false;

        @Setting(comment = "一站到底规则")
        public boolean forfeit = false;

        @Setting(comment = "超级进化规则")
        public boolean mega = false;

        @Setting(comment = "背包规则")
        public boolean bag = true;

        @Setting(comment = "种类规则")
        public boolean pokemon = true;

        @Setting(comment = "闪避招式规则")
        public boolean evasion = false;

        @Setting(comment = "反转对战规则")
        public boolean inverse = false;

        @Setting(comment = "喋喋不休规则")
        public boolean chatter = false;

        @Setting(comment = "催眠规则")
        public boolean sleep = true;

        @Setting(comment = "一击必杀规则")
        public boolean ohko = true;

        @Setting(comment = "治疗回复")
        public boolean fullHeal = false;

        @Setting(comment = "提高到上限")
        public boolean raisetocap = true;

        @Setting(comment = "最大等级")
        public int levelCap = 120;

        @Setting(comment = "回合时间")
        public int Time = 20;

        @Setting(comment = "宝可梦数量")
        public int numpokemon = 1;
    }

    @ConfigSerializable
    public static class Messages {
        @Setting(comment = "重载配置文件提示")
        public String reload = "&6配置文件已经重新加载";

        @Setting(comment = "执行必须由玩家执行的命令不是玩家时提示")
        public String notPlayer = "&c该命令只能玩家执行";

        @Setting(comment = "重置排行榜提示")
        public String resettop = "&e所有玩家排位数据已经重置";

        @Setting(comment = "管理员禁赛玩家时提示")
        public String ban = "你已将玩家&a %name% &e加入了禁赛列表";

        @Setting(comment = "管理员禁赛玩家时已经存在提示")
        public String ban1 = "玩家&a %name% &e早已经在禁赛列表了";

        @Setting(comment = "管理员解除禁赛玩家提示")
        public String unban = "你已将玩家&a %name% &e解禁了";

        @Setting(comment = "管理员解除的玩家不在ban位提示")
        public String unban1 = "玩家&a %name% &e还不在禁赛列表";

        @Setting(comment = "被禁赛玩家提示")
        public String onBan = "你已被禁赛，请确认是否存在恶意行为";

        @Setting(comment = "被禁赛玩家提示")
        public String onBan1 = "你已被系统自动禁赛10分钟，等你变强了再来吧";

        @Setting(comment = "设置玩家积分提示")
        public String setScore = "成功把 &a%name% &6的积分设置为: &a%score%";

        @Setting(comment = "为玩家添加积分提示")
        public String addScore = "成功给 &a%name% &6增加积分: &a%score%";

        @Setting(comment = "扣除玩家积分提示提示")
        public String takeScore = "成功扣除 &a%name% &6的 &a%score% 积分";

        @Setting(comment = "加入排位时提示信息")
        public String join = "§a排位 §4// §e玩家 %name% §a加入了排位列表 !\n§a点击 §d接受挑战   &7排队人数： %number%";

        @Setting(comment = "加入排位时悬浮信息")
        public String join1 = "§e玩家 %name% §6的战绩:  \n§e段位：%rank% \n§7积分:§a%score% §7胜场:§a %winne% §7败场:§a%loser% §7胜率: §a%=%§7%\n§d规则：§7%rules%";

        @Setting(comment = "提示玩家对手是谁")
        public String pvp = "&e对战开始你的对手是&a %name%";

        @Setting(comment = "等待中队伍不符合规则被踢出")
        public String check = "你携带的宝可梦不符合规则被移出排位列表";

        @Setting(comment = "携带的宝可梦全部不合格提示")
        public String PokeEmpty = "你携带的宝可梦全部不符合排位规则\n&d规则：\n&7%rules%";

        @Setting(comment = "携带了过多神兽提示")
        public String checkLeg = "你只能携带 %leg% 只神兽参赛";

        @Setting(comment = "携带了过多蛋被提示")
        public String checkEgg = "你不能携带这么多蛋来参赛";

        @Setting(comment = "已经在排位列表提示信息")
        public String join2 = "你已经在排位列表了";

        @Setting(comment = "退出排位时提示信息")
        public String quit = "你已经退出排位列表了";

        @Setting(comment = "没有加入退出时提示信息")
        public String quit2 = "你还没有加入排位列表";

        @Setting(comment = "排位即将开始玩家1却在对战中被提出排位提示")
        public String quit3 = "&a由于你处在战斗中已经自动帮你退出了排位列表";

        @Setting(comment = "排行榜信息: 支持 %winne% %loser%")
        public String TOP = "(§a%i%§r) §d玩家: §r%name% §d积分: §r%score% §d段位: %rank%";

        @Setting(comment = "排行榜标题信息")
        public String TOPtitle = "&7&ki&6&ki&5&ki&4&ki&3&ki&2&ki&1&ki§d+ + + + + &e&l段位排行榜 &1&ki&2&ki&3&ki&4&ki&4&ki&5&ki&7&ki";

        @Setting(comment = "查询个人战绩信息")
        public String me = "&e排位的个人战绩：\n§r> &7当前段位：%rank%\n§r> &7当前积分：§a%score%\n§r> &7胜场数：§a%winne%\n§r> &7败场数：§a%loser%\n§r> &7胜率：§a%=%%";

        @Setting(comment = "战斗胜利信息")
        public String VICTORY = "§e玩家 §a%winner% §d在排位中战胜了 §e玩家 §a%loser%";

        @Setting(comment = "胜利加分提示信息")
        public String winneMsg = "恭喜你取得了本次排位的胜利，积分 +&a%score%";

        @Setting(comment = "连胜胜利加分提示信息")
        public String winneWinningStreakMsg = "§6玩家 §a%winner% §d%s% §6连胜，积分附加 §d%s%";

        @Setting(comment = "失败扣分提示信息")
        public String loserMsg = "很遗憾本次排位你失败了：积分 -&a%score%";
    }
}
