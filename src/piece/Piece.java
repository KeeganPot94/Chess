package piece;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Board;
import main.GamePanel;

public class Piece {

    public BufferedImage image;
    public int x, y;
    public int col, row, sourceCol, sourceRow;
    public int color;
    public Piece occupiedTile;
    public boolean moved;

    public Piece(int color, int col, int row) {

        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        sourceCol = col;
        sourceRow = row;
    }

    public BufferedImage getImage(String imagePath) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getX(int col) {
        return col * Board.TILE_SIZE;
    }

    public int getY(int row) {
        return row * Board.TILE_SIZE;
    }

    public int getCol(int x) {
        return (x + Board.HALF_TILE_SIZE) / Board.TILE_SIZE;
    }

    public int getRow(int y) {
        return (y + Board.HALF_TILE_SIZE) / Board.TILE_SIZE;
    }

    public int getIndex() {
        for (int index = 0; index < GamePanel.simPieces.size(); index++) {
            if (GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    // center the piece to tile after mouse button released
    public void updatePosition() {

        x = getX(col);
        y = getY(row);
        sourceCol = getCol(x);
        sourceRow = getRow(y);
        moved = true;
    }

    // reset pieces position if placed outside movement range
    public void resetPosition() {
        col = sourceCol;
        row = sourceRow;
        x = getX(col);
        y = getY(row);
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }

    public boolean withinBoard(int targetCol, int targetRow) {
        if (targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSameTile(int targetCol, int targetRow) {
        if (targetCol == sourceCol && targetRow == sourceRow) {
            return true;
        }

        return false;
    }

    // check if tile is occupied by another piece
    public Piece getOccupiedTile(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simPieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public boolean validTile(int targetCol, int targetRow) {
        occupiedTile = getOccupiedTile(targetCol, targetRow);

        if (occupiedTile == null) { // tile is vacant
            return true;
        } else { // tile is occupised
            if (occupiedTile.color != this.color) { // capture piece if colors are different
                return true;
            } else {
                occupiedTile = null;
            }
        }

        return false;
    }

    // check if other pieces obstruct one another in a straight line
    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {

        // moving left
        for (int c = sourceCol - 1; c > targetCol; c--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    occupiedTile = piece;
                    return true;
                }
            }
        }

        // moving right
        for (int c = sourceCol + 1; c < targetCol; c++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    occupiedTile = piece;
                    return true;
                }
            }
        }

        // moving up
        for (int r = sourceRow - 1; r > targetRow; r--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == r && piece.col == targetCol) {
                    occupiedTile = piece;
                    return true;
                }
            }
        }

        // moving down
        for (int r = sourceRow + 1; r < targetRow; r++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == r && piece.col == targetCol) {
                    occupiedTile = piece;
                    return true;
                }
            }
        }

        return false;
    }

    // check if other pieces obstruct one another in a diagonal line
    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow) {

        if (targetRow < sourceRow) {
            // up left
            for (int c = sourceCol - 1; c > targetCol; c--) {
                int difference = Math.abs(c - sourceCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == sourceRow - difference) {
                        occupiedTile = piece;
                        return true;
                    }
                }
            }

            // up right
            for (int c = sourceCol + 1; c < targetCol; c++) {
                int difference = Math.abs(c - sourceCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == sourceRow - difference) {
                        occupiedTile = piece;
                        return true;
                    }
                }
            }
        }

        if (targetCol < sourceCol) {
            // down left
            for (int c = sourceCol - 1; c > targetCol; c--) {
                int difference = Math.abs(c - sourceCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == sourceRow + difference) {
                        occupiedTile = piece;
                        return true;
                    }
                }
            }

            // down right
            for (int c = sourceCol + 1; c < targetCol; c++) {
                int difference = Math.abs(c - sourceCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == sourceRow + difference) {
                        occupiedTile = piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.TILE_SIZE, Board.TILE_SIZE, null);
    }

}
