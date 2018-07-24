package dtp.biz

import dtp.biz.BranchWarehouse
import grails.rest.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class BranchWarehouseController extends RestfulController {
    static responseFormats = ['json', 'xml']
    BranchWarehouseController() {
        super(BranchWarehouse)
    }

    @Override
	def index(Integer max) {
	    params.max = Math.min(max ?: 10, 100)
	    // We pass which fields to be rendered with the includes attributes,
	    // we exclude the class property for all responses. ***when includes are defined excludes are ignored.
	    //params.fetch = [recordTypeRs:"eager"] from params.fields???
	    def result;
        if(params.query){
            result=resource.createCriteria().list(params){
                ilike(params.query,"%${params.queryParams}%")
            }
        }else{
            result=resource.list(params)
        }
        respond result,[metadata: [total: countResources(), max: params.max, offset: params.offset?:0]]
	}

	@Transactional
    def getBranchWarehouseByParams(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        // println 'params ============= : ' + params
        // println 'length= ============= : ' + resource.countByBranchWarehouseNoIlikeOrBranchWarehouseNameIlike("%"+params.name+"%","%"+params.name+"%")

        def _total = resource.countByBranchWarehouseNoIlikeOrBranchWarehouseNameIlike("%"+params.name+"%","%"+params.name+"%")
        respond resource.findAllByBranchWarehouseNoIlikeOrBranchWarehouseNameIlike("%"+params.name+"%","%"+params.name+"%",
        	[max: params.max, offset: params.offset ?: 0]),
                [metadata: [total: _total, max: params.max, offset: params.offset ?: 0]]

    }
}
