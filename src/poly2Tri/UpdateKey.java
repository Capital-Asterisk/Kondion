package poly2Tri;

import poly2Tri.splayTree.BTreeNode;
import poly2Tri.splayTree.SplayTreeAction;

public class UpdateKey implements SplayTreeAction {

	@Override
	public void action(BTreeNode node, double y) {
		((Linebase) node.data()).setKeyValue(y);
	}

}
