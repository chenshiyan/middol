package dtp

class UrlMappings {

    static mappings = {
        delete "/api/$controller/$id(.$format)?"(action:"delete")
        get "/api/$controller(.$format)?"(action:"index")
        get "/api/$controller/$id(.$format)?"(action:"show")
        post "/api/$controller(.$format)?"(action:"save")
        put "/api/$controller/$id(.$format)?"(action:"update")
        patch "/api/$controller/$id(.$format)?"(action:"patch")

        "/api/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }        

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')

        "/api/items"(resources:'item')
        '/api/items/queryById'(controller: 'item', action: 'queryById')   
        "/api/customer"(resources:'customer')
        "/api/hospital"(resources:'hospital')
        "/api/sales"(resources:'sales')
        "/api/user"(resources:'user') 
        '/api/user/changePassword'(controller: 'user', action: 'changePassword') 
        '/api/user/findAllUsersByRoleName'(controller: 'user', action: 'findAllUsersByRoleName')          
        "/api/Warehouse"(resources:'Warehouse') 
        "/api/group"(resources:'Group') 
        "/api/branchWarehouse"(resources:'branchWarehouse') 
                     
        '/api/getBranchWarehouseByParams'(controller: 'branchWarehouse', action: 'getBranchWarehouseByParams')

        '/api/createOrder'(controller: 'order', action: 'createOrder')
        '/api/orderOptionInit'(controller: 'order', action: 'optionInit')
        '/api/salesOrderOptionInit'(controller: 'order', action: 'salesOrderOptionInit')
        '/api/createShipOrder'(controller: 'shipOrder', action: 'createShipOrder')             
        '/api/queryShipOrder'(controller: 'shipOrder', action: 'queryShipOrder')             
        '/api/queryShipOrderDetailById'(controller: 'shipOrder', action: 'queryShipOrderDetailById') 

        '/api/listSalesHospitalById'(controller: 'sales', action: 'listSalesHospitalById') 
        '/api/listSalesHospitalByUserName'(controller: 'sales', action: 'listSalesHospitalByUserName') 

        '/api/findAllForItemByItemId'(controller: 'item', action: 'findAllForItemByItemId')             
    }
}
