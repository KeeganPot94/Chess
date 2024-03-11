package main;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // add GamePanel to frame
        GamePanel gp = new GamePanel();
        frame.add(gp);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gp.launchGame();

    }
}
