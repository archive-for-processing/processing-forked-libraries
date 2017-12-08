int setDepthGradient( int depth, int[] colorList ) { //<>//
  return setDepthGradientTime( depth, colorList, 0, 0);
}

int setDepthGradientTime( int depth, int[] colorList, int maxColors, int time ) {


  if ( maxColors > colorList.length || maxColors == 0 ) maxColors = colorList.length;

  int[] colorListFlow = new int[ colorList.length ];

  for (int i = 0; i < colorList.length; i++) {
    colorListFlow[i] = colorList[abs( (i + time)%maxColors) ];
  }

  //if (tooClose)return -1;
  float debugMouse = map(mouseX, 0, width, 0.0, 2.0);
  
  float correction = debugMouse;
  float correctedDepth = depth * pow( correction , norm(depth, minMaxDist[1], minMaxDist[0])) ; 

  float normalizedDepth =  constrain( norm(correctedDepth, minMaxDist[0], minMaxDist[1]), 0, 0.999999);

  int sMaxColors = maxColors - 1;

  int colorIndex =   constrain( floor( normalizedDepth * (sMaxColors) ), 0, sMaxColors) ;

  float subGradients = (sMaxColors) * (normalizedDepth % ( 1.0/(sMaxColors)) );

  return lerpColor( colorListFlow[colorIndex], colorListFlow[colorIndex+1], subGradients );
}