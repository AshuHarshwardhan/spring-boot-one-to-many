package com.cepheid.cloud.skel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "items")
public class Item extends AbstractEntity {

	@ApiModelProperty(notes = "The name attribute of item")
	@Size(min=1, message="Name should have atleast 5 characters")
	@Column(name = "name")
	private String name;

	@Min(value = 0l, message = "Price can not be negative. Please select positive numbers Only")
	@Column(name = "price")
	private int price;

	public Item() {
	}

	public Item(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", price=" + price + "]";
	}
}