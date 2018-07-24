package dtp.biz

import dtp.biz.Item
import dtp.biz.Sales
import dtp.sys.User
import grails.rest.*
import grails.converters.*

class ItemController extends RestfulController {
    static responseFormats = ['json', 'xml']
    ItemController() {
        super(Item)
    }


    def index(Integer max) {
        def searchClosure = {
            if(params.query&&params.query!=""){
                like(params.query,"%${params.queryParams}%")
            }
            if(params.name&&params.name!=""){
                like("itemName","%"+params.name+"%")
            }
            if(params.isMedicine!=""){
                eq("isMedicine",Boolean.valueOf(params.isMedicine).booleanValue())
            }
        }
        def items = Item.createCriteria();
        items = items.list([max:params.max,offset:params.offset ?: 0],searchClosure)
        def total= items.totalCount
        items = items.collect{[
            id:it.id,
            itemNo:it.itemNo,
            commonName:it.commonName,
            itemName:it.itemName,
            spec:it.spec,
            unit:it.unit,
            price:it.price,
            validityPeriod:it.validityPeriod,
            status:it.status,
            remark:it.remark,
            imageURL:it.imageURL,
            isMedicine : it.isMedicine,
            hasCode : it.hasCode,
            forItem : it.forItem,
            manufacturer:it.manufacturer
        ]}
        def responseData = [
            'results': items,
            'total':total
            ]
        render responseData as JSON
    }

    def queryItemBySales(){
        def ins = request.JSON
        def user
        Sales sales
        def queryParams=ins['queryParams']
        def query=ins['query']
        def isMedicine=ins['isMedicine']
        if(ins['username']){
            user=User.findByUsername(ins['username'])
            //search hospitals
            //TODO 根据username或者userid查出sales，通过sales查出hospital
            sales=Sales.findByUser(user)
        }else{
            sales=Sales.get(ins['salesId'])
        }
      def searchClosure = {
            if(queryParams&&queryParams!=""){
                like(query,"%${queryParams}%")
            }
            if(isMedicine!=""){
                eq("isMedicine",Boolean.valueOf(isMedicine).booleanValue())
            }
            eq("manufacturer",sales.manufacturer)
        }
        def items= Item.createCriteria().list(params,searchClosure)

        def total= items.totalCount
        items = items.collect{[
            id:it.id,
            itemNo:it.itemNo,
            commonName:it.commonName,
            itemName:it.itemName,
            spec:it.spec,
            unit:it.unit,
            price:it.price,
            validityPeriod:it.validityPeriod,
            status:it.status,
            remark:it.remark,
            imageURL:it.imageURL,
            isMedicine : it.isMedicine,
            hasCode : it.hasCode,
            forItem : it.forItem,
            manufacturer:it.manufacturer
        ]}
        def responseData = [
            'results': items,
            'total':total
            ]
        render responseData as JSON
    }

    def queryById(){
        // println "params ============= "+params
        respond resource.get(params.id)
    }

    def findAllForItemByItemId(){
        def item = Item.get(params.id)
        def items = Item.findAllByForItem(item)
        items = items.collect{[
            id:it.id,
            itemNo:it.itemNo,
            commonName:it.commonName,
            itemName:it.itemName,
            spec:it.spec,
            unit:it.unit,
            price:it.price,
            validityPeriod:it.validityPeriod,
            status:it.status,
            remark:it.remark,
            imageURL:it.imageURL,
            isMedicine : it.isMedicine,
            hasCode : it.hasCode,
            manufacturer:it.manufacturer
        ]}
        render new JSON(results: items)
    }

    def findAllForItemByItem(){
        def items = Item.findAllByForItem(request.JSON.item)
        items = items.collect{[
            id:it.id,
            itemNo:it.itemNo,
            commonName:it.commonName,
            itemName:it.itemName,
            spec:it.spec,
            unit:it.unit,
            price:it.price,
            validityPeriod:it.validityPeriod,
            status:it.status,
            remark:it.remark,
            imageURL:it.imageURL,
            isMedicine : it.isMedicine,
            hasCode : it.hasCode,
            manufacturer:it.manufacturer
        ]}
        render items as JSON


    }

}
