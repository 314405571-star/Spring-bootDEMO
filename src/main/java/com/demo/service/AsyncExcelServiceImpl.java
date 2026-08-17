package com.demo.service;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.mapper.AccountMapper;
import com.demo.model.Account;
import com.demo.model.AccountExcelRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsyncExcelServiceImpl implements AsyncExcelService {
    @Autowired
    private AccountMapper mapper;
    @Override
    @Async
    public void exportAccounts(String filePath) {
        List<Account> accounts = mapper.selectList(
                new LambdaQueryWrapper<Account>().orderByAsc(Account::getId)
        );
        if (accounts == null || accounts.isEmpty()) {
            return;
        }
        List<AccountExcelRow> rows = accounts.stream()
                .sorted(Comparator.comparing(Account::getId))
                .map(a -> new AccountExcelRow(a.getId(), a.getName()))
                .collect(Collectors.toList());
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        EasyExcel.write(file, AccountExcelRow.class)
                .sheet("账户列表")
                .doWrite(rows);
    }
}
