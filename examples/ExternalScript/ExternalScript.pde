import java.util.Properties;
import template.library.*;
import org.raissi.spark.App;
import org.raissi.spark.query.Query;
import org.raissi.spark.interpreter.SparkInterpreter;

HelloLibrary hello;

Integer res = null;

void setup() {
  size(400,400);
  smooth();
  
  res = executeExternalScala();
  
  PFont font = createFont("",40);
  textFont(font);
}

Integer executeExternalScala(){
  Properties p = new Properties();
    p.setProperty("master", "spark://MAC122.local:7077");
    p.setProperty("spark.app.name", "External Scala code execution");
    p.setProperty("zeppelin.spark.maxResult", "1000");
    p.setProperty("zeppelin.spark.importImplicit", "true");

  SparkInterpreter repl = new SparkInterpreter(p);
  
  Object result = Query.get().interpreter(repl)
                    .queryScriptFile("/Users/raissilaabidi/work/rcp216/project/processing-spark-client/examples/ExternalScript/simple-script.txt")
                    .setParamter("c",15)
                    .execute();
  return (Integer) result;
}

void draw() {
  background(0);
  fill(255);
  text(res, 40, 200);
}