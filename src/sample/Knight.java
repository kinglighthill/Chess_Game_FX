package sample;

/**
 * Created by KCA on 12/11/2017.
 */

public class Knight extends Piece{

    public String getName() {
        return "Knight";
    }

    @Override
    public void move() {
        int x = getX_position();
        int y = getY_position();

        possible_moves.clear();

        int temp_x[] = {(x + 2), (x + 2), (x + 1), (x + 1), (x - 2), (x - 2), (x - 1), (x - 1)};
        int temp_y[] = {(y + 1), (y - 1), (y + 2), (y - 2), (y + 1), (y - 1), (y + 2), (y - 2)};

        for (int i=0; i<8; i++){
            if(temp_x[i] < 8 && temp_y[i] < 8 && temp_x[i] >= 0 && temp_y[i] >= 0){
                if (cell[temp_x[i]][temp_y[i]].getPiece() == null ||
                        cell[temp_x[i]][temp_y[i]].getPiece().getColor() != this.getColor()) {
                    possible_moves.add(cell[temp_x[i]][temp_y[i]]);
                }
            }
        }

        this.dummyPiece = new Knight();
        this.dummyPiece.setColor(this.getColor());

        super.move();
    }

    @Override
    public void checkKing() {
        move();
        super.checkKing();
    }
}
