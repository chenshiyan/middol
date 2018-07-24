package dtp.biz

import dtp.biz.Hospital
import dtp.biz.Sales
import dtp.sys.User
import grails.rest.*
import grails.converters.*

class HospitalController extends RestfulController {
    static responseFormats = ['json', 'xml']
    HospitalController() {
        super(Hospital)
    }

    @Override
    def index(Integer max) {
        def searchClosure = {
            if(params.query&&params.query!=""){
                like(params.query,"%${params.queryParams}%")
            }
             if(params.name&&params.name!=""){
                like("hospitalName","%"+params.name+"%")
            }
        }
        def hospital = Hospital.createCriteria();
        hospital = hospital.list([max:params.max,offset:params.offset ?: 0],searchClosure)
        def total= hospital.totalCount
        hospital = hospital.collect{[
            id:it.id,
            hospitalNo:it.hospitalNo,
            hospitalName:it.hospitalName,
            address:it.address,
            remark:it.remark
        ]}
        def responseData = [
            'results': hospital,
            'total':total
            ]
        render responseData as JSON

    }
    def queryHosptialBySales(){
        def ins = request.JSON
        def user
        Sales sales
        if(ins['username']){
            user=User.findByUsername(ins['username'])
            //search hospitals
            //TODO 根据username或者userid查出sales，通过sales查出hospital
            sales=Sales.findByUser(user)
        }else{
            sales=Sales.get(ins['salesId'])
        }
        def hospital
        if(!sales){
            //TODO find no rows exception
        }else{
           hospital=sales.hospitals
        }
        def total= hospital.size()
        hospital = hospital.collect{[
            id:it.id,
            hospitalNo:it.hospitalNo,
            hospitalName:it.hospitalName,
            address:it.address,
            remark:it.remark
        ]}
        def responseData = [
            'results': hospital,
            'total':total
            ]
        render responseData as JSON

    }

}
