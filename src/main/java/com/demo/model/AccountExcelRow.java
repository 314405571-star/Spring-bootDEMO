package com.demo.model;
import com.alibaba.excel.annotation.ExcelProperty;
public class AccountExcelRow {
    @ExcelProperty("id")
    private Long id;
    @ExcelProperty("name")
    private String name;
    public AccountExcelRow() {
    }
    public AccountExcelRow(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Long getId() {return id;}
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
