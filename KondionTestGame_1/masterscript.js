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
	World.passes.add(new GKO_DeferredPass(0));
	//World.passes.add(new GKO_RenderPass(2, KJS.NORMALS, true));
	//World.passes.add(new GKO_RenderPass(2, KJS.DEPTH, true));
	//World.passes[0].type = 2;
	//World.passes[1].setType(2);

	//SCN.rounGd = new SKO_InfinitePlane();
	SCN.Ground = new SKO_InfinitePlane();
	SCN.Wallz = new SKO_InfinitePlane();
	//SCN.WallE = new SKO_InfinitePlane();

	//SCN.Apple.transform.translate(0, 2, 0);
	SCN.Ground.transform.rotateX(-Math.PI / 2);
	SCN.Ground.textureSize = 10;
	SCN.Ground.transform.translate(0, 0, 0);

	//SCN.rounGd.transform.rotateX(Math.PI / 2);
	//SCN.rounGd.textureSize = 2;
	//SCN.rounGd.transform.translate(0, 0, -200);

	SCN.Wallz.transform.translate(0, 0, -40);
	SCN.Wallz.textureSize = 10;
	SCN.Wallz.transform.rotateX(-Math.PI / 2);

	//SCN.WallE.transform.rotateX(Math.PI);
	//SCN.WallE.transform.rotateZ(0.3);
	//SCN.WallE.transform.translate(0, 0, -40);
	//SCN.WallE.textureSize = 1;

	//World.Layers[0].Apple = SCN.Apple;

	var plane = new SKO_Cube();
	//plane.transform.m30 = 6000000;
	plane.drag = new SKO_Cube();
	plane.drag.transform.translate(0.1, 0.8, 0);
	plane.drag.transform.scale(0.6, 0.05, 0.05);
	delete plane.remove("drag");
	//plane.drag.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
	//plane.drag.transform.rotateX(Math.random() * Math.PI * 2);
	//plane.drag.transform.rotateY(Math.random() * Math.PI * 2);
	//plane.drag.transform.rotateZ(Math.random() * Math.PI * 2);
	camera = new OKO_Camera_();
	camera.transform.translate(0, 1, 0);
	camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.s = {
		speed: 0.0,
		bob: 0,
		y: 0,
		p: 0,
		mss: 0,
		falling: false,
		collide: function(kobj, normal) {
			this.mss = Math.min(normal.dot(0, 1, 0), this.mss);
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
			SCN.Wallz.transform.rotateX(Math.sin(KJS.currentTick() / 400) / 250);
		
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
				camera.moveSpeed = 20;
			} else {
				camera.moveSpeed = 8;
			}

			if (KJS.i.mouseDown(1)) {
				KJS.i.setMouseLock(false);
			}

			KJS.d.pos = "(" + this.obj.actTransform.m30 + ", " + this.obj.actTransform.m31 + ", " + this.obj.actTransform.m32 + ")";
			KJS.d.speed = this.speed;
			//print(Math.floor(this.speed * 3.6) + "km/h");
			
			if (!this.falling) {
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' '))) {
					if (this.obj.velocity.y < 0) {
						this.obj.velocity.y = 5;
						this.speed *= 1.2;
					}
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
			}
			
			this.infl = Math.min(Math.abs(this.speed / 5), 2) * !this.falling;
			
			camera.transform.identity();
			camera.transform.translate(0, 1 + Math.abs(Math.cos(this.bob / 2) / 24) * this.infl, Math.sin(-this.bob / 2) / 24 * this.infl);
			
			
			this.y = (this.y + (-KJS.i.getMouseDX() / 100)) % (Math.PI * 2);
			this.p = Math.max(Math.min(Math.PI / 2, this.p + (-KJS.i.getMouseDY() / 100)), -Math.PI / 2);
			this.obj.camera.transform.setRotationYXZ(-Math.PI / 2, this.p, 0);
			this.obj.transform.setRotationYXZ(this.y, 0, 0);
			this.obj.transform.translate(this.speed * delta, 0, 0);
			
			this.mss = 0;
			this.falling = true;
		},
		onupdateb: function() {
			this.falling = this.mss > -0.6;
			//print(this.falling);
		}
	}
	SCN.car = plane;
	plane.camera = camera;
	World.camera = camera;
	//SCN.Camera.look(0, 0, 5, 0, 0, 0);

	for (var i = 0; i < 80; i++) {
		var cube = new SKO_Cube();
		cube.morecube = new SKO_Cube(1);
		//cube.rotVelocity.rotateX(0.01);
		//cube.rotVelocity.rotateY(0.01);
		cube.transform.translate((Math.random() - 0.5) * 100, Math.random() * 20, (Math.random() - 0.5) * 100);
		//cube.transform.rotateX(Math.random() * Math.PI * 2);
		//cube.transform.rotateY(Math.random() * Math.PI * 2);
		//cube.transform.rotateZ(Math.random() * Math.PI * 2);
		cube.s = {
			onupdate: function() {
				this.obj.velocity.z -= Math.random() / 7
				//this.obj.transform.translate(0.01, 0, 0);
				this.obj.morecube.transform.translate(0, Math.sin(KJS.currentTick() / 20) / 10, 0);
				//this.obj.transform.translate(0.04, 0, 0);
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
	World.compMode = KJS.COMPOSITE;
	World.s = {fpav: 0};
	World.compositor = function(ctx, passes) {
		//print(ctx);
		//print(passes);
		passes[0].render();
		//passes[1].render();
		//passes[2].render();
		ctx.next();
		
		var eggs = Math.max(Math.min(KJS.fps(), 1000), 0);
		//print("neat: " + eggs);
		//print("fuck: " + (this.s.fpav - eggs))
		this.s.fpav -= (this.s.fpav - eggs) / 20;//Math.floor(this.s.fpav - KJS.fps());
		//print("eggs: " + (this.s.fpav - Math.round(Math.floor(KJS.fps()) / 60)));
		ctx.drawImage(passes[0], 0, 0, passes[0].width, passes[0].height);
		//ctx.drawImage(passes[1], passes[0].width / 2, 0, passes[1].width / 6, passes[1].height / 6);
		//ctx.deferredRender(passes[0], passes[2], passes[1],
		//	0, 0, 0, 0, 0, 0,
		//	passes[0].width, passes[0].height);
		ctx.fillText("Frame r8: " + Math.floor(this.s.fpav), 10, 30);
	}

};
