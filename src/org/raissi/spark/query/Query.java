package org.raissi.spark.query;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.text.StrSubstitutor.replace;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.raissi.spark.FileSplitter;
import org.raissi.spark.interpreter.InterpreterResult;
import org.raissi.spark.interpreter.InterpreterResult.Code;
import org.raissi.spark.interpreter.SparkInterpreter;

public class Query {

	private static final String LINE_SEPARATOR;
	static{
		LINE_SEPARATOR = defaultIfEmpty(SystemUtils.LINE_SEPARATOR, "\n");
	}
	private String[] content;
	private Map<String, Object> params;

	private SparkInterpreter interpreter;
	
	

	public static Query get() {
		return new Query();
	}
	
	/**
	 * Executes the code by starting a new SparkContext and stopping it after execution. <br/>
	 * This method is meant to be called only once inside a Java thread.
	 * @return
	 */
	public Object execute(){
		prepareForExecution();
		interpreter.open();
		Object res = executeContent();
		interpreter.close();
		return res;
	}
	
	/**
	 * Executes the code inside a started context. <br/>
	 * This method can be called multiple times but must be preceded by a call to {@link SparkInterpreter#open()}.
	 * @return
	 */
	public Object executeInContext(){
		prepareForExecution();
		return executeContent();
	}

	private Object executeContent() {
		InterpreterResult result = interpreter.interpret(content);
		if(result.code() == Code.ERROR){
			return result;
		}
		Object res = interpreter.resources.get("lastObject");
		return res;
	}

	private void prepareForExecution() {
		if(interpreter == null){
			throw new IllegalStateException("Interpreter can not be null");
		}
		if (params != null) {
			String[] tmp = new String[content.length];
			for(int i = 0; i < content.length; i++){
				tmp[i] = replace(content[i], params);
			}
			content = tmp;
		}
	}
	
	
	public Query interpreter(SparkInterpreter interpreter){
		this.interpreter = interpreter;
		return this;
	}
	public Query query(String[] code) {
		assertNotEmpty(code, "query");
		this.content = code;
		return this;
	}
	public Query query(String code) {
		assertNotEmpty(code, "query");
		this.content = code.split(LINE_SEPARATOR);
		return this;
	}
	public Query queryScriptFile(String path) {
		assertNotEmpty(path, "File Path");
		String[] code = FileSplitter.split(path);
		return query(code);
	}

	public Query setParamter(String name, Object value) {
		if (params == null) {
			params = new HashMap<>();
		}
		params.put(name, value);
		return this;
	}

	protected void assertNotEmpty(String s, String name) {
		if (isBlank(s)) {
			throw new IllegalArgumentException("Value of" + name + "  can not be empty");
		}
	}
	protected void assertNotEmpty(String[] arr, String name){
		if(isEmpty(arr)){
			throw new IllegalArgumentException("Value of" + name + "  can not be empty");
		}
	}

}
