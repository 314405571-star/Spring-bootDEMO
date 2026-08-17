package com.demo.service;

import com.demo.common.Result;
import com.demo.model.Account;

import java.util.List;

/**
 * 账户业务层接口。
 *
 * 只声明“做什么”（方法签名），不写“怎么做”（具体实现）。
 * 具体实现放在 AccountServiceImpl 里。
 *
 * 为什么这样设计（面向对象的多态）：
 *   Controller 只依赖这个接口，不依赖具体实现类。
 *   以后如果想换成另一种实现（比如换成 Redis 缓存版），
 *   只需新建一个实现类，Controller 代码一行都不用改。
 */
public interface AccountService {

    /** 单条查询 */
    Result<Account> findById(Long id);

    /** 单条新增 */
    Result<Account> create(Account account);

    /** 单条更新 */
    Result<Account> update(Account account);

    /** 单条删除 */
    Result<Boolean> delete(Long id);

    /** 批量查询 */
    Result<List<Account>> findByIds(List<Long> ids);

    /** 批量新增 */
    Result<List<Account>> createBatch(List<Account> accounts);

    /** 批量更新 */
    Result<Integer> updateBatch(List<Account> accounts);

    /** 批量删除 */
    Result<Integer> deleteBatch(List<Long> ids);
}
