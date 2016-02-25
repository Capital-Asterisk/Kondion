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

	KJS.i.setMouseLock(true);

	// Create a new render layer (GKO: Game Kondion Object)
	World.passes.add(new GKO_RenderPass());
	World.passes.add(new GKO_RenderPass());
	World.passes[1].type = 2;

	SCN.rounGd = new SKO_InfinitePlane();
	SCN.Ground = new SKO_InfinitePlane();
	SCN.Wallz = new SKO_InfinitePlane();
	SCN.WallE = new SKO_InfinitePlane();

	//SCN.Apple.transform.translate(0, 2, 0);
	SCN.Ground.transform.rotateX(-Math.PI / 2);
	SCN.Ground.textureSize = 2;
	SCN.Ground.transform.translate(0, 0, 1);

	SCN.rounGd.transform.rotateX(Math.PI / 2);
	SCN.rounGd.textureSize = 2;
	SCN.rounGd.transform.translate(0, 0, -20);

	SCN.Wallz.transform.translate(0, 0, -40);
	SCN.Wallz.textureSize = 8;
	SCN.Wallz.transform.rotateX(-Math.PI / 2);

	SCN.WallE.transform.rotateX(Math.PI);
	SCN.WallE.transform.rotateZ(0.3);
	SCN.WallE.transform.translate(0, 0, -40);
	SCN.WallE.textureSize = 8;

	//World.Layers[0].Apple = SCN.Apple;

	var plane = new SKO_Cube();
	//plane.drag = new SKO_Cube();
	//plane.drag.transform.translate(0, 1, 0);
	//plane.drag.transform.scale(0.2, 0.2, 0.2);
	//plane.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
	//plane.transform.rotateX(Math.random() * Math.PI * 2);
	//plane.transform.rotateY(Math.random() * Math.PI * 2);
	//plane.transform.rotateZ(Math.random() * Math.PI * 2);
	camera = new OKO_Camera_();
	camera.transform.translate(0, 1, 0);
	camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.s = {
		speed: 0.0,
		bob: 0,
		y: 0,
		p: 0,
		collide: function(kobj, normal) {
			print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
		
			SCN.Wallz.transform.rotateX(Math.sin(KJS.currentTick() / 400) / 300);
		
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
				camera.moveSpeed = 20;
			} else {
				camera.moveSpeed = 8;
			}
			
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' '))) {
				if (this.obj.velocity.y < 0)
					this.obj.velocity.y = 5;
			}

			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('p'))) {
				camera.transform.translate(0, 0.02, 0.08)
			}

			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('s'))) {
				//this.obj.rotVelocity.rotateZ(0.006);
				this.speed = Math.max(this.speed - delta * 9, -5);
				this.bob -= this.speed / 24;
			} else if (KJS.i.keyboardDown(KJS.i.toGLFWCode('w'))) {
				//this.obj.rotVelocity.rotateZ(-0.006);
				this.speed = Math.min(this.speed + delta * 9, 6 + KJS.i.keyboardDown(KJS.i.toGLFWCode('q')) * 10);
				this.bob += this.speed / 24;
			} else  {
				this.speed *= 0.8
			}
			
			this.infl = Math.abs(this.speed / 5);
			
			camera.transform.identity();
			camera.transform.translate(0, 1 + Math.abs(Math.cos(this.bob / 2) / 24) * this.infl, Math.sin(-this.bob / 2) / 24 * this.infl);
			
			
			this.y += (-KJS.i.getMouseDX() / 100);
			this.p += (-KJS.i.getMouseDY() / 100);
			this.obj.camera.transform.setRotationYXZ(-Math.PI / 2, this.p, 0);
			this.obj.transform.setRotationYXZ(this.y, 0, 0);
			this.obj.transform.translate(this.speed * delta, 0, 0);
		}
	}
	SCN.car = plane;
	plane.camera = camera;
	World.camera = camera;
	//SCN.Camera.look(0, 0, 5, 0, 0, 0);

	for (var i = 0; i < 6; i++) {
		var cube = new SKO_Cube();
		cube.morecube = new SKO_Cube();
		//cube.rotVelocity.rotateX(0.01);
		//cube.rotVelocity.rotateY(0.01);
		cube.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
		//cube.transform.rotateX(Math.random() * Math.PI * 2);
		//cube.transform.rotateY(Math.random() * Math.PI * 2);
		//cube.transform.rotateZ(Math.random() * Math.PI * 2);
		cube.s = {
			onupdate: function() {
				//this.obj.transform.translate(0.01, 0, 0);
				this.obj.morecube.transform.translate(0, Math.sin(KJS.currentTick() / 20) / 10, 0);
				this.obj.transform.translate(0.04, 0, 0);
				//this.obj.rotVelocity.rotateX((Math.random() - 0.5) / 60);
				this.obj.rotVelocity.rotateY((Math.random() - 0.5) / 60);
				//this.obj.rotVelocity.rotateZ((Math.random() - 0.5) / 60);
			}
		}
		cube.morecube.transform.translate(0, 1, 0);
		SCN["CUBE_" + i] = cube;
	}


	World.fogIntensity = 0.0001;
	World.clearColor.set(0, 0, 0, 1);
	World.skyColor.set(1, 1, 1, 1);
};
