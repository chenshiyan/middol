package dtp.biz.order

import dtp.biz.Customer
import dtp.biz.Hospital
import dtp.biz.Item
import dtp.biz.Sales
import dtp.biz.Warehouse
import dtp.biz.customer.CustomerItem
import dtp.biz.order.Order
import dtp.biz.order.OrderItem
import dtp.sys.User
import dtp.util.*
import grails.converters.*

import static java.lang.Enum.valueOf
import static org.springframework.http.HttpStatus.NO_CONTENT

class OrderController {
    static responseFormats = ['json', 'xml']

    def orderService

    def bizRuleService

    def partyOrderService

    def index(Integer max) {
        def searchClosure = {
            if(params.query&&params.query!=""){
                like(params.query,"%${params.queryParams}%")
            }
            if(params.username&&params.username!=""){
                def salesInstance = Sales.findByUser(User.findByUsername(params.username))
                if(salesInstance){
                eq("sales",salesInstance)
                }   
            }
            if(params.status&&params.status!=""){
                eq("status",params.status)
            }
        }
        def orders = Order.createCriteria();
        orders = orders.list([max:params.max,offset:params.offset ?: 0],searchClosure)
        def total= orders.totalCount
        orders = orders.collect{[
            id:it.id,
            orderNo:it.orderNo,
            orderDate:it.orderDate.format("yyyy-MM-dd"),
            orderType:it.orderType,
            forCurrentPeriod:it.forCurrentPeriod,
            requested:it.requested,
            hospital:it.hospital,
            sales:it.sales,
            customer:it.customer,
            paymentType:it.paymentType,
            warehouse:it.warehouse,
            status:it.status,
            deliveryType:it.deliveryType,
            deliveryTime:it.deliveryTime,
            remark:it.remark,
            dateCreated:it.dateCreated.format("yyyy-MM-dd"),
            lastUpdated:it.lastUpdated.format("yyyy-MM-dd"),
            createdBy:it.createdBy,
            lastUpdatedBy:it.lastUpdatedBy,
            items:OrderItem.findAllByOrder(it).collect{
                [
                id:it.id,
                item:it.item.itemName,
                price:it.price,
                qty:it.qty
            ]}
        ]}
        def responseData = [
            'results': orders,
            'total':total
            ]
        render responseData as JSON
    }


    def delete() {
        Order order=Order.get(params.orderId);
        OrderItem oi=OrderItem.findByOrder(order)
        oi.delete failOnError:true
        order.delete failOnError: true

        render status: NO_CONTENT
    }

    def optionInitForOrder(){
        log.info('request.JSON:'+request.JSON)
        //search paymentType
        def paymentTypeOption=PaymentType.list()
        def deliveryTypeOption=DeliveryType.list()
        def deliveryTimeOption=DeliveryTime.list()
        def warehouseOption=Warehouse.list()
        render new JSON(paymentTypeOption: paymentTypeOption,
            warehouseOption:warehouseOption,
            deliveryTimeOption:deliveryTimeOption,
            deliveryTypeOption:deliveryTypeOption)
    }

    def optionInit() {
        log.info('request.JSON: ' + request.JSON)

        //search sales
        def salesOption = Sales.list(params)

        //search customer
        def customersOption= Customer.list(params)

        //search item
        def itemsOption =Item.list(params)
        render new JSON(salesOption: salesOption, customersOption: customersOption,itemsOption:itemsOption)
    }

    def salesOrderOptionInit(){
        log.info('request.JSON: ' + request.JSON)
        def user
        Sales sales
        if(params.username){
            user=User.findByUsername(params.username)
            //search hospitals
            //TODO 根据username或者userid查出sales，通过sales查出hospital
            sales=Sales.findByUser(user)
        }else{
            sales=Sales.get(params.salesId)
        }
        render new JSON(sales:sales)
    }

    def dispatcherOrderOptionInit(){
        log.info('request.JSON: ' + request.JSON)
        Sales sales;
        def salesOption
        def hospitalsOption
        def customersOption
        def itemsOption
        if(params.salesId){
            sales=Sales.get(params.salesId)
            if(!sales){
            //TODO find no rows exception
            }
            hospitalsOption=sales.hospitals
            //search customer
            customersOption= Customer.createCriteria().list(params) {eq("sales",sales)}
            //search item
            itemsOption= Item.findAllByManufacturer(sales.manufacturer)
        }
        salesOption= Sales.list(params)
        
        render new JSON(sales:sales,salesOption: salesOption,hospitalsOption: hospitalsOption, customersOption: customersOption,itemsOption:itemsOption)
    }


    def createOrderBySales(){
        log.info('request.JSON: ' + request.JSON)
        def ins = request.JSON
        def _sales = Sales.get(ins['salesId'])
        def _hospital = Hospital.get(ins['hospital'])
        def _customer = Customer.get(ins['customerId'])
        def _warehouse = Warehouse.findByWarehouseNo(ins['warehouseNo'])

        //def order = new Order(orderNo:'dummy',status:'DRAFT', requested:'TRUE', sales:sales, customer:customer, warehouse:warehouse, deliveryType:dt, deliveryTime:dti)
        def _order  = new Order(ins)
        _order.properties=ins
        _order.hospital=_hospital
        _order.sales = _sales
        _order.customer = _customer
        _order.warehouse = _warehouse

        log.info('order: ' + _order.properties)
        def _item = Item.get(ins['itemId'])
        def _orderItem = new OrderItem(price: ins['price'], qty: ins['qty'])
        _orderItem.order = _order
        _orderItem.item = _item

        log.info('order in controller: ' + _order)

        log.info('orderService: ' + orderService)        

        _order = orderService.createOrder(_order, _orderItem)

        log.info('partyOrderService: ' + partyOrderService)  
        
        partyOrderService.dispatchOrders(_order)

        respond(_order)

    }

    def createOrderByDispatcher(){
        //_order.requested = "代为创建"
        //createOrderBySales();
        //updateOrderByDispatchConfirm()

    }

    def updateOrderBySales() {
        log.info('request.JSON: ' + request.JSON)
        def ins = request.JSON
        def _sales = Sales.get(ins['salesId'])
        def _hospital = Hospital.get(ins['hospital'])
        def _customer = Customer.get(ins['customerId'])
        def _warehouse = Warehouse.findByWarehouseNo(ins['warehouseNo'])

        //def order = new Order(orderNo:'dummy',status:'DRAFT', requested:'TRUE', sales:sales, customer:customer, warehouse:warehouse, deliveryType:dt, deliveryTime:dti)
        def _order =Order.findByOrderNo(ins['orderNo'])
        _order.properties=ins
        _order.hospital=_hospital
        _order.sales = _sales
        _order.customer = _customer
        _order.warehouse = _warehouse

        log.info('order: ' + _order.properties)
        def _item = Item.get(ins['itemId'])
        def _orderItem = OrderItem.findByOrder(_order)
        _orderItem.properties=ins

        log.info('order in controller: ' + _order)

        log.info('orderService: ' + orderService)        

        _order = orderService.createOrder(_order, _orderItem)

        log.info('partyOrderService: ' + partyOrderService)  
        
        partyOrderService.dispatchOrders(_order)

        respond(_order)
    }

    
    def createOrder() {
        log.info('request.JSON: ' + request.JSON)
        def ins = request.JSON
        def _sales = Sales.get(ins['salesId'])
        def _hospital = Hospital.get(ins['hospital'])
        def _customer = Customer.get(ins['customerId'])
        def _warehouse = Warehouse.findByWarehouseNo(ins['warehouseNo'])

        //def order = new Order(orderNo:'dummy',status:'DRAFT', requested:'TRUE', sales:sales, customer:customer, warehouse:warehouse, deliveryType:dt, deliveryTime:dti)
        def _order  = new Order(ins)
        _order.properties=ins
        _order.hospital=_hospital
        _order.sales = _sales
        _order.customer = _customer
        _order.warehouse = _warehouse

        log.info('order: ' + _order.properties)
        def _item = Item.get(ins['itemId'])
        def _orderItem=new OrderItem(price: ins['price'], qty: ins['qty'])
          _orderItem.order = _order
          _orderItem.item = _item
        log.info('order in controller: ' + _order)
		log.info('orderService: ' + orderService)        
        _order = orderService.createOrder(_order, _orderItem)
        respond(_order)
    }


    //when dispatcher click the confirm button
    def updateOrderByDispatchConfirm() {

        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])
        
        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        //no order item changed
        orderService.updateOrder(_order, null)  

        partyOrderService.dispatchOrders(_order)        

        respond(_order)

    }

    //when dispatcher click submit button with supplementary items
    def updateOrderByDispatchSubmit() {
        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])

        _order.properties = ins

        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        Customer customer=Customer.get(ins['customerId'])
        def accessoryList=ins['accessory']
        accessoryList.each{id->
            Item item=Item.get(id)
            def instance = new CustomerItem(customer: customer, item: item)
            instance.save(flush:true)
        }


        //todo: use orderService instead
        //no order item changed
 		orderService.updateOrder(_order, null)        

        partyOrderService.dispatchOrders(_order)
        respond(_order)
    }    

    //when dispatcher click submit button with warehouse information
    def updateOrderByDispatchAllocateWarehouse() {
        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])

        _order.properties = ins
        
        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        //no order item changed
        orderService.updateOrder(_order, null)

        partyOrderService.dispatchOrders(_order)        

        respond(_order)
    }    

    //when warehouse owner accept the warehouse information
    def updateOrderByWarehouse(){
        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])

        _order.properties = ins
        
        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        //no order item changed
        orderService.updateOrder(_order, null)

        partyOrderService.dispatchOrders(_order)        

        respond(_order)
    }

    //when warehouse owner assign the delivery
    def updateOrderByWarehouseDelivery(){
        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])

        _order.properties = ins
        
        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        //no order item changed
        orderService.updateOrder(_order, null)

        partyOrderService.dispatchOrders(_order)        

        respond(_order)
    }

    //when delivery accept the order
    def updateOrderByDeliveryConfirm(){
        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])

        _order.properties = ins
        
        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        //no order item changed
        orderService.updateOrder(_order, null)

        partyOrderService.dispatchOrders(_order)        

        respond(_order)
    }


    //when delivery ship the order
    def updateOrderByDeliveryShipped(){
        log.info('request.JSON: ' + request.JSON)

        def ins = request.JSON

        //todo: use order id only?
        def _order = Order.findByOrderNo(ins['orderNo'])

        _order.properties = ins
        
        log.info('order current status: ' + _order.status)

        //todo: constant OrderProcess
        def nextStatus = bizRuleService.getNextStatus('OrderProcess', _order.status)

        log.info('order next status: ' + nextStatus)

        _order.status = nextStatus

        //no order item changed
        orderService.updateOrder(_order, null)

        partyOrderService.dispatchOrders(_order)        

        respond(_order)
    }
    
    def fetchOrder() {
    	log.info('test controller invoke')
        def _order=Order.findByOrderNo(params.orderNo)
        def _orderItem= OrderItem.findByOrder(_order)


    	// respond [{test:'test message'}]
    	// render {message:'successfully'} as JSON
        render new JSON(hospital: _order.hospital,customer:_order.customer,warehouse:_order.warehouse,orderItem:_orderItem)

    }

    def myOrders() {

    	
    }
}
