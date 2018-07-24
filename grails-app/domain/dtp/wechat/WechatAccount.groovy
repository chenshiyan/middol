package dtp.wechat

class WechatAccount implements Comparable{

    String nickName
    String openId
    Date  dateCreated
    Date lastUpdated
    String headimgUrl
    String province
    String sex
    String city
    String country
    String privilege
    String language
    String source
    String subscribe_time
    String remark
    String groupid     //用户所在的分组ID(微信公众平台上的)
    String keyWord
    String registDate
    String isRegisted = "签到"
    Long integration = 0
    String status="1"


    static belongsTo = [wechatAccountGroup:WechatAccountGroup]

    static mapping={
        table 'Wechat_account'
    }

    static constraints = {
        nickName(nullable:true)
        headimgUrl(nullable:true)
        sex(nullable:true)
        city(nullable:true)
        province(nullable:true)
        country(nullable:true)
        privilege(nullable:true)
        keyWord(nullable:true)
        groupid(nullable:true)
        remark(nullable:true)
        subscribe_time(nullable:true)
        registDate(nullable:true)
        isRegisted(nullable:true)
        source(nullable:true)
    }

    @Override
    int compareTo(Object o) {
        return o.id > id ? 1 : -1;
    }
}
