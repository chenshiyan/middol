package dtp.biz


import grails.rest.*

class Spec {
	String specName
	String status
	Date dateCreated
	Date lastUpdated
	static constraints = {
		specName  size: 0..40, blank: false
		status size: 0..40, nullable: true
	}
}