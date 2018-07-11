package loop;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import primary.DataManager;
import primary.WindowManager;
import primary.WindowRenderer;
import reliant.Camera;
import reliant.Light;
import secondary.ModelLoader;
import secondary.Prefab;
//import sound.Listener;
//import sound.SoundEngine;
import utility.ConfigManager;
import utility.Console;
import utility.VersionManager;

public class Main {
	public static void main(String[] args) {
		//Initialize Everything Once Here.
		Console.callStart();
		ConfigManager.callStart();
		ConfigManager.callBaseConfig();
		VersionManager.callFix();
		WindowManager.callStart();
		Camera cam = new Camera();
		DataManager datman = new DataManager();
		WindowRenderer ren = new WindowRenderer(cam);
		Light main = new Light(new Vector3f(1, 0, 0));
		ren.addLight(main);
		
		Random r = new Random();
		for(int i = 0; i < 100; i++){
			Prefab test = ModelLoader.loadModel("box", "download", datman);
			test.getModel().setScale(1);
			test.setPosition(new Vector3f((r.nextFloat() * 500), (r.nextFloat() * 500), (r.nextFloat() * 500)));
			test.setRotation(new Vector3f((r.nextFloat() * 500), (r.nextFloat() * 500), (r.nextFloat() * 500)));
			ren.addPrefab(test);
			Console.callUpdate();
		}
		
		
		while(WindowManager.canRun()){
			//Update Everything Here.
			ren.callUpdate();
			Console.callUpdate();
			WindowManager.callUpdate();
		}
		//Close Everything Once Here.
		ConfigManager.callExit();
		datman.callExit();
		ren.callExit();
		
		WindowManager.callExit();
	}
}
