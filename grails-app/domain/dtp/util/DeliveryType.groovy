package dtp.util

class DeliveryType {

	String deliveryType
	String description

    static mapping = {
        version (false)
 		id name:'deliveryType', generator: 'assigned'    
 		cache true    
    }

	static constraints = {
		deliveryType size: 0..20, blank: false, unique:true
		description size: 0..20, blank: false
	}
}