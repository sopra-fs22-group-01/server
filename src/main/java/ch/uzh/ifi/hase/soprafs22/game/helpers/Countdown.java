package ch.uzh.ifi.hase.soprafs22.game.helpers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is for timer
 * Usage: create an instance of this class
 * and call startTimer();
 *
 * It will count down asynchronous while main program runs
 * */

public class Countdown extends TimerTask {


    private int time = 30;
    private Timer timer;

    public void resetTimer(){
        this.time = 30;
    }

    // call this method to start the timer
    public void startTimer(){
        this.timer = new Timer();
        timer.scheduleAtFixedRate(this, 1500, 1000);
    }

    // Method which will be executed periodically by the timer
    @Override
    public void run() {
        time--;
        if(time == 0)
            // cancel() returns a boolean value (can be used to stop card voting)
            timer.cancel();
    }

    public int getTime(){
        return time;
    }


}
