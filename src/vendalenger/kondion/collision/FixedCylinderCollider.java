package vendalenger.kondion.collision;

public class FixedCylinderCollider extends EntityCollider {

	public byte collisionAmt;
	public float collisions[][];
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
		collisionAmt = 0;
		// Unit x, y, z, distance
		// Max of 8 collisions
		// listed the lazy way
		collisions = new float[][] {
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0},
				new float[] {0, 0, 0, 0}
		};
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

}
