package com.plugin.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Description: 正则相关的工具类  http://toutiao.com/i6231678548520731137/
 * @Author: zxl
 * @Date: 1/9/16 上午10:55.
 */
public class RegularUtils {
    private static volatile RegularUtils mInstance = null;

    private RegularUtils() {
    }

    public static RegularUtils getInstance() {
        RegularUtils instance = mInstance;
        if (instance == null) {
            synchronized (RegularUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new RegularUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    //中英文数字
    public static final String REGEX_WORD = "[^0-9a-zA-Z\u4E00-\u9FA5_]";
    //纯数字
    public static final String REGEX_NUM = "[^0-9]";
    //验证手机号（简单）
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * 验证手机号（精确）
     * <p/>
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * <p>联通：130、131、132、145、155、156、175、176、185、186
     * <p>电信：133、153、173、177、180、181、189
     * <p>全球星：1349
     * <p>虚拟运营商：170
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-8])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    //验证座机号,正确格式：xxx/xxxx-xxxxxxx/xxxxxxxx/
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    //验证邮箱
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    //验证url
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    public static final String REGEX_URLLINK = "^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$";
    //验证汉字
    public static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    //验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    //验证IP地址
    public final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    ///验证QQ，腾讯QQ号从10000开始
    public final String REGEX_QQ = "[1-9][0-9]{4,}";
    // 验证邮政编码,中国邮政编码为6位数字
    public final String REGEX_DAK = "[1-9]\\d{5}(?!\\d)";
    //日期格式
    public final String REGEX_DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}";
    //强密码(必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间)
    public final String REGEX_STRONG_PWD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";
    //密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)
    public final String REGEX_PWD = "^[a-zA-Z]\\w{5,17}$";
    //帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)：
    public final String REGEX_ACCOUNT = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    public final String REGEX_IDCARD1 = "^([0-9]){7,18}(x|X)?$";
    // 短身份证号码(数字、字母x结尾)
    public final String REGEX_IDCARD2 = "^\\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$";

    /**
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    /***
     * 在EditText onTextChanged 监听器上使用
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        Pattern p = Pattern.compile(REGEX_WORD);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
