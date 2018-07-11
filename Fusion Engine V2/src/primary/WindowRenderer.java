package primary;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import reliant.Camera;
import reliant.Light;
import secondary.Prefab;
import shaders.ModelShader;

public class WindowRenderer {
	private static final float R = 0.45f;
	private static final float G = 0.63f;
	private static final float B = 0.63f;
	private Camera cam;
	private ModelShader shader = new ModelShader();
	private ModelRenderer mRender;

	private List<Prefab> prefabs = new ArrayList<Prefab>();
	private List<Light> lights = new ArrayList<Light>();
	
	public WindowRenderer(Camera cam){
		this.cam = cam;
		mRender = new ModelRenderer(shader, cam);
	}
	
	private void callClear(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(R, G, B, 1);
	}
	
	public void callUpdate(){
		cam.callUpdate();
		callClear();
		shader.callStart();
		shader.setSkyColor(new Vector3f(R, G, B));
		shader.setLights(lights);
		shader.setViewMatrix(cam);
		mRender.callUpdate(prefabs);
		shader.callStop();
	}
	
	public static void callStopCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public static void callStartCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void callExit(){
		shader.callExit();
		prefabs.clear();
		lights.clear();
	}
	
	public void addLight(Light l){
		lights.add(l);
	}
	
	public void addPrefab(Prefab p){
		prefabs.add(p);
	}

	public void addLightCollection(List<Light> lColl){
		lights.addAll(lColl);
	}
	
	public void addPrefabCollection(List<Prefab> pColl){
		prefabs.addAll(pColl);
	}
}
