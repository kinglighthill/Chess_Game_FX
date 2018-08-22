package sample;

import java.util.ArrayList;

/**
 * Created by KCA on 12/11/2017.
 */
public abstract class Piece {
    private int color;
    private int x_position;
    private int y_position;

    private boolean moved = false;

    private String id;
    private String path;
    private String orientation;

    protected Cell[][] cell = Board.getInstance().getCells();

    protected Piece dummyPiece = null;

    protected abstract String getName();

    protected ArrayList<Cell> possible_moves = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getX_position() {
        return x_position;
    }

    public void setX_position(int x_position) {
        this.x_position = x_position;
    }

    public int getY_position() {
        return y_position;
    }

    public void setY_position(int y_position) {
        this.y_position = y_position;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public ArrayList<Cell> getPossible_moves() {
        return possible_moves;
    }

    public Cell getCell() {
        return cell[getX_position()][getY_position()];
    }

    public void checkKing(){
        for (Cell c : possible_moves) {
            if (c.getPiece() != null && c.getPiece().getName() == "King") {
                King king = (King) c.getPiece();
                king.setOn_check(true);
            }
        }
    }

    public void move() {
        King king;
        if (this.getColor() == 0) {
            king = Board.blackKing;
        }
        else {
            king = Board.whiteKing;
        }

        // filters possible moves when King is in check
        if (king.isOn_check()) {
            Cell[] tempCell = new Cell[possible_moves.size()];
            tempCell = possible_moves.toArray(tempCell);
            possible_moves.clear();
            for (Cell c : tempCell) {
                Piece piece = Board.addDummyPiece(dummyPiece, c.getX_position(), c.getY_position());
                king.isChecked(king.getX_position(), king.getY_position());
                if (!king.isChecked()) {
                    possible_moves.add(c);
                }
                Board.removeDummyPiece(piece, c.getX_position(), c.getY_position());
            }
        }
        // filters moves that will put King in danger
        else {
            Piece tempPiece = cell[x_position][y_position].getPiece();
            cell[x_position][y_position].setPiece(null);
            king.isChecked(king.getX_position(), king.getY_position());
            if (king.isChecked()) {
                possible_moves.clear();
            }
            cell[x_position][y_position].setPiece(tempPiece);
        }
    }
}
