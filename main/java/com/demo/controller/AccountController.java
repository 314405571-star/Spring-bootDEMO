package com.demo.controller;
import com.demo.common.Result;
import com.demo.model.Account;
import com.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService service;
    @PostMapping("/get")
    public Result<Account> get(@RequestParam Long id) {
        return service.findById(id);
    }
    @PostMapping("/create")
    public Result<Account> create(@RequestBody Account account) {
        return service.create(account);
    }
    @PostMapping("/update")
    public Result<Account> update(@RequestBody Account account) {
        return service.update(account);
    }
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam Long id) {
        return service.delete(id);
    }
    // 批量
    @PostMapping("/list")
    public Result<List<Account>> list(@RequestBody List<Long> ids) {
        return service.findByIds(ids);
    }
    @PostMapping("/createBatch")
    public Result<List<Account>> createBatch(@RequestBody List<Account> accounts) {
        return service.createBatch(accounts);
    }
    @PostMapping("/updateBatch")
    public Result<Integer> updateBatch(@RequestBody List<Account> accounts) {
        return service.updateBatch(accounts);
    }
    @PostMapping("/deleteBatch")
    public Result<Integer> deleteBatch(@RequestBody List<Long> ids) {
        return service.deleteBatch(ids);
    }
}
