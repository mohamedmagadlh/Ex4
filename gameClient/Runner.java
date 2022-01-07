package GameClient;

import api.DirectedWeightedGraph;
import api.game_service;
import com.google.gson.JsonParser;
import gameClient.Arena;
import gameClient.Client;
import gameClient.GameGUI;

import static gameClient.Algo.*;


public class Runner implements Runnable {

    private GameGUI _win;
    private Arena _ar;
    private DirectedWeightedGraph _graph;
    private Client _game;
    private final String _id;
    private final Thread _painter_thread;


    public Runner( int id) {
        _id = id;
        _painter_thread = new Thread(new Painter());
    }


    @Override
    public void run() {
        _game = new Client();

        if (_id != -1) {
            _game.login(_id);
        }
        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI();

        _game.startGame();
        long sum_time = _game.timeToEnd();
        _ar.set_timeStart(sum_time);
        GameView.set_startT(System.currentTimeMillis());
        _ar.update(_game);

        _painter_thread.start();

        while (_game.isRunning()) {
            long minSleepTime = Integer.MAX_VALUE;
            int next_dest = -1;

            for (Agent a : _ar.getAgents()) {
                _ar.update(_game);
                if (a.get_path().isEmpty()) {
                    createPath(a);
                }
                _ar.update(_game);
                if (!a.isMoving()) {
                    next_dest = nextMove(_game, a);
                }
                long timeToSleep = toSleep(a, next_dest);
                if (timeToSleep < minSleepTime) {
                    minSleepTime = timeToSleep;
                }
            }

            // sleep before move
            try {
                Thread.sleep(minSleepTime);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            _game.move();
        }

        // finish _painter_thread
        try {
            _painter_thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("Client").get("moves").getAsInt();
        System.out.printf("Level: %d\t\tGrade: %d,\tMoves: %d,\tAvg moves per sec: %.3f%n", _scenario_num, _ar.getGrade(), moves, moves / ((double) sum_time / 1000));
    }

    class Painter implements Runnable {
        @Override
        public void run() {
            while (_game.isRunning()) {
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                _win.repaint();
            }
        }
    }


    private void initArena(Client game) {
        _ar = new Arena(_game);
        _graph = _ar.get_graph();
        initAlgo();
        int num_of_agents = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("Client").get("agents").getAsInt();
        placeAgents(num_of_agents, game);
        _ar.updatePokemons(game.getPokemons());
        _ar.updateAgents(game.getAgents());
    }

    private void initGUI() {
        _win.set_ar(_ar);
        _win.set_level(_scenario_num);
        _win.setTitle("Pokemons Game " + _scenario_num + (_id != -1 ? (" - " + _id) : ""));
    }

    private void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
    }

    public Client get_game() {
        return _game;
    }

    public void set_win(GameGUI win) {
        _win = win;
    }
}
