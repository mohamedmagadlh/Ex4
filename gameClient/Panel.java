package gameClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class Panel extends JPanel {

    private final JFrame _frame;
    private final Controller _ctrl;
    private Arena _ar;
    private static boolean muteFlag;
    private static JTextField _id_field, _s_n;
    private JButton _submit;
    private static JButton un_mute;
    private JLabel _id_label, _time, _score, _name_img;
    private static ImageIcon[] _image_sound;

    public Panel(JFrame frame, Controller ctrl) {
        super();
        _frame = frame;
        _ctrl = ctrl;
        setBackground(Color.gray);
        setBorder(new BevelBorder(BevelBorder.RAISED));

        sound_button();
        insertBox();
        infoBox();
        nameImg();

        setPreferredSize(new Dimension(_frame.getWidth(), 110));
        setLayout(null);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        updateInsertBox();
        updateInfoBox();
        updateNameImg();
        update_sound_button();
    }
    private void insertBox() {
        _id_label = new JLabel("ID: ");
        _id_field = new JTextField();
        _s_n = new JTextField();
        _submit = new JButton("Submit");
        _submit.addActionListener(_ctrl);
        _id_label.setForeground(Color.white);
        _id_label.setForeground(Color.white);
        add(_submit);
        add(_id_field);
        add(_s_n);
        add(_id_label);
        updateInsertBox();
    }
    private void updateInsertBox() {
        _id_field.setBounds(getWidth() - 150, 20, 120, 22);
        _id_label.setBounds(getWidth() - 185, 20, 50, 22);
        _s_n.setBounds(getWidth() - 150, 45, 120, 22);
        _submit.setBounds(getWidth() - 135, 70, 95, 25);
    }
    private void infoBox() {
        _time = new JLabel("Time to end: ");
        _time.setForeground(Color.white);
        add(_time);
        _score = new JLabel("Score: ");
        _score.setForeground(Color.white);
        add(_score);
        updateInfoBox();
    }
    private void updateInfoBox() {
        if (_ar != null) {
            _time.setText("Time to end: " + (int) _ar.getTime() / 1000);
            _score.setText("Score: " + _ar.getGrade());
        }
        _time.setBounds(20, 20, 150, 30);
        _score.setBounds(20, 60, 150, 30);
    }
    private void nameImg() {
        BufferedImage name = null;
        try {
            name = ImageIO.read(new File("img/name.gif"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        _name_img = new JLabel();
        double size = 0.8;
        _name_img.setIcon(new javax.swing.ImageIcon(name.getScaledInstance((int) (324 * size), (int) (124 * size), WIDTH)));
        add(_name_img);
        updateNameImg();
    }
    private void updateNameImg() {
        _name_img.setBounds(_frame.getWidth() / 2 - 150, 0, 300, 100);
    }
    public void sound_button() {
        _image_sound = new ImageIcon[2];

        _image_sound[0] = (new ImageIcon("img/mute.png"));
        _image_sound[1] = (new ImageIcon("img/unmute.png"));

        Image image = _image_sound[0].getImage();
        Image new_img = image.getScaledInstance(27, 27, java.awt.Image.SCALE_SMOOTH);
        _image_sound[0] = new ImageIcon(new_img);

        image = _image_sound[1].getImage();
        new_img = image.getScaledInstance(27, 27, java.awt.Image.SCALE_SMOOTH);
        _image_sound[1] = new ImageIcon(new_img);

        un_mute = new JButton("mute");
        un_mute.setFont(new Font("mo", Font.PLAIN, 1));
        un_mute.addActionListener(_ctrl);
        un_mute.setIcon(_image_sound[0]);
        muteFlag = true;
        add(un_mute);
        update_sound_button();
    }
    public void update_sound_button() {
        un_mute.setBounds(_frame.getWidth() - 250, 27, 40, 40);
    }
    public static int changeMuteIcon() {
        if (muteFlag) {
            un_mute.setIcon(_image_sound[1]);
            muteFlag = false;
            return 1;
        } else {
            un_mute.setIcon(_image_sound[0]);
            muteFlag = true;
            return 0;
        }
    }
    public void set_ar(Arena ar) {
        _ar = ar;
    }

    public static String getId() {
        return _id_field.getText();
    }

    public JButton get_submit() {
        return _submit;
    }
}
