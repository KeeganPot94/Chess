package piece;

import main.GamePanel;

public class King extends Piece {

    public King(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white_king");
        } else {
            image = getImage("/piece/black_king");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (withinBoard(targetCol, targetRow) && isSameTile(targetCol, targetRow) == false) {

            // piece movement range
            if (Math.abs(targetCol - sourceCol) + Math.abs(targetRow - sourceRow) == 1
                    || Math.abs(targetCol - sourceCol) * Math.abs(targetRow - sourceRow) == 1) {

                if (validTile(targetCol, targetRow)) {
                    return true;
                }
            }

            // castling
            if (moved == false) {

                // right castling
                if (targetCol == sourceCol + 2 && targetRow == sourceRow
                        && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.col == sourceCol + 3 && piece.row == sourceRow && piece.moved == false) {
                            GamePanel.castlingPiece = piece;
                            return true;
                        }
                    }
                }

                // left castling
                if (targetCol == sourceCol - 2 && targetRow == sourceRow
                        && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    Piece p[] = new Piece[2];
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.col == sourceCol - 3 && piece.row == targetRow) {
                            p[0] = piece;
                        }
                        if (piece.col == sourceCol - 4 && piece.row == targetRow) {
                            p[1] = piece;
                        }

                        if (p[0] == null && p[1].moved == false) { // BUG
                            GamePanel.castlingPiece = p[1];
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
