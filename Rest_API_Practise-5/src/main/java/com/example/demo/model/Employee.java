package com.example.demo.model;
import jakarta.persistence.*;
@Entity
@Table(name="EmployeeRelation")
public class Employee {
	public int getEmpid() {
		return empid;
	}
	public void setEmpid(int empid) {
		this.empid = empid;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	
	@Id
	@Column(name="Employeeid")
	private int empid;
	@Column(name="Employeename")
	private String empname;

}