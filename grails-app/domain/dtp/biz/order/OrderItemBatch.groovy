package dtp.biz.order

class OrderItemBatch {

    String batchNo
    String emc

    static belongsTo = [order: OrderItem]

    static mapping = {
        version(false)
    }

    static constraints = {
        batchNo nullable: true, blank:true
        emc nullable:true, blank: true
    }
}
