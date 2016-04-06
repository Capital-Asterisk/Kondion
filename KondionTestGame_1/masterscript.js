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

var fpsGuy = function() {
	var plane = new SKO_Cube();

	var camera = new OKO_Camera_();
	camera.transform.translate(0, 1, 0);
	camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.drag = new SKO_Cube();
	//plane.drag.transform.translate(0.0, 0.2, 0);
	//plane.drag.transform.scale(0.6, 0.05, 0.05);
	
	plane.camera = camera;

	plane.s = {
		speed: 0.0,
		bob: 0,
		y: 0,
		p: 0,
		on: false,
		mss: 0,
		falling: false,
		neaat: new Mat_Monotexture(),
		collide: function(kobj, normal) {
			this.mss = Math.min(normal.dot(0, 1, 0), this.mss);
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
			
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
				camera.moveSpeed = 20;
			} else {
				camera.moveSpeed = 8;
			}

			if (KJS.i.mouseDown(0) && this.on) {
				var eggs = new SKO_Cube();
				eggs.setMaterial(this.neaat);
				eggs.velocity.y = 40;
				eggs.transform.translate(Math.random() * 30, Math.random() * 10, Math.random() * 30);
				SCN[SCN.nextName("boolet")] = eggs;
				SCN.rescan();
				KJS.duck();
			}

			KJS.d.pos = "(" + this.obj.actTransform.m30 + ", " + this.obj.actTransform.m31 + ", " + this.obj.actTransform.m32 + ")";
			KJS.d.speed = this.speed;

			if (!this.falling) {
				this.infl = Math.min(Math.abs(this.speed / 5), 2) * !this.falling;
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' ')) && this.on) {
					if (this.obj.velocity.y < 0) {
						this.obj.velocity.y = 5;
						this.speed *= 1.2;
					}
				}
	
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('p')) && this.on) {
					camera.transform.translate(0, 0.02, 0.08)
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('s')) && this.on) {
					//this.obj.rotVelocity.rotateZ(0.006);
					this.speed = Math.max(this.speed - delta * 9, -5);
					this.bob -= this.speed / 24;
				} else if (KJS.i.keyboardDown(KJS.i.toGLFWCode('w')) && this.on) {
					//this.obj.rotVelocity.rotateZ(-0.006);
					this.speed = Math.min(this.speed + delta * 9, 6 + KJS.i.keyboardDown(KJS.i.toGLFWCode('q')) * 10);
					this.bob += this.speed / 24;
				} else  {
					this.speed *= 0.8
				}
			} else
				this.infl *= 0.95;
			
			//this.infl = 0;

			if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' ')) && this.on) {
				this.obj.velocity.y = 5;
			}
			
			camera.transform.identity();
			camera.transform.translate(0, 1 + Math.abs(Math.cos(this.bob / 2) / 24) * this.infl, Math.sin(-this.bob / 2) / 24 * this.infl);
			plane.drag.transform.identity();
			plane.drag.transform.translate(0.0, 0.7, 0);
			//plane.drag.transform.identity();
			this.obj.drag.transform.setRotationYXZ(-Math.PI / 2, this.p, 0); // formalitites
			plane.drag.transform.scale(0.1, 0.1, 2.0);
			
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
		}
	}
	
	plane.s.neaat.texture = KJS.texture("human");

	return plane;
}

var flyingThing = function() {
	var plane = new SKO_Cube();
	plane.drag = new SKO_Cube();
	plane.drag.transform.translate(0.1, 0.8, 0);
	plane.drag.transform.scale(0.6, 0.05, 0.05);

	camera = new OKO_Camera_();
	camera.transform.translate(0, 1, 0);
	camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.camera = camera;

	plane.s = {
		on: false,
		collide: function(kobj, normal) {
			this.mss = Math.min(normal.dot(0, 1, 0), this.mss);
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
		
		},
		onupdateb: function() {
		}
	}
	
	return plane;
}

var start = function() {

	KJS.i.setMouseLock(true);

	// Create a new render layer (GKO: Game Kondion Object)
	World.passes.add(new GKO_DeferredPass(0));
	
	// Add light
	SCN.LightB = new RKO_DirectionalLight();
	// Add ground, and that moving thing
	SCN.Ground = new SKO_InfinitePlane();
	SCN.Wallz = new SKO_InfinitePlane();

	// Do stuff
	SCN.Ground.transform.rotateX(-Math.PI / 2);
	SCN.Ground.textureSize = 10;
	SCN.Ground.transform.translate(0, 0, 0);
	SCN.LightB.color.set(1.0, 1.0, 1.0, 1.0);
	SCN.Wallz.transform.translate(0, 0, -40);
	SCN.Wallz.textureSize = 10;
	SCN.Wallz.transform.rotateX(-Math.PI / 2);
	
	SCN.eggs = new RKO_Obj(KJS.obj("coolthing"));
	SCN.eggs.transform.translate(0.0, 5.0, 0);
	SCN.eggs.setMaterial(new Mat_Monotexture("bird"));
	
	SCN.guy = fpsGuy();
	SCN.plane = flyingThing();
	SCN.s.players = [SCN.guy, SCN.plane];
	SCN.guy.s.on = true;
	SCN.s.currentPlayer = 0;

	for (var i = 0; i < 2; i++) {
		var cube = new SKO_Cube();
		cube.morecube = new SKO_Cube(0);
		cube.transform.translate((i - 50) * 7, Math.random() * 20, Math.random() * 100);
		cube.s = {
			onupdate: function() {
				this.obj.velocity.z -= Math.random() / 7;
				this.obj.morecube.transform.translate(0, Math.sin(KJS.currentTick() / 20) / 10, 0);
				this.obj.rotVelocity.rotateY((Math.random() - 0.5) / 60);
			}
		}
		cube.morecube.transform.translate(0, 1, 0);
		SCN["CUBE_" + i] = cube;
	}

	KJS.obj("coolthing").load();
	KJS.texture("noah").load();
	KJS.texture("human").load();
	KJS.texture("bird").load();
	

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

	SCN.s.onupdate = function() {
		SCN.eggs.transform.rotateZ(0.01);
		SCN.Wallz.transform.rotateX(Math.sin(KJS.currentTick() / 400) / 250);
		World.camera = this.players[this.currentPlayer].camera;
		if (KJS.i.mouseDown(1)) {
			KJS.i.setMouseLock(false);
		}
		if (KJS.i.keyboardDown(KJS.i.toGLFWCode('z'))) {
			this.players[this.currentPlayer].s.on = false;
			this.currentPlayer = (this.currentPlayer + 1) % this.players.length;
			this.players[this.currentPlayer].s.on = true;
		}
	}

	SCN.rescan();

};
