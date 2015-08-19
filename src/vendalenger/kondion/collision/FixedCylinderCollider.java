package vendalenger.kondion.collision;

public class FixedCylinderCollider extends EntityCollider {

	public float radius;
	public float up;
	public float dn;

	/**
	 * Create a new Fixed Cylinder, diagram below
	 * 
	 * <pre>
	 * {@code
	 * 
	 * ◄────►
	 * Radius
	 *    █████      ▲
	 * ███     ███   │ Up
	 * █  █████  █   │
	 * █         █ Center
	 * █         █   │
	 * █         █   │
	 * ███     ███   │ Down
	 *    █████      ▼
	 * }
	 * </pre>
	 * 
	 * @param rad
	 *            Radius
	 * @param height
	 *            Up
	 * @param down
	 *            Down
	 * @see vendalenger.kondion.collision.EntityCollider
	 */
	public FixedCylinderCollider(float rad, float height, float down) {
		radius = rad;
		up = height;
		dn = down;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

}
