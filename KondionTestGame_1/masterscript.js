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
	
	SCN.Ground = new SKO_InfinitePlane();
	SCN.rounGd = new SKO_InfinitePlane();
	
	//SCN.Apple.transform.translate(0, 2, 0);
	SCN.Ground.transform.rotateX(-Math.PI / 2);
	SCN.Ground.textureSize = 1;
	
	SCN.rounGd.transform.rotateX(Math.PI / 2);
	SCN.rounGd.textureSize = 1;
	SCN.rounGd.transform.translate(0, 0, -200);
	
	//World.Layers[0].Apple = SCN.Apple;
	
	var plane = new SKO_Cube();
	//plane.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
	//plane.transform.rotateX(Math.random() * Math.PI * 2);
	//plane.transform.rotateY(Math.random() * Math.PI * 2);
	//plane.transform.rotateZ(Math.random() * Math.PI * 2);
	camera = new OKO_Camera_();
	camera.transform.translate(0, 0, 0);
	camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.s = {
		speed: 0,
		onupdate: function() {
			//this.obj.transform.translate(0.01, 0, 0);
			//this.obj.morecube.transform.translate(0, Math.sin(KJS.currentTick() / 20) / 10, 0);
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
			camera.moveSpeed = 20;
			} else {
			camera.moveSpeed = 8;
			}
			this.speed += KJS.i.keyboardDown(KJS.i.toGLFWCode('q')) ? 0 : 0.009;
			this.speed -= KJS.i.keyboardDown(KJS.i.toGLFWCode('e')) ? 0 : 0.009;
			//this.obj.rotVelocity.w *= 1.07;
			//this.obj.rotVelocity.normalize();
			
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('p'))) {
			camera.transform.translate(0, 0.02, 0.08)
			}
			if (this.obj.transform.m31 < 0) {
				this.obj.transform.m31 = 0;
				//this.obj.transform.reflection(0, 1, 0, 0);
				//this.obj.transform.reflection(0, 1, 0, 1);
			}
		   
			this.obj.transform.translate(this.speed, 0, 0);
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('k'))) {
				this.obj.rotVelocity.rotateZ(0.006);
			}
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('i'))) {
				this.obj.rotVelocity.rotateZ(-0.006);
			}
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('j'))) {
				this.obj.rotVelocity.rotateX(-0.006);
			}
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('l'))) {
				this.obj.rotVelocity.rotateX(0.006);
			}
		}
	};
	//SCN.camera = camera;
	plane.camera = camera;
	World.camera = camera;
	SCN.plane = plane;
	//SCN.Camera.look(0, 0, 5, 0, 0, 0);
	
	for (var i = 0; i < 111; i++) {
		var cube = new SKO_Cube();
		cube.morecube = new SKO_Cube();
		cube.rotVelocity.rotateX(0.01);
		cube.rotVelocity.rotateY(0.01);
		cube.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
		cube.transform.rotateX(Math.random() * Math.PI * 2);
		cube.transform.rotateY(Math.random() * Math.PI * 2);
		cube.transform.rotateZ(Math.random() * Math.PI * 2);
		cube.s = {
			onupdate: function() {
				//this.obj.transform.translate(0.01, 0, 0);
				this.obj.morecube.transform.translate(0, Math.sin(KJS.currentTick() / 20) / 10, 0);
				if (this.obj.transform.m31 < 0) {
					this.obj.transform.m31 = 0;
				}
				if (this.obj.transform.m31 > 200)
					this.obj.transform.m31 = 200;
				this.obj.transform.translate(0.04, 0, 0);
				this.obj.rotVelocity.rotateX((Math.random() - 0.5) / 60);
				this.obj.rotVelocity.rotateY((Math.random() - 0.5) / 60);
				this.obj.rotVelocity.rotateZ((Math.random() - 0.5) / 60);
			}
		}
		cube.morecube.transform.translate(0, 1, 0);
		SCN["CUBE_" + i] = cube;
	}
	
	
	World.fogIntensity = 0.003;
	World.clearColor.set(1, 1, 1, 1);
};