import java.util.List;
import java.util.Properties;
import org.raissi.spark.query.Query;
import org.raissi.spark.interpreter.SparkInterpreter;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.MapPartitionsRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;

class Point {
  PVector p;
  color c;
  Point( PVector _p, color _c ){
    p = _p;
    c = _c;
  }
  void process(){
    render();
  }
  
  void render(){
    stroke(c);
    point( p.x, p.y, p.z );
  }
}
Point[] points ;
void setup(){
  size(800,800,P3D);
  points = computeKMeans();
  
}
color getColorForCluster(int cluster){
  color c = #000000;      
  switch(cluster){
   case 0: c = #ff0000;
   break;
   case 1: c = #ffa500;
   break;
   case 2: c = #a52a2a;
   break;
   case 3: c = #008000;
   break;
   case 4: c = #0000ff;
   break;       
  }
  return c;
}
void draw(){
  background(0);
  translate(width/2.0,height/2.0);
  rotateX(map(mouseY, 0,height-1, -PI,PI));
  rotateY(map(mouseX, 0,width-1, -PI,PI));
  scale(100);
  noFill();
  stroke(255);
  box(2);
  for( int t=0; t < points.length;t++){
    points[t].process();
  }
}

Point[] computeKMeans(){
  Properties p = new Properties();
    p.setProperty("master", "spark://MAC122.local:7077");
    p.setProperty("spark.app.name", "Kmeans App");
    p.setProperty("zeppelin.spark.maxResult", "1000");
    p.setProperty("zeppelin.spark.importImplicit", "true");

  SparkInterpreter repl = new SparkInterpreter(p);
  repl.open();
  
  Query.get().interpreter(repl).query("import org.apache.spark.mllib.clustering.KMeans").executeInContext();
  Query.get().interpreter(repl).query("import org.apache.spark.mllib.linalg.Vectors").executeInContext();
  Query.get().interpreter(repl).query("import org.apache.spark.mllib.util.KMeansDataGenerator").executeInContext();
  
  Query.get().interpreter(repl).query("val donneesGenerees = KMeansDataGenerator.generateKMeansRDD(sc, 1000, 5, 3, 5, 1)").executeInContext();
  Query.get().interpreter(repl).query("val donnees = donneesGenerees.map(s => Vectors.dense(s)).cache()").executeInContext();
  
  Query.get().interpreter(repl).query("val nbClusters = 5").executeInContext();
  Query.get().interpreter(repl).query("val nbIterations = 200").executeInContext();
  Query.get().interpreter(repl).query("val clusters = KMeans.train(donnees, nbClusters, nbIterations)").executeInContext();
  Query.get().interpreter(repl).query("val siic1 = clusters.computeCost(donnees)").executeInContext();
  Query.get().interpreter(repl).query("val indices = clusters.predict(donnees)").executeInContext();
  
  Object donnees = Query.get().interpreter(repl).query("donnees").executeInContext();
  Vector[] points = (Vector[]) ((MapPartitionsRDD<?,?>)donnees).collect();
  
  Object indicesAsObj = Query.get().interpreter(repl).query("indices.toDF()").executeInContext();
  List<?> indices = ((Dataset<Integer>)indicesAsObj).collectAsList();
  
  Point[] result = new Point[points.length];
  for(int i = 0; i<points.length; i++){
    Vector point = points[i];
    double x = point.apply(0);
    double y = point.apply(1);
    double z = point.apply(2);
    int cluster = ((GenericRowWithSchema)indices.get(i)).getInt(0);
    color c = getColorForCluster(cluster);
    result[i] = new Point( new PVector( (float)x, (float)y, (float)z ), c );
  }
  
  repl.close();
  return result;
}