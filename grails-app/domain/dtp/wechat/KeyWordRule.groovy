package dtp.wechat

class KeyWordRule {
    String ruleName
    String keyWordCache
    int replyType
    String textAnswer
    Articles articles
    String status="1"
    Date dateCreated
    Date lastUpdated

    static hasMany = [keywords:KeyWord]
    static mapping = {
        table 'Wehcat_KeyWordRule'
        textAnswer type: 'text'
    }
    static constraints = {
        ruleName(nullable: false)
        keyWordCache(nullable: false)
        replyType(nullable: false)
        textAnswer(nullable: true)
        articles(nullable:true)
    }
}
