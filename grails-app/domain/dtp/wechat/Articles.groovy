package dtp.wechat

class Articles {
    Integer articleCount
    String author
    String title
    String description
    Date dateCreated
    Date lastUpdated
    Integer[] sequence
    String status="1"
    static hasMany = [articles: Article]

    static mapping={
        table 'Wechat_articles'
        articles column: 'articles_articleId',joinTable:'wechat_articles_article'
        description type: 'text'
        title type: 'text'
        author type: 'text'
        description  type: 'text'
    }

    static constraints = {
        title(nullable:true)
        author(nullable:true)
        articleCount(nullable:false)
        description(nullable:true)
        sequence(nullable:true)
    }
}
