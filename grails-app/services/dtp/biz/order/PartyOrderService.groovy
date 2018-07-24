package dtp.biz.order

import dtp.biz.Sales
import dtp.biz.order.Order
import dtp.biz.order.PartyOrder
import grails.transaction.Transactional

@Transactional
class PartyOrderService {

	def bizRuleService

	def userService

    def dispatchOrders(Order order) {

    	//todo: add order process as constants
    	def currentStepInfo = bizRuleService.getCurrentStepInfo('OrderProcess', order.status)

    	if (currentStepInfo) {
    		log.info('get currentStepInfo: ' + currentStepInfo.properties)	
    	}


        def username = Sales.get(order.sales.id.toLong()).user.username
    	//rule 1 : all creator can view or update his/her order
		def result = PartyOrder.createCriteria().list {
		    projections {
		        property('orderNo')
		        property('username')
		    }
		    eq ('orderType', order.orderType.getId())
		    eq ('orderNo', order.orderNo)
		    eq ('username', username)
		}

		log.info('bizRuleService : ' + bizRuleService)

		log.info('party orders: ' + result)

		if (!result) {
			log.info('order: ' + order.properties)			

			new PartyOrder(
				orderType: order.orderType.getId(), orderNo:order.orderNo, createBy : order.createdBy,
                username: username, status:order.status, editUrl:currentStepInfo.editUrl,
				viewUrl:currentStepInfo.viewUrl, actionUrl:currentStepInfo.actionUrl
			).save(failOnError:true)
		}


    	//rule 2 : generate for the participants
    	log.info('currentStepInfo participants: ' + currentStepInfo.participants)

        def hsql = """update PartyOrder set status = '${order.status}', editUrl='${currentStepInfo.editUrl}',
                viewUrl='${currentStepInfo.viewUrl}', actionUrl='null' 
                where orderNo='${order.orderNo}' """

        PartyOrder.executeUpdate(hsql)

    	def participantList = []

    	if (currentStepInfo.participants) {
    		participantList = currentStepInfo.participants.split(',')	
    	}
    	
    	def usernames = []

    	participantList.each {
    		log.info('participant： ' + it)

    		//non variable evaluation
    		if (!it.contains('#')) {
    			def resolvedParticipants = userService.getResolvedUsernames(it.trim())		
    			//in future it may support multiple users
    			resolvedParticipants.each{
					usernames.add(it)  			    				
    			}
    		} else {

    		}

    	}
        
    	log.info('resolved usernames: ' + usernames)

    	usernames.each {
    		
    		log.info('party username: ' + it.username)

    		def partyOrder = PartyOrder.findByOrderNoAndUsername(order.orderNo, it.username)

			log.info('partyOrder: ' + partyOrder)

    		if (partyOrder) {
				partyOrder.status = order.status
				partyOrder.viewUrl = currentStepInfo.viewUrl
                partyOrder.editUrl = currentStepInfo.editUrl
				partyOrder.actionUrl = currentStepInfo.actionUrl
				partyOrder.save(failOnError:true)
    		} else {
				new PartyOrder(
					orderType: order.orderType.getId(), orderNo:order.orderNo, createBy : "null",
                    username:it.username, status:order.status, editUrl:currentStepInfo.editUrl,
					viewUrl:currentStepInfo.viewUrl, actionUrl:currentStepInfo.actionUrl
				).save(failOnError:true)      	    		    			
    		}
    	}

        def editDisableList = []

        if (currentStepInfo.editDisable) {
            editDisableList = currentStepInfo.editDisable.split(',')   
        }
        def editUsername = []
        editDisableList.each {
            //non variable evaluation
            if (!it.contains('#')) {
                if(!it.trim().equals('ROLE_SALES')){
                    def resolvedEditDisable = userService.getResolvedUsernames(it.trim())      
                    //in future it may support multiple users
                    resolvedEditDisable.each{
                        editUsername.add(it)                               
                    }
                }else{
                    editUsername.add(['username':username])
                }
            } else {

            }

        }
        log.info('resolved editUsername: ' + editUsername)

        editUsername.each {
            log.info('it.username: ' + it.username)
            def partyOrder = PartyOrder.findByOrderNoAndUsername(order.orderNo, it.username)

            if (partyOrder) {
                partyOrder.editUrl = 'null'
                partyOrder.save(failOnError:true)
            }
        }

    	//post for the existing party orders
    	
    	
   //  	def resolvedParticipants = userService.getResolvedUsernames(currentStepInfo.participants)

   //  	resolvedParticipants.each {
   //  		new PartyOrder(
			// 	orderType: order.orderType.getId(), orderNo:order.orderNo, 
			// 	username:it.username, status:order.status, 
			// 	viewUrl:currentStepInfo.viewUrl, actionUrl:currentStepInfo.actionUrl
			// ).save(failOnError:true)
   //  	}

    }


}
