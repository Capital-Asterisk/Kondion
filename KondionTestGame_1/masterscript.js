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
	plane.sound = new NKO_Audio(KJS.aud("viznut"));
	plane.sound.volume = 0.3;
	//plane.drag.transform.translate(0.0, 0.2, 0);
	//plane.drag.transform.scale(0.6, 0.05, 0.05);
	
	plane.camera = camera;

	plane.s = {
		speed: 0.0,
		bob: 0,
		y: 0,
		p: 0,
		deb: 0,
		on: false,
		camera: camera,
		mss: 0,
		falling: false,
		neaat: new Mat_Monotexture(),
		onselect: function() {
			KJS.i.setMouseLock(true);
			
		},
		collide: function(kobj, normal) {
			this.mss = Math.min(normal.dot(0, 1, 0), this.mss);
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
			
			this.obj.hidden = this.on;
			
			if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
				camera.moveSpeed = 20;
			} else {
				camera.moveSpeed = 8;
			}

			/*if (KJS.i.mouseDown(0) && this.on) {
				var eggs = new SKO_Cube();
				eggs.setMaterial(this.neaat);
				eggs.velocity.y = 40;
				eggs.transform.translate(Math.random() * 30, Math.random() * 10, Math.random() * 30);
				SCN[SCN.nextName("boolet")] = eggs;
				SCN.rescan();
				KJS.duck();
			}*/

			this.deb = Math.min(this.deb + delta, 0.2);

			if (this.on && KJS.i.mouseDown(0) && this.deb >= 0.05) {
				var eggs = new SKO_Cube();
				eggs.velocity.set(0, 0, 0);
				camera.dir(eggs.velocity, 0, 0, -1, 100, false);
				//eggs.transform.set(camera.transform);
				eggs.transform.translate(this.obj.drag.actTransform.m30, this.obj.drag.actTransform.m31, this.obj.drag.actTransform.m32);
				eggs.transform.scale(0.1, 0.1, 0.1);
				eggs.s = {
					collide: function(kobj, normal) {
						this.obj.killMe = true;
					}
				};
				SCN[SCN.nextName("boolet")] = eggs;
				SCN.rescan();
				this.deb -= 0.05;
				plane.sound.play();
				//this.deb = Math.min(this.deb + delta, 0.05);
				//KJS.duck();
			}


			KJS.d.pos = "(" + this.obj.actTransform.m30 + ", " + this.obj.actTransform.m31 + ", " + this.obj.actTransform.m32 + ")";
			KJS.d.speed = this.speed;
			KJS.d.sound = plane.sound.position();

			if (!this.falling) {
				this.infl = Math.min(Math.abs(this.speed / 5), 2) * !this.falling;
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' ')) && this.on) {
					if (this.obj.velocity.y < 0) {
						this.obj.velocity.y = 5;
						this.speed *= 1.2;
					}
				}
				
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' ')) && this.on) {
					this.obj.velocity.y = 2;
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

			
			camera.transform.identity();
			camera.transform.translate(0, 1 + Math.abs(Math.cos(this.bob / 2) / 24) * this.infl, Math.sin(-this.bob / 2) / 24 * this.infl);
			plane.drag.transform.identity();
			plane.drag.transform.translate(0.9, 0.7, 0.3);
			//plane.drag.transform.identity();
			
			
			this.y = (this.y + (-KJS.i.getMouseDX() / 100)) % (Math.PI * 2);
			this.p = Math.max(Math.min(Math.PI / 2, this.p + (-KJS.i.getMouseDY() / 100)), -Math.PI / 2);
			this.obj.camera.transform.setRotationYXZ(-Math.PI / 2, this.p, 0);
			this.obj.transform.setRotationYXZ(this.y, 0, 0);
			this.obj.transform.translate(this.speed * delta, 0, 0);
			this.obj.drag.transform.setRotationYXZ(-Math.PI / 2, this.p, 0); // formalitites
			plane.drag.transform.scale(0.1, 0.1, 0.5);
			
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
	plane.transform.rotateY(Math.PI / 2);
	plane.mod = new RKO_Obj(KJS.obj("coolthing"));
	plane.mod.setMaterial(new Mat_Monotexture("bird"));

	camera = new OKO_Camera_();
	//camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.camera = camera;
	plane.sound = new NKO_Audio(KJS.aud("noice"));
	plane.sound.volume = 2.0;

	plane.drag = new SKO_Cube();
	plane.drag.transform.translate(0, 2, 0);

	plane.drag.drag = new SKO_Cube();
	plane.drag.drag.transform.translate(0, 2, 0);
	plane.drag.drag.setMaterial(new Mat_Monotexture("K_Cube"));
	
	plane.s = {
		on: false,
		p: 0,
		y: 0,
		speed: 0,
		mul: 1,
		deb: 0,
		bcam: 0,
		forward: new Vector3f(),
		up: new Vector3f(),
		heading: new Vector3f(),
		worldup: new Vector3f(0, 1, 0),
		camera: camera,
		vp: 0,
		vy: 0,
		vr: 0,
		onselect: function() {
			KJS.i.setMouseLock(true);
			
		},
		collide: function(kobj, normal) {
			//this.obj.dir(this.up, 0, 1, 0, 1, false);
			var doot = normal.dot(this.up);
			//KJS.d.wheels = "(" + Math.floor(this.up.x * 100) + ", " + Math.floor(this.up.y * 100) + ", " + Math.floor(this.up.z * 100) + ")";
			KJS.d.wheels = doot;
			if (doot > -0.97)
				this.speed *= Math.pow(0.004, delta) / 1.0;
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
			
			if (this.on) {
				
				this.obj.velocity.y += delta * (9.806 - Math.pow(0.00002, this.speed / 3000) * 9.806);
				
				this.obj.transform.translate(0, 0, -this.speed * delta);
				//plane.transform.rotateZ(0.02);
				
				this.y = (this.y + (-KJS.i.getMouseDX() / 100)) % (Math.PI * 2);
				this.p = Math.max(Math.min(Math.PI / 2, this.p + (-KJS.i.getMouseDY() / 100)), -Math.PI / 2);
				
				//this.obj.camera.transform.setRotationYXZ(this.y, this.p, 0);
				
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('a'))) {
					//plane.rotVelocity.rotateZ(0.2 * delta);
					this.vr += 1.2 * delta * Math.min(1, this.speed / 277);
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('d'))) {
					//plane.rotVelocity.rotateZ(-0.2 * delta);
					this.vr -= 1.2 * delta * Math.min(1, this.speed / 277);
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('s'))) {
					//plane.rotVelocity.rotateX(0.2 * delta);
					this.vp += 1.2 * delta * Math.min(1, this.speed / 277);
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('w'))) {
					//plane.rotVelocity.rotateX(-0.2 * delta);
					this.vp -= 1.2 * delta * Math.min(1, this.speed / 277);
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('q'))) {
					//plane.rotVelocity.rotateX(0.2 * delta);
					this.vy += 0.2 * delta * Math.min(1, this.speed / 277);
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('e'))) {
					//plane.rotVelocity.rotateX(-0.2 * delta);
					this.vy -= 0.2 * delta * Math.min(1, this.speed / 277);
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('v'))) {
					//plane.rotVelocity.rotateX(-0.2 * delta);
					this.vy -= 20 * delta;
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('f'))) {
					this.mul -= delta / 2;
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('r'))) {
					this.mul += delta / 2;
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('x'))) {
					this.bcam = Math.min(Math.PI / 2, this.bcam + delta * 2)
				} else {
					this.bcam = Math.max(0, this.bcam - delta * 4)
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' '))) {
					plane.sound.play(false);
					plane.sound.loop(true);
					this.speed += delta * 70;
				} else {
					this.speed = Math.max(0, this.speed - delta * 10);
				}

				plane.sound.pitch = Math.max(0.02, this.speed / 2000);

				this.forward.set(0, 0, 0);
				this.up.set(0, 0, 0); // how upsetting
				this.obj.dir(this.forward, 0, 0, -1, 1, false);
				this.obj.dir(this.up, 0, 1, 0, 1, false);
				this.forward.cross(0, 1, 0, this.heading);
				KJS.d.roll = Math.floor(this.heading.dot(this.up) * 1000) / 1000;
				KJS.d.pitch = Math.floor(this.up.y * 1000) / 1000;
				KJS.d.eggs = this.up;
				//KJS.d.pitch = Math.floor(this.forward.dot(0, 1, 0) * 1000) / 1000;
				
				this.deb = Math.min(this.deb + delta, 0.1);
				
				//if (this.speed > 50) {
					this.obj.velocity.y *= Math.pow(0.5, delta * (this.speed / 10));
					KJS.d.yvel = Math.floor(this.obj.velocity.y * 10000) / 10000;
				//}

				this.vy -= this.heading.dot(this.up) * delta / Math.max(0.7, Math.min(60, this.speed / 30));
				this.vp -= Math.min(0.1, this.up.y * delta * -this.obj.velocity.y / 10 / Math.max(0.01, this.speed / 30) / 5);

				if (KJS.i.mouseDown(0) && this.deb >= 0.1) {
					var eggs = new SKO_Cube();
					//eggs.setMaterial(this.neaat);
					eggs.velocity.set(this.forward);
					eggs.velocity.mul(this.speed + 500);
					eggs.transform.set(this.obj.transform);
					eggs.s = {
						collide: function(kobj, normal) {
							this.obj.killMe = true;
						}
					};
					SCN[SCN.nextName("bam")] = eggs;
					SCN.rescan();
					this.deb -= 0.1;
					
					//KJS.duck();
				}
	
				this.speed -= this.forward.y * delta * 98.06;
				
				this.vp *= Math.pow(0.00002, delta) / 1.2; // discovered after fish?
				this.vr *= Math.pow(0.00004, delta) / 1.2;
				this.vy *= Math.pow(0.00004, delta) / 1.5;
				
				this.obj.camera.transform.identity();
				this.obj.camera.transform.setRotationYXZ(-this.vy * 1.4 + Math.sin(this.bcam) * Math.PI, -this.vp * 1.4, -this.vr * 1.4);
				this.obj.camera.transform.translate(0, 3 * this.mul, 16 * this.mul);

				plane.transform.rotateX(this.vp);
				plane.transform.rotateY(this.vy);
				plane.transform.rotateZ(this.vr);
				
				KJS.d.planeTA = "(" + (Math.floor(this.obj.actTransform.m00 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m01 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m02 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m03 * 1000) / 1000) + ")";
				KJS.d.planeTB = "(" + (Math.floor(this.obj.actTransform.m10 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m11 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m12 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m13 * 1000) / 1000) + ")";
				KJS.d.planeTC = "(" + (Math.floor(this.obj.actTransform.m20 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m21 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m22 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m23 * 1000) / 1000) + ")";
				KJS.d.planeTD = "(" + (Math.floor(this.obj.actTransform.m30 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m31 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m32 * 1000) / 1000) + ", " + (Math.floor(this.obj.actTransform.m33 * 1000) / 1000) + ")";
				KJS.d.bird = "(" + Math.floor(this.forward.x * 100) + ", " + Math.floor(this.forward.y * 100) + ", " + Math.floor(this.forward.z * 100) + ")";
				KJS.d.birdup = "(" + Math.floor(this.up.x * 100) + ", " + Math.floor(this.up.y * 100) + ", " + Math.floor(this.up.z * 100) + ")";
				KJS.d.birdspeed = Math.floor(this.speed);

			} else {

				this.spd = 0;

			}
		},
		onupdateb: function() {
		}
	}
	
	return plane;
}

var sign = function(a) {
	if (a == 0)
		return 0;
	else
		return (a < 0) ? -1 : 1;
	
}

var fanbot = function() {
	var verybase = new SKO_Cube();
	var material = new Mat_Monotexture("fanbot");
	var camera = new OKO_Camera_(); // third person camera
	verybase.transform.rotateY(Math.PI / 2);
	verybase.hidden = true;
	verybase.base = new RKO_Obj(KJS.obj("fanbot_base"));
	verybase.base.setMaterial(material);
	verybase.base.transform.translate(0, -0.5, 0);
	verybase.base.head = new RKO_Obj(KJS.obj("fanbot_head"));
	verybase.base.head.setMaterial(material);
	verybase.base.head.transform.translate(0, 1.1, 0.25);
	verybase.base.head.fan = new RKO_Obj(KJS.obj("fanbot_fan"));
	verybase.base.head.fan.setMaterial(material);
	verybase.base.head.fan.transform.translate(0, 1.85961 - 1.1, 0.31 - 0.25);
	verybase.s = {
		speed: 0.0,
		camera: camera,
		cameraY: 1,
		cameraP: 0,
		headRot: 0,
		on: false,
		angle: 0,
		prefAngle: 0,
		fanspeed: 5,
		moving: false,
		turning: false,
		onselect: function() {
			KJS.i.setMouseLock(true);
			
		},
		collide: function(kobj, normal) {
			//this.obj.dir(this.up, 0, 1, 0, 1, false);
			//var doot = normal.dot(this.up);
			//KJS.d.wheels = "(" + Math.floor(this.up.x * 100) + ", " + Math.floor(this.up.y * 100) + ", " + Math.floor(this.up.z * 100) + ")";
			//KJS.d.wheels = doot;
			//if (doot > -0.97)
			//	this.speed *= Math.pow(0.004, delta) / 1.0;
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
			verybase.base.head.fan.transform.rotateZ(delta * this.fanspeed);
			this.cameraY += (((this.cameraY + (-KJS.i.getMouseDX() / 100))) - this.cameraY) / 8;
			this.cameraP += ((Math.max(Math.min(Math.PI / 2, this.cameraP + (-KJS.i.getMouseDY() / 100)), -Math.PI / 2)) - this.cameraP) / 8;
			//this.headRot += (sign(this.headRot + sign(this.cameraP - this.headRot) * delta) == sign(this.headRot)) ? sign(this.cameraP - this.headRot) * delta : this.headRot - this.cameraP;
			this.obj.base.head.transform.setRotationYXZ(0, this.cameraP, 0);
			//this.camera.transform.translate(0, 0, 4);
			//public void dir(Matrix4f in, float x, float y, float z, float amt, boolean local) {
			//this.camera.transform.rotateY(0.01);
			this.obj.transform.setRotationYXZ(this.angle, 0, 0);
			if (this.on) {
				//this.speed = 0;
				this.moving = false;
				
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('w'))) {
					this.prefAngle = this.cameraY;
					//this.speed = 20;
					this.fanspeed = Math.min(30, this.fanspeed + delta * 4);
					this.moving = true;
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('s'))) {
					this.prefAngle = this.cameraY - Math.PI;
					//this.speed = 20;
					this.fanspeed = Math.min(30, this.fanspeed + delta * 4);
					this.moving = true;
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('d'))) {
					this.prefAngle = this.cameraY - Math.PI / 2;
					//this.speed = 20;
					this.fanspeed = Math.min(30, this.fanspeed + delta * 4);
					this.moving = true;
				}
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode('a'))) {
					this.prefAngle = this.cameraY + Math.PI / 2;
					//this.speed = Math.max(0, this.speed - delta * 10);
					this.fanspeed = Math.min(30, this.fanspeed + delta * 4);
					this.moving = true;
				}
				
				var diff = Math.atan2(Math.sin(this.prefAngle - this.angle), Math.cos(this.prefAngle - this.angle));
				
				KJS.d.sinthing = Math.floor(diff * 1000) / 1000;
				KJS.d.thing = Math.floor(this.angle * 1000) / 1000 + " " + Math.floor((this.angle + sign(diff) * delta) * 1000) / 1000
				if (Math.abs(diff) > 0.01) {
					if (sign(this.angle - this.prefAngle + sign(diff) * delta) == sign(this.angle - this.prefAngle)) {
						this.angle += sign(diff) * delta;
						//this.speed = Math.max(0, this.speed - delta * 40);
						if (Math.abs(diff) > 1.58)
						this.turning = true;
					} else {
						this.angle = this.prefAngle;
						this.turning = false;
					}
				} else
					this.turning = false;
				
				if (this.turning || !this.moving) {
					this.speed = Math.max(0, this.speed - delta * 40);
				} else if (this.moving) {
					this.speed = Math.min(10, this.speed + delta * 20);
				}
				
				this.obj.transform.translate(0, 0, -this.speed * delta);
				this.camera.moveTo(this.obj.transform);
				this.camera.transform.translate(0, 1, 0);
				this.camera.transform.setRotationYXZ(this.cameraY, this.cameraP, 0);
				this.camera.transform.translate(1, 0, 4);
			}
			
			this.fanspeed += sign(this.speed - this.fanspeed) * delta;//Math.max(2, this.fanspeed - delta * 3);
			
		}
	};
	SCN.BotCam = camera;
	return verybase;
}

var start = function() {
	KJS.i.setMouseLock(true);
	KJS.s.clear();
	KJS.s.load("ktg1:scenes/gameA");
};
