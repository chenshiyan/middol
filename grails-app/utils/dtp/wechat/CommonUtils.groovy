package dtp.wechat

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpException
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.RequestEntity
import org.apache.commons.httpclient.methods.StringRequestEntity

import javax.servlet.ServletInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by KnightC on 2015/9/21.
 */
class CommonUtils {

    //服务器验证
    public static genPassword(String token, String timestamp, String nonce) {
        String[] ArrTmp =  [token, timestamp, nonce ];
        Arrays.sort(ArrTmp);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ArrTmp.length; i++) {
            sb.append(ArrTmp[i]);
        }
        return this.Encrypt(sb.toString());
    }

    //SHA-1 加密
    public static String Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    //httpClient3.1 post方法
    public static httpPost(String dataStr,String sendUrl){
        RequestEntity entity = null

        try {
            entity = new StringRequestEntity(dataStr,"text/json","utf-8")
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace()
        }

        HttpClient client = new HttpClient()
        PostMethod post = new PostMethod(sendUrl)

        post.setRequestEntity(entity)
        post.getParams().setContentCharset("utf-8")

        String respStr = ""
        try {
            client.executeMethod(post)
            respStr = post.getResponseBodyAsString()
        } catch (HttpException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }

        return respStr
    }

    //byte转16进制
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    //检测是否是中文
    public static boolean isChinese(String str) {
        boolean result = false;
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result = true;
            }
        }
        return result;
    }


    //将流转成String
    public static String readStreamParameter(ServletInputStream ins){
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader=null;
        try{
            reader = new BufferedReader(new InputStreamReader(ins));
            String line=null;
            while((line = reader.readLine())!=null){
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(null!=reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }
    //生产随机字符串
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //生产随机字符串
    public static String getRandomNum(int length) { //length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //检测是否是中文(正则)
    public  static boolean checkChinese(String str) {
        def check_reg = /^[0-9a-zA-Z.]*$/
        return str.matches(check_reg);
    }

    //获得时间戳 精确到秒
    public static String getTimeStamp() {
        String time=String.valueOf(System.currentTimeMillis() / 1000)
        return time.substring(0,time.lastIndexOf("."));
    }

}
