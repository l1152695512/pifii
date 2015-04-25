function LinkChartsExtWindow(linkVar){
	 var params=linkVar.split('=');
	 var chartTitle=params[0];
     var jsonLink= eval("("+params[1]+")");
	 
	 var lineCharts= new Ext.Panel({
		title:'lineCharts',
		layout:'fit',
		region:'center',
		id:'chartsDiv',
		listeners: {
    		render: function(p) {
			  var charts="<chart caption='"+chartTitle+"折线分析图 ' showvalues='0'  palette='5' canvasBgColor='D9E5F1' canvasBaseColor='C91D1D' numberSuffix='度' numDivLines='4' formatNumberScale='0' >";
		var categories="<categories>";
		var category="";
		var dataset="<dataset seriesName='"+chartTitle+"' showvalues='0'   rotateValues='1' placeValuesInside='1' forceYAxisValueDecimals='1'  yAxisValueDecimals='2' >";
		var dataSetValue="";
		var datasetLink="<dataset seriesname='"+chartTitle+"折线' renderAs='Line' color='F6BD0F' anchorSides='4' anchorRadius='5' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2'>";
	    var linkSet="";
	    var num=0;
	    
	    var result = [];
	    for(var key in jsonLink.jsonData){
	    	result.push(jsonLink.jsonData[key]);
	    }
	    var tempval;
	 for(var key in jsonLink.jsonData){
		 
	 	  category+=" <category label='"+key+"年' />";
		  dataSetValue+="<set value='"+jsonLink.jsonData[key]+"' color='"+renderXml(key)+"' />";
		  
		  if(num != 0)
		  {
			  if(result[num-1] !=0)
			  {
				 tempval= ((result[num]-result[num-1])/result[num-1]*100).toFixed(2);
			  }
			  else
			  {
				 tempval= ((result[num]-result[num-1]) * 100).toFixed(2);
			  }
			  linkSet+="<set value='"+jsonLink.jsonData[key]+"' showValue='1' displayValue='"+tempval+"%'/>";
		  }
		  else
		  {
			  linkSet+="<set value='"+jsonLink.jsonData[key]+"' showValue='1' displayValue=' '/>";
		  }
		  num++;
//		  linkSet+="<set value='"+jsonLink.jsonData[key]+"' showValue='1' displayValue='rewrwe'/>";

         
      }
	  
	 var chartsLine= charts+categories+category+"</categories>"+dataset+dataSetValue+"	</dataset>"+datasetLink+linkSet+"</dataset>"+"</chart>";
	 var delay_chart = new FusionCharts('charts/MSColumnLine3D.swf', 'chartsId',Ext.getCmp('chartsDiv').getInnerWidth(),'350', "0", "1");
	 delay_chart.setDataXML(chartsLine);
	 delay_chart.setTransparent(true);
     delay_chart.render(p.id);
		      
    		   
    		}
    	}
		})
	 
        
		

	 var win = new Ext.Window({
            title: chartTitle+'趋势图',
            closable:true,
			frame : true,
			shadow : true,
            modal : true,
			layout: 'border',
			resizable :false,
            width:600,
            height:380,
            items: [lineCharts]
        });
		
	  win.show();
	  
                      
}



function renderXml(type){
	  switch(type){
	  	
		    case '2010':
			   return "FFFF00";
			break;
			 case '2011':
			    return "A66EDD";
			break;
			case '2012':
			    return "F6BD0F";
			break;
			 case '2013':
			    return "F0807F";
			break;
			case '2014':
			    return "F1C7D2";
			break;
			case '2015':
			    return "C91D1D";
			break;
		
	  }
	
	
	
}
