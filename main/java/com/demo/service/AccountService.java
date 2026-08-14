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
 * 账户业务层。
 *
 * 本层约定：
 *   1. 查询用 LambdaQueryWrapper（lambda 表达式）实现
 *   2. 更新走 Mapper 的 @Update 注解方法
 *   3. 删除走 Mapper 的 XML 方法
 *   4. 批量操作优先使用 Stream 流
 *   5. 所有方法都不返回 null，统一返回新建的 Result 对象
 */
@Service
public class AccountService {

    @Autowired
    private AccountMapper mapper;

    // ==================== 单条操作 ====================

    /** 单条查：LambdaQueryWrapper（lambda 表达式） */
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
    public Result<Account> create(Account account) {
        mapper.insert(account);
        return Result.ok(account);
    }

    /** 单条更新：调用 Mapper 的 @Update 注解方法 */
    public Result<Account> update(Account account) {
        Account existing = mapper.selectById(account.getId());
        if (existing == null) {
            return Result.fail("账户不存在");
        }
        mapper.updateAccount(account);
        return Result.ok(mapper.selectById(account.getId()));
    }

    /** 单条删：调用 Mapper 的 XML 方法 */
    public Result<Boolean> delete(Long id) {
        int count = mapper.deleteAccountById(id);
        if (count == 0) {
            return Result.fail("账户不存在");
        }
        return Result.ok(true);
    }

    // ==================== 批量操作（Stream 流） ====================

    /** 批量查：LambdaQueryWrapper + Stream 排序 */
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

    /** 批量插：Stream.forEach + 事务 */
    @Transactional
    public Result<List<Account>> createBatch(List<Account> accounts) {
        accounts.stream().forEach(mapper::insert);
        return Result.ok(accounts);
    }

    /** 批量更新：Stream.mapToInt + @Update 注解方法 */
    @Transactional
    public Result<Integer> updateBatch(List<Account> accounts) {
        int count = accounts.stream()
                .mapToInt(mapper::updateAccount)
                .sum();
        return Result.ok(count);
    }

    /** 批量删：XML 方法 */
    public Result<Integer> deleteBatch(List<Long> ids) {
        int count = mapper.deleteAccountByIds(ids);
        return Result.ok(count);
    }
}
