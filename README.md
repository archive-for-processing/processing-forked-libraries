
## Introduction
---

The CountdownTimer is an easy to use library for performing timed events.
It will allow the user to configure the timer's duration and intervals at which tick events will occur.
The timer will trigger onTickEvents after each interval and finish with an onFinishEvent.
In order to use the library, the main Processing applet will have to implement two callback events:

* onTickEvent(int timerId, long timeLeftUntilFinish)
* onFinishEvent(int timerId)

Multiple CountdownTimers can run inside the applet, in which case the timers will have their own unique id when needed to be distinguished.

## Installing the Library
---

The official method for installing Processing libraries can be found [here](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).
In case the library needs to be downloaded manually, you can download it from [here](https://raw.github.com/dhchoi/processing-countdowntimer/master/release/CountdownTimer.zip).

## Using the Library
---

### Creating a CountdownTimer

A CountdownTimer should be created using the static factory method `CountdownTimer.getNewCountdownTimer(PApplet)`.
Several examples of creating a CountdownTimer are:

```java
// creates a new timer that has not been configured and started
CountdownTimer timer = CountdownTimer.getNewCountdownTimer(this);
```

```java
// creates a new timer that has been configured to have tick intervals every 1000 ms and run for a total of 5000 ms
CountdownTimer timer = CountdownTimer.getNewCountdownTimer(this).configure(1000, 5000);
```

```java
// creates a new configured timer and start it right away
CountdownTimer timer = CountdownTimer.getNewCountdownTimer(this).configure(1000, 5000).start;
```

A CountdownTimer **MUST** be configured at least once before being started, or else an exception will be thrown.
It won't make sense for a CountdownTimer to start when it doesn't know how long it should run.

### Using a CountdownTimer

* configure(long tickIntervalMillis, long timerDurationMillis)

    Configures the duration and tick intervals of the timer in milliseconds. The timer must be configured first before calling the start() method.

* start()

    Starts the timer with the most recent interval and duration configurations. If the timer was stopped before the finish time, the method call will resume the timer from where it was stopped. Starting an already running timer will have no effect.

* stop()

    Interrupts the timer to stop after the currently running interval has been completed. Attempts to stop a timer that was already stopped or reset will have no effect.

* reset()

    Stops the timer and resets it to the most recent configuration. If the method was called while the timer was running, it will first stop it by effectively calling stop(). Attempts to reset a timer that was already reset or stopped will have no effect.

* isRunning()

    Returns true if the timer is currently running.

* isPaused()

    Returns true if the timer was stopped before being finished.

* getId()

    Returns the id of the timer.

### Using Multiple CountdownTimers

The first created timer will have an id of 0.
All subsequently created timers will have an id that is an increment of 1 of the previously created timer's id.

* CountdownTimer.getCountdownTimerForId(int id)

    Returns the timer associated with the corresponding id.

### Implementing Callback Events Inside the Main Processing Applet

The following callback events need to be implemented in order to use the library.

* onTickEvent(int timerId, long timeLeftUntilFinish)

    The onTickEvent will be called whenever a tick interval has finished. The event will provide info about which timer has triggered the event and how much time is left till that timer finishes.

* void onFinishEvent(int timerId)

    The onFinishEvent will be called whenever a timer has finished running the configured amount of duration. The event will provide info about which timer has triggered the event.


