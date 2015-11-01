package vendalenger.kondion.scene;

public class MapCollider {
	
	public int priority;

	public byte shape;
	public boolean solid;
	public float up, dn, no, ea, so, we;
	public float x, y, z;

	// {0 PointA-X, 1 PointA-Y, 2 PointB-X, 3 PointB-Y,
	// 4 A-Top, 5 A-Bottom, 6 B-Top, 7 B-Bottom,
	// 8 unit normx, 9 unit normy}
	public float[][] walls;
	
	// {x, y, width, height}
	// North, East, South, West, Floor, Ceil
	public float[][][] rectHoles;
	
	// {x1, y1, x2, y2, x3...}
	public float[][] polyHoles;
}