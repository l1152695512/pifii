
package com.yinfu.business.page.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.jbase.jfinal.ext.Model;

@TableBind(tableName = "bp_shop_page")
public class Page extends Model<Page> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Page dao = new Page();
	
}
