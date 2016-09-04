package org.raissi.spark;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.MapPartitionsRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.raissi.spark.interpreter.InterpreterResult;
import org.raissi.spark.interpreter.SparkInterpreter;
import org.raissi.spark.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

public class App  {
	
	private File tmpDir;
	  public static Logger LOGGER = LoggerFactory.getLogger(App.class);
	  public static SparkInterpreter repl;
	  
	  public static String BASE_PATH = "/Users/raissilaabidi/work/rcp216/project/spark/src/test/resources/";
	  
	  public App(){
	        repl = new SparkInterpreter(getSparkTestProperties());
	  }
	  
	  public App(Properties p){
	        repl = new SparkInterpreter(p);
	  }
	  
    public static void main( String[] args ) {
    	//executeDistantSparkClient();
    	executeScalaOnSpark();
    }

	protected static void executeScalaOnSpark() {
		App app = new App();
    	
    	 Object last = null;
    	 
    	 
    	System.out.println(last);
    	repl.open();
    	//last = app.interpretQuery(BASE_PATH+"k-means.txt");
    	//app.interpretInline();
    	
    	//Interpret with external jars:
    	Properties p = getSparkTestProperties();
    	p.setProperty("spark.externalJars", "/Users/raissilaabidi/work/rcp216/tptexte/deps/lsa/target/lsa-1.0.0.jar,"
    										+"/Users/raissilaabidi/work/rcp216/tptexte/deps/stanford-corenlp-3.6.0.jar,"
    										+ "/Users/raissilaabidi/.m2/repository/edu/umd/cloud9/1.5.0/cloud9-1.5.0.jar,"
    										+ "/Users/raissilaabidi/work/rcp216/tptexte/deps/common/target/common-1.0.0.jar,"
    										+ "/Users/raissilaabidi/work/rcp216/tptexte/deps/stanford-corenlp-3.6.0-models.jar,"
    										+ "/Users/raissilaabidi/.m2/repository/info/bliki/wiki/bliki-core/3.0.19/bliki-core-3.0.19.jar");
    	p.setProperty("SPARK_HOME", "usr/local/spark/spark-1.6.1-bin-hadoop2.6");
    	//app = new App(p);
    	
    	last = app.interpretQuery(BASE_PATH+"k-means.txt");
    	
    	
    	//   indices
    	Vector[] points = (Vector[]) ((MapPartitionsRDD<?,?>)last).collect();
    	
    	Object indicesAsObj = Query.get().interpreter(repl).query("indices.toDF()").executeInContext();
    	@SuppressWarnings("unchecked")
		List<?> indices = ((Dataset<Integer>)indicesAsObj).collectAsList();
    	
    	for(int i = 0; i<points.length; i++){
    		Vector point = points[i];
    		double x = point.apply(0);
    		double y = point.apply(1);
    		double z = point.apply(2);
    		int cluster = ((GenericRowWithSchema)indices.get(i)).getInt(0);
    	}
    	
    	repl.close();
	}
    
    public Object interpretQuery(String filePath){
    	return Query.get().interpreter(repl).queryScriptFile(filePath).executeInContext();
    }
    
    public Object interpretInline(){
    	String code = "val a = 1 + 2 + ${c}";
    	Object result = Query.get().interpreter(repl).query(code).setParamter("c",15).executeInContext();
    	System.out.println("--------------"+result);
    	return result;
    }
    
    private InterpreterResult interpretFromFile(String filePath){
    	String[] code = FileSplitter.split(filePath);
    	if(code == null){
    		return null;
    	}
    	tmpDir = new File(System.getProperty("java.io.tmpdir") + "/ZeppelinLTest_" + System.currentTimeMillis());
        System.setProperty("zeppelin.dep.localrepo", tmpDir.getAbsolutePath() + "/local-repo");
        tmpDir.mkdirs();
        
        repl = new SparkInterpreter(getSparkTestProperties());
        repl.open();
        return repl.interpret(code);
        
    }
    
    
    private void interpretSparkScala(){
    	tmpDir = new File(System.getProperty("java.io.tmpdir") + "/ZeppelinLTest_" + System.currentTimeMillis());
        System.setProperty("zeppelin.dep.localrepo", tmpDir.getAbsolutePath() + "/local-repo");

        //tmpDir.mkdirs();
        
        repl = new SparkInterpreter(getSparkTestProperties());
        repl.open();
        
        
        InterpreterResult result = repl.interpret("val a = 1\nval b = 2".split("\n"));
        System.out.println(result);
        
        String code = "class Counter {\nvar value: Long = 0\n}\n // comment\n\n object Counter {\n def apply(x: Long) = new Counter()\n}";
        result = repl.interpret(code.split("\n"));
        System.out.println(result);
        
        
        
        repl.interpret("case class Person(name:String, age:Int)\n".split("\n"));
        repl.interpret("val people = sc.parallelize(Seq(Person(\"moon\", 33), Person(\"jobs\", 51), Person(\"gates\", 51), Person(\"park\", 34)))\n".split("\n"));
        repl.interpret("people.toDF.count".split("\n"));
        
        Object last = repl.resources.get("lastObject");
        
        System.out.println(last);
        
        result = 
        		repl.interpret(("def category(min: Int) = {"
                			+ "    if (0 <= value) \"error\"" + "}").split("\n"));
        //Expect error 
        System.out.println(result);
        delete(tmpDir);
    }
    
    
    private void delete(File file) {
        if (file.isFile()) file.delete();
        else if (file.isDirectory()) {
          File[] files = file.listFiles();
          if (files != null && files.length > 0) {
            for (File f : files) {
              delete(f);
            }
          }
          file.delete();
        }
      }
    
    public static Properties getSparkTestProperties() {
        Properties p = new Properties();
        p.setProperty("master", "spark://MAC122.local:7077");
        p.setProperty("spark.app.name", "Zeppelin Test");
        p.setProperty("zeppelin.spark.useHiveContext", "false");
        p.setProperty("zeppelin.spark.maxResult", "1000");
        p.setProperty("zeppelin.spark.importImplicit", "true");

        return p;
      }

	public static void executeDistantSparkClient() {
		SparkConf conf = new SparkConf().setAppName("Testing from Java").setMaster("spark://MAC122.local:7077")
    			.setJars(new String[]{"/Users/raissilaabidi/work/rcp216/project/spark/spark.jar"});
    	try(JavaSparkContext sc = new JavaSparkContext(conf)){
	    	JavaRDD<String> lines = sc.textFile("/Users/raissilaabidi/work/rcp216/project/spark/pom.xml");
	    	JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
	    	int totalLength = lineLengths.reduce((a, b) -> a + b);
	    	System.out.println(totalLength);
    	}finally{
    		
    	}
	}
    
    
    
    
}
