
## Introduction

The CountdownTimer is an easy to use library for performing timed events.
It will allow the user to configure the timer's duration and intervals at which tick events will occur.
The timer will trigger onTickEvents after each interval and finish with an onFinishEvent.
In order to use the library, the main Processing applet will have to implement two callback events:

* __onTickEvent(int timerId, long timeLeftUntilFinish)__
* __onFinishEvent(int timerId)__

Multiple CountdownTimers can run inside the applet, in which case the timers will have their own unique id when needed to be distinguished.

## Installing the Library

The official method for installing Processing libraries can be found [here](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).
In case the library needs to be downloaded manually, you can download it from [here](https://raw.github.com/dhchoi/processing-countdowntimer/master/release/CountdownTimer.zip).

## Using the Library

### Creating a CountdownTimer

A CountdownTimer should be created using the static factory method `CountdownTimer.getNewCountdownTimer(PApplet)`.
Several examples of creating a CountdownTimer are:

```java
// creates a new timer that has not been configured and started
CountdownTimer timer = CountdownTimer.getNewCountdownTimer(this);

// creates a new timer that has been configured to have tick intervals every 1000 ms and run for a total of 5000 ms
CountdownTimer timer = CountdownTimer.getNewCountdownTimer(this).configure(1000, 5000);

// creates a new configured timer and start it right away
CountdownTimer timer = CountdownTimer.getNewCountdownTimer(this).configure(1000, 5000).start;
```

A CountdownTimer **MUST** be configured at least once before being started, or else an exception will be thrown.
It won't make sense for a CountdownTimer to start when it doesn't know how long it should run.

### Using a CountdownTimer

* __configure(long tickIntervalMillis, long timerDurationMillis)__

    Configures the tick intervals and duration of the timer in milliseconds. The timer must be configured first before calling the start() method.

* __start()__

    Starts the timer with the most recent tick interval and duration configurations. If the timer was stopped before the finish time, the method call will resume the timer from where it was stopped. Starting an already running timer will have no effect.

* __stop()__

    Interrupts the timer to stop after the currently running interval has been completed. Attempts to stop a timer that was already stopped or reset will have no effect.

* __reset()__

    Stops the timer and resets it to the most recent configuration. If the method was called while the timer was running, it will first stop the timer by effectively calling stop(). Attempts to reset a timer that was already reset or stopped will have no effect.

* __isRunning()__

    Returns true if the timer is currently running.

* __isPaused()__

    Returns true if the timer was stopped before being finished.

* __getId()__

    Returns the id of the timer.

### Using Multiple CountdownTimers

The first created timer will always have an id of 0.
All subsequently created timers will have an id that is 1 higher than the previously created timer's id (e.g. second created timer will have id 1, third created timer will have id 2, and so on).
The id of the timer that is calling the callback event will always be passed along, so this can be used to distinguish among different timers when multiple timers are used.
In case you want to perform operations to a timer with a specific id, you can use the following method to get the corresponding timer:

* __CountdownTimer.getCountdownTimerForId(int id)__

    Returns the timer associated with the corresponding id.

Once the CountdownTimer with the corresponding id has been found, regular operations can be applied to it as usual.
```java
// example of stopping a timer with an id of 5
CountdownTimer.getCountdownTimerForId(5).stop();
```

### Implementing Callback Events Inside the Main Processing Applet

The following callback events need to be implemented inside the main Processing applet in order for the library to be used.

* __onTickEvent(int timerId, long timeLeftUntilFinish)__

    The onTickEvent will be called whenever a tick interval has finished. The event will provide info about which timer has triggered the event and how much time is left till that timer finishes.

* __onFinishEvent(int timerId)__

    The onFinishEvent will be called whenever a timer has finished running the configured amount of duration. The event will provide info about which timer has triggered the event.

Any other details can be found inside the reference folder from the downloaded library.
