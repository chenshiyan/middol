package dtp.util

class OrderStatus {

	String orderStatus
	String description

    static mapping = {
        version (false)
 		id name:'orderStatus', generator: 'assigned'        
    }

	static constraints = {
		orderStatus size: 0..20, blank: false, unique:true
		description size: 0..20, blank: false
	}
}