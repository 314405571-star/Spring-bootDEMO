package com.demo.service;
import com.demo.common.Result;
import com.demo.model.Account;
import java.util.List;
public interface AccountService {
    Result<Account> findById(Long id);
    Result<Account> create(Account account);
    Result<Account> update(Account account);
    Result<Boolean> delete(Long id);
    Result<List<Account>> findByIds(List<Long> ids);
    Result<List<Account>> createBatch(List<Account> accounts);
    Result<Integer> updateBatch(List<Account> accounts);
    Result<Integer> deleteBatch(List<Long> ids);
}
