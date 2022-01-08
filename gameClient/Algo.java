package gameClient;

import api.*;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Algo {
    private static Arena _ar;
    private static DirectedWeightedGraph _graph;
    private static final double EPS = 0.000001;

    static int nextMove(Client client, Agent a) {
        int id = a.getId();
        if (indexOfPok(_ar.getPokemons(), a.get_curr_fruit()) == -1) {
            createPath(a);
            return -1;
        }

        int next_dest = a.get_path().get(0).getKey();
        a.get_path().remove(0);
        if (a.get_path().isEmpty()) {
            _ar.get_pokemonsWithOwner().remove(a.get_curr_fruit());
        }

       chooseEdge(id, next_dest,client);
        return next_dest;
    }
    public static void chooseEdge(int agentId, int nextNode, Client client) {
       client. chooseNextEdge("{\"agent_id\":" + agentId + ", \"next_node_id\":" + nextNode + "}");

    }



    static void placeAgents(int num_of_agents, Client client) {
        PriorityQueue<Pokemon> pq = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Pokemon o1, Pokemon o2) {
                return Double.compare(o2.get_value(), o1.get_value());
            }
        });
        pq.addAll(_ar.getPokemons());

        for (int i = 0; i < num_of_agents && !pq.isEmpty(); i++) {

            addAgent(pq.poll().get_edge().getSrc(),client);

            num_of_agents--;
        }
        if (num_of_agents > 0) {
            placeAgentsByDist(num_of_agents, client);
        }
    }
    static  public  void addAgent(int src,Client C)
    {
        C.addAgent("{\"id\":" + src + "}");
    }

    static void placeAgentsByDist(int num_of_agents, Client client) {
        DirectedWeightedGraphAlgorithms ga = new WDGraph_Algo(_graph);
        ga.shortestPathDist(0, 0);

        PriorityQueue<NodeData> pq = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(NodeData o1, NodeData o2) {
                return Double.compare(o1.getWeight(), o2.getWeight());
            }
        });

        for (Pokemon i : _ar.getPokemons()) {
            pq.add(_graph.getNode(i.get_edge().getSrc()));
        }
        int div = pq.size() / num_of_agents;
        for (int i = 0; i < num_of_agents && !pq.isEmpty(); i++) {
            addAgent(pq.peek().getKey(),client);
            for (int j = 0; j < div; j++) {
                pq.poll();
            }
        }
    }

    synchronized static void createPath(Agent a) {
        if (_ar.getAgents().size() == _ar.getPokemons().size()) {
            createPathByDistance(a);
        } else {
            if (a.get_speed() > 3) {
                createPathByDistance(a);
            } else {
                createPathByValDist(a);
            }
        }
    }

    synchronized static void createPathByValDist(Agent a) {
        DirectedWeightedGraphAlgorithms ga = new WDGraph_Algo();
        ga.init(_graph);

        ga.shortestPathDist(a.getSrcNode(), a.getSrcNode());
        Pokemon min_pokemon = _ar.getPokemons().get(0);
        double shortest_way = _graph.getNode(min_pokemon.get_edge().getSrc()).getWeight();
        if (shortest_way == 0) {
            shortest_way = EPS;
        }
        double max_ValDivDist = min_pokemon.get_value() / shortest_way;
        for (Pokemon p : _ar.getPokemons()) {
            if (indexOfPok(_ar.get_pokemonsWithOwner(), p) == -1) {
                double p_src_weight = _graph.getNode(p.get_edge().getSrc()).getWeight();
                if (p_src_weight == 0) {
                    p_src_weight = EPS;
                }
                double temp = p.get_value() / p_src_weight;
                if (max_ValDivDist < temp) {
                    max_ValDivDist = temp;
                    min_pokemon = p;
                }
            }
        }
        List<NodeData> path = ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc());
        path.add(_graph.getNode(min_pokemon.get_edge().getDest()));
        path.remove(0);
        a.set_path(path);

        a.set_curr_fruit(min_pokemon);
        _ar.get_pokemonsWithOwner().add(min_pokemon);
    }

    synchronized static void createPathByDistance(Agent a) {
        DirectedWeightedGraphAlgorithms ga = new WDGraph_Algo();
        ga.init(_graph);

        Pokemon min_pokemon = _ar.getPokemons().get(0);
        int n = min_pokemon.get_edge().getSrc();
        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);

        for (Pokemon p : _ar.getPokemons()) {
            if (indexOfPok(_ar.get_pokemonsWithOwner(), p) == -1) {
                EdgeData pokemon_edge = p.get_edge();
                int s = pokemon_edge.getSrc();
                double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
                if (dist_src < shortest_way) {
                    shortest_way = dist_src;
                    min_pokemon = p;
                }
            }
        }
        List<NodeData> path = ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc());
        path.add(_graph.getNode(min_pokemon.get_edge().getDest()));
        path.remove(0);
        a.set_path(path);

        a.set_curr_fruit(min_pokemon);
        _ar.get_pokemonsWithOwner().add(min_pokemon);
    }
    public static int indexOfPok(List<Pokemon> arr, Pokemon pok) {
        int ans = -1;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(pok)) {
                ans = i;
                break;
            }
        }
        return ans;
    }

    synchronized static long toSleep(Agent a, int next_dest) {
        EdgeData edge = _graph.getEdge(a.getSrcNode(), next_dest);

        if (next_dest == -1 || edge == null) {
            return (long) (130 - a.get_speed() * 7);
        }
        NodeData node = _graph.getNode(next_dest);

        if (a.get_curr_fruit() != null && !edge.equals(a.get_curr_fruit().get_edge())) {
            double way = edge.getWeight() / a.get_speed();
            way *= 1000;
            return (long) way;

        } else if (edge.equals(a.get_curr_fruit().get_edge())) {


            double way = a.getPos().distance(a.get_curr_fruit().get_pos());
            double way_to_node = a.getPos().distance(node.getLocation());
            way = way / way_to_node;
            way *= edge.getWeight();
            way /= a.get_speed();
            way *= 1000;
            return (long) way;
        }
        return -120;
    }

    public static void set_ar(Arena _ar) {
        Algo._ar = _ar;
    }

    public static void set_graph(DirectedWeightedGraph _graph) {
        Algo._graph = _graph;
    }
}
