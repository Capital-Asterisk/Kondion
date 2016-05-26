print("woot");
World.passes.add(new GKO_DeferredPass(0, 1));
World.passes.add(new GKO_RenderPass(0, 2));
World.passes[0].width = KJS.width();
World.passes[0].height = KJS.height();
World.passes[0].sizeOverride = true;

var buttonDraw = function(ctx, state) {
	
	switch (state) {
		case 0:
			ctx.fillRgba(1.0, 1.0, 1.0, 0.1);
			break;
		case 2:
			ctx.fillRgba(1.0, 1.0, 1.0, 0.2);
			break;
		default:
		break;
	}
	//ctx.rotate(state);
	ctx.fillRect(-this.width / 2, -this.height / 2, this.width, this.height);
	ctx.fillRgb(1.0, 1.0, 1.0);
	ctx.textAlign = "center";
	ctx.fillText(this.s.text, 0, 5);

}

var buttonA = new GUI_Button({
	x: 0.7140625 * KJS.width(),
	y: 514 / 1080 * KJS.height(),
	width: 0.30625 * KJS.width(),
	height: 76 / 1080 * KJS.height(),
	draw: buttonDraw,
	text: "Some Game",
	click: function(button, mouse) {
		print("Loading some game...");
		unLoad();
		KJS.i.setMouseLock(true);
		KJS.s.clear();
		KJS.s.load("ktg1:scenes/gameA");
	}
});
var buttonB = new GUI_Button({
	x: 0.7140625 * KJS.width(),
	y: 514 / 1080 * KJS.height() + 90 / 1080 * KJS.height(),
	width: 0.30625 * KJS.width(),
	height: 76 / 1080 * KJS.height(),
	draw: buttonDraw,
	text: "Bird crushing",
	click: function(button, mouse) {
		print("Game B");
		unLoad();
		KJS.i.setMouseLock(true);
		KJS.s.clear();
		KJS.s.load("ktg1:scenes/dogefite");
	}
});
var buttonC = new GUI_Button({
	x: 0.7140625 * KJS.width(),
	y: 514 / 1080 * KJS.height() + 90 / 1080 * KJS.height() * 2,
	width: 0.30625 * KJS.width(),
	height: 76 / 1080 * KJS.height(),
	draw: buttonDraw,
	text: "Join server",
	click: function(button, mouse) {
		print("Game C");
	}
});
buttonA.down = buttonB;
buttonB.down = buttonC;
buttonC.down = buttonA;
buttonA.up = buttonC;
buttonB.up = buttonA;
buttonC.up = buttonB;
World.passes[1].gui.add(buttonA);
World.passes[1].gui.add(buttonB);
World.passes[1].gui.add(buttonC);

KJS.texture("titlepic").load();
KJS.texture("select").load();
KJS.texture("select_l").load();
KJS.texture("select_r").load();
KJS.obj("durian").load();

var unLoad = function() {
	KJS.texture("titlepic").unLoad();
	KJS.texture("select").unLoad();
	KJS.texture("select_l").unLoad();
	KJS.texture("select_r").unLoad();
	KJS.obj("durian").unLoad();
}

SCN.LightB = new RKO_DirectionalLight();
SCN.Durian = new RKO_Obj(KJS.obj("durian"));
SCN.Camera = new OKO_Camera_();
SCN.Durian.setMaterial(new Mat_FlatColor());
SCN.Durian.getMaterial().setColorf(1.0, 1.0, 1.0);
///SCN.Camera.transform.translate(0, 0, 4);
//SCN.Camera.mode = 1;
SCN.LightB.color.set(1.0, 1.0, 1.0, 1.0);
SCN.Camera.moveTo(0, 0, 5);
World.camera = SCN.Camera;
//SCN.Durian.moveTo(SCN.Camera.transfor);

World.fogIntensity = 0.0001;
World.s = {
		fpav: 0,
		first: true,
		mode: 0,
		angle: 0,
		selectorA: {
			x: 0,
			y: -30,
		},
		selectorB: {
			x: -30,
			y: -30,
		}
};
World.clearColor.set(0, 0, 0, 1);
World.skyColor.set(0.02, 0.02, 0.02, 1);
World.compMode = KJS.COMPOSITE;

World.compositor = function(ctx, passes) {
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	passes[0].render();
	if (World.s.first) {
		World.s.first = false;
		passes[1].render();
	}
	ctx.next();
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	var eggs = Math.max(Math.min(KJS.fps(), 1000), 0);
	this.s.fpav -= (this.s.fpav - eggs) / 20;//Math.floor(this.s.fpav - KJS.fps());
	ctx.drawImage(passes[0], 0, 0, passes[1].width, passes[1].height);
	ctx.drawImage(KJS.texture("titlepic"), 0, 0, passes[1].width, passes[1].height);
	
	ctx.scale(0.5, 0.5);
	ctx.fillRgba(0.0, 0.0, 0.0, 0.5);
	ctx.fillRect(0, 0, 500, 68);
	ctx.textAlign = "left";
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	ctx.fillText("Frame r8: " + Math.floor(this.s.fpav) + " + Tap: " + (KJS.i.buttonTap(0)), 10, 30);
	ctx.fillText("Selected: " + passes[1].gui.indexOf(passes[1].selected), 10, 60);
	ctx.identity();
	passes[1].renderGUI(ctx);
	if (passes[1].selected != null) {
		
		
		/*if (this.s.angle != (this.s.mode * Math.PI * 2 / 3 + Math.PI / 6)) {
			if (sign(this.s.angle) == sign(this.s.angle + sign(this.s.mode * Math.PI * 2 / 3 + Math.PI / 6 - this.s.angle) / 5)) {
				this.s.angle += sign(this.s.mode * Math.PI * 2 / 3 + Math.PI / 6 - this.s.angle) / 5;
				
			} else {
				this.s.angle = this.s.mode * Math.PI * 2 / 3 + Math.PI / 6;
			}
		}*/
		
		this.s.angle += (this.s.mode * Math.PI * 2 / 3 + Math.PI / 6 - this.s.angle) / 3;
		
		// Draw Left Selector (A)
		this.s.selectorA.x += ((passes[1].selected.x + 0.30625 / 2 * KJS.width() - 76 / 1080 * KJS.height() / 2) - this.s.selectorA.x) / 3;
		this.s.selectorA.y += ((passes[1].selected.y + 76 / 1080) - this.s.selectorA.y) / 3; //* KJS.height() / 2;
		ctx.save();
		
		ctx.translate(this.s.selectorA.x, this.s.selectorA.y);
		ctx.rotate(this.s.angle);
		ctx.drawImage(KJS.texture("select"), 
				-76 / 1080 * KJS.height() / 2, 
				-76 / 1080 * KJS.height() / 2, 
				76 / 1080 * KJS.height(), 
				76 / 1080 * KJS.height()
				);
		
		ctx.restore();
		
		// Draw Right Selector (A)
		this.s.selectorB.x += ((passes[1].selected.x - 0.30625 / 2 * KJS.width() + 76 / 1080 * KJS.height() / 2) - this.s.selectorB.x) / 3;
		this.s.selectorB.y += ((passes[1].selected.y + 76 / 1080) - this.s.selectorB.y) / 3; //* KJS.height() / 2;
		ctx.save();
		
		ctx.translate(this.s.selectorB.x, this.s.selectorB.y);
		ctx.rotate(-this.s.angle);
		ctx.drawImage(KJS.texture("select"), 
				-76 / 1080 * KJS.height() / 2, 
				-76 / 1080 * KJS.height() / 2, 
				76 / 1080 * KJS.height(), 
				76 / 1080 * KJS.height()
				);
		
		ctx.restore();
		/*ctx.drawImage(KJS.texture("select"), 
				passes[1].selected.x + 0.30625 / 2 * KJS.width() - 76 / 1080 * KJS.height(), 
				passes[1].selected.y - 76 / 1080 * KJS.height() / 2, 
				76 / 1080 * KJS.height(), 
				76 / 1080 * KJS.height()
				);
		ctx.drawImage(KJS.texture("select"), 
				passes[1].selected.x - 0.30625 / 2 * KJS.width(), 
				passes[1].selected.y - 76 / 1080 * KJS.height() / 2, 
				76 / 1080 * KJS.height(), 
				76 / 1080 * KJS.height()
				);*/
	}

	ctx.fillRgba(0.0, 1.0, 0.0, 1.0);
	ctx.identity();
	ctx.fillRect(KJS.i.getMouseX(), KJS.i.getMouseY(), 10, 10);
	
}
SCN.s.upbutton = KJS.i.getButtonIndex("up");
SCN.s.downbutton = KJS.i.getButtonIndex("down");
SCN.s.selectbutton = KJS.i.getButtonIndex("select");
SCN.s.onupdate = function() {
	//KJS.freeCam();
	SCN.Camera.setZ(5 + Math.cos(KJS.currentTime() / 6000));
	SCN.Durian.eularYXZ(KJS.currentTime() / 2000, Math.sin(KJS.currentTime() / 7000), Math.cos(KJS.currentTime() / 9000) * 2);
	if (KJS.i.buttonTap(SCN.s.downbutton) == 1) {
		World.passes[1].guiShift(KJS.DOWN);
		World.s.mode -= 1;
	}
	if (KJS.i.buttonTap(SCN.s.upbutton) == 1) {
		World.passes[1].guiShift(KJS.UP);
		World.s.mode += 1;
	}
	if (KJS.i.buttonTap(SCN.s.selectbutton) == 1) {
		World.passes[1].guiClick(0, false);
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('c')) && KJS.currentTick() % 60 == 3) {
		World.compMode ++;
		World.compMode %= 3;
	}
}

SCN.rescan();