package com.cepheid.cloud.skel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "descriptions")
public class Description extends AbstractEntity {

	@ApiModelProperty(notes = "Description attribute")
	@Size(min=5, message="Decription should have atleast 5 characters")
	@Column(name = "description")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "item_mId", nullable = false)
	private Item item;

	public Description() {

	}

	public Description(String description, Item item) {
		this.description = description;
		this.item = item;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Description [description=" + description + ", item=" + item + "]";
	}
}
