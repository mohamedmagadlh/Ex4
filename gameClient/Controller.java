package gameClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends WindowAdapter implements ActionListener {

    public Runner _run;
    private Thread _thread;
    private int _id;

    public Controller(Runner run, Thread thread, int id) {
        _run = run;
        _thread = thread;
        _id = id;
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
                    id = Integer.parseInt(Panel.getId());
                } catch (NumberFormatException ignored) {
                }

            }
            Client client = _run.get_client();
            if (client.isRunning() == "false") {
                System.out.print("Game stopped:\t");
                client.stop();
            }
            try {
                _thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            _run = new Runner(id);
            _thread = new Thread(_run);
            _thread.start();
        }
    }

}
