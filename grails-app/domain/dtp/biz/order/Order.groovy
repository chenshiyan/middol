package dtp.biz.order

import dtp.biz.Warehouse
import dtp.biz.Customer
import dtp.biz.Hospital
import dtp.biz.Sales
import dtp.util.*

//@CompileStatic
class Order {

	transient springSecurityService
	//auto generated rule
	String orderNo

	Date orderDate = new Date()
	//default value
	OrderType orderType = OrderType.SALE

	Boolean forCurrentPeriod = Boolean.TRUE

	String status

	String requested

	Hospital hospital
	Sales sales
	Customer customer
	Warehouse warehouse

	PaymentType paymentType
	DeliveryType deliveryType
	DeliveryTime deliveryTime

	String remark

	Date dateCreated
	Date lastUpdated
    String  createdBy
    String  lastUpdatedBy

    static transients = ['springSecurityService']	

	def beforeInsert() {
		generateOrderNo()
		createdBy = springSecurityService.getPrincipal()?.getUsername()
	}	

    def beforeUpdate(){ 
    	lastUpdatedBy = springSecurityService.getPrincipal()?.getUsername() 
    }	

	//Simple rule to use timestamp 
	protected void generateOrderNo() {
		//random string
		//2 digital length
		orderNo = orderType.getId() + new Date().format('yyyyMMddmmss') 
	}

	static mapping = {
		warehouse column:'warehouse_no'
		deliveryType column:'delivery_type'
		deliveryTime column:'delivery_time'
		paymentType column:'payment_type'
	}	

	static constraints = {
    	orderNo blank: false, unique:true
    	requested nullable:true
    	remark nullable:true
    	warehouse nullable:true
        dateCreated   nullable: true
        lastUpdated   nullable: true
        createdBy     nullable: true
        lastUpdatedBy nullable: true    	
	}	

}