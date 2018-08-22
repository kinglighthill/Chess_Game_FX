package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by KCA on 7/21/2018.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer gamesPlayed;
    private Integer gamesWon;

    public Player(String name) {
        this.name = name.trim();
        gamesPlayed = new Integer(0);
        gamesWon = new Integer(0);
    }

    public String getName() {
        return name;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public Integer getGameswon() {
        return gamesWon;
    }

    public Integer calculateWinPercent() {
        return new Integer((gamesWon * 100) / gamesPlayed);
    }

    public void updateGamesPlayed() {
        gamesPlayed++;
    }

    public void updateGamesWon() {
        gamesWon++;
    }

    //Function to fetch the list of the players
    public static ArrayList<Player> fetchPlayers()    {
        Player tempPlayer;
        ObjectInputStream input = null;
        ArrayList<Player> players = new ArrayList<>();
        try {
            File  inFile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
            input = new ObjectInputStream(new FileInputStream(inFile));
            try {
                while(true) {
                    tempPlayer = (Player) input.readObject();
                    players.add(tempPlayer);
                }
            }
            catch(EOFException e) {
                input.close();
            }
        }
        catch (FileNotFoundException e) {
            players.clear();
            return players;
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                input.close();
            }
            catch (IOException e1) { }
            Alert alert = new Alert(null, "Unable to read the required Game files !!", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(null, "Game Data File Corrupted !! Click Ok to Continue Building New File",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return players;
    }

    //Function to delete a player
    public void deletePlayer() {
        ObjectInputStream input;
        ObjectOutputStream output;
        Player temp_player;
        File inputFile = null;
        File outputFile = null;
        try {
            inputFile  = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
            outputFile  = new File(System.getProperty("user.dir")+ File.separator + "tempfile.dat");
        }
        catch (SecurityException e) {
            Alert alert = new Alert(null, "Read-Write Permission Denied !! Program Cannot Start", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            System.exit(0);
        }

        try {
            if(!outputFile.exists()) {
                outputFile.createNewFile();
            }

            input = new ObjectInputStream(new FileInputStream(inputFile));
            output = new ObjectOutputStream(new FileOutputStream(outputFile));
            try {
                while(true) {
                    temp_player = (Player)input.readObject();
                    if (temp_player.getName().equals(getName())) {
                    }
                    else {
                        output.writeObject(temp_player);
                    }
                }
            }
            catch(EOFException e){
                input.close();
            }

            inputFile.delete();
            output.close();
            File newFile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
            if(!outputFile.renameTo(newFile)) {
                Alert alert = new Alert(null, "File Renaming Unsuccessful", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(null, "Unable to read/write the required Game files !! Press ok to continue",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(null, "Game Data File Corrupted !! Click Ok to Continue Building New File",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        catch (Exception e) { }
    }

    //Function to update the statistics of a player
    public void updatePlayer() {
        ObjectInputStream input;
        ObjectOutputStream output;
        Player temp_player;
        File inputFile = null;
        File outputFile = null;
        try {
            inputFile  = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
            outputFile  = new File(System.getProperty("user.dir")+ File.separator + "tempfile.dat");
        }
        catch (SecurityException e) {
            Alert alert = new Alert(null, "Read-Write Permission Denied !! Program Cannot Start", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            System.exit(0);
        }
        boolean playerDoNotExist;
        try {
            if(!outputFile.exists()) {
                outputFile.createNewFile();
            }
            if(!inputFile.exists()) {
                output = new ObjectOutputStream(new FileOutputStream(outputFile ,true));
                output.writeObject(this);
            }
            else {
                input = new ObjectInputStream(new FileInputStream(inputFile));
                output = new ObjectOutputStream(new FileOutputStream(outputFile));
                playerDoNotExist = true;
                try {
                    while(true) {
                        temp_player = (Player)input.readObject();
                        if (temp_player.getName().equals(getName())) {
                            output.writeObject(this);
                            playerDoNotExist = false;
                        }
                        else
                            output.writeObject(temp_player);
                    }
                }
                catch(EOFException e){
                    input.close();
                }
                if(playerDoNotExist)
                    output.writeObject(this);
            }
            inputFile.delete();
            output.close();
            File newFile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
            if(!outputFile.renameTo(newFile)) {
                Alert alert = new Alert(null, "File Renaming Unsuccessful", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(null, "Unable to read/write the required Game files !! Press ok to continue",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(null, "Game Data File Corrupted !! Click Ok to Continue Building New File",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        catch (Exception e) { }
    }
}
