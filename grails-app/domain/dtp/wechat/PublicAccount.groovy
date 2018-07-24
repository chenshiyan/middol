package dtp.wechat

class PublicAccount {
    String appid
    String appsecret
    String token
    String publicAccountName
    String weixinIcon
    String partnerKey
    String partnerId
    String paySignKey
    String payReturnUrl
    Date dateCreated
    Date lastUpdated
    String status="1"
    String dr
    String htmlTitle
    String accountType
    String accessToken
    String jsApiTicket

    static mapping={
        table 'Wechat_PublicAccount'
    }
    static constraints = {
        appid(nullable:true)
        appsecret(nullable:true)
        token(nullable:true)
        publicAccountName(nullable:true)
        dr(nullable:true)
        weixinIcon(nullable:true)
        partnerId(nullable:true)
        partnerKey(nullable:true)
        paySignKey(nullable:true)
        payReturnUrl(nullable:true)
        htmlTitle(nullable:true)
        accountType(nullable:true)
        accessToken(nullable:true)
        jsApiTicket(nullable:true)
    }
}
