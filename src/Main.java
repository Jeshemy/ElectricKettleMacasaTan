import java.util.Scanner;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        ElectricKettle kettle1 = new ElectricKettle();
        String[] menu = {"ADD", "REMOVE", "TURN ON", "TURN OFF", "EXIT"};        String choice = "",hold="";
        int amount = 0;
        JOptionPane j = new JOptionPane();

        do{
            hold = "Power: " + kettle1.getPowerStatus() + "\t\t" +
                    "Temperature: " + kettle1.getTemperature() + " °C\n" +
                    "Capacity: " + kettle1.getCapacity() + " mL\t" +
                    "Level: " + kettle1.getLevel() + " mL\n" +
                    "Full: " + kettle1.isFull() + "\t\t" +
                    "Empty: " + kettle1.isEmpty() + "\n\n" +
                    "CHOOSE:";

            choice=j.showInputDialog(null,new JTextArea(hold),
                    "Menu",1,null,menu,menu[0]).toString();

            switch (choice) {

                case "ADD":
                    if (kettle1.isFull()) {
                        JOptionPane.showMessageDialog(null, "Kettle is full!");
                    } else {
                        amount = Integer.parseInt(
                                JOptionPane.showInputDialog("Amount to add (mL):")
                        );
                        kettle1.addWater(amount);
                    }
                    break;

                case "REMOVE":
                    if (kettle1.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Kettle is empty!");
                    } else {
                        amount = Integer.parseInt(
                                JOptionPane.showInputDialog("Amount to remove (mL):")
                        );
                        kettle1.removeWater(amount);
                    }
                    break;

                case "TURN ON":
                    if (kettle1.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "Cannot turn on. Kettle has no water!");
                    } else {
                        kettle1.turnOn();
                        JOptionPane.showMessageDialog(null, "Kettle is now ON");
                    }
                    break;

                case "TURN OFF":
                    kettle1.turnOff();
                    JOptionPane.showMessageDialog(null, "Kettle is now OFF");
                    break;
                }
            } while(!choice.equals("EXIT"));
        System.exit(0);
        }
    }