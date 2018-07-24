package dtp.biz


import grails.rest.*

class Hospital {
	String hospitalNo
	String hospitalName
	String address
	String remark
	Date dateCreated
	Date lastUpdated

    static mapping = {
        version (false)
    }

	static constraints = {
		hospitalNo size: 0..20, blank: false, unique: true
		hospitalName  size: 0..40, blank: false
		address size: 0..80, blank: false
		remark size: 0..40, nullable: true
	}
}