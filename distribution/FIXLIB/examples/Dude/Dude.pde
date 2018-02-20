import fixlib.*;

Fixlib fix = Fixlib.init(this);


/***/
void setup() {
    background(-1);
    size( displayWidth, displayHeight);
    smooth();

    textSize(42);
    fill(#EF2018);
    text( fix.pdeName(), 11, height-100 );
}

/***/
void draw() {

    stroke(frameCount%255);
    fill(random(frameCount%width), random(height), frameCount%42, 88);

    beginShape();
        shape( fix.shapeJous( random(frameCount%width), random(height), 42, frameCount%42 ) );


    endShape();

    if(frameCount>(width+height)){
        println("peace out");
        exit();
    }

}