package dtp.biz.order

import dtp.biz.Warehouse
import dtp.biz.Customer
import dtp.biz.Hospital
import dtp.biz.Sales
import dtp.util.*

class OrderHistory {

	String orderNo

	Date orderDate
	//default value
	OrderType orderType 

	Boolean forCurrentPeriod 

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

    OrderHistory(Order order) {
    	this()
    	this.properties = order.properties
    }


	static mapping = {
		warehouse column:'warehouse_no'
		deliveryType column:'delivery_type'
		deliveryTime column:'delivery_time'
		paymentType column:'payment_type'
		version (false)
		autoTimestamp(false)
	}	

    static constraints = {
    	orderNo index: 'idx_orderhistory_orderno'    
		orderDate nullable: true
		orderType nullable: true
		forCurrentPeriod nullable: true
		status nullable: true
		hospital nullable: true
		sales nullable: true
		customer nullable: true
		paymentType nullable: true
		deliveryType nullable: true
		deliveryTime nullable: true
    	requested nullable:true
    	remark nullable:true
    	warehouse nullable:true
        dateCreated   nullable: true
        lastUpdated   nullable: true
        createdBy     nullable: true
        lastUpdatedBy nullable: true
	 
    }
}
