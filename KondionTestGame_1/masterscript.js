/* KONDION TEST
 * THE KONDION masterscript.js
 * Contains everything about KONDION TEST
 * 
 * Object types
 * 1 - Walls
 * 2 - Players
 * 4 - Enemies
 * 8 - 
 * 16 - Bullets
 * 32 - woot
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
	plane.sound = new NKO_Audio(KJS.aud("bang"));
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
		bullet: {
			collide: function(kobj, normal) {
				if (kobj instanceof SKO_InfinitePlane && (kobj != plane.drag || kobj != plane))
					this.obj.killMe = true;
			}
		},
		onselect: function() {
			KJS.i.setMouseLock(true);
			
		},
		collide: function(kobj, normal) {
			if (normal != null)
				this.mss = Math.min(normal.dot(0, 1, 0), this.mss);
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
		},
		onupdate: function() {
			
			//this.obj.hidden = this.on;
			
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
				camera.dir(eggs.velocity, 0, 0, -1, 20, false);
				eggs.collideType = 4;
				eggs.collideMove = 1 | 4;
				//eggs.transform.set(camera.transform);
				eggs.transform.translate(this.obj.drag.actTransform.m30, this.obj.drag.actTransform.m31, this.obj.drag.actTransform.m32);
				eggs.transform.scale(0.1, 0.1, 0.1);
				eggs.s = this.bullet;
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
					this.obj.velocity.y = 8;
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

var betterFps = function() {
	var guy = new SKO_Cube();
	guy.transform.scale(1, 2, 1);
	guy.hidden = true;
	guy.drag = new SKO_Cube();
	guy.drag.hidden = true;
	guy.sound = new NKO_Audio(KJS.aud("bang"));
	guy.sound.volume = 0.3;
	guy.divFriction = 1.1;
	guy.collideType = 2;
	
	guy.hitsound = new NKO_Audio(KJS.aud("hit"));
	guy.damsound = new NKO_Audio(KJS.aud("damage"));
	guy.cashsound = new NKO_Audio(KJS.aud("cash"));
	guy.damsound.pitch = 3;
	guy.damsound.volume = 0.6;
	
	guy.drag.obj = new RKO_Obj(KJS.obj("guy"));
	guy.drag.obj.setMaterial(new Mat_FlatColor(0.24, 0.24, 1.0))
	guy.drag.obj.transform.translate(0, -1, 0);
	guy.drag.obj.transform.scale(0.5, 0.5, 0.5)
	guy.drag.obj.transferScale = false;
	
	//plane.drag.transform.translate(0.0, 0.2, 0);
	//plane.drag.transform.scale(0.6, 0.05, 0.05);
	
	guy.drag.camera = new OKO_Camera_();
	guy.drag.camera.transferScale = false;
	guy.drag.camera.transform.translate(0, 0.25, 0);
	guy.drag.camera.mode = 0;
	
	guy.drag.camera.third = new OKO_Camera_();
	guy.drag.camera.third.transferScale = false;
	guy.drag.camera.third.transform.translate(0.5, 1, 4);
	guy.drag.camera.third.mode = 0;
	
	guy.drag.camera.weapon = new RKO_Obj(KJS.obj("deli_4l558"));
	guy.drag.camera.weapon.setMaterial(new Mat_Monotexture("d4l558"));
	guy.drag.camera.weapon.transform.translate(0.5, -0.2, -0.8);
	guy.drag.camera.weapon.transform.rotateX(Math.PI / 2);
	guy.drag.camera.weapon.transform.rotateZ(Math.PI);
	guy.drag.camera.weapon.transform.scale(0.05, 0.05, 0.05);
	
	var dog = new Mat_Monotexture("laser");
	dog.alphaBlend = 1;
	SCN.TargetBlock = new RKO_Board(2);
	SCN.TargetBlock.setMaterial(dog);
	//SCN.TargetBlock.plant = 
	//SCN.TargetBlock.plant.transform.rotateY(Math.PI / 2);
	//SCN.TargetBlock.plant.setMaterial(dog);
	
	var lasers = 10;
	for (var i = 0; i < lasers; i++) {
		var wall = new RKO_Board(2);
		wall.setMaterial(dog);
		wall.transform.rotateY((Math.PI * 2) / lasers * i);
		SCN.TargetBlock[SCN.TargetBlock.nextName("plant")] = wall;
	}
	
	guy.drag.camera.weapon.flare = new RKO_Board(2);
	guy.drag.camera.weapon.flare.setMaterial(new Mat_Monotexture("flare"));
	guy.drag.camera.weapon.flare.material.alphaBlend = 1;
	guy.drag.camera.weapon.flare.transform.translate(0, 12.3, 3.30367);
	guy.drag.camera.weapon.flare.transform.scale(20, 20, 20);
	guy.drag.camera.weapon.flare.transform.rotateX(Math.PI / 2);
	guy.drag.camera.weapon.sound = new NKO_Audio(KJS.aud("hicol"));
	guy.drag.camera.weapon.sound.volume = 19;
	//SCN.TargetBlock.collideType = 32;
	//SCN.TargetBlock.collideMove = 32;
	//SCN.TargetBlock.collideCall = 32;
	//SCN.TargetBlock.anchor = true;
	
	guy.drag.camera.weapon.s = {
		seq: 0,
		alt: false,
		trace: new Vector3f(),
		dir: new Vector3f(),
		fire: function() {
			if (this.seq < 0.3) {
				guy.drag.camera.weapon.sound.play();
				this.seq = 1;
				SCN.TargetBlock.hideAll(false);
				this.alt = false;
			}
		},
	
		altfire: function() {
			this.seq = 1;
			SCN.TargetBlock.hideAll(false);
			this.alt = true;
		},
		
		onupdate: function() {
			
			guy.drag.camera.weapon.transform.translate(0, -Math.pow(this.seq, 6) * 6, 0);
		},
		
		onupdateb: function() {
			
			if (this.seq > 0) {
				this.trace.set(0, 0, 0);
				this.dir.set(0, 0, 0);
				guy.drag.camera.weapon.flare.dir(this.dir, 0, 0, -1, 1, false);
				var tgt = KJS.raycast(this.trace, guy.drag.camera.weapon.flare.actTransform.m30,
						guy.drag.camera.weapon.flare.actTransform.m31,
						guy.drag.camera.weapon.flare.actTransform.m32,
						this.dir.x / 10,
						this.dir.y / 10,
						this.dir.z / 10, 1000, 1 | 4);
				
				KJS.d.spamwoot = this.trace;
				SCN.TargetBlock.stretchSimple(
						guy.drag.camera.weapon.flare.actTransform.m30,
						guy.drag.camera.weapon.flare.actTransform.m31,
						guy.drag.camera.weapon.flare.actTransform.m32,
						this.trace.x, this.trace.y, this.trace.z, (2 + Math.random()) * this.seq);
				//SCN.TargetBlock.transform.rotateY(KJS.orandom() * Math.PI);
				if (tgt != null && tgt.s != null && tgt.s.killable) {
					//tgt.killMe = true;
					tgt.s.hit(delta * 2 + 4000 * (this.seq > 0.9));
				}
				var arr = SCN.TargetBlock.values();
				for (var i = 0; i < arr.size(); i++) {
					arr[i].applyTransform();
				}
				SCN.TargetBlock.transform.m30 = this.trace.x;
				SCN.TargetBlock.transform.m31 = this.trace.y;
				SCN.TargetBlock.transform.m32 = this.trace.z;
				
				guy.drag.camera.weapon.flare.transform.identity();
				guy.drag.camera.weapon.flare.transform.translate(0, 12.3, 3.30367);
				guy.drag.camera.weapon.flare.transform.scale(
						(80 + Math.random() * 10) * this.seq,
						(80 + Math.random() * 10) * this.seq,
						(80 + Math.random() * 10) * this.seq);
				guy.drag.camera.weapon.flare.transform.rotateX(Math.PI / 2);
				
				SCN.s.shake = 0.02 * this.seq * this.seq * this.seq * this.seq;
				
				this.seq = Math.max(0, this.seq - delta);
				
				
				
				if (this.seq == 0) {
					SCN.TargetBlock.hideAll(true);
				}
				
			} else {
				
				SCN.s.shake = 0.0;
			}
		}
		
	}
	
	guy.s = {
		camera: guy.drag.camera.third,
		ctrUp: KJS.i.getButtonIndex("up"),
		ctrDn: KJS.i.getButtonIndex("down"),
		ctrLf: KJS.i.getButtonIndex("left"),
		ctrRt: KJS.i.getButtonIndex("right"),
		ctrGr: KJS.i.getButtonIndex("grapple"),
		ctrSw: KJS.i.getButtonIndex("switch"),
		p: 0,
		y: 0,
		mss: 0,
		falling: false,
		on: false,
		onground: false,
		walkAngle: 0,
		walkSpeed: 13,
		health: 100,
		walk: new Vector3f(),
		killable: true,
		bob: 0,
		bobInfl: 0,
		inspect: 0,
		inspecting: false,
		useButton: KJS.i.getButtonIndex("open"),
		collide: function(kobj, normal) {
			if (normal != null)
				this.mss = Math.min(normal.dot(0, 1, 0), this.mss);
			//print(this.mss);
			//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
			
		},
		hit: function(dmg) {
			SCN.s.blood += 0.5;
			this.health = Math.max(0, this.health - dmg);
			guy.damsound.play();
		},
		onupdateb: function() {
			this.falling = this.mss > -0.6;
			KJS.d.IAMFALLING = this.falling;
			
		},
		onupdate: function() {
			
			guy.drag.obj.hidden = this.on && this.camera == guy.drag.camera;
			//print(guy.drag.hidden);
		
			if (this.on && this.health > 0) {
					
				//if (KJS.i.keyboardDown(KJS.i.toGLFWCode('o'))) {
				//	guy.drag.camera.weapon.transform.rotateZ(delta);
				//}
				//if (KJS.i.keyboardDown(KJS.i.toGLFWCode('l'))) {
				//	this.walkSpeed -= 1;
				//	print(this.walkSpeed)
				//}
				
				if (KJS.i.buttonTap(this.ctrSw) == 1) {
					this.camera = (this.camera == guy.drag.camera) ? guy.drag.camera.third : guy.drag.camera;
					
				}
				
				if (KJS.i.buttonTap(this.ctrGr) == 1) {
					var t = this.ctrGr;
					var p = this.obj;
					SCN.GHook = new SKO_Cube();
					SCN.GHook.transform.m30 = this.obj.transform.m30;
					SCN.GHook.transform.m31 = this.obj.transform.m31;
					SCN.GHook.transform.m32 = this.obj.transform.m32;
					SCN.GHook.transform.scale(0.3, 0.3, 0.3);
					this.obj.drag.camera.dir(SCN.GHook.velocity, 0, 0, -1, 100, false);
					SCN.GHook.s = {
						ctrGr: t,
						player: p,
						grab: false,
						collide: function(kobj, normal) {
							this.obj.gravityOverride = true;
							this.obj.velocity.set(0, 0, 0);
							this.grab = true;
							this.obj.collideType = 32;
							this.obj.collideMove = 32;
							this.obj.collideCall = 32;
						},
						onupdate: function() {
							
							SCN.GRope.stretchSimple(
									this.obj.transform.m30, this.obj.transform.m31, this.obj.transform.m32,
									this.player.transform.m30, this.player.transform.m31, this.player.transform.m32, 0.02);
							
							if (!KJS.i.buttonIsDown(this.ctrGr)) {
								this.obj.killMe = true;
								SCN.GRope.killMe = true;
								//print("KIIIIIIIILMEEEEEEEEEEEEEEEEEEEEEEEEEEEe")
							}
							
							if (this.grab) {
								this.player.shoot(this.obj.transform.m30, this.obj.transform.m31, this.obj.transform.m32, delta * 60, false);
								//KJS.d.vel = this.player.velocity.x + " " + this.player.velocity.y + " " + this.player.velocity.z;
								//print(KJS.d.vel);
							}
						}
							
					}
					
					SCN.GRope = new RKO_Board(2);
					SCN.GRope.setMaterial(new Mat_FlatColor(1.0, 0.63529, 0.0));
					//SCN.GRope.gravityOverride = true;
					//SCN.GRope.collideType = 32;
					//SCN.GRope.collideMove = 32;
					//SCN.GRope.collideCall = 32;
					SCN.rescan();
				}
				
				if (KJS.i.buttonTap(this.useButton) == 1) {
					this.inspecting = !this.inspecting;
				}
				
				if (this.inspecting) {
					this.inspect = Math.min(1, this.inspect + delta);
				} else {
					
					this.inspect = Math.max(0, this.inspect - delta);
				}
				
				var moveForward = 0, moveRight = 0;
				if (KJS.i.buttonIsDown(this.ctrUp))
					moveForward += 1;
				if (KJS.i.buttonIsDown(this.ctrDn))
					moveForward -= 1;
				if (KJS.i.buttonIsDown(this.ctrLf))
					moveRight -= 1;
				if (KJS.i.buttonIsDown(this.ctrRt))
					moveRight += 1;
				
				if (KJS.i.mouseDown(0)) {
					guy.drag.camera.weapon.s.fire();
				}
		
				if (!this.falling) {
					if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' '))) {
						this.obj.velocity.y = 8;
					}
					//.transform.translate(this.speed * delta, 0, 0);
					//this.obj.drag.transform.setRotationYXZ(-Math.PI / 2, this.p, 0); // formalitites
					
					//guy.hidden = this.on;
				}
				
				this.walk.set(0, 0, 0);
				//print(moveForward + " " + moveRight);
				
				if (moveForward | moveRight != 0) {
					var mag = Math.sqrt(moveForward * moveForward + moveRight * moveRight);
					moveForward /= mag;
					moveRight /= mag;
					//print(mag);
					this.bob += delta;
					this.bobInfl = Math.min(1, this.bobInfl + delta);
					guy.drag.dir(this.walk, moveForward,
							0, moveRight, this.walkSpeed, false);
					//print(this.walk);
					if (this.falling)
						guy.accelerateTo(this.walk.x, this.walk.y, this.walk.z, delta * 40, true, false, true);
					else
						guy.accelerateTo(this.walk.x, this.walk.y, this.walk.z, delta * 100, true, false, true);
					//guy.velocity.x += this.walk.x;
					//guy.velocity.y += this.walk.y;
					//guy.velocity.z += this.walk.z;
				} else {
					this.bobInfl *= Math.pow(0.00004, delta) / 1.2;
				}
				
				this.y = (this.y + (-KJS.i.getMouseDX() / (World.s.sense * 100))) % (Math.PI * 2);
				this.p = Math.max(Math.min(Math.PI / 2, this.p + (-KJS.i.getMouseDY() / (World.s.sense * 100))), -Math.PI / 2);
				guy.drag.camera.transform.setRotationYXZ(-Math.PI / 2 + KJS.orandom() * SCN.s.shake, this.p + KJS.orandom() * SCN.s.shake, 0);
				guy.drag.transform.setRotationYXZ(this.y, 0, 0);
				
			} else if (this.health <= 0) {
				// ded
				guy.collideType = 32;
				SCN.s.blood = 2;
			} else {
				guy.collideType = 2;
			}
			
			guy.drag.camera.weapon.transform.identity();
			guy.drag.camera.weapon.transform.rotateX(Math.PI / 2);
			guy.drag.camera.weapon.transform.rotateZ(Math.PI + Math.PI / 2 * this.inspect);
			guy.drag.camera.weapon.transform.rotateY(-0.3 * this.inspect);
			guy.drag.camera.weapon.transform.scale(0.05, 0.05, 0.05);
			
			guy.drag.camera.weapon.transform.m30 = 0.5 + Math.sin(this.bob * 2 * Math.PI) / 18 * this.bobInfl;
			guy.drag.camera.weapon.transform.m31 = -0.2 - Math.cos(this.bob * 4 * Math.PI) / 28 * this.bobInfl;
			guy.drag.camera.weapon.transform.m32 = -0.8;
			
			guy.drag.camera.weapon.transform.m30 += this.inspect * -0.5;
			guy.drag.camera.weapon.transform.m31 += this.inspect * 0.2;
			guy.drag.camera.weapon.transform.m32 += this.inspect * 0.1;
			
			this.mss = 0;
			this.falling = true;
		},
		onselect: function() {
			
		}
	}
	
	return guy;
}

var cookies = {
	collide: function(kobj, normal) {
		if (kobj.collideType == 2 && kobj.s.killable) {
			// is player
			kobj.s.health = Math.min(100, kobj.s.health + 0.5);
			//SCN.s.hitsound();
			kobj.cashsound.play();
			SCN.s.score ++;
			this.obj.delete();
		}
	},
	onupdate: function() {
		//this.obj.transform.rotateY(delta);
		this.time += delta;
		if ((this.time * 3 * (this.time / 10 + 1)) % 1 < 0.5)
			this.obj.material.setColorf(1, 0.945, 0);
		else
			this.obj.material.setColorf(1, 1, 0.5);
		
		if (this.time > 30) {
			this.obj.delete();
		} else {
			var player = SCN.s.players[SCN.s.currentPlayer];
			var dist = this.obj.distance(player);
			if (dist < 8) {
				// 8m collection radius
				this.obj.gravityOverride = true;
				this.obj.velocity.div(2);
				this.obj.shoot(player.transform.m30, player.transform.m31, player.transform.m32, dist * 2, false);
			} else {
				this.obj.gravityOverride = false;
			}

		}
		
	}
}

var cookie = function() {
	var cookie = new SKO_Cube(1 | 2);
	cookie.setMaterial(new Mat_FlatColor(1, 0.945, 0));
	cookie.transform.scale(0.3, 0.2, 0.3);
	cookie.collideType = 16;
	cookie.collideCall = 2;
	cookie.s = {
		time: Math.random(),
		collide: cookies.collide,
		onupdate: cookies.onupdate
	}
	return cookie;
}

var zombieCol = function(kobj, normal) {
	if (normal != null) {
		var mag = Math.sqrt(Math.pow(this.tgtX - this.obj.transform.m30, 2) + Math.pow(this.tgtY - this.obj.transform.m32, 2));
		//print("normal: " + normal)
		//print("dot: " + normal.dot((this.tgtX - this.obj.transform.m30) / mag, 0, (this.tgtY - this.obj.transform.m32) / mag));
		if (normal.dot((this.tgtX - this.obj.transform.m30) / mag, 0, (this.tgtY - this.obj.transform.m32)) > 0.9) {
			this.obj.velocity.y = 10;
		}
	}
}

var laserShot = {
	onupdate: function() {
		this.obj.velocity.x += this.obj.velocity.x * delta;
		this.obj.velocity.y += this.obj.velocity.y * delta;
		this.obj.velocity.z += this.obj.velocity.z * delta;
		this.obj.transform.rotateZ(delta * 12);
	},
	collide: function(kobj, normal) {
		if (kobj.collideType == 2 && kobj.s.killable) {
			// is player
			kobj.s.hit(Math.floor(1 + Math.random() * 2));
		}
		this.obj.killMe = true;
	}
}

var tempv = new Vector3f();

var zombieUpd = function() {
	//this.obj.velocity.x += KJS.orandom() * delta * 30;
	//this.obj.velocity.y += KJS.orandom() * delta * 30;
	//this.obj.velocity.z += KJS.orandom() * delta * 30;
	this.obj.base.head.fan.transform.rotateZ(delta * 20);
	
	if (KJS.currentTick() % 20 == this.firstTick) {
		this.attack = false;
		var dist = this.obj.distance(SCN.s.players[SCN.s.currentPlayer]);
		if (dist < 50) {
			this.dir.set(0, 0, 0);
			this.obj.point(this.dir, SCN.s.players[SCN.s.currentPlayer]);
			var tgt = KJS.raycast(this.trace, this.obj.actTransform.m30,
				this.obj.actTransform.m31,
				this.obj.actTransform.m32,
				this.dir.x / 2,
				this.dir.y / 2,
				this.dir.z / 2,
				50 * 2, 1 | 2);
			if (tgt == SCN.s.players[SCN.s.currentPlayer]) {
				//this.obj.sound.play();
				this.attack = true;
			}
		}
		//print("woot: " + dist);
	}
	
	// am i in position
	if (Math.abs(this.tgtX - this.obj.transform.m30) < 2
		&& Math.abs(this.tgtY - this.obj.transform.m32) < 2) {
		// yes, now chill...
		if (Math.random() < 0.05) {
			// 5% chance of stop chilling
			this.tgtX = fun * 18 * KJS.orandom() - 9;
			this.tgtY = fun * 18 * KJS.orandom() - 9;
			
		}
		
	} else {
		
		var mag = Math.sqrt(Math.pow(this.tgtX - this.obj.transform.m30, 2) + Math.pow(this.tgtY - this.obj.transform.m32, 2));
		//this.obj.transform.m30 = this.tgtX;
		//this.obj.transform.m32 = this.tgtY;
		if (this.attack) {
			// A_FaceTarget
			this.angle = Math.atan2((SCN.s.players[SCN.s.currentPlayer].transform.m30 - this.obj.transform.m30) / mag, (SCN.s.players[SCN.s.currentPlayer].transform.m32 - this.obj.transform.m32) / mag) + Math.PI;
			this.obj.accelerateTo((this.tgtX - this.obj.transform.m30) / mag * 0.2, 0, (this.tgtY - this.obj.transform.m32) / mag * 0.2, delta * 4, true, false, true);
		} else {
			this.angle = Math.atan2((this.tgtX - this.obj.transform.m30) / mag, (this.tgtY - this.obj.transform.m32) / mag) + Math.PI;
			this.obj.accelerateTo((this.tgtX - this.obj.transform.m30) / mag * 5, 0, (this.tgtY - this.obj.transform.m32) / mag * 5, delta * 40, true, false, true);
		}
		//this.tgtX /= mag;
		//this.tgtX /= mag;
		
		//this.angle += (Math.atan2((this.tgtY - this.obj.transform.m32) / mag, (this.tgtX - this.obj.transform.m30) / mag) - this.angle) / 20;
		
	}
	
	if (this.attack) {
		if (this.seq == 1) {
			this.obj.sound.change(KJS.aud("fan_beep"));
			this.obj.sound.play();
			this.obj.sound.pitch = 1;
		}
		this.seq = Math.max(0, this.seq - delta);
		if (this.seq == 0) {
			// shoot
			this.obj.sound.change(KJS.aud("fan_shot"));
			this.obj.sound.play();
			this.obj.sound.pitch = 1;
			this.seq = this.shotdel;
			
			var boolet = new SKO_Cube();
			boolet.transform.m30 = this.obj.transform.m30;
			boolet.transform.m31 = this.obj.transform.m31;
			boolet.transform.m32 = this.obj.transform.m32;
			
			this.obj.base.dir(boolet.transform, this.shot ? 0.333 : -0.333, 0, 0, 1, false);
			this.shot = !this.shot
			//boolet.velocity.y += 10;
			//boolet.anchor = false;
			boolet.transform.rotateY(Math.PI / 2);
			boolet.hidden = true;
			boolet.transform.scale(0.1, 0.1, 0.1);
			boolet.collideType = 16;
			//boolet.collideMove = 1 | 2;
			boolet.collideCall = 1 | 2;
			boolet.applyTransform();
			boolet.transform.rotateY(Math.PI / 2);
			boolet.s = {
				onupdate: laserShot.onupdate,
				collide: laserShot.collide
			};
			var dist = boolet.distance(SCN.s.players[SCN.s.currentPlayer]);
			boolet.shoot(
					SCN.s.players[SCN.s.currentPlayer].transform.m30 + SCN.s.players[SCN.s.currentPlayer].velocity.x * (dist / 20),
					SCN.s.players[SCN.s.currentPlayer].transform.m31,
					SCN.s.players[SCN.s.currentPlayer].transform.m32 + SCN.s.players[SCN.s.currentPlayer].velocity.z * (dist / 20), 10, true);
			boolet.gravityOverride = true;
			boolet.transform.scale(1, 1, 16);
			boolet.setMaterial(new Mat_FlatColor(1.0, 0.0, 0.0))
			boolet.model = new RKO_Obj(KJS.obj("fanbot_laser"), 2);
			boolet.model.transform.scale(1, 1, 1 / 16);
			boolet.model.setMaterial(new Mat_Monotexture("laser2"));
			boolet.model.material.alphaBlend = 1;
			if (this.shotdel < 0.2) {
				boolet.model.material.setColorf(Math.random(), Math.random(), Math.random());
			} else {
				boolet.model.material.setColorf(1, 1, 0);
			}
			SCN[SCN.nextName("laser")] = boolet;
			SCN.rescan();
		}
	} else {
		this.seq = 1;
	}
	
	this.obj.base.transform.identity();
	this.obj.base.transform.translate(0, -1, 0);
	this.obj.base.transform.rotateY(this.angle);
	
	if (this.health <= 0) {
		this.obj.sound.pitch = 0.6;
		this.obj.sound.play();
		SCN.s.addFans ++;
		for (var i = 0; i < ((this.shotdel < 0.2) ? 10 : 3); i ++) {
			var cash = cookie();
			cash.moveTo(this.obj.transform);
			cash.velocity.x = KJS.orandom() * 6;
			cash.velocity.y = Math.random() * 6;
			cash.velocity.z = KJS.orandom() * 6;
			SCN[SCN.nextName("cash")] = cash;
		}
		SCN.rescan();
		this.obj.delete();
	}
	
}

var zombieHit = function(p) {
	this.obj.sound.play();
	this.health -= p * 200;
	SCN.s.hitmarker = 0.5;
	SCN.s.hitsound();
}

var zombie = function() {
	var zarm = new SKO_Cube();
	var material = new Mat_Monotexture("fanbot");
	zarm.collideType = 4;
	zarm.hidden = true;
	zarm.transform.scale(1, 2, 1);
	zarm.sound = new NKO_Audio(KJS.aud("fan_beep"));
	//zarm.sound.volume = 0.3;
	zarm.base = new RKO_Obj(KJS.obj("fanbot_base"));
	zarm.base.transferScale = false;
	zarm.base.setMaterial(material);
	zarm.base.transform.translate(0, -1, 0);
	zarm.base.head = new RKO_Obj(KJS.obj("fanbot_head"));
	zarm.base.head.setMaterial(material);
	zarm.base.head.transform.translate(0, 1.1, 0.25);
	zarm.base.head.fan = new RKO_Obj(KJS.obj("fanbot_fan"));
	zarm.base.head.fan.setMaterial(material);
	zarm.base.head.fan.transform.translate(0, 1.85961 - 1.1, 0.31 - 0.25);
	//plane.drag.transform.translate(0.0, 0.2, 0);
	//plane.drag.transform.scale(0.6, 0.05, 0.05);
	
	//verybase.hidden = fal;
	
	
	zarm.s = {
		dir: new Vector3f(),
		trace: new Vector3f(),
		attack: false,
		shot: false,
		shotdel: (Math.random() < 0.07) ? 0.05 : 0.4,
		seq: 0,
		firstTick: KJS.currentTick() % 20,
		killable: true,
		health: 100,
		tgtX: fun * 18 * KJS.orandom() - 9,
		tgtY: fun * 18 * KJS.orandom() - 9,
		angle: 0,
		collide: zombieCol,
		onupdate: zombieUpd,
		hit: zombieHit
	}
	
	return zarm;
}

var flyingThing = function() {
	var plane = new SKO_Cube();
	plane.transform.rotateY(Math.PI / 2);
	plane.hidden = true;
	plane.mod = new RKO_Obj(KJS.obj("coolthing"));
	plane.mod.setMaterial(new Mat_Monotexture("bird"));

	camera = new OKO_Camera_();
	//camera.transform.rotateY(-3.14159 / 2);
	camera.mode = 0;
	plane.camera = camera;
	plane.sound = new NKO_Audio(KJS.aud("jet"));
	plane.shoot = new NKO_Audio(KJS.aud("fan_shot"));
	plane.sound.volume = 2.0;
	plane.shoot.pitch = 2;
	plane.shoot.volume = 3.0;

	plane.sound.play(false);
	plane.sound.loop(true);
	
	//plane.drag = new SKO_Cube();
	//plane.drag.transform.translate(0, 2, 0);

	//plane.drag.drag = new SKO_Cube();
	//plane.drag.drag.transform.translate(0, 2, 0);
	//plane.drag.drag.setMaterial(new Mat_Monotexture("K_Cube"));
	
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
			if (normal != null) {
				var doot = normal.dot(this.up);
				//KJS.d.wheels = "(" + Math.floor(this.up.x * 100) + ", " + Math.floor(this.up.y * 100) + ", " + Math.floor(this.up.z * 100) + ")";
				KJS.d.wheels = doot;
				if (doot > -0.97)
					this.speed *= Math.pow(0.004, delta) / 1.0;
				//print(Math.round(normal.dot(0, 1, 0) * 100) / 100);
			}
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
					this.vy -= 0.5 * delta;
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
				
				if (this.speed < 360) {
					this.speed += delta * 70;
				}

				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' '))) {
					this.speed += delta * 70;
				} else {
					this.speed = Math.max(0, this.speed - delta * 10);
				}

				plane.sound.pitch = Math.max(0.02, this.speed * this.speed / 44 / 2000);

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
					
					plane.shoot.play();
					
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
				this.obj.camera.transform.setRotationYXZ(-this.vy * 3 + Math.sin(this.bcam) * Math.PI, -this.vp * 1.4, -this.vr * 3);
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

var fanbot = function() {
	var verybase = new SKO_Cube();
	var material = new Mat_Monotexture("fanbot");
	var camera = new OKO_Camera_(); // third person camera
	verybase.transform.rotateY(Math.PI / 2);
	//verybase.hidden = fal;
	verybase.transform.scale(1.4, 1.4, 1.4);
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
			//this.headRot += (Math.sign(this.headRot + Math.sign(this.cameraP - this.headRot) * delta) == Math.sign(this.headRot)) ? Math.sign(this.cameraP - this.headRot) * delta : this.headRot - this.cameraP;
			this.obj.base.head.transform.setRotationYXZ(0, this.cameraP, 0);
			//this.camera.transform.translate(0, 0, 4);#
			//public void dir(Matrix4f in, float x, float y, float z, float amt, boolean local) {
			//this.camera.transform.rotateY(0.01);
			this.obj.changed = true;
			this.obj.transform.setRotationYXZ(this.angle, 0, 0);
			if (this.on) {
				//this.speed = 0;
				this.moving = false;
				
				if (KJS.i.keyboardDown(KJS.i.toGLFWCode(' '))) {
					this.obj.velocity.y = 2;
				}
				
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
				KJS.d.thing = Math.floor(this.angle * 1000) / 1000 + " " + Math.floor((this.angle + Math.sign(diff) * delta) * 1000) / 1000
				if (Math.abs(diff) > 0.01) {
					if (Math.sign(this.angle - this.prefAngle + Math.sign(diff) * delta) == Math.sign(this.angle - this.prefAngle)) {
						this.angle += Math.sign(diff) * delta;
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
			
			this.fanspeed += Math.sign(this.speed - this.fanspeed) * delta * 3;//Math.max(2, this.fanspeed - delta * 3);
			
		}
	};
	SCN.BotCam = camera;
	return verybase;
}

var start = function() {
	//World.fixFrame = 60;
	KJS.i.setMouseLock(true);
	KJS.s.clear();
	
	KJS.obj("coolthing").load();
	KJS.obj("guy").load();
	KJS.obj("fanbot_base").load();
	KJS.obj("fanbot_head").load();
	KJS.obj("fanbot_fan").load();
	KJS.obj("fanbot_laser").load();
	KJS.obj("deli_4l558").load();

	KJS.texture("fanbot").load();
	KJS.texture("noah").load();
	KJS.texture("brightcube").load();
	KJS.texture("generic").load();
	KJS.texture("water").load();
	KJS.texture("human").load();
	KJS.texture("bird").load();
	KJS.texture("d4l558").load();
	KJS.texture("laser").load();
	KJS.texture("laser2").load();
	KJS.texture("flare").load();
	KJS.texture("blood").load();

	KJS.aud("sss").load();
	KJS.aud("viznut").load();
	KJS.aud("noice").load();
	KJS.aud("bang").load();
	KJS.aud("damage").load();
	KJS.aud("hicol").load();
	KJS.aud("hit").load();
	KJS.aud("cash").load();
	KJS.aud("jet").load();
	KJS.aud("fan_beep").load();
	KJS.aud("fan_shot").load();
	
	KJS.texture("titlepic").load();
	KJS.texture("select").load();
	KJS.texture("select_l").load();
	KJS.texture("select_r").load();
	KJS.obj("durian").load();
	
	KJS.s.load("ktg1:scenes/menu");
};

