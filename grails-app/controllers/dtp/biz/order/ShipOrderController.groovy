package dtp.biz.order

import dtp.biz.Item
import dtp.biz.Warehouse
import dtp.biz.order.ShipOrder
import dtp.biz.order.ShipOrderItem
import grails.converters.*
import dtp.util.*

class ShipOrderController {
	static responseFormats = ['json', 'xml']

	def shipOrderService

	def warehouseService
	
    def index() { }
	
    def createShipOrder() {

    	log.info('request.JSON: ' + request.JSON)

    	def ins = request.JSON

    	def _warehouse = Warehouse.findByWarehouseNo(ins['warehouseNo'])

		def _order = new ShipOrder(ins)

		_order.warehouse = _warehouse

		log.info('order: ' + _order.properties)

		// _order.save()
		

		log.info('items: ' + ins['items'])


		def items = ins['items']

		def _orderItems = []

		items.each{
			log.info('item info: ' + it)
			def _item = Item.get(it.itemId)
			def _shipOrderItem = new ShipOrderItem(it)
			log.info('order item : ' + _shipOrderItem.properties)
			_shipOrderItem.shipOrder = _order
			_shipOrderItem.item = _item
			log.info('order item binded: ' + _shipOrderItem.properties)			
			// new ShipOrderItem(shipOrder:_order, item:_item, batchNo:it.batchNo, qty:it.qty).save()
			_orderItems.add(_shipOrderItem)
			// _shipOrderItem.save()
		}

		shipOrderService.createShipOrder(_order, _orderItems)

		//todo: only confirmed then we will increase warehouse
		warehouseService.createWarehouseItem(_order)

		// def _item = Item.get(ins['itemId'])		

		// def _orderItem = new OrderItem(price:ins['price'], qty:ins['qty'])

		// _orderItem.order = _order
		// _orderItem.item = _item

		// _order = orderService.createOrder(_order, _orderItem)  

    	respond(_order)
    }

    def createTransferOrder() {

    	log.info('request.JSON: ' + request.JSON)

    	def ins = request.JSON

    	def _warehouse = Warehouse.findByWarehouseNo(ins['warehouseNo'])
    	def _toWarehouse = Warehouse.findByWarehouseNo(ins['toWarehouseNo'])

		def _order = new ShipOrder(ins)

		_order.warehouse = _warehouse
		_order.toWarehouse = _toWarehouse
		_order.shipOrderType = ShipOrderType.TRANSFER

		log.info('order: ' + _order.properties)

		log.info('items: ' + ins['items'])


		def items = ins['items']

		def _orderItems = []

		items.each{
			log.info('item info: ' + it)
			def _item = Item.get(it.itemId)
			def _shipOrderItem = new ShipOrderItem(it)
			log.info('order item : ' + _shipOrderItem.properties)
			_shipOrderItem.shipOrder = _order
			_shipOrderItem.item = _item
			log.info('order item binded: ' + _shipOrderItem.properties)			
			// new ShipOrderItem(shipOrder:_order, item:_item, batchNo:it.batchNo, qty:it.qty).save()
			_orderItems.add(_shipOrderItem)
			// _shipOrderItem.save()
		}

		//create transfer order indeed
		//distinguished by ship order type TRANSFER
		shipOrderService.createShipOrder(_order, _orderItems)

		//todo: only confirmed then we will increase warehouse
		warehouseService.transferWarehouseItem(_order)

 

    	respond(_order)
    }    

    def queryShipOrder() {
    	Integer max
    	params.max = Math.min(max ?: 10, 100)
        
        def result;
        if(params.query){
            result=ShipOrder.createCriteria().list(params){
                ilike(params.query,"%${params.queryParams}%")
            }
        }else {
            result= ShipOrder.list(params)
        }
        respond result,[metadata: [total: ShipOrder.count(), max: params.max, offset: params.offset ?: 0]]

        // def responseData = [
        //     'results': items,
        //     'total':total
        //     ]
        // render responseData as JSON
    }

    def queryShipOrderByMedicine(){
    	Integer max

    	// log.info(' =========== params ' + params)

    	params.max = Math.min(max ?: 10, 100)
    	def isMedicine = params.isMedicine
		def shipOrderItems = []
		 
		def items = Item.findAllByIsMedicine(isMedicine)
		// log.info(' =========== items ' + items)
		// log.info(' =========== items.size() ' + items.size())
		def results = []
		def total = 0

		

		if(items.size()>0){
			items.each{

			    if(ShipOrderItem.findByItem(it)){
			    	ShipOrderItem.findAllByItem(it).each{
			    		shipOrderItems.push(it)
			    	}
			        // shipOrderItems.push(ShipOrderItem.findAllByItem(it))
			    }
			}
			//for count
			// log.info(' =========== items ' + items)
			// log.info(' =========== shipOrderItems ' + shipOrderItems)

			if(shipOrderItems.size()>0){
				shipOrderItems.each{
			       if(ShipOrder.findByIdAndShipOrderType(it.shipOrderId,ShipOrderType.STOCKIN)){
			           total++
			       }
			    }

				def searchClosure = {
					eq("shipOrderType", ShipOrderType.STOCKIN)
					or {
					    shipOrderItems.each{
					    	// println "_id =============== " + it.shipOrderId
					       	if(ShipOrder.findById(it.shipOrderId)){
					           	def _id = it.shipOrderId
					           	// println "_id =============== " + _id
								
							  	eq("id", _id)
								
					       	}
					    }
				    }
				}

				results = ShipOrder.createCriteria().list ([max: params.max, offset: params.offset ?: 0],searchClosure) 
			}
			
		}

		def responseData = [
            'results': results,
            'total':total
        ]

        JSON.use('deep') {
            render responseData as JSON
        }  
		// log.info(' =========== results ' + results)
		// respond results,[metadata: [total: total, max: params.max, offset: params.offset ?: 0]]

    }

    def queryTransferShipOrder(){
    	ShipOrderType shipOrderType = ShipOrderType.TRANSFER

		def searchClosure = {
		  eq("shipOrderType",shipOrderType)
		}

		def results = ShipOrder.createCriteria().list ([max: params.max, offset: params.offset ?: 0],searchClosure) 

		def responseData = [
            'results': results,
            'total':results.size()
        ]

        JSON.use('deep') {
            render responseData as JSON
        }  


		// respond results,[metadata: [total: results.size(), max: params.max, offset: params.offset ?: 0]]
    }

    def queryShipOrderDetailById(){

    	def shipOrder = ShipOrder.get(request.JSON.shipOrderId)

		def shipOrderItem = ShipOrderItem.findAllByShipOrder(shipOrder)

		def map = new HashMap()

		def item = shipOrderItem.first().item

		def shipOrderItems = new ArrayList()

		shipOrderItem.each{
			shipOrderItems.push(it.properties)
		}

		map.put("shipOrder",shipOrder)
		map.put("item",item)
		map.put("shipOrderItems",shipOrderItems)

		// println "map = ==================== "+ map
 		// respond map
 		render map as JSON
    }

    
}
