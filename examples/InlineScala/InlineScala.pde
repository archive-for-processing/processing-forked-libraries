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
  
  res = executeInlineScala();
  
  PFont font = createFont("",40);
  textFont(font);
}

Integer executeInlineScala(){
	Properties p = new Properties();
    p.setProperty("master", "spark://MAC122.local:7077");
    p.setProperty("spark.app.name", "Inline Scala execution");
    //p.setProperty("zeppelin.spark.maxResult", "1000");
    //p.setProperty("zeppelin.spark.importImplicit", "true");

	SparkInterpreter repl = new SparkInterpreter(p);
    repl.open();
    
    String code = "val a = 1 + 2 + ${c}";
    Object result = Query.get().interpreter(repl).query(code).setParamter("c",15).executeInContext();
    
    code = "val b = 1 + a";
    result = Query.get().interpreter(repl).query(code).executeInContext();
    repl.close();
    
    return (Integer) result;
}

void draw() {
  background(0);
  fill(255);
  text(res, 40, 200);
}