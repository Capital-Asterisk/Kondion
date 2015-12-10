package vendalenger.kondion.materials;

public interface KMat_erial {
	
	/**
	 * This is called before drawing the renderable.
	 * @return 0 for successful
	 */
	public int bind();
	
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
	
}
