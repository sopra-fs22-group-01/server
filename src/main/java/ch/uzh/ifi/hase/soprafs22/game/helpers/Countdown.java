package ch.uzh.ifi.hase.soprafs22.game.helpers;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown extends TimerTask {

    private static int time = 31;

    public void setTimer(){
        Timer timer = new Timer();
        TimerTask task = new Countdown();

        timer.schedule(task, 1000, 1000);
    }

    public void startTimer(){
    }
    public int getTimeLeft(){
        return time;
    }


    @Override
    public void run() {
        System.out.println("Time left: " + --time);
        if(time == 0) cancel();
    }

    public static void main(String[] args) {
        Countdown countdown = new Countdown();
        countdown.setTimer();
    }
}
