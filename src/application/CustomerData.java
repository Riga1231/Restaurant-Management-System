package application;

import java.sql.Date;

public class CustomerData {

    private Integer id;
    private Integer customerID;
    private Double total;
    private Date date;
    private String employeeName;

    public CustomerData(Integer id, Integer customerID, Double total,
             Date date, String employeeName) {
        this.id = id;
        this.customerID = customerID;
        this.total = total;
        this.date = date;
        this.employeeName = employeeName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public Double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public String getEmployeeName() {
        return employeeName;
    }

}
