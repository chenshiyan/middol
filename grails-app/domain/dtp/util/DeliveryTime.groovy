package dtp.util

class DeliveryTime {

	String deliveryTime
	String description

    static mapping = {
        version (false)
 		id name:'deliveryTime', generator: 'assigned' 
 		cache true       
    }

	static constraints = {
		deliveryTime size: 0..20, blank: false, unique:true
		description size: 0..20, blank: false
	}
}