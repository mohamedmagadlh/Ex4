package gameClient;

import api.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gameClient.util.point;

import java.util.HashMap;
import java.util.List;

public class Agent {

    private int _id, _src, _dest;
    private double _value, _speed;
    private float _time;
    private GeoLocation _pos;
    private NodeData _node;
    private impEdgeData _edge;
    private List<NodeData> _path;
    private Pokemon _curr_fruit;
    private static DirectedWeightedGraph _graph;

    public Agent(int _id, int _src, int _dest, double _speed, GeoLocation _pos) {
        this._time = Float.MAX_VALUE;
        this._speed = _speed;
        this._pos = _pos;
        this._dest = _dest;
        this._src = _src;
        this._id = _id;
    }

    public Agent(AgentJson.AgentJsonInner agent) {
        this._id = agent.id;
        this._value = agent.value;
        this._src = agent.src;
        this._dest = agent.dest;
        this._speed = agent.speed;
        this._pos = new geo(agent.locationStr);
    }


    public static HashMap<Integer, Agent> load(String json) {
        AgentJson dgj = new Gson().fromJson(json, AgentJson.class);
        HashMap<Integer, Agent> agents = new HashMap<>();
        for (int i = 0; i < dgj.getAgents().size(); i++) {
            Agent agent = new Agent(dgj.getAgents().get(i).getAgent());
            agents.put(agent.getId(), agent);
        }
        return agents;
    }

    public void update(JsonObject agent) {
        _id = agent.get("id").getAsInt();
        _value = agent.get("value").getAsDouble();
        _speed = agent.get("speed").getAsDouble();
        _pos = new point(agent.get("pos").getAsString());
        setNode(agent.get("src").getAsInt());
    }
    public void set_time(float time){
        this._time = time ;
    }
    public float get_time(){
        return _time;
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
    public geo getPos() {
        return (geo) _pos;
    }
    public  void  set_pos(GeoLocation pos){
        this._pos = pos ;
    }
    public int getSrcNode() {
        return this._node.getKey();
    }
    public List<NodeData> get_path() {
        return _path;
    }
    public  void set_src(int src){
        this._src = src;
    }
    public int get_src(){
        return _src;
    }
    public  void set_dest(int dest){
        this._dest = dest ;
    }
    public int get_dest(){
        return _dest;
    }
    public void set_path(List<NodeData> _path) {
        this._path = _path;
    }
    public void set_Speed(double speed) {
        this._speed = speed;
    }
    public double get_speed() {
        return _speed;
    }
    public impEdgeData get_edge() {
        return _edge;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "_id=" + _id +
                ", _speed=" + _speed +
                ", _pos=" + _pos +
                ", _node=" + _node +
                ", _edge=" + _edge +
                " , _time=" + _time +
                '}';
    }

}

