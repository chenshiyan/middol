package dtp.biz

import dtp.biz.Manufacturer
import grails.rest.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class ManufacturerController extends RestfulController{
	static responseFormats = ['json', 'xml']

    ManufacturerController() {
        super(Manufacturer)
    }

    @Override
    index(Integer max) {
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
             result= resource.findAllByManufacturerNameIlike("%"+params.name+"%")
        }
        respond result,[metadata: [total: countResources(), max: params.max, offset: params.offset?:0]]
    }
}
