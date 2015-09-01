## Introduction

The `CountdownTimer` is an easy to use library for performing timed events.
It allows the user to configure the timer's duration and interval at which tick events will occur.
The timer will trigger an `onTickEvent()` after each tick interval and will eventually trigger an `onFinishEvent()` when the whole timer duration has finished.
In order to use the library, the main Processing applet will have to implement the two callback events:

* `void onTickEvent(CountdownTimer t, long timeLeftUntilFinish)`
* `void onFinishEvent(CountdownTimer t)`

Detailed examples of using the `CountdownTimer` can be found inside the bundled example codes.


## Installing the Library

The official method for installing Processing libraries can be found [here](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).
In case the library needs to be downloaded manually, you can download it from [here](https://raw.github.com/dhchoi/processing-countdowntimer/master/release/CountdownTimer.zip).


## How It Works

### Operation Behavior

Once a configured timer starts, the first `onTickEvent()` will be triggered after a period of tick interval has passed.
Subsequent `onTickEvent()` will follow until the time left is equal or less than the tick interval.
If there is no more time left for an `onTickEvent()` to happen, the `onFinishEvent()` will be triggered as soon as the timer runs out of remaining time. 

* Example with tickIntervalMillis=10 and timerDurationMillis=50

    triggeredEvent | when timeLeftUntilFinish is
    -------------- | :-------------------------:
    onTickEvent    | 40
    onTickEvent    | 30
    onTickEvent    | 20
    onTickEvent    | 10
    onFinishEvent  | 0

* Example with tickIntervalMillis=10 and timerDurationMillis=45

    triggeredEvent | when timeLeftUntilFinish is
    -------------- | :-------------------------:
    onTickEvent    | 35
    onTickEvent    | 25
    onTickEvent    | 15
    onTickEvent    | 5
    onFinishEvent  | 0

If the `onTickEvent()` operation runs longer than the tick interval,
the subsequent `onTickEvent()` that was supposed to be triggered under normal circumstances will be skipped.

* Example with tickIntervalMillis=10 and timerDurationMillis=50

    triggeredEvent                                    | when timeLeftUntilFinish is
    ------------------------------------------------- | :-------------------------:
    onTickEvent                                       | 40
    onTickEvent (took longer than 10ms to finish!)    | 30
    onTickEvent                                       | 10
    onFinishEvent                                     | 0

### Operation States

The timer has two booleans for representing its states: `isRunning` and `isPaused`.
This was intended in order to differentiate a not-running timer that has never started yet and a not-running timer that was stopped in the middle.

By using the two booleans, there can be three possible states.

* `isRunning() == false` && `isPaused() == false`  
  A timer that has not started yet, has finished running, or has been reset.

* `isRunning() == true` && `isPaused() == false`  
  A timer that has started and is currently running.

* `isRunning() == false` && `isPaused() == true`  
  A timer that has started but is not currently running (i.e. stopped before being finished).

A state consisting `isRunning() == true` && `isPaused == true` cannot happen, since a timer cannot simultaneously stop and run at the same time.

As a summary, each boolean can be used for checking the following conditions:  
* `isRunning()`: is the timer currently running or not?  
* `isPaused()`: if not timer is not running, has the timer finished or not?


## Using the Library

### Creating a Timer

All `CountdownTimer` are created and managed through the `CountdownTimerService`.
A `CountdownTimer` should be created using the static factory method `CountdownTimerService.getNewCountdownTimer(PApplet)`.
Several examples of creating a `CountdownTimer` are:

```java
// creates a new timer that has not been configured and started
CountdownTimer timer = CountdownTimerService.getNewCountdownTimer(this);

// creates a new timer that has been configured to trigger ticks every 1000 ms and run for a total of 5000 ms
CountdownTimer timer = CountdownTimerService.getNewCountdownTimer(this).configure(1000, 5000);

// creates and configures a new timer and starts it right away
CountdownTimer timer = CountdownTimerService.getNewCountdownTimer(this).configure(1000, 5000).start();
```

### Using a Timer

A `CountdownTimer` **MUST** be configured at least once before being started, or else an `IllegalStateException` will be thrown.
It won't make sense for a `CountdownTimer` to start when it doesn't know how long it should run.

The following methods are available for each timer:

* `configure(long tickIntervalMillis, long timerDurationMillis)`  
    Configures the tick interval and timer duration in milliseconds. The timer must be configured first before calling the `start()` method.

* `start()`  
    Starts the timer with the most recent tick interval and timer duration configuration.
    If the timer was stopped before the finish time, the method call will resume the timer from where it was stopped.
    Starting an already running timer will have no effect.

* `stop(StopBehavior)`  
    Interrupts the timer to stop running, based on the intended stop behavior.
    Attempts to stop a timer that was already stopped or reset will have no effect.

* `reset(StopBehavior)`  
    Stops the timer and resets it to the most recent configuration.
    If the method was called while the timer was running, it will first stop the timer by effectively calling `stop(StopBehavior)`.
    Attempts to reset a timer that was already reset or stopped will have no effect.

* `isRunning()`  
    Returns true if the timer is currently running.

* `isPaused()`  
    Returns true if the timer was stopped before being finished.

* `getId()`  
    Returns the id of the timer.

* `getTickInterval()`  
    Returns the configured tick interval in milliseconds.

* `getTimerDuration()`  
    Returns the configured timer duration in milliseconds.

* `getTimeLeftUntilNextEvent()`  
    Returns the current time left until the upcoming tick or finish event in milliseconds.
    Returns 0 if the timer is not running.

* `getTimeLeftUntilFinish()`  
    Returns the current time left until the timer finishes in milliseconds.
    Returns 0 if the timer is not running.

A `CountdownTimer` can be stopped or reset with different behaviors based on the `StopBehavior` type being used:

* `StopBehavior.STOP_IMMEDIATELY`  
    The timer will stop immediately regardless of how much time was left until the next tick or finish event.
    This is especially useful if the tick interval was set to be long.

* `StopBehavior.STOP_AFTER_INTERVAL`  
    The timer will stop only after the current ongoing interval has finished.
    This is the default behavior if `stop()` or `reset()` is called with no parameters. 

### Using Multiple Timers

Multiple timers can run inside the applet, in which case the timers will run independently of each other.
Individual timers can be referred to by either:

* Saving references to each `CountdownTimer` when creating them
* Using the unique timer id of each `CountdownTimer`
    * The first created timer will always have an id of 0
    * All subsequently created timers will have an id that is 1 higher than the previously created timer's id
    (e.g. second created timer will have an id of 1, third created timer will have an id of 2, and so on)
* Using the corresponding `CountdownTimer` inside each callback

### Managing All Timers Through `CountdownTimerService`

The `CountdownTimerService` provides methods for managing all `CountdownTimer` that have been created:

* `CountdownTimerService.getCountdownTimerForId(int id)`  
    Returns the timer associated with the corresponding id.  
    Once the timer with the corresponding id has been found, any necessary operations can be applied to it as usual, such as:
    ```java
    // example of stopping a timer with an id of 5
    CountdownTimerService.getCountdownTimerForId(5).stop();
    ```

* `CountdownTimerService.getTimerIds()`  
    Returns a set of timer ids that have been created.
    ```java
    CountdownTimerService.getNewCountdownTimer(this); // new CountdownTimer with id=0
    CountdownTimerService.getNewCountdownTimer(this); // new CountdownTimer with id=1
    CountdownTimerService.getTimerIds(); // will return a set with ids [0, 1]
    ```

* `CountdownTimerService.removeCountdownTimer(CountdownTimer)`,
  `CountdownTimerService.removeCountdownTimerWithId(timerId)`  
    Removes a specific `CountdownTimer`.
    Note that if a reference to a `CountdownTimer` still exists inside the app, the `CountdownTimer` may not be garbage collected,
    even though it was removed inside the `CountdownTimerService`.


### Implementing Callback Events

The following callback events need to be implemented inside the main Processing applet in order for the library to be used properly:

* `onTickEvent(CountdownTimer t, long timeLeftUntilFinish)`  
    The onTickEvent will be called whenever a tick interval has finished.
    The event will be called with the timer that has triggered the event, along with the time left until the timer finishes.

* `onFinishEvent(CountdownTimer t)`  
    The onFinishEvent will be called whenever a timer has finished running for the configured amount of timer duration.
    The event will be called with the timer that has triggered the event.

*__NOTICE__*: The legacy callbacks `void onTickEvent(int timerId, long timeLeftUntilFinish)` and
 `void onTickEvent(int timerId, long timeLeftUntilFinish)` will still function for backwards compatibility,
 but it is recommended to use the new callbacks that were added to v0.9.3 for better method chaining. 


## Additional Info

Any other details about how to use this can be found inside the reference folder from the downloaded library.
The library does not require any other dependencies to run, and it has been tested with the following environments:

* __Platforms:__ > OS X Mavericks (10.9), > Windows 7
* __Processing Version:__ >= 2.2.1, >= 3.0
