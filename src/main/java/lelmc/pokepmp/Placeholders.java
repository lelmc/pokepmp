package lelmc.pokepmp;

import lelmc.pokepmp.data.DataBase;
import lelmc.pokepmp.ranking.Data;
import lelmc.pokepmp.util.util;
import me.rojo8399.placeholderapi.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class Placeholders {
    public static void register() {
        Sponge.getServiceManager().provideUnchecked(PlaceholderService.class)
                .loadAll(new Placeholders(), Pokepmp.instance).stream().map(builder -> {
                    if ("pmp".equals(builder.getId())) {
                        return builder.tokens("score", "rank", "wins", "losses")
                                .description("获取排位玩家的个人数据,如获取积分: %pmp_score% \n获取排行榜数据,如第一名的名字: %pmp_1_name%");
                    }
                    return builder;
                }).map(builder -> builder.author("lelmc").plugin(Pokepmp.instance).version("1.3")).forEach(builder -> {
                    try {
                        builder.buildAndRegister();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }

    @Placeholder(id = "pmp")
    public Object pmp(@Source Player player, @Token String token) throws NoValueException {
        String[] values = token.split("_");
        if (values.length == 2) {
            List<Data> p = DataBase.getTop();
            int number = Integer.parseInt(values[0]) - 1;
            if (number < 0 || number >= p.size()) {
                throw new NoValueException("排行榜值应该在1 - 10之间");
            }
            switch (values[1]) {
                case "name":
                    return p.get(number).getID();
                case "rank":
                    return util.rank(p.get(number).getScore());
                case "wins":
                    return p.get(number).getWinne();
                case "losses":
                    return p.get(number).getLoser();
            }
        }
        if (values.length == 1) {
            Data data = DataBase.getAccount(player.getUniqueId());
            switch (values[0]) {
                case "score":
                    return data.getScore();
                case "rank":
                    return util.rank(data.getScore());
                case "wins":
                    return data.getWinne();
                case "losses":
                    return data.getLoser();
            }
        } else {
            throw new NoValueException("变量缺少信息");
        }
        throw new NoValueException();
    }
}
