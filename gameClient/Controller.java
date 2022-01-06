package gameClient;

import api.game_service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends WindowAdapter implements ActionListener {

    public Runner _run;
    private Thread _thread;
    private GameGUI _win;
    private int _id, _level;


    public Controller(Runner run, Thread thread, int id, int level) {
        _run = run;
        _thread = thread;
        _id = id;
        _level = level;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("done");
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals("mute")) {
            if (gameClient.Panel.changeMuteIcon() == 1) {
                gameClient.GameViewPlus.soundOff();
            } else {
                gameClient.GameViewPlus.soundOn();
            }
        } else {
            int id = -1;
            if (str.equals("Submit")) {
                try {
                    _level = Integer.parseInt(Panel.getLevel());
                } catch (NumberFormatException ignored) {
                    return;
                }
                try {
                    id = Integer.parseInt(Panel.getId());
                } catch (NumberFormatException ignored) {
                }

            } else {
                String[] strA = str.split("\\D+");
                _level = Integer.parseInt(strA[1]);
            }

            game_service game = _run.get_game();
            if (game.isRunning()) {
                System.out.print("Game stopped:\t");
                game.stopGame();
            }
            try {
                _thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            _run = new Runner(_level, id);
            _run.set_win(_win);
            _thread = new Thread(_run);
            _thread.start();
        }
    }

    public void set_win(GameGUI win) {
        _win = win;
    }
}

