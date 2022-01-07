package gameClient;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;

public class Pokemon {

    private static final double EPS = 0.001 * 0.001;
    private double _value;
    private int _type;
    private Point3D _pos;
    private EdgeData _edge;
    static private DirectedWeightedGraph _graph;

    public Pokemon(JsonObject json) {
        update(json);
    }

    public void update(JsonObject json) {
        _value = json.get("value").getAsDouble();
        _type = json.get("type").getAsInt();
        _pos = new Point3D(json.get("pos").getAsString());
        _edge = findEdge();
    }


    public EdgeData findEdge() {
        for (NodeData v : _graph.getV()) {
            for (EdgeData e : _graph.getE(v.getKey())) {
                if (isOnEdge(e)) {
                    return e;
                }
            }
        }
        return null;
    }

    private boolean isOnEdge(EdgeData e) {
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

    public Point3D get_pos() {
        return _pos;
    }

    public EdgeData get_edge() {
        return _edge;
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

