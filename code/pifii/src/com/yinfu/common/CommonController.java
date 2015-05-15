package com.yinfu.common;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.Consts;
import com.yinfu.UrlConfig;
import com.yinfu.common.validator.LoginValidator;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.L;
import com.yinfu.jbase.util.RSA;
import com.yinfu.jbase.util.Sec;
import com.yinfu.model.easyui.Tree;
import com.yinfu.servlet.CaptchaServlet;
import com.yinfu.shiro.CaptchaException;
import com.yinfu.shiro.NoShopException;
import com.yinfu.system.model.Res;
import com.yinfu.system.model.User;

/***
 * 
 * 月落斜阳 灯火阑珊
 * 
 * @author 12
 * 
 */
@ControllerBind(controllerKey = "/")
public class CommonController extends Controller {
	//@formatter:off 
	/**
	 * Title: index
	 * Description:系统管理员首页
	 * Created On: 2014年7月21日 下午5:23:47
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index()
	{
		setAttr("menuList", showMneu());
		L.i(" session id ="+getSession().getId());
		setAttr("user",ShiroExt.getSessionAttr(Consts.SESSION_USER));
		render(UrlConfig.VIEW_USERINDEX);
	}
	//@formatter:off 
	/**
	 * Title: userIndex
	 * Description:用户登录首页
	 * Created On: 2014年7月21日 下午5:24:02
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void userIndex(){
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		setAttr("user", user);
		render(UrlConfig.VIEW_USERINDEX);
	}
	
	public void loginView()
	{
		RSAPublicKey publicKey = RSA.getDefaultPublicKey();
		String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
		String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
		setAttr("modulus", modulus);
		setAttr("exponent", exponent);
		render(UrlConfig.VIEW_COMMON_LOGIN);

	}


	//@formatter:off 
	/**
	 * Title: loginOut
	 * Description:注销
	 * Created On: 2014年7月21日 下午5:26:03
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void loginOut()
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
			//renderHtml("<html><script> window.open('" + UrlConfig.LOGIN + "','_top') </script></html>");
			//renderTop(UrlConfig.LOGIN);
			RSAPublicKey publicKey = RSA.getDefaultPublicKey();
			String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
			String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
			setAttr("modulus", modulus);
			setAttr("exponent", exponent);
			render(UrlConfig.VIEW_COMMON_LOGIN);
		} catch (AuthenticationException e)
		{
			e.printStackTrace();
			renderText("异常：" + e.getMessage());
		}
	}

	//@formatter:off 
	/**
	 * Title: login
	 * Description:登录
	 * Created On: 2014年7月21日 下午5:26:11
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	@Before(LoginValidator.class)
	public void login()
	{
		String[] result = RSA.decryptUsernameAndPwd(getPara("key"));
		setAttr("name", result[0]);
		try
		{
			// 增加判断验证码逻辑
			String captcha = getPara("validateCode");
			//String exitCode = getSessionAttr(CaptchaServlet.KEY_CAPTCHA);
			String exitCode = (String) getSession().getAttribute(CaptchaServlet.KEY_CAPTCHA);
			if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
//				setAttr("msg", "验证码错误");
				throw new CaptchaException();
			}
			UsernamePasswordToken token = new UsernamePasswordToken(result[0], Sec.md5(result[1]));
			Subject subject = SecurityUtils.getSubject();
			User user = User.dao.findByName(result[0]);
//			if (!subject.isAuthenticated())
//			{
				token.setRememberMe(true);
				subject.login(token);
				checkShop(user);
				subject.getSession(true).setAttribute(Consts.SESSION_USER,user);
//			}
			redirect("/");
		} catch (UnknownAccountException e)
		{
			forwardAction("用户名或密码错误", UrlConfig.LOGIN);

		} catch (IncorrectCredentialsException e)
		{
			forwardAction("用户名或密码错误", UrlConfig.LOGIN);

		} catch (LockedAccountException e)
		{
			forwardAction("对不起 帐号被封了", UrlConfig.LOGIN);
			e.printStackTrace();
		} catch (ExcessiveAttemptsException e)
		{
			forwardAction("尝试次数过多 请明天再试", UrlConfig.LOGIN);
		} catch (AuthenticationException e)
		{
			forwardAction("对不起 没有权限 访问", UrlConfig.LOGIN);
		}catch (CaptchaException e)
		{
			forwardAction("验证码错误", UrlConfig.LOGIN);
		}catch (NoShopException e)
		{
			forwardAction("您的账户下没有商铺，不能登录平台", UrlConfig.LOGIN);
		}catch (Exception e)
		{
			e.printStackTrace();
			forwardAction("请重新登录", UrlConfig.LOGIN);
		}

	}
	private void checkShop(User user) throws NoShopException{
		List<Record> shops = Db.find("select id from bp_shop where delete_date is null and owner = ? ", new Object[]{user.getId()});
		if(shops.size() == 0){
			throw new NoShopException();
		}
	}
	
	public void unauthorized()
	{

		render(UrlConfig.VIEW_ERROR_401);
	}


	public void forwardAction(String msg, String url) {

		setAttr("msg", msg);
		forwardAction(url);
	}
	
	//@formatter:off 
	/**
	 * Title: showMneu
	 * Description:首页菜单显示
	 * Created On: 2014年7月24日 下午3:25:13
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public List<Tree> showMneu(){
		List<Tree> menuList = Res.dao.getTree(null,1,null);
		return menuList;
	}
	//@formatter:off 
	/**
	 * Title: userContent
	 * Description:用户首页内容展示
	 * Created On: 2014年8月7日 下午6:51:56
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void userContent(){
		render("page/business/mainContentPage.jsp");
	}
}
