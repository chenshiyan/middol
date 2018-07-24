package dtp.wechat

import java.text.SimpleDateFormat


class WechatReplyService {

    def getThreeDayAfter(date){
        return PathUtils.getDayAfter(date,3)
    }
    def threeDayCompare(dayofthree){
        return PathUtils.dateCompare(dayofthree)
    }
    def saveTextInmessageLogs (def wechatAccount,def node, def postStr) {
        StringBuilder sb = new StringBuilder()
        sb.append("保存 InMessage:\n")
        InMessage inmessage
        if(node.MsgType.text()=="text"){
            inmessage = new InMessage(
                msgId:node.MsgId.text(),
                msgType:node.MsgType.text(),
                toUserName:node.ToUserName.text(),
                fromUserName:node.FromUserName.text(),
                openId:node.FromUserName.text(),
                createTime:node.CreateTime.text(),
                message:postStr,
                content:node.Content.text()
            )
        }
        if(node.MsgType.text()=="image"){
            inmessage = new InMessage(
                msgId:node.MsgId.text(),
                msgType:node.MsgType.text(),
                toUserName:node.ToUserName.text(),
                fromUserName:node.FromUserName.text(),
                openId:node.FromUserName.text(),
                createTime:node.CreateTime.text(),
                message:postStr,
                mediaId:node.MediaId.text(),
                picUrl:node.PicUrl.text()

            )
        }
        if(node.MsgType.text()=="shortvideo"){
            inmessage = new InMessage(
                msgId:node.MsgId.text(),
                msgType:node.MsgType.text(),
                toUserName:node.ToUserName.text(),
                fromUserName:node.FromUserName.text(),
                openId:node.FromUserName.text(),
                createTime:node.CreateTime.text(),
                message:postStr,
                mediaId:node.MediaId.text(),
                thumbMediaId:node.ThumbMediaId.text()
            )
        }
        if(node.MsgType.text()=="voice"){
            inmessage = new InMessage(
                msgId:node.MsgId.text(),
                msgType:node.MsgType.text(),
                toUserName:node.ToUserName.text(),
                fromUserName:node.FromUserName.text(),
                openId:node.FromUserName.text(),
                createTime:node.CreateTime.text(),
                message:postStr,
                mediaId:node.MediaId.text(),
                format:node.Format.text()
            )
        }
        if(node.MsgType.text()=="location"){
             inmessage = new InMessage(
                msgType:node.MsgType.text(),
                toUserName:node.ToUserName.text(),
                fromUserName:node.FromUserName.text(),
                openId:node.FromUserName.text(),
                createTime:node.CreateTime.text(),
                message:postStr,
                event:node.Event.text(),
                latitude:node.Latitude.text(),
                longitude:node.Longitude.text(),
                locPrecision:node.Precision.text()
            )
        }
        if(node.MsgType.text()=="event"){
            def eventValue = node.Event.text()
            if (eventValue=="CLICK") {
                inmessage = new InMessage(
                    msgType:node.MsgType.text(),
                    toUserName:node.ToUserName.text(),
                    fromUserName:node.FromUserName.text(),
                    openId:node.FromUserName.text(),
                    createTime:node.CreateTime.text(),
                    message:postStr,
                    event:node.Event.text(),
                    eventKey:node.EventKey.text()
                )
            }else if(eventValue=="VIEW"){
                inmessage = new InMessage(
                    msgType:node.MsgType.text(),
                    toUserName:node.ToUserName.text(),
                    fromUserName:node.FromUserName.text(),
                    openId:node.FromUserName.text(),
                    createTime:node.CreateTime.text(),
                    message:postStr,
                    event:node.Event.text(),
                    url:node.EventKey.text()
                )
            }else{
                inmessage = new InMessage(
                    msgType:node.MsgType.text(),
                    toUserName:node.ToUserName.text(),
                    fromUserName:node.FromUserName.text(),
                    openId:node.FromUserName.text(),
                    createTime:node.CreateTime.text(),
                    message:postStr,
                    event:node.Event.text()
                )
            }
        }
        inmessage.wechatAccount=wechatAccount
        if (!inmessage.save(failOnError:true)) {
            inmessage.errors.each {
                sb.append(it).append("\n")
            }
            log.info(sb.toString())
            return null
        } else {
            return inmessage
        }
    }
    def saveTextOutmessageLogs (def outmessage, def postStr) {
        StringBuilder sb = new StringBuilder()
        outmessage.message=postStr
        if (!outmessage.save(failOnError:true)){
            outmessage.errors.each {
                println it
                sb.append(it).append("\n")
            }
            log.info(sb.toString())
            return null
        } else {
            return outmessage
        }
    }
    def saveNewsOutmessageLogs(def outmessage, def postStr) {
        StringBuilder sb = new StringBuilder()
        outmessage.message=postStr
        def node = new XmlParser().parseText(postStr)
        outmessage.articlesContent=node.Articles
        if (!outmessage.save(failOnError:true)){
            outmessage.errors.each {
                println it
                sb.append(it).append("\n")
            }
            log.info(sb.toString())
            return null
        } else {
            return outmessage
        }
    }

    def getAnswer(def keyword,def keywordType,def keyWordEvent) {
        def keyWordList
        //微信系统表情的处理

        if(keyword!=null&&keyword.contains("/::")){
            keywordType="noKeyWord" 
        }
        if(keywordType=="normal"){
            keyWordList=KeyWord.executeQuery("select k from KeyWord k where '${keyword}' like concat('%', k.question, '%') and status=1 and k.keyWordType='normal' ORDER BY lastUpdated")
            if(!keyWordList){
                keywordType="noKeyWord"
            }else{
                keyWordList=keyWordList.collect{
                        if(it.exactMatch){
                            if(keyword.equals(it.question)){
                                [
                                      keyword:it
                                ]
                            }
                        } else{
                            if(keyword.contains(it.question)){
                                [
                                        keyword:it
                                ]
                            }
                        }
                }
                return keyWordList.keyword
            }
        }
        if(keywordType=="noKeyWord"){
            keyWordList=KeyWord.createCriteria().list(){
                eq("keyWordType", "noKeyWord")
                eq("status",1)
                order("lastUpdated","desc")
            }
        }
        if(keywordType=="subscribe"){
            keyWordList=KeyWord.createCriteria().list(){
                eq("keyWordType", "subscribe")
                eq("status",1)
                order("lastUpdated","desc")
            }
        }
        if(keywordType=="location"){
            keyWordList=KeyWord.createCriteria().list(){
                eq("keyWordType", "noKeyWord")
                eq("status",1)
                order("lastUpdated","desc")
            }
        }
        if(keywordType=="CLICK"){
            keyWordList=KeyWord.createCriteria().list(){
                eq("keyWordEvent", keyWordEvent)
                eq("status",1)
                order("lastUpdated","desc")
            }
        }
        if(!keyWordList){
            keyWordList=KeyWord.createCriteria().list(){
                eq("keyWordType", "noKeyWord")
                eq("status",1)
                order("lastUpdated","desc")
            }
        }
        return keyWordList
    }


    def textMessagetoXml(def message) {
        println "-------------------textMessagetoXml-----------------------"
        StringBuilder itemsSb = new StringBuilder();
        def engine = new groovy.text.SimpleTemplateEngine();
        def xmlTemp = engine.createTemplate(textTemplet);
        def amap=[];
        amap = ["toUserName":message.toUserName,
                "fromUserName":message.fromUserName,
                "createTime":message.createTime,
                "content":message.content]
        return xmlTemp.make(amap).toString();
    }
    def newsMessagetoXml(def message,def articles,def sequence){
        println "-------------------newsMessagetoXml-----------------------"
        StringBuilder itemsSb = new StringBuilder();
            for (Article item : articles.articles.sort(new Comparator<Article>() {
                public int compare(Article o1, Article o2) {
                    return o1.sequ > o2.sequ ? 1 : -1;
                }
            })) {
                def engine = new groovy.text.SimpleTemplateEngine();
                def url
                if(item.isCreated){
                    url=item.url
                }else{
                    url=item.url
                }
                def map = ["title":item.title,
                           "description":item.description,
                           "picUrl":item.picUrl,
                           "url":url]
                def itemTemp = engine.createTemplate(itemTemplate);
                String result = itemTemp.make(map).toString()
                itemsSb.append(result).append("\n")
            }
        def engine = new groovy.text.SimpleTemplateEngine();
        def xmlTemp = engine.createTemplate(newsTemplate);
        def amap = ["toUserName":message.toUserName,
                    "fromUserName":message.fromUserName,
                    "createTime":message.createTime,
                    "msgType":message.msgType,
                    "articleCount":message.articleCount,
                    "items":itemsSb.toString()]
        return xmlTemp.make(amap).toString();
    }

    def textTemplet = """<xml>
         <ToUserName><![CDATA[\${toUserName}]]></ToUserName>
         <FromUserName><![CDATA[\${fromUserName}]]></FromUserName>
         <CreateTime>\${createTime}</CreateTime>
         <MsgType><![CDATA[text]]></MsgType>
         <Content><![CDATA[\${content}]]></Content>
         </xml>"""


    def newsTemplate = """<xml>
         <ToUserName><![CDATA[\${toUserName}]]></ToUserName>
         <FromUserName><![CDATA[\${fromUserName}]]></FromUserName>
         <CreateTime>\${createTime}</CreateTime>
         <MsgType><![CDATA[news]]></MsgType>
         <ArticleCount>\${articleCount}</ArticleCount>
         <Articles>
            \${items}</Articles>
         <FuncFlag>1</FuncFlag>
         </xml>"""

    def itemTemplate = """<item>
         <Title><![CDATA[\${title}]]></Title>
         <Description><![CDATA[\${description}]]></Description>
         <PicUrl><![CDATA[\${picUrl}]]></PicUrl>
         <Url><![CDATA[\${url}]]></Url>
         </item>"""


    def NewstoHtml(def htmltitle,def title,def newimagesPath,def isShowPic,def originUrl,def author,def content,def htmldir,def htmlurl) {
        println "-------------------NewstoHtml-----------------------"
        StringBuilder itemsSb = new StringBuilder();
        def engine = new groovy.text.SimpleTemplateEngine();
        def htmlTemp = engine.createTemplate(htmltemplate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        def editorTime=sdf.format(System.currentTimeMillis());
        def amap=[];
        amap = ["content":content]
        amap <<["title":title]
        amap <<["editorTime":editorTime]
        amap <<["htmltitle":htmltitle]
        if(originUrl){
            int index=originUrl.indexOf("http://");
            if(!(index==-1)){
                originUrl=originUrl.substring(index+7)
            }
            amap<<["originUrl":"<a class='left' href='http://${originUrl}'>阅读原文</a>"]
        }else{
            amap<<["originUrl":""]
        }
        if(author){
            amap<<["author":"${author}<br>"]
        }else{
            amap<<["author":""]
        }
        if(isShowPic.equals("true")){
            amap<<["image":"<img width='100%' src='"+newimagesPath+"'/>"]
        }else{
            amap<<["image":""]
        }

        amap<<["wechat_icon":PublicAccount.first().weixinIcon]


        def contentTemp=htmlTemp.make(amap).toString();
        def htmltemp = new File("${htmldir}/${htmlurl}")
        htmltemp.createNewFile()
        OutputStream out=new FileOutputStream(htmltemp);
        PrintWriter pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"utf-8")));
        pw.print(contentTemp);
        pw.close();
        return htmlurl
    }


    def htmltemplate="""
<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
  <script type="text/javascript" src="/js/jquery.min.js"></script>
  <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="/WechatHtml/js/wxconfig.js"></script>
    <script type="text/javascript" src="/WechatHtml/js/wxshare.js"></script>
  <link rel="stylesheet" type="text/css" href="/WechatHtml/css/wechatHtml.css">
  <title>\${htmltitle}</title>
 </head>
 <body>
  <div class="wrapper">
   <div class="main">
    <h2>\${title}</h2>
    <div>
     <em class="date">\${editorTime}</em><a href="" class="titleName">\${author}</a>
    </div>
    <div>\${image}</div>
    \${content}
   </div>
   <div class="footer clearfix">
    \${originUrl}
   </div>
   <div class="qr_code_pc">
    <img id="js_pc_qr_code_img" class="qr_code_pc_img" src="\${wechat_icon}">
    <p>微信扫一扫<br>关注该公众号</p>
   </div>
  </div>
 </body>
</html>
"""

}
