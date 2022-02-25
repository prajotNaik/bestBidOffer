package com.pjt.bidoffer.model;

public class Order implements Comparable<Order>{

	private Double price;
	private long size;

	
	public Order(Double price, long size) {
		super();
		this.price = price;
		this.size = size;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Order [price=" + price + ", size=" + size + "]";
	}

	@Override
	public int compareTo(Order o) {
		return this.price > o.price ? 1 : this.price < o.price ? -1 : 0;
	}

	
}
