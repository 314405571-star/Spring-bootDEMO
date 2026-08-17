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

/**
 * 账户业务层实现类。
 *
 * 约定：
 *   1. 查询用 LambdaQueryWrapper（lambda 表达式）
 *   2. 更新走 Mapper 的 @Update 注解方法，并判断受影响行数
 *   3. 删除走 Mapper 的 XML 方法
 *   4. 批量插入走 XML 的 insertBatch（一条 SQL 插多行），其余批量操作用 Stream 流
 *   5. 所有方法都不返回 null，统一返回新建的 Result 对象
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper mapper;

    // ==================== 单条操作 ====================

    /** 单条查：LambdaQueryWrapper（lambda 表达式） */
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

    /** 单条插：BaseMapper.insert */
    @Override
    public Result<Account> create(Account account) {
        mapper.insert(account);
        return Result.ok(account);
    }

    /** 单条更新：调用 Mapper 的 @Update 注解方法，并判断更新结果 */
    @Override
    public Result<Account> update(Account account) {
        Account existing = mapper.selectById(account.getId());
        if (existing == null) {
            return Result.fail("账户不存在");
        }
        // 关键：判断更新结果。受影响行数 > 0 才算更新成功（没有异常、确实改到了数据）
        int rows = mapper.updateAccount(account);
        if (rows == 0) {
            return Result.error("更新失败，没有数据被修改");
        }
        return Result.ok(mapper.selectById(account.getId()));
    }

    /** 单条删：调用 Mapper 的 XML 方法 */
    @Override
    public Result<Boolean> delete(Long id) {
        int count = mapper.deleteAccountById(id);
        if (count == 0) {
            return Result.fail("账户不存在");
        }
        return Result.ok(true);
    }

    // ==================== 批量操作（Stream 流） ====================

    /** 批量查：LambdaQueryWrapper + Stream 排序 */
    @Override
    public Result<List<Account>> findByIds(List<Long> ids) {
        List<Account> accounts = mapper.selectList(
                new LambdaQueryWrapper<Account>().in(Account::getId, ids)
        );
        // Stream 流：按 id 升序排序
        List<Account> sorted = accounts.stream()
                .sorted(Comparator.comparing(Account::getId))
                .collect(Collectors.toList());
        return Result.ok(sorted);
    }

    /** 批量插：一条 SQL 插入多行（不是 for 循环单条插入） */
    @Override
    @Transactional
    public Result<List<Account>> createBatch(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return Result.ok(accounts);
        }
        int rows = mapper.insertBatch(accounts);
        // 判断结果：插入的行数应等于传入的条数，否则视为失败
        if (rows != accounts.size()) {
            return Result.error("批量插入失败，实际插入 " + rows + " 行");
        }
        return Result.ok(accounts);
    }

    /** 批量更新：Stream.mapToInt + @Update 注解方法 */
    @Override
    @Transactional
    public Result<Integer> updateBatch(List<Account> accounts) {
        int count = accounts.stream()
                .mapToInt(mapper::updateAccount)
                .sum();
        return Result.ok(count);
    }

    /** 批量删：XML 方法 */
    @Override
    public Result<Integer> deleteBatch(List<Long> ids) {
        int count = mapper.deleteAccountByIds(ids);
        return Result.ok(count);
    }
}
