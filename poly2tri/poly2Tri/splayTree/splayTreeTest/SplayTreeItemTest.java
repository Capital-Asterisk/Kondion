package poly2Tri.splayTree.splayTreeTest;

import poly2Tri.splayTree.SplayTreeItem;

public class SplayTreeItemTest implements SplayTreeItem {

	public int data = 1;

	public SplayTreeItemTest(int a) {
		data = a;
	}

	@Override
	public void increaseKeyValue(double delta) {
		data += 1;
	}

	@Override
	public Comparable keyValue() {
		return data;
	}

}
