package api;
import java.util.HashMap;
import java.util.Objects;

public  class impNodeData implements NodeData, Comparable<NodeData> {

    private static int _masterKey = 0;
    private  int _key;
    private int _tag;
    private double _weight;
    private String _remark;
    private GeoLocation _GLocation;


    private  HashMap<Integer, NodeData> _connectedNode;
    private  HashMap<Integer, EdgeData> _neighborsDis;
    public impNodeData() {
        this._key = _masterKey++;
        this._connectedNode = new HashMap<>();
        this._neighborsDis = new HashMap<>();
        this._remark = "";
        this.setTag(-1);
        _GLocation = new impGeoLocation(0, 0, 0);
        _weight = 0;
    }
    public impNodeData(int key) {
        this._key = key;
        this._connectedNode = new HashMap<>();
        this._neighborsDis = new HashMap<>();
        this._remark = "";
        this.setTag(-1);
        _GLocation = new impGeoLocation(0, 0, 0);
        _weight = 0;
    }
    public impNodeData(NodeData n) {
        _key = n.getKey();
        _remark = n.getInfo();
        _weight = n.getWeight();
        _tag = n.getTag();
        _connectedNode = new HashMap<>();
        _neighborsDis = new HashMap<>();
        if (n.getLocation() == null)
            _GLocation = null;
        else
            _GLocation = new impGeoLocation(n.getLocation());
    }


    public HashMap<Integer, EdgeData> getNeighborsDis() {
        return _neighborsDis;
    }
    public HashMap<Integer, NodeData> getConnectedNode() {
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

    @Override
    public GeoLocation getLocation() {
        return _GLocation;
    }



    @Override
    public void setLocation(GeoLocation p) {
        this._GLocation = p;
    }

    @Override
    public double getWeight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
    }


    @Override
    public String getInfo() {
        return this._remark;
    }

    @Override
    public void setInfo(String s) {
        this._remark = s;
    }

    @Override
    public int getTag() {
        return this._tag;
    }

    @Override
    public void setTag(int t) {
        this._tag = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        impNodeData nodeData = (impNodeData) o;
        return _key == nodeData._key &&
                _neighborsDis.equals(nodeData._neighborsDis) &&
                _GLocation.equals(nodeData._GLocation);
    }

    @Override
    public int compareTo(NodeData o) {
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
