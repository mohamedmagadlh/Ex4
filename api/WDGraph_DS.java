package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WDGraph_DS implements DirectedWeightedGraph {

    private final HashMap<Integer, NodeData> _graphNodes;
    private int _edge_size;
    private int _mode_count;

    /**
     * Default constructor
     */
    public WDGraph_DS() {
        this._graphNodes = new HashMap<>();
        this._edge_size = 0;
        this._mode_count = 0;
    }

    public WDGraph_DS(DirectedWeightedGraph g) {
        _graphNodes = new HashMap<>();
        for (NodeData i : g.getV()) {
            _graphNodes.put(i.getKey(), new impNodeData(i));
        }
        for (NodeData i : g.getV()) {
            for (EdgeData j : g.getE(i.getKey())) {
                connect(i.getKey(), j.getDest(), g.getEdge(i.getKey(), j.getDest()).getWeight());
            }
        }
    }

    @Override
    public NodeData getNode(int key) {
        return this._graphNodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        impNodeData temp = (impNodeData) this.getNode(src);
        if (temp == null)
            return null;
        return temp.getNeighborsDis().get(dest);
    }
    @Override
    public void addNode(NodeData n) {
        if (!this._graphNodes.containsKey(n.getKey())) {
            this._graphNodes.put(n.getKey(), n);
        }
    }
    @Override
    public void connect(int src, int dest, double w) {
        impNodeData tempSrc = (impNodeData) this.getNode(src);
        impNodeData tempDest = (impNodeData) this.getNode(dest);
        if (tempSrc == null || tempDest == null) {
            return;
        }
        if (this.getEdge(src, dest) != null)
            if (this.getEdge(src, dest).getWeight() == w || src == dest) {
                return;
            }
        if (this.getEdge(src, dest) == null) {
            this._edge_size++;
        }

        tempDest.getConnectedNode().put(src, tempSrc);
        tempSrc.getNeighborsDis().put(dest, new impEdgeData(src, dest, w));
        this._mode_count++;
    }

    public Collection<NodeData> getV() {
        return this._graphNodes.values();
    }
    public Collection<EdgeData> getE(int node_id) {
        impNodeData temp = (impNodeData) this.getNode(node_id);
        if (temp == null)
            return null;
        return temp.getNeighborsDis().values();
    }


    @Override
    public NodeData removeNode(int key) {
        impNodeData temp = (impNodeData) this.getNode(key);
        if (temp == null) {
            return null;
        }
        for (NodeData i : temp.getConnectedNode().values()) {
            impNodeData tempI = (impNodeData) i;
            tempI.getNeighborsDis().remove(key);
            tempI.getConnectedNode().remove(key);
            this._edge_size--;
            this._mode_count++;
        }
        int t = getE(key).size();
        _edge_size -= t;
        _mode_count += t;
        _mode_count++;
        this._graphNodes.remove(key);
        return temp;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        impNodeData tempSrc = (impNodeData) this.getNode(src);
        impNodeData tempDest = (impNodeData) this.getNode(dest);
        if (tempDest == null || tempSrc == null || this.getEdge(src, dest) == null) {
            return null;
        }
        this._mode_count++;
        this._edge_size--;
        tempDest.getConnectedNode().remove(src);
        return tempSrc.getNeighborsDis().remove(dest);
    }

    @Override
    public int nodeSize() {
        return this._graphNodes.size();
    }

    @Override
    public int edgeSize() {
        return this._edge_size;
    }

    @Override
    public int getMC() {
        return this._mode_count;
    }

    @Override
    public String toString() {
        String edgesStr = "[";
        for (NodeData i : getV()) {
            for (EdgeData j : getE(i.getKey())) {
                edgesStr += j + ", ";
            }
        }
        return "WDGraph_DS{" +
                ", edge_size=" + _edge_size +
                ", mode_count=" + _mode_count +
                "\n\tNodes=" + _graphNodes +
                "\n\tEdges=" + edgesStr + "]" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WDGraph_DS that = (WDGraph_DS) o;
        return _edge_size == that._edge_size &&
                _graphNodes.equals(that._graphNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_graphNodes, _edge_size);
    }

    /**
     * This inner class represent a directional edge between 2 nodes in the graph.
     * In addition every edge has weight, tag and info fields.
     */
    private class impEdgeData implements EdgeData {

        private final int _src, _dest;
        private final double _weight;
        private int _tag;
        private String _info;

        /**
         * Constructor.
         * @param src id of source node
         * @param dest id of destination node
         * @param weight weight of this edge
         */
        public impEdgeData(int src, int dest, double weight) {
            _src = src;
            _dest = dest;
            _tag = 0;
            _weight = weight;
            _info = "";
        }

        /**
         * Returns the id of the source node of this edge.
         * @return src id
         */
        @Override
        public int getSrc() {
            return _src;
        }

        /**
         * Returns the id of the destination node of this edge.
         * @return dest id
         */
        @Override
        public int getDest() {
            return _dest;
        }

        /**
         * Returns the weight of this edge (positive value).
         * @return weight
         */
        @Override
        public double getWeight() {
            return _weight;
        }

        /**
         * Returns the remark (meta data) associated with this edge.
         * @return info
         */
        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * Allows changing the remark (meta data) associated with this edge.
         * @param s string replace the current remark.
         */
        @Override
        public void setInfo(String s) {
            this._info = s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         * @return tag
         */
        @Override
        public int getTag() {
            return _tag;
        }

        /**
         * This method allows setting the "tag" value for temporal marking an edge - common
         * practice for marking by algorithms.
         * @param t the new value of the tag.
         */
        @Override
        public void setTag(int t) {
            this._tag = t;
        }

        @Override
        public String toString() {
            return "(" + _src + " -> " + _dest + "): weight=" + _weight + '}';
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            impEdgeData edgeData = (impEdgeData) o;
            return _src == edgeData._src &&
                    _dest == edgeData._dest &&
                    Double.compare(edgeData._weight, _weight) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(_src, _dest, _weight);
        }
    }
}

