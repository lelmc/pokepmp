package lelmc.pokepmp.util;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import lelmc.pokepmp.config.Config;
import lelmc.pokepmp.config.ConfigLoader;
import lelmc.pokepmp.config.setMsg;
import lelmc.pokepmp.data.DataBase;
import lelmc.pokepmp.ranking.Data;
import lelmc.pokepmp.ranking.pmp;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class util {


    //按照升序排列
    private static TreeMap<Integer, String> ranks() {
        TreeMap<Integer, String> ranks = new TreeMap<>();
        Map<String, String> map = ConfigLoader.instance.getConfig().ranks.ranks;
        for (String key : map.keySet()) {
            String value = map.get(key);
            ranks.put(Integer.parseInt(key), value);
        }
        return ranks;
    }

    //返回段位
    public static String rank(int score) {
        TreeMap<Integer, String> ranks = ranks();
        String rank = "";
        for (Integer key : ranks.keySet()) {
            String value = ranks.get(key);
            if (score >= key) {
                rank = value;
            }
        }
        return rank;
    }

    public static Player pmpU(UUID uuid) {//返回一个实体玩家
        if (Sponge.getServer().getPlayer(uuid).isPresent()) {
            return Sponge.getServer().getPlayer(uuid).get();
        }
        return null;
    }

    //检查技能是否合规
    public static boolean moveCheck(Pokemon pokemon) {
        rules r = new rules();
        Moveset moveset = pokemon.getMoveset();
        int size = r.getMove().size();
        String[] array = r.getMove().toArray(new String[size]);
        boolean mov = moveset.hasAttack(array);
        if (ConfigLoader.instance.getConfig().rulesConfig.ohko && mov) {
            return true;
        }
        return mov;
    }

    //检查特性是否合规
    public static boolean AbilityCheck(Pokemon pokemon) {
        rules r = new rules();
        AbilityBase ability = pokemon.getAbility();
        return r.getAbility().contains(ability.getUnlocalizedName());
    }

    //返回宝可梦了列表
    public static ArrayList<Pokemon> PokemonList(EntityPlayerMP participant) {
        ArrayList<Pokemon> playerPokemonList = new ArrayList<>();
        List<String> pokeName = new ArrayList<>();
        Pokemon[] playerParty = Pixelmon.storageManager.getParty(participant).getAll();
        Config config = ConfigLoader.instance.getConfig();
        for (Pokemon pokemon : playerParty) {
            if (pokemon == null || pokemon.isEgg()) {
                continue;
            }
            if (AbilityCheck(pokemon)) {
                toMsg(pokemon, "特性");
                continue;
            }
            if (moveCheck(pokemon)) {
                toMsg(pokemon, "招式");
                continue;
            }
            if (config.rulesConfig.pokemon) {//种族规则开启时
                if (pokeName.contains(pokemon.getBaseStats().getPokemonName())) {
                    toMsg(pokemon, "种族");
                    continue;
                }
            }
            if (config.BanConfig.Legendary.containsKey(true)) {//如果禁用神兽
                if (pokemon.isLegendary()) {
                    toMsg(pokemon, "神兽");
                    continue;
                }
            }

            if (config.rulesConfig.OU) {
                if (rules.poke().contains(pokemon.getBaseStats().getPokemonName())) {
                    toMsg(pokemon, "OU");
                    continue;
                }
            }
            if (config.BanConfig.banPokemon.contains(pokemon.getBaseStats().getPokemonName())) {
                toMsg(pokemon, "禁赛");
                continue;
            }
            playerPokemonList.add(pokemon);
            pokeName.add(pokemon.getBaseStats().getPokemonName());
        }

        return playerPokemonList;
    }

    public static void toMsg(Pokemon pokemon, String T) {
        String m = pokemon.getLocalizedName() + " 违反了 " + T + " 规则已自动跳过";
        pokemon.getOwnerPlayer().sendMessage(new TextComponentString(m));
    }

    //检查神兽是否超过了配置文件
    public static boolean checkLeg(UUID uuid) {
        if (ConfigLoader.instance.getConfig().BanConfig.Legendary.containsKey(false)) {
            int leg = 0;
            Pokemon[] poke = Pixelmon.storageManager.getParty(uuid).getAll();
            for (Pokemon p : poke) {
                if (p != null && p.isLegendary()) {
                    leg++;
                }
            }
            return leg > ConfigLoader.instance.getConfig().BanConfig.Legendary.get(false);
        }
        return false;
    }

    //检查玩家是否携带了6个蛋来参赛
    public static boolean checkEgg(UUID uuid) {
        boolean flag = false;
        int egg = 0;
        Pokemon[] poke = Pixelmon.storageManager.getParty(uuid).getAll();
        for (Pokemon p : poke) {
            if (p != null && p.isEgg()) {
                egg++;
            }
        }
        if (egg >= 6) {
            flag = true;
        }
        return flag;
    }

    //调整积分
    public static void addScore(EntityPlayerMP player) {
        Config.ScoreConfig config = ConfigLoader.instance.getConfig().ScoreConfig;
        int i = 0;
        if (pmp.WinningStreak.containsKey(player.getUniqueID())) {
            i = pmp.WinningStreak.get(player.getUniqueID()) + 1;
            pmp.WinningStreak.put(player.getUniqueID(), i);
            if (pmp.WinningStreak.get(player.getUniqueID()) >= config.WinningStreakMax) {
                i = config.WinningStreakMax;
            }

            if (i > 1) {
                setMsg.winneWinningStreak(player, i);
            }
        } else {
            pmp.WinningStreak.put(player.getUniqueID(), 1);
        }

        int upScore = config.add + i;

        Data data = DataBase.getAccount(player.getUniqueID());
        if (data.getID() == null) {
            DataBase.createAcc(player.getUniqueID(), player.getName(), config.add, 1, 0);
            return;
        }

        setMsg.winne(player, upScore);
        DataBase.UpAccount(player.getUniqueID(), data.getScore() + upScore, data.getWinne() + 1, data.getLoser());
    }

    //调整积分
    public static void takeScore(EntityPlayerMP player) {
        Config.ScoreConfig config = ConfigLoader.instance.getConfig().ScoreConfig;
        Data data = DataBase.getAccount(player.getUniqueID());
        if (data.getID() == null) {
            DataBase.createAcc(player.getUniqueID(), player.getName(), 0, 0, 1);
            return;
        }
        int upScore = config.take;

        setMsg.loser(player, upScore);
        DataBase.UpAccount(player.getUniqueID(), data.getScore() - upScore, data.getWinne(), data.getLoser() + 1);
    }
}
