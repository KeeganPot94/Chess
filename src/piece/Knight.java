package piece;

import main.GamePanel;
import main.Type;

public class Knight extends Piece {

    public Knight(int color, int col, int row) {
        super(color, col, row);

        type = Type.KNIGHT;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white_knight");
        } else {
            image = getImage("/piece/black_knight");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (withinBoard(targetCol, targetRow) && isSameTile(targetCol, targetRow) == false) {

            // piece movement range
            if (Math.abs(targetCol - sourceCol) * Math.abs(targetRow - sourceRow) == 2) {
                if (validTile(targetCol, targetRow)) {
                    return true;
                }
            }
        }

        return false;
    }
}
