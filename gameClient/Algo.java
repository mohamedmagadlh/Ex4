package gameClient;

import java.util.List;

public class Algo {
    public Pokemon _pokemons;

    private static final double EPS = 0.000001;

    public void setPokemons(List<Pokemon> pokemons) {
        this._pokemons = (Pokemon) pokemons;
    }
}