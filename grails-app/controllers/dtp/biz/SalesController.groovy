package dtp.biz

import dtp.biz.Hospital
import dtp.biz.Manufacturer
import dtp.biz.Sales
import grails.rest.*
import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured


@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class SalesController extends RestfulController {
    static responseFormats = ['json', 'xml']
    SalesController() {
        super(Sales)
    }

    @Override
      def index(Integer max) {
        def searchClosure = {
            if(params.query&&params.query!=""){
                like(params.query,"%${params.queryParams}%")
            }
             if(params.name&&params.name!=""){
                like("salesName","%"+params.name+"%")
            }
             if(params.salesNo&&params.salesNo!=""){
                like("salesNo","%"+params.salesNo+"%")
            }
            def user = dtp.sys.User.findByUsername(params.username);
            if(params.username&&params.username!=""){
                eq("user",user)
            }
            if(params.id&&params.id!=""){
                eq("id",params.id.toLong())
            }
        }
        def sales = Sales.createCriteria();
        sales = sales.list([max:params.max,offset:params.offset ?: 0],searchClosure)
        def total= sales.totalCount
        sales = sales.collect{[
            id:it.id,
            salesNo:it.salesNo,
            salesName:it.salesName,
            tel:it.tel,
            groupName:it.groupName,
            manufacturer:it.manufacturer,
            user:it.user,
            // status:it.status,
            remark:it.remark,
            hospitals: it.hospitals.hospitalName
        ]
    }
        def responseData = [
            'results': sales,
            'total':total
            ]
        render responseData as JSON
    }
   def listSalesHospitalById(){
    def sales  = Sales.get(request.JSON.id.toLong());
    render sales.hospitals as JSON
   }

     def listSalesHospitalByUserName(){
    def user = dtp.sys.User.findByUsername(request.JSON.username);
    def sales  = Sales.findByUser(user);
    render sales.hospitals as JSON
   }
  @Override
   def save() {
        def salesInstance = Sales.findBySalesNo(request.JSON.salesNo)
        if(salesInstance){
            render "error"
        }else{
            salesInstance = new Sales()
            salesInstance.salesNo = request.JSON.salesNo
            salesInstance.salesName = request.JSON.salesName
            salesInstance.tel = request.JSON.tel
            salesInstance.groupName = request.JSON.groupName
            salesInstance.manufacturer = Manufacturer.get(request.JSON.manufacturer)
            salesInstance.remark = request.JSON.remark
            // salesInstance.hospitals = request.JSON.hospitals;
            
            for(hospitalId in request.JSON.hospitals){
                salesInstance.addToHospitals(Hospital.get(hospitalId))
            }

            
            def username = PinYinMaUtil.stringToPinyin(request.JSON.salesName,false)
            def userInstance 
            def avalibleName = true
            def i = 1
            while(avalibleName){
                userInstance = dtp.sys.User.findByUsername(username)
                if(userInstance){
                username=request.JSON.salesName+i
                i++
                }else{
                    def pw =String.valueOf(request.JSON.tel)
                    if(pw.length()>4){
                       pw =pw.substring(pw.length()-4,pw.length())
                    }
                    userInstance =  new dtp.sys.User(username,request.JSON.salesName+pw).save(flush:true,failOnError:true)
                    dtp.sys.UserRole.create userInstance,dtp.sys.Role.findByAuthority("ROLE_SALES")
                    avalibleName =false
                }
            }
            salesInstance.user = userInstance
            salesInstance.save(flush:true,failOnError:true)
            render username
        }
    }
}
