package shaders;

import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import reliant.Camera;
import reliant.Light;
import utility.MathBoi;

public class ModelShader extends Shader{
	private final int LIGHTS = 128;
	private int lCount;
	private int glow;
	private int tMat;
	private int pMat;
	private int vMat;
	private int diff;
	private int ref;
	private int ufl;
	private int sCol;
	private int nor;
	private int off;
	private int lPos[];
	private int lCol[];
	
	public ModelShader(){
		super();
	}

	@Override
	public void callAttribs() {
		super.callBind(0, "pos");
		super.callBind(1, "tex");
		super.callBind(2, "norm");
	}

	@Override
	public void callUniforms() {
		lCount = super.callUniformLocation("lCount");
		glow = super.callUniformLocation("glowing");
		tMat = super.callUniformLocation("tMat");
		pMat = super.callUniformLocation("pMat");
		vMat = super.callUniformLocation("vMat");
		diff = super.callUniformLocation("diffuse");
		ref = super.callUniformLocation("reflection");
		ufl = super.callUniformLocation("fakelight");
		sCol = super.callUniformLocation("sky");
		nor = super.callUniformLocation("rows");
		off = super.callUniformLocation("offset");
		
		lPos = new int[LIGHTS];
		lCol = new int[LIGHTS];
		
		for(int i = 0; i < LIGHTS; i++){
			lPos[i] = super.callUniformLocation("lightP[" + i + "]");
			lCol[i] = super.callUniformLocation("lightC[" + i + "]");
		}
	}

	public void setGlowing(boolean glow){
		super.callBindBoolean(glow, this.glow);
	}
	
	public void setRows(int rows){
		super.callBindFloat(rows, nor);
	}
	
	public void setOffset(Vector2f off){
		super.callBindVector(off, this.off);
	}
	
	public void setIlluminated(boolean light){
		super.callBindBoolean(light, ufl);
	}
	
	public void setReflectivity(float reflect){
		super.callBindFloat(reflect, ref);
	}
	
	public void setDiffuse(float diffuse){
		super.callBindFloat(diffuse, diff);
	}
	
	public void setSkyColor(Vector3f sky){
		super.callBindVector(sky, sCol);
	}
	
	public void setLights(List<Light> lights){
		for(int i = 0; i < LIGHTS; i++){
			if(i < lights.size()){
				super.callBindVector(lights.get(i).getPosition(), lPos[i]);
				super.callBindVector(lights.get(i).getColor(), lCol[i]);
			}else{
				super.callBindVector(new Vector3f(0, 0, 0),  lPos[i]);
				super.callBindVector(new Vector3f(0, 0, 0),  lCol[i]);
			}
		}
		super.callBindInt(lights.size(), lCount);
	}
	
	public void setTransformMatrix(Matrix4f transform){
		super.callBindMatrix(transform, tMat);
	}
	
	public void setProjectionMatrix(Matrix4f project){
		super.callBindMatrix(project, pMat);
	}
	
	public void setViewMatrix(Camera camera){
		super.callBindMatrix(MathBoi.calcVMat(camera), vMat);
	}
}
