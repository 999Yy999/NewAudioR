function doDataChart2(filterOBJ, n) {
    var outer = d3.map(), 
        inner = [],
        links = [],
        outerId = 0;
    
    filterOBJ.forEach(function(d){
        if (d == null) return;
        var i = { id: 'i' + inner.length, name: d[0], IID: d, related_links: [], c:0 };
        i.related_nodes = [i.id];
        inner.push(i);
    });
    
    data.forEach(function(d1){        
        var o = outer.get(d1[0]);
        o = { 
            name: d1[0], 
            desc: d1[0],
            clas: "outer_node",
            url: d1[2],
            id: 'o' + outerId,
            related_links: [] };
        o.related_nodes = [o.id];
        outerId++;
        outer.set(d1[0], o);
        
        if (!Array.isArray(d1[n])) { d1[n] = [d1[n]]; }
        
        d1[n].forEach(function(d2) {            
            var i = getInner(inner, d2);                
            if (i != null) {
                l = { id: 'l-' + i.id + '-' + o.id, inner: i, outer: o }
                links.push(l);
                i.related_nodes.push(o.id);
                i.related_links.push(l.id);
                o.related_nodes.push(i.id);
                o.related_links.push(l.id);
            }
        });        
        function getInner(inn, name) {           
            if (filterOBJ === sets){ 
                for(var i=0; i < inn.length; i++) { 
                    if (inn[i].IID[0] === name) { inn[i].c++; return inn[i]; }
                }
            } else {
                for(var i=0; i < inn.length; i++) { 
                    if (inn[i].IID === name) { inn[i].c++; return inn[i]; }
                }
            }
            return null;
        }        
    });
    data = { inner: inner, outer: outer.values(), links: links }		
}

function doChart2(diameter, filterOBJ, n) {

    doDataChart2(filterOBJ, n);

    var rect_width = 250;
    var rect_height = 22;
    var il = data.inner.length;
    var ol = data.outer.length;
    var mid = ol/2.0;

    var outer_x = d3.scale.linear()
        .domain([0, mid, mid, ol])
        .range([15, 165, 195, 345]);

    data.outer = data.outer.map(function(d, i) {
        d.x = outer_x(i);
        d.y =  diameter / 2;
        return d;
    });
	
	var inner_y = d3.scale.linear()
        .domain([0, il])
        .range([-(il * rect_height)/2, (il * rect_height)/2]);

    data.inner = data.inner.map(function(d, i) {
        d.x = -(rect_width / 2);
        d.y = inner_y(i);
        return d;
    });

    var box = diameter + 800;
	
    var svg = d3.select("#svgx").append("svg")
        .attr("id", "XXX")
        .attr("width", "100%")
        .attr("height", "100%")
		.attr("viewBox", "0 0 " + box + " " + box)
        .append("g")
		.attr("transform", "translate(" + ((box/2) + 50) + "," + (box/2) + ")");
    
    function projectX(x) {
        return ((x - 90) / 180 * Math.PI) - (Math.PI/2);
    }

    var diagonal = d3.svg.diagonal()
        .source(function(d) { return {"x": d.outer.y * Math.cos(projectX(d.outer.x)), "y": -d.outer.y * Math.sin(projectX(d.outer.x))}; })
        .target(function(d) { return {"x": d.inner.y + rect_height/2, "y": d.outer.x > 180 ? d.inner.x: d.inner.x + rect_width}; })
        .projection(function(d) { return [d.y, d.x]; });

    // links
    var link = svg.append('g')
        .attr('class', 'links')
        .selectAll(".link")
        .data(data.links)
        .enter()
        .append('path')
        .attr('class', 'link')
        .attr('id', function(d) { return d.id })
        .attr("d", diagonal);

    // outer nodes
    var onode = svg.append('g')
        .selectAll(".outer_node")
        .data(data.outer)
        .enter().append("g")
        .attr("class", function(d) { return d.clas })
        .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
        .on("mouseover", mouseover)
        .on("mouseout", mouseout);		
    onode.append("circle")
        .attr('id', function(d) { return d.id })
        .attr("r", 3);		
    onode.append("text")
        .attr('id', function(d) { return d.id + '-txt'; })
        .attr("dy", ".31em")
        .attr("text-anchor", function(d) { return d.x < 180 ? "start": "end"; })
        .attr("transform", function(d) { return d.x < 180 ? "translate(8)": "rotate(180)translate(-8)"; })
		.append("svg:a")
        .attr("xlink:show", function(d){return d.ref!="" ? "new" : "self";})
        .attr("xlink:href", function(d){return d.ref!="" ? d.url : "#";})
		.text(function(d) { return d.desc; });

    // inner nodes
    var inode = svg.append('g')
        .selectAll(".inner_node")
        .data(data.inner)
        .enter().append("g")
        .attr("class", "inner_node")        
        .attr("transform", function(d, i) { return "translate(" + d.x + "," + d.y + ")"})
        .on("mouseover", mouseover)
        .on("mouseout", mouseout);
    inode.append('rect')
        .attr('width', rect_width)
        .attr('height', rect_height)
        .attr("class", function(d) { return d.clas; })
        .attr('id', function(d) { return d.id; });
    inode.append('rect')
        .attr('width', function(d) { return ((rect_width * d.c) / ol) })
        .attr('height', rect_height - 2)
        .style("fill", "d6d6d6").style("stroke-width", "0px").style("stroke", "#000")
        .attr("transform", function(d) { return "translate(1,1)"; })
    inode.append("text")
        .attr('id', function(d) { return d.id + '-txt'; })
        .attr('text-anchor', 'middle')
        .attr("transform", "translate(" + rect_width/2 + ", " + rect_height * .75 + ")")
        .text(function(d) { return d.name; });

    function mouseover(d) {		
        if (d.name != null) {
            $("#articleref").html(d.name);
            $("#mydiv").show();
        }
        d3.selectAll('.links .link').sort(function(a, b){ return d.related_links.indexOf(a.id); });
        for (var i = 0; i < d.related_nodes.length; i++) { d3.select('#' + d.related_nodes[i]).classed('highlight', true);
            d3.select('#' + d.related_nodes[i] + '-txt').attr("font-weight", 'bold');
        }
        for (var i = 0; i < d.related_links.length; i++) { d3.select('#' + d.related_links[i]).classed('highlight', true); }
    }

    function mouseout(d) {
        $("#articleref").html("");
        $("#mydiv").hide();
        for (var i = 0; i < d.related_nodes.length; i++) {
            d3.select('#' + d.related_nodes[i]).classed('highlight', false);
            d3.select('#' + d.related_nodes[i] + '-txt').attr("font-weight", 'normal');
        }
        for (var i = 0; i < d.related_links.length; i++) { d3.select('#' + d.related_links[i]).classed('highlight', false); }
    }

}

function doChart2_print(diameter, filterOBJ, n) {

    doDataChart2(filterOBJ, n);

    var rect_width = 250;
    var rect_height = 22;
    var il = data.inner.length;
    var ol = data.outer.length;
    var mid = ol/2.0;

    var outer_x = d3.scale.linear()
        .domain([0, mid, mid, ol])
        .range([15, 165, 195, 345]);

    data.outer = data.outer.map(function(d, i) {
        d.x = outer_x(i);
        d.y =  diameter / 2;
        return d;
    });
	
	var inner_y = d3.scale.linear()
        .domain([0, il])
        .range([-(il * rect_height)/2, (il * rect_height)/2]);

    data.inner = data.inner.map(function(d, i) {
        d.x = -(rect_width / 2);
        d.y = inner_y(i);
        return d;
    });

    var box = diameter + 800;
	
    var svg = d3.select("#svgx").append("svg")
        .attr("id", "XXX")
        .attr("width", "100%")
        .attr("height", "100%")
		.attr("viewBox", "0 0 " + box + " " + box)
        .append("g")
		.attr("transform", "translate(" + ((box/2) + 50) + "," + (box/2) + ")");
    
    function projectX(x) {
        return ((x - 90) / 180 * Math.PI) - (Math.PI/2);
    }

    var diagonal = d3.svg.diagonal()
        .source(function(d) { return {"x": d.outer.y * Math.cos(projectX(d.outer.x)), "y": -d.outer.y * Math.sin(projectX(d.outer.x))}; })
        .target(function(d) { return {"x": d.inner.y + rect_height/2, "y": d.outer.x > 180 ? d.inner.x: d.inner.x + rect_width}; })
        .projection(function(d) { return [d.y, d.x]; });
    
    // links
    var link = svg.append('g')
        .attr('class', 'links')
        .selectAll(".link")
        .data(data.links)
        .enter()
        .append('path')
        .attr('id', function(d) { return d.id })
        .attr("d", diagonal)
		.style("fill", "none").style("stroke-width", "1px").style("stroke", "#d6d6d6");

    // outer nodes
    var onode = svg.append('g')
        .selectAll(".outer_node")
        .data(data.outer)
        .enter().append("g")
        .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
        .style("font-family", "arial")
		.style("font-size", "20px");		
    onode.append("circle")
        .attr('id', function(d) { return d.id })
        .attr("r", 3)
		.style("fill", "#000").style("stroke-width", "1px").style("stroke", "#000");		
    onode.append("text")
        .attr('id', function(d) { return d.id + '-txt'; })
        .attr("dy", ".31em")
        .attr("text-anchor", function(d) { return d.x < 180 ? "start": "end"; })
        .attr("transform", function(d) { return d.x < 180 ? "translate(8)": "rotate(180)translate(-8)"; })
		.text(function(d) { return d.desc; });

    // inner nodes
    var inode = svg.append('g')
        .selectAll(".inner_node")
        .data(data.inner)
        .enter().append("g")
        .attr("transform", function(d, i) { return "translate(" + d.x + "," + d.y + ")"})
    inode.append('rect')
        .attr('width', rect_width)
        .attr('height', rect_height)
        .attr('id', function(d) { return d.id; })
		.style("fill", "none").style("stroke-width", "1px").style("stroke", "#000");
    inode.append('rect')
        .attr('width', function(d) { return ((rect_width * d.c) / ol) })
        .attr('height', rect_height - 2)
        .style("fill", "#d6d6d6").style("stroke-width", "0px").style("stroke", "#000")
        .attr("transform", function(d) { return "translate(1,1)"; });		
    inode.append("text")
        .attr('id', function(d) { return d.id + '-txt'; })
        .attr('text-anchor', 'middle')
        .attr("transform", "translate(" + rect_width/2 + ", " + rect_height * .75 + ")")
		.style("font-family", "arial")
		.style("font-size", "12px")
        .text(function(d) { return d.name; });
}
