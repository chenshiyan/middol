package dtp.wechat

class Article {
    String author
    boolean isShowPic=false
    String originUrl
    String title
    String description
    String picUrl
    boolean isCreated=false
    String url
    String articleContent
    Date dateCreated
    Date lastUpdated
    String status="1"
    int sequ

    static mapping={
        table 'Wechat_article'
        description type: 'text'
        articleContent type: 'text'
        originUrl type: 'text'
    }

    static constraints = {
        title(nullable:false)
        description(nullable:false)
        picUrl(nullable:false)
        url(nullable:false)
        originUrl(nullable:true)
        author(nullable:true)
        articleContent(nullable:false)
        sequ(nullable:false)
    }
}
