package piece;

import main.GamePanel;

public class Rook extends Piece {

    public Rook(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white_rook");
        } else {
            image = getImage("/piece/black_rook");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (validTile(targetCol, targetRow)) {

            // piece movement range
            if (validTile(targetCol, targetRow) && isSameTile(targetCol, targetRow) == false) {
                if (targetCol == sourceCol || targetRow == sourceRow) {
                    if (validTile(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
