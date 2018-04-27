package ie.gmit.sw.fyp.me;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

@Entity
public class TestEntity {
	
//	@Id
//	private String Id;
//	private float num1;
//	private int num2;
	
	
//	@ElementCollection
//    @MapKeyColumn(name="name")
//    @Column(name="value")
	private Map<String, Object> properties;
	
	
	
	public TestEntity() {
		properties = new HashMap<>();
	}

	
//	@Id
//	public String getId() {
//		return Id;
//	}

//	@Id
//	public void setId(String id) {
//		this.Id = id;
//	}
	
	
	@Id
	public String getId() {
		return (String) properties.get("Id");
	}

//	@Id
	public void setId(Object id) {
		properties.put("Id", id);
	}

	@Column
	public float getNum1() {
		return (float) properties.get("num1");
	}

//	@Column
	public void setNum1(float num1) {
		properties.put("num1", num1);
	}

	@Column
	public int getNum2() {
		return (int) properties.get("num2");
	}

//	@Column
	public void setNum2(int num2) {
		properties.put("num2", num2);
	}

//	@Override
//	public String toString() {
//		return "TestEntity [Id=" + Id + ", num1=" + num1 + ", num2=" + num2 + "]";
//	}
	
	
	
}
