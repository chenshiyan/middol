package dtp.biz.order

import dtp.biz.order.OrderHistory
import dtp.biz.order.OrderItemHistory
import grails.converters.*

class OrderHistoryController {
    static responseFormats = ['json', 'xml']

    def orderService

    def index(Integer max) {
        def searchClosure = {
            if(params.orderNo&&params.orderNo!=""){
               eq("orderNo",params.orderNo)
            }
            order("id", "asc")
        }
        def orderHistorys = OrderHistory.createCriteria().list(searchClosure)
        orderHistorys = orderHistorys.collect{[
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
            lastUpdatedTime:it.lastUpdated.format("HH:mm"),
            createdBy:it.createdBy,
            lastUpdatedBy:it.lastUpdatedBy,

            items:OrderItemHistory.findAllByOrderHistory(it).collect{[
                id:it.id,
                item:it.item,
                price:it.price,
                qty:it.qty
            ]}
        ]}
        render orderHistorys as JSON
    }

}
