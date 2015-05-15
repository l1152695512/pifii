
package com.yinfu.system.controller;

import java.io.File;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.yinfu.Consts;
import com.yinfu.UrlConfig;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.service.EmailService;
import com.yinfu.shiro.ShiroCache;
import com.yinfu.system.model.User;
import com.yinfu.system.validator.UserValidator;

@ControllerBind(controllerKey = "/system/user", viewPath = UrlConfig.SYSTEM)
public class UserController extends Controller<User> {
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: index
	 * Description: 进入首页
	 * Created On: 2014年8月4日 上午9:47:57
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#index()
	 */
	//@formatter:on
	@Override
	public void index() {
		SplitPage splitPages = User.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/system/user/userIndex.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: addUser
	 * Description:增加用户
	 * Created On: 2014年8月4日 上午10:01:00
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void addUser() {
		render("/page/system/user/userAdd.jsp");
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 删除
	 * Created On: 2014年8月5日 上午9:22:19
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	@Override
	public void delete() {
		renderJsonResult(User.dao.deleteById(getPara("id")));
	}
	
	//@formatter:off 
	/**
	 * Title: freeze
	 * Description:
	 * Created On: 2014年8月5日 上午9:22:25
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void freeze() {
		renderJsonResult(User.dao.changeStaus(getParaToInt("id"), getParaToInt("status")));
	}
	//@formatter:off 
	/**
	 * Title: batchDelete
	 * Description:批量删除
	 * Created On: 2014年8月5日 上午9:20:13
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void batchDelete() {
		renderJsonResult(User.dao.batchDelete(getPara("ids")));
	}
	
	//@formatter:off 
	/**
	 * Title: batchGrant
	 * Description:批量授权
	 * Created On: 2014年8月5日 上午9:20:24
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void batchGrant() {
		
		Integer[] role_ids = getParaValuesToInt("role_ids");
		String ids = getPara("ids");
		
		renderJsonResult(User.dao.batchGrant(role_ids, ids));
		
		ShiroCache.clearAuthorizationInfoAll();
		
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存用户信息
	 * Created On: 2014年8月4日 下午6:49:52
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
	
		UploadFile file = getFile("userImage", PathKit.getWebRootPath()+"/"+PREVIEW_PATH);
		String icon ="/upload/image/user/" + file.getFileName();
		String password = getPara("password");
		if(getModel().set("icon", icon).set("pwd", Sec.md5(password)).saveAndDate()){
			renderJson("success", "true");
		}else{
			renderJson("error", "false");
		}
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: edit
	 * Description: 修改用户信息
	 * Created On: 2014年8月5日 上午9:20:44
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#edit()
	 */
	//@formatter:on
	@Override
	@Before(value = { UserValidator.class })
	public void edit() {
		
		renderJsonResult(getModel().update());
		
	}
	
	//@formatter:off 
	/**
	 * Title: pwd
	 * Description:忘记密码
	 * Created On: 2014年8月5日 上午9:21:28
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	@Before(value = { UserValidator.class })
	public void pwd() {
		renderJsonResult(getModel().encrypt().update());
		
		// send eamil
		User user = User.dao.findById(getModel().getId());
		if (!Validate.isEmpty(user.getStr("email")))
			;
		new EmailService().sendModifyPwdEmail(user.getStr("email"));
		
	}
	
	//@formatter:off 
	/**
	 * Title: grant
	 * Description:授权
	 * Created On: 2014年8月5日 上午9:21:36
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void grant() {
		Integer[] role_ids = getParaValuesToInt("role_ids");
		renderJsonResult(User.dao.grant(role_ids, getModel().getId()));
		ShiroCache.clearAuthorizationInfoAll();
		
	}
	
	//@formatter:off 
	/**
	 * Title: changePassword
	 * Description:修改密码
	 * Created On: 2014年8月5日 上午9:21:42
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void changePassword() {
		String msg = "";
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		String oldPassword = getPara("oldPass");
		String newPassword = getPara("newPass");
		String repeatNewPassword = getPara("repeatNewPass");
		if (newPassword.equals(repeatNewPassword)) {
			if (user.getPwd().equals(Sec.md5(oldPassword))) {
				if (!newPassword.equals(oldPassword)) {
					String newPasswordEncrypt = Sec.md5(newPassword);
					boolean success = new User().set("id", user.getId()).set("pwd", newPasswordEncrypt).update();
					if (success) {// 更新session中的用户信息
						user.set("pwd", newPasswordEncrypt);
					} else {
						msg = "更新失败稍后请重试！";
					}
				}
			} else {
				msg = "原始密码输入有误！";
			}
		} else {
			msg = "新密码两次输入不同！";
		}
		renderJson("msg", msg);
	}
	
	//@formatter:off 
	/**
	 * Title: getPic
	 * Description:获得图片
	 * Created On: 2014年7月29日 下午3:33:27
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getPic() {
		String id = getAttr("id");// 获得用户ID
		if (id != null && id != "") {
			User.dao.getUserPic(Long.valueOf(id), getResponse());
		}
	}
	
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" + File.separator + "user" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: savePic
	 * Description:保存图片信息
	 * Created On: 2014年7月29日 下午3:52:01
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	@SuppressWarnings("deprecation")
	public void savePic() {
		User user = ShiroExt.getSessionAttr("user");
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + PREVIEW_PATH);
		String name = String.valueOf(System.currentTimeMillis());
		ImageKit.renameFile(file, name);
		File src = file.getFile();
		String icon = "/upload/image/user/" + name + ImageKit.getFileExtension(src.getName());
		if (true == User.dao.update("icon", icon, user.getId())) {
			User users = user.dao.findById(user.getId());
			setSessionAttr("user", users);
			redirect("/userIndex");
		}
	}
}
