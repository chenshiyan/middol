package dtp

import dtp.biz.Customer
import dtp.biz.Group
import dtp.biz.Hospital
import dtp.biz.Item
import dtp.biz.Manufacturer
import dtp.biz.Sales
import dtp.biz.Warehouse
import dtp.util.*
import dtp.sys.*
import dtp.config.*
import dtp.wechat.PublicAccount

class BootStrap {

    def init = { servletContext ->

        populateUserRoles()

        populateRequestmaps()             

        populateEnums()
 
        populateWarehouses()

        populateManufacturers()

        populateItems()

        populategroups()

        populatehospitals()

        populatesales()

        populatecustomers()

        populateBizRules()

        populateSpecType()

        populateUnitType()

        publicAccount()

    }

    def publicAccount(){
        if (PublicAccount.count() == 0) {
            new PublicAccount(appid:"wx2bba60c334f6907d",appsecret:"9f99ddca73104a26710ce1a670f9750c",publicAccountName:"test",htmlTitle:"test",token:"test",accountType:"服务号").save(flush:true, failOnError:true)
        }
    }

    def populateEnums() {
        //enum data
        if(DeliveryType.count() == 0) {
            def dt = new DeliveryType(description:'DTP')
            dt.deliveryType = 'DTP'            
            dt.save(flush:true)             
        }

        if (DeliveryTime.count() == 0) {
            def dti1 = new DeliveryTime(description:'全天')
            dti1.deliveryTime = 'WHOLE'            
            dti1.save(flush:true)


            def dti2 = new DeliveryTime(description:'上午')
            dti2.deliveryTime = 'MORNING'            
            dti2.save(flush:true)

            def dti3 = new DeliveryTime(description:'下午')
            dti3.deliveryTime = 'AFTERNOON'            
            dti3.save(flush:true)            
        }


        if(PaymentType.count() == 0) {
            def pt = new PaymentType(description:'现金')
            pt.paymentType = 'CASH'            
            pt.save(flush:true)

            def pt2 = new PaymentType(description:'支付宝')
            pt2.paymentType = 'ALIPAY'            
            pt2.save(flush:true)

        }

    }

    def populateWarehouses() {

        //warehouse data
        if(!Warehouse.findByWarehouseNo('ZPK')){
            def zpk = new Warehouse(warehouseName:'周浦总库', address:'temp', tel:'xxx')
            zpk.warehouseNo = 'ZPK'            
            zpk.save(flush:true)              
        }

        if(!Warehouse.findByWarehouseNo('LJBK')){
            def ljbk = new Warehouse(warehouseName:'陆家浜路库', address:'temp', tel:'xxx')
            ljbk.warehouseNo = 'LJBK'            
            ljbk.save(flush:true)              
        } 

        if(!Warehouse.findByWarehouseNo('YLK')){
            def ylk = new Warehouse(warehouseName:'沂林库', address:'temp', tel:'xxx')
            ylk.warehouseNo = 'YLK'            
            ylk.save(flush:true)              
        }         


    }

    def populateUserRoles() {
            def adminRole = new Role('ROLE_ADMIN').save()
            def userRole = new Role('ROLE_USER').save()
            def salesRole = new Role('ROLE_SALES').save()
            def dispatcherRole = new Role('ROLE_DISPATCHER').save()
            def warehouseRole = new Role('ROLE_WAREHOUSE').save()
            def deliveryRole = new Role('ROLE_DELIVERY').save()

            def testUser = new User('me', 'password').save()
            def adminUser = new User('admin', 'admin').save()
            def salesUser1 = new User('s001', 's001').save()
            def salesUser2= new User('s002', 's002').save()
            def salesUser3 = new User('s003', 's003').save()
            def salesUser4 = new User('s004', 's004').save()
            def salesUser5 = new User('s005', 's005').save()
            def salesUser6 = new User('s006', 's006').save()
            def dispatcherUser = new User('d001', 'd001').save()
            def warehouseUser = new User('w001', 'w001').save()
            def deliveryUser = new User('p001', 'p001').save()

            UserRole.create testUser, userRole
            UserRole.create adminUser, adminRole
            UserRole.create salesUser1, salesRole
            UserRole.create salesUser2, salesRole
            UserRole.create salesUser3, salesRole
            UserRole.create salesUser4, salesRole
            UserRole.create salesUser5, salesRole
            UserRole.create salesUser6, salesRole
            UserRole.create dispatcherUser, dispatcherRole
            UserRole.create warehouseUser, warehouseRole
            UserRole.create deliveryUser, deliveryRole

            UserRole.withSession {
                it.flush()
                it.clear()
            }

    }

    def populateRequestmaps() {
        for (String url in [
        '/', '/error', '/index', '/index.gsp', '/**/favicon.ico', '/shutdown',
        '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**',
        '/login', '/login.*', '/login/*','/api/login/*',
        '/logout', '/logout.*', '/logout/*']) {
            new Requestmap(url: url, configAttribute: 'permitAll').save()
        }

        new Requestmap(url: '/api/**', configAttribute: 'ROLE_USER,ROLE_ADMIN,ROLE_SALES,ROLE_DISPATCHER,ROLE_WAREHOUSE,ROLE_DELIVERY', ).save()

        new Requestmap(url: '/api/logout',
            configAttribute: 'isAuthenticated()').save()        

        /*
        new Requestmap(url: '/profile/**', configAttribute: 'ROLE_USER').save()
        new Requestmap(url: '/admin/**', configAttribute: 'ROLE_ADMIN').save()
        new Requestmap(url: '/admin/role/**', configAttribute: 'ROLE_SUPERVISOR').save()
        new Requestmap(url: '/admin/user/**',
        configAttribute: 'ROLE_ADMIN,ROLE_SUPERVISOR').save()
        new Requestmap(url: '/login/impersonate',
        configAttribute: 'ROLE_SWITCH_USER,isFullyAuthenticated()').save()        
        */
    }

    def populateManufacturers() {
            //manufacturer data
            if(!Manufacturer.findByManufacturerNo('江苏恒瑞医药股份有限公司')) {
                new Manufacturer(manufacturerNo:'江苏恒瑞医药股份有限公司', manufacturerName:'江苏恒瑞医药股份有限公司').save(failOnError:true, flush:true)
            }

            if(!Manufacturer.findByManufacturerNo('丹麦诺和诺德公司')) {
                new Manufacturer(manufacturerNo:'丹麦诺和诺德公司', manufacturerName:'丹麦诺和诺德公司').save(failOnError:true, flush:true)
            }

            if(!Manufacturer.findByManufacturerNo('Patheon Inc.(加拿大）')) {
                new Manufacturer(manufacturerNo:'Patheon Inc.(加拿大）', manufacturerName:'Patheon Inc.(加拿大）').save(failOnError:true, flush:true)
            }        
    }

    def populateItems() {
        if(!Item.findByItemNo('9010215210690')) {
           def mn = Manufacturer.findByManufacturerNoLike('Patheon%')
          new Item(itemNo:'9010215210690', commonName:'波生坦片', itemName:'全可利', spec:'125mg*56片', unit:'盒',
        price:3996.00, manufacturer:mn,imageURL:'http://imgcdn.baiji.com.cn/images/201503/goods_img/12895_G_1425525131697.jpg',validityPeriod:'48', status:'1', remark:'').save(flush:true, failOnError:true)
        }

        if(!Item.findByItemNo('9040121230620')) {
           def mn = Manufacturer.findByManufacturerNoLike('丹麦诺和诺德公司%')
          new Item(itemNo:'9040121230620', commonName:'利拉鲁肽注射液', itemName:'诺和力', spec:'3ml：18mg(预填充注射笔)', unit:'盒',
        price:850.00, manufacturer:mn,imageURL:'http://imgcdn.baiji.com.cn/images/201503/goods_img/12895_G_1425525131697.jpg',validityPeriod:'30', status:'1', remark:'').save(flush:true, failOnError:true)
        }

        if(!Item.findByItemNo('9010209220520')) {
           def mn = Manufacturer.findByManufacturerNoLike('江苏恒瑞医药股份有限公司%')
          new Item(itemNo:'9010209220520', commonName:'甲磺酸阿帕替尼片', itemName:'艾坦', spec:'0.25g*10片', unit:'盒',
        price:2220.00, manufacturer:mn,imageURL:'http://imgcdn.baiji.com.cn/images/201503/goods_img/12895_G_1425525131697.jpg',validityPeriod:'24', status:'1', remark:'').save(flush:true, failOnError:true)
        }

        if(!Item.findByItemNo('FZ00001')) {
           def item = Item.findByItemName('艾坦')
          new Item(itemNo:'FZ00001', commonName:'NA', itemName:'冰', spec:'NA', unit:'NA',
        price:0, manufacturer:"",imageURL:'http://imgcdn.baiji.com.cn/images/201503/goods_img/12895_G_1425525131697.jpg',validityPeriod:'NA', status:'1',forItem:item,isMedicine:Boolean.FALSE, remark:'').save(flush:true, failOnError:true)
        }

        if(!Item.findByItemNo('FZ00002')) {
           def item = Item.findByItemName('诺和力')
          new Item(itemNo:'FZ00002', commonName:'NA', itemName:'资料', spec:'NA', unit:'NA',
        price:0, manufacturer:"",imageURL:'http://imgcdn.baiji.com.cn/images/201503/goods_img/12895_G_1425525131697.jpg',validityPeriod:'NA', status:'1',forItem:item,isMedicine:Boolean.FALSE, remark:'').save(flush:true, failOnError:true)
        }


    }
     def populategroups() {
        if(!Group.findByGroupNo('ZB100001')) {
            new Group(groupNo:'ZB100001', groupName:'一办', area:'上海', item:'艾坦', status:'1').save(flush:true, failOnError:true)
        }
        if(!Group.findByGroupNo('ZB100002')) {
            new Group(groupNo:'ZB100002', groupName:'六办', area:'上海', item:'艾坦', status:'1').save(flush:true, failOnError:true)
        }
        if(!Group.findByGroupNo('ZB200001')) {
            new Group(groupNo:'ZB200001', groupName:'混合组-04', area:'上海', item:'诺和力', status:'1').save(flush:true, failOnError:true)
        }
        if(!Group.findByGroupNo('ZB200002')) {
            new Group(groupNo:'ZB200002', groupName:'混合组-05', area:'上海', item:'诺和力', status:'1').save(flush:true, failOnError:true)
        }
         if(!Group.findByGroupNo('ZB300001')) {
            new Group(groupNo:'ZB300001', groupName:'一组', area:'上海', item:'全可利', status:'1').save(flush:true, failOnError:true)
        }
    }

    def populatehospitals() {
        if(!Hospital.findByHospitalNo('YY1000001')) {
            new Hospital(hospitalNo:'YY1000001', hospitalName:'东方肝胆医院', address:'上海市杨浦区长海路225号', remark:'').save(flush:true, failOnError:true)
        }
        if(!Hospital.findByHospitalNo('YY1000002')) {
            new Hospital(hospitalNo:'YY1000002', hospitalName:'东肝安亭', address:'墨玉北路700号', remark:'').save(flush:true, failOnError:true)
        }
        if(!Hospital.findByHospitalNo('YY1000003')) {
            new Hospital(hospitalNo:'YY1000003', hospitalName:'肺科医院', address:'上海市杨浦区政民路507号', remark:'').save(flush:true, failOnError:true)
        }
         if(!Hospital.findByHospitalNo('YY1000004')) {
            new Hospital(hospitalNo:'YY1000004', hospitalName:'肿瘤医院', address:'上海市徐汇区东安路270号', remark:'').save(flush:true, failOnError:true)
        }
         if(!Hospital.findByHospitalNo('YY1000005')) {
            new Hospital(hospitalNo:'YY1000005', hospitalName:'上海市第七人民医院', address:'大同路358号', remark:'').save(flush:true, failOnError:true)
        }
         if(!Hospital.findByHospitalNo('YY1000006')) {
            new Hospital(hospitalNo:'YY1000006', hospitalName:'上海市公安消防总队职工医院', address:'上海市虹漕路13号', remark:'').save(flush:true, failOnError:true)
        }
         if(!Hospital.findByHospitalNo('YY1000007')) {
            new Hospital(hospitalNo:'YY1000007', hospitalName:'仁济医院', address:'东方路1630号', remark:'').save(flush:true, failOnError:true)
        }
         if(!Hospital.findByHospitalNo('YY1000008')) {
            new Hospital(hospitalNo:'YY1000008', hospitalName:'东方医院', address:'上海市浦东新区即墨路150号', remark:'').save(flush:true, failOnError:true)
        }
          if(!Hospital.findByHospitalNo('YY1000009')) {
            new Hospital(hospitalNo:'YY1000009', hospitalName:'上海儿童医学中心', address:'上海市东方路1678号', remark:'').save(flush:true, failOnError:true)
        }
        if(!Hospital.findByHospitalNo('YY1000010')) {
            new Hospital(hospitalNo:'YY1000010', hospitalName:'市六医院', address:'徐汇区宜山路600号', remark:'').save(flush:true, failOnError:true)
        }
        if(!Hospital.findByHospitalNo('YY1000011')) {
            new Hospital(hospitalNo:'YY1000011', hospitalName:'市一医院', address:'上海市虹口区海宁路100号', remark:'').save(flush:true, failOnError:true)
        }
        if(!Hospital.findByHospitalNo('YY1000012')) {
            new Hospital(hospitalNo:'YY1000012', hospitalName:'岳阳医院', address:'上海市虹口区甘河路110号', remark:'').save(flush:true, failOnError:true)
        }
    }

     def populatesales() {
        def salesInstance
        if(!Sales.findBySalesNo('ZY100001')) {
            def mn = Manufacturer.findByManufacturerNoLike('江苏恒瑞医药股份有限公司%')
            def hospital= Hospital.findByHospitalNameLike('东方肝胆医院%')
            def user = User.findByUsername("s001")
            salesInstance = new Sales(salesNo:'ZY100001', salesName:'陈巧', tel:'13817483606', groupName:'艾坦-上海-一办',hospitals:[hospital.id], manufacturer:mn,user:user, remark:'').save(flush:true, failOnError:true)
        }
        if(!Sales.findBySalesNo('ZY100002')) {
            def mn = Manufacturer.findByManufacturerNoLike('江苏恒瑞医药股份有限公司%')
            def hospital1= Hospital.findByHospitalNameLike('东方肝胆医院%')
            def hospital2= Hospital.findByHospitalNameLike('东肝安亭%')
            def hospital3= Hospital.findByHospitalNameLike('肺科医院%')
            def user = User.findByUsername("s002")
            salesInstance = new Sales(salesNo:'ZY100002', salesName:'吉敏', tel:'13501834320', groupName:'艾坦-上海-一办',hospitals:[hospital1.id,hospital2.id,hospital3.id], manufacturer:mn,user:user, remark:'').save(flush:true, failOnError:true)
        }
        if(!Sales.findBySalesNo('ZY100003')) {
           def mn = Manufacturer.findByManufacturerNoLike('江苏恒瑞医药股份有限公司%')
            def hospital= Hospital.findByHospitalNameLike('肿瘤医院%')
            def user = User.findByUsername("s003")
            salesInstance = new Sales(salesNo:'ZY100003', salesName:'冯辉', tel:'18360692722', groupName:'艾坦-上海-六办',hospitals:[hospital.id], manufacturer:mn,user:user, remark:'').save(flush:true, failOnError:true)
        }
        if(!Sales.findBySalesNo('ZY200001')) {
            def mn = Manufacturer.findByManufacturerNoLike('丹麦诺和诺德公司%')
            def hospital= Hospital.findByHospitalNameLike('上海市第七人民医院%')
            def user = User.findByUsername("s004")
            salesInstance = new Sales(salesNo:'ZY200001', salesName:'沈国平', tel:'18917611102', groupName:'诺和力-上海-混合组-04',hospitals:[hospital.id], manufacturer:mn,user:user, remark:'').save(flush:true, failOnError:true)
        }
         if(!Sales.findBySalesNo('ZY200002')) {
            def mn = Manufacturer.findByManufacturerNoLike('丹麦诺和诺德公司%')
            def hospital= Hospital.findByHospitalNameLike('上海市公安消防总队职工医院%')
            def user = User.findByUsername("s005")
            salesInstance = new Sales(salesNo:'ZY200002', salesName:'邓继武', tel:'13641839872', groupName:'诺和力-上海-混合组-05',hospitals:[hospital.id], manufacturer:mn,user:user, remark:'').save(flush:true, failOnError:true)
        }
         if(!Sales.findBySalesNo('ZY300001')) {
            def mn = Manufacturer.findByManufacturerNoLike('Patheon Inc.(加拿大）%')
            def hospital1= Hospital.findByHospitalNameLike('仁济医院%')
            def hospital2= Hospital.findByHospitalNameLike('东方医院%')
            def hospital3= Hospital.findByHospitalNameLike('上海儿童医学中心%')
            def hospital4= Hospital.findByHospitalNameLike('市六医院%')
            def hospital5= Hospital.findByHospitalNameLike('市一医院%')
            def hospital6= Hospital.findByHospitalNameLike('岳阳医院%')
            def user = User.findByUsername("s006")
            salesInstance = new Sales(salesNo:'ZY300001', salesName:'倪雪峰', tel:'13301730522', groupName:'全可利-上海-一组',hospitals:[hospital1.id,hospital2.id,hospital3.id,hospital4.id,hospital5.id,hospital6.id], manufacturer:mn, remark:'').save(flush:true, failOnError:true)
        }

    }
     def populatecustomers() {
        if(!Customer.findByCustomerNo('HZ100001')) {
            def sales = Sales.findBySalesNameLike('吉敏%')
            new Customer(customerNo:'HZ100001', customerName:'患者1', sales:sales, sex:"1",birth:new Date(), tel:18888888888, address:'上海市长宁区金钟路633号',contact:'张三',status:'1').save(flush:true, failOnError:true)
        }
         if(!Customer.findByCustomerNo('HZ100002')) {
            def sales = Sales.findBySalesNameLike('陈巧%')
            new Customer(customerNo:'HZ100002', customerName:'患者2', sales:sales, sex:"1",birth:new Date(), tel:18888888888, address:'上海市长宁区金钟路633号',contact:'张三',status:'1').save(flush:true, failOnError:true)
        }
        if(!Customer.findByCustomerNo('HZ100003')) {
            def sales = Sales.findBySalesNameLike('倪雪峰%')
            new Customer(customerNo:'HZ100003', customerName:'患者3', sales:sales, sex:"1",birth:new Date(), tel:18888888888, address:'上海市长宁区金钟路633号',contact:'张三',status:'1').save(flush:true, failOnError:true)
        }
         if(!Customer.findByCustomerNo('HZ100004')) {
            def sales = Sales.findBySalesNameLike('吉敏%')
            new Customer(customerNo:'HZ100004', customerName:'患者4', sales:sales, sex:"0",birth:new Date(), tel:18888888888, address:'上海市长宁区金钟路633号',contact:'张三',status:'1').save(flush:true, failOnError:true)
        }
          if(!Customer.findByCustomerNo('HZ100005')) {
            def sales = Sales.findBySalesNameLike('沈国平%')
            new Customer(customerNo:'HZ100005', customerName:'患者5', sales:sales, sex:"0",birth:new Date(), tel:18888888888, address:'上海市长宁区金钟路633号',contact:'张三',status:'1').save(flush:true, failOnError:true)
        }
          if(!Customer.findByCustomerNo('HZ100006')) {
            def sales = Sales.findBySalesNameLike('吉敏%')
            new Customer(customerNo:'HZ100006', customerName:'患者6', sales:sales, sex:"0",birth:new Date(), tel:18888888888, address:'上海市长宁区金钟路633号',contact:'张三',status:'1').save(flush:true, failOnError:true)
        }

    }

    def populateBizRules(){

        if (BizRule.count() == 0) {
            def fsm = new BizRule(name:'OrderProcess').save()
            //def fsm = BizRule.findByName('OrderProcess')

            new BizRuleItem(bizRule:fsm, seq:1, event:'sales-submit', fromStatus:'INITIAL', toStatus:'DRAFT', viewUrl:'SalesOrder',editUrl:'SalesOrder',editDisable:'', actionUrl:'ConfirmOrder', participants:'ROLE_DISPATCHER').save()
            new BizRuleItem(bizRule:fsm, seq:2, event:'confirm', fromStatus:'DRAFT', toStatus:'RECEIVED',viewUrl:'ConfirmOrder',editUrl:'ConfirmOrder',editDisable:'', actionUrl:'DispatcherOrder', participants:'ROLE_DISPATCHER').save()
            new BizRuleItem(bizRule:fsm, seq:3, event:'edit-order', fromStatus:'RECEIVED', toStatus:'DISPATCHING' , viewUrl:'DispatcherOrder',editUrl:'DispatcherOrder',editDisable:'', actionUrl:'DispatchedOrder', participants:'ROLE_DISPATCHER').save()
            new BizRuleItem(bizRule:fsm, seq:4, event:'dispatch', fromStatus:'DISPATCHING', toStatus:'DISPATCHED', viewUrl:'DispatchedOrder',editUrl:'DispatchedOrder',editDisable:'', actionUrl:'WarehouseOrder', participants:'ROLE_WAREHOUSE').save()
            new BizRuleItem(bizRule:fsm, seq:5, event:'allocate', fromStatus:'DISPATCHED', toStatus:'ALLOCATING', viewUrl:'WarehouseOrder',editUrl:'WarehouseOrder',editDisable:'ROLE_SALES', actionUrl:'AllocatchedOrder', participants:'ROLE_WAREHOUSE').save()
            new BizRuleItem(bizRule:fsm, seq:6, event:'assign', fromStatus:'ALLOCATING', toStatus:'ALLOCATED', viewUrl:'AllocatchedOrder',editUrl:'AllocatchedOrder',editDisable:'ROLE_SALES', actionUrl:'DeliveryOrder', participants:'ROLE_DELIVERY').save()
            new BizRuleItem(bizRule:fsm, seq:7, event:'ship-confirm', fromStatus:'ALLOCATED', toStatus:'SHIPPING', viewUrl:'DeliveryOrder',editUrl:'DeliveryOrder',editDisable:'ROLE_SALES,ROLE_WAREHOUSE', actionUrl:'ShippedOrder', participants:'ROLE_DELIVERY').save()
            new BizRuleItem(bizRule:fsm, seq:8, event:'ship', fromStatus:'SHIPPING', toStatus:'SHIPPED', viewUrl:'ShippedOrder',editUrl:'ShippedOrder',editDisable:'ROLE_SALES,ROLE_WAREHOUSE,ROLE_DELIVERY', actionUrl:'null', participants:'NA').save()
            
        }
    }
    def populateSpecType(){
        if (SpecType.count() == 0) {
            new SpecType(spec:"125mg*56片",description:"125mg*56片").save(flush:true, failOnError:true)
            new SpecType(spec:"3ml：18mg(预填充注射笔)",description:"3ml：18mg(预填充注射笔)").save(flush:true, failOnError:true)
            new SpecType(spec:"3ml：18mg",description:"3ml：18mg").save(flush:true, failOnError:true)
            new SpecType(spec:"0.25g*10片",description:"0.25g*10片").save(flush:true, failOnError:true)
            new SpecType(spec:"0.25g*60片",description:"0.25g*60片").save(flush:true, failOnError:true)
        }

    }

    def populateUnitType(){
        if (UnitType.count() == 0) {
            new UnitType(unit:"盒",description:"盒").save(flush:true, failOnError:true)
            new UnitType(unit:"支",description:"支").save(flush:true, failOnError:true)
            new UnitType(unit:"罐",description:"罐").save(flush:true, failOnError:true)
            new UnitType(unit:"片",description:"片").save(flush:true, failOnError:true)
        }
    }

    def destroy = {
    }
}
