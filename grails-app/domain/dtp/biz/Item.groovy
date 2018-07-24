package dtp.biz

class Item {

	String itemNo				//编号
	String commonName			//通用名
	String itemName				//商品名
	String spec					//规格
	String unit					//单位
	Double price				//价格
	Manufacturer manufacturer 	//厂家
	String validityPeriod		//效期
	String status				//状态 0停用 1在用
	String remark				//备注
	String imageURL				//图像链接
	Item forItem				
	Boolean isMedicine = Boolean.TRUE			//是否药品
	Boolean hasCode = Boolean.FALSE           	//是否关联电子监管码
	Date dateCreated
	Date lastUpdated

    static mapping = {
        version (false)
    }

	static constraints = {
		itemNo size: 0..20, blank: false, unique: true
		commonName size: 0..50, blank: false
		itemName   size: 0..50, blank: false
		spec size: 0..50, blank: false
		unit  blank: false
		price blank: false
		validityPeriod size: 0..10, nullable: false
		remark size: 0..200,  nullable: true
		imageURL nullable: true
		isMedicine nullable: true
		forItem nullable: true
		manufacturer nullable: true
	}
}