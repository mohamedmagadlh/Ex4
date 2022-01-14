package gameClient;

import api.*;
import gameClient.util.point;

import java.util.*;

import static gameClient.util.point.EPS;

public class Game
{
    public ArrayList<Pokemon> pokemons;
    public HashMap<Integer, Agent> agents;
    private DirectedWeightedGraphAlgorithms graphalgo;
    public Client client;
    private boolean finish;
    public Algo algo;
    public  GeoLocation pos;

    public Game(Client client) {
        this.graphalgo = null;
        this.agents = new HashMap<>();
        this.pokemons = new ArrayList<>();
        this.client = client;
        finish = false;

}

    public void updateAgents() {
        HashMap<Integer, Agent> newAgents = Agent.load(client.getAgents());
        for (Agent agent : newAgents.values()) {
            this.agents.get(agent.getId()).set_pos(agent.getPos());
            this.agents.get(agent.getId()).set_dest(agent.get_dest());
            this.agents.get(agent.getId()).set_src(agent.get_src());
            this.agents.get(agent.getId()).set_Speed(agent.get_speed());
        }
    }

    public void updatePokemons() {
        List<Pokemon> ps = Pokemon.load(client.getPokemons());
        if (ps == null)
            return;
        this.pokemons = (ArrayList<Pokemon>) ps;
        for (Pokemon p : pokemons) {
            p.set_edge((impEdgeData) this.graphalgo.getGraph());
        }
        this.algo.setPokemons(pokemons);
    }
    public impEdgeData findEdge(GeoLocation location , int type) {
        for (Iterator<NodeData> it = graphalgo.getGraph().nodeIter(); it.hasNext(); ) {
            NodeData v = it.next();
            for (Iterator<EdgeData> iter = graphalgo.getGraph().edgeIter(); iter.hasNext(); ) {
                EdgeData e = iter.next();
                boolean flag;
                Iterator<EdgeData> edgesIter = graphalgo.getGraph().edgeIter();
                int src = e.getSrc();
                int dest = e.getDest();
                if (type < 0 && dest > src) {
                    flag = false;
                }
                if (type > 0 && src > dest) {
                    flag = false ;
                }
                GeoLocation src_loc = graphalgo.getGraph().getNode(src).getLocation();
                GeoLocation dest_loc = graphalgo.getGraph().getNode(dest).getLocation();
                double dist = src_loc.distance(dest_loc);
                double d1 = src_loc.distance((get_pos())) + get_pos().distance(dest_loc);
                flag = (dist > d1 - EPS);

            }
        }
        return null;
    }
    public Agent currAgent(Pokemon pokemon)
    {
        Agent curr = null;
        float minTime = Float.MAX_VALUE;

        for (Agent agent: agents.values())

        {   float agentime = agent.get_time();
            int src = agent.get_src();
            int dest = pokemon.get_edge().getSrc();
            double pweight = pokemon.get_edge().getWeight();
            double agentspeed=agent.get_speed();
            double weight = graphalgo.shortestPathDist(src,dest) + pweight;
            float time = (float) (weight / agentspeed);
            if(time < agentime) {
                if (time < minTime) {
                    minTime = time;
                    curr = agent;
                }
            }
        }
        if(curr != null)curr.set_time(minTime);
        return curr;
    }
    public void next()
    {

        for (Pokemon pokemon: pokemons)
        {
            Agent agent = currAgent(pokemon);
            if(agent == null)
                continue;

            if(agent.get_src() == pokemon.get_edge().getSrc())
            {
                agent.set_dest(pokemon.get_edge().getDest());
                continue;
            }

            List<NodeData> tempPath = graphalgo.shortestPath(agent.get_src(), pokemon.get_edge().getSrc());
            agent.set_dest(tempPath.get(1).getKey());
        }
    }


    public DirectedWeightedGraphAlgorithms getAlgo() {
        return graphalgo;
    }
    public point get_pos() {
        return (point) pos;
    }
    public boolean Finished() {
        return finish;
    }

    public void Finish(boolean finish) {
        this.finish = finish;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public HashMap<Integer, Agent> getAgents() {
        return agents;
    }

    public void setGraph(String json) {
        this.graphalgo = new DWGAlgo(json);
    }

    public Client getClient() {
        return client;
    }
}
