/* KONDION TEST
 * THE KONDION masterscript.js
 * Contains everything about KONDION TEST
 * 
 * 
 * hey paolo.
 */

var init = function() {
	KJS.java.issueCommand("^eggs");
};

var start = function() {
	KJS.g.setMouseGrab(true);
	var player = KJS.e.spawnEnt("mv_player", {});
	player.mouseRotate = true;
	//KJS.c.freeCam(true);
	KJS.c.bindCam(player.obj);
	KJS.kondion.getCurrentCamera().lockRotation(player.obj.getRotation());
	/*KJS.s.newAABlockCS(new Vector3f(0, 0, 0), true, 2, 7, 1, 8, 8, 8, 8);
	KJS.s.newAABlockCS(new Vector3f(0, 2.5, 0), true, 2, 2, 2, 500, 2, 2, 2);
	KJS.s.newAABlockCS(new Vector3f(0, 0, 0), true, 2, 1, 21, 2, 2, 2, 2);
	KJS.s.newAABlockCS(new Vector3f(-1, 0, 0), true, 2, 4, 0.5, 12, 4, 16, 4);
	KJS.s.newAABlockCS(new Vector3f(-2, 4, 20), true, 2, 39, 15, 8, 8, 8, 8);
	KJS.s.newAABlockCS(new Vector3f(0, -20, 1), true, 2, 1, 3, 8, 8, 8, 8);*/
	
	/*KJS.s.newAABlockCS(new Vector3f(0, 2, 0), true, 2, 9, 9, 9, 9, 9, 9);
	KJS.s.newAABlockCS(new Vector3f(1, 5, 0), true, 2, 2, 1, 20, 2, 20, 2);
	KJS.s.newAABlockCS(new Vector3f(-1, 0, 0), true, 2, 2, 1, 20, 2, 20, 2);
	KJS.s.newAABlockCS(new Vector3f(0, -5, 1), true, 2, 2, 1, 20, 2, 20, 2);
	KJS.s.newAABlockCS(new Vector3f(0, 2, 1), true, 2, 2, 1, 2, 20, 2, 20);
	KJS.s.newAABlockCS(new Vector3f(0, -5, 5), true, 2, 2, 1, 2, 20, 2, 20);*/

	for (var i = 0; i < 11; i ++) {
		KJS.s.newAABlockCS(new Vector3f(Math.floor(Math.random() * 33 - 15), Math.floor(Math.random() * 33 - 15), Math.floor(Math.random() * 33 - 15)),
			true, 2, 2, Math.random() * 33, Math.random() * 33, Math.random() * 33, Math.random() * 33, Math.random() * 33);
	}
	
	KJS.kondion.getCurrentScene().doGlBuffers();
};

KJS.e.rEnt({
	id: "mv_player",
	name: "You",
	traits: ["et_alive", "ph_gravity"],
	tickInterval: 1,
	jumpDeb: false,
	create: function() {
		this.obj.setGravity(0.4);
	},
	notify: function(msg) {
		
	},
	tick: function() {
		if (KJS.i.buttonDown(KJS.b.open)) {
			if (!jumpDeb) {
				this.obj.getVelocity().y += 1;
				jumpDeb = false;
			}
		} else
			jumpDeb = false
		if (KJS.i.buttonDown(KJS.b.up)) {
			this.obj.thrustYAngle(this.obj.getRotation().x - Math.PI, 0.5, 10);
		} else if (KJS.i.buttonDown(KJS.b.down)) {
			this.obj.thrustYAngle(this.obj.getRotation().x, 0.5, 10);
		} else if (KJS.i.buttonDown(KJS.b.left)) {
			this.obj.thrustYAngle(this.obj.getRotation().x + Math.PI / 2, 0.5, 10);
		} else if (KJS.i.buttonDown(KJS.b.right)) {
			this.obj.thrustYAngle(this.obj.getRotation().x - Math.PI / 2, 0.5, 10);
		} else {
			this.obj.velocity.set(this.obj.velocity.x * 0.8, this.obj.velocity.y, this.obj.velocity.z * 0.8);
		}
	}
});