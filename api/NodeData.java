package api;

import java.util.HashMap;
import java.util.Objects;

/**
 * This class represents a node in directional and wighted graph.
 * every node has a unique id key, tag, weight, remark and location.
 * In addition, every node store the edge_data that coming out from this node,
 * and also the node that connect to this node.
 */
public class NodeData implements node_data, Comparable<node_data> {

    private static int _masterKey = 0;
    private final int _key;
    private int _tag;
    private double _weight;
    private String _remark;
    private geo_location _GLocation;

    /**
     * This {@link HashMap} stores the nodes that connect to this node.
     * key - Integer number, the id of the node.
     * value - The node_data corresponding to the key.
     */
    private final HashMap<Integer, node_data> _connectedNode;

    /**
     * This {@link HashMap} stores the neighbors nodes of this node by the edge_data between them.
     * key - Integer number, the id of the neighbor node.
     * value - The edge_data between this to the node with the corresponding key.
     */
    private final HashMap<Integer, edge_data> _neighborsDis;

    /**
     * Default constructor.
     * init the field, and creates the HashMaps.
     * key field get value from the static field _masterKey.
     */
    public NodeData() {
        this._key = _masterKey++;
        this._connectedNode = new HashMap<>();
        this._neighborsDis = new HashMap<>();
        this._remark = "";
        this.setTag(-1);
        _GLocation = new Geo_locationImpl(0, 0, 0);
        _weight = 0;
    }

    /**
     * Constructor.
     * init the field, and creates the HashMaps.
     * key field get value from key param.
     *
     * @param key id
     */
    public NodeData(int key) {
        this._key = key;
        this._connectedNode = new HashMap<>();
        this._neighborsDis = new HashMap<>();
        this._remark = "";
        this.setTag(-1);
        _GLocation = new Geo_locationImpl(0, 0, 0);
        _weight = 0;
    }

    /**
     * Copy constructor.
     * All fields are copied.
     * Except from the HashMaps that initialized to new empty HashMaps.
     *
     * @param n node data
     */
    public NodeData(node_data n) {
        _key = n.getKey();
        _remark = n.getInfo();
        _weight = n.getWeight();
        _tag = n.getTag();
        _connectedNode = new HashMap<>();
        _neighborsDis = new HashMap<>();
        if (n.getLocation() == null)
            _GLocation = null;
        else
            _GLocation = new Geo_locationImpl(n.getLocation());
    }

    /**
     * Returns the HashMap contains all the edges that coming out of this node.
     * @return _neighborsDis {@link HashMap}
     */
    public HashMap<Integer, edge_data> getNeighborsDis() {
        return _neighborsDis;
    }

    /**
     * Returns the HashMap contains all the nodes that connect to this node.
     * @return _neighborsDis {@link HashMap}
     */
    public HashMap<Integer, node_data> getConnectedNode() {
        return _connectedNode;
    }

    /**
     * Returns the key (id) associated with this node.
     * @return key
     */
    @Override
    public int getKey() {
        return this._key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     * @return geo_location
     */
    @Override
    public geo_location getLocation() {
        return _GLocation;
    }

    /**
     * Allows changing this node's location.
     * @param p new location (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this._GLocation = p;
    }

    /**
     * Returns the weight associated with this node.
     * @return weight
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    /**
     * Allows changing this node's weight.
     * @param w the new weight.
     */
    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     * @return remark.
     */
    @Override
    public String getInfo() {
        return this._remark;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s the new remark.
     */
    @Override
    public void setInfo(String s) {
        this._remark = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms.
     * @return the new tag.
     */
    @Override
    public int getTag() {
        return this._tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t the new value of the tag.
     */
    @Override
    public void setTag(int t) {
        this._tag = t;
    }

    /**
     * Equal method. return true iff o is {@link NodeData},
     * and they both has the same key, same neighbors, and same nodes are neighbors of they both.
     * @param o {@link Objects}
     * @return true iff o is NodeData and is equals to this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return _key == nodeData._key &&
                _neighborsDis.equals(nodeData._neighborsDis) &&
                _GLocation.equals(nodeData._GLocation);
    }

    /**
     * This method is implements because this class implements Comparable.
     * Allows to compare nodes by their weight.
     * @param o other node to compare
     * @return
     */
    @Override
    public int compareTo(node_data o) {
        if (this._weight > o.getWeight())
            return 1;
        if (this._weight < o.getWeight())
            return -1;
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_key);
    }

    @Override
    public String toString() {
        return "(" + _key + ")";
    }
}