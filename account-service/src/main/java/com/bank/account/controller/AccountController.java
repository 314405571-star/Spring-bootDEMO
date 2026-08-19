package com.bank.account.controller;
import com.bank.account.entity.Account;
import com.bank.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    //查询
    @GetMapping("/list")
    public Map<Long, Account> list() {
        return accountService.findAll();
    }
    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id) {
        return accountService.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("账户 " + id + " 不存在"));
    }

    //转账，请求体示例：

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        return accountService.transfer(request.getFrom(), request.getTo(), request.getAmount());
    }
    // 转账请求参数
    public static class TransferRequest {
        private Long from;
        private Long to;
        private double amount;
        public Long getFrom() {
            return from;
        }
        public void setFrom(Long from) {
            this.from = from;
        }
        public Long getTo() {
            return to;
        }
        public void setTo(Long to) {
            this.to = to;
        }
        public double getAmount() {
            return amount;
        }
        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    //处理报错
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }
}
