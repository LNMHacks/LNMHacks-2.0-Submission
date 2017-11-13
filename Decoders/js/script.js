	var left_ring=0;
	var right_ring=0;
	var right_middle = 0;
	var left_middle = 0;
	var right_index = 0;
	var left_index=0;
	var left_small=0;
	var right_small=0;
	var thumb = 0;
	var time_left_ring = 0;
	var time_right_middle = 0;
	var time_right_ring = 0;
	var time_left_middle = 0;
	var time_right_index = 0;
	var time_left_index = 0;
	var time_right_small = 0;
	var time_left_small = 0;
	var time_thumb =0;
	var start = 0;
	var end = 0;
	start = new Date().getTime();

	var dps1 = [{x: 0, y: 0}];
	var dps2 = [{x: 0, y: 0}];
	var dps3 = [{x: 0, y: 0}];
	var dps4 = [{x: 0, y: 0}];
	var dps5 = [{x: 0, y: 0}];
	var dps6 = [{x: 0, y: 0}];
	var dps7 = [{x: 0, y: 0}];
	var dps8 = [{x: 0, y: 0}];
	var dps9 = [{x: 0, y: 0}];
	
	function myFunction(event) {

	    var x = event.which || event.keyCode;   
	       //dataPoints. 

      var chart = new CanvasJS.Chart("chartContainer",{
      	title :{
      		text: "Live Data"
      	},
      	axisX: {						
      		title: "Axis X Title(count)"
      	},
      	axisY: {						
      		title: "Units(speed)"
      	},
      	data: [{
      		type: "line",
      		dataPoints : dps1,
      		color: "#9966cc",
			
      	},
		{
      		type: "line",
      		dataPoints : dps2,
      		color:"#669999",
		},
		{
      		type: "line",
      		dataPoints : dps3,
      		color:"darkgrey",
		},
		{
      		type: "line",
      		dataPoints : dps4,
      		color:"#cc6666",
		},
		{
      		type: "line",
      		dataPoints : dps5,
      		color:"#ff9999",
		},
		{
      		type: "line",
      		dataPoints : dps6,
			color:"#99cc99",
		},
		{
      		type: "line",
      		dataPoints : dps7,
      		color:"#ffff33",
		},
		{
      		type: "line",
      		dataPoints : dps8,
      		color:"#ff6633",
		},
		{
      		type: "line",
      		dataPoints : dps9,
      		color:"#0066ff",
		}					
			]
      });
	  

      chart.render();
	  

      function updateChart1(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps1.push({x: xVal,y: yVal});
      	if (dps1.length >  10 )
      	{
      		dps1.shift();				
      	}
      	chart.render();		 

};
 
	 function updateChart2(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps2.push({x: xVal,y: yVal});
      	if (dps2.length >  10 )
      	{
      		dps2.shift();				
      	}
      	chart.render();		 

};
	
	function updateChart3(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps3.push({x: xVal,y: yVal});
      	if (dps3.length >  10 )
      	{
      		dps3.shift();				
      	}
      	chart.render();		 

};

	function updateChart4(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps4.push({x: xVal,y: yVal});
      	if (dps4.length >  10 )
      	{
      		dps4.shift();				
      	}
      	chart.render();		 

};
	function updateChart5(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps5.push({x: xVal,y: yVal});
      	if (dps5.length >  10 )
      	{
      		dps5.shift();				
      	}
      	chart.render();		 

};
	function updateChart6(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps6.push({x: xVal,y: yVal});
      	if (dps6.length >  10 )
      	{
      		dps6.shift();				
      	}
      	chart.render();		 

};
	function updateChart7(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps7.push({x: xVal,y: yVal});
      	if (dps7.length >  10 )
      	{
      		dps7.shift();				
      	}
      	chart.render();		 

};
	function updateChart8(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps8.push({x: xVal,y: yVal});
      	if (dps8.length >  10 )
      	{
      		dps8.shift();				
      	}
      	chart.render();		 

};
	function updateChart9(xx,yy) {
      		var xVal = xx;
        	var yVal = yy;
      	dps9.push({x: xVal,y: yVal});
      	if (dps9.length >  10 )
      	{
      		dps9.shift();				
      	}
      	chart.render();		 

};
	

	    if(x==50 || x==119 || x==115 || x==120 || x==64 || x==87 || x==83 || x==88){
	    	left_ring++;
			end = new Date().getTime();
			time_left_ring = (end - start)/1000;
			console.log(1/time_left_ring);
			start = new Date().getTime();	
			updateChart1(left_ring,(1/time_left_ring));

	    }else if(x==56 || x==105 || x==107 || x==44 || x==60 || x==42 || x==73 || x==75){
	    	right_middle++;
	    	end = new Date().getTime();
			time_right_middle = (end - start)/1000;
			console.log(1/time_right_middle);
			start = new Date().getTime();	
			updateChart2(right_middle,1/time_right_middle);

	   	}else if(x==57 || x==111 || x==108 || x==46 || x==62 || x==40 || x==79 || x==76){
	    	right_ring++;
	    	end = new Date().getTime();
			time_right_ring = (end - start)/1000;
			console.log(1/time_right_ring);
			start = new Date().getTime();	
			updateChart3(right_ring,1/time_right_ring);
			
	   	}

	    else if(x==51 || x==35 || x==101 || x==100 || x==99 || x==69 || x==67 || x==68){
	    	left_middle++;
	    	end = new Date().getTime();
			time_left_middle = (end - start)/1000;
			console.log(1/time_left_middle);
			start = new Date().getTime();	
			updateChart4(left_middle,1/time_left_middle);
	    }

	    else if(x==89 || x==85 || x==72 || x==74 || x==77 |x==78 || x==54 || x==94 || x==55 || x==38 || x==121 || x==117 || x==104 || x==106 || x==110 || x==109){
	    	right_index++;
	    	end = new Date().getTime();
			time_right_index = (end - start)/1000;
			console.log(1/time_right_index);
			start = new Date().getTime();	
			updateChart5(right_index,1/time_right_index);
	    }
		 else if(x==82 || x==84 || x==70 || x ==71 || x==86 || x==66 ||x==52 || x==36 || x==53 || x==37 || x==114 || x==116 || x==102 || x==103 || x==118|| x==98){
	    	left_index++; 
	    	end = new Date().getTime();
			time_left_index = (end - start)/1000;
			console.log(1/time_left_index);
			start = new Date().getTime();	
			updateChart6(left_index,1/time_left_index);

	    }else if(x==48 || x== 41 || x==45 || x==95 || x== 80 || x==112 || x==91 || x== 123 || x==125 || x==93 || x==59 || x==58 || x==34 || x== 39 || x==13 || x==47 || x==63){
	    	right_small++;
	    	end = new Date().getTime();
			time_right_small = (end - start)/1000;
			console.log(1/right_small);
			start = new Date().getTime();	
			updateChart7(right_small,1/time_right_small);
	    }

	    else if(x==113 || x== 81 || x==97 || x==65 || x== 122 || x==90 || x==49 || x== 33){
		    left_small++;
		    end = new Date().getTime();
			time_left_small = (end - start)/1000;
			console.log(1/time_left_small);
			start = new Date().getTime();
			updateChart8(left_small,1/time_left_small);	

	    }else if(x == 32){
	    	thumb++;	
	    	end = new Date().getTime();
			time_thumb = (end - start)/1000;
			console.log(1/time_thumb);
			start = new Date().getTime();
			updateChart9(thumb,1/time_thumb);		 
	    }

	}	

	$("#btn").on('click',function(){

		$("#demo").removeClass("hor");

		$("#demo1").html(
			"Left-Index = " + left_index +
			"<br>Left-Middle = " + left_middle +
			"<br>Left-Ring = " + left_ring +
			"<br>Left-Small = " + left_small +
			"<br> Thumb = "  + thumb
		);	

		$('#demo2').html(
			"Right-Index = " + right_index +
			"<br>Right-Middle = " + right_middle +
			"<br>Right-Ring = " + right_ring +	
			"<br>Right-Small = " + right_index 
			);


		 left_ring=0;
		 right_ring=0;
		 right_middle = 0;
		 left_middle = 0;
		 right_index = 0;
		 left_index=0;
		 left_small=0;
		 right_small=0;
		 thumb = 0;
		 $(".text").val('');
	})

	$("#prospects_form").submit(function(e) {
	    e.preventDefault();
	});

	var arr = [ "Word counter is a word count and a character count tool. Simply place your cursor into the box and begin typing. Word counter will automatically count the number of words and characters as you type. You can also copy and paste a document you have already written into the word counter box and it will display the word count and character numbers for that piece of writing.",
				"Scolding is something common in student life. Being a naughty boy, I am always scolded by my parents. But one day I was severely scolded by my English teacher. She infect teaches well. But that day, I could not resist the temptation that an adventure of Nancy Drew offered. While she was teaching, I was completely engrossed in reading that book. Nancy Drew was caught in the trap laid by some smugglers and it was then when I felt a light tap on my bent head. The teacher had caught me red handed. She scolded me then and there and insulted me in front of the whole class.",
				" I was embarrassed. My cheeks burned being guilty conscious. When the class was over, I went to the teacher to apologize. When she saw that I had realized my mistake, she cooled down and then told me in a very kind manner how disheartening it was when she found any student not paying attention. I was genuinely sorry and promised to myself never to commit such a mistake again."
			  ]

	$("#content").html(arr[Math.floor((Math.random() * 3))])

