package dtp.wechat

class KeyWord {
    String question
    boolean exactMatch  //完全匹配
    String keyWordType="normal"  //1:text  2:subscribe  3:no_keyword
    String category
    int replyType                //1:text  2:articles 3:kefu
    Date dateCreated
    Date lastUpdated
    String textAnswer
    String keyWordEvent
    int sequ
    String status="1"
    boolean kf =false     //客服
    Articles articles

    static mapping={
        table 'Wechat_keyWord'
        textAnswer type: 'text'
    }

    static constraints = {
        sequ(nullable:true)
        keyWordType(nullable:true)
        category(nullable:true)
        question(nullable:true)
        replyType(nullable:false)
        exactMatch(nullable:true)
        textAnswer(nullable:true)
        articles(nullable:true)
        keyWordEvent(nullable:true)
        kf(nullable:true)
    }
}
