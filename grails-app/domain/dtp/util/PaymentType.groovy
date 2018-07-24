package dtp.util

class PaymentType {

	String paymentType
	String description

    static mapping = {
        version (false)
 		id name:'paymentType', generator: 'assigned'        
 		cache true
    }

	static constraints = {
		paymentType size: 0..20, blank: false, unique:true
		description size: 0..20, blank: false
	}
}