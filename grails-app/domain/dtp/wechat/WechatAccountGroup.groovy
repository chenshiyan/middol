package dtp.wechat

class WechatAccountGroup {
	String groupName
	String description
	String dr
	String status="1"
	Date dateCreated
	Date lastUpdated
	int present = 0

	static hasMany = [wechatAccounts:WechatAccount]

	static mapping = {
		table 'wechat_accountGroup'
	}

    static constraints = {
    	wechatAccounts(nullable:true)
    	groupName(nullable:true)
    	description(nullable:true)
    	dr(nullable:true)
    }
}
