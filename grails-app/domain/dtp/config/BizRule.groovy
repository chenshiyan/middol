package dtp.config

class BizRule {

	String name
	String description

	static mapping = {
		version(false)
	}

    static constraints = {
    	name unique:true
    	description nullable:true
    }
}
