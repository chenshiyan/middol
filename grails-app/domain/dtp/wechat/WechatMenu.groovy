package dtp.wechat

class WechatMenu {

        String name
        String type
        String eventKey
        String url
        String mark
        int sequ
        Date dateCreated
        Date lastUpdated
        String status="1"
        KeyWord keyWord

        static mapping={
            table 'Wechat_Menu'
        }
        static hasMany = [wechatSubMenu:WechatSubMenu]
        static constraints = {
            type(nullable:true)
            url(nullable:true)
            eventKey(nullable:true)
            mark(nullable:true)
            keyWord(nullable:true)
        }

}
