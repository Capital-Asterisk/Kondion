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
	World.Layers.add(new GKO_Layer(GKO_Layer.RENDER));
	
	// Create a board (RKO: Renderable Kondion Object)
	SCN.Apple = new RKO_Board();
	SCN.Apple.pos.z = -1;
	
	World.Layers[0].Apple = SCN.Apple;
	
	//SCN.Apple.Seeds = new RKO_Board();
	//SCN.Apple.Seeds.Poison = new RKO_Board();
	
	//SCN.Foo = new RKO_Board();
	//SCN.Foo.Bar = new RKO_Board();
	//SCN.Foo.Bar.HelloWorld = new RKO_Board();
};