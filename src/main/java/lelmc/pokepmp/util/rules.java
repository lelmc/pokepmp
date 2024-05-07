package lelmc.pokepmp.util;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.Tier;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import lelmc.pokepmp.config.Config;
import lelmc.pokepmp.config.ConfigLoader;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class rules {

    private BattleRules battleRules = new BattleRules();
    private ArrayList<String> move;
    private ArrayList<String> Ability;

    public rules() {
        buildFormat();
    }

    public static List<String> poke() {
        return asList("Urshifu", "Arceus", "Spectrier", "Calyrex", "Lugia", "Mewtwo", "Cinderace", "Zacian", "Xerneas", " Kyogre",
                "Rayquaza", "Groudon", "Zygarde", "Dracozolt", "Dracovish", "Naganadel", "Pheromosa", "Lunala", "Darkrai", "Eternatus",
                "Zamazenta", "Yveltal", "Genesect", "Giratina", "Zekrom", "Reshiram", "Solgaleo", "Marshadow", "Palkia", "Deoxys", "Dialga");
    }

    public void buildFormat() {
        Config config = ConfigLoader.instance.getConfig();
        BattleRules newRules = new BattleRules();
        ArrayList<BattleClause> allClauses = new ArrayList<>();
        this.move = new ArrayList<>();
        move.addAll(config.BanConfig.move);
        this.Ability = new ArrayList<>();
        Ability.addAll(config.BanConfig.ability);

        newRules.turnTime = config.rulesConfig.Time;
        newRules.teamSelectTime = config.rulesConfig.Time;
        newRules.levelCap = config.rulesConfig.levelCap;
        newRules.fullHeal = config.rulesConfig.fullHeal;
        newRules.raiseToCap = config.rulesConfig.raisetocap;
        newRules.numPokemon = config.rulesConfig.numpokemon;
        newRules.oldgen = EnumOldGenMode.World;
        newRules.teamPreview = true;

        if (config.rulesConfig.OU) {
            newRules.tier = new Tier("ou");
            //开始OU规则(禁止特性
            Ability.add("Shadow Tag");
            Ability.add("Arena Trap");
            Ability.add("Moody");
        }

        if (config.rulesConfig.batonpass) {
            allClauses.add(new BattleClause("batonpass"));
            move.add("Baton Pass");
        }
        if (config.rulesConfig.chatter) {
            move.add("Chatter");
            allClauses.add(new BattleClause("chatter"));
        }
        if (config.rulesConfig.swagger) {
            move.add("swagger");
            allClauses.add(new BattleClause("Swagger"));
        }
        if (config.rulesConfig.evasion) {
            move.add("Double Team");
            move.add("Minimize");
            allClauses.add(new BattleClause("evasion"));
        }
        if (config.rulesConfig.ohko) {
            move.add("Fissure");
            move.add("Guillotine");
            move.add("Horn Drill");
            move.add("Sheer Cold");
            allClauses.add(new BattleClause("ohko"));
        }
        if (config.rulesConfig.batonpass1) {
            allClauses.add(new BattleClause("batonpass1"));
        }
        if (config.rulesConfig.drizzle) {
            allClauses.add(new BattleClause("drizzle"));
        }
        if (config.rulesConfig.drizzleswim) {
            allClauses.add(new BattleClause("drizzleswim"));
        }
        if (config.rulesConfig.drought) {
            allClauses.add(new BattleClause("drought"));
        }
        if (config.rulesConfig.endlessbattle) {
            allClauses.add(new BattleClause("endlessbattle"));
        }
        if (config.rulesConfig.item) {
            allClauses.add(new BattleClause("item"));
        }
        if (config.rulesConfig.forfeit) {
            allClauses.add(new BattleClause("forfeit"));
        }
        if (config.rulesConfig.mega) {
            allClauses.add(new BattleClause("mega"));
        }
        if (config.rulesConfig.pokemon) {
            allClauses.add(new BattleClause("pokemon"));
        }
        if (config.rulesConfig.inverse) {
            allClauses.add(new BattleClause("inverse"));
        }
        if (config.rulesConfig.sleep) {
            allClauses.add(new BattleClause("sleep"));
        }
        if (config.rulesConfig.bag) {
            allClauses.add(new BattleClause("bag"));
        }
        if (ConfigLoader.instance.getConfig().BanConfig.Legendary.containsKey(true)) {
            allClauses.add(new BattleClause("legendary"));
        }

        newRules.setNewClauses(allClauses);
        this.battleRules = newRules;
    }

    public BattleRules getBattleRules() {
        return battleRules;
    }


    public ArrayList<String> getAbility() {
        return Ability;
    }


    public ArrayList<String> getMove() {
        return move;
    }
}
