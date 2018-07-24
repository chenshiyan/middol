package dtp.biz

import dtp.biz.Hospital
import dtp.biz.Manufacturer
import dtp.sys.*

class Sales {
	
	String salesNo
	String salesName
	String tel

	// Group group
	String groupName
	Manufacturer manufacturer
	String status = 1
	String remark
	User user
	Date dateCreated
	Date lastUpdated

	static hasMany = [hospitals: Hospital]

    static mapping = {
    	user column:'username'
        version (false)
    }

	static constraints = {
		salesNo size: 0..20, blank: false, unique: true
		salesName  size: 0..20, blank: false
		tel size: 0..20, blank: false
		remark size: 0..40,  nullable: true
		// status size: 0..10, blank: false
		user nullable: true
	}

}