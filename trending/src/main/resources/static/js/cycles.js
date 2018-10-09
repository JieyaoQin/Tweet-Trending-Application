

var svg = d3.select("svg"),
    margin = 20,
    diameter = +svg.attr("width"),
    g = svg.append("g")
    	.attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");

var color = d3.scaleLinear()
    .domain([-1, 5])
    .range(["hsl(152,80%,80%)", "hsl(228,30%,40%)"])
    .interpolate(d3.interpolateHcl);

var pack = d3.pack()
    .size([diameter - margin, diameter - margin])
    .padding(2);

// a string
var kwString = $("#keywords").val();
// remove [] and split by comma
var keywords = kwString.substring(1, kwString.length-1).split(", ");
console.log(keywords)

var data = {
	"name": "flare"
}


var children = new Array();
keywords.forEach(function(keyword) {
	children.push({"name": keyword, "size": 1000, "url": "https://google.com"});
});
	
data["children"] = children;



//var data = {
//			 "name": "flare",
//			 "children": [
//			  {"name": "IScaleMap", "size": 2105},
//			  {"name": "LinearScale", "size": 1316},
//			  {"name": "LogScale", "size": 3151},
//			  {"name": "OrdinalScale", "size": 3770},
//			  {"name": "QuantileScale", "size": 2435},
//			  {"name": "QuantitativeScale", "size": 4839},
//			  {"name": "RootScale", "size": 1756},
//			  {"name": "Scale", "size": 4268},
//			  {"name": "ScaleType", "size": 1821},
//			  {"name": keywords, "size": 5833}
//			 ]
//			};


  var root = d3.hierarchy(data)
      .sum(function(d) { return d.size; })
      .sort(function(a, b) { return b.value - a.value; });

  var focus = root,
      nodes = pack(root).descendants(),
      view;

  var circle = g.selectAll("circle")
    .data(nodes)
    .enter().append("circle")
      .attr("class", function(d) { return d.parent ? d.children ? "node" : "node node--leaf" : "node node--root"; })
      .style("fill", function(d) { return d.children ? color(d.depth) : null; });
//      .on("click", function(d) { if (focus !== d) zoom(d), d3.event.stopPropagation(); });
  
  circle.append("svg.a")
	.attr("xlink:href", function(d) {return d.url;});

  var text = g.selectAll("text")
    .data(nodes)
    .enter().append("text")
      .attr("class", "label")
      .style("fill-opacity", function(d) { return d.parent === root ? 1 : 0; })
      .style("display", function(d) { return d.parent === root ? "inline" : "none"; })
      .text(function(d) { return d.data.name; });

  var node = g.selectAll("circle,text");

  svg
      .style("background", color(-1))
      .on("click", function() { zoom(root); });

  //new coordinates that should zoom to
  zoomTo([root.x, root.y, root.r * 2 + margin]);

  function zoom(d) {
    var focus0 = focus; focus = d;

    var transition = d3.transition()
        .duration(d3.event.altKey ? 7500 : 750)
        .tween("zoom", function(d) {
          var i = d3.interpolateZoom(view, [focus.x, focus.y, focus.r * 2 + margin]);
          return function(t) { zoomTo(i(t)); };
        });

    transition.selectAll("text")
      .filter(function(d) { return d.parent === focus || this.style.display === "inline"; })
        .style("fill-opacity", function(d) { return d.parent === focus ? 1 : 0; })
        .on("start", function(d) { if (d.parent === focus) this.style.display = "inline"; })
        .on("end", function(d) { if (d.parent !== focus) this.style.display = "none"; });
  }

  function zoomTo(v) {
    var k = diameter / v[2]; view = v;
    node.attr("transform", function(d) { return "translate(" + (d.x - v[0]) * k + "," + (d.y - v[1]) * k + ")"; });
    circle.attr("r", function(d) { return d.r * k; });
  }