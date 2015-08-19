package vendalenger.kondion.scene;

public class MapCollider {
	public int priority;

	public byte shape;
	public boolean solid;
	public float up, dn, no, ea, so, we;
	public float x, y, z;

	// {PointA-X, PointA-Y, PointB-X, PointB-Y,
	// A-Top, A-Bottom, B-Top, B-Bottom,
	// unit normx, unit normy,
	// holex, holey, holewidth, holeheight}
	public float[][] walls;
}