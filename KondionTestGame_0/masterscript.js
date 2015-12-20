/* KONDION TEST
 * THE KONDION masterscript.js
 * Contains everything about KONDION TEST
 * 
 * 
 * hey paolo.
 */

var init = function() {
	KJS.java.issueCommand("^eggs");
	//^kdion.js (scene.addRenderable*(new KJS.r.Board*(*)*))
};

var start = function() {
	KJS.g.setMouseGrab(true);
	SCN.Apple = new KObj_Board();
	SCN.Apple.Seeds = new KObj_Board();
	SCN.Apple.Seeds.Poison = new KObj_Board();
	
	SCN.Foo = new KObj_Board();
	SCN.Foo.Bar = new KObj_Board();
	SCN.Foo.Bar.HelloWorld = new KObj_Board();
};