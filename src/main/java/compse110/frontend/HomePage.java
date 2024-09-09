package compse110.frontend;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("TrainWeatherInsight");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Set the frame visibility to true
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Create a label
        JLabel label = new JLabel("Welcome to TrainWeatherInsight!");
        label.setBounds(10, 20, 300, 25);
        panel.add(label);

        // Create a button
        JButton button = new JButton("Click Me");
        button.setBounds(10, 80, 150, 25);
        panel.add(button);

        // Add action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Hello, TrainWeatherInsight!");
            }
        });
    }
}
