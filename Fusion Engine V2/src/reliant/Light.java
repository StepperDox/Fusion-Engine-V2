package reliant;

import org.lwjgl.util.vector.Vector3f;

import primary.WindowManager;
import secondary.Prefab;

public class Light extends Prefab{
	private Vector3f color = new Vector3f();
	private int intensity = 1;
	
	public Light(Vector3f color, Vector3f position, int intensity){
		this.color = color;
		this.intensity = intensity;
		super.setPosition(position);
	}
	
	public Light(Vector3f color, Vector3f position){
		this.color = color;
		super.setPosition(position);
	}
	
	public Light(Vector3f color, int intensity){
		this.color = color;
		this.intensity = intensity;
	}
	
	public Light(Vector3f color){
		this.color = color;
	}
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
	public void setIntensity(int intensity){
		this.intensity = intensity;
	}
	
	public void iterateColor(Vector3f target, float speed){
		while(color != target){
			if(color.x < target.x){
				color.x += (speed * WindowManager.getUpdateTime());
				if(color.x < (target.x + 0.3) && color.x > (target.x - 0.3)){
					color.x = target.x;
				}
			}
			if(color.y < target.y){
				color.y += (speed * WindowManager.getUpdateTime());
				if(color.y < (target.y + 0.3) && color.y > (target.y - 0.3)){
					color.y = target.y;
				}
			}
			if(color.z < target.z){
				color.z += (speed * WindowManager.getUpdateTime());
				if(color.z < (target.z + 0.3) && color.z > (target.z - 0.3)){
					color.z = target.z;
				}
			}
			if(color.x > target.x){
				color.x -= (speed * WindowManager.getUpdateTime());
				if(color.x < (target.x + 0.3) && color.x > (target.x - 0.3)){
					color.x = target.x;
				}
			}
			if(color.y > target.y){
				color.y -= (speed * WindowManager.getUpdateTime());
				if(color.y < (target.y + 0.3) && color.y > (target.y - 0.3)){
					color.y = target.y;
				}
			}
			if(color.z > target.z){
				color.z -= (speed * WindowManager.getUpdateTime());
				if(color.z < (target.z + 0.3) && color.z > (target.z - 0.3)){
					color.z = target.z;
				}
			}
		}
	}
	
	public Vector3f getColor(){
		Vector3f col = new Vector3f();
		col.x = color.x * intensity;
		col.y = color.y * intensity;
		col.z = color.z * intensity;
		return col;
	}
	
	public int getIntensity(){
		return intensity;
	}
}
