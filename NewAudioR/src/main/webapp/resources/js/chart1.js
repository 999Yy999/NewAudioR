function doData() {
    var Data = [], articles = [];
    data.map(function(item) {
        var t = { "name" : "Data/ARTICLE/" + item[0], "Key": item[0], "url"  : item[2], "Description" : item[3], "compareWith": [ ], "compareStr": "", "isCompared": "" };
        if (item[6].length) { t.compareStr = "Compares with:<ul>"; }
        for (var i=0; i < item[6].length; ++i) { 
            t.compareWith.push("Data/ARTICLE/" + item[6][i]); 
            t.compareStr += "<li>- " + item[6][i] + "</li>";
        }
        if (item[6].length) { t.compareStr += "</ul>"; }
        articles.push(t);
    });
    return articles;
}

function Mytype(d) { return d.name.split("/").reverse()[1]; }

function packageHierarchy(classes) {
    var map = {};
    function find(name, data) {
        var node = map[name], i;
        if (!node) {
            node = map[name] = data || { name: name, children: [] };
            if (name.length) {
                i= name.lastIndexOf("/");
                if (i > -1) {
                    node.parent=find(name.substring(0,i));
                    node.parent.children.push(node);
                    node.key = name.substring(i + 1);
                }
                else { node.parent=null; }
            }
        }
        return node;
    }
    classes.forEach(function (d) {
        if(typeof d.children === 'undefined') { d.children=[]; }
        find(d.name, d);
    });
    return map["Data"];
}

function packageImports(nodes) {
    var map = {}, imports = [];
    nodes.forEach(function (d) { map[d.name] = d; });
    nodes.forEach(function (d) {
        if (d.compareWith) d.compareWith.forEach(function (i) {
            var X = findObj(Data, i);
            imports.push({ source: map[d.name], target: map[X.name] }); 
            if (X.isCompared == "") { X.isCompared = "Is compared by:<ul>"; }
            X.isCompared += "<li>- " + d.Key + "</li>";
        });
    });
    nodes.forEach(function (d) {
         if (d.isCompared != "") { d.isCompared += "</ul>"; }
    });
    return imports;
}

function findObj(obj, name) {
    for (var i = 0; i < obj.length; ++i){
        if (obj[i].name == name) { return obj[i]; }
    }
    return null;
}

function findName(obj, name) {
    for (var i = 0; i < obj.length; ++i){
        if (obj[i].name == name) { return obj[i].name; } 
    }
    return "";
}

function doChart(diameter, tension) {

    Data = doData();

    var radius = (diameter) / 2, innerRadius = radius - 200;

    var cluster = d3.layout.cluster()
        .size([360, innerRadius])
        .sort(null)
        .value(function (d) { return d.name; });

    var bundle = d3.layout.bundle();

    var line = d3.svg.line.radial()
        .interpolate("bundle")
        .tension( tension )
        .radius(function (d) { return d.y; })
        .angle(function (d) { return d.x / 180 * Math.PI; });

    var svg = d3.select("#svgx").append("svg")
        .attr("id", "XXX")
        .attr("width", "100%")
        .attr("height", "100%")
        .attr("viewBox", "0 0 " + (diameter + 100) + " " + (diameter + 100))        
        .append("g")
        .attr("transform", "translate(" + (radius + 50) + "," + radius + ")");

    var nodes = cluster.nodes(packageHierarchy(Data));
    var links = packageImports(nodes);

    var link = svg.append("g")
        .selectAll(".link")
        .data(bundle(links))
        .enter().append("path")
        .each(function (d) { d.source = d[0], d.target = d[d.length - 1]; })
        .attr("class", "link")
        .attr("d", line);

    var node = svg.append("g")
        .selectAll(".node")
        .data(nodes.filter(function (n) { return !n.children; }))
        .enter()
        .append("g")
        .attr("dx", function (d) { return d.x < 180 ? 8 : -8; })
        .attr("dy", ".31em")
        .attr("transform", function (d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")" + (d.x < 180 ? "" : "rotate(180)"); });
    node.append("circle").attr("r", 3);
    node.append("text")
        .attr("class", "node article")
        .attr("dy", 3)
        .attr("dx", function (d) { return d.x < 180 ? 8 : -8; })
        .append("svg:a")
        .attr("xlink:href", function(d){return d.url;})
        .attr("xlink:show", function(d){return d.url!="" ? "new" : "self";})
        .style("text-anchor", function (d) { return d.x < 180 ? "start" : "end"; })
        .text(function (d) { return d.key; })
        .on("mouseover", mouseovered)
        .on("mouseout", mouseouted);
    
    function mouseovered(d) {
        node.each(function (n) { n.target = n.source = false; });
        link.classed("link--target", function (l) { if (l.target === d) return l.source.source = true; })
            .classed("link--source", function (l) { if (l.source === d) return l.target.target = true; })
            .filter(function (l) { return l.target === d || l.source === d; })
            .each(function () { this.parentNode.appendChild(this); });
        node.classed("node--target", function (n) { return n.target; })
            .classed("node--source", function (n) { return n.source; });        
        if (d.Description != null) {
            $("#articleref").html(d.key);
            $("#comparesWith").html(d.compareStr);       
            $("#comparedBy").html(d.isCompared); 
            $("#mydiv").show();
        }
    }

    function mouseouted(d) {
        link.classed("link--target", false).classed("link--source", false);
        node.classed("node--target", false).classed("node--source", false);   
        $("#articleref").html("");
        $("#comparesWith").html("");
        $("#comparedBy").html("");
        $("#mydiv").hide();
    }
    
}

function doChart_print(diameter, tension) {

    Data = doData();

    var radius = (diameter) / 2, innerRadius = radius - 200;

    var cluster = d3.layout.cluster()
        .size([360, innerRadius])
        .sort(null)
        .value(function (d) { return d.name; });

    var bundle = d3.layout.bundle();

    var line = d3.svg.line.radial()
        .interpolate("bundle")
        .tension( tension )
        .radius(function (d) { return d.y; })
        .angle(function (d) { return d.x / 180 * Math.PI; });

    var svg = d3.select("#svgx").append("svg")
        .attr("id", "XXX")
        .attr("width", "100%")
        .attr("height", "100%")
        .attr("viewBox", "0 0 " + (diameter+200) + " " + (diameter+200))
        .append("g")
        .attr("transform", "translate(" + (radius + 50) + "," + radius + ")");
    
    var nodes = cluster.nodes(packageHierarchy(Data));
    var links = packageImports(nodes);

    var link = svg.append("g")
        .selectAll(".link")
        .data(bundle(links))
        .enter().append("path")
        .each(function (d) { d.source = d[0], d.target = d[d.length - 1]; })
        .attr("d", line)
		.style("fill", "none").style("stroke-width", "1px").style("stroke", "#000");
    
    var node = svg.append("g")
        .selectAll(".node")
        .data(nodes.filter(function (n) { return !n.children; }))
        .enter()
        .append("g")
        .attr("dx", function (d) { return d.x < 180 ? 8 : -8; })
        .attr("dy", ".31em")
        .attr("transform", function (d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")" + (d.x < 180 ? "" : "rotate(180)"); });
    node.append("circle").attr("r", 3).style("fill", "#000").style("stroke-width", "1px").style("stroke", "#000");
    node.append("text")
        .attr("dy", 3)
        .attr("dx", function (d) { return d.x < 180 ? 8 : -8; })
        .append("svg:a")
        .style("font-family", "arial")
		.style("font-size", "12px")
        .style("text-anchor", function (d) { return d.x < 180 ? "start" : "end"; })
        .text(function (d) { return d.key; });

}
