package dtp.config


import grails.rest.*
import grails.converters.*
import dtp.config.*

class BizRuleController {
	static responseFormats = ['json', 'xml']
	
    def index() { }

    def findAllByProcess() {

    	log.info('params: ' + params['processName'])

    	def bizRule = BizRule.findByName(params['processName'])

    	log.info('bizRule: ' + bizRule)

    	respond BizRuleItem.findAllByBizRule(bizRule)

    }

    def findAllProcessSteps() {
    	log.info('params: ' + params['processName'])

    	def bizRule = BizRule.findByName(params['processName'])

    	log.info('bizRule: ' + bizRule)

    	 def searchClosure = {
            eq ('bizRule', bizRule)
            order('seq')
        }

		def result = BizRuleItem.createCriteria().list(searchClosure)
        result = result.collect{[
            seq:it.seq,
            status:it.toStatus
        ]}

    	render result as JSON
    }
}
