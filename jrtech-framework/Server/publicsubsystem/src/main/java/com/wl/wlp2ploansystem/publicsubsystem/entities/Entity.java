package com.wl.wlp2ploansystem.publicsubsystem.entities;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;

import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity implements Serializable {

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (id == null || other.id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	//http://mp.baomidou.com/#/question
	@JsonSerialize(using=ToStringSerializer.class)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 

	@Override
	public String toString() {
		return "Entity [id=" + id + "]";
	}

	 
	@TableId(value = "ID", type = IdType.INPUT)
	@Display("主键")
	@Size(max = 50)
	private String id;
}
