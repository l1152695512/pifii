package com.yf.base.actions.sys.action.item;

import com.opensymphony.xwork2.ActionSupport;

public class NewForm extends ActionSupport {
	 	private String said;//权限主表主键
		private String name;//权限名称

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSaid() {
			return said;
		}

		public void setSaid(String said) {
			this.said = said;
		}


	

}
