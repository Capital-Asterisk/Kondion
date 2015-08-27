package vendalenger.kondion.scene;

public class MapCollider {
	public int priority;

	public byte shape;
	public boolean solid;
	public float up, dn, no, ea, so, we;
	public float x, y, z;

	// {0 PointA-X, 1 PointA-Y, 2 PointB-X, 3 PointB-Y,
	// 4 A-Top, 5 A-Bottom, 6 B-Top, 7 B-Bottom,
	// 8 unit normx, 9 unit normy,
	// 10 holex, 11 holey, 12 holewidth, 13 holeheight}
	public float[][] walls;
}