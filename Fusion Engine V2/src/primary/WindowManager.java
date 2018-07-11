package primary;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import utility.ConfigManager;
import utility.Console;
import utility.FrameCounter;
import utility.VersionManager;

public class WindowManager {
	private static final int WIDTH = 800, HEIGHT = 600;
	private static final String TITLE = ConfigManager.getString("DisplayTitle") + " | Version: " + VersionManager.getVersion();
	private static final int MAX_FPS = ConfigManager.getInt("MaxFrameRate");
	private static boolean canRun = true;
	private static boolean isFS = ConfigManager.getBoolean("TrueFullScreen");
	private static long lFT;
	private static float dta;
	
	public static void callStart(){
		callStartAttribs();
		ContextAttribs attr = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.create(new PixelFormat().withSamples(ConfigManager.getInt("Samples")).withDepthBits(24), attr);
			Display.setTitle(TITLE);
			if(isFS){
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
				Console.println("Window is now Fullscreen!");
			}else{
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			}
			Display.setVSyncEnabled(ConfigManager.getBoolean("VSync"));
			Display.setResizable(true);
			if(ConfigManager.getBoolean("Multisample")){
				GL11.glEnable(GL13.GL_MULTISAMPLE);
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void callUpdate(){
		Display.sync(MAX_FPS);
		Display.update();
		canRun = !Display.isCloseRequested();
		if(Keyboard.isKeyDown(Keyboard.KEY_F11)){
			changeDisplayMode();
		}
		FrameCounter.update();
		Display.setTitle(TITLE + " | FPS: " + FrameCounter.getFPS());
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		long cFT = getCurrentTime();
		dta = (float) ((cFT - lFT) / 1000.0);
		lFT = cFT;
	}
	
	private static void changeDisplayMode(){
		if(isFS){
			try {
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			Console.println("No Longer Fullscreen!");
			isFS = false;
		}else{
			try {
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			Console.println("Now Fullscreen!");
			isFS = true;
		}
	}
	
	public static void callExit(){
		Display.destroy();
	}
	
	public static boolean canRun(){
		return canRun;
	}
	
	public static float getUpdateTime(){
		return dta;
	}
	
	private static long getCurrentTime(){
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static float getAspectRatio(){
		return Display.getWidth() / Display.getHeight();
	}
	
	private static void callStartAttribs(){
		Console.println("Starting Engine...");
		Console.println("CPU Cores Available: " + Runtime.getRuntime().availableProcessors());
		Console.println("Total RAM (BYTES): " + Runtime.getRuntime().totalMemory());
		Console.println("Available RAM (BYTES): " + Runtime.getRuntime().freeMemory());
		Console.println("Maximum RAM (BYTES): " + Runtime.getRuntime().maxMemory());
		Console.println("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
		Console.println("Java Version: " + System.getProperty("java.version"));
		Console.println("Heya " + System.getProperty("user.name") + ", Enjoy " + ConfigManager.getString("DisplayTitle") + "!");
	}
}
