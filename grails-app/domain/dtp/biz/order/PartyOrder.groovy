package dtp.biz.order

class PartyOrder {

	//in future role and user are all possible
	//now temparorily only allow user?
	String partyType = 'USER'
	//User user
	String username
	String createBy
	//use orderNo as unique id?
	String orderType
	String orderNo

	String status
	String action
	String viewUrl
	String editUrl
	String actionUrl

	static mapping = {
		version(false)
	}

    static constraints = {    	
    	orderNo index: 'idx_partyorder_orderno'  
    	username index: 'idx_partyorder_username'  
    	action nullable:true
    	viewUrl nullable:true
    	editUrl nullable:true
    	actionUrl nullable:true
    }
}
