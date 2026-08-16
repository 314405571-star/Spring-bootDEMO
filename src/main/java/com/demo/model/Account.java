package com.demo.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("account")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("name")
    @NotBlank(message = "账户名不能为空")
    private String name;
    @TableField("balance")
    @NotNull(message = "余额不能为空")
    @DecimalMin(value = "0.00", message = "余额不能为负数")
    private BigDecimal balance;
    @TableField("type")
    @NotBlank(message = "账户类型不能为空")
    private String type;
    @TableField("create_time")
    private LocalDateTime createTime;
    public Account() {}
    public Account(String name, BigDecimal balance, String type) {
        this.name = name;
        this.balance = balance;
        this.type = type;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
