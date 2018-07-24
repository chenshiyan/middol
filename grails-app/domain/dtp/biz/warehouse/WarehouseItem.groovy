package dtp.biz.warehouse

import dtp.biz.Item
import dtp.biz.Warehouse

class WarehouseItem {

	Warehouse warehouse

	Item item

	String batchNo
	Double qty
	Date dateManufactured
	Date dateExpired

    static mapping = {
        version (false)
        warehouse column:'warehouse_no', index:'idx_warehouseitem'
        item index:'idx_warehouseitem_item_batch'
		batchNo index:'idx_warehouseitem_item_batch'
    }

    static constraints = {
    }
}
