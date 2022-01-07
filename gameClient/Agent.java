package gameClient;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;

import java.util.ArrayList;
import java.util.List;


public class Agent {

    private int _id, _is_moving;
    private double _value, _speed;
    private GeoLocation _pos;
    private NodeData _node;
    private EdgeData _edge;
    private List<node_data> _path;
    private Pokemon _curr_fruit;
    private static DirectedWeightedGraph _graph;

    public Agent(JsonObject json) {
        update(json);
        _path = new ArrayList<>();
    }


    public void update(JsonObject agent) {
        _id = agent.get("id").getAsInt();
        _value = agent.get("value").getAsDouble();
        _speed = agent.get("speed").getAsDouble();
        _pos = new Point3D(agent.get("pos").getAsString());
        setNode(agent.get("src").getAsInt()); // update _node
        setNextNode(agent.get("dest").getAsInt()); // update _edge
        _is_moving = agent.get("dest").getAsInt();
    }


    public void setNextNode(int dest) {

        int src = this._node.getKey();
        this._edge = _graph.getEdge(src, dest);
    }


    public boolean isMoving() {
        return _is_moving != -1;
    }


    public static void set_graph(DirectedWeightedGraph _graph) {
        Agent._graph = _graph;
    }

    public void setNode(int src) {
        this._node = _graph.getNode(src);
    }

    public int getId() {
        return _id;
    }

    public double getValue() {
        return _value;
    }

    public GeoLocation getPos() {
        return _pos;
    }

    public int getSrcNode() {
        return this._node.getKey();
    }

    public Pokemon get_curr_fruit() {
        return _curr_fruit;
    }

    public void set_curr_fruit(Pokemon _curr_fruit) {
        this._curr_fruit = _curr_fruit;
    }

    public List<NodeData> get_path() {
        return _path;
    }

    public void set_path(List<NodeData> _path) {
        this._path = _path;
    }

    public double get_speed() {
        return _speed;
    }

    public EdgeData get_edge() {
        return _edge;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "_id=" + _id +
                ", _value=" + _value +
                ", _speed=" + _speed +
                ", _pos=" + _pos +
                ", _node=" + _node +
                ", _edge=" + _edge +
                '}';
    }

}

