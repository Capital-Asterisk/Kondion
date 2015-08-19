package vendalenger.kondion.collision;

import java.util.List;

public abstract class EntityCollider {
	protected List<EntityCollider> children;
	protected boolean collideMap = true;

	public abstract void move();
}
