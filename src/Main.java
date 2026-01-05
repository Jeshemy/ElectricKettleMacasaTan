import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ElectricKettleUI::new);
    }
}

class ElectricKettleUI extends JFrame {

    private final ElectricKettle kettle = new ElectricKettle();

    private JTextField txtPower, txtTemp, txtWater, txtEmpty, txtFull, inputWater;
    private JLabel imageLabel;
    private ImageIcon imgDefault, imgOn, imgOff, imgBoil;
    private Timer heatTimer;

    private final Font appFont = new Font("Sitka Display", Font.BOLD, 14);
    private final Color bgColor = new Color(0xFEFAE0);
    private final Color textColor = new Color(0x283618);
    private final Color btnColor = new Color(0xDDA15E);

    public ElectricKettleUI() {
        setTitle("Electric Kettle");
        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));
        root.setBackground(bgColor);
        add(root);

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(350, 500));
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        imagePanel.setBackground(bgColor);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        imgDefault = scaleImage(new ImageIcon("images/default.png"), 300, 300);
        imgOn = new ImageIcon("images/on.gif");
        imgOff = scaleImage(new ImageIcon("images/off.png"), 300, 300);
        imgBoil = new ImageIcon("images/boil.gif");

        imageLabel = new JLabel(imgDefault);
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        imagePanel.add(Box.createVerticalGlue());
        imagePanel.add(imageLabel);
        imagePanel.add(Box.createVerticalGlue());

        root.add(imagePanel);

        JPanel uiPanel = new JPanel();
        uiPanel.setPreferredSize(new Dimension(350, 500));
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));
        uiPanel.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 30));
        uiPanel.setBackground(bgColor);

        root.add(uiPanel);

        uiPanel.add(Box.createVerticalGlue());

        txtPower = createStatusField("POWER : OFF");
        txtTemp = createStatusField("TEMPERATURE : 25 °C");
        txtWater = createStatusField("WATER LEVEL : 0 mL");
        txtEmpty = createStatusField("EMPTY : true");
        txtFull = createStatusField("FULL : false");

        uiPanel.add(txtPower);
        uiPanel.add(Box.createVerticalStrut(6));
        uiPanel.add(txtTemp);
        uiPanel.add(Box.createVerticalStrut(6));
        uiPanel.add(txtWater);
        uiPanel.add(Box.createVerticalStrut(6));
        uiPanel.add(txtEmpty);
        uiPanel.add(Box.createVerticalStrut(6));
        uiPanel.add(txtFull);
        uiPanel.add(Box.createVerticalStrut(24));

        JLabel lblInput = new JLabel("ENTER WATER AMOUNT (mL)");
        lblInput.setFont(appFont);
        lblInput.setForeground(textColor);
        lblInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        uiPanel.add(lblInput);
        uiPanel.add(Box.createVerticalStrut(8));

        inputWater = new JTextField();
        inputWater.setFont(appFont);
        inputWater.setForeground(textColor);
        inputWater.setMaximumSize(new Dimension(260, 34));
        inputWater.setAlignmentX(Component.LEFT_ALIGNMENT);

        uiPanel.add(inputWater);
        uiPanel.add(Box.createVerticalStrut(28));

        RoundedButton btnAdd = new RoundedButton("ADD");
        RoundedButton btnRemove = new RoundedButton("REMOVE");
        RoundedButton btnOn = new RoundedButton(" ON ");
        RoundedButton btnOff = new RoundedButton(" OFF ");

        btnAdd.setPreferredSize(new Dimension(90, 36));
        btnRemove.setPreferredSize(new Dimension(90, 36));

        btnOn.setPreferredSize(new Dimension(90, 36));
        btnOff.setPreferredSize(new Dimension(90, 36));


        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(bgColor);
        buttonContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        row1.setBackground(bgColor);
        row1.add(btnAdd);
        row1.add(btnRemove);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        row2.setBackground(bgColor);
        row2.add(btnOn);
        row2.add(btnOff);

        buttonContainer.add(row1);
        buttonContainer.add(Box.createVerticalStrut(12));
        buttonContainer.add(row2);

        uiPanel.add(buttonContainer);
        uiPanel.add(Box.createVerticalGlue());

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
                JOptionPane.showMessageDialog(this, "Cannot turn ON. Kettle is empty!");
                return;
            }
            kettle.turnOn();
            imageLabel.setIcon(imgOn);
            startHeating();
            updateStatus();
        });

        btnOff.addActionListener(e -> {
            kettle.turnOff();
            if (heatTimer != null) heatTimer.stop();
            imageLabel.setIcon(imgOff);
            updateStatus();
        });

        setVisible(true);
    }

    private JTextField createStatusField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(null);
        field.setFont(appFont);
        field.setBackground(bgColor);
        field.setForeground(textColor);
        field.setMaximumSize(new Dimension(260, 22));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setHorizontalAlignment(JTextField.LEFT);
        return field;
    }

    private ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private int parseMilliliters() {
        try {
            int ml = Integer.parseInt(inputWater.getText());
            if (ml < 0 || ml > 1700) {
                JOptionPane.showMessageDialog(this, "Enter 0–1700 mL only.");
                return -1;
            }
            return ml;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
            return -1;
        }
    }

    private void startHeating() {
        heatTimer = new Timer(500, e -> {
            kettle.heatWater();
            updateStatus();

            if (kettle.getTemperature() >= 100) {
                ((Timer) e.getSource()).stop();

                imageLabel.setIcon(imgBoil);

                Timer resetTimer = new Timer(2000, evt -> {
                    kettle.turnOff();
                    kettle.removeWater(kettle.getLevel());
                    kettle.temperature = 25;

                    imageLabel.setIcon(imgDefault);
                    updateStatus();

                    ((Timer) evt.getSource()).stop();
                });
                resetTimer.start();

                JOptionPane.showMessageDialog(this,
                        "Water has finished boiling!");
            }
        });
        heatTimer.start();
    }

    private void updateStatus() {
        txtPower.setText("POWER : " + kettle.getPowerStatus());
        txtTemp.setText("TEMPERATURE : " + kettle.getTemperature() + " °C");
        txtWater.setText("WATER LEVEL : " + kettle.getLevel() + " mL");
        txtEmpty.setText("EMPTY : " + kettle.isEmpty());
        txtFull.setText("FULL : " + kettle.isFull());
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFont(appFont);
            setForeground(textColor);
            setBackground(btnColor);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setPreferredSize(new Dimension(120, 38));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}
