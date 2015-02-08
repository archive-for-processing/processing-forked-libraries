
## Introduction

The CountdownTimer is an easy to use library for performing timed events.
It will allow the user to configure the timer's duration and interval at which tick events will occur.
The timer will trigger onTickEvents after each tick interval and will eventually trigger an onFinishEvent when the whole timer duration has finished.
In order to use the library, the main Processing applet will have to implement the two callback events:

* __onTickEvent(int timerId, long timeLeftUntilFinish)__
* __onFinishEvent(int timerId)__

Multiple CountdownTimers can run inside the applet, in which case the timers will have their own unique id when needed to be distinguished.

## How It Works

### Operation Behavior

Once a configured timer starts, the first onTickEvent will be triggered after a period of tick interval has passed.
Subsequent onTickEvents will follow until the time left is equal or less than the tick interval.
If there is no more time left for an onTickEvent to happen, the onFinishEvent will be triggered as soon as the timer runs out of remaining time. 

* Example with tickIntervalMillis=10, timerDurationMillis=50  
start         (timeLeftUntilFinish=50)  
onTickEvent   (timeLeftUntilFinish=40)  
onTickEvent   (timeLeftUntilFinish=30)  
onTickEvent   (timeLeftUntilFinish=20)  
onTickEvent   (timeLeftUntilFinish=10)  
onFinishEvent (timeLeftUntilFinish=0)  

* Example with tickIntervalMillis=10, timerDurationMillis=45  
start         (timeLeftUntilFinish=45)  
onTickEvent   (timeLeftUntilFinish=35)  
onTickEvent   (timeLeftUntilFinish=25)  
onTickEvent   (timeLeftUntilFinish=15)  
onTickEvent   (timeLeftUntilFinish=5)  
onFinishEvent (timeLeftUntilFinish=0) 

### Operation States

The timer has two booleans for representing its states: `isRunning` and `isPaused`.
This was intended in order to differentiate a not-running timer that has never started yet and a not-running timer that was stopped in the middle.

By using the two booleans, there can be three possible states.

* isRunning() == false && isPaused() == false  
  A timer that has not started yet, has finished running, or has been reset.

* isRunning() == true && isPaused() == false  
  A timer that has started and is currently running.

* isRunning() == false && isPaused() == true  
  A timer that has started but is not currently running (i.e. stopped before being finished).

A state consisting isRunning() == true && isPaused == true cannot happen, since a timer cannot simultaneously stop and run at the same time.

As a summary, each boolean can be used for checking the following conditions:  
- isRunning(): is the timer currently running or not?  
- isPaused(): if not timer is not running, has the timer finished or not?

## Installing the Library

The official method for installing Processing libraries can be found [here](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).
In case the library needs to be downloaded manually, you can download it from [here](https://raw.github.com/dhchoi/processing-countdowntimer/master/release/CountdownTimer.zip).

## Using the Library

### Creating a CountdownTimer

All CountdownTimers are managed through the CountdownTimerService.
A CountdownTimer should be created using the static factory method `CountdownTimerService.getNewCountdownTimer(PApplet)`.
Several examples of creating a CountdownTimer are:

```java
// creates a new timer that has not been configured and started
CountdownTimer timer = CountdownTimerService.getNewCountdownTimer(this);

// creates a new timer that has been configured to trigger ticks every 1000 ms and run for a total of 5000 ms
CountdownTimer timer = CountdownTimerService.getNewCountdownTimer(this).configure(1000, 5000);

// creates and configures a new timer and starts it right away
CountdownTimer timer = CountdownTimerService.getNewCountdownTimer(this).configure(1000, 5000).start();
```

A CountdownTimer **MUST** be configured at least once before being started, or else an IllegalStateException will be thrown.
It won't make sense for a CountdownTimer to start when it doesn't know how long it should run.

### Using a CountdownTimer

* __configure(long tickIntervalMillis, long timerDurationMillis)__

    Configures the tick interval and timer duration in milliseconds. The timer must be configured first before calling the start() method.

* __start()__

    Starts the timer with the most recent tick interval and timer duration configuration. If the timer was stopped before the finish time, the method call will resume the timer from where it was stopped. Starting an already running timer will have no effect.

* __stop(StopBehavior)__

    Interrupts the timer to stop running, based on the intended stop behavior. Attempts to stop a timer that was already stopped or reset will have no effect.

* __reset(StopBehavior)__

    Stops the timer and resets it to the most recent configuration. If the method was called while the timer was running, it will first stop the timer by effectively calling stop(). Attempts to reset a timer that was already reset or stopped will have no effect.

* __isRunning()__

    Returns true if the timer is currently running.

* __isPaused()__

    Returns true if the timer was stopped before being finished.

* __getId()__

    Returns the id of the timer.

* __getTickInterval()__

    Returns the configured tick interval in milliseconds.

* __getTimerDuration()__

    Returns the configured timer duration in milliseconds.

* __getTimeLeftUntilNextEvent()__

    Returns the current time left until the upcoming tick or finish event in milliseconds.

* __getTimeLeftUntilFinish()__

    Returns the current time left until the timer finishes in milliseconds.

### Using Multiple CountdownTimers

The first created timer will always have an id of 0.
All subsequently created timers will have an id that is 1 higher than the previously created timer's id (e.g. second created timer will have an id of 1, third created timer will have an id of 2, and so on).
The id of the timer that is calling the callback event will always be passed along to the callback event method. This can be used to distinguish timers from one another when multiple timers are being used.
In case you want to perform operations to a timer with a specific id, you can use the following static method to get the timer with the corresponding id:

* __CountdownTimer.getCountdownTimerForId(int id)__

    Returns the timer associated with the corresponding id.

Once the timer with the corresponding id has been found, any necessary operations can be applied to it as usual.
```java
// example of stopping a timer with an id of 5
CountdownTimer.getCountdownTimerForId(5).stop();
```

If you want to check the set of timers that have been created along with its ids, you can use the following static method:

* __CountdownTimer.getTimerIds()__

    Returns a set of timer ids that have been created.

```java
CountdownTimer.getNewCountdownTimer(this); // new CountdownTimer with id=0
CountdownTimer.getNewCountdownTimer(this); // new CountdownTimer with id=1
CountdownTimer.getTimerIds(); // will return a set with ids [0, 1]
```
    

### Implementing Callback Events

The following callback events need to be implemented inside the main Processing applet in order for the library to be used.

* __onTickEvent(int timerId, long timeLeftUntilFinish)__

    The onTickEvent will be called whenever a tick interval has finished.
    The event will provide info about which timer has triggered the event and how much time is left till that timer finishes.

    *NOTICE:* If the onTickEvent operation runs longer than the tick interval, the subsequent onTickEvent that was supposed to be triggered under normal circumstances will be skipped.

* __onFinishEvent(int timerId)__

    The onFinishEvent will be called whenever a timer has finished running for the configured amount of timer duration. The event will provide info about which timer has triggered the event.

## Additional Info

Any other details about how to use this can be found inside the reference folder from the downloaded library.
Additional examples are also bundled along with the library.
The library does not require any other dependencies to run, and it has been tested with the following environments:

* __Platforms:__ OS X Mavericks, Windows 7
* __Processing Version:__ > 2.0
