package gameClient;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel {

    private Arena _ar;
    private Range2Range _w2f;
    private static long _startT;

    public GameView() {
        super();
        _startT = System.currentTimeMillis();
    }

    public void set_ar(Arena ar) {
        _ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(40, this.getWidth() - 40);
        Range ry = new Range(this.getHeight() - 40, 40);
        Range2D frame = new Range2D(rx, ry);
        DirectedWeightedGraph g = _ar.get_graph();
        _w2f = Arena.w2f(g, frame);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int w = getWidth();
        int h = getHeight();

        Image buffer_image = createImage(w, h);
        Graphics buffer_graphics = buffer_image.getGraphics();
        if (_ar != null) {
            paintComponents(buffer_graphics);
            g.drawImage(buffer_image, 0, 0, null);
        }
    }

    @Override
    public void paintComponents(Graphics g) {
        drawGraph(g);
        drawPokemons(g, _ar, _w2f);
        drawAgents(g);
        drawTimeLine(g);
        updateFrame();
    }

    private void drawGraph(Graphics g) {
        DirectedWeightedGraph graph = _ar.get_graph();
        for (NodeData i : graph.getV()) {
            drawNode(i, g);
            for (EdgeData e : graph.getE(i.getKey())) {
                drawEdge(e, g);
            }
        }
    }

    private void drawNode(NodeData n, Graphics g) {
        int radius = 6;
        GeoLocation pos = n.getLocation();
        GeoLocation fp = _w2f.world2frame(pos);
        nodeIcon(g, radius, fp);
        g.setColor(Color.BLACK);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * radius);
    }

    private void drawEdge(EdgeData e, Graphics g) {

        DirectedWeightedGraph gg = _ar.get_graph();
        GeoLocation s = gg.getNode(e.getSrc()).getLocation();
        GeoLocation d = gg.getNode(e.getDest()).getLocation();
        GeoLocation s0 = _w2f.world2frame(s);
        GeoLocation d0 = _w2f.world2frame(d);

        g.setColor(new Color(0x000099));
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Float((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y()));


        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 13));
        String t = String.format("%.2f", e.getWeight());
        int x = (int) ((s0.x() + d0.x()) / 2);
        int y = (int) ((s0.y() + d0.y()) / 2) - 3;
        if (e.getSrc() < e.getDest()) y += 15;

    }

    protected void nodeIcon(Graphics g, int radius, GeoLocation fp) {
        g.setColor(new Color(0x000099));
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
    }

    protected void drawPokemons(Graphics g, Arena ar, Range2Range _w2f) {
        List<Pokemon> fs = new ArrayList<>(_ar.getPokemons());
        if (fs.isEmpty())
            return;
        for (Pokemon f : fs) {
            if (f == null) continue;
            Point3D c = f.get_pos();
            int radius = 10;
            g.setColor(Color.green);
            if (f.get_type() < 0) {
                g.setColor(Color.orange);
            }
            if (c != null) {
                GeoLocation fp = _w2f.world2frame(c);
                pokIcon(g, radius, fp, 0);
                g.setColor(Color.BLACK);
                g.setFont(new Font(null, Font.BOLD, 12));
                g.drawString("" + (int) f.get_value(), (int) fp.x(), (int) fp.y() + 2);
            }
        }
    }

    protected void pokIcon(Graphics g, int radius, GeoLocation fp, int flag) {
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
    }
    private void drawAgents(Graphics g) {
        List<Agent> rs = _ar.getAgents();
        for (Agent a : rs) {
            GeoLocation loc = a.getPos();
            int r = 8;
            GeoLocation fp = _w2f.world2frame(loc);
            agentIcon(g, r, fp, a.getId());
            String v = (int) a.getValue() + "";
            g.setColor(Color.BLACK);
            g.setFont(new Font(null, Font.BOLD, 12));
            g.drawString(v, (int) fp.x() + 10, (int) fp.y() + 10);
        }
    }

    protected void agentIcon(Graphics g, int r, GeoLocation fp, int id) {
        g.setColor(new Color(150, 60, 90));
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
    }

    private void drawTimeLine(Graphics g) {
        g.setColor(new Color(0xCD1818));
        double ts = (double) _ar.get_timeStart();
        long curT = System.currentTimeMillis()-_startT;
        double dt = curT / ts;

        double w = getWidth();
        g.fillRoundRect(0, 0, (int) (w * dt), 10, 10, 10);
    }
    public static void set_startT(long _startT1) {
    _startT = _startT1;
    }
}
