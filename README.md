Polargraph Library

This is to be a reusable set of components that can be used to build commands for, and communicate with a Polargraph machine.

Use the pattern

``` java
//  Create a new machine of a certain size, specifying a drawable area within it, and a native unit size
Polargraph machine = new Polargraph(700, 500, new Rectangle2D.Float(0,0,350,500), 1.0D);

// Create a drawing, which is essentially an addressable "sub-machine".  This can be anywhere inside the drawable area of the machine.
PolargraphDrawing drawing = machine.createNewDrawing("main", new Rectangle2D.Float(100,100,100,100));

// Create a Queue, that will communicate on a usb COM port (ie a connected Arduino).
QueueWriter queue = new VirtualComQueueWriter("COM 14");

// Build a simple command
Command c = CommandFactory.newCommand(CommandFactory.CMD_PEN_UP);
queue.add(c);

// Try a command with parameters
Command c = CommandFactory.newCommand(CommandFactory.CMD_SET_MOTOR_SPEED)
	.addParam("maxSpeed", 4000);
queue.add(c);

// and why not try getting some values from the machine
RPoint p = drawing.getAsNative(mouseX, mouseY);
Command c = CommandFactory.newCommand(CommandFactory.CMD_CHANGELENGTH)
	.addParam("a", p.x),
	.addParam("b", p.y);
queue.add(c);

// Send some commands
if (queue.isConnected()) {
	while (queue.hasCommands) {
		if (queue.isReady()) 
			queue.dispatchNextCommand();
	}
}
```

