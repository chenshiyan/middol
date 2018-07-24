package dtp.biz.order

import dtp.biz.Item

class OrderItemHistory {

	//item reference
	Item item

	Integer qty
	BigDecimal price

	static belongsTo = [orderHistory: OrderHistory]

	static mapping = {
		version(false)		
	}

	static constraints = {
    	// '*'(nullable:true,blank:true)

	}
}
