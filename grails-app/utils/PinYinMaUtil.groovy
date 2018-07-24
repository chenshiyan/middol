package dtp;  
  
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
 
/**
 * java汉字转拼音操作工具类,如包含非汉字则不对其转换,直接跳过
 */
public class PinYinMaUtil {
 
    /**
     * 将字符串转换成拼音数组
     * @param str 待转换的汉字字符串
     * @param onlyFirst 是否只取每个汉字对应的拼音的首字母
     * @return  转换成拼音码的字符串
     */
    public static String stringToPinyin(String str,boolean onlyFirst) {
        return stringToPinyin(str,onlyFirst, false, null);
    }
    /**
     * 将字符串转换成拼音数组
     *  @param str 待转换的汉字字符串
     * @param onlyFirst 是否只取每个汉字对应的拼音的首字母
     * @return  转换成拼音码的字符串
     */
    public static String stringToPinyin(String str,boolean onlyFirst,String separator) {
        return stringToPinyin(str,onlyFirst, true, separator);
    }
 
    /**
     * 将字符串转换成拼音数组
     * @param str 待转换的汉字字符串
     * @param isPolyphone 是否查出多音字的所有拼音
     * @param separator 多音字拼音之间的分隔符
     * @return 
     */
    public static String stringToPinyin(String str,boolean onlyFirst, boolean isPolyphone,
            String separator) {
        // 判断字符串是否为空
        if ("".equals(str) || null == str) {
            return null;
        }
        char[] strChar = str.toCharArray();
        int strCount = strChar.length;
        StringBuffer sb = new StringBuffer();
 
        String temp = null;
        for (int i = 0; i < strCount; i++) {
        temp = charToPinyin(strChar[i], isPolyphone, separator);
            if(onlyFirst){
            sb.append( temp.substring(0, 1));
            }else{
            sb.append(temp);
            }
        }
        return sb.toString();
    }
 
    /**
     * 将单个字符转换成拼音
     * @param str 待转换的汉字字符串
     * @return  转换成拼音码后的字符串
     */
    public static String charToPinyin(char str, boolean isPolyphone,
            String separator) {
        // 创建汉语拼音处理类
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出设置，大小写，音标方式
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
 
        StringBuffer tempPinying = new StringBuffer();
         
        // 如果是中文
        if (str > 128) {
            try {
                // 转换得出结果
                String[] strs = PinyinHelper.toHanyuPinyinStringArray(str,
                        defaultFormat);
                 
                         
                // 是否查出多音字，默认是查出多音字的第一个字符
                if (isPolyphone && null != separator) {
                    for (int i = 0; i < strs.length; i++) {
                        tempPinying.append(strs[i]);
                        if (strs.length != (i + 1)) {
                            // 多音字之间用特殊符号间隔起来
                            tempPinying.append(separator);
                        }
                    }
                } else {
                    tempPinying.append(strs[0]);
                }
 
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            tempPinying.append(str);
        }
 
        return tempPinying.toString();
 
    }
 
     
    public static String hanziToPinyin(String hanzi){
        return hanziToPinyin(hanzi," ");
    }
    /**
     * 将汉字转换成拼音
     * @param hanzi
     * @param separator
     * @return 
     */
    @SuppressWarnings("deprecation")
    public static String hanziToPinyin(String hanzi,String separator){
            // 创建汉语拼音处理类
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            // 输出设置，大小写，音标方式
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        defaultFormat.setVCharType(HanyuPinyingVCharType);
             
            String pinyingStr="";
            try {
                pinyingStr=PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat, separator);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }  
            return pinyingStr;
    }
    /**
     * 将字符串数组转换成字符串
     * @param str
     * @param separator 各个字符串之间的分隔符
     * @return 
     */
    public static String stringArrayToString(String[] str, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
            if (str.length != (i + 1)) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
    /**
     * 简单的将各个字符数组之间连接起来
     * @param str
     * @return 
     */
    public  static String stringArrayToString(String[] str){
        return stringArrayToString(str,"");
    }
    /**
     * 将字符数组转换成字符串
     * @param str
     * @param separator 各个字符串之间的分隔符
     * @return 
     */
    public static String charArrayToString(char[] ch, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ch.length; i++) {
            sb.append(ch[i]);
            if (ch.length != (i + 1)) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
     
    /**
     * 将字符数组转换成字符串
     * @param str
     * @return 
     */
    public static String charArrayToString(char[] ch) {
        return charArrayToString(ch," ");
    }
 
    /**
     * 取汉字的首字母
     * @param str
     * @param isCapital 是否是大写
     * @return 
     */
    public static char[]  getHeadByChar(char str,boolean isCapital){
        //如果不是汉字直接返回
        if (str <= 128) {
            return;
        }
        //获取所有的拼音
        String []pinyingStr=PinyinHelper.toHanyuPinyinStringArray(str);
        //创建返回对象
        int polyphoneSize=pinyingStr.length;
        char [] headChars=new char[polyphoneSize];
        int i=0;
        //截取首字符
        for(String s:pinyingStr){
            char headChar=s.charAt(0);
            //首字母是否大写，默认是小写
            if(isCapital){
                headChars[i]=Character.toUpperCase(headChar);
             }else{
                headChars[i]=headChar;
             }
            i++;
        }
         
        return headChars;
    }
    /**
     * 取汉字的首字母(默认是大写)
     * @param str
     * @return 
     */
    public static char[]  getHeadByChar(char str){
        return getHeadByChar(str,true);
    }
    /**
     * 查找字符串首字母
     * @param str
     * @return 
     */
    public  static String[] getHeadByString(String str){
        return getHeadByString( str, true);
    }
    /**
     * 查找字符串首字母
     * @param str
     * @param isCapital 是否大写
     * @return 
     */
    public  static String[] getHeadByString(String str,boolean isCapital){
        return getHeadByString( str, isCapital,null);
    }
    /**
     * 查找字符串首字母
     * @param str
     * @param isCapital 是否大写
     * @param separator 分隔符
     * @return 
     */
    public  static String[] getHeadByString(String str,boolean isCapital,String separator){
        char[]chars=str.toCharArray();
        String[] headString=new String[chars.length];
        int i=0;
        for(char ch:chars){
             
            char[]chs=getHeadByChar(ch,isCapital);
            StringBuffer sb=new StringBuffer();
            if(null!=separator){
                int j=1;
                 
                for(char ch1:chs){
                    sb.append(ch1);
                    if(j!=chs.length){
                        sb.append(separator);
                    }
                    j++;
                }
            }else{
                sb.append(chs[0]);
            }
            headString[i]=sb.toString();
            i++;
        }
        return headString;
    }
     
    public static void main(String[] args) {
        System.out.println(hanziToPinyin("是都就写",""));
        String pym =stringToPinyin("的8说法'()[]是",false);
        System.out.println(pym);
    }
 
}