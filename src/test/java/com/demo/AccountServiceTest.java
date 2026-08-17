package com.demo;

import com.demo.common.Result;
import com.demo.mapper.AccountMapper;
import com.demo.model.Account;
import com.demo.service.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * AccountServiceImpl 的单元测试（Mockito）。
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountServiceImpl service;

    /** 工具方法：快速构造一个带 id 的 Account */
    private Account account(Long id, String name, String balance, String type) {
        Account a = new Account(name, new BigDecimal(balance), type);
        a.setId(id);
        return a;
    }

    @Test
    void 单条查询_存在() {
        when(mapper.selectOne(any())).thenReturn(account(1L, "张三", "5000.00", "活期"));

        Result<Account> r = service.findById(1L);

        assertEquals(200, r.getCode());
        assertEquals("张三", r.getData().getName());
        verify(mapper).selectOne(any());
    }

    @Test
    void 单条查询_不存在() {
        when(mapper.selectOne(any())).thenReturn(null);

        Result<Account> r = service.findById(999L);

        assertEquals(404, r.getCode());
        assertNull(r.getData());
    }

    @Test
    void 批量查询_按id升序() {
        when(mapper.selectList(any())).thenReturn(Arrays.asList(
                account(3L, "王五", "8000.00", "活期"),
                account(1L, "张三", "5000.00", "活期"),
                account(2L, "李四", "30000.00", "定期")
        ));

        Result<List<Account>> r = service.findByIds(Arrays.asList(1L, 2L, 3L));

        assertEquals(200, r.getCode());
        assertEquals(3, r.getData().size());
        assertEquals(1L, r.getData().get(0).getId());
        assertEquals(2L, r.getData().get(1).getId());
        assertEquals(3L, r.getData().get(2).getId());
    }

    @Test
    void 单条插入() {
        Account a = new Account("赵六", new BigDecimal("15000.00"), "定期");
        when(mapper.insert(a)).thenReturn(1);

        Result<Account> r = service.create(a);

        assertEquals(200, r.getCode());
        verify(mapper).insert(a);
    }

    @Test
    void 单条更新_成功() {
        Account req = account(1L, "张三丰", "5500.00", "定期");
        when(mapper.selectById(1L)).thenReturn(
                account(1L, "张三", "5000.00", "活期"),
                account(1L, "张三丰", "5500.00", "定期")
        );
        when(mapper.updateAccount(req)).thenReturn(1);

        Result<Account> r = service.update(req);

        assertEquals(200, r.getCode());
        assertEquals("张三丰", r.getData().getName());
        verify(mapper).updateAccount(req);
    }

    @Test
    void 单条更新_账户不存在() {
        when(mapper.selectById(999L)).thenReturn(null);
        Result<Account> r = service.update(account(999L, "x", "1.00", "活期"));
        assertEquals(404, r.getCode());
        verify(mapper, never()).updateAccount(any());
    }

    @Test
    void 单条更新_无数据被修改返回失败() {
        Account req = account(1L, "张三丰", "5500.00", "定期");
        when(mapper.selectById(1L)).thenReturn(account(1L, "张三", "5000.00", "活期"));
        when(mapper.updateAccount(req)).thenReturn(0);

        Result<Account> r = service.update(req);

        assertEquals(500, r.getCode());
        assertNull(r.getData());
    }

    @Test
    void 单条删除_成功() {
        when(mapper.deleteAccountById(1L)).thenReturn(1);

        Result<Boolean> r = service.delete(1L);

        assertEquals(200, r.getCode());
        assertTrue(r.getData());
    }

    @Test
    void 单条删除_不存在() {
        when(mapper.deleteAccountById(999L)).thenReturn(0);

        Result<Boolean> r = service.delete(999L);

        assertEquals(404, r.getCode());
        assertNull(r.getData());
    }

    @Test
    void 批量插入() {
        Account a1 = new Account("钱七", new BigDecimal("1000.00"), "活期");
        Account a2 = new Account("孙八", new BigDecimal("2000.00"), "定期");
        when(mapper.insert(any(Account.class))).thenReturn(1);
        Result<List<Account>> r = service.createBatch(Arrays.asList(a1, a2));
        assertEquals(200, r.getCode());
        assertEquals(2, r.getData().size());
        verify(mapper, times(2)).insert(any(Account.class));
    }

    @Test
    void 批量更新() {
        Account a1 = account(2L, "李四改", "30001.00", "活期");
        Account a2 = account(3L, "王五改", "8001.00", "定期");
        when(mapper.updateAccount(any(Account.class))).thenReturn(1);
        Result<Integer> r = service.updateBatch(Arrays.asList(a1, a2));
        assertEquals(200, r.getCode());
        assertEquals(2, r.getData());
        verify(mapper, times(2)).updateAccount(any(Account.class));
    }

    @Test
    void 批量删除() {
        when(mapper.deleteAccountByIds(anyList())).thenReturn(2);
        Result<Integer> r = service.deleteBatch(Arrays.asList(1L, 2L));
        assertEquals(200, r.getCode());
        assertEquals(2, r.getData());
        verify(mapper).deleteAccountByIds(anyList());
    }
}
