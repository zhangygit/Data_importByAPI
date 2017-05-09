package com.luculent.data.utils.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luculent.data.constant.JsonKey;
import com.luculent.data.model.BackBean;

public class ConventionUtils {

    private final static Logger logger = LogManager.getLogger("run_long");

    /**
     * 防止类被实例化
     */
    private ConventionUtils() {
	// TODO Auto-generated constructor stub
	throw new AssertionError();
    }

    private static List<ConcurrentHashMap<String, String>> params;

    private final static String STR_SEPARATOR = "=";

    /**
     * 
     * @Description: 转化参数列表
     * @Author: zhangy
     * @Since: 2017年4月25日下午4:32:02
     * @param map
     * @return
     */
    public static List<ConcurrentHashMap<String, String>> toParamsMap(Map<String, List<String>> map) {
	Set<String> keys = map.keySet();
	List<String> keyList = new ArrayList<String>();
	keyList.addAll(keys);
	List<String> temp = new ArrayList<String>();
	params = new ArrayList<ConcurrentHashMap<String, String>>();
	solutionList(keyList, map, 0, temp);
	return params;
    }

    /**
     * 
     * @Description: 将返回的json串转化为BackBean
     * @Author: zhangy
     * @Since: 2017年4月25日下午4:25:35
     * @param json
     * @return
     */
    public static BackBean jsonToBackBean(String json) {
	if (StringUtils.isEmpty(json)) {
	    return null;
	}
	JSONObject obj = JSON.parseObject(json);
	if (obj.containsKey(JsonKey.head.name()) && obj.containsKey(JsonKey.body.name())) {
	    JSONObject head = obj.getJSONObject(JsonKey.head.name());
	    JSONObject body = obj.getJSONObject(JsonKey.body.name());
	    return new BackBean.Builder(head.getString(JsonKey.rtnCode.name()), head.getString(JsonKey.rtnMsg.name()))
		    .sql(body.getString(JsonKey.sql.name())).total(body.getString(JsonKey.total.name()))
		    .page(body.getString(JsonKey.page.name())).list(body.getString(JsonKey.list.name())).build();
	}
	if (logger.isDebugEnabled()) {
	    logger.debug("请求数据错误，响应为" + json);
	}
	return null;

    }

    /**
     * 
     * @Description:首字母大写
     * @Author: zhangy
     * @Since: 2017年4月27日下午1:55:30
     * @param src
     * @return
     */
    public static String firstSpellToUp(String src) {
	char[] cs = src.toCharArray();
	cs[0] -= 32;
	return String.valueOf(cs);
    }

    /**
     * 
     * @Description:首字母小写
     * @Author: zhangy
     * @Since: 2017年4月27日下午1:55:30
     * @param src
     * @return
     */
    public static String firstSpellToLow(String src) {
	char[] cs = src.toCharArray();
	cs[0] += 32;
	return String.valueOf(cs);
    }

    /**
     * 
     * <p>Description: map转换工具</p> @param objs @return Map @throws
     */
    public static Map toMap(Object... objs) {
	HashMap map = new HashMap();
	Object key = null;
	for (int i = 0; i < objs.length; i++) {
	    if (i % 2 == 0) {
		key = objs[i];
	    } else {
		map.put(key, objs[i]);
	    }
	}
	return map;
    }

    /**
     * 
     * <p>Description: list转换工具</p> @param objs @return Map @throws
     */
    public static List toList(Object... objs) {
	List result = new ArrayList();
	for (int i = 0; i < objs.length; i++) {
	    result.add(objs[i]);
	}
	return result;
    }

    private static ConcurrentHashMap<String, String> toMapByList(List<String> list) {
	ConcurrentHashMap<String, String> pams = new ConcurrentHashMap<String, String>();
	for (String str : list) {
	    String[] arr = StringUtils.split(str, STR_SEPARATOR);
	    pams.put(arr[0], arr[1]);
	}
	return pams;
    }

    private static void solutionList(List<String> key, Map<String, List<String>> cc, int num, List<String> stt) {
	String keystr = key.get(num);
	List<String> aa = cc.get(keystr);
	for (String ca : aa) {
	    stt.add(String.format("%s" + STR_SEPARATOR + "%s", keystr, ca));
	    if (num + 1 < key.size()) {
		solutionList(key, cc, num + 1, stt);
	    } else {
		params.add(toMapByList(stt));
	    }
	    stt.remove(stt.size() - 1);
	}
    }
}
