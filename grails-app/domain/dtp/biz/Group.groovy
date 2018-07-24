package dtp.biz


class Group {

	String groupNo
	String groupName
	String area
	String item
	String status
	Date dateCreated
	Date lastUpdated

    static mapping = {
        version (false)
    }

	static constraints = {
		groupNo size: 0..20, blank: false, unique: true
		groupName size: 0..20, blank: false
		area   size: 0..20, blank: false
		item size: 0..10, blank: false
	}
}