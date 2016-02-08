/* KONDION TEST
 * THE KONDION masterscript.js
 * Contains everything about KONDION TEST
 * 
 * 
 * hey paolo.
 */
 
var init = function() {
	KJS.issueCommand("^eggs");
	//^kdion.js (scene.addRenderable*(new KJS.r.Board*(*)*))
	
};

var start = function() {

	//KJS.g.setMouseGrab(true);
	
	// Create a new render layer (GKO: Game Kondion Object)
	World.passes.add(new GKO_RenderPass());

	SCN.Camera = new OKO_Camera_();
	SCN.Camera.look(0, 0, 5, 0, 0, 0);
	SCN.Camera.setFreeMode(true);
	
	SCN.Ground = new SKO_InfinitePlane();
	SCN.rounGd = new SKO_InfinitePlane();
	
	//SCN.Apple.transform.translate(0, 2, 0);
	SCN.Ground.transform.rotateX(-Math.PI / 2);
	SCN.Ground.textureSize = 1;
	
	SCN.rounGd.transform.rotateX(Math.PI / 2);
	SCN.rounGd.textureSize = 1;
	SCN.rounGd.transform.translate(0, 0, -30);
	
	//World.Layers[0].Apple = SCN.Apple;
	World.camera = SCN.Camera;
	
	for (var i = 0; i < 666; i++) {
		var cube = new SKO_Cube();
		cube.rotVelocity.rotateX(0.01);
		cube.rotVelocity.rotateY(0.01);
		cube.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
		cube.transform.rotateX(Math.random() * Math.PI * 2);
		cube.transform.rotateY(Math.random() * Math.PI * 2);
		cube.transform.rotateZ(Math.random() * Math.PI * 2);
		cube.s = {
			onupdate: function() {
				//this.obj.transform.translate(0.01, 0, 0);
				if (this.obj.transform.m31 < 0)
					this.obj.transform.m31 = 0;
				if (this.obj.transform.m31 > 30)
					this.obj.transform.m31 = 30;
				this.obj.transform.translate(0.2, 0, 0);
				this.obj.rotVelocity.rotateX((Math.random() - 0.5) / 20);
				this.obj.rotVelocity.rotateY((Math.random() - 0.5) / 20);
				this.obj.rotVelocity.rotateZ((Math.random() - 0.5) / 20);
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('k'))) {
					this.obj.rotVelocity.rotateZ(0.008);
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('i'))) {
					this.obj.rotVelocity.rotateZ(-0.008);
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('j'))) {
					this.obj.rotVelocity.rotateX(-0.008);
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('l'))) {
					this.obj.rotVelocity.rotateX(0.008);
				}
			}
		}
		SCN["CUBE_" + i] = cube;
	}
	
	SCN.Camera.s = {
		onupdate: function() {
			//SCN.rounGd.rotateX(Math.sin(KJS.currentTick()) / 5);
			//SCN.Ground.rotateX(Math.sin(KJS.currentTick()) / 5);
			//SCN.Ground.transform.rotateZ(Math.PI / 1024);
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
				SCN.Camera.moveSpeed = 20;
				SCN.Camera.transform.translate(0, 0.02, 0);
			} else {
				SCN.Camera.moveSpeed = 8;
			}
		}
	}
	
	World.clearColor.set(1, 1, 1, 1);
};