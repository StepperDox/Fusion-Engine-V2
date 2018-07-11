package primary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import secondary.Model;
import utility.ConfigManager;
import utility.Console;

public class DataManager {
	private static boolean aFilter = ConfigManager.getBoolean("Anisotropic");
	private static int aLevels = 4;
	private static List<Integer> VAO = new ArrayList<Integer>();
	private static List<Integer> VBO = new ArrayList<Integer>();
	private static List<Integer> TEX = new ArrayList<Integer>();
	
	public DataManager(){
		ByteBuffer[] ico = new ByteBuffer[3];
		try {
			ico[0] = new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/Textures/16x16 Icon.png")), false, false, null);
			ico[1] = new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/Textures/32x32 Icon.png")), false, false, null);
			ico[2] = new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/Textures/64x64 Icon.png")), false, false, null);
		} catch (IOException e) {
			Console.printerr("Could not load Image Icons! Defaulting...");
		}finally{
			Display.setIcon(ico);
		}
	}
	
	public Model createModel(String name, String texture, float[] vertices, float[] textures, float[] normals, int[] indices){
		int vao = makeVAO();
		bindInd(indices);
		makeAttrList(0, 3, vertices);
		makeAttrList(1, 2, textures);
		makeAttrList(2, 3, normals);
		removeVAO();
		Model m = new Model(vao, vertices.length, vertices, textures, normals, indices);
		if(texture != null){
			try{
				ByteBuffer buf = null;
				FileInputStream in = new FileInputStream("res/Textures/" + texture + ".png");
				PNGDecoder dec = new PNGDecoder(in);
				m.setWidth(dec.getWidth());
				m.setHeight(dec.getHeight());
				buf = ByteBuffer.allocateDirect(4 * dec.getWidth() * dec.getHeight());
				dec.decode(buf, 4 * dec.getWidth(), Format.RGBA);
				buf.flip();
				in.close();
				m.setBuffer(buf);
			}catch(Exception e){
				Console.printerr("Could not decode texture: " + texture);
			}
		}else{
			try{
				ByteBuffer buf = null;
				FileInputStream in = new FileInputStream("res/Textures/white.png");
				PNGDecoder dec = new PNGDecoder(in);
				m.setWidth(dec.getWidth());
				m.setHeight(dec.getHeight());
				buf = ByteBuffer.allocateDirect(4 * dec.getWidth() * dec.getHeight());
				dec.decode(buf, 4 * dec.getWidth(), Format.RGBA);
				buf.flip();
				in.close();
				m.setBuffer(buf);
			}catch(Exception e){
				Console.printerr("Could not load fallback texture!");
			}
		}
		Console.println("Created new Model!");
		return m;
	}
	
	private static int makeTexture(String file){
		Texture t = null;
		try {
			t = TextureLoader.getTexture("PNG", new FileInputStream("src/Textures/" + file + ".png"));
			if(ConfigManager.getBoolean("Mipmapping")){
				GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, ConfigManager.getFloat("MipmapBias"));
			}
			if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic && aFilter){
				float levels = Math.min(aLevels, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, levels);
			}else if(!aFilter){
				Console.println("Anisotropic Filtering is disabled.");
			}else{
				Console.printerr("Anisotropic Filtering is not supported! Disabling...");
				ConfigManager.setData("Anisotropic", "0");
			}
		} catch (FileNotFoundException e) {
			Console.printerr("Couldn't find texture file: " + file);
			Console.printerr("Defaulting to blank!");
			redoTexture();
		} catch (IOException e) {
			Console.printerr("Couldn't use texture file: " + file);
			Console.printerr("Defaulting to blank!");
			redoTexture();
		}
		int tid = t.getTextureID();
		TEX.add(tid);
		return tid;
	}
	
	private static void redoTexture(){
		makeTexture("white");
	}
	
	private static int makeVAO(){
		int vao = GL15.glGenBuffers();
		VAO.add(vao);
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	private static void makeAttrList(int aNumb, int cSize, float[] data){
		int vbo = GL15.glGenBuffers();
		VBO.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buf = makeFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(aNumb, cSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static IntBuffer makeIntBuffer(int[] data){
		IntBuffer b = BufferUtils.createIntBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
	
	private static FloatBuffer makeFloatBuffer(float[] data){
		FloatBuffer b = BufferUtils.createFloatBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
	
	private static void bindInd(int[] data){
		int vbo = GL15.glGenBuffers();
		VBO.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buf = makeIntBuffer(data);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
	}
	
	private static void removeVAO(){
		GL30.glBindVertexArray(0);
	}
	
	public static void setAnisotropic(boolean data, int levels){
		aFilter = data;
		ConfigManager.setData("Anisotropic", String.valueOf(data));
		aLevels = levels;
		ConfigManager.setData("aFilteringLevels", String.valueOf(levels));
	}
	
	public void callExit(){
		for(int v:VAO){
			GL30.glDeleteVertexArrays(v);
		}
		for(int v:VBO){
			GL15.glDeleteBuffers(v);
		}
		for(int t:TEX){
			GL11.glDeleteTextures(t);
		}
	}
}
