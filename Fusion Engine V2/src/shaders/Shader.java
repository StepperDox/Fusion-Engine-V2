package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utility.Console;

public abstract class Shader {
	private static FloatBuffer matBuf = BufferUtils.createFloatBuffer(16);
	private int program;
	private int vertex;
	private int fragment;
	
	public Shader(){
		vertex = compileShader("res/Shaders/VERTEX.shd", GL20.GL_VERTEX_SHADER);
		fragment = compileShader("res/Shaders/FRAGMENT.shd", GL20.GL_FRAGMENT_SHADER);
		program = GL20.glCreateProgram();
		GL20.glAttachShader(program, vertex);
		GL20.glAttachShader(program, fragment);
		callAttribs();
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		callUniforms();
	}
	
	public void callStart(){
		GL20.glUseProgram(program);
	}
	
	public void callStop(){
		GL20.glUseProgram(0);
	}
	
	public void callExit(){
		callStop();
		GL20.glDetachShader(program, vertex);
		GL20.glDetachShader(program, fragment);
		GL20.glDeleteShader(vertex);
		GL20.glDeleteShader(fragment);
		GL20.glDeleteProgram(program);
	}
	
	protected abstract void callAttribs();
	
	protected abstract void callUniforms();
	
	protected int callUniformLocation(String loc){
		return GL20.glGetUniformLocation(program, loc);
	}
	
	protected void callBind(int attr, String name){
		GL20.glBindAttribLocation(program, attr, name);
	}
	
	protected void callBindFloat(float data, int loc){
		GL20.glUniform1f(loc, data);
	}
	
	protected void callBindInt(int data, int loc){
		GL20.glUniform1i(loc, data);
	}

	protected void callBindVector(Vector2f data, int loc){
		GL20.glUniform2f(loc, data.x, data.y);
	}
	
	protected void callBindVector(Vector3f data, int loc){
		GL20.glUniform3f(loc, data.x, data.y, data.z);
	}
	
	protected void callBindBoolean(boolean data, int loc){
		if(data){
			GL20.glUniform1i(loc, 1);
		}else{
			GL20.glUniform1i(loc, 0);
		}
	}
	
	protected void callBindMatrix(Matrix4f data, int loc){
		data.store(matBuf);
		matBuf.flip();
		GL20.glUniformMatrix4(loc, false, matBuf);
	}
	
	@SuppressWarnings("deprecation")
	private static int compileShader(String location, int type){
		StringBuilder src = new StringBuilder();
		try{
			BufferedReader dat = new BufferedReader(new FileReader(location));
			String line;
			while((line = dat.readLine()) != null){
				src.append(line).append("\n");
			}
			dat.close();
		}catch(IOException e){
			Console.printerr("Could not load Shader: " + location + "!");
			Console.printerr(e.getMessage());
			return 0;
		}
		int ret = 0;
		int SID = GL20.glCreateShader(type);
		GL20.glShaderSource(SID, src);
		GL20.glCompileShader(SID);
		if(GL20.glGetShader(SID, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE){
			ret = SID;
		}else{
			Console.printerr("Couldn't compile shader: " + location + "!");
			Console.printerr(GL20.glGetShaderInfoLog(SID, 750));
			return 0;
		}
		return ret;
	}
}
