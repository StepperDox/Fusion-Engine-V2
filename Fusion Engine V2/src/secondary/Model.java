package secondary;

import java.nio.ByteBuffer;
import org.lwjgl.util.vector.Vector2f;

public class Model {
	private String name;
	private int vID;
	private int tID;
	private int vCount;
	private float[] vertices;
	private float[] textures;
	private float[] normals;
	private float scale = 1;
	private int[] indices;
	private float diffuse = 1;
	private float shine = 0;
	private boolean transparent = false;
	private boolean fakeLight = false;
	private int NOR = 1;
	private Vector2f offset = new Vector2f(0, 0);
	
	private int width, height;
	private ByteBuffer buffer;
	
	public Model(int vID, int vCount, float[] vertices, float[] textures, float[] normals, int[] indices){
		this.vID = vID;
		this.vCount = vCount;
		this.vertices = vertices;
		this.textures = textures;
		this.normals = normals;
		this.indices = indices;
	}
	
	public String getName() {
		return name;
	}
	
	public int getvID() {
		return vID;
	}
	
	public int gettID() {
		return tID;
	}
	
	public int getvCount() {
		return vCount;
	}
	
	public float[] getVertices() {
		return vertices;
	}
	
	public float[] getTextures() {
		return textures;
	}
	
	public float[] getNormals() {
		return normals;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float getDiffuse() {
		return diffuse;
	}
	
	public void setDiffuse(float diffuse) {
		this.diffuse = diffuse;
	}
	
	public float getShine() {
		return shine;
	}
	
	public void setShine(float shine) {
		this.shine = shine;
	}
	
	public boolean isTransparent() {
		return transparent;
	}
	
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
	
	public boolean hasFakeLight() {
		return fakeLight;
	}
	
	public void setFakeLight(boolean fakeLight) {
		this.fakeLight = fakeLight;
	}
	
	public int getNOR() {
		return NOR;
	}
	
	public void setNOR(int nOR) {
		NOR = nOR;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public Vector2f getOffset(){
		return offset;
	}
	
	public void setOffset(Vector2f offset){
		this.offset = offset;
	}
	
	public float getScale(){
		return scale;
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
}
