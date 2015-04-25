/**
 * @author Shea Frederick - http://www.vinylfox.com
 * @class Ext.ux.form.HtmlEditor.Image
 * @extends Ext.util.Observable
 * <p>A plugin that creates an image button in the HtmlEditor toolbar for inserting an image. The method to select an image must be defined by overriding the selectImage method. Supports resizing of the image after insertion.</p>
 * <p>The selectImage implementation must call insertImage after the user has selected an image, passing it a simple image object like the one below.</p>
 * <pre>
 *      var img = {
 *         Width: 100,
 *         Height: 100,
 *         ID: 123,
 *         Title: 'My Image'
 *      };
 * </pre>
 */
Ext.ns('Ext.ux.form.HtmlEditor.Image');
Ext.ux.form.HtmlEditor.Image = Ext.extend(Ext.util.Observable, {
	// Image language text
	langTitle: 'Insert Image',
    urlSizeVars: ['width','height'],
    basePath: 'image.php',
    init: function(cmp){
     
        this.cmp = cmp;
        this.cmp.on('render', this.onRender, this);
        this.cmp.on('initialize', this.onInit, this, {delay:100, single: true});
    },
    onEditorMouseUp : function(e){
        Ext.get(e.getTarget()).select('img').each(function(el){
            var w = el.getAttribute('width'), h = el.getAttribute('height'), src = el.getAttribute('src')+' ';
            src = src.replace(new RegExp(this.urlSizeVars[0]+'=[0-9]{1,5}([&| ])'), this.urlSizeVars[0]+'='+w+'$1');
            src = src.replace(new RegExp(this.urlSizeVars[1]+'=[0-9]{1,5}([&| ])'), this.urlSizeVars[1]+'='+h+'$1');
            el.set({src:src.replace(/\s+$/,"")});
        }, this);
        
    },
    onInit: function(){
        Ext.EventManager.on(this.cmp.getDoc(), {
			'mouseup': this.onEditorMouseUp,
			buffer: 100,
			scope: this
		});
    },
    onRender: function() {
        var btn = this.cmp.getToolbar().addButton({
            iconCls: 'x-edit-image',
            handler: this.selectImage,
            scope: this,
            tooltip: {
                title: this.langTitle
            },
            overflowText: this.langTitle
        });
    },
    selectImage: Ext.emptyFn,
    insertImage: function(img) {
        this.cmp.insertAtCursor('<img src="'+this.basePath+'?'+this.urlSizeVars[0]+'='+img.Width+'&'+this.urlSizeVars[1]+'='+img.Height+'&id='+img.ID+'" title="'+img.Name+'" alt="'+img.Name+'">');
    }
});



Ext.override(Ext.ux.form.HtmlEditor.Image,{selectImage:function() {  

        cmpCusor =this.cmp;  

         var imgform = new Ext.FormPanel({    
              fileUpload: true,
              width: 500,
              frame: true,
              autoHeight: true,
              bodyStyle: 'padding: 10px 10px 0 10px;',
              labelWidth: 55,
              defaults: {
                     anchor: '95%',
                     allowBlank: false,
                     msgTarget: 'side'
            }, 
             items : [{  
                    xtype: 'fileuploadfield',  
                    id: 'form-file'+Math.random()*(20-10)+10,  
                    emptyText: '输入图片地址',  
                    fieldLabel: '图片地址',
                    name: 'upload',
                    width:150,  
                    buttonText: '',  
                    buttonCfg: {  
                        iconCls: 'upload-icon'  
                    } 
                     }],  

             buttons : [{  
                text : '上传',  
                type : 'submit',  
                 handler : function() {  
                     if (!imgform.form.isValid()) 
                     {
                      return;
                      }  
                    imgform.form.submit({  
                        
                         waitMsg : '正在上传',  
                         url : 'jngh/knowledgebase/details/image-upload.action',
                         success : function(form, action) {
                            var element = document.createElement("img");
                            element.src =action.result.imageFilePath; 
                            if (Ext.isIE) {  
                                cmpCusor.insertAtCursor(element.outerHTML);  
                             } else {  
                                 var selection = cmpCusor.win.getSelection();  
                                 if (!selection.isCollapsed) {  
                                    selection.deleteFromDocument();  
                                 }  
                                 
                                 
                                 selection.getRangeAt(0).insertNode(element);  
                             }  
                            win.hide();  
                         },  

                         failure : function(form, action) {  
                             form.reset();  
                             if (action.failureType == Ext.form.Action.SERVER_INVALID)  
                                 Ext.MessageBox.alert('提示',"上传失败");  

                         }  

                     });  

                 }  

             }, {  

                 text : '关闭',  

                type : 'submit',  

                 handler : function() {  

                     win.close(this);  

                }  

             }]  

         });  

      var   win = new Ext.Window({  
                     title : "上传图片", 
                     width :300,
                     id:'win_img_add'+Math.random()*(20-10)+10,  
                     modal : true,  
                     border : false,  
                     iconCls : "upload-win",  
                     layout : "fit",  
                     items : imgform  
                 });  
          

         win.show();  
         
     

    }}); 
