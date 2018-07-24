package dtp.config

import grails.transaction.Transactional
import dtp.config.*

@Transactional
class BizRuleService {

    def getNextStatus(String processName, String fromStatus) {

    	//todo:change to enum; 
    	//default value if no further step found
    	def nextStatus = 'DONE'

    	def bizRule = BizRule.findByName(processName)

    	log.info('bizRule: ' + bizRule)

    	def currentStep = BizRuleItem.findByBizRuleAndFromStatus(bizRule, fromStatus)

		// log.info('bizRule step: ' + currentStep.seq)    	

		// def seq = currentStep.seq + 1

		// log.info('bizRule next step: ' + seq)    	

		// def nextStep = BizRuleItem.findBySeq(seq)

		// if (nextStep) {
		// 	nextStatus = nextStep.toStatus
		// }

		nextStatus = currentStep.toStatus

		log.info('next status: '+ nextStatus)

		return nextStatus
    }

    def getCurrentStepInfo(String processName, String toStatus){

    	def bizRule = BizRule.findByName(processName)

    	log.info('bizRule: ' + bizRule)

    	def currentStepInfo = BizRuleItem.findByBizRuleAndToStatus(bizRule, toStatus)

		log.info('bizRule currentStepInfo: ' + currentStepInfo)  

		return currentStepInfo
    }
}
