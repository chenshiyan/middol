package dtp.wechat

class Text {

    String content
    Date dateCreated
    Date lastUpdated
    String status="1"

    static mapping={
        table 'Wechat_text'
        content type: 'text'
    }

    static constraints = {
        content(nullable:false)
    }
}

