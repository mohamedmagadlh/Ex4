package api;
import com.google.gson.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class WDGraph_Algo implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph _g;
    public WDGraph_Algo(DirectedWeightedGraph g) {
        init(g);
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this._g = g;
    }

    @Override
    public Collection<NodeData> getV() {
        return null;
    }

    @Override
    public Collection<EdgeData> getE(int node_id) {
        return null;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this._g;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new WDGraph_DS(_g);
    }


    @Override
    public boolean isConnected() {
        int num = -1;
        if (_g.getV().iterator().hasNext()) {
            // initialize the nodes tag
            for (NodeData i : this._g.getV()) {
                i.setTag(num);
            }

            NodeData connectedNode = _g.getV().iterator().next();

            int sum = this.connectedCheck(connectedNode, connectedNode, num);

            // checking if the value that we get from the connectedCheck function equal to the number of node in the graph
            // if not we return false
            if (sum != _g.getV().size()) {
                return false;
            }
            // here we check if other node can connect to first node(connected node) if not we return false
            for (NodeData i : _g.getV()) {
                this.connectedCheck(i, connectedNode, ++num);
                if (connectedNode.getTag() == num) {
                    return false;
                }
            }
        }
        //we return true if all the node connected to another
        return true;
    }

    /**
     * change the tag to the distance between node_data to the rest of the graph nodes until it reach the destination
     * if the tag = -1 the nodes are not connected
     *
     * @return the sum of the marked nodes
     * @@param node_data
     */
    private int connectedCheck(NodeData src, NodeData dest, int num) {
        int sum = 0;

        Queue<NodeData> q = new LinkedList<>();
        src.setTag(num + 1);
        sum++;
        //the Queue add the first node and then add their neighbors and so on
        q.add(src);

        while (!q.isEmpty()) {
            int temp = q.poll().getKey();
            for (EdgeData i : this._g.getE(temp)) {
                NodeData n_d = _g.getNode(i.getDest());
                if (n_d.getTag() != num + 1) {
                    //every time that new unseen node enter the queue sum++
                    sum++;
                    q.add(n_d);
                    n_d.setTag(num + 1);
                    if (n_d == dest)
                        return 0;
                }
            }
        }
        return sum;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        List<NodeData> ll = new ArrayList<>();
        double flag = shortestPathDist(src, dest);
        //first we check if the nodes are connected and if so we use the function ShortPath to return the shortest Path
        if (flag != -1) {
            NodeData Src = this._g.getNode(src);
            NodeData Dest = this._g.getNode(dest);

            return ShortPath(Dest, Src, ll);
        }
        return null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return weight of shortest path
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        NodeData Src = _g.getNode(src);
        NodeData Dest = _g.getNode(dest);

        if (Src != null && Dest != null) {
            this.initNodeWeight();
            this.initNodeTag();

            setDistance(Src);

            return Dest.getWeight();
        }
        return -1;
    }

    private void setDistance(NodeData n) {
        PriorityQueue<NodeData> q = new PriorityQueue<>();
        n.setWeight(0);
        q.add(n);

        while (!q.isEmpty()) {
            NodeData temp = q.poll();

            for (EdgeData i : _g.getE(temp.getKey())) {
                double SEdge = i.getWeight() + temp.getWeight();
                if (_g.getNode(i.getDest()).getWeight() == -1 || (_g.getNode(i.getDest()).getWeight() > SEdge)) {//&& _g.getNode(i.getDest()).getWeight() != 0)
                    q.add(_g.getNode(i.getDest()));
                    _g.getNode(i.getDest()).setWeight(SEdge);
                }
            }
        }
    }

    /**
     * return list with the path from one node to the other
     *
     * @param src  id of src node
     * @param dest id of dest node
     * @param ll   list
     */
    private List<NodeData> ShortPath(NodeData dest, NodeData src, List<NodeData> ll) {
        //we start by putting the dest in the stack
        Stack<NodeData> stack = new Stack<>();

        stack.add(dest);
        NodeData temp = stack.peek();
        temp.setTag(-2);
        //then we check until we reach the source node
        while (temp != src) {
            impNodeData n_d = (impNodeData) temp;
            for (NodeData i : n_d.getConnectedNode().values()) {
                //if the neighbors weight + the edge wight to the node equal to the node wight then we putting it in the stack
                if (n_d.getWeight() == i.getWeight() + _g.getEdge(i.getKey(), temp.getKey()).getWeight() && i.getTag() != -2) {//
                    stack.add(i);
                    temp = stack.peek();
                    temp.setTag(-2);
                    break;
                }
            }
        }

        int t = stack.size();
        //we changing the stack into list , and then return it
        for (int i = 0; i < t; i++) {
            ll.add(stack.pop());
        }
        return ll;
    }

    /**
     * Initialize the nodes weight to -1.
     */
    private void initNodeWeight() {
        for (NodeData i : _g.getV()) {
            i.setWeight(-1);
        }
    }

    /**
     * Initialize the nodes tag to -1.
     */
    private void initNodeTag() {
        for (NodeData i : _g.getV()) {
            i.setTag(-1);
        }
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file the file name (may include a relative path).
     * @return true iff the file was successfully saved.
     */
    @Override
    public boolean save(String file) {
        // we save the file using json format
        JsonObject json_obj = new JsonObject();
        JsonArray nodes_arr = new JsonArray();
        for (NodeData i : _g.getV()) {
            JsonObject jo_node = new JsonObject();
            if (i.getLocation() != null) {
                String loc = i.getLocation().x() + "," + i.getLocation().y() + "," + i.getLocation().z();
                jo_node.addProperty("pos", loc);
            } else {
                jo_node.addProperty("pos", ",,");
            }
            jo_node.addProperty("id", i.getKey());
            nodes_arr.add(jo_node);
        }

        JsonArray edges_arr = new JsonArray();
        for (NodeData i : _g.getV()) {
            for (EdgeData j : _g.getE(i.getKey())) {
                JsonObject jo_edge = new JsonObject();
                jo_edge.addProperty("src", j.getSrc());
                jo_edge.addProperty("w", j.getWeight());
                jo_edge.addProperty("dest", j.getDest());
                edges_arr.add(jo_edge);
            }
        }

        json_obj.add("Edges", edges_arr);
        json_obj.add("Nodes", nodes_arr);

        Gson gs = new Gson();
        File f = new File(file);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(gs.toJson(json_obj));
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        //we opening the json file that wee save and make from it a new graph
        JsonObject json_obj;
        try {
            String json_str = new String(Files.readAllBytes(Paths.get(file)));
            DirectedWeightedGraph g = new WDGraph_DS();
            json_obj = JsonParser.parseString(json_str).getAsJsonObject();

            JsonArray nodes_arr = json_obj.get("Nodes").getAsJsonArray();
            for (JsonElement i : nodes_arr) {
                String[] xyz = i.getAsJsonObject().get("pos").getAsString().split(",");
                impNodeData n = new impNodeData(i.getAsJsonObject().get("id").getAsInt());
                GeoLocation loc = new impGeoLocation
                        (Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]), Double.parseDouble(xyz[2]));
                n.setLocation(loc);
                g.addNode(n);
            }

            JsonArray edges_arr = json_obj.get("Edges").getAsJsonArray();
            for (JsonElement i : edges_arr) {
                int src = i.getAsJsonObject().get("src").getAsInt();
                int dest = i.getAsJsonObject().get("dest").getAsInt();
                double w = i.getAsJsonObject().get("w").getAsDouble();
                g.connect(src, dest, w);
            }
            init(g);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<List<NodeData>> connected_components() {
        Set<NodeData> ll = new LinkedHashSet<>();
        List<List<NodeData>> res = new LinkedList<>();
        List<NodeData> ll_index = new LinkedList<>(_g.getV());

        for (NodeData i : ll_index) {
            if (!ll.contains(i)) {
                List<NodeData> temp = connected_component(i.getKey());
                res.add(temp);
                ll.addAll(temp);
            }
        }
        return res;
    }

    public List<NodeData> connected_component(int id) {
        NodeData src = _g.getNode(id);
        int num = -1;
        for (NodeData i : this._g.getV()) {
            i.setTag(num);
        }
        num++; //==0
        set_connected_tag(_g.getNode(id), num);
        List<NodeData> ll = new LinkedList<>();
        ll.add(src);
        for (NodeData i : this._g.getV()) {
            if (i.getTag() == 0) {
                ll.add(i);
            }
        }
        List<NodeData> res = new LinkedList<>();

        for (NodeData i : ll) {
            num++;
            if (connect_to_src(src, i, num)) {
                res.add(i);
            }
        }
        return res;
    }


    private void set_connected_tag(NodeData src, int num) {
        PriorityQueue<NodeData> q = new PriorityQueue<>();
        src.setTag(-2);
        q.add(src);

        while (!q.isEmpty()) {
            NodeData temp = q.poll();

            for (EdgeData i : _g.getE(temp.getKey())) {
                NodeData t_node = _g.getNode(i.getDest());
                if (t_node.getTag() == -1) {
                    //set tag to 0 if we can reach if from the src node
                    t_node.setTag(num);
                    q.add(t_node);
                }
            }
        }
    }

    private boolean connect_to_src(NodeData src, NodeData node, int num) {
        if (src == node) {
            return true;
        }
        PriorityQueue<NodeData> q = new PriorityQueue<>();
        node.setTag(num);
        q.add(node);

        while (!q.isEmpty()) {
            NodeData temp = q.poll();
            for (EdgeData i : _g.getE(temp.getKey())) {
                NodeData t_node = _g.getNode(i.getDest());
                if (t_node.getTag() != num) {
                    if (t_node.getTag() == -2) {
                        node.setTag(-2);
                        return true;
                    }
                    t_node.setTag(num);
                    q.add(t_node);
                }
            }
        }
        return false;
    }
}
