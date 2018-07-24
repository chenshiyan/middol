package dtp.wechat;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by KnightC on 2015/9/19.
 */
public class WechatHelper {
    /** charset */
    private String charset="UTF-8";


    // special character handle
    public String UrlEncode(String src,String charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, charset).replace("+", "%20");
    }

    // get package sign
    public String genPackage(SortedMap<String, String> packageParams,String key,String charset)
            throws UnsupportedEncodingException {
        String sign = createSign(packageParams,key);

        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + UrlEncode(v,charset) + "&");
        }

        // get rid of the last &
        String packageValue = sb.append("sign=" + sign).toString();
        return packageValue;
    }

    /**
      * create md5 summary
     */
    public String createSign(SortedMap<String, String> packageParams,String key) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5Util.MD5Encode(sb.toString(), this.charset)
                .toUpperCase();
        return sign;

    }

    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    //created SHA1
    public static String createSHA1Sign(SortedMap<String, String> signParams) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set es = signParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
            //use URLENCODER original data
        }
        String params = sb.substring(0, sb.lastIndexOf("&"));
        return getSha1(params);
    }
    //Sha1 sign
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("GBK"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * caifutong sign? the rule is order by a-z. null parameter not sign
     * @return boolean
     */
    public boolean isValidSign(SortedMap<String, String> packageParams,String key,String sign) {

        String ValidSign = createSign(packageParams,key);
        return ValidSign.equals(sign);
    }
}
