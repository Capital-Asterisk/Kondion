World.passes.add(new GKO_DeferredPass(1));
World.passes.add(new GKO_RenderPass(2, KJS.DIFFUSE));
//World.passes[1].width = KJS.width() / 4;
//World.passes[1].height = KJS.height() / 4;
//World.passes[1].sizeOverride = true;
World.passes[1].depthMask = World.passes[0];
/*World.passes[0].gui.add(new GUI_Button({
	x: 500,
	y: 500,
	width: 300,
	height: 300,
	text: "neat birds m8"//,
}))*/;

// Add light
SCN.LightB = new RKO_DirectionalLight();
SCN.LightB.color.set(1.0, 1.0, 1.0, 1.0);
// Add ground and wall
SCN.Floor = new SKO_InfinitePlane();
SCN.Floor.setMaterial(new Mat_Monotexture("generic"));
SCN.Floor.transform.rotateX(-Math.PI / 2);
SCN.Floor.textureSize = 2;
SCN.Floor.transform.translate(0, 0, -64);

SCN.Ceil = new SKO_InfinitePlane();
SCN.Ceil.setMaterial(SCN.Floor.material);
SCN.Ceil.transform.rotateX(Math.PI / 2);
SCN.Ceil.textureSize = 8;
SCN.Ceil.transform.translate(0, 0, -64);

//SCN.Wallz.transform.translate(0, 0, -40);
//SCN.Wallz.textureSize = 				guy.drag.transform.setRotationYXZ(this.y, 0, 0);
//SCN.Wallz.transform.rotateX(-Math.PI / 2);

/*SCN.Platform = new SKO_Cube();
SCN.Platform.transform.translate(0, -64 + 30 / 2, 0);
SCN.Platform.transform.scale(30, 30, 30);
SCN.Platform.anchor = true;
SCN.Platform.setMaterial(new Mat_Monotexture("K_Cube"));
SCN.Platform.s = {
	onupdate: function() {
		if (KJS.i.keyboardDown(KJS.i.toGLFWCode('b')))
			this.obj.rotVelocity.x = 0.1;
	}
}*/

//SCN.PlatformB = new SKO_Cube();
//SCN.PlatformB.transform.translate(0, -10, 0);
//SCN.PlatformB.transform.scale(3, 3, 3);
//SCN.PlatformB.anchor = true;
//SCN.PlatformB.setMaterial(new Mat_Monotexture("K_Cube"));

KJS.d["------ Controls ------"] = 0;
KJS.d["WASD:"] = "Move";
KJS.d["OL"] = "Change sensitivity";
KJS.d["IK"] = "Change cursor";
KJS.d["MOUSE1"] = "Fire laser";
KJS.d["E"] = "Inspect weapon";
KJS.d["F"] = "Grapple hook";
KJS.d["Q"] = "Third person";
KJS.d["V"] = "Slow motion";
KJS.d["----------------------"] = 0;

SCN.guy = betterFps();
SCN.s.hitsound = function() {
	SCN.guy.hitsound.play();
}
//SCN.planeA = flyingThing();
//SCN.planeB = flyingThing();
//SCN.FanBot = fanbot();
//SCN.guy.transform.m30 = 40;
//SCN.FanBot.transform.m30 = 20;
//SCN.planeB.transform.m30 = 6211000;
//SCN.s.players = [SCN.guy, SCN.planeA, SCN.FanBot, SCN.planeB];
SCN.s.players = [SCN.guy];
SCN.guy.s.on = true;
SCN.s.currentPlayer = 0;

var fun = 3;
var randomSize = 0;
var mat = new Mat_Monotexture("brightcube");

for (var i = -fun; i < fun; i++) {
	for (var j = -fun; j < fun; j++) {
		
		randomSize = Math.floor(Math.random() * 12 + 1);
		heightAdd = 0;
		if (Math.random() > 0.85) {
			heightAdd += Math.floor(Math.random() * 30 + 8);
		}
		var plat = new SKO_Cube();
		//plat = new SKO_Cube();
		plat.transform.translate(j * 18, -64 + (randomSize + heightAdd) / 2, i * 18);
		plat.transform.scale(randomSize, randomSize + heightAdd, randomSize);
		plat.anchor = true;
		plat.setMaterial(mat);
		SCN[SCN.nextName("RANDOMCUBE_")] = plat;
	}
	
}

//shape generator thing
var sides = 6;
for (var i = 0; i < sides; i++) {
	
	var wall = new SKO_InfinitePlane();
	wall.setMaterial(SCN.Floor.material);
	wall.transform.rotateY((Math.PI * 2) / sides * i);
	wall.textureSize = 8;
	wall.transform.translate(0, 0, -fun * 18 * Math.sqrt(2) - 9);
	//wall.collideType = 4;
	SCN[SCN.nextName("Side")] = wall;
	
}

for (var i = 0; i < 16; i++) {
	var z = zombie();
	z.transform.m30 = fun * 18 * KJS.orandom() - 9;
	z.transform.m32 = fun * 18 * KJS.orandom() - 9;
	SCN[SCN.nextName("fanbot")] = z;
	SCN.rescan();
}

for (var i = 0; i < 0; i++) {
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


World.fogIntensity = 0.0001;
World.clearColor.set(0, 0, 0, 1);
World.skyColor.set(0, 0, 0, 1);
World.compMode = KJS.COMPOSITE;
World.s = {fpav: 0, siav: 0, lines: 3, sense: 3};

World.compositor = function(ctx, passes) {
	//print(ctx);
	//print(passes);
	ctx.fillRgb(1.0, 1.0, 1.0);
	passes[0].render();
	passes[1].render();
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
	ctx.globalCompositeOperation = KJS.ADD;
	ctx.drawImage(passes[1], 0, 0, passes[0].width, passes[0].height);
	ctx.globalCompositeOperation = KJS.DEFAULT;
	ctx.fillRgba(1.0, 1.0, 1.0, SCN.s.blood);
	SCN.s.blood = Math.min(2, Math.max(0, SCN.s.blood - delta));
	ctx.drawImage(KJS.texture("blood"), 0, 0, passes[0].width, passes[0].height);
	//ctx.drawImage(passes[1], passes[0].width / 2, 0, passes[1].width / 6, passes[1].height / 6);
	//ctx.deferredRender(passes[0], passes[2], passes[1],
	//	0, 0, 0, 0, 0, 0,
	//	passes[0].width, passes[0].height);
	ctx.scale(0.5, 0.5);
	ctx.fillRgba(0.0, 0.0, 0.0, 0.5);
	ctx.fillRect(0, 0, KJS.width() * 2, 68);
	ctx.textAlign = "left";
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	ctx.fillText("Sense: " + this.s.sense + " Frame r8: " + Math.floor(this.s.fpav), 10, 30);
	ctx.fillText("Structural Integrity: [" + KJS.repeat("I", SCN.s.players[SCN.s.currentPlayer].s.health) + KJS.repeat(" ", 100 - Math.floor(SCN.s.players[SCN.s.currentPlayer].s.health)) + "]", KJS.width() / 3, 30);
	ctx.fillText("Timescale: " + Math.floor(KJS.timescale * 100) / 100, 10, 60);
	ctx.fillText("HOLD [C] TO SEE DEBUG MODE / CONTROLS        Score: " + Math.floor(SCN.s.score), KJS.width() / 3, 60);
	
	// Draw crosshair
	ctx.identity();
	ctx.translate(KJS.width() / 2, KJS.height() / 2);
	var si = 40 + SCN.s.shake * 3000 + SCN.s.players[SCN.s.currentPlayer].velocity.length() * 5;
	this.s.siav -= (this.s.siav - si) / 20;
	var hei = 20;
	for (var i = 0; i < this.s.lines; i++) {
		ctx.rotate((Math.PI * 2) / this.s.lines);
		ctx.fillRgba(0.0, 0.0, 0.0, 1.0);
		ctx.fillRect(-2, -this.s.siav, 4, hei);
		ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
		ctx.fillRect(-1, -this.s.siav + 1, 2, hei - 2);
	}
	
	if (SCN.s.hitmarker > 0.02) {
		ctx.rotate((Math.PI * 2) / this.s.lines / 2);
		for (var i = 0; i < this.s.lines; i++) {
			ctx.rotate((Math.PI * 2) / this.s.lines);
			ctx.fillRgba(0.0, 0.0, 0.0, 1.0);
			ctx.fillRect(-2, -32, 4, hei * 0.8);
			ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
			ctx.fillRect(-1, -32 + 1, 2, hei * 0.8 - 2);
		}
	}
	
	if (SCN.s.players[SCN.s.currentPlayer].s.health <= 0) {
		ctx.fillRgba(0.0, 0.0, 0.0, 0.5);
		ctx.fillRect(-KJS.width() / 2, -50, KJS.width() * 2, 100);
		ctx.textAlign = "center";
		ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
		ctx.fillText("Dead__You", 0, -0);
		ctx.scale(0.5, 0.5);
		ctx.fillText("Final score: " + Math.floor(SCN.s.score), 0, 20);
	}
	passes[0].renderGUI(ctx);

	ctx.fillRgba(0.0, 1.0, 0.0, 1.0);
	ctx.identity();
	//ctx.fillRect(KJS.i.getMouseX(), KJS.i.getMouseY(), 10, 10);
	
}
SCN.Camera = new OKO_Camera_();
SCN.Camera.moveTo(0, 4, 0);
SCN.s.addFans = 0;
SCN.s.shake = 0;
SCN.s.blood = 0;
SCN.s.score = 0;
SCN.s.hitmarker = 0;
SCN.s.onupdate = function() {
	//SCN.eggs.transform.rotateZ(0.01);
	//SCN.Wallz.transform.rotateX(Math.sin(KJS.currentTick() / 400) / 250);
	World.camera = this.players[this.currentPlayer].s.camera;
	if (KJS.i.mouseDown(1)) {
		KJS.i.setMouseLock(false);
	}
	
	if (KJS.i.mouseDown(0)) {
		KJS.i.setMouseLock(true);
	}
	
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('z')) && KJS.currentTick() % 60 == 3) {
		this.players[this.currentPlayer].s.on = false;
		this.currentPlayer = (this.currentPlayer + 1) % this.players.length;
		this.players[this.currentPlayer].s.on = true;
		this.players[this.currentPlayer].s.onselect();
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('i')) && KJS.currentTick() % 60 == 3) {
		World.s.lines ++;
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('k')) && KJS.currentTick() % 60 == 3) {
		World.s.lines --;
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('o')) && KJS.currentTick() % 30 == 3) {
		World.s.sense ++;
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('l')) && KJS.currentTick() % 30 == 3) {
		World.s.sense --;
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('c')) && KJS.currentTick() % 60 == 3) {
		World.compMode ++;
		World.compMode %= 3;
	}
	
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('v')))
		KJS.timescale = Math.max(0.4, KJS.timescale - 0.05);
	else 
		KJS.timescale = Math.min(1.0, KJS.timescale + 0.02);
	
	this.hitmarker = Math.max(0, this.hitmarker - delta);
	
	if (this.addFans != 0) {
		
		print("ADDING FAN")
		
		this.addFans --;
		
		var z = zombie();
		z.transform.m30 = fun * 18 * KJS.orandom() - 9;
		z.transform.m32 = fun * 18 * KJS.orandom() - 9;	
		SCN[SCN.nextName("zombie")] = z;
		SCN.rescan();
		
	}
	
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('p'))) {
		KJS.i.setMouseLock(false);
		KJS.s.clear();
		KJS.s.load("ktg1:scenes/menu");
	}
}

SCN.rescan();


var serverTest = function() {
	SCN.Server = new GKO_Server();
	SCN.Server.start(24558, 5);
	//SCN.Server.track(SCN.FanBot, 1);
}

var connectClientTest = function(host) {
	var client = new GKO_Client();
	client.s = {
		func: [
			function() { // make fan
				return null;
			},
			function() { // make fan
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
				SCN[SCN.nextName("Fans")] = verybase;
				return verybase;
			}
		]
	}
	
	client.connect(host, 24558);
	SCN[SCN.nextName("Client")] = client;
}