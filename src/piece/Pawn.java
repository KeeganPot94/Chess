package piece;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece {

    public Pawn(int color, int col, int row) {
        super(color, col, row);

        type = Type.PAWN;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white_pawn");
        } else {
            image = getImage("/piece/black_pawn");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (withinBoard(targetCol, targetRow) && isSameTile(targetCol, targetRow) == false) {

            // piece movement range
            // movement based on piece color
            int moveValue;
            if (color == GamePanel.WHITE) { // if white
                moveValue = -1;
            } else { // if black
                moveValue = 1;
            }

            // check if tile opposed to pawn is occupied
            occupiedTile = getOccupiedTile(targetCol, targetRow);

            // 1 tile movement
            if (targetCol == sourceCol && targetRow == sourceRow + moveValue && occupiedTile == null) {
                return true;
            }

            // 2 tile movement
            if (targetCol == sourceCol && targetRow == sourceRow + moveValue * 2 && occupiedTile == null
                    && moved == false && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                return true;
            }

            // capturing pieces diagonally
            if (Math.abs(targetCol - sourceCol) == 1 && targetRow == sourceRow + moveValue && occupiedTile != null
                    && occupiedTile.color != color) {
                return true;
            }

            // en passant
            if (Math.abs(targetCol - sourceCol) == 1 && targetRow == sourceRow + moveValue) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == targetCol && piece.row == sourceRow && piece.twoStepped == true) {
                        occupiedTile = piece;
                        return true;
                    }
                }
            }

        }

        return false;
    }
}
