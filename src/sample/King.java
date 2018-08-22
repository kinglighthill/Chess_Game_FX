package sample;

/**
 * Created by KCA on 12/11/2017.
 */

public class King extends Piece{

    private boolean is_checked = false;
    private boolean on_check = false;
    private boolean check_mate = false;

    public String getName() {
        return "King";
    }

    @Override
    public void move() {
        int x = getX_position();
        int y = getY_position();

        possible_moves.clear();

        int temp_x[] = {(x + 1), (x + 1), (x + 1), x, x, (x - 1), (x - 1), (x - 1)};
        int temp_y[] = {(y + 1), y, (y - 1), (y + 1), (y - 1), (y + 1), y, (y - 1)};

        for (int i=0; i<8; i++){
            if(temp_x[i] < 8 && temp_y[i] < 8 && temp_x[i] >= 0 && temp_y[i] >= 0){
                Piece piece = cell[x][y].getPiece();
                cell[x][y].setPiece(null);
                isChecked(temp_x[i], temp_y[i]);
                if ((cell[temp_x[i]][temp_y[i]].getPiece() == null ||
                        cell[temp_x[i]][temp_y[i]].getPiece().getColor() != this.getColor()) && !is_checked )
                {
                    possible_moves.add(cell[temp_x[i]][temp_y[i]]);
                }
                cell[x][y].setPiece(piece);
            }
        }
        castling();
    }

    public boolean isOn_check() {
        return on_check;
    }

    public void setOn_check(boolean on_check) {
        this.on_check = on_check;
    }

    public void castling() {
        int x = getX_position();
        int y = getY_position();

        for(int i=y+1; i<8; i++) {
            if(cell[x][i].getPiece() == null) {
                continue;
            }
            else if(cell[x][i].getPiece() instanceof Rook && cell[x][i].getPiece().getColor() == this.getColor()) {
                this.isChecked(x, y+2);
                if(!this.isMoved() && !cell[x][i].getPiece().isMoved() && !is_checked) {
                    possible_moves.add(cell[x][y+2]);
                    cell[x][y+2].setCastle(true);
                    if((this.getColor() == 0 && this.getOrientation() == "up") ||
                            (this.getColor() == 1 && this.getOrientation() == "down") ) {
                        Cell castleCell[] = {cell[x][i], cell[x][i-3]};
                        cell[x][y+2].setCastleCell(castleCell);
                    }
                    else if((this.getColor() == 0 && this.getOrientation() == "down") ||
                            (this.getColor() == 1 && this.getOrientation() == "up") ) {
                        Cell castleCell[] = {cell[x][i], cell[x][i-2]};
                        cell[x][y+2].setCastleCell(castleCell);
                    }
                }
            }
            else {
                break;
            }
        }

        for(int i=y-1; i>=0; i--) {
            if(cell[x][i].getPiece() == null) {
                continue;
            }
            else if(cell[x][i].getPiece() instanceof Rook && cell[x][i].getPiece().getColor() == this.getColor()) {
                this.isChecked(x, y-2);
                if(!this.isMoved() && !cell[x][i].getPiece().isMoved() && !is_checked) {
                    possible_moves.add(cell[x][y-2]);
                    cell[x][y-2].setCastle(true);
                    if((this.getColor() == 0 && this.getOrientation() == "up") ||
                            (this.getColor() == 1 && this.getOrientation() == "down") ) {
                        Cell castleCell[] = {cell[x][i], cell[x][i+2]};
                        cell[x][y-2].setCastleCell(castleCell);
                    }
                    else if((this.getColor() == 0 && this.getOrientation() == "down") ||
                            (this.getColor() == 1 && this.getOrientation() == "up") ) {
                        Cell castleCell[] = {cell[x][i], cell[x][i+3]};
                        cell[x][y-2].setCastleCell(castleCell);
                    }
                }
            }
            else {
                break;
            }
        }
    }

    public void isChecked(int x, int y) {
        is_checked = false;

        for(int i=x+1; i<8; i++) {
            if(cell[i][y].getPiece() == null) {
                continue;
            }
            else {
                if(cell[i][y].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[i][y].getPiece() instanceof Queen || cell[i][y].getPiece() instanceof Rook) {
                    is_checked = true;
                }
                else {
                    break;
                }
            }
        }

        for(int i=x-1; i>=0; i--) {
            if(cell[i][y].getPiece() == null) {
                continue;
            }
            else {
                if(cell[i][y].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[i][y].getPiece() instanceof Queen || cell[i][y].getPiece() instanceof Rook) {
                    is_checked = true;
                }
                else {
                    break;
                }
            }
        }

        for(int i=y+1; i<8; i++) {
            if(cell[x][i].getPiece() == null) {
                continue;
            }
            else {
                if(cell[x][i].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[x][i].getPiece() instanceof Queen || cell[x][i].getPiece() instanceof Rook) {
                    is_checked = true;
                }
                else {
                    break;
                }
            }
        }

        for(int i=y-1; i>=0; i--) {
            if(cell[x][i].getPiece() == null) {
                continue;
            }
            else {
                if(cell[x][i].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[x][i].getPiece() instanceof Queen || cell[x][i].getPiece() instanceof Rook) {
                    is_checked = true;
                }
                else {
                    break;
                }
            }
        }

        int temp_x1 = x + 1;
        int temp_y1 = y + 1;
        while ( temp_x1 < 8  && temp_y1 < 8) {
            if(cell[temp_x1][temp_y1].getPiece() == null) {
                temp_x1++;
                temp_y1++;
            }
            else {
                if(cell[temp_x1][temp_y1].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[temp_x1][temp_y1].getPiece() instanceof Queen ||
                        cell[temp_x1][temp_y1].getPiece() instanceof Bishop) {
                    is_checked = true;
                    break;
                }
                else {
                    break;
                }
            }
        }

        temp_x1 = x + 1;
        temp_y1 = y - 1;
        while ( temp_x1 < 8  && temp_y1 >= 0) {
            if(cell[temp_x1][temp_y1].getPiece() == null) {
                temp_x1++;
                temp_y1--;
            }
            else {
                if(cell[temp_x1][temp_y1].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[temp_x1][temp_y1].getPiece() instanceof Queen ||
                        cell[temp_x1][temp_y1].getPiece() instanceof Bishop) {
                    is_checked = true;
                    break;
                }
                else {
                    break;
                }
            }
        }

        temp_x1 = x - 1;
        temp_y1 = y + 1;
        while ( temp_x1 >= 0  && temp_y1 < 8) {
            if(cell[temp_x1][temp_y1].getPiece() == null) {
                temp_x1--;
                temp_y1++;
            }
            else {
                if(cell[temp_x1][temp_y1].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[temp_x1][temp_y1].getPiece() instanceof Queen ||
                        cell[temp_x1][temp_y1].getPiece() instanceof Bishop) {
                    is_checked = true;
                    break;
                }
                else {
                    break;
                }
            }
        }

        temp_x1 = x - 1;
        temp_y1 = y - 1;
        while ( temp_x1 >= 0  && temp_y1 >= 0) {
            if(cell[temp_x1][temp_y1].getPiece() == null) {
                temp_x1--;
                temp_y1--;
            }
            else {
                if(cell[temp_x1][temp_y1].getPiece().getColor() == this.getColor()) {
                    break;
                }
                else if(cell[temp_x1][temp_y1].getPiece() instanceof Queen ||
                        cell[temp_x1][temp_y1].getPiece() instanceof Bishop) {
                    is_checked = true;
                    break;
                }
                else {
                    break;
                }
            }
        }

        int[] temp_x2 = {(x + 2), (x + 2), (x + 1), (x + 1), (x - 2), (x - 2), (x - 1), (x - 1)};
        int[] temp_y2 = {(y + 1), (y - 1), (y + 2), (y - 2), (y + 1), (y - 1), (y + 2), (y - 2)};

        for (int i=0; i<8; i++){
            if(temp_x2[i] < 8 && temp_y2[i] < 8 && temp_x2[i] >= 0 && temp_y2[i] >= 0){
                if (cell[temp_x2[i]][temp_y2[i]].getPiece() != null &&
                        cell[temp_x2[i]][temp_y2[i]].getPiece().getColor() != this.getColor() &&
                        cell[temp_x2[i]][temp_y2[i]].getPiece() instanceof Knight) {
                    is_checked = true;
                    break;
                }
            }
        }

        if (getOrientation() == "down") {
            if (x < 7 && y < 7 && cell[x+1][y+1].getPiece() != null &&
                    cell[x+1][y+1].getPiece().getOrientation() != "down" &&
                    cell[x+1][y+1].getPiece() instanceof Pawn) {
                possible_moves.remove(cell[x+1][y+1]);
                is_checked = true;
            }
            if (x < 7 && y > 0 && cell[x+1][y-1].getPiece() != null &&
                    cell[x+1][y-1].getPiece().getOrientation() != "down"
                    && cell[x+1][y-1].getPiece() instanceof Pawn) {
                is_checked = true;
            }
        }

        else {
            if (x > 0 && y < 7 && cell[x-1][y+1].getPiece() != null &&
                    cell[x-1][y+1].getPiece().getOrientation() == "down"
                    && cell[x-1][y+1].getPiece() instanceof Pawn) {
                is_checked = true;
            }
            if (x > 0 && y > 0 && cell[x-1][y-1].getPiece() != null &&
                    cell[x-1][y-1].getPiece().getOrientation() == "down"
                    && cell[x-1][y-1].getPiece() instanceof Pawn) {
                is_checked = true;
            }
        }

        int temp_x3[] = {(x + 1), (x + 1), (x + 1), x, x, (x - 1), (x - 1), (x - 1)};
        int temp_y3[] = {(y + 1), y, (y - 1), (y + 1), (y - 1), (y + 1), y, (y - 1)};

        for (int i=0; i<8; i++){
            if(temp_x3[i] < 8 && temp_y3[i] < 8 && temp_x3[i] >= 0 && temp_y3[i] >= 0){
                if (cell[temp_x3[i]][temp_y3[i]].getPiece() != null &&
                        cell[temp_x3[i]][temp_y3[i]].getPiece().getColor() != this.getColor() &&
                        cell[temp_x3[i]][temp_y3[i]].getPiece() instanceof King) {
                    is_checked = true;
                }
            }
        }
    }

    public boolean isChecked() {
        return is_checked;
    }

    public boolean isCheck_mate() {
        return check_mate;
    }

    public void setCheck_mate(boolean check_mate) {
        this.check_mate = check_mate;
    }

    @Override
    public void checkKing() {
        move();
        super.checkKing();
    }
}
