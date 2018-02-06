import fixlib.*;

Fixlib fix = Fixlib.init(this);


/***/
void setup() {
    background(-1);
    size( displayWidth, displayHeight);
    smooth();

    textSize(42);
    text( fix.pdeName(), 11, height-100 );
}

/**
    TODO: fix the glitches, the make a better sample
*/
void draw() {




    strokeWeight(random(4));
    stroke(random(255));
    noFill();
    fix.trunk( width/2, height/2 );

    //strokeWeight(2);
    //fill(random(255));
    //fix.circleGrid( frameCount % width/2, frameCount % height/2) ;

    if(frameCount>(width+height)){
        println("peace out");
        exit();
    }

}