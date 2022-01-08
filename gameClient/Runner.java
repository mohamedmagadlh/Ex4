package gameClient;
import api.*;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.UnknownHostException;

import static gameClient.Algo.*;


public class Runner implements Runnable {
//UPDATE,
    private Arena _ar;
    private DirectedWeightedGraph _graph;
    private Client client;
    private final int  _id;
    public final Thread _painter_thread;

    public Runner (int id) {
        _id = id;
        _painter_thread = new Thread(new Painter());
    }

    @Override
    public void run() {
        client =new Client();
        try {
            client.startConnection("127.0.0.1",6666);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (_id != -1) {
            client.login(""+_id);
        }
        System.out.println("Game Info: " + client);

        initArena(client);
        client.start();
        String sum_time = client.timeToEnd();
        _ar.set_timeStart(Long.parseLong(sum_time));
        GameView.set_startT(System.currentTimeMillis());
        _ar.update(client);
        _painter_thread.start();

        while (client.isRunning()=="isRunning") {
            long minSleepTime = Integer.MAX_VALUE;
            int next_dest = -1;

            for (Agent a : _ar.getAgents()) {
                _ar.update(client);
                if (a.get_path().isEmpty()) {
                    createPath(a);
                }
                _ar.update(client);
                if (!a.isMoving()) {
                    next_dest = Algo.nextMove(client, a);
                    if(next_dest!=-1)client.move();
                }
                long timeToSleep = toSleep(a, next_dest);
                if (timeToSleep < minSleepTime) {
                    minSleepTime = timeToSleep;
                }
            }
            try {
                Thread.sleep(minSleepTime);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            client.move();
        }

        try {
            _painter_thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        int moves = JsonParser.parseString(client.toString()).getAsJsonObject().getAsJsonObject("Client").get("moves").getAsInt();
        double x=Double.parseDouble(sum_time);
        double res=(moves /(x/1000));
        System.out.printf("Grade: %d,\tMoves: %d,\tAvg moves per sec: %.3f%n", _ar.getGrade(), moves,res );
    }
    class Painter implements Runnable {
        @Override
        public void run() {
            while (client.isRunning()=="isRunning") {
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void initArena(Client client) {
        _ar = new Arena(client);
        _graph = _ar.get_graph();
        initAlgo();
        int num_of_agents = JsonParser.parseString(client.toString()).getAsJsonObject().getAsJsonObject("Client").get("agents").getAsInt();
        placeAgents(num_of_agents, client);
        _ar.updatePokemons(client.getPokemons());
        _ar.updateAgents(client.getAgents());
    }
    private void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
    }
    public Client get_client() {
        return client;
    }

}

