package dtp.biz.order

import dtp.biz.Item

class OrderItem {
	//item reference
	Item item

	Integer qty
	BigDecimal price

	static belongsTo = [order: Order]

	static mapping = {
		version(false)		
	}

	static constraints = {
		item blank: false
		qty blank: false
		price blank: false
	}	
}