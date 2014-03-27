/**
 * CountdownTimer
 * A countdown timer for Processing which can trigger callback events during a user-defined set of tick intervals and duration.
 * https://github.com/dhchoi/processing-countdowntimer
 *
 * Copyright (c) 2014 Dong Hyun Choi
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 *
 * @author      Dong Hyun Choi
 * @modified    Mar 26, 2014
 * @version     0.9.0 (1)
 */

package com.dhchoi;

import processing.core.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;
import java.lang.reflect.Method;

/**
 * @example CountdownTimerExample
 */
public class CountdownTimer {
    // processing related fields
    private final PApplet mApp;
    // callback event related fields
    private static final String ON_TICK_EVENT_NAME = "onTickEvent";
    private static final String ON_FINISH_EVENT_NAME = "onFinishEvent";
    private Method onTickEvent;
    private Method onFinishEvent;
    // timerIdMap related fields
    private final int mId;
    private static int timerIdCounter = -1;
    private static Map<Integer, CountdownTimer> timerIdMap = new HashMap<Integer, CountdownTimer>();
    // timer related fields
    private final TimeUnit mTimeUnit = TimeUnit.MILLISECONDS;
    private ScheduledExecutorService mScheduledExecutorService = getNewScheduledExecutorService();
    private long mTimerDuration = 0;
    private long mTickInterval = 0;
    private long mTimeLeftAtStartOfTick = 0;
    private long mFinishTimeInFuture;
    private boolean mIsRunning = false;
    private boolean mDoReset = false;
    private final Runnable mCallableTask = new Runnable() {
        @Override
        public void run() {
            mTimeLeftAtStartOfTick = mFinishTimeInFuture - getSystemTimeMillis();

            if(mTimeLeftAtStartOfTick <= 0) {
                invokeMethod(onFinishEvent, mId);
                stop();
            }
            else {
                // perform tick and get tick performance time
                // the tick for the ongoing interval before the timer being stopped will always be performed
                long tickStartTime = getSystemTimeMillis();
                invokeMethod(onTickEvent, mId, mTimeLeftAtStartOfTick);
                long tickPerformanceTime = getSystemTimeMillis() - tickStartTime;

                // no time left, so finish timer
                if(tickPerformanceTime > mTimeLeftAtStartOfTick) {
                    invokeMethod(onFinishEvent, mId);
                    stop();
                }
                else {
                    // get next closest scheduled time
                    long nextScheduledTime = Math.min(mTimeLeftAtStartOfTick, mTickInterval) - tickPerformanceTime;
                    // in case mTickInterval < tickPerformanceTime < mTimeLeftAtStartOfTick, skip to next available interval
                    while (nextScheduledTime < 0) {
                        nextScheduledTime += mTickInterval;
                    }

                    // schedule new service only if timer is running and doesn't require reset
                    if(mIsRunning && !mDoReset) {
                        mScheduledExecutorService.schedule(mCallableTask, nextScheduledTime, mTimeUnit);
                    }
                }
            }
        }
    };


    /**
     * Returns a new timer that can be used inside the main Processing applet.
     * The first created timer will always have an id of 0.
     * All subsequently created timers will have an id that is 1 higher than the previously created timer's id
     * (e.g. second created timer will have id 1, third created timer will have id 2, and so on).
     *
     * @param app the main Processing applet
     * @return CountdownTimer
     */
    public static CountdownTimer getNewCountdownTimer(PApplet app) {
        timerIdCounter++;
        timerIdMap.put(timerIdCounter, new CountdownTimer(app, timerIdCounter));

        return getCountdownTimerForId(timerIdCounter);
    }

    private CountdownTimer(PApplet app, int id) {
        mId = id;
        mApp = app;
        mApp.registerMethod("dispose", this);

        try {
            onTickEvent = mApp.getClass().getMethod(ON_TICK_EVENT_NAME, new Class[] {int.class, long.class});
            onFinishEvent = mApp.getClass().getMethod(ON_FINISH_EVENT_NAME, new Class[] {int.class});
        } catch (NoSuchMethodException e) {
            System.err.println("Applet needs to implement both void " + ON_TICK_EVENT_NAME + "(int timerId, long timeLeftUntilFinish) and void " + ON_FINISH_EVENT_NAME + "(int timerId)");
            e.printStackTrace();
        }
    }

    /**
     * Returns the timer associated with the corresponding id.
     *
     * @param id id of the desired timer
     * @return CountdownTimer
     */
    public static CountdownTimer getCountdownTimerForId(int id) {
        return timerIdMap.get(id);
    }

    /**
     * Configures the tick intervals and duration of the timer in milliseconds.
     * The timer must be configured first before calling the start() method.
     *
     * @param tickIntervalMillis the tick interval (in milliseconds)
     * @param timerDurationMillis the total timer duration (in milliseconds)
     * @return CountdownTimer
     * @throws IllegalArgumentException if timerDurationMillis or tickIntervalMillis is not greater than zero, or if tickIntervalMillis is greater than timerDurationMillis
     * @throws IllegalStateException when attempted to configure a running or paused timer
     */
    public synchronized final CountdownTimer configure(long tickIntervalMillis, long timerDurationMillis) {
        if(tickIntervalMillis <= 0) {
            System.err.println("tickIntervalMillis has to be greater than zero");
            throw new IllegalArgumentException("tickIntervalMillis has to be greater than zero");
        }

        if(timerDurationMillis <= 0) {
            System.err.println("timerDurationMillis has to be greater than zero");
            throw new IllegalArgumentException("timerDurationMillis has to be greater than zero");
        }

        if(timerDurationMillis < tickIntervalMillis) {
            System.err.println("tickIntervalMillis cannot be longer than timerDurationMillis");
            throw new IllegalArgumentException("tickIntervalMillis cannot be longer than timerDurationMillis");
        }

        if(mIsRunning) {
            System.err.println("cannot configure timer when it is already running");
            throw new IllegalStateException("cannot configure timer when it is already running");
        }

        if(isPaused()) {
            System.err.println("cannot configure timer when it has paused");
            throw new IllegalStateException("cannot configure timer when it has paused");
        }

        mTickInterval = tickIntervalMillis;
        mTimerDuration = timerDurationMillis;

        return this;
    }

    /**
     * Starts the timer with the most recent tick interval and duration configurations.
     * If the timer was stopped before the finish time, the method call will resume the timer from where it was stopped.
     * Starting an already running timer will have no effect.
     *
     * @return CountdownTimer
     * @throws IllegalStateException if timer has not been configured before the initial call to the method
     */
    public synchronized final CountdownTimer start() {
        if(isPaused()) {
            return start(mTimeLeftAtStartOfTick);
        }

        return start(mTimerDuration); // case when timer has finished, has been reset, or is the first time starting
    }

    private synchronized CountdownTimer start(long duration) {
        if(mTimerDuration == 0 || mTickInterval == 0) {
            System.err.println("CountdownTimer has not been configured yet");
            throw new IllegalStateException("CountdownTimer has not been configured yet");
        }

        if(!mIsRunning) {
            if(mScheduledExecutorService.isShutdown()) {
                mScheduledExecutorService = getNewScheduledExecutorService();
            }

            try {
                mIsRunning = true;
                mDoReset = false;

                mFinishTimeInFuture = getSystemTimeMillis() + duration;
                mScheduledExecutorService.schedule(mCallableTask, mTickInterval, mTimeUnit);
            }
            catch (Exception e) {
                System.err.println("Failed to start the timer");
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Stops the timer and resets it to the most recent configuration.
     * If the method was called while the timer was running, it will first stop the timer by effectively calling stop().
     * Attempts to reset a timer that was already reset or stopped will have no effect.
     *
     * @return CountdownTimer
     */
    public synchronized final CountdownTimer reset() {
        mDoReset = true;
        return stop();
    }

    /**
     * Interrupts the timer to stop after the currently running interval has been completed.
     * Attempts to stop a timer that was already stopped or reset will have no effect.
     *
     * @return CountdownTimer
     */
    public synchronized final CountdownTimer stop() {
        mIsRunning = false;
        mScheduledExecutorService.shutdown();

        return this;
    }

    /**
     * Returns the id of the timer.
     *
     * @return int
     */
    public final int getId() {
        return mId;
    }

    /**
     * Returns true if the timer is currently running.
     *
     * @return boolean
     */
    public final boolean isRunning() {
        return mIsRunning;
    }

    /**
     * Returns true if the timer was stopped before being finished.
     *
     * @return boolean
     */
    public final boolean isPaused() {
        return !mIsRunning && (mTimeLeftAtStartOfTick > 0) && !mDoReset;
    }

    /**
     *  Method used by the main Processing applet to clear up any resources used by the timer when closing the applet.
     */
    public void dispose() {
        mScheduledExecutorService.shutdownNow();
    }

    private long getSystemTimeMillis() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    private ScheduledExecutorService getNewScheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    private void invokeMethod(Method method, Object... args) {
        if(method != null) {
            try {
                method.invoke(mApp, args);
            } catch (Exception e) {
                System.err.println("failed to call " + method.toString() + " event inside main app");
                e.printStackTrace();
            }
        }
    }
}
