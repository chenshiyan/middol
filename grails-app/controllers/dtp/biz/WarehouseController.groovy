package dtp.biz

import dtp.biz.Warehouse
import grails.rest.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class WarehouseController extends RestfulController {
    static responseFormats = ['json', 'xml']
    WarehouseController() {
        super(Warehouse)
    }
    
    @Override
	def index(Integer max) {
	    params.max = Math.min(max ?: 10, 100)
	    // We pass which fields to be rendered with the includes attributes,
	    // we exclude the class property for all responses. ***when includes are defined excludes are ignored.
	    //params.fetch = [recordTypeRs:"eager"] from params.fields???
	    println resource.list(params)
	    respond resource.list(params),
	            [metadata: [total: countResources(), max: params.max, offset: params.offset?:0]]
	}
}
