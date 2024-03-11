package main;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

import piece.Piece;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    // pieces
    public static ArrayList<Piece> pieces = new ArrayList<>(); // backup for reset purposes
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activePiece;
    public static Piece castlingPiece;

    // color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // movement booleans
    boolean canMove;
    boolean validTile;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse); // detect mouse movement
        addMouseListener(mouse); // detect mouse clicks

        setPieces();
        copyPieces(pieces, simPieces); // copyPieces(source, target)
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); // calls run()
    }

    public void setPieces() {

        // white pieces
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        // pieces.add(new Bishop(WHITE, 5, 7));
        // pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // black pieces
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    public void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {

        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    @Override
    public void run() {

        // game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(); // update values
                repaint(); // repaint graphics
                delta--;
            }
        }
    }

    private void update() {

        // mouse button clicked
        if (mouse.pressed) {
            if (activePiece == null) {
                // if activePiece == null, check if you can pick up piece
                for (Piece piece : simPieces) {
                    // if mouse is over ally piece, pick up piece as activePiece
                    if (piece.color == currentColor && piece.col == mouse.x / Board.TILE_SIZE
                            && piece.row == mouse.y / Board.TILE_SIZE) {
                        activePiece = piece;
                    }
                }
            } else {
                // if player is already holding a piece
                simulate();
            }
        }

        // mouse button released
        if (mouse.pressed == false) {

            if (activePiece != null) {

                if (validTile) {
                    // confirm move
                    copyPieces(simPieces, pieces); // update piece array values with simPiece array values as pieces are
                                                   // captured
                    activePiece.updatePosition(); // center piece
                    if (castlingPiece != null) {
                        castlingPiece.updatePosition();
                    }
                    activePiece = null;

                    changePlayer();
                } else {
                    copyPieces(pieces, simPieces); // reset array values if move in invalid or cancelled
                    activePiece.resetPosition();
                    activePiece = null;
                }
            }
        }
    }

    public void simulate() {

        canMove = false;
        validTile = false;

        // reset piece array each loop
        copyPieces(pieces, simPieces);

        // reset castling piece's positions
        if (castlingPiece != null) {
            castlingPiece.col = castlingPiece.sourceCol;
            castlingPiece.x = castlingPiece.getX(castlingPiece.col);
            castlingPiece = null;
        }

        // update pieces position while being held by mouse
        activePiece.x = mouse.x - Board.HALF_TILE_SIZE;
        activePiece.y = mouse.y - Board.HALF_TILE_SIZE;
        activePiece.col = activePiece.getCol(activePiece.x);
        activePiece.row = activePiece.getRow(activePiece.y);

        // check if piece is hovering over a valid tile
        if (activePiece.canMove(activePiece.col, activePiece.row)) {

            canMove = true;

            if (activePiece.occupiedTile != null) {
                simPieces.remove(activePiece.occupiedTile.getIndex());
            }

            checkCastling();

            validTile = true;
        }
    }

    private void checkCastling() {

        if (castlingPiece != null) {
            if (castlingPiece.col == 0) {
                castlingPiece.col += 3;
            } else if (castlingPiece.col == 7) {
                castlingPiece.col -= 2;
            }

            castlingPiece.x = castlingPiece.getX(castlingPiece.col);
        }
    }

    private void changePlayer() {
        if (currentColor == WHITE) {
            currentColor = BLACK;
        } else {
            currentColor = WHITE;
        }

        activePiece = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // board
        board.draw(g2);

        // pieces
        for (Piece p : simPieces) {
            p.draw(g2);
        }

        if (activePiece != null) {
            if (canMove) {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activePiece.col * Board.TILE_SIZE, activePiece.row * Board.TILE_SIZE, Board.TILE_SIZE,
                        Board.TILE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            activePiece.draw(g2);
        }

        // status message
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Arial", Font.PLAIN, 40));
        g2.setColor(Color.white);

        if (currentColor == WHITE) {
            g2.drawString("White's turn", 840, 550);
        } else {
            g2.drawString("Black's turn", 840, 250);
        }
    }

}