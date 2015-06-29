
Ext.ns("Ext.ux.grid");
var grids;
Ext.ux.grid.CheckColumn = function (config) {
	this.addEvents({checkchange:true});
	Ext.ux.grid.CheckColumn.superclass.constructor.call(this);
	Ext.apply(this, config, {init:function (grid) {
		this.grid = grid;
		grids=grid;
		this.grid.on("render", function () {
			var view = this.grid.getView();
			view.mainBody.on("mousedown", this.onMouseDown, this);
		}, this);
	}, onMouseDown:function (e, t) {
		if (t.className && t.className.indexOf("x-grid3-cc-" + this.id) != -1) {
			e.stopEvent();
			
			var index = this.grid.getView().findRowIndex(t);
			var record = this.grid.store.getAt(index);
			record.set(this.dataIndex, !record.data[this.dataIndex]);
			this.fireEvent("checkchange", this, e, record);
		}
	}, 
	renderer:function (v, p, record) {
	  p.css += ' x-grid3-check-col-td';
     return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
	}
	
	
	});
	
	if (!this.id) {
		this.id = Ext.id();
	}
	this.renderer = this.renderer.createDelegate(this);
};
function checkedBox(obj){

       var answer;
       if(obj.checked){
          answer='1';
       }else if(obj.checked==false){
          answer='0';
       }
          var reloadFn = function(xhr){
            	try{
            		var result = Ext.decode(xhr.responseText);
            		if(result.msg)Ext.Msg.alert("警告",result.msg);
            	}catch(e){
            		alert(e.description);
            	}
            
			    grids.store.load(grids.store.lastOptions);
			   };
       
       
       
       Ext.Ajax.request({
				url: 'pg/measure/update-state.action',
				success : reloadFn,
				params : {
						mid : obj.id,
					    enable:answer
					      }
						});
      
      }
	  
	  
function selectcheckedbox(obj){
         var answer;
        if(obj.checked){
          answer='1';
       }else if(obj.checked==false){
          answer='0';
       }
	   var store= Ext.getCmp('grides').getStore();

   Ext.Ajax.request({
				   url: 'jngh/assessmentresult/item/update-state.action',
				   params: {ids:obj.id,checked:answer},
				   success:function(){
				    store.load(store.lastOptions);
				   },
				   failure: function(){
				    store.load(store.lastOptions);
				   
				   }
				   
				});
 
 }	  
// register ptype
Ext.preg("checkcolumn", Ext.ux.grid.CheckColumn);


// backwards compat
Ext.grid.CheckColumn = Ext.ux.grid.CheckColumn;
Ext.extend(Ext.grid.CheckColumn, Ext.util.Observable);

