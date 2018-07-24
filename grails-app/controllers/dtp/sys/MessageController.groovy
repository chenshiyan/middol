package dtp.sys


import grails.rest.*
import grails.converters.*

class MessageController {
	static responseFormats = ['json', 'xml']

    transient def messageService
    transient springSecurityService
	
    def index() { }

//    def exceptionHandler(Exception e){
//        /*
//           This method will be called if any unhandled Execption occurs in the code
//        */
//        render (['status':'failaure...']) as JSON
//    }

    def myMessagesCount() {

        render (['count': messageService.myMessagesCount()]) as JSON
    }

    def myMessages() {

        render messageService.myMessages() as JSON
    }


    def sendMessage() {

        def ins = request.JSON

        log.info('request.JSON params: ' + ins)

        def fromUser = User.findByUsername(ins['fromUser'])
        def toUser = User.findByUsername(ins['toUser'])

        log.info('from user: ' + fromUser)
        log.info('to user: ' + toUser)

        messageService.sendMessage(fromUser, toUser, ins['subject'],ins['body'])

        render (['success':'success']) as JSON

    }

    def acknowledgeMessage(){
        def ins = request.JSON

        log.info('request.JSON params: ' + ins)

        messageService.acknowledgeMessage(ins['id'])

        render (['status':200]) as JSON
    }
}
