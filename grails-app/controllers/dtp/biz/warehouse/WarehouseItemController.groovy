package dtp.biz.warehouse

import dtp.biz.Warehouse
import dtp.biz.warehouse.WarehouseItem
import grails.converters.*

class WarehouseItemController {
    static responseFormats = ['json', 'xml']

    def listItemBatchesByWarehouseNo(String warehouseNo) {

    	log.info('===========warehouse param: ' + warehouseNo)

    	def _warehouse = Warehouse.findByWarehouseNo(warehouseNo)

		log.info('warehouse : ' + _warehouse.properties)  

		JSON.use('deep') {
			render WarehouseItem.findAllByWarehouse(_warehouse) as JSON
		}	
    }

    def queryWarehouseItem() {
        Integer max
        params.max = Math.min(max ?: 10, 100)
        
        def result;
        if(params.query){
            result=WarehouseItem.createCriteria().list(params){
                ilike(params.query,"%${params.queryParams}%")
            }
        }else {
            result= WarehouseItem.list(params)
        }

        def responseData = [
            'results': result,
            'total':WarehouseItem.count()
        ]

        JSON.use('deep') {
            render responseData as JSON
        }   

        // respond result,[metadata: [total: WarehouseItem.count(), max: params.max, offset: params.offset ?: 0]]

    }
    
}
