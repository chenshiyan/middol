package dtp.biz.order

import dtp.biz.order.ShipOrder
import grails.transaction.Transactional

@Transactional
class ShipOrderService {

    def createShipOrder(ShipOrder shipOrder, List shipOrderItems) {

    	def _order = shipOrder.save()

    	shipOrderItems.each {
    		it.save()
    	}

    	return _order
    }
}
