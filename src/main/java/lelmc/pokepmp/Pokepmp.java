package lelmc.pokepmp;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import lelmc.pokepmp.Command.CommandManager;
import lelmc.pokepmp.config.ConfigLoader;
import lelmc.pokepmp.data.DataBase;
import lelmc.pokepmp.ranking.pmp;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


@Plugin(
        id = "pokepmp",
        name = "PokePmp",
        description = "pokepmp",
        authors = {
                "lelmc"
        }
)
public class Pokepmp {
    public static Pokepmp instance;
    @Inject
    @ConfigDir(sharedRoot = false)
    public File file;
    @Inject
    private Logger logger;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        instance = this;
        new CommandManager();
        //configLoader = new CLoader(file);
        new ConfigLoader(configDir);
        Sponge.getEventManager().registerListeners(this, new pmp());
        Pixelmon.EVENT_BUS.register(new pmp());
        DataBase.setDB(this.file);
        DataBase.getBan();

        logger.info("PokePmP加载完成");

        if (Sponge.getPluginManager().getPlugin("PlaceholderAPI").isPresent()) {
            Placeholders.register();
            logger.info("排位Papi注册成功");
        }
    }
}
