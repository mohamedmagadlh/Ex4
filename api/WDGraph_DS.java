package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class represent a weighted and directional graph, implements directed_weighted_graph interface.
 * WDGraph_DS contains inner private class {@link EdgeData} that represent the edge of the graph.
 * every {@link WDGraph_DS} has a {@link HashMap} calls _graphNodes contains all the vertices in the graph
 * the keys in this hashmap are the keys of the {@link NodeData} keys, that has a unique key to each node.
 */
public class WDGraph_DS implements directed_weighted_graph {

    private final HashMap<Integer, node_data> _graphNodes;
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

    /**
     * Copy constructor
     * get directed_weighted_graph g as param and compute a deep copy of g.
     *
     * @param g directed_weighted_graph
     */
    public WDGraph_DS(directed_weighted_graph g) {
        _graphNodes = new HashMap<>();
        for (node_data i : g.getV()) {
            _graphNodes.put(i.getKey(), new NodeData(i));
        }
        for (node_data i : g.getV()) {
            for (edge_data j : g.getE(i.getKey())) {
                connect(i.getKey(), j.getDest(), g.getEdge(i.getKey(), j.getDest()).getWeight());
            }
        }
    }

    /**
     * Returns the node_data by the node_id.
     * this method take this vale by the key from _graphNodes field.
     *
     * @param key the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return this._graphNodes.get(key);
    }

    /**
     * Returns the data of the edge (src,dest), null if none.
     * this method take this vale by the key from _neighborsDis in {@link NodeData}.
     *
     * @param src  the node id of the source node.
     * @param dest the node id of the destination node.
     * @return edge_data
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        NodeData temp = (NodeData) this.getNode(src);
        if (temp == null)
            return null;
        return temp.getNeighborsDis().get(dest);
    }

    /**
     * Adds a new node to the graph with the given node_data.
     * adds to _graphNodes HashMap.
     *
     * @param n node_data
     */
    @Override
    public void addNode(node_data n) {
        if (!this._graphNodes.containsKey(n.getKey())) {
            this._graphNodes.put(n.getKey(), n);
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * connects the edge in {@link NodeData} fields, in _connectedNode and _neighborsDis.
     *
     * @param src  the source of the edge.
     * @param dest the destination of the edge.
     * @param w    positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        NodeData tempSrc = (NodeData) this.getNode(src);
        NodeData tempDest = (NodeData) this.getNode(dest);
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
        tempSrc.getNeighborsDis().put(dest, new EdgeData(src, dest, w));
        this._mode_count++;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * this method runs in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return this._graphNodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * this method runs in O(1) time.
     *
     * @param node_id key
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        NodeData temp = (NodeData) this.getNode(node_id);
        if (temp == null)
            return null;
        return temp.getNeighborsDis().values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method runs in O(k), k = degree(key).
     *
     * @param key node_id
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        NodeData temp = (NodeData) this.getNode(key);
        if (temp == null) {
            return null;
        }
        for (node_data i : temp.getConnectedNode().values()) {
            NodeData tempI = (NodeData) i;
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

    /**
     * Deletes the edge from the graph,
     * this method run in O(1) time.
     *
     * @param src the source of the edge.
     * @param dest the destination of the edge.
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        NodeData tempSrc = (NodeData) this.getNode(src);
        NodeData tempDest = (NodeData) this.getNode(dest);
        if (tempDest == null || tempSrc == null || this.getEdge(src, dest) == null) {
            return null;
        }
        this._mode_count++;
        this._edge_size--;
        tempDest.getConnectedNode().remove(src);
        return tempSrc.getNeighborsDis().remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * This method run in O(1) time.
     *
     * @return number of nodes.
     */
    @Override
    public int nodeSize() {
        return this._graphNodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * This method run in O(1) time.
     *
     * @return number of edges.
     */
    @Override
    public int edgeSize() {
        return this._edge_size;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return mode_count
     */
    @Override
    public int getMC() {
        return this._mode_count;
    }

    @Override
    public String toString() {
        String edgesStr = "[";
        for (node_data i : getV()) {
            for (edge_data j : getE(i.getKey())) {
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

    /**
     * Equal method. return true iff o is {@link WDGraph_DS},
     * and they both has the same nodes and edges. uses equal method of {@link NodeData}
     * @param o {@link Objects}
     * @return true iff o is WDGraph_DS and is equals to this.
     */
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
    private class EdgeData implements edge_data {

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
        public EdgeData(int src, int dest, double weight) {
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

        /**
         * Equal method. returns true iff o is {@link EdgeData} and they both
         * has the same src, dest and weight.
         * @param o {@link Objects}
         * @return true iff o and this are equal.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeData edgeData = (EdgeData) o;
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
