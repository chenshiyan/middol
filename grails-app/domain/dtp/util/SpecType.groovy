package dtp.util

class SpecType {
	String spec
	String description

	static mapping = {
        version (false)
 		id name:'spec', generator: 'assigned'        
 		cache true
    }
	static constraints = {
		spec size: 0..20, blank: false, unique:true
		description size: 0..20, blank: false
	}
}