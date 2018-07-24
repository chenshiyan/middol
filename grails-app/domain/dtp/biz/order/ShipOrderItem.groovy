package dtp.biz.order

import dtp.biz.Item

class ShipOrderItem {

	Item item

	String batchNo
	Double qty
	Date dateManufactured
	Date dateExpired

	static belongsTo = [shipOrder: ShipOrder]

    static constraints = {
    	item blank:false
		batchNo   size: 0..80, blank: false
		qty size: 0..20, blank: false    	
    }
}
