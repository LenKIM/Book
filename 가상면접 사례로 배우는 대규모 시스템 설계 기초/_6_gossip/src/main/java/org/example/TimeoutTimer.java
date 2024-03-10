package org.example;

import javax.management.timer.Timer;
import java.util.Date;

public class TimeoutTimer extends Timer {

    /**
     * The amount of time this timer waits before generating a wake-up event.
     */
    private long sleepTime;

    private Member source;

    /**
     * Creates a reset-able timer that wakes up after millisecondsSleepTime.
     *
     * @param millisecondsSleepTime The time for this timer to wait before an event.
     * @param client
     * @param member
     */
    public TimeoutTimer(long millisecondsSleepTime, Client client, Member member) {
        super();
        this.sleepTime = millisecondsSleepTime;
        this.source = member;
        addNotificationListener(client, null, null);
    }

    public void start() {
        this.reset();
        super.start();
    }

    /**
     * Resets timer to start counting down from original time.
     */
    public void reset() {
        removeAllNotifications();
        setWakeupTime(sleepTime);
    }

    /**
     * Adds a new wake-up time for this timer.
     *
     * @param milliseconds
     */
    private void setWakeupTime(long milliseconds) {
        addNotification("type", "message", source, new Date(System.currentTimeMillis() + milliseconds));
    }
}