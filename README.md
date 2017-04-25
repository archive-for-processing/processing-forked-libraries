
# Tactile Graphics for Processing

A library for Processing that allows sketches to be transformed into a tactile graphic.

## To Build

```bash
ant library.run -C resources
```

Assuming the values in `build.properties` are correct (particularly `classpath.libraries.location`),
this will create the files for distribution, and place them in Processing's libraries folder. It 
will then be able to be imported.

## To Use

See `examples/` for more.

```processing
import tactilegraphics.*

TactileGraphics tactilegraphics;

void setup() {
  tactilegraphics = new TactileGraphics(this);
}
```

That's it. When things are drawn to the sketch, the library is triggered.
