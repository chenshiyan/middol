package dtp.wechat

class PictureSource {
    String pictureName
    String picUrl
    String description
    String dr
    String status="1"
    Date dateCreated
    Date lastUpdated
    Long groupId=0
    String type
    static mapping = {
        table 'Wechat_Picture'

    }
    static constraints = {
        groupId(nullable: true)
        pictureName(nullable: true)
        picUrl(nullable: true)
        description(nullable: true)
        dr(nullable: true)
    }
}
