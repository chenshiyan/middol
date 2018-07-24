package dtp.util

class UnitType {
	String unit
	String description

	static mapping = {
        version (false)
 		id name:'unit', generator: 'assigned'        
 		cache true
    }
	static constraints = {
		unit size: 0..20, blank: false, unique:true
		description size: 0..20, blank: false
	}
}