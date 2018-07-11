package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ConfigManager {
	private static HashMap<String, String> data = new HashMap<String, String>();
	private static String[] defData = {"Multisample@1", "Version@PRE-ALPHA_0_0_0_0", "Samples@4", "VSync@0", "MaxFrameRate@1000",
			"DisplayTitle@Fusion Engine V2 ", "Anisotropic@1", "aFilteringLevels@4", "FOV@70", "SHAD_DIST@1000", 
			"NEAR_PLANE@0.1", "FAR_PLANE@10000", "TrueFullScreen@0", "LOG@Using Rebuilt Config!", "Mipmapping@1", "MipmapBias@-0.1", "END"};
	private static String workingDIR;
	
	public static void callStart(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String oSys = System.getProperty("os.name").toUpperCase();
		if(oSys.contains("WIN")){
			workingDIR = System.getenv("AppData");
		}else{
			workingDIR = System.getProperty("user.home");
			workingDIR += "/Library/Application Support/";
		}
		Console.println("Set Working Directory to: " + workingDIR);
	}
	
	public static void callExit(){
		saveConfig();
	}
	
	public static void callBaseConfig(){
		FileReader fr = null;
		try {
			fr = new FileReader(new File(workingDIR + "/Fusion Engine/Core.fsn"));
		} catch (FileNotFoundException e) {
			Console.printerr("Couldn't Find Base Config, Building new...");
			buildNewBase();
			try {
				fr = new FileReader(new File(workingDIR + "/Fusion Engine/Core.fsn"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		try{
			while(true){
				line = reader.readLine();
				String[] cLine = line.split("@");
				if(line.startsWith("END")){
					break;
				}else if(line.startsWith("LOG")){
					Console.println("Config LOG: " + cLine[1]);
				}else{
					data.put(cLine[0], cLine[1]);
				}
			}
		}catch(Exception e){
			Console.printerr("Reached an Unknown Error!");
			e.printStackTrace();
		}
	}

	protected static void buildNewBase(){
		try {
			new File(workingDIR + "/Fusion Engine/").mkdirs();
			PrintWriter writer = new PrintWriter(workingDIR + "/Fusion Engine/Core.fsn", "UTF-8");
			for(String s:defData){
				writer.println(s);
			}
			writer.close();
			Console.println("Completed New Config Successfully!");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	protected static void saveConfig(){
		try {
			new File(workingDIR + "/Fusion Engine/").mkdirs();
			PrintWriter writer = new PrintWriter(workingDIR + "/Fusion Engine/Core.fsn", "UTF-8");
			for(String s:data.keySet()){
				writer.println(s + "@" + data.get(s));
			}
			writer.println("END");
			writer.close();
			Console.println("Saved Config Successfully!");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void callCustomConfig(String name){
		FileReader fr = null;
		try {
			fr = new FileReader(new File(workingDIR + "/Fusion Engine/" + name +".fsn"));
		} catch (FileNotFoundException e) {
			Console.printerr("Couldn't Find Custom Config! Skipping.");
			Console.printerr(e.getLocalizedMessage());
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		try{
			while(true){
				line = reader.readLine();
				String[] cLine = line.split("@");
				if(line.startsWith("END")){
					break;
				}else if(line.startsWith("LOG")){
					Console.println("Config LOG: " + cLine[1]);
				}else{
					data.put(cLine[0], cLine[1]);
				}
			}
		}catch(Exception e){
			Console.printerr("Reached an Unknown Error!");
			e.printStackTrace();
		}
	}
	
	public static void setData(String key, String newData){
		data.replace(key, newData);
	}
	
	public static void addData(String key, String addition){
		data.put(key, addition);
	}
	
	public static boolean getBoolean(String key){
		boolean ret = false;
		if(data.get(key).equalsIgnoreCase("0") || data.get(key).equalsIgnoreCase("false")){
			ret = false;
		}else{
			ret = true;
		}
		return ret;
	}
	
	public static int getInt(String key){
		return Integer.parseInt(data.get(key));
	}
	
	public static float getFloat(String key){
		return Float.parseFloat(data.get(key));
	}
	
	public static String getString(String key){
		return data.get(key);
	}
}
