

// *****************DATA**************************
// a string
var kwString = $("#keywords").val();
// remove [] and split by comma
var keywords = kwString.substring(1, kwString.length-1).split(", ");
console.log("Keywords:", keywords)

// The data json object
var data = {
	"name": "flare"
}

var children = new Array();
keywords.forEach(function(keyword) {
	children.push({"name": keyword, 
					"size": 200, 
					"url": "/cycles/keyword/keyword?keyword="+keyword});
});
data["children"] = children;
console.log("Keyword Json List:", data);


// **********************D3**************************
var width = 800;
var height = 700;

// canvas
var canvas = d3.select("body")
	.append("svg")
	.attr("width", width)
	.attr("height", height).append("g").attr("transform", "translate(50, 50)");

var pack = d3.layout.pack()
	.size([width, height-50])
	.padding(10);

// put data in pack
var nodes = pack.nodes(data);

// for each node
var node = canvas.selectAll(".node")
	.data(nodes)
	.enter()
	.append("g")
		.attr("class", "node")
		.attr("transform", function(d) {return "transform(" + d.x + "," + d.y + ")";});

// append link
node.append("a")
	.attr("xlink:href", function(d) {return d.url;});

//append circle
node.append("circle").attr("r", function(d) {return d.size;})
	.attr("fill", "steelblue")
	.attr("opacity", 0.5);





















