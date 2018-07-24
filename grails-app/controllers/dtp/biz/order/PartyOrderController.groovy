package dtp.biz.order

import dtp.biz.order.Order
import dtp.biz.order.OrderItem
import dtp.biz.order.PartyOrder
import grails.converters.*

class PartyOrderController {

	static responseFormats = ['json', 'xml']

	def partyOrderService
	
    def index(Integer max) {
        def searchClosure = {
            if(params.query&&params.query!=""){
                like(params.query,"%${params.queryParams}%")
            }
            if(params.username&&params.username!=""){
                or{
                    eq("createBy",params.username)
                    eq("username",params.username)
                }
            }
            if(params.status&&params.status!=""){
                eq("status",params.status)
            }
            order("id", "desc")
        }
        def partyOrders = PartyOrder.createCriteria();
        partyOrders = partyOrders.list([max:params.max,offset:params.offset ?: 0],searchClosure)
        def total= partyOrders.totalCount
        partyOrders = partyOrders.collect{[
            id:it.id,
            order:Order.findByOrderNo(it.orderNo),
            items:OrderItem.findAllByOrder(Order.findByOrderNo(it.orderNo)).collect{
                [
                id:it.id,
                itemId:it.item.id,
                itemName:it.item.itemName,
                imageURL:it.item.imageURL,
                item:it.item,
                price:it.price,
                qty:it.qty
            ]},
            status:it.status,
            viewUrl:it.viewUrl,
            editUrl:it.editUrl,
            actionUrl:it.actionUrl
        ]}
        def responseData = [
            'results': partyOrders,
            'total':total
            ]
        JSON.use('deep'){
        render responseData as JSON
        }
    }

    def testPartyOrderService(){

    	def order = Order.get(1)

    	log.info('order: ' + order)

    	partyOrderService.dispatchOrders(order)

    	render order as JSON

    }

    def findAllMyOrdersByUsername() {
    	def username = params['username']

    	render PartyOrder.findAllByUsername(username) as JSON
    }

    def findAllMyOrders(String username) {

    }

}
