package dtp.wechat

import grails.converters.JSON
import grails.transaction.Transactional

import java.text.MessageFormat

@Transactional
class WechatTemplateService {

    //获取模板信息
    protected  static  String GETTEMPLATE_URL="https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token={0}";
    protected  static  String SENDTEMPLATE_URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";

    WechatService wechatService
     /**
     * 发送模板信息给用户
     * @param touser      用户的openid
     * @param templat_id  信息模板id
     * @param clickurl    用户点击详情时跳转的url
     * @param topcolor    模板字体的颜色
     * @param data        模板详情变量 Json格式
     * @return
     */
    def sendWechatmsgToUser(String touser, String templat_id, String clickurl, String topcolor, def data){
        String serverUrl=MessageFormat.format(SENDTEMPLATE_URL,wechatService.getAccessToken());
        def postStr=[:]
        postStr.put("touser",touser);
        postStr.put("template_id",templat_id);
        postStr.put("url",clickurl);
        postStr.put("topcolor",topcolor);
        postStr.put("data",data);
        postStr=(postStr as JSON).toString()
        def respStr = CommonUtils.httpPost(postStr,serverUrl)
        return JSON.parse(respStr)
    }


    /**
     * 发送模板信息给用户
     * @param touser      用户的openid
     * @param templat_id  信息模板id
     * @param clickurl    用户点击详情时跳转的url
     * @param topcolor    模板字体的颜色
     * @param data        模板详情变量 Json格式
     * @return
     */
    def getPrivateTemplate(){
        def serverUrl=MessageFormat.format(GETTEMPLATE_URL,wechatService.getAccessToken());
        def templateStr=CommonUtils.httpPost("",serverUrl)
        println templateStr
    }

}
