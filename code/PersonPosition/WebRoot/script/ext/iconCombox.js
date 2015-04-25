//Create   (User eXtensions namespace (Ext.ux))<img alt="" src="http://p.blog.csdn.net/images/p_blog_csdn_net/yangfengjob/fet.jpg">  
Ext.namespace("Ext.ux");
/** 
 
  *Ext.ux.IconCombo   
 
  * 
 
  *@author Steve.yang 
 
  *@version 1.0 
 
  * 
 
  *@class Ext.ux.IconCombo 
 
  *@extends Ext.form.ComboBox 
 
  *@constructor 
 
  *@param{Object} config  
 
  * 
 
  */
Ext.BLANK_IMAGE_URL = "ext/resources/images/default/s.gif";
Ext.ux.IconCombo = function (config) {
	Ext.ux.IconCombo.superclass.constructor.call(this, config);
	
	this.tpl = config.tpl || "<tpl for=\".\">" + "<div class=\"x-combo-list-item\">" + "<table><tbody><tr>" + "<td>" + "<div class=\"{" + this.iconClsField + "} x-icon-combo-icon\"></div></td>" + "<td class='x-select-field-text'>{" + this.displayField + "}</td>" + "</tr></tbody></table>" + "</div></tpl>";
	this.on({render:{scope:this, fn:function () {
		var wrap = this.el.up("div.x-form-field-wrap");  
  
//            this.wrap.applyStyles({position:'relative'});
  
//            this.el.addClass('x-icon-combo-input');  
		this.flag = Ext.DomHelper.append(wrap, {tag:"div", style:"position:absolute"});
	}}});
};
Ext.extend(Ext.ux.IconCombo, Ext.form.ComboBox, {setIconCls:function () {
	var rec = this.store.query(this.valueField, this.getValue()).itemAt(0);
	if (rec) {
		this.flag.className = "x-icon-combo-icon " + rec.get(this.iconClsField);
	}
}, setValue:function (value) {
	Ext.ux.IconCombo.superclass.setValue.call(this, value);
	this.setIconCls();
}});

