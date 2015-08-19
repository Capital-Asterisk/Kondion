package poly2Tri;

import poly2Tri.splayTree.SplayTreeItem;

/**
 * base class for polygon boundary Linebase class is a directed line segment
 * with start/end point
 */
public class Linebase implements SplayTreeItem {

	/**
	 * two end points;
	 */
	protected Pointbase[] _endp = {null, null};

	/**
	 * Was unsigned int! helper of a line segemnt
	 */
	protected int _helper = -1;

	/**
	 * Was unsigned int! id of a line segment;
	 */
	protected int _id = -1;

	/**
	 * key of a line segment for splay tree searching
	 */
	protected double _key = 0;

	/**
	 * type of a line segement, input/insert Type...
	 */
	protected int _type = Poly2TriUtils.UNKNOWN;

	public Linebase() {
		for (int i = 0; i < 2; i++)
			_endp[i] = null;
		_id = 0;
	}

	public Linebase(Linebase line) {
		this._id = line._id;
		this._endp[0] = line._endp[0];
		this._endp[1] = line._endp[1];
		this._key = line._key;
		this._helper = line._helper;
	}

	public Linebase(Pointbase ep1, Pointbase ep2, int iType) {
		_endp[0] = ep1;
		_endp[1] = ep2;
		_id = ++Poly2TriUtils.l_id;
		_type = iType;
	}

	public Pointbase endPoint(int i) {
		return _endp[i];
	}

	public int helper() {
		return _helper;
	}

	public int id() {
		return _id;
	}

	/**
	 * slightly increased the key to avoid duplicated key for searching tree.
	 */
	@Override
	public void increaseKeyValue(double delta) {
		_key += delta;
	}

	@Override
	public Comparable keyValue() {
		return _key;
	}

	/**
	 * reverse a directed line segment; reversable only for inserted diagonals
	 */
	public void reverse() {
		assert (_type == Poly2TriUtils.INSERT);
		Pointbase tmp = _endp[0];
		_endp[0] = _endp[1];
		_endp[1] = tmp;
	}

	/**
	 * set and return helper of a directed line segment
	 */
	public void setHelper(int i) {
		_helper = i;
	}

	public void setKeyValue(double y) {
		if (_endp[1].y == _endp[0].y)
			_key = _endp[0].x < _endp[1].x ? _endp[0].x : _endp[1].x;
		else
			_key = (y - _endp[0].y) * (_endp[1].x - _endp[0].x)
					/ (_endp[1].y - _endp[0].y) + _endp[0].x;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Linebase(");
		sb.append("ID = " + _id);
		sb.append(", " + Poly2TriUtils.typeToString(_type));
		sb.append(", [");
		sb.append(_endp[0]);
		sb.append(", ");
		sb.append(_endp[1]);
		sb.append("], type = " + _type);
		sb.append(", keyValue =" + keyValue());
		return sb.toString();
	}

	public int type() {
		return _type;
	}

}
