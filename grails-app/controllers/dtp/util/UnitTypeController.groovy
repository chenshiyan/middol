package dtp.util


import grails.rest.*
import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured

class UnitTypeController extends RestfulController {
    static responseFormats = ['json', 'xml']
    UnitTypeController() {
        super(dtp.util.UnitType)
    }

}
