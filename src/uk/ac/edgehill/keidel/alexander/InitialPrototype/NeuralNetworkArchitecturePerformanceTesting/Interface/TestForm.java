package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import javax.swing.*;

/**
 * Created by Alexander Keidel, 22397868 on 12/02/2017.
 */
public class TestForm {
    private JPanel MainPanel;
    private JLabel MainLabel;
    private JTextPane NNInfoTextPane;
    private JPanel NNTestPane;
    private JButton StartTestButton;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TestForm");
        frame.setContentPane(new TestForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
