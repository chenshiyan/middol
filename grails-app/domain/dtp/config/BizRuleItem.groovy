package dtp.config

class BizRuleItem {

	Integer seq

	String event
	String fromStatus
	String toStatus
	String participants 
	String editDisable
	String viewUrl
	String editUrl
	String actionUrl

	static belongsTo = [bizRule: BizRule]

	static mapping = {
		version(false)
		cache:true
	}

    static constraints = {
    	participants nullable:true
    	viewUrl nullable:true
    	editUrl nullable:true
    	actionUrl nullable:true
    	editDisable nullable:true
    }
}
