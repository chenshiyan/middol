package dtp.sys

class Message {

    User fromUser
    User toUser

    String subject
    String body

    String status
    Boolean acknowledged = Boolean.FALSE

    Message(User fromUser, User toUser, String subject, String body) {
//        this()
        this.fromUser = fromUser
        this.toUser = toUser
        this.subject = subject
        this.body = body
    }

    static mapping =  {
        version(false)
    }

    static constraints = {
        fromUser nullable:true, blank:true
        status nullable: true, blank:true
        subject nullable: true, blank:true
        body nullable: true, blank:true
    }
}
