World.passes.add(new GKO_DeferredPass(0));
/*World.passes[0].gui.add(new GUI_Button({
	x: 500,
	y: 500,
	width: 300,
	height: 300,
	text: "neat birds m8"//,
}))*/;

// Add light
SCN.LightB = new RKO_DirectionalLight();
// Add ground, and that moving thing
SCN.Ground = new SKO_InfinitePlane();
SCN.Wallz = new SKO_InfinitePlane();

// Do stuff
SCN.Ground.transform.rotateX(-Math.PI / 2);
SCN.Ground.textureSize = 100;
SCN.Ground.transform.translate(0, 0, 0);
//SCN.LightB
SCN.LightB.color.set(1.0, 1.0, 1.0, 1.0);
SCN.Wallz.transform.translate(0, 0, -40);
SCN.Wallz.textureSize = 50;
SCN.Wallz.transform.rotateX(-Math.PI / 2);

KJS.d["------ Controls ------"] = 0;
KJS.d["Z: Hold to switch camera / character"] = 0;
KJS.d["C: Hold it"] = 0;
KJS.d["WASD: Movement"] = 0;
KJS.d["X: Look Behind"] = 0;
KJS.d["QE: YAW"] = 0;
KJS.d["V: Do not press"] = 0;
KJS.d["----------------------"] = 0;

SCN.guy = fpsGuy();
SCN.planeA = flyingThing();
SCN.planeB = flyingThing();
SCN.FanBot = fanbot();
SCN.FanBot.transform.m30 = 20;
SCN.planeB.transform.m30 = 6211000;
SCN.s.players = [SCN.guy, SCN.planeA, SCN.FanBot, SCN.planeB];
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
KJS.obj("fanbot_base").load();
KJS.obj("fanbot_head").load();
KJS.obj("fanbot_fan").load();

KJS.texture("fanbot").load();
KJS.texture("noah").load();
KJS.texture("human").load();
KJS.texture("bird").load();
KJS.aud("sss").load();
KJS.aud("viznut").load();
KJS.aud("noice").load();
KJS.aud("bang").load();

World.fogIntensity = 0.0001;
World.clearColor.set(0, 0, 0, 1);
World.skyColor.set(1, 1, 1, 1);
World.compMode = KJS.DEBUG;
World.s = {fpav: 0};

World.compositor = function(ctx, passes) {
	//print(ctx);
	//print(passes);
	ctx.fillRgb(1.0, 1.0, 1.0);
	passes[0].render();
	//passes[1].render();
	//passes[2].render();
	ctx.next();
	
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
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
	ctx.scale(0.5, 0.5);
	ctx.fillRgba(0.0, 0.0, 0.0, 0.5);
	ctx.fillRect(0, 0, 500, 68);
	ctx.textAlign = "left";
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	ctx.fillText("Frame r8: " + Math.floor(this.s.fpav) + " + Tap: " + (KJS.i.buttonTap(0)), 10, 30);
	ctx.fillText("Speed: " + (Math.floor(SCN.s.players[SCN.s.currentPlayer].s.speed * 3.6 * 100) / 100) + " km/h", 10, 60);

	passes[0].renderGUI(ctx);

	ctx.fillRgba(0.0, 1.0, 0.0, 1.0);
	ctx.identity();
	ctx.fillRect(KJS.i.getMouseX(), KJS.i.getMouseY(), 10, 10);
	
}
SCN.Camera = new OKO_Camera_();
SCN.Camera.moveTo(0, 4, 0);
SCN.Durian = new SKO_Cube();
SCN.s.onupdate = function() {
	//SCN.eggs.transform.rotateZ(0.01);
	SCN.Wallz.transform.rotateX(Math.sin(KJS.currentTick() / 400) / 250);
	World.camera = this.players[this.currentPlayer].s.camera;
	if (KJS.i.mouseDown(1)) {
		KJS.i.setMouseLock(false);
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('z')) && KJS.currentTick() % 60 == 3) {
		this.players[this.currentPlayer].s.on = false;
		this.currentPlayer = (this.currentPlayer + 1) % this.players.length;
		this.players[this.currentPlayer].s.on = true;
		this.players[this.currentPlayer].s.onselect();
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('c')) && KJS.currentTick() % 60 == 3) {
		World.compMode ++;
		World.compMode %= 3;
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('f'))) {
		KJS.s.clear();
		KJS.s.load("ktg1:scenes/menu");
	}
}

SCN.rescan();