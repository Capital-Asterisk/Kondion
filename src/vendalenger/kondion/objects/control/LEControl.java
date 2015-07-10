package vendalenger.kondion.objects.control;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.objects.LivingEntity;

public abstract class LEControl {
	public abstract void tick(LivingEntity ent, ScriptObjectMirror mirror);
}
