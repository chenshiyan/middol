package dtp.wechat

class JsApiTicket {

    String jsApiTicket
    String publicAccountName
    Long createdTime
    Date dateCreated

    static mapping={
        table 'Wechat_JsApiTicket'
        jsApiTicket type: 'text'
    }

    static constraints = {
        jsApiTicket(nullable:true)
        publicAccountName(nullable:true)
        createdTime(nullable:true)
    }
}
