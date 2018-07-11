package primary;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.util.vector.Matrix4f;
import reliant.Camera;
import secondary.Prefab;
import shaders.ModelShader;
import utility.Console;
import utility.MathBoi;

public class ModelRenderer {
	private ModelShader shader;

	public ModelRenderer(ModelShader shader, Camera cam) {
		this.shader = shader;
		shader.callStart();
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		shader.callStop();
	}

	public void callUpdate(List<Prefab> prefabs) {
		if (!prefabs.isEmpty()) {
			for (Prefab pref : prefabs) {
				GL30.glBindVertexArray(pref.getModel().getvID());
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				GL20.glEnableVertexAttribArray(2);
				shader.setRows(pref.getModel().getNOR());
				if (pref.getModel().isTransparent()) {
					WindowRenderer.callStopCulling();
				}
				shader.setIlluminated(pref.getModel().hasFakeLight());
				shader.setDiffuse(pref.getModel().getDiffuse());
				shader.setReflectivity(pref.getModel().getShine());
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, pref.getModel().gettID());
				Matrix4f tMat = MathBoi.calcTMat(pref.getPosition(), pref.getRotation(), pref.getModel().getScale());
				shader.setTransformMatrix(tMat);
				shader.setOffset(pref.getModel().getOffset());
				try{
					GL11.glDrawElements(GL11.GL_TRIANGLES, pref.getModel().getvCount(), GL11.GL_UNSIGNED_INT, 0);
				}catch(OpenGLException e){
					Console.printerr(e.getLocalizedMessage());
					callRemove();
				}
				callRemove();
				Console.println("Rendered Prefab: " + pref.toString());
			}
		}else{
			Console.printerr("No Prefabs to Render!");
		}
	}

	private void callRemove() {
		WindowRenderer.callStartCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}
