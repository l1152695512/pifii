package com.yf.tradecontrol;

import net.sf.json.JSONObject;

public interface Msfactory {
    /**
     * 返回解析为JSONObject的解析
     */
	public JSONObject transControlJson(JSONObject json);

}
