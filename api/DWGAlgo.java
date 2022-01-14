package api;

import java.util.*;


public class DWGAlgo implements DirectedWeightedGraphAlgorithms {
    private DWG g;
   public DWGAlgo(String json){
       g = new DWG(json);
       init(g);
   }
    @Override
    public void init(DirectedWeightedGraph g) {

        this.g = (DWG) g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {

        return this.g;
    }

    public DirectedWeightedGraph copy() {return null;}
    /*    DWG newG = new DWG("");
        for (NodeData i : this.g.NodesHash.values()) {
            NodeData v = new impNodeData(i.getKey(), i.getLocation(), i.getWeight(), i.getInfo(), i.getTag());
            newG.NodesHash.put(v.getKey(), v);
            newG.addNode(v);
        }
        newG.setMC(g.getMC());

        NodeData vert = new impNodeData(this.g.NodesHash.getKey(), this.g.NodesHash.getLocation(), this.g.NodesHash.getWeight(), this.g.NodesHash.getInfo(), this.g.NodesHash.getTag());

        return newG;
    }*/

    @Override
    public boolean isConnected() {
        for (int i = 0; i < this.g.NodesHash.size(); i++) {
            boolean[] visited = new boolean[this.g.NodesHash.size()];
            DFS(this.g.NodesHash.get(i).getKey(), visited);
            for (boolean b : visited)
                if (!b) return false;

        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {

        double pathDist = 0;
        Iterator<NodeData> I = shortestPath(src, dest).stream().iterator();
        while (I.hasNext()) {
            pathDist += I.next().getWeight();
        }

        return pathDist;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        List<NodeData> list = null;
        list.add(g.NodesHash.get(src));
        Iterator<NodeData> IN = this.g.nodeIter();
        PriorityQueue<NodeData> Q = new PriorityQueue<>();
        for (NodeData i : g.NodesHash.values()) {
            if (i.getKey() == src)
                g.NodesHash.get(i.getKey()).setInfo(0 + "");
            else if (!g.EdgesHash.get(src).containsKey(i.getKey()))
                g.NodesHash.get(i.getKey()).setInfo(Double.MAX_VALUE + "");
            else if (g.EdgesHash.get(src).containsKey(i.getKey()))
                g.NodesHash.get(i.getKey()).setInfo("" + g.NodesHash.get(i.getKey()).getWeight());
        }
        int start = src;
        int i = 0;
        ArrayList l = null;
        while (i < g.NodesHash.size()) {
            if (start == dest)
                return list;
            NodeData Min = SHORT(start, dest, l);
            l.add(start);
            list.add(Min);
            start = Min.getKey();
            i++;
        }
        return list;
    }

    public NodeData SHORT(int start, int dest, ArrayList arr) {
        double min = Double.MAX_VALUE;
        Iterator<NodeData> IN = this.g.nodeIter();
        NodeData Min = null;
        while (IN.hasNext()) {
            NodeData D = IN.next();
            if (g.EdgesHash.get(start).containsKey(D.getKey())
                    && !arr.contains(D.getKey())
                    && start != D.getKey()) {
                double x = Double.parseDouble(D.getInfo()) +
                        Double.parseDouble(g.NodesHash.get(start).getInfo());
                D.setInfo("" + x);
                if (x < min) {
                    min = x;
                    Min = D;
                }
            }
        }
        return Min;
    }


    @Override
    public NodeData center() {
        if (!isConnected()) {
            return null;
        }
        double rad = Integer.MAX_VALUE;
        int n = this.g.nodeSize();
        double[][] dist = new double[n][n];
        double[] ecc = new double[n];
        ArrayList<Integer> centL = new ArrayList<>();
        double diam = 0;
        for (int k = 0; k < n; k++) {
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ecc[i] = Math.max(ecc[i], dist[i][j]);
            }
        }
        for (int i = 0; i < n; i++) {
            rad = Math.min(rad, ecc[i]);
            diam = Math.max(diam, ecc[i]);
        }
        for (int i = 0; i < n; i++) {
            if (ecc[i] == rad) {
                centL.add(i);
            }
        }
        int h = centL.size();
        Integer[] centA = new Integer[h];
        centA = centL.toArray(centA);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < centA.length; i++) {
            if (centA[i] < min)
                min = centA[i];
        }
        return this.g.getNode(min);
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities.size() == 0) return null;
        LinkedList<NodeData> nodesPath = new LinkedList<NodeData>();
        int i = 0;
        int srcNode = cities.get(i++).getKey();
        if (cities.size() == 1) {
            nodesPath.add(this.g.getNode(srcNode));
            return nodesPath;
        }
        while (i < cities.size()) {
            int destNode = cities.get(i++).getKey();
            if (shortestPath(srcNode, destNode) == null) return null;
            LinkedList<NodeData> newPath = (LinkedList<NodeData>) shortestPath(srcNode, destNode);
            if (i != 2)
                newPath.remove(newPath.get(0).getKey());
            nodesPath.addAll(newPath);
            srcNode = destNode;

        }


        return nodesPath;
    }

    @Override
    public boolean save(String file) {return  false;}
       /* GsonBuilder g = new GsonBuilder();
        g.registerTypeAdapter(DWG.class, new Json_Graph());
        Gson gson = g.create();
        String json = gson.toJson(getGraph());
        try {
            PrintWriter p = new PrintWriter(new File(file));
            p.write(json);
            p.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/

    @Override
    public boolean load(String file) {return  false;}
       /* try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWG.class, new Json_Graph());
            Gson gson = builder.create();

            FileReader reader = new FileReader(file);
            DWG nGraph = gson.fromJson(reader, DWG.class);
            this.init(nGraph);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }*/

    public void DFS(int v, boolean[] visited)//algo DFS
    {
        // mark current node as visited
        visited[v] = true;
        // do for every edge (v, u)
        for (int j = 0; j < g.NodesHash.size(); j++) {
            if (this.g.EdgesHash.get(v).containsKey(j)) {
                if (!visited[j]) {
                    DFS(j, visited);

                }
            }
        }

    }
}