package lelmc.pokepmp.Command;

import lelmc.pokepmp.Pokepmp;
import lelmc.pokepmp.config.ConfigLoader;
import lelmc.pokepmp.config.setMsg;
import lelmc.pokepmp.data.DataBase;
import lelmc.pokepmp.ranking.Data;
import lelmc.pokepmp.ranking.pmp;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class CommandManager {
    private static final CommandSpec join = CommandSpec.builder()
            .executor((src, args) -> {
                if (src instanceof Player) {
                    Player player = (Player) src;
                    Data data = DataBase.getAccount(player.getUniqueId());
                    pmp.check(player, data);
                } else {
                    setMsg.notPlayer(src);
                }
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec quit = CommandSpec.builder()
            .executor((src, args) -> {
                if (src instanceof Player) {
                    setMsg.quit(src);
                } else {
                    setMsg.notPlayer(src);
                }
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec me = CommandSpec.builder()
            .executor((src, args) -> {
                if (src instanceof Player) {
                    Player player = (Player) src;
                    //util.rr(player);
                    setMsg.me(player);
                } else {
                    setMsg.notPlayer(src);
                }
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec setScore = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                Player player = args.<Player>getOne("player").get();
                int integer = args.<Integer>getOne("integer").get();
                Data data = DataBase.getAccount(player.getUniqueId());
                if (data.getID() == null) {
                    DataBase.createAcc(player.getUniqueId(), player.getName(), integer, 1, 0);
                } else {
                    DataBase.UpScore(player.getUniqueId(), integer);
                }
                setMsg.setScore(src, player, integer);
                return CommandResult.success();
            })
            .arguments(GenericArguments.seq(
                    GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                    GenericArguments.integer(Text.of("integer"))))
            .build();

    private static final CommandSpec add = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                Player player = args.<Player>getOne("player").get();
                int integer = args.<Integer>getOne("integer").get();
                Data data = DataBase.getAccount(player.getUniqueId());
                if (data.getID() == null) {
                    DataBase.createAcc(player.getUniqueId(), player.getName(), integer, 1, 0);
                    setMsg.addScore(src, player, integer);
                } else {
                    DataBase.UpScore(player.getUniqueId(), (data.getScore() + integer));
                    setMsg.addScore(src, player, integer);
                }
                return CommandResult.success();
            })
            .arguments(GenericArguments.seq(
                    GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                    GenericArguments.integer(Text.of("integer"))))
            .build();

    private static final CommandSpec take = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                Player player = args.<Player>getOne("player").get();
                int integer = args.<Integer>getOne("integer").get();
                Data data = DataBase.getAccount(player.getUniqueId());
                if (data.getID() == null) {
                    DataBase.createAcc(player.getUniqueId(), player.getName(), integer, 1, 0);
                    setMsg.takeScore(src, player, integer);
                } else {
                    DataBase.UpScore(player.getUniqueId(), (data.getScore() - integer));
                    setMsg.takeScore(src, player, integer);
                }
                return CommandResult.success();
            })
            .arguments(GenericArguments.seq(
                    GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                    GenericArguments.integer(Text.of("integer"))))
            .build();

    private static final CommandSpec ban = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                Player player = args.<Player>getOne("player").get();
                setMsg.ban(player, src);
                return CommandResult.success();
            })
            .arguments(GenericArguments.player(Text.of("player")))
            .build();

    private static final CommandSpec unban = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                Player player = args.<Player>getOne("player").get();
                setMsg.unban(player, src);
                return CommandResult.success();
            })
            .arguments(GenericArguments.player(Text.of("player")))
            .build();

    private static final CommandSpec resetTOP = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                DataBase.clear();
                setMsg.resettop(src);
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec reload = CommandSpec.builder()
            .permission("pmp.admin")
            .executor((src, args) -> {
                ConfigLoader.instance.load();
                setMsg.reload(src);
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec top = CommandSpec.builder()
            .executor((src, args) -> {
                setMsg.TOP(src);
                return CommandResult.success();
            })
            .build();


    private static final CommandSpec myCommand = CommandSpec.builder()
            .executor((src, args) -> {
                setMsg.help(src);
                return CommandResult.success();
            })
            .child(reload, "reload")
            .child(resetTOP, "resettop")
            .child(ban, "ban")
            .child(unban, "unban")
            .child(setScore, "set", "setScore")
            .child(add, "a", "add")
            .child(take, "r", "take")
            .child(me, "m", "me", "s")
            .child(join, "j", "join")
            .child(quit, "q", "quit")
            .child(top, "t", "top")
            .build();

    public CommandManager() {
        Sponge.getCommandManager().register(Pokepmp.instance, myCommand, "pmp");
    }
}
