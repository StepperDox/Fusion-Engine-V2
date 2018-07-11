package secondary;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import primary.WindowManager;
import utility.Console;

public class Prefab {
	public boolean mmb = false;
	private Model self = null;
	private List<Component> comp = new ArrayList<Component>();
	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f rot = new Vector3f(0, 0, 0);
	
	public void callUpdate(){
		if(mmb){
			Console.println("Object Details; Name: " + self.getName() + " Reflectivity: " + self.getShine() + " Vertices: " + self.getvCount());
			mmb = false;
		}
		for(Component c:comp){
			c.callUpdate();
		}
	}
	
	public void translateLocal(Vector3f speed){
		Vector3f d = new Vector3f(speed.x * WindowManager.getUpdateTime(), speed.y * WindowManager.getUpdateTime(), speed.z * WindowManager.getUpdateTime());
		d.x = (float)(d.x * Math.sin(Math.toRadians(rot.y)));
		d.z = (float)(d.z * Math.cos(Math.toRadians(rot.y)));
		pos.x += d.x;
		pos.z += d.z;
		pos.y += d.y;
	}
	
	public void setPosition(Vector3f position){
		pos = position;
	}
	
	public void setRotation(Vector3f rotation){
		rot = rotation;
	}
	
	public void setModel(Model m){
		self = m;
	}
	
	public Model getModel(){
		return self;
	}
	
	public void addComponent(Component c){
		comp.add(c);
	}
	
	public void removeComponent(Component c){
		comp.remove(c);
	}
	
	public void removeComponent(int c){
		comp.remove(c);
	}
	
	public void callComponentKill(){
		for(Component c:comp){
			c.callExit();
		}
		comp.clear();
	}
	
	public Vector3f getPosition(){
		return pos;
	}
	
	public Vector3f getRotation(){
		return rot;
	}
}
