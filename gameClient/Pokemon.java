package gameClient;

import api.DirectedWeightedGraph;
import api.GeoLocation;
import api.geo;
import api.impEdgeData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gameClient.util.point;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

    private static final double EPS = 0.001 * 0.001;
    private double _value;
    private int _type;
    private GeoLocation _pos;
    private impEdgeData _edge;
    static private DirectedWeightedGraph _graph;
    public List<Pokemon> pokemons;
    private boolean isCaptured;
    private boolean isAssigned;

    public Pokemon( int type, double value, GeoLocation pos) {
        this._type = type;
        this._value = value;
        this._pos = pos;

    }

    public Pokemon(PokemonJson.PokemonJsonInner pokemonJsonInner) {
        this._value = pokemonJsonInner.value;
        this._type = pokemonJsonInner.type;
        this._pos = new geo(pokemonJsonInner.locationString);
        this._edge = null;
        isAssigned = false;
        isCaptured = false;
    }

    public void update(JsonObject json) {
        _value = json.get("value").getAsDouble();
        _type = json.get("type").getAsInt();
        _pos = new point(json.get("pos").getAsString());

    }

    public static List<Pokemon> load(String json) {
        try {
            PokemonJson dgj = new Gson().fromJson(json, PokemonJson.class);
            List<Pokemon> pokemons = new ArrayList<>();
            for (int i = 0; i < dgj.getPokemonJsonWrappers().size(); i++) {
                pokemons.add(new Pokemon(dgj.getPokemonJsonWrappers().get(i).getPokemon()));
            }
            return pokemons;
        } catch (Exception e) {
            return null;
        }

    }
    private boolean isOnEdge(impEdgeData e) {

        int src = e.getSrc();
        int dest = e.getDest();
        if (_type < 0 && dest > src) {
            return false;
        }
        if (_type > 0 && src > dest) {
            return false;
        }
        GeoLocation src_loc = _graph.getNode(src).getLocation();
        GeoLocation dest_loc = _graph.getNode(dest).getLocation();
        double dist = src_loc.distance(dest_loc);
        double d1 = src_loc.distance(get_pos()) + get_pos().distance(dest_loc);
        return dist > d1 - EPS;
    }

    public static void set_graph(DirectedWeightedGraph _graph) {
        Pokemon._graph = _graph;
    }

    public double get_value() {
        return _value;
    }

    public int get_type() {
        return _type;
    }

    public geo get_pos() {
        return (geo)_pos;
    }

    public impEdgeData get_edge() {
        return _edge;
    }

    public void set_edge(impEdgeData _edge) {
        this._edge = _edge;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "_value=" + _value +
                ", _type=" + _type +
                ", _pos=" + _pos +
                ", _edge=" + _edge +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Double.compare(pokemon._value, _value) == 0 && _type == pokemon._type && _pos.equals(pokemon._pos) && _edge.equals(pokemon._edge);
    }

}
