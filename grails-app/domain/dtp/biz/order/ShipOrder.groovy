package dtp.biz.order

import dtp.biz.Warehouse
import dtp.util.*

class ShipOrder {

	//default value
	ShipOrderType shipOrderType = ShipOrderType.STOCKIN
	
	//auto generated rule?
	String orderNo 

	//original reference order no
	String refOrderNo

	// enum ShipOrderStatus {
	// 	SHIPPED(1)
	// }
	def beforeInsert() {
		generateOrderNo()
	}	

	//Simple rule to use timestamp 
	protected void generateOrderNo() {
		//random string
		//2 digital length
		orderNo = shipOrderType.getId() + new Date().format('yyyyMMddmmss') 
	}	

	String status
	//reference to warehouseNo
	Warehouse warehouse

	//for transfer order
	Warehouse toWarehouse

	// static hasMany = [shipOrderItems: ShipOrderItem]
	
	Date dateShipped = new Date()

	Date dateCreated
	Date lastUpdated	

	static mapping = {
		warehouse column:'warehouse_no'
		toWarehouse column:'to_warehouse_no'
	}

    static constraints = {
    	 orderNo unique:true
    	 refOrderNo nullable:true
    	 toWarehouse nullable:true
    	 // dateShipped defaultValue: "now()"
    }
}
