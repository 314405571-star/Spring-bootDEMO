package com.demo;
import com.demo.common.Result;
import com.demo.model.Account;
import com.demo.service.AccountService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceTest {
    @Autowired
    private AccountService service;
    @Test
    @Order(1)
    void 单条查询() {
        Result<Account> r = service.findById(1L);
        assertEquals(200, r.getCode());
        assertNotNull(r.getData());
        assertEquals("张三", r.getData().getName());
        assertEquals(new BigDecimal("5000.00"), r.getData().getBalance());
    }
    @Test
    @Order(2)
    void 单条查询不存在返回404() {
        Result<Account> r = service.findById(999L);
        assertEquals(404, r.getCode());
        assertNull(r.getData());
    }
    @Test
    @Order(3)
    void 批量查询() {
        Result<List<Account>> r = service.findByIds(Arrays.asList(3L, 1L, 2L));
        assertEquals(200, r.getCode());
        assertEquals(3, r.getData().size());
        // Stream 排序后应按 id 升序：1,2,3
        assertEquals(1L, r.getData().get(0).getId());
        assertEquals(2L, r.getData().get(1).getId());
        assertEquals(3L, r.getData().get(2).getId());
    }
    @Test
    @Order(4)
    void 单条插入() {
        Account account = new Account("赵六", new BigDecimal("15000.00"), "定期");
        Result<Account> r = service.create(account);
        assertEquals(200, r.getCode());
        assertNotNull(r.getData().getId()); // 自增 id 被回填
    }
    @Test
    @Order(5)
    void 批量插入() {
        Account a1 = new Account("钱七", new BigDecimal("1000.00"), "活期");
        Account a2 = new Account("孙八", new BigDecimal("2000.00"), "定期");
        Result<List<Account>> r = service.createBatch(Arrays.asList(a1, a2));
        assertEquals(200, r.getCode());
        assertEquals(2, r.getData().size());
        assertNotNull(r.getData().get(0).getId());
        assertNotNull(r.getData().get(1).getId());
    }
    @Test
    @Order(6)
    void 单条更新() {
        Account account = new Account("张三丰", new BigDecimal("5500.00"), "定期");
        account.setId(1L);
        Result<Account> r = service.update(account);
        assertEquals(200, r.getCode());
        assertEquals("张三丰", r.getData().getName());
        assertEquals(new BigDecimal("5500.00"), r.getData().getBalance());
    }
    @Test
    @Order(7)
    void 批量更新() {
        Account a1 = new Account("李四改", new BigDecimal("30001.00"), "活期");
        a1.setId(2L);
        Account a2 = new Account("王五改", new BigDecimal("8001.00"), "定期");
        a2.setId(3L);
        Result<Integer> r = service.updateBatch(Arrays.asList(a1, a2));
        assertEquals(200, r.getCode());
        assertEquals(2, r.getData());
    }
    @Test
    @Order(8)
    void 单条删除() {
        Result<Boolean> r = service.delete(3L);
        assertEquals(200, r.getCode());
        assertTrue(r.getData());
    }
    @Test
    @Order(9)
    void 批量删除() {
        Result<Integer> r = service.deleteBatch(Arrays.asList(1L, 2L));
        assertEquals(200, r.getCode());
        assertEquals(2, r.getData());
    }
}
