package application;

import java.util.Date;

public class  Item {
	private String name;
	private double unit;
	private double time;
	private Date date;
	
	
	public Item() {
		super();
	}
	public Item(String name, double unit, double time, Date date) {
		super();
		this.name = name;
		this.unit = unit;
		this.time = time;
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getUnit() {
		return unit;
	}
	public void setUnit(double unit) {
		this.unit = unit;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public double getPower() {
		return this.unit * this.time;
	}
	@Override
	public String toString() {
		return "Item [name=" + name + ", unit=" + unit + ", time=" + time + ", date=" + date + "]";
	}
	
	

}
