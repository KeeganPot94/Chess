package piece;

import main.GamePanel;
import main.Type;

public class Queen extends Piece {

    public Queen(int color, int col, int row) {
        super(color, col, row);

        type = Type.QUEEN;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white_queen");
        } else {
            image = getImage("/piece/black_queen");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (withinBoard(targetCol, targetRow) && isSameTile(targetCol, targetRow) == false) {

            // piece movement range
            // vertically & horizontally
            if (targetCol == sourceCol || targetRow == sourceRow) {
                if (validTile(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    return true;
                }
            }

            // diagonally
            if (Math.abs(targetCol - sourceCol) == Math.abs(targetRow - sourceRow)) {
                if (validTile(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
                    return true;
                }
            }
        }

        return false;
    }
}
