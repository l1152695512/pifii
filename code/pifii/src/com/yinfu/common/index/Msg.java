package com.yinfu.common.index;

import java.util.Collections;
import java.util.List;

import com.yinfu.jbase.jfinal.ext.Model;
import com.jfinal.ext.plugin.tablebind.TableBind;


public class Msg extends Model<Msg>
{
	private static final long serialVersionUID = -128801010211787215L;

	public static Msg dao = new Msg();

	public List<Msg> list()
	{
		List<Msg> list= dao.find(sql("index.msg.list"));
		Collections.reverse(list);
		return list;
	}
}
