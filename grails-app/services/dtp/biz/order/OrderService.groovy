package dtp.biz.order

import dtp.biz.order.Order
import dtp.biz.order.OrderHistory
import dtp.biz.order.OrderItem
import dtp.biz.order.OrderItemHistory
import grails.transaction.Transactional
import dtp.util.*

@Transactional
class OrderService {
    transient springSecurityService
    def createOrder(Order order, OrderItem orderItem) {

    	log.info('order in service: ' + order.properties)

    	def _order = order.save(failOnError:true)
    	// if (!order.save()) {
    		
    	// }
        def _orderItem = orderItem

    	_orderItem.order = _order

    	log.info('_orderItem in service: ' + _orderItem.properties)

    	_orderItem.save()

    	def orderH = new OrderHistory(_order)
        orderH.lastUpdatedBy = null
    	orderH.save(failOnError:true)  

        def _orderItemH =  new OrderItemHistory()

        _orderItemH.qty = orderItem.qty
        _orderItemH.price = orderItem.price
        _orderItemH.item = orderItem.item

        _orderItemH.orderHistory = orderH

        _orderItemH.save()

		log.info('_order in service: ' + _order.properties)

    	return _order
    }

    def updateOrder(Order order, OrderItem orderItem) {

    	log.info('order in service: ' + order.properties)

    	log.info('orderitem in service: ' + orderItem)

    	def _order = order.save(failOnError:true)

        def _orderItem = orderItem

    	if (_orderItem != null) {
	    	_orderItem.order = _order

	    	log.info('_orderItem in service: ' + _orderItem.properties)

	    	_orderItem.save()    		
    	}


    	def orderH = new OrderHistory(_order)
        orderH.lastUpdatedBy = springSecurityService.getPrincipal()?.getUsername()
    	orderH.save(failOnError:true)  

        def _orderItemH =  new OrderItemHistory()

        if(orderItem){
        _orderItemH.qty = orderItem.qty
        _orderItemH.price = orderItem.price
        _orderItemH.item = orderItem.item

        _orderItemH.orderHistory = orderH

        _orderItemH.save()
        }else{
            orderItem = OrderItem.findByOrder(_order)
            _orderItemH.qty = orderItem.qty
            _orderItemH.price = orderItem.price
            _orderItemH.item = orderItem.item

            _orderItemH.orderHistory = orderH

            _orderItemH.save()
        }
		log.info('_order in service: ' + _order.properties)

    	return _order
    }    

    def processOrderStatus(Order order) {

    }

    def getOrderStatus(String event) {
    	def bizRule = BizRule.findByName(BizRuleType.ORDERPROCESS)
    	
    }
}
