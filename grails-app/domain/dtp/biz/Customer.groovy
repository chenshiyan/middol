package dtp.biz


class Customer {
	String customerNo
	String customerName
	String idNo    //身份证号码
	Sales sales
	String sex
	Date birth
	String tel
	String address
	String contact
	
	String status //0表示新客户
	int times
	Date dateStarted

	Date dateCreated
	Date lastUpdated

    static mapping = {
        version (false)
    }

	static constraints = {
		customerNo size: 0..20, blank: false, unique: true
		customerName  size: 0..40, blank: false
		dateStarted nullable: true
		sex size: 0..10, blank: false
		tel size: 0..20, blank: false
		address size: 0..60, nullable: true
		status  size: 0..10, blank: false
		contact size:0..40,nullable: true
		times nullable: true
		idNo nullable: true
		dateStarted nullable: true
	}
}