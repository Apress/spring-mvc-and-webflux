package com.apress.prospringmvc.bookstore.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Iuliana Cosmina
 */
//@Embeddable
public class Order {

	@Id // TODO add autogenerator here
	private String id;

	@Transient private Account account;

	// mongo one-to-one, embedded
	private Address shippingAddress;

	// mongo one-to-one, embedded
	private Address billingAddress;

	private boolean billingSameAsShipping = true;

	private Date orderDate;
	private Date deliveryDate;

	private BigDecimal totalOrderPrice = BigDecimal.ZERO;

 // embed
	private List<OrderDetail> orderDetails = new ArrayList<>();

	public Order() {
		super();
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Address getShippingAddress() {
		return this.shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		if (this.billingSameAsShipping) {
			return this.shippingAddress;
		}
		return this.billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public boolean isBillingSameAsShipping() {
		return this.billingSameAsShipping;
	}

	public void setBillingSameAsShipping(boolean billingSameAsShipping) {
		this.billingSameAsShipping = billingSameAsShipping;
	}

	public String getId() {
		return this.id;
	}

	public List<OrderDetail> getOrderDetails() {
		return this.orderDetails;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public BigDecimal getTotalOrderPrice() {
		return this.totalOrderPrice;
	}

	public int getTotalNumberOfbooks() {
		int total = 0;
		for (OrderDetail orderDetail : getOrderDetails()) {
			total += orderDetail.getQuantity();
		}
		return total;
	}

	/**
	 * Update the order details and update the total price. If the quantity is 0 or less the order detail is removed from the list.
	 */
	public void updateOrderDetails() {
		BigDecimal total = BigDecimal.ZERO;
		Iterator<OrderDetail> details = this.orderDetails.iterator();
		while (details.hasNext()) {
			OrderDetail detail = details.next();
			if (detail.getQuantity() <= 0) {
				details.remove();
			} else {
				total = total.add(detail.getPrice());

			}
		}
		this.totalOrderPrice = total.setScale(2, RoundingMode.HALF_UP);
	}

	public void addOrderDetail(OrderDetail detail) {
		this.orderDetails.add(detail);
		this.totalOrderPrice = this.totalOrderPrice.add(detail.getPrice());
	}

	public void setOrderDetails(List<OrderDetail> details) {
		this.orderDetails = details;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id='" + id + '\'' +
				", shippingAddress=" + shippingAddress +
				", orderDate=" + orderDate +
				", totalOrderPrice=" + totalOrderPrice +
				", orderDetails=" + orderDetails +
				'}';
	}
}
