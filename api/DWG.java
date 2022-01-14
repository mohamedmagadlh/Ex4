package api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DWG implements DirectedWeightedGraph {
    private HashMap<String , impEdgeData>jsonEdge;
    private HashMap<Integer , NodeData>jsoNode;
    public HashMap<Integer,HashMap<Integer,impEdgeData>>jsonIn;
    public HashMap<Integer,HashMap<Integer,impEdgeData>>jsonOut;
    public HashMap<Integer , Integer>jsonMc;
    private NodeData nodeData;
    public HashMap<Integer, NodeData> NodesHash;
    public HashMap<Integer,HashMap<Integer,EdgeData>>EdgesHash;
    int MC;

    public DWG(String json) {
        jsonMc = new HashMap<>();
        jsonIn = new HashMap<>();
        jsonOut = new HashMap<>();
        jsonEdge = new HashMap<>();
        jsoNode = new HashMap<>();
        nodeData = null;
        this.NodesHash = new HashMap<Integer, NodeData>();
        this.EdgesHash = new HashMap<Integer, HashMap<Integer, EdgeData>>();
        this.MC = 0;
        loadjson(json);
    }
    @Override
    public NodeData getNode(int key) {
        return NodesHash.get(key);
    }
    @Override
    public EdgeData getEdge(int src, int dest) {
        if(NodesHash.containsKey(src) && NodesHash.containsKey(dest) &&EdgesHash.get(src).containsKey(dest))
            return EdgesHash.get(src).get(dest);
        return null;
    }

    @Override
    public void addNode(NodeData n) {
        if (this.NodesHash.containsKey(n.getKey()))
            return;

        this.NodesHash.put(n.getKey(), n);
        this.EdgesHash.put(n.getKey(), new HashMap<>());
        this.jsonIn.put(n.getKey(), new HashMap<>());
        changeHappened();
    }


    @Override
    public void connect(int src, int dest, double w) {
        if (src == dest || !this.NodesHash.containsKey(src) || !this.NodesHash.containsKey(dest))
            return;
        impEdgeData ed = new impEdgeData(src, dest, w);
        this.EdgesHash.get(src).put(dest, ed);
        this.jsonIn.get(dest).put(src, ed);
        changeHappened();
    }
    @Override
    public Iterator<NodeData> nodeIter() {
        return this.NodesHash.values().iterator();
    }

    private List<EdgeData> getEdges() {
        List<EdgeData> edgeData = new ArrayList<>();
        for (HashMap<Integer, EdgeData> value : EdgesHash.values()) {
            edgeData.addAll(value.values());
        }
        return edgeData;
    }
    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<EdgeData>() {
            private int mc = MC;
            private int i = 0;
            private List<EdgeData> ed = getEdges();


            @Override
            public boolean hasNext() {
                if (mc != MC)
                    throw new RuntimeException("object has been changed");
                return i < ed.size();
            }

            @Override
            public EdgeData next() {
                if (mc != MC)
                    throw new RuntimeException("object has been changed");
                return ed.get(i++);
            }


            @Override
            public void remove() {
                i--;
                if (i < 0)
                    throw new RuntimeException("");
                EdgeData eg = ed.remove(i);
                removeEdge(eg.getSrc(), eg.getDest());
                this.mc = MC;
            }

        };
    }


    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        if (!NodesHash.containsKey(node_id))
            throw new RuntimeException("node id not exists");
        return new Iterator<EdgeData>() {
            private int mc = MC;
            private List<EdgeData> it = new ArrayList<>(EdgesHash.get(node_id).values());
            private int i = 0;

            @Override
            public boolean hasNext() {
                if (mc != MC)
                    throw new RuntimeException("object has been changed");
                return i < it.size();
            }

            @Override
            public EdgeData next() {
                if (mc != MC)
                    throw new RuntimeException("object has been changed");
                return it.get(i++);
            }

            @Override
            public void remove() {
                i--;
                if (i < 0)
                    throw new RuntimeException("");
                EdgeData eg = it.remove(i);
                removeEdge(eg.getSrc(), eg.getDest());
                this.mc = mc;
            }
        };
    }

    @Override
    public NodeData removeNode(int key) {
        if(NodesHash.containsKey(key)) {
            NodesHash.remove(key);
            Set<Integer> Keys=EdgesHash.keySet();
            for(Integer e:Keys)
            {
                if(EdgesHash.get((int)e).containsKey(key))
                    EdgesHash.get((int)e).remove(key);
            }
        }
        else {
            System.err.println("REMOVE NODE FAIL\n");
            return null;
        }
        MC++;
        return this.NodesHash.remove(key);
    }

    private void changeHappened() {
        MC++;
    }

    @Override
    public impEdgeData removeEdge(int src, int dest) {
        if(getEdge(src,dest)==null)return null;
        impEdgeData e=new impEdgeData(src,dest);
        EdgesHash.get(src).remove(dest);
        MC++;
        return e;
    }

    @Override
    public int nodeSize() {
        return NodesHash.size();
    }

    @Override
    public int edgeSize() {
        return EdgesHash.size();
    }

    @Override
    public int getMC() {
        return MC;

    }
    public void setMC(int m)
    {
        this.MC=m;
    }
    public void loadjson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jNodes = jsonObject.getJSONArray("Nodes");
        JSONArray jEdges = jsonObject.getJSONArray("Edges");
        for (int i = 0; i < jNodes.length(); i++) {
            String pos = jNodes.getJSONObject(i).getString("pos");
            int id = jNodes.getJSONObject(i).getInt("id");

            String[] locatioNode = pos.split(",");
            double x = Double.parseDouble(locatioNode[0]);
            double y = Double.parseDouble(locatioNode[1]);
            double z = Double.parseDouble(locatioNode[2]);
            NodeData node = new impNodeData(id, x, y, z);
            addNode(node);
            MC = 0;
        }
        for (int i = 0; i < jEdges.length(); i++) {
            int src = jEdges.getJSONObject(i).getInt("src");
            double w = jEdges.getJSONObject(i).getDouble("w");
            int dest = jEdges.getJSONObject(i).getInt("dest");
            connect(src, dest, w);
            jsonMc.put(dest, 0);
            MC = 0;
        }
    }
    @Override
    public String toString() {
        return "DirectedWeightedGraphClass{" +
                "nodeData=" + nodeData +
                ", NodesHash=" + NodesHash +
                ", EdgesHash=" + EdgesHash +
                ", MC=" + MC +
                '}';
    }}


