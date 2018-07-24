package dtp.wechat


import grails.converters.JSON

import javax.servlet.http.Cookie
import java.text.MessageFormat

/**
 * 微信号设置Service
 * @author Knight
 * @version 2017-05-24
 */
class WechatService {

    //获取ACCESSTOKEN
    protected  static  String GETACCESSTOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";  //获取accesstoken
    //获取JSAPITICKETS
    protected  static  String GETJSAPITICKETS_URL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi"; //获取jsapitickets
    //获取openid
    protected  static  String GETOPENID_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code ";
    //拉取用户信息
    protected  static  String GETUSERMSG_URL="https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN ";

    //默认公众号名称
    protected  static  String TESTPUBLICACCOUNTNAME="test"

    /**
     * 获取最新的accessToken
     * @params publicAccountName 公众号名称
     * @return 返回accesstoken的json格式数据
     */
    def getAccessToken(publicAccountName){
        if(!publicAccountName){
            publicAccountName=TESTPUBLICACCOUNTNAME
        }
        def now = Calendar.instance.getTimeInMillis()
        def accessInstance = AccessTokenLog.createCriteria().list(){
            eq('publicAccountName',publicAccountName)
            between('createdTime', now-6500*1000, now)
            order("id", "desc")
        }[0]
        if(accessInstance){
            return accessInstance.accessToken
        }else{
            return getAccessTokenFromWechat(publicAccountName).accessToken
        }
    }

    /**
     * 从微信获取accessToken
     * @params publicAccountName 公众号名称
     * @return 返回accesstoken的json格式数据
     */
    def getAccessTokenFromWechat(publicAccountName){
        def pa = PublicAccount.findByPublicAccountName(publicAccountName)
        //log.info(pa)
        def accessTokenUrl =MessageFormat.format(GETACCESSTOKEN_URL,pa.appid,pa.appsecret)
        def text = new URL(accessTokenUrl).getText(connectTimeout:5000, readTimeout:5000,'utf-8')

        def createdTime = Calendar.instance.getTimeInMillis()
        def postResult = JSON.parse(text)
        if(postResult.errcode){
            return JSON.parse("{'result':false,'res':'${postResult.errmsg}','errorCode':'${postResult.errcode}'}")
        }
        pa.accessToken=postResult.access_token
        def AccessTokenLogInstance=new AccessTokenLog(accessToken:postResult.access_token,publicAccountName:publicAccountName,createdTime:createdTime)
        if(!AccessTokenLogInstance.save(failOnError:true)){
            return JSON.parse("{'result':false,'res':'AccessTokenLog Save fail','errorCode':'1000'}")
        }
        return JSON.parse("{'result':true,'accessToken':${pa.accessToken}}")
    }

    //验证更新JSAPITICKET
    def getJsApiTicket(publicAccountName){
        if(!publicAccountName){
            publicAccountName=TESTPUBLICACCOUNTNAME
        }
        def now = Calendar.instance.getTimeInMillis()
        def jsApiTicketInstance = JsApiTicket.createCriteria().list{
            eq('publicAccountName',publicAccountName)
            between('createdTime', now-6500*1000, now)
            order("id", "desc")
        }[0]
        if(jsApiTicketInstance){
            return jsApiTicketInstance.jsApiTicket
        }else{
            return getJsApiTicketFromWechat(publicAccountName).jsApiTicket
        }
    }

    /**
     * 从微信获取JSAPITICKET
     * @params publicAccountName 公众号名称
     * @return 返回accesstoken的json格式数据
     */
    //获得JSAPITICKET
    def getJsApiTicketFromWechat(publicAccountName){
        def pa = PublicAccount.findByPublicAccountName(publicAccountName)
        def accessToken=getAccessToken()
        def jsApiTicketUrl = MessageFormat.format(GETJSAPITICKETS_URL,accessToken);
        def text = new URL(jsApiTicketUrl).getText(connectTimeout:5000, readTimeout:5000,'utf-8')
        def createdTime = Calendar.instance.getTimeInMillis()
        def jsApiTicketObj=JSON.parse(text)
        if(jsApiTicketObj.errcode){
            return JSON.parse("{'result':false,'res':'${jsApiTicketObj.errmsg}','errorCode':'${jsApiTicketObj.errcode}'}")
        }
        pa.jsApiTicket=jsApiTicketObj.ticket
        def jsApiTicketInstance=new JsApiTicket(jsApiTicket:jsApiTicketObj.ticket,publicAccountName:publicAccountName,createdTime:createdTime)
        if(!jsApiTicketInstance.save(failOnError: true)){
            return JSON.parse("{'result':false,'res':'保存jsapiticket失败','errorCode':'10046'}")
        }
        return JSON.parse("{'result':true,'jsApiTicket':${pa.jsApiTicket}}")
    }



    //获得分享信息
    def createShareInfo(String url,publicAccountName){
        if(!publicAccountName){
            publicAccountName=TESTPUBLICACCOUNTNAME
        }
        def appid=PublicAccount.findByPublicAccountName(publicAccountName).appid
        WechatHelper wechatHelper = new WechatHelper();
        SortedMap<String, String> shareParams = new TreeMap<String, String>();
        def jsApiTicket=getJsApiTicket()
        String noncestr = wechatHelper.getNonceStr();
        String timestamp = wechatHelper.getTimeStamp();
        shareParams.put("jsapi_ticket", jsApiTicket);
        shareParams.put("noncestr", noncestr);
        shareParams.put("timestamp", timestamp);
        shareParams.put("url", url);
        String sign = wechatHelper.createSHA1Sign(shareParams);
        SortedMap<String, String> returnSignParams = new TreeMap<String, String>();
        returnSignParams.put("appId",appid)
        returnSignParams.put("noncestr", noncestr);
        returnSignParams.put("timestamp", timestamp);
        returnSignParams.put("signature", sign);
        returnSignParams.put("jsApiTicket", jsApiTicket);
        return returnSignParams
    }




    //通过code获得openid
    def gainOpenId(code,publicAccountName){
        if(!publicAccountName){
            publicAccountName=TESTPUBLICACCOUNTNAME
        }
        def pa = PublicAccount.findByPublicAccountName(publicAccountName)
        def addr=MessageFormat.format(GETOPENID_URL,pa.appid,pa.appsecret,code);
        def text = new URL(addr).getText([connectTimeout:5000, readTimeout:5000])
        def userBase=JSON.parse(text)
        //log.info(userBase)
        return userBase.openid
    }

    //通过openid,accesstoken获得用户信息
    def gainAccountInfo(openId){
        def ACCESS_TOKEN = getAccessToken()
        def infoUrl=MessageFormat.format(GETUSERMSG_URL,ACCESS_TOKEN,openId);
        def text = new URL(infoUrl).getText(connectTimeout:5000, readTimeout:5000,'utf-8')
        //tryAgain
        if(text.contains("40001")){
            ACCESS_TOKEN=getAccessTokenFromWechat(TESTPUBLICACCOUNTNAME)
            infoUrl=MessageFormat.format(GETUSERMSG_URL,ACCESS_TOKEN,openId);
            text = new URL(infoUrl).getText(connectTimeout:5000, readTimeout:5000,'utf-8')
        }
        def accountInfo = JSON.parse(text)
        if(accountInfo.errcode){
            return JSON.parse("{'result':false,'res':'${accountInfo.errmsg}','errorCode':'${accountInfo.errcode}'}")
        }
        if(!accountInfo.subscribe){
            return JSON.parse("{'subscribe':false,'openId':"+openId+"}")
        }
        return accountInfo
    }

//    def getCookies(cookies){
//        if(!cookies){
//            return false
//        }
//        for(int i=0;i<cookies.length;i++){
//            Cookie c = cookies[i];
//            if(c.getName()=="openId"){
//                return c.getValue()
//            }
//        }
//        return false
//    }


//    def createWeMenu(menuStr){
//
//        def ACCESS_TOKEN = getAccessToken()
//
//        def menuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ACCESS_TOKEN
//
//        def respStr = CommonUtils.httpPost(menuStr,menuUrl)
//        return JSON.parse(respStr)
//    }

}
