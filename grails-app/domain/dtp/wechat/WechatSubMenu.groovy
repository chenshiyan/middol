package dtp.wechat

class WechatSubMenu {
    String name
    String type
    String eventKey
    String url
    String mark
    String sequ
    Date dateCreated
    Date lastUpdated
    String status="1"
    KeyWord keyWord

    static mapping={
        table 'Wechat_SubMenu'
    }
    static belongsTo = [wechatMenu:WechatMenu]
    static constraints = {
        type(nullable:true)
        url(nullable:true)
        eventKey(nullable:true)
        mark(nullable:true)
        wechatMenu(nullable: true)
        keyWord(nullable: true)
    }
}
