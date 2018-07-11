package utility;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import reliant.Camera;

public class MathBoi {
	
	public static Matrix4f calcTMat(Vector2f t, Vector2f scale){
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		Matrix4f.translate(t, mat, mat);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), mat, mat);
		return mat;
	}
	
	public static Matrix4f calcTMat(Vector3f t, Vector3f r, float scale){
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		Matrix4f.translate(t, mat, mat);
		Matrix4f.rotate((float)Math.toRadians(r.x), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(r.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(r.z), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.scale(new Vector3f(scale, scale, scale), mat, mat);
		return mat;
	}
	
	public static Matrix4f calcVMat(Camera c){
		Matrix4f vMat = new Matrix4f();
		vMat.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(c.getRotation().x), new Vector3f(1, 0, 0), vMat, vMat);
		Matrix4f.rotate((float) Math.toRadians(c.getRotation().y), new Vector3f(0, 1, 0), vMat, vMat);
		Matrix4f.rotate((float) Math.toRadians(c.getRotation().z), new Vector3f(0, 0, 1), vMat, vMat);
		Vector3f nCPos = new Vector3f(-c.getPosition().x, -c.getPosition().y, -c.getPosition().z);
		Matrix4f.translate(nCPos, vMat, vMat);
		return vMat;
	}
	
	public static List<Integer> buildIndices(int vertCount){
		List<Integer> indices = new ArrayList<Integer>();
		int pointer = 0;
		for(int i = 0; i < (vertCount / 3); i++){
			if(i < 3){
				indices.add(pointer);
				indices.add(pointer += 1);
				indices.add(pointer += 1);
			}
			indices.add(pointer);
			indices.add(pointer -= 1);
			indices.add(pointer += 2);
		}
		return (List<Integer>) indices;
	}
}
