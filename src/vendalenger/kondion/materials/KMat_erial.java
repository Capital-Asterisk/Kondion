package vendalenger.kondion.materials;

public interface KMat_erial {
	
	/**
	 * This is called before drawing the renderable.
	 * @return 0 for successful
	 */
	public int bind(int type);
	
	/**
	 * This is called
	 * @return 0 for successful
	 */
	public int prepare();
	
	/**
	 * This is called after drawing the renderable
	 * @return 0 for successful
	 */
	public int unbind();
	
	/**
	 * Override the fog, reset on bind();
	 */
	public void fogOverride(float fog);
	
}
