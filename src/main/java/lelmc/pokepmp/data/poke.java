package lelmc.pokepmp.data;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.UUID;

public class poke extends Pokemon {

    public void list() {
        setSpecies(EnumSpecies.randomPoke());
        setUUID(UUID.randomUUID());
    }
}
