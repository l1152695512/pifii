package com.yinfu.business.application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/app/auth", viewPath = "/page/business/application/auth")
public class AuthSettingController extends Controller<Record>{
	
	public void index(){
		StringBuffer sql = new StringBuffer();
		sql.append("select bat.id,bat.name,if(bas.id is null,'0','1') checked ");
		sql.append("from bp_auth_type bat left join bp_auth_setting bas on (bas.shop_id=? and bat.id=bas.auth_type_id) ");
		sql.append("group by bat.id ");
		List<Record> authTypes = Db.find(sql.toString(), new Object[]{getPara("shopId")});
		setAttr("authTypes", authTypes);
//		setAttr("shopId", getPara("shopId"));
		render("authSetting.jsp");
	}
	
	public void save(){
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			Db.update("delete from bp_auth_setting where shop_id=? ", new Object[]{getPara("shopId")});
			List<String> selectedAuthType = new ArrayList<String>();
			if(null != getParaValues("selectedAuthTypes")){
				Collections.addAll(selectedAuthType, getParaValues("selectedAuthTypes"));
			}
			if(selectedAuthType.size() > 0){
				Object[][] params = new Object[selectedAuthType.size()][3];
				for(int i=0;i<selectedAuthType.size();i++){
					params[i][0] = UUID.randomUUID().toString();
					params[i][1] = getPara("shopId");
					params[i][2] = selectedAuthType.get(i);
				}
				DbExt.batch("insert into bp_auth_setting(id,shop_id,auth_type_id,create_date) values(?,?,?,now())",params);
			}
			return true;
		}});
		renderJsonResult(isSuccess);
	}
}

