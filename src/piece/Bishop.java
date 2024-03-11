package piece;

import main.GamePanel;

public class Bishop extends Piece {

    public Bishop(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white_bishop");
        } else {
            image = getImage("/piece/black_bishop");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (withinBoard(targetCol, targetRow) && isSameTile(targetCol, targetRow) == false) {

            // piece movement range
            if (Math.abs(targetCol - sourceCol) == Math.abs(targetRow - sourceRow)) {
                if (validTile(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
                    return true;
                }
            }
        }

        return false;
    }
}
