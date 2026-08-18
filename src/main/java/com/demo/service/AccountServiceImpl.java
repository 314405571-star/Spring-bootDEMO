package com.demo.service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.Result;
import com.demo.mapper.AccountMapper;
import com.demo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper mapper;
    @Override
    public Result<Account> findById(Long id) {
        Account account = mapper.selectOne(
                new LambdaQueryWrapper<Account>().eq(Account::getId, id)
        );
        if (account == null) {
            return Result.fail("账户不存在");
        }
        return Result.ok(account);
    }
    @Override
    public Result<Account> create(Account account) {
        mapper.insert(account);
        return Result.ok(account);
    }
    @Override
    public Result<Account> update(Account account) {
        Account existing = mapper.selectById(account.getId());
        if (existing == null) {
            return Result.fail("账户不存在");
        }
        int rows = mapper.updateAccount(account);
        if (rows == 0) {
            return Result.error("更新失败，没有数据被修改");
        }
        return Result.ok(mapper.selectById(account.getId()));
    }
    @Override
    public Result<Boolean> delete(Long id) {
        int count = mapper.deleteAccountById(id);
        if (count == 0) {
            return Result.fail("账户不存在");
        }
        return Result.ok(true);
    }
    // 批量
    @Override
    public Result<List<Account>> findByIds(List<Long> ids) {
        List<Account> accounts = mapper.selectList(
                new LambdaQueryWrapper<Account>().in(Account::getId, ids)
        );
        List<Account> sorted = accounts.stream()
                .sorted(Comparator.comparing(Account::getId))
                .collect(Collectors.toList());
        return Result.ok(sorted);
    }
    @Override
    @Transactional
    public Result<List<Account>> createBatch(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return Result.ok(accounts);
        }
        int rows = mapper.insertBatch(accounts);
        if (rows != accounts.size()) {
            return Result.error("批量插入失败，实际插入 " + rows + " 行");
        }
        return Result.ok(accounts);
    }
    @Override
    @Transactional
    public Result<Integer> updateBatch(List<Account> accounts) {
        int count = accounts.stream()
                .mapToInt(mapper::updateAccount)
                .sum();
        return Result.ok(count);
    }
    @Override
    public Result<Integer> deleteBatch(List<Long> ids) {
        int count = mapper.deleteAccountByIds(ids);
        return Result.ok(count);
    }
}
