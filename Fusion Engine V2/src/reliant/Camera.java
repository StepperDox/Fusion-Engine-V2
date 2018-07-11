package reliant;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import primary.WindowManager;
import secondary.Prefab;
import utility.ConfigManager;

public class Camera extends Prefab{
	private final float FOV = ConfigManager.getFloat("FOV");
	private final float NPLANE = ConfigManager.getFloat("NEAR_PLANE");
	private final float FPLANE = ConfigManager.getFloat("FAR_PLANE");
	private boolean isFree = false;
	private Matrix4f projection;
	
	public Camera(){
		//Upon creating a new Camera object, a new projection matrix is made.
		float aRat = WindowManager.getAspectRatio();
		float Y = (float)(1f / Math.tan(Math.toRadians(FOV / 2f)) * aRat);
		float X = Y / aRat;
		float frustum = FPLANE / NPLANE;
		projection = new Matrix4f();
		projection.m00 = X;
		projection.m11 = Y;
		projection.m22 = -((NPLANE + FPLANE) / frustum);
		projection.m23 = -1;
		projection.m32 = -((2 * NPLANE * FPLANE) / frustum);
		projection.m33 = 0;
	}
	
	public void callUpdate(){
		if(isFree){
			super.setRotation(new Vector3f(Mouse.getDX(), Mouse.getDY(), 0));
			Vector3f nPos = new Vector3f(0, 0, 0);
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				nPos.z += 15;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				nPos.z -= 15;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				nPos.x -= 15;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				nPos.x += 15;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				nPos.y += 5;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				nPos.y -= 5;
			}
			super.translateLocal(nPos);
		}
		Vector3f nRot = new Vector3f();
		nRot.x = (Mouse.getDX() * WindowManager.getUpdateTime());
		nRot.z = (Mouse.getDY() * WindowManager.getUpdateTime());
		super.setRotation(nRot);
	}
	
	public Matrix4f getProjectionMatrix(){
		return projection;
	}
}
