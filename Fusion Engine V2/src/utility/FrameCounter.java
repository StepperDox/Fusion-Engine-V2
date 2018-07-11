package utility;

import primary.WindowManager;

public class FrameCounter {
	private static int FPS;
	private static int holder;
	private static double timestamp;
	
	public static void update(){
		if(timestamp < 1){
			holder++;
			timestamp += WindowManager.getUpdateTime();
		}else{
			FPS = holder;
			holder = 0;
			timestamp = 0;
		}
	}
	
	public static int getFPS(){
		return FPS;
	}
}
