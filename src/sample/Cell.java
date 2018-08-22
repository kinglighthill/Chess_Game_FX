package sample;


import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

/**
 * Created by KCA on 12/11/2017.
 */

public class Cell extends StackPane {

    private String name;
    private String file;
    private String rank;
    private int x_position;
    private int y_position;
    private Cell[] castleCell  = new Cell[2];
    private Cell enPassantCell;
    private Piece piece;
    private Color blue;
    private Color green;
    private Color purple;
    private Color orange;
    private SimpleObjectProperty<Color> color = new SimpleObjectProperty<>();
    private boolean is_selected = false;
    private boolean is_highlighted = false;
    private boolean is_checked = false;
    private boolean is_castle = false;
    private boolean is_en_passant = false;

    @FXML private StackPane context;
    @FXML private ImageView image;
    @FXML private Rectangle outerSquare;
    @FXML private Rectangle innerSquare;

    public Cell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cell.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        }

        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        context = this;

        blue = new Color(0.0, 0.5, 1.0, 1.0);
        green = new Color(0.0, 1.0, 0.0, 1.0);
        purple = new Color(0.5, 0.0, 0.5, 1.0);
        orange = new Color(1.0, 0.5, 0.0, 1.0);

    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setX_position(int x_position) {
        this.x_position = x_position;
    }

    public void setY_position(int y_position) {
        this.y_position = y_position;
    }

    public void setCastle(boolean is_castle) {
        this.is_castle = is_castle;
    }

    public void setEnPassant(boolean is_en_passant) {
        this.is_en_passant = is_en_passant;
    }

    public String getName() {
        return file + rank;
    }

    public String getFile() {
        return file;
    }

    public String getRank() {
        return rank;
    }

    public int getX_position() {
        return x_position;
    }

    public int getY_position() {
        return y_position;
    }

    public boolean isSelected() {
        return is_selected;
    }

    public boolean isHighlighted() {
        return is_highlighted;
    }

    public boolean isChecked() {
        return is_checked;
    }

    public boolean isCastle() {
        return is_castle;
    }

    public boolean isEnPassant() {
        return is_en_passant;
    }

    public Cell[] getCastleCell() {
        return castleCell;
    }

    public void setCastleCell(Cell[] castleCell) {
        this.castleCell = castleCell;
    }

    public Cell getEnPassantCell() {
        return enPassantCell;
    }

    public void setEnPassantCell(Cell enPassantCell) {
        this.enPassantCell = enPassantCell;
    }

    public void setColor(Color color){
        this.color.set(color);
        outerSquare.setFill(this.color.get());
        innerSquare.setFill(this.color.get());
    }

    public Color getColor() {
        return color.get();
    }

    public void setPiece(Piece piece, String path) {
        image.setImage(new Image(path));
        this.piece = piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void deletePiece() {
        image.setImage(null);
        outerSquare.setFill(color.get());
        this.piece = null;
    }

    public void selectCell() {
        outerSquare.setFill(Color.RED);
        is_selected = true;
    }

    public void deselectCell() {
        outerSquare.setFill(color.get());
        is_selected = false;
    }

    public void highlightCell() {
        if (is_castle) {
            outerSquare.setFill(purple);
        }
        else {
            outerSquare.setFill(green);
        }
        is_highlighted = true;
    }

    public void dehighlightCell() {
        outerSquare.setFill(color.get());
        is_highlighted = false;
    }

    public void checkCell() {
        checkCell(Color.RED);
        is_checked = true;
    }

    public void checkMateCell() {
        checkCell(orange);
    }

    private void checkCell(Color color) {
        outerSquare.setFill(color);
        innerSquare.setFill(color);
    }

    public void decheckCell() {
        outerSquare.setFill(getColor());
        innerSquare.setFill(getColor());
        int k = (x_position + y_position) % 2;
        if ( k != 0) {
            this.setColor(blue);
        }
        else {
            this.setColor(Color.WHITE);
        }
        is_checked = false;
    }
}
