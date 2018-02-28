package com.whaty.platform.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServletRequest帮助类
 *
 * @author zhoudonghua
 */
public class RequestUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);

    /**
     * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。
     *
     * @param request web请求
     * @param name    参数名称
     * @return
     */
    public static String getQueryParam(HttpServletRequest request, String name, String encoding) {
        String s = request.getQueryString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            s = URLDecoder.decode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            log.error("encoding " + encoding + " not support.", e);
        }
        if (StringUtils.isBlank(s)) {
            return null;
        }
        String[] values = parseQueryString(s).get(name);
        if (values != null && values.length > 0) {
            return values[values.length - 1];
        } else {
            return null;
        }
    }

    /**
     * Parses a query string passed from the client to the server and builds a
     * <code>HashTable</code> object with key-value pairs. The query string
     * should be in the form of a string packaged by the GET or POST method,
     * that is, it should have key-value pairs in the form <i>key=value</i>,
     * with each pair separated from the next by a &amp; character.
     * <p/>
     * <p/>
     * A key can appear more than once in the query string with different
     * values. However, the key appears only once in the hashtable, with its
     * value being an array of strings containing the multiple values sent by
     * the query string.
     * <p/>
     * <p/>
     * The keys and values in the hashtable are stored in their decoded form, so
     * any + characters are converted to spaces, and characters sent in
     * hexadecimal notation (like <i>%xx</i>) are converted to ASCII characters.
     *
     * @param s a string containing the query to be parsed
     * @return a <code>HashTable</code> object built from the parsed key-value
     * pairs
     * @throws IllegalArgumentException if the query string is invalid
     */
    public static Map<String, String[]> parseQueryString(String s) {
        String valArray[] = null;
        if (s == null) {
            throw new IllegalArgumentException();
        }
        Map<String, String[]> ht = new HashMap<String, String[]>();
        StringTokenizer st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens()) {
            String pair = st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                continue;
            }
            String key = pair.substring(0, pos);
            String val = pair.substring(pos + 1, pair.length());
            if (ht.containsKey(key)) {
                String oldVals[] = ht.get(key);
                valArray = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++)
                    valArray[i] = oldVals[i];
                valArray[oldVals.length] = val;
            } else {
                valArray = new String[1];
                valArray[0] = val;
            }
            ht.put(key, valArray);
        }
        return ht;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getRequestMap(HttpServletRequest request,
                                                    String prefix) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> names = request.getParameterNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name.startsWith(prefix)) {
                request.getParameterValues(name);
                map.put(name.substring(prefix.length()), StringUtils.join(
                        request.getParameterValues(name), ','));
            }
        }
        return map;
    }

    public static Map<String, Object> getRequestObjectMap(HttpServletRequest request, String prefix) {
        return getRequestObjectMap(request, prefix, false);
    }

    public static Map<String, Object> getRequestObjectMap(HttpServletRequest request, String prefix, boolean singleVal) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> names = request.getParameterNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name.startsWith(prefix)) {
                if(singleVal){
                    String[] values = request.getParameterValues(name);
                    if(null != values && values.length == 1){
                        map.put(name.substring(prefix.length()), values[0]);
                    }
                }else{
                    map.put(name.substring(prefix.length()), request.getParameterValues(name));
                }
            }
        }
        return map;
    }

    public static String fixNull(Object obj) {
        String result = "";
        if (obj != null) {
            result = obj.toString();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> result = new HashMap<String, String>();
        for (String key : map.keySet()) {
            result.put(key, fixNull(map.get(key)[0]));
        }
        return result;
    }

    /**
     * 获取访问者IP
     * <p/>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p/>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        } else {
            return ip;
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }
        return ip;
    }

    /**
     * 获得当的访问路径
     * <p/>
     * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
     *
     * @param request
     * @return
     */
    public static String getLocation(HttpServletRequest request) {
        StringBuffer sb = request.getRequestURL();
        if (request.getQueryString() != null) {
            sb.append("?").append(request.getQueryString());
        }
        return sb.toString();
    }

    /**
     * 获取当前域名及部署项目路径
     * @param request
     * @return http(s)://www.whaty.com/res/
     */
    public static String getBasePath(HttpServletRequest request){
        String port = request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
        String basePath = request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
        return basePath;
    }

    public static void main(String[] args) {
    }
}

