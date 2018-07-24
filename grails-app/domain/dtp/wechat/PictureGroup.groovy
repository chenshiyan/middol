package dtp.wechat

class PictureGroup {
    String groupName
    String description
    String dr
    String status="1"
    Date dateCreated
    Date lastUpdated

    static hasMany = [pictureSources:PictureSource]

    static mapping = {
        table 'Wechat_PictureGroup'

    }
    static constraints = {
        pictureSources(nullable: true)
        groupName(nullable: true)
        description(nullable: true)
        dr(nullable: true)
    }
}
