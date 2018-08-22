package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KCA on 7/21/2018.
 */
public class Time {
    private Label label;

    private ArrayList<Integer> addedTimeList;

    private Timeline timeline;

    private int timeRem;
    private int addedTimePos;
    boolean typeB;

    public Time(Label passedLabel, boolean typeA, boolean typeB) {
        addedTimePos = -1;
        addedTimeList = new ArrayList<>();
        this.label = passedLabel;
        this.typeB = typeB;
        if (typeA) {
            this.timeRem = Main.TIME_REMAINING;
        }
        else {
            this.timeRem = Main.TIME_REMAINING_2;
        }
        timeline = new Timeline();
        setUpTimeLine(timeRem * 2 + 1);
        if (!typeA) {
            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            timeline.pause();
                        }
                    }, 1000
            );
        }

        timeline.setOnFinished(event -> {
            if (addedTimePos + 1 < addedTimeList.size()) {
                addedTimePos++;
            }
            setUpTimeLine(addedTimeList.get(addedTimePos) * 2 + 1);
        });
    }

    private void animate() {
        int hr, min, sec;
        if (timeRem >= 0)
        {
            hr = timeRem / 3600;
            min = (timeRem / 60) - (hr * 60);
            sec = timeRem % 60;
            Platform.runLater(
                    () ->
                            label.setText(String.valueOf(String.valueOf(hr) + ":" +
                                    (min >= 10 ? String.valueOf(min) : "0" + String.valueOf(min)) + ":"
                                    + (sec >= 10 ? String.valueOf(sec) : "0" + String.valueOf(sec))))

            );
            timeRem--;
        }
        if (timeRem == 0) {
            if (typeB) {
                Platform.runLater(
                        () -> {
                            label.setTextFill(Color.RED);
                            label.setText("Time's up!");
                        }
                );
                reset();
                start();
                Platform.runLater(
                        () -> Board.getInstance().changeChance()
                );
            }
            else {
                int player = Board.getInstance().getPlayer();
                if (player == 0) {
                    Board.getInstance().createGameOverDialog("Black wins");
                }
                else {
                    Board.getInstance().createGameOverDialog("White wins");
                }
            }
        }
    }

    private void setUpTimeLine(int time) {
        timeline.getKeyFrames().clear();
        timeline.setCycleCount(time);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.millis(500),
                        e -> animate()
                )
        );
        timeline.play();
    }

    //A function that starts the timer
    public void start() {
        timeline.playFromStart();
    }

    //A function that continues the timer
    public void play() {
        timeline.play();
    }

    //A function that pauses the timer
    public void pause() {
        timeline.pause();
    }

    //A function that resets the timer
    public void reset() {
        timeRem = Main.TIME_REMAINING;
    }

    //A function that stops the timer
    public void stop() {
        timeline.getKeyFrames().clear();
    }

    //A function that increases timeRem
    public void increment(int addedTime) {
        timeRem += addedTime;
        addedTimeList.add(addedTime);
    }
}
