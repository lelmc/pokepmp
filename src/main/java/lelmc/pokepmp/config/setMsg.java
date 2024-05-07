package lelmc.pokepmp.config;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import lelmc.pokepmp.Pokepmp;
import lelmc.pokepmp.data.DataBase;
import lelmc.pokepmp.ranking.Data;
import lelmc.pokepmp.ranking.pmp;
import lelmc.pokepmp.util.rules;
import lelmc.pokepmp.util.util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.title.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static lelmc.pokepmp.ranking.pmp.plist;

public class setMsg {
    public static void help(CommandSource src) {
        src.sendMessage(Text.of("§epokepmp 排位帮助信息: "));
        if (src.hasPermission("pmp.admin")) {
            src.sendMessage(Text.of("§3/pmp reload §7 - 重载配置文件"));
            src.sendMessage(Text.of("§3/pmp set <玩家> <积分> §7 - 设置玩家积分"));
            src.sendMessage(Text.of("§3/pmp add <玩家> <积分> §7 - 给玩家增加积分"));
            src.sendMessage(Text.of("§3/pmp take <玩家> <积分> §7 - 扣除玩家积分"));
            src.sendMessage(Text.of("§3/pmp ban <玩家> §7 - 将该玩家加入到排位黑名单"));
            src.sendMessage(Text.of("§3/pmp unban <玩家> §7 - 将该玩家移除排位黑名单"));
            src.sendMessage(Text.of("§c/pmp resettop §7 - 清空所有玩家排位数据"));
        }
        src.sendMessage(Text.of("§2/pmp me §r - 查询个人的段位积分"));
        src.sendMessage(Text.of("§2/pmp top §r - 查看战绩排行榜"));
        src.sendMessage(Text.of("§2/pmp join §r - 加入排位列表"));
        src.sendMessage(Text.of("§2/pmp quit §r - 退出排位列表"));
    }

    //加入排位时发送的信息
    public static void joinMsg(Player p, Data data) {
        BattleRules rules = new rules().getBattleRules();
        int AS = data.getID() == null ? 0 : data.getWinne() * 100 / (data.getWinne() + data.getLoser());
        String t = ConfigLoader.instance.getConfig().messages.join.replace("%name%", p.getName())
                .replace("&", "§")
                .replace("%number%", "" + plist.size());

        Text toPrint = Text.of(ConfigLoader.instance.getConfig().messages.join1
                .replace("%name%", p.getName())
                .replace("%rank%", util.rank(data.getScore()))
                .replace("%score%", "" + data.getScore())
                .replace("%winne%", "" + data.getWinne())
                .replace("%loser%", "" + data.getLoser())
                .replace("%=%", "" + AS)
                .replace("%rules%", "" + rules.getClauseList())
                .replace("&", "§"));
        Text tr = Text.builder(t).onClick(TextActions.runCommand("/pmp join")).onHover(TextActions.showText(toPrint))
                .build();
        MessageChannel.TO_PLAYERS.send(tr);
    }

    public static void Title(CommandSource player) {
        Player p = (Player) player;
        Title t = Title.builder().stay(1).actionBar(Text.of("§7排位匹配中...")).build();
        Task.builder().execute(task -> {
            if (plist.contains(p.getUniqueId()) && !plist.isEmpty()) {
                p.sendTitle(t);
            } else {
                task.cancel();
            }
        }).delayTicks(1).interval(5, TimeUnit.SECONDS).submit(Pokepmp.instance);
    }

    public static void quit(CommandSource player) {
        EntityPlayerMP playerMP = ((EntityPlayerMP) player);
        if (pmp.plist.contains(playerMP.getUniqueID())) {
            pmp.plist.remove(playerMP.getUniqueID());
            player.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.quit
                    .replace("&", "§")));
        } else {
            player.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.quit2
                    .replace("&", "§")));
        }
    }

    public static void quit3(Player p) {
        pmp.plist.remove(p.getUniqueId());
        p.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.quit3
                .replace("&", "§")));
    }

    public static void ban(Player player, CommandSource src) {
        if (!pmp.BanList.contains(player.getUniqueId())) {
            pmp.BanList.add(player.getUniqueId());
            upBan();
            src.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.ban
                    .replace("%name%", player.getName())
                    .replace("&", "§")));
        } else {
            src.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.ban1
                    .replace("%name%", player.getName())
                    .replace("&", "§")));
        }
    }

    public static void unban(Player player, CommandSource src) {
        if (pmp.BanList.contains(player.getUniqueId())) {
            pmp.BanList.remove(player.getUniqueId());
            upBan();
            src.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.unban
                    .replace("%name%", player.getName())
                    .replace("&", "§")));
        } else {
            src.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.unban1
                    .replace("%name%", player.getName())
                    .replace("&", "§")));
        }
    }

    public static void me(Player player) {
        Data data = DataBase.getAccount(player.getUniqueId());
        int AS = data.getID() == null ? 0 : data.getWinne() * 100 / (data.getWinne() + data.getLoser());
        player.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.me
                .replace("%rank%", util.rank(data.getScore()))
                .replace("%score%", "" + data.getScore())
                .replace("%winne%", "" + data.getWinne())
                .replace("%loser%", "" + data.getLoser())
                .replace("%=%", "" + AS)
                .replace("&", "§")));
    }

    public static void winne(EntityPlayerMP playerMP, int score) {
        playerMP.sendMessage(new TextComponentString(ConfigLoader.instance.getConfig().messages.winneMsg
                .replace("%score%", "" + score)
                .replace("&", "§")));
    }

    public static void winneWinningStreak(EntityPlayerMP winner, int s) {
        String replace = ConfigLoader.instance.getConfig().messages.winneWinningStreakMsg
                .replace("%winner%", winner.getName()).replace("%s%", s + "")
                .replace("&", "§");
        Text tr = Text.builder(replace).build();
        MessageChannel.TO_PLAYERS.send(tr);
    }

    public static void loser(EntityPlayerMP playerMP, int score) {
        playerMP.sendMessage(new TextComponentString(ConfigLoader.instance.getConfig().messages.loserMsg
                .replace("%score%", "" + score)
                .replace("&", "§")));
    }

    public static void setScore(CommandSource p, Player player, int score) {
        p.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.setScore
                .replace("%name%", player.getName())
                .replace("%score%", "" + score)
                .replace("&", "§")
        ));
    }

    public static void addScore(CommandSource p, Player player, int score) {
        p.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.addScore
                .replace("%name%", player.getName())
                .replace("%score%", "" + score)
                .replace("&", "§")
        ));
    }

    public static void takeScore(CommandSource p, Player player, int score) {
        p.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.takeScore
                .replace("%name%", player.getName())
                .replace("%score%", "" + score)
                .replace("&", "§")
        ));
    }

    public static void removePlayer(Player p) {
        pmp.plist.remove(p.getUniqueId());
        p.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.check)
                .replace("&", "§")));
    }

    public static void PokeEmpty(Player p) {
        Config config = ConfigLoader.instance.getConfig();
        BattleRules rules = new rules().getBattleRules();
        p.sendMessage(Text.of((config.messages.PokeEmpty)
                .replace("%rules%", "" + rules.getClauseList())
                .replace("%poke%", "" + config.BanConfig.banPokemon)
                .replace("%move%", "" + config.BanConfig.move)
                .replace("&", "§")));
    }

    public static void checkEgg(Player p) {
        p.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.checkEgg)
                .replace("&", "§")));
    }

    public static void checkLeg(Player p) {
        p.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.checkLeg)
                .replace("%leg%", "" + ConfigLoader.instance.getConfig().BanConfig.Legendary.get(false))
                .replace("&", "§")));
    }

    public static void pvp(Player p, String p1) {
        p.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.pvp)
                .replace("%name%", p1)
                .replace("&", "§")));
    }

    public static void onBan(Player player) {
        player.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.onBan
                .replace("&", "§"))));
    }

    public static void onBan1(Player player) {
        player.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.onBan1
                .replace("&", "§"))));
    }

    public static void joinMsgExist(Player p) {
        p.sendMessage(Text.of((ConfigLoader.instance.getConfig().messages.join2).replace("&", "§")));
    }

    //对战结束后胜负信息
    public static void EndMsg(EntityPlayerMP winner, EntityPlayerMP loser) {
        String t = ConfigLoader.instance.getConfig().messages.VICTORY.replace("%winner%", winner.getName())
                .replace("%loser%", loser.getName()).replace("&", "§");
        Text tr = Text.builder(t)
                .build();
        MessageChannel.TO_PLAYERS.send(tr);
    }

    public static void notPlayer(CommandSource player) {
        player.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.notPlayer
                .replace("&", "§")));
    }

    public static void resettop(CommandSource player) {
        player.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.resettop
                .replace("&", "§")));
    }

    public static void reload(CommandSource player) {
        player.sendMessage(Text.of(ConfigLoader.instance.getConfig().messages.reload
                .replace("&", "§")));
    }

    public static void TOP(CommandSource player) {
        List<Data> p = DataBase.getTop();
        List<Text> contents = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            Data s = p.get(i);
            contents.add(Text.of(ConfigLoader.instance.getConfig().messages.TOP
                    .replace("%i%", "" + (i + 1)).replace("%rank%", util.rank(s.getScore()))
                    .replace("%score%", "" + s.getScore())
                    .replace("%winne%", "" + s.getWinne())
                    .replace("%loser%", "" + s.getLoser())
                    .replace("%name%", "" + s.getID())
                    .replace("&", "§")
            ));
        }
        PaginationList.builder()
                .title(Text.of(ConfigLoader.instance.getConfig().messages.TOPtitle.replace("&", "§")))
                .contents(contents)
                .padding(Text.of("§r"))
                .linesPerPage(12)
                .sendTo(player);
    }

    //更新ban数据
    public static void upBan() {
        Task.builder().execute(task -> {
            DataBase.clearBan();
            for (UUID uuid : pmp.BanList) {
                DataBase.UpBan(uuid.toString());
            }
        }).async().submit(Pokepmp.instance);
    }
}
