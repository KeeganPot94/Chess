package main;

import java.awt.*;

public class Board {

    // chess board properties
    final int MAX_COL = 8;
    final int MAX_ROW = MAX_COL;
    public static final int TILE_SIZE = 100;
    public static final int HALF_TILE_SIZE = TILE_SIZE / 2;

    public void draw(Graphics2D g2) {

        int c = 0; // used to alternate tile colors

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {

                if (c == 0) {
                    g2.setColor(new Color(210, 165, 125));
                    c = 1;
                } else {
                    g2.setColor(new Color(175, 115, 70));
                    c = 0;
                }
                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            if (c == 0) {
                c = 1;
            } else {
                c = 0;
            }
        }
    }
}
