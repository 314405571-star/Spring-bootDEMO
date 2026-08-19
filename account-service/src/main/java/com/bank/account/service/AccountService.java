package com.bank.account.service;

import com.bank.account.entity.Account;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 账户业务逻辑（演示版：数据存在内存 Map 里）
 * 正式项目里这一步会换成操作数据库。
 */
@Service
public class AccountService {

    /** 模拟账户数据：账户号 -> 账户 */
    private final Map<Long, Account> accountMap = new ConcurrentHashMap<>();

    /** 服务启动时预置 3 个银行账户 */
    @PostConstruct
    public void init() {
        accountMap.put(1001L, new Account(1001L, "张三", 10000.0));
        accountMap.put(1002L, new Account(1002L, "李四", 20000.0));
        accountMap.put(1003L, new Account(1003L, "王五", 5000.0));
    }

    /** 查询所有账户 */
    public Map<Long, Account> findAll() {
        return accountMap;
    }

    /** 按账户号查询单个账户 */
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountMap.get(id));
    }

    /**
     * 转账：从 fromId 扣款，给 toId 加款
     * synchronized 保证同一时刻只处理一笔转账（最简单的并发安全）
     */
    public synchronized String transfer(Long fromId, Long toId, double amount) {
        Account from = accountMap.get(fromId);
        Account to = accountMap.get(toId);

        if (from == null) {
            throw new IllegalArgumentException("转出账户 " + fromId + " 不存在");
        }
        if (to == null) {
            throw new IllegalArgumentException("转入账户 " + toId + " 不存在");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("转账金额必须大于 0");
        }
        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("余额不足，当前余额 " + from.getBalance());
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        return String.format("转账成功：%s(%s) -> %s(%s)，金额 %.2f 元，转出方剩余 %.2f 元",
                from.getName(), fromId, to.getName(), toId, amount, from.getBalance());
    }
}
