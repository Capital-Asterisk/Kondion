// Make objs into arrays
// Used for KONDION
// Paste into chrome js console
// Also this doesn't work
// -Capital_Asterisk

var data = window.prompt("Eggs here", "").split("\n");
var output = [];
var vstart = -1;
var vtstart = -1;
var vnstart = -1;
var fstart = -1;

var splitAdd = function(str) {
	var eggs = str.split(" ");
	// var = 1 skips first
	for (var i = 1; i < eggs.length; i++) {
		output.push(Number(eggs[i]));
	}

}

for (var i = 0; i < data.length; i++) {
	if (vstart == -1 && data[i].charAt(0) == "v")
		vstart = i;
	if (vtstart == -1 && data[i].indexOf("vt") == 0)
		vtstart = i;
	if (vnstart == -1 && data[i].indexOf("vn") == 0)
		vnstart = i;
	if (data[i].charAt(0) == "f") {
		console.log("FACE");
		var face = data[i].split(" ");
		for (var j = 1; j < face.length; j++) {
			var vert = face[j].split("/");
			console.log(
			vert[0] + " V: (" + data[parseInt(vert[0]) + vstart - 1] +
			vert[1] + ") C: (" + data[parseInt(vert[1]) + vtstart - 1] +
			vert[2] + ") N: " + data[parseInt(vert[2]) + vnstart - 1]);
			splitAdd(data[parseInt(vert[0]) + vstart - 1]); // Vertex first
			splitAdd(data[parseInt(vert[2]) + vnstart - 1]); // Normal
			splitAdd(data[parseInt(vert[1]) + vtstart - 1]); // Tex Coord
		}
	}
	
}
console.log("------------------OUTPUT------------------");
console.log("Size: " + output.length + " elements.");
var final = "new float[] {";
for (var i = 0; i < output.length; i++) {
	final += output[i] + "f, ";
}
final += "};"
//console.log(output);