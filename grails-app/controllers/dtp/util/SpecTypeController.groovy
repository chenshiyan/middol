package dtp.util


import grails.rest.*
import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured

class SpecTypeController extends RestfulController {
    static responseFormats = ['json', 'xml']
    SpecTypeController() {
        super(dtp.util.SpecType)
    }

}
