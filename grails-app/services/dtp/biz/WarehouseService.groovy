package dtp.biz

import dtp.biz.warehouse.WarehouseItem
import dtp.biz.order.ShipOrder
import dtp.biz.order.ShipOrderItem
import grails.transaction.Transactional

@Transactional
class WarehouseService {

    def createWarehouseItem(ShipOrder shipOrder) {

    	def _warehouse = shipOrder.warehouse

    	def _shipOrderItems = ShipOrderItem.findAllByShipOrder(shipOrder)

    	def _warehouseItems = []

    	_shipOrderItems.each {
    		def _whitem = new WarehouseItem(warehouse:_warehouse, item:it.item, qty:it.qty, batchNo:it.batchNo, dateExpired:it.dateExpired, dateManufactured:it.dateManufactured).save()
    		_warehouseItems.add(_whitem)
    	}

    	return _warehouseItems
    }


    def transferWarehouseItem(ShipOrder shipOrder) {

    	log.info('transferWarehouseItem:' + shipOrder.properties)

    	def _warehouse = shipOrder.warehouse

    	def _toWarehouse = shipOrder.toWarehouse

    	def _shipOrderItems = ShipOrderItem.findAllByShipOrder(shipOrder)

    	def _warehouseItems = []

    	_shipOrderItems.each {_orderItem ->
    		// def _whitem = new WarehouseItem(warehouse:_warehouse, item:it.item, qty:it.qty, batchNo:it.batchNo, dateExpired:it.dateExpired, dateManufactured:it.dateManufactured).save()
			log.info('_shipOrderItem: ' + _orderItem.properties)
    		def _whitem = WarehouseItem.withCriteria(uniqueResult: true) {
    			eq("warehouse", _warehouse)
    			eq("item", _orderItem.item)
    			eq("batchNo", _orderItem.batchNo)
    		}

    		log.info('from whitem: ' + _whitem)

    		//minus fromwarehouse qty
    		_whitem.qty = _whitem.qty - _orderItem.qty

    		_whitem.save()
    		//add towarehouse qty if exist
    		//or else create a new one
    		def _towhitem = WarehouseItem.withCriteria(uniqueResult: true) {
    			eq("warehouse", _toWarehouse)
    			eq("item", _orderItem.item)
    			eq("batchNo", _orderItem.batchNo)
    		}

			log.info('to whitem: ' + _towhitem)

    		if (!_towhitem) {
    			_towhitem = new WarehouseItem(warehouse:_toWarehouse, 
    					item:_orderItem.item, qty:_orderItem.qty, batchNo:_orderItem.batchNo, dateExpired:_orderItem.dateExpired, dateManufactured:_orderItem.dateManufactured)
    			_towhitem.save()
    		}


    		_warehouseItems.add(_whitem)
    	}

    	//only return success status is good enough?
    	return _warehouseItems
    }    
}
