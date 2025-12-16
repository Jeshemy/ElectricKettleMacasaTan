import javax.swing.*;
import java.awt.Image;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ElectricKettleUI::new);
    }
}

class ElectricKettleUI extends JFrame {

    private final ElectricKettle kettle = new ElectricKettle();

    private JTextField txtPower;
    private JTextField txtTemp;
    private JTextField txtWater;
    private JTextField txtEmpty;
    private JTextField txtFull;
    private JTextField inputWater;

    private JLabel imageLabel;
    private ImageIcon imgDefault;
    private ImageIcon imgOn;
    private ImageIcon imgOff;

    public ElectricKettleUI() {

        setTitle("Electric Kettle");
        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));
        add(root);

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new java.awt.Dimension(350, 500));
        imagePanel.setMaximumSize(new java.awt.Dimension(350, 500));
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        imgDefault = scaleImage(new ImageIcon("images/default.png"), 300, 300);
        imgOn      = new ImageIcon("images/on.gif");
        imgOff     = scaleImage(new ImageIcon("images/off.png"), 300, 300);


        imageLabel = new JLabel(imgDefault);
        imageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        imagePanel.add(Box.createVerticalGlue());
        imagePanel.add(imageLabel);
        imagePanel.add(Box.createVerticalGlue());

        root.add(imagePanel);


        JPanel uiPanel = new JPanel();
        uiPanel.setPreferredSize(new java.awt.Dimension(350, 500));
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));
        uiPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        root.add(uiPanel);

        txtPower = createStatusField("POWER : OFF");
        txtTemp = createStatusField("TEMPERATURE : 25 °C");
        txtWater = createStatusField("WATER LEVEL : 0 mL");
        txtEmpty = createStatusField("EMPTY : true");
        txtFull = createStatusField("FULL : false");

        uiPanel.add(txtPower);
        uiPanel.add(txtTemp);
        uiPanel.add(txtWater);
        uiPanel.add(txtEmpty);
        uiPanel.add(txtFull);

        uiPanel.add(Box.createVerticalStrut(15));


        uiPanel.add(new JLabel("ENTER WATER AMOUNT (mL)"));
        inputWater = new JTextField();
        inputWater.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 25));
        uiPanel.add(inputWater);

        uiPanel.add(Box.createVerticalStrut(20));

        JButton btnAdd = new JButton("ADD");
        JButton btnRemove = new JButton("REMOVE");
        JButton btnOn = new JButton("TURN ON");
        JButton btnOff = new JButton("TURN OFF");

        uiPanel.add(btnAdd);
        uiPanel.add(Box.createVerticalStrut(8));
        uiPanel.add(btnRemove);
        uiPanel.add(Box.createVerticalStrut(15));
        uiPanel.add(btnOn);
        uiPanel.add(Box.createVerticalStrut(8));
        uiPanel.add(btnOff);


        btnAdd.addActionListener(e -> {
            int amount = parseMilliliters();
            if (amount >= 0) {
                kettle.addWater(amount);
                updateStatus();
            }
        });

        btnRemove.addActionListener(e -> {
            int amount = parseMilliliters();
            if (amount >= 0) {
                kettle.removeWater(amount);
                updateStatus();
            }
        });

        btnOn.addActionListener(e -> {
            if (kettle.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Cannot turn ON. Kettle has no water!");
                return;
            }
            kettle.turnOn();
            imageLabel.setIcon(imgOn);
            updateStatus();
            JOptionPane.showMessageDialog(this,
                    "Kettle is now ON");
        });

        btnOff.addActionListener(e -> {
            kettle.turnOff();
            imageLabel.setIcon(imgOff);
            updateStatus();
            JOptionPane.showMessageDialog(this,
                    "Kettle is now OFF");
        });

        imageLabel.setIcon(imgDefault);

        updateStatus();
        setVisible(true);
    }


    private JTextField createStatusField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(null);
        field.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 20));
        return field;
    }

    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private int parseMilliliters() {
        try {
            int ml = Integer.parseInt(inputWater.getText());
            if (ml < 0 || ml > 1700) {
                JOptionPane.showMessageDialog(this,
                        "Water must be between 0 and 1700 mL");
                return -1;
            }
            return ml;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number");
            return -1;
        }
    }

    private void updateStatus() {
        txtPower.setText("POWER : " + kettle.getPowerStatus());
        txtTemp.setText("TEMPERATURE : " + kettle.getTemperature() + " °C");
        txtWater.setText("WATER LEVEL : " + kettle.getLevel() + " mL");
        txtEmpty.setText("EMPTY : " + kettle.isEmpty());
        txtFull.setText("FULL : " + kettle.isFull());
    }
}
