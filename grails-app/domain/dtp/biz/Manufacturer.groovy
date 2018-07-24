package dtp.biz

class Manufacturer {

    String manufacturerNo
    String manufacturerName
    String address
    String tel
    String remark
    String status = 1
    Date dateCreated
    Date lastUpdated

    static mapping = {
        table('t_manufacturer')
        version (false)
    }

    static constraints = {
        manufacturerNo size: 0..20, blank: false, unique: true
        manufacturerName  size: 0..40, blank: false
        remark size: 0..40,nullable: true
        address size: 0..255,nullable: true
        tel size: 0..20, nullable: true
        status size: 0..20, nullable: true
    }
}
