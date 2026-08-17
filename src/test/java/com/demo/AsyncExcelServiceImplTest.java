package com.demo;

import com.alibaba.excel.EasyExcel;
import com.demo.mapper.AccountMapper;
import com.demo.model.Account;
import com.demo.model.AccountExcelRow;
import com.demo.service.AsyncExcelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 异步导出 Excel 的单元测试（Mockito）。
 *
 * 注意：单元测试里没有 Spring 上下文，没有代理对象，
 * @Async 不会真正切到别的线程，方法会「同步」跑完，
 * 正好方便我们直接验证写出来的 Excel 内容和顺序。
 */
@ExtendWith(MockitoExtension.class)
class AsyncExcelServiceImplTest {

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AsyncExcelServiceImpl service;

    /** JUnit 的临时目录，测试结束自动删除，不留垃圾文件 */
    @TempDir
    Path tempDir;

    /** 工具方法：快速构造一个带 id 的 Account */
    private Account account(Long id, String name) {
        Account a = new Account(name, new BigDecimal("100.00"), "活期");
        a.setId(id);
        return a;
    }

    @Test
    void 导出excel_只写id和name两列且顺序不乱() {
        // 数据库返回的顺序是乱的：3, 1, 2
        when(mapper.selectList(any())).thenReturn(Arrays.asList(
                account(3L, "name3"),
                account(1L, "name1"),
                account(2L, "name2")
        ));

        String filePath = tempDir.resolve("accounts.xlsx").toString();
        service.exportAccounts(filePath);

        // 把生成的 Excel 读回来，验证内容和顺序
        List<AccountExcelRow> rows = EasyExcel.read(filePath)
                .head(AccountExcelRow.class)
                .sheet()
                .doReadSync();

        assertEquals(3, rows.size());
        // 关键断言：行序必须是 id 升序（1, 2, 3），顺序不能乱
        assertEquals(1L, rows.get(0).getId());
        assertEquals("name1", rows.get(0).getName());
        assertEquals(2L, rows.get(1).getId());
        assertEquals("name2", rows.get(1).getName());
        assertEquals(3L, rows.get(2).getId());
        assertEquals("name3", rows.get(2).getName());
    }
}
