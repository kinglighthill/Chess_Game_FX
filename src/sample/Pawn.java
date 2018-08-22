package sample;


/**
 * Created by KCA on 12/11/2017.
 */

public class Pawn extends Piece{

    private boolean en_passant = false;

    public String getName() {
        return "Pawn";
    }

    public boolean isEn_passant() {
        return en_passant;
    }

    public void setEn_passant(boolean en_passant) {
        this.en_passant = en_passant;
    }

    @Override
    public void move() {
        int x = getX_position();
        int y = getY_position();
        boolean check = false;

        possible_moves.clear();

        if (getOrientation() == "down") {
            if (x == 7) {
                possible_moves.clear();
                check = true;
            }

            if (!check) {
                if(cell[x+1][y].getPiece() == null)	{
                    possible_moves.add(cell[x+1][y]);
                    if (!isMoved() && getOrientation() == "down") {
                        if(cell[x+2][y].getPiece() == null) {
                            possible_moves.add(cell[x+2][y]);
                        }
                    }
                }

                if(x == 4) {
                    if (y < 7 && cell[x][y+1].getPiece() != null && cell[x][y+1].getPiece().getName() == "Pawn" &&
                            ((Pawn)cell[x][y+1].getPiece()).isEn_passant()) {
                        cell[x+1][y+1].setEnPassant(true);
                        cell[x+1][y+1].setEnPassantCell(cell[x][y+1]);
                        possible_moves.add(cell[x+1][y+1]);
                        Board.getInstance().setEnPassantCell(cell[x][y+1]);
                    }
                    if (y > 0 && cell[x][y-1].getPiece() != null && cell[x][y-1].getPiece().getName() == "Pawn" &&
                            ((Pawn)cell[x][y-1].getPiece()).isEn_passant()) {
                        cell[x+1][y-1].setEnPassant(true);
                        cell[x+1][y-1].setEnPassantCell(cell[x][y-1]);
                        possible_moves.add(cell[x+1][y-1]);
                        Board.getInstance().setEnPassantCell(cell[x][y-1]);
                    }
                }

                if((y > 0) && (cell[x+1][y-1].getPiece()!=null) && (cell[x+1][y-1].getPiece().getColor()!=
                        this.getColor())) {
                    possible_moves.add(cell[x+1][y-1]);
                }

                if((y < 7) && ( cell[x+1][y+1].getPiece()!=null) && (cell[x+1][y+1].getPiece().getColor()!=
                        this.getColor())) {
                    possible_moves.add(cell[x+1][y+1]);
                }
            }
        }

        else {
            if(x == 0) {
                possible_moves.clear();
                check = true;
            }

            if(!check){
                if(cell[x-1][y].getPiece() == null) {
                    possible_moves.add(cell[x-1][y]);
                    if(!isMoved() && getOrientation() == "up") {
                        if(cell[x-2][y].getPiece() == null) {
                            possible_moves.add(cell[x-2][y]);
                        }
                    }
                }

                if(x == 3) {
                    if (y < 7 && cell[x][y+1].getPiece() != null && cell[x][y+1].getPiece().getName() == "Pawn" &&
                            ((Pawn)cell[x][y+1].getPiece()).isEn_passant()) {
                        cell[x-1][y+1].setEnPassant(true);
                        cell[x-1][y+1].setEnPassantCell(cell[x][y+1]);
                        possible_moves.add(cell[x-1][y+1]);
                        Board.getInstance().setEnPassantCell(cell[x][y+1]);
                    }
                    if (y > 0 && cell[x][y-1].getPiece() != null && cell[x][y-1].getPiece().getName() == "Pawn" &&
                            ((Pawn)cell[x][y-1].getPiece()).isEn_passant()) {
                        cell[x-1][y-1].setEnPassant(true);
                        cell[x-1][y-1].setEnPassantCell(cell[x][y-1]);
                        possible_moves.add(cell[x-1][y-1]);
                        Board.getInstance().setEnPassantCell(cell[x][y-1]);
                    }
                }

                if((y > 0) && (cell[x-1][y-1].getPiece() != null) && (cell[x-1][y-1].getPiece().getColor() !=
                        this.getColor())) {
                    possible_moves.add(cell[x-1][y-1]);
                }

                if((y < 7) && (cell[x-1][y+1].getPiece() != null) && (cell[x-1][y+1].getPiece().getColor() !=
                        this.getColor())) {
                    possible_moves.add(cell[x-1][y+1]);
                }
            }
        }

        this.dummyPiece = new Pawn();
        this.dummyPiece.setColor(this.getColor());
        this.dummyPiece.setOrientation(this.getOrientation());

        super.move();
    }


    @Override
    public void checkKing() {
        move();
        super.checkKing();
    }
}
