package dtp.biz


class Warehouse {

	String warehouseNo
	String warehouseName
	String address
	String tel

	Date dateCreated
	Date lastUpdated

    static mapping = {
        version (false)
 		id name:'warehouseNo', generator: 'assigned'        
    }

	static constraints = {
		warehouseName size: 0..20, blank: false, unique:true
		address   size: 0..80, blank: false
		tel size: 0..20, blank: false
	}
}