package sample;

/**
 * Created by KCA on 12/11/2017.
 */

public class Bishop extends Piece {

    public String getName() {
        return "Bishop";
    }

    @Override
    public void move() {
        int x = getX_position();
        int y = getY_position();

        possible_moves.clear();

        int temp_x = x + 1;
        int temp_y = y + 1;
        while ( temp_x < 8  && temp_y < 8) {
            if(cell[temp_x][temp_y].getPiece() == null) {
                possible_moves.add(cell[temp_x][temp_y]);
            }
            else if (cell[temp_x][temp_y].getPiece().getColor() != this.getColor()){
                possible_moves.add(cell[temp_x][temp_y]);
                break;
            }
            else {
                break;
            }
            temp_x++;
            temp_y++;
        }

        temp_x = x + 1;
        temp_y = y - 1;
        while ( temp_x < 8  && temp_y >= 0) {
            if(cell[temp_x][temp_y].getPiece() == null) {
                possible_moves.add(cell[temp_x][temp_y]);
            }
            else if (cell[temp_x][temp_y].getPiece().getColor() != this.getColor()) {
                possible_moves.add(cell[temp_x][temp_y]);
                break;
            }
            else {
                break;
            }
            temp_x++;
            temp_y--;
        }

        temp_x = x - 1;
        temp_y = y + 1;
        while ( temp_x >= 0  && temp_y < 8) {
            if(cell[temp_x][temp_y].getPiece() == null) {
                possible_moves.add(cell[temp_x][temp_y]);
            }
            else if (cell[temp_x][temp_y].getPiece().getColor() != this.getColor()) {
                possible_moves.add(cell[temp_x][temp_y]);
                break;
            }
            else {
                break;
            }
            temp_x--;
            temp_y++;
        }

        temp_x = x - 1;
        temp_y = y - 1;
        while (temp_x >= 0  && temp_y >= 0) {
            if(cell[temp_x][temp_y].getPiece() == null) {
                possible_moves.add(cell[temp_x][temp_y]);
            }
            else if (cell[temp_x][temp_y].getPiece().getColor() != this.getColor()) {
                possible_moves.add(cell[temp_x][temp_y]);
                break;
            }
            else {
                break;
            }
            temp_x--;
            temp_y--;
        }

        this.dummyPiece = new Bishop();
        this.dummyPiece.setColor(this.getColor());

        super.move();
    }

    @Override
    public void checkKing() {
        move();
        super.checkKing();
    }
}
