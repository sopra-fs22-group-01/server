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


    private int startTime;
    private boolean timerRunning = false;
    private int currentTime;


    public Countdown(int startTime) {
        this.startTime = startTime;
    }

    // call this method to start the timer
    public void startCountdown(){
        //sets the time which keeps track of the countdown to the current time.
        if(timerRunning){
            return;
        }
        timerRunning = true;
        currentTime = startTime;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            // run() method to carry out the action of the task
            public void run() {
                System.out.println("Keep on calling " + currentTime);
                currentTime--;
                if(currentTime <=0) {
                    System.out.println("Stop calling");
                    timerRunning = false;
                    // cancel method to cancel the execution
                    timer.cancel();
                }
            };
        };


        timer.schedule(task, 1000, 1000);
    }

    public int getCurrentTime(){
        return this.currentTime;
    }

}
