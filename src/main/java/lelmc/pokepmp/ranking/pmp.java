package lelmc.pokepmp.ranking;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import lelmc.pokepmp.Pokepmp;
import lelmc.pokepmp.config.ConfigLoader;
import lelmc.pokepmp.config.setMsg;
import lelmc.pokepmp.data.DataBase;
import lelmc.pokepmp.util.rules;
import lelmc.pokepmp.util.util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class pmp {
    public static List<UUID> BanList = new ArrayList<>();
    public static List<UUID> plist = new ArrayList<>();//匹配列表
    public static Map<UUID, UUID> pkList = new HashMap<>();//对战列表
    public static Map<UUID, UUID> FailedList = new HashMap<>();
    public static Map<UUID, Integer> loserNumber = new HashMap<>();
    public static Map<UUID, Integer> WinningStreak = new HashMap<>();//记录玩家连胜

    public static void check(Player player, Data data) {
        if (joinCheck(player)) {
            return;
        }
        setMsg.Title(player);
        plist.add(player.getUniqueId());
        setMsg.joinMsg(player, data);
        if (plist.size() >= 2) {
            Player player1 = pkPlayer1(player, data);
            if (player1 == null) {
                return;
            }
            setMsg.pvp(player, player1.getName());
            setMsg.pvp(player1, player.getName());
            //传送玩家2到玩家1的位置
            if (ConfigLoader.instance.getConfig().comm.stp) {
                player.setLocation(player1.getLocation());
            }
            //治愈2个玩家的宝可梦
            Pixelmon.storageManager.getParty(player1.getUniqueId()).heal();
            Pixelmon.storageManager.getParty(player.getUniqueId()).heal();
            onBattle(player1.getUniqueId(), player.getUniqueId());
            plist.remove(player1.getUniqueId());
            plist.remove(player.getUniqueId());
        }
    }

    public static boolean joinCheck(Player player) {//检查加入条件
        if (pmp.plist.contains(player.getUniqueId())) {
            setMsg.joinMsgExist(player);
            return true;
        }
        if (pmp.BanList.contains(player.getUniqueId())) {
            setMsg.onBan(player);
            return true;
        }
        if (util.PokemonList((EntityPlayerMP) player).isEmpty()) {
            setMsg.PokeEmpty(player);
            return true;
        }
        //检查背包的神兽
        if (util.checkLeg(player.getUniqueId())) {
            setMsg.checkLeg(player);
            return true;
        }
        if (util.checkEgg(player.getUniqueId())) {
            setMsg.checkEgg(player);
            return true;
        }
        return false;
    }

    public static Player pkPlayer1(Player player, Data data) {//返回列表中第一个合适的对手
        for (UUID uuid : plist) {
            Player player1 = util.pmpU(uuid);
            if (uuid == player.getUniqueId() || player1 == null) {
                continue;
            }
            if (util.checkLeg(uuid) || util.checkEgg(uuid)) {
                setMsg.removePlayer(player1);
                continue;
            }
            if (ConfigLoader.instance.getConfig().BanConfig.Legendary.containsKey(true)) {
                if (util.PokemonList((EntityPlayerMP) player1).isEmpty()) {
                    setMsg.removePlayer(player1);
                    continue;
                }
            }
            if (BattleRegistry.getBattle((EntityPlayer) player1) != null) {
                setMsg.quit3(player1);
                continue;
            }
            if (player.getConnection().getAddress().getHostString().contains(player1.getConnection().getAddress().getHostString())) {
                if (ConfigLoader.instance.getConfig().comm.TheIP) {
                    pmp.plist.remove(player.getUniqueId());
                    continue;
                }
            }
            Data account = DataBase.getAccount(uuid);
            if (FailedList.containsKey(player.getUniqueId())) {
                if (FailedList.get(player.getUniqueId()) == uuid) {
                    continue;
                }
            }
            if (FailedList.containsKey(uuid)) {
                if (FailedList.get(uuid) == player.getUniqueId()) {
                    continue;
                }
            }
            if (util.rank(account.getScore()).contains(util.rank(data.getScore()))) {
                return player1; //返回一个段位相同的玩家
            }
            if (data.getScore() >= 10 && data.getScore() < 300 && account.getScore() >= 10 && account.getScore() < 300) {
                return player1;
            }

            if (data.getScore() > 300 && account.getScore() > 300) {
                return player1;
            }
        }
        return null;
    }

    private static void onBattle(UUID uuid1, UUID uuid2) {
        if (Sponge.getServer().getPlayer(uuid1).isPresent() && Sponge.getServer().getPlayer(uuid2).isPresent()) {
            //将列表UUID实例化
            Player player1 = Sponge.getServer().getPlayer(uuid1).get();
            Player player2 = Sponge.getServer().getPlayer(uuid2).get();
            EntityPlayerMP participant1 = (EntityPlayerMP) player1;
            EntityPlayerMP participant2 = (EntityPlayerMP) player2;
            //准备规则
            BattleRules rules = new rules().getBattleRules();
            //双打准备
            PlayerParticipant part1 = new PlayerParticipant(participant1, util.PokemonList(participant1), rules.numPokemon);
            PlayerParticipant part2 = new PlayerParticipant(participant2, util.PokemonList(participant2), rules.numPokemon);
            //开始战斗
            part1.startedBattle = true;
            part2.startedBattle = true;
            BattleRegistry.startBattle(new BattleParticipant[]{part1}, new BattleParticipant[]{part2}, rules);
            //添加到对战监听列表
            pkList.put(uuid1, uuid2);
        }
    }

    @SubscribeEvent
    public void BattleEnd(BattleEndEvent event) {
        if (event.getPlayers().size() == 2) {
            EntityPlayerMP p1 = event.getPlayers().get(0);
            EntityPlayerMP p2 = event.getPlayers().get(1);
            if (pkList.containsKey(p1.getUniqueID())) {
                Task.builder().execute(task -> {
                    BattleParticipant pk1 = event.results.keySet().asList().get(0);
                    BattleParticipant pk2 = event.results.keySet().asList().get(1);
                    PlayerParticipant pe1 = (PlayerParticipant) pk1;
                    PlayerParticipant pe2 = (PlayerParticipant) pk2;
                    EntityPlayerMP winner, loser;
                    if (Sponge.getServer().getPlayer(p1.getName()).isPresent() && Sponge.getServer().getPlayer(p2.getName()).isPresent()) {
                        if (event.results.get(pk1) == BattleResults.VICTORY) {
                            winner = pe1.player;
                            loser = pe2.player;
                        } else {
                            winner = pe2.player;
                            loser = pe1.player;
                        }
                    } else {
                        if (Sponge.getServer().getPlayer(pe1.player.getUniqueID()).isPresent()) {
                            winner = pe1.player;
                            loser = pe2.player;
                        } else {
                            winner = pe2.player;
                            loser = pe1.player;
                        }
                    }
                    endProcessing(winner, loser);
                    //防止玩家卡对战结束战斗
                    if (BattleRegistry.getBattle(p1) != null) {
                        BattleRegistry.getBattle(p1).endBattle();
                    }
                    if (BattleRegistry.getBattle(p2) != null) {
                        BattleRegistry.getBattle(p2).endBattle();
                    }
                }).submit(Pokepmp.instance);
            }
        }
    }

    @Listener
    public void onCommandSend(SendCommandEvent event) {
        if (event.getCommand().contains("endbattle")) {
            if (!(event.getSource() instanceof Player)) {
                return;
            }
            Player player = (Player) event.getSource();
            if (pkList.containsKey(player.getUniqueId()) || pkList.containsValue(player.getUniqueId())) {
                if (ConfigLoader.instance.getConfig().comm.Console) {
                    Sponge.getCommandManager().process(Sponge.getGame().getServer().getConsole(),
                            ConfigLoader.instance.getConfig().comm.endbattle.replace("%playe%", player.getName()));
                    return;
                }
                EntityPlayer ePlayer = (EntityPlayer) player;
                BattleControllerBase bcb = BattleRegistry.getBattle(ePlayer);
                List<PlayerParticipant> players = bcb.getPlayers();
                EntityPlayerMP p1, p2;
                if (players.get(0).player.getUniqueID().toString().contains(player.getUniqueId().toString())) {
                    p1 = players.get(0).player;
                    p2 = players.get(1).player;
                } else {
                    p1 = players.get(1).player;
                    p2 = players.get(0).player;
                }
                EntityPlayerMP winner, loser;
                if (player.getUniqueId().equals(p1.getUniqueID())) {
                    winner = p2;
                    loser = p1;
                } else {
                    winner = p1;
                    loser = p2;
                }
                endProcessing(winner, loser);
            }
        }
    }

    public static void endProcessing(EntityPlayerMP winner, EntityPlayerMP loser) {
        pmp.WinningStreak.remove(loser.getUniqueID());
        util.addScore(winner);
        util.takeScore(loser);
        pmp.pkList.remove(winner.getUniqueID());
        pmp.pkList.remove(loser.getUniqueID());
        setMsg.EndMsg(winner, loser);
        FailedList.put(loser.getUniqueID(), winner.getUniqueID());

        Integer i = loserNumber.getOrDefault(loser.getUniqueID(), 0);
        loserNumber.put(loser.getUniqueID(), i + 1);
        if (i >= 3) {
            setMsg.onBan1((Player) loser);
            BanList.add(loser.getUniqueID());
            Task.builder().execute(task -> {
                pmp.BanList.remove(loser.getUniqueID());
                pmp.loserNumber.remove(loser.getUniqueID());
            }).async().delay(10, TimeUnit.MINUTES).submit(Pokepmp.instance);
        }
    }

    @Listener//玩家断开连接移除排位列表
    public void ClientConnection(ClientConnectionEvent.Disconnect event, @First Player player) {
        plist.remove(player.getUniqueId());
    }

}

