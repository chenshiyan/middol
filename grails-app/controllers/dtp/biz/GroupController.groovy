package dtp.biz

import dtp.biz.Group
import grails.rest.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class GroupController extends RestfulController {
    static responseFormats = ['json', 'xml']
    GroupController() {
        super(Group)
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
        }else if(params.name){
    
            result= resource.findAllByGroupNameOrAreaOrItemIlike("%"+params.name+"%","%"+params.name+"%","%"+params.name+"%")
        }else{
            result=resource.list(params)
        }
             respond result,[metadata: [total: countResources(), max: params.max, offset: params.offset ?: 0]]

    }
}
