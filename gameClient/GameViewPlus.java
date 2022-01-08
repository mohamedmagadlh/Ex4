package gameClient;

import api.GeoLocation;
import gameClient.util.Point3D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameViewPlus extends GameView {

    private BufferedImage[] _image_agents;
    private BufferedImage[] _image_fruits;
    static Clip clip;

    public GameViewPlus() {
        super();
        loadImg();
        sound();
    }
    private void loadImg() {
        try {
            _image_agents = new BufferedImage[4];
            for (int i = 0; i < 4; i++) {
                _image_agents[i] = ImageIO.read(new File("img/agent" + (i + 1) + ".png"));
            }

            _image_fruits = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                _image_fruits[i] = ImageIO.read(new File("img/fruit" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void pokIcon(Graphics g, int radius, GeoLocation fp, int flag) {
        g.drawImage(_image_fruits[flag],
                (int) fp.x() - radius, (int) fp.y() - radius, 3 * radius, 3 * radius, null);
    }
    @Override
    protected void agentIcon(Graphics g, int r, GeoLocation fp, int id) {
        g.drawImage(_image_agents[(id % 4)],
                (int) fp.x() - r, (int) fp.y() - r - 1, 4 * r, 4 * r, null);
    }

    @Override
    protected void drawPokemons(Graphics g, Arena ar, Range2Range _w2f) {
        List<Pokemon> fs = new ArrayList<>(ar.getPokemons());
        int flag = 2;
        if (fs.isEmpty()) {
            return;
        }
        for (Pokemon f : fs) {
            if (f == null) continue;
            Point3D c = f.get_pos();
            int radius = 10;
            if (f.get_type() < 0) {
                flag = 1;
            }
            if (f.get_value() > 10) {
                flag = 0;
            }
            if (c != null) {
                GeoLocation fp = _w2f.world2frame(c);
                pokIcon(g, radius, fp, flag);
                g.setColor(Color.BLACK);
                g.setFont(new Font(null, Font.BOLD, 12));
                g.drawString("" + (int) f.get_value(), (int) fp.x(), (int) fp.y() + 2);
            }
        }
    }

    public void sound() {
        try {
            clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("songs/pokemonSong.wav"));
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
    public static void soundOff(){
        clip.stop();
    }
    public static void soundOn(){
        clip.start();
    }
}
