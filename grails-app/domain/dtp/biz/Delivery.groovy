package dtp.biz

import dtp.sys.*

import grails.rest.*

class Delivery {
	
	String deliveryNo
	String deliveryName
	String tel

	String remark
	User user
	Date dateCreated
	Date lastUpdated

    static mapping = {
    	user column:'username'
        version (false)
    }

	static constraints = {
		deliveryNo size: 0..20, blank: false, unique: true
		deliveryName  size: 0..20, blank: false
		tel size: 0..20, blank: false
		remark size: 0..40,  nullable: true
		// status size: 0..10, blank: false
		user nullable: true
	}

}