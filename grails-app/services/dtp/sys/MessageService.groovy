package dtp.sys

import grails.transaction.Transactional
import org.springframework.messaging.simp.SimpMessagingTemplate

@Transactional
class MessageService {

    transient springSecurityService

    def serviceMethod() {

    }

    def myMessagesCount() {
        def username = springSecurityService.getPrincipal()?.getUsername()

        def user = User.findByUsername(username)

        def count = Message.countByToUserAndAcknowledged(user, Boolean.FALSE)

        return count
    }

    def myMessages(){
        def username = springSecurityService.getPrincipal()?.getUsername()

        def user = User.findByUsername(username)

        def messages = Message.findAllByToUserAndAcknowledged(user, Boolean.FALSE)

        return messages
    }

    def sendMessage(User fromUser, User toUser, String subject, String body){
        def msg = new Message(fromUser,toUser,subject,body)

        log.info('message: ' + msg)

        if (!msg.save()){
            throw new Exception('error occured...')
        }

        sendMessageWebsocket('/topic/msg', subject, body)
    }

    //private method
    void sendMessageWebsocket(String topic, String subject, String body) {

        if (topic == null) {
            topic = '/topic/msg'
        }

        def msg = subject + ' ' + body

        brokerMessagingTemplate.convertAndSend topic, msg
    }

    def acknowledgeMessage(int messageId) {
        log.info('messageId: ' + messageId)

        def msg = Message.get(messageId)

        log.info('msg : ' + msg)

        if (msg != null) {
            msg.acknowledged = Boolean.TRUE
            if (!msg.save()){
                throw new Exception('error occured...')
            }
        }
    }


}
