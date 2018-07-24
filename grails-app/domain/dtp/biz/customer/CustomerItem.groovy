package dtp.biz.customer

import dtp.biz.Customer
import dtp.biz.Item

/**
 * 客户辅助材料
 */
import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class CustomerItem implements Serializable {

    private static final long serialVersionUID = 1

    Customer customer
    Item item

    @Override
    boolean equals(other) {
        if (other instanceof CustomerItem) {
            other.customerId == customer?.id && other.itemId == item?.id
        }
    }

    @Override
    int hashCode() {
        def builder = new HashCodeBuilder()
        if (customer) builder.append(customer.id)
        if (item) builder.append(item.id)
        builder.toHashCode()
    }

    static CustomerItem get(long customerId, long itemId) {
        criteriaFor(customerId, itemId).get()
    }

    static boolean exists(long customerId, long itemId) {
        criteriaFor(customerId, itemId).count()
    }

    private static DetachedCriteria criteriaFor(long customerId, long itemId) {
        CustomerItem.where {
            customer == Customer.load(customerId) &&
                    item == Item.load(itemId)
        }
    }

    static CustomerItem create(Customer customer, Item item) {
        def instance = new CustomerItem(customer: customer, item: item)
        instance.save()
        instance
    }

    static boolean remove(Customer c, Item i) {
        if (c != null && i != null) {
            CustomerItem.where { customer == c && item == i }.deleteAll()
        }
    }

    static int removeAll(Customer c) {
        c == null ? 0 : CustomerItem.where { customer == c }.deleteAll()
    }

    static int removeAll(Item i) {
        i == null ? 0 : CustomerItem.where { item == i }.deleteAll()
    }

    static constraints = {
        item validator: { Item i, CustomerItem ci ->
            if (ci.customer?.id) {
                CustomerItem.withNewSession {
                    if (CustomerItem.exists(ci.customer.id, i.id)) {
                        return ['CustomerItem.exists']
                    }
                }
            }
        }
    }

    static mapping = {
        id composite: ['customer', 'item']
        version false
    }
}
