package dtp.wechat

class InMessage {
    String toUserName
    String fromUserName
    String msgType
    String openId
    String msgId
    String picUrl
    String content
    String createTime
    Date dateCreated
    String event
    String url
    String eventKey
    String message   //记录接收的xml
    String mediaId
    String ticket    //二维码的ticket，可用来换取二维码图片
    String latitude   //地理位置纬度
    String longitude  //地理位置经度
    String locPrecision  //地理位置精度
    String thumbMediaId
    String format
    String status="1"

    static belongsTo = [wechatAccount:WechatAccount]

    static mapping={
        table 'Wechat_InMessage'
        content type: 'text'
        message type: 'text'
    }

    static constraints = {
        content(nullable:true)
        toUserName(nullable:false)
        fromUserName(nullable:false)
        msgType(nullable:false)
        openId(nullable:false)
        msgId(nullable:true)
        createTime(nullable:false)
        event(nullable:true)
        message(nullable:true)
        mediaId(nullable:true)
        eventKey(nullable:true)
        url(nullable:true)
        ticket(nullable:true)
        latitude(nullable:true)
        longitude(nullable:true)
        locPrecision(nullable:true)
        picUrl(nullable:true)
        thumbMediaId(nullable:true)
        format(nullable:true)
    }
}
