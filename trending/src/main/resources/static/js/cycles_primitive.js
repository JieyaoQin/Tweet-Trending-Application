

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
	children.push({"name": keyword, "size": 1000});
});
data["children"] = children;
console.log("Keyword Json List:", data);


// **********************D3**************************
var width = 2000;
var height = 700;

var holder = d3.select("body")
	.append("svg")
	.attr("width", width)    
	.attr("height", height); 


for(var i = 0; i < keywords.length; i++) {
	// draw circle
	drawCircle(keywords[i], 50, 100*(i+1), 50);

	// show text
	drawText(keywords[i], 100*(i+1), 50);
}


function drawCircle(keyword, r, cx, cy) {
	holder.append("a")
		.attr("xlink:href", "/cycles/keyword/keyword?keyword="+keyword)
		.append("circle")
	    .attr("r", r)
	    .attr("cx", cx)
	    .attr("cy", cy)
	    .style("fill", "aliceblue")
	    .attr("rx", 10)
	    .attr("ry", 10);
}

function drawText(keyword, x, y) {
	holder.append("text")
	    .attr("x", x)
	    .attr("y", y)
	    .style("fill", "black")
	    .style("font-size", "20px")
	    .attr("dy", ".35em")
	    .attr("text-anchor", "middle")
	    .style("pointer-events", "none")
	    .text(keyword);
}















