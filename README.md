## Introduction

This library can be used to analyze the raw data of the Wooting Keyboard.
The library will trigger onReadEvents when recieve raw data from Wooting keyboard. This data can be parsed into data of type String or HashMap through parseLog and parseData functions.

## How to install WootingKeyboard

### Install with the Contribution Manager

Add contributed Libraries by selecting the menu item _Sketch_ ¡æ _Import Library..._ ¡æ _Add Library..._ This will open the Contribution Manager, where you can browse for WootingKeyboard, or any other Library you want to install.

Not all available Libraries have been converted to show up in this menu. If a Library isn't there, it will need to be installed manually by following the instructions below.

### Manual Install

Contributed Libraries may be downloaded separately and manually placed within the `libraries` folder of your Processing sketchbook. To find (and change) the Processing sketchbook location on your computer, open the Preferences window from the Processing application (PDE) and look for the "Sketchbook location" item at the top.

By default the following locations are used for your sketchbook folder: 
  * For Mac users, the sketchbook folder is located inside `~/Documents/Processing` 
  * For Windows users, the sketchbook folder is located inside `My Documents/Processing`

Download WootingKeyboard from [here](https://github.com/Shinhoo/Wooting-Keyboard-Library/raw/master/distribution/WootingKeyboard-1/download/WootingKeyboard-1.zip).

Unzip and copy the contributed Library's folder into the `libraries` folder in the Processing sketchbook. You will need to create this `libraries` folder if it does not exist.

The folder structure for Library WootingKeyboard should be as follows:

```
Processing
  libraries
    WootingKeyboard
      examples
      library
        WootingKeyboard.jar
      reference
      src
```
             
Some folders like `examples` or `src` might be missing. After Library WootingKeyboard has been successfully installed, restart the Processing application.

### Troubleshooting

If you're having trouble, have a look at the [Processing Wiki](https://github.com/processing/processing/wiki/How-to-Install-a-Contributed-Library) for more information, or contact the author [Shinhoo Park @ KAIST Interactive Media Lab](http://kiml.org/WootingKeyboard).


## Using the Library

### Create Manager and connect to Wooting keyboard

```java
WootingManager wManager;

void setup()
{
  wManager = new WootingManager(this);
  wManager.openWootingDevices();
  
  ...
}
```

### Tracking mode
* Tracking All of key

```java
	wManager.setAllMode();
```

* Tracking Alphabet, number, arrow and function key

```java
	wManager.setTextMode();
```

* Tracking selected keys 

```java
	ArrayList<KeyValue> custom = new ArrayList<KeyValue>();
	custom.add(KeyValue.A);
	custom.add(KeyValue.S);
	custom.add(KeyValue.D);
	custom.add(KeyValue.F);
	wManager.setCustomMode(custom);
```

### Implementing Callback Events

The following callback events need to be implemented inside the main Processing applet in order for the library to be used properly:

* `void onReadEvent(byte[] buffer)`  
    The onReadEvent will be called whenever recieve the raw data from the Wooting keyboard.
    The length of raw data is 32 bytes, and it consists of up to 16 pairs of key information.

### Parsing function

* `String parseLog(byte[] data)`  
    Parse raw data into log string.
    A log string consists of pairs of key names and depth values.
    
```
	ex) A: 145 / S: 202 / d: 44 /
```
    
* `HashMap<KeyValue, Integer> parseData(byte[] data)`  
    Parsing raw data into Hash map data.
    A Hash map consists of pairs of key Information and depth values.

## Additional Info

* __Platforms:__ > OS X Mavericks (10.9), > Windows 7
* __Processing Version:__ >= 2.2.1, >= 3.0


