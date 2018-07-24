package dtp.wechat

class AccessTokenLog {

    String accessToken
    String publicAccountName
    Long createdTime
    Date dateCreated
    Date lastUpdated


    static mapping={
        table 'Wechat_accessTokenLog'
        accessToken type: 'text'
    }

    static constraints = {
        accessToken(nullable:true)
        publicAccountName(nullable:true)
        createdTime(nullable:true)
    }
}
