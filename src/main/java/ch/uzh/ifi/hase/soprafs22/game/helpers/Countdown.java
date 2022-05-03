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

public class Countdown {

    private boolean exit = false;
    private int time = 15;
    private Timer timer;

    private void resetTimer() {
        this.time = 90;
    }

    public void killTimer(){
        this.exit = true;
        this.timer.cancel();
    }

    // call this method to start the timer
    public void startTimer() {

            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(!exit){
                    System.out.println(time--);
                    if(time <= 0){
                        timer.cancel();
                        try {
                            Thread.sleep(500); //was 500 before, worth a try for heroku
                            resetTimer();
                            startTimer();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    timer.cancel();
                }

            }
        };
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 1500, 1000);


        /*
        this.timer = new Timer();
        timer.scheduleAtFixedRate(this, 1500, 1000);*/
    }

    /*
    // Method which will be executed periodically by the timer
    @Override
    public void run() {
        System.out.println(time--);
        if(time == 0) {
            // cancel() returns a boolean value (can be used to stop card voting)
            resetTimer();
            timer.cancel();
        }
    }*/

    public int getTime(){
        return time;
    }
}
