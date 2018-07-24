package dtp.biz


import grails.rest.*

@Resource(readOnly = false, formats = ['json', 'xml'])
class BranchWarehouse {

	String branchWarehouseNo
	String branchWarehouseName
	String address
	String tel

	Date dateCreated
	Date lastUpdated

    static mapping = {
        version (false)
 		id name:'branchWarehouseNo', generator: 'assigned'        
    }

	static constraints = {
		branchWarehouseName size: 0..20, blank: false
		address   size: 0..80, blank: false
		tel size: 0..20, blank: false
	}
}