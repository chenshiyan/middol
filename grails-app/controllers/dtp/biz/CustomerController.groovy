package dtp.biz

import dtp.biz.Customer
import dtp.biz.Sales
import dtp.sys.User
import grails.rest.*
import grails.converters.*

class CustomerController extends RestfulController {
    static responseFormats = ['json', 'xml']
    CustomerController() {
        super(Customer)
    }


   @Override
    def index(Integer max) {
        def searchClosure = {
            if(params.query&&params.query!=""){
                like(params.query,"%${params.queryParams}%")
            }
             if(params.name&&params.name!=""){
                like("customerName","%"+params.name+"%")
            }
        }
        def customers = Customer.createCriteria();
        customers = customers.list([max:params.max,offset:params.offset ?: 0],searchClosure)
        def total= customers.totalCount
        customers = customers.collect{[
            id:it.id,
            customerNo:it.customerNo,
            customerName:it.customerName,
            sales:it.sales,
            contact:it.contact,
            sex:it.sex,
            birth:it.birth.format("yyyy-MM-dd"),
            tel:it.tel,
            address:it.address,
            status:it.status,
            times:it.times,
            dateStarted:it.dateStarted
        ]}
        def responseData = [
            'results': customers,
            'total':total
            ]
        render responseData as JSON
    }

    def queryCustomerBySales(){
        def ins = request.JSON
        def user
        Sales sales
        def queryParams=ins['queryParams']
        def query=ins['query']
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
            eq("sales",sales)
        }
        def customers= Customer.createCriteria().list(params,searchClosure)

        def total= customers.totalCount
        customers = customers.collect{[
            id:it.id,
            customerNo:it.customerNo,
            customerName:it.customerName,
            sales:it.sales,
            contact:it.contact,
            sex:it.sex,
            birth:it.birth.format("yyyy-MM-dd"),
            tel:it.tel,
            idNo:it.idNo,
            address:it.address,
            status:it.status,
            times:it.times,
            dateStarted:it.dateStarted
        ]}
        def responseData = [
            'results': customers,
            'total':total
            ]
        render responseData as JSON
    }

}
