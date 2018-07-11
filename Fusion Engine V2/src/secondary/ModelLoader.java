package secondary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import primary.DataManager;
import utility.Console;

public class ModelLoader {
	
	public static Prefab loadModel(String model, String texture, DataManager datMan){
		FileReader fReader = null;
		try {
			fReader = new FileReader(new File("res/Objects/" + model + ".obj"));
		} catch (FileNotFoundException e) {
			Console.printerr("Could not load model: " + model + ".obj");
		}
		BufferedReader reader = new BufferedReader(fReader);
		String line;
		String name = "";
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] vArr = null;
		float[] tArr = null;
		float[] nArr = null;
		int[] iArr = null;
		try{
			while(true){
				line = reader.readLine();
				String[] cLine = line.split(" ");
				if(line.contains("o ")){
					name = cLine[1];
				}else if(line.contains("v ")){
					vertices.add(new Vector3f(Float.parseFloat(cLine[1]), Float.parseFloat(cLine[2]), Float.parseFloat(cLine[3])));
				}else if(line.contains("vt ")){
					textures.add(new Vector2f(Float.parseFloat(cLine[1]), Float.parseFloat(cLine[2])));
				}else if(line.contains("vn ")){
					normals.add(new Vector3f(Float.parseFloat(cLine[1]), Float.parseFloat(cLine[2]), Float.parseFloat(cLine[3])));
				}else if(line.contains("f ")){
					tArr = new float[vertices.size() * 2];
					nArr = new float[vertices.size() * 3];
					break;
				}
			}
			while(line != null){
				if(!line.startsWith("f")){
					line = reader.readLine();
					continue;
				}
				String[] cLine = line.split(" ");
				String[] vert0 = cLine[1].split("/");
				String[] vert1 = cLine[2].split("/");
				String[] vert2 = cLine[3].split("/");
				workInd(vert0, indices, textures, normals, tArr, nArr);
				workInd(vert1, indices, textures, normals, tArr, nArr);
				workInd(vert2, indices, textures, normals, tArr, nArr);
				line = reader.readLine();
			}
			reader.close();
		}catch(Exception e){
			Console.printerr("Caught an unknown Exception, cannot load model: " + model + ".obj");
		}
		vArr = new float[vertices.size() * 3];
		iArr = new int[indices.size()];
		int vp = 0;
		for(Vector3f v:vertices){
			vArr[vp++] = v.x;
			vArr[vp++] = v.y;
			vArr[vp++] = v.z;
		}
		for(int i = 0; i < indices.size(); i++){
			iArr[i] = indices.get(i);
		}
		Prefab p = new Prefab();
		p.setModel(datMan.createModel(name, texture, vArr, tArr, nArr, iArr));
		return p;
	}
	
	private static void workInd(String[] data, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] tArr, float[] nArr){
		int cVP = Integer.parseInt(data[0]) - 1;
		indices.add(cVP);
		Vector2f cT = textures.get(Integer.parseInt(data[1]) - 1);
		tArr[cVP * 2] = cT.x;
		tArr[cVP * 2 + 1] = 1 - cT.y;
		Vector3f cN = normals.get(Integer.parseInt(data[2]) - 1);
		nArr[cVP * 3] = cN.x;
		nArr[cVP * 3 + 1] = cN.y;
		nArr[cVP * 3 + 2] = cN.z;
	}
}
