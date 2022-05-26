package com.bazra.usermanagement.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.TransactionRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.request.DepositRequest;
import com.bazra.usermanagement.request.TransferRequest;
import com.bazra.usermanagement.request.WithdrawRequest;
import com.bazra.usermanagement.response.DepositResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.TransferResponse;
import com.bazra.usermanagement.response.WithdrawResponse;
import com.bazra.usermanagement.util.JwtUtil;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    TransactionRepository transactionRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
   
    Account toAccount;
    Account fromAccount;

    public Account save(Account account) {
        accountRepository.save(account);
        return accountRepository.findByAccountNumberEquals(account.getAccountNumber()).get();
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    
    public List<Transaction> findall(String accountnumber){
        List<Transaction> transactions = transactionRepository.findByAccountNumberEquals(accountnumber);
        return transactions;
    }

    public Account findByAccountNumber(String accountnumber) {
        Account account = accountRepository.findByAccountNumberEquals(accountnumber).get();
        return account;
    }
    
    public ResponseEntity<?> sendMoney(TransferRequest transferBalanceRequest,String name) {

        String toAccountNumber = transferBalanceRequest.getToAccountNumber();
        BigDecimal amount = transferBalanceRequest.getAmount();
        fromAccount= accountRepository.findByAccountNumberEquals(name).get();
        toAccount = accountRepository.findByAccountNumberEquals(toAccountNumber).get();
        if (toAccount == null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid receipient"));
        }
        if(transferBalanceRequest.getAmount()==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
        }
        
        if (amount.compareTo(new BigDecimal(5)) == -1) {
            return ResponseEntity.badRequest().body(new ResponseError("Minimum amount to transfer is 5"));
        }
        if (transferBalanceRequest.getMessage() == null) {
            return ResponseEntity.badRequest().body(new ResponseError("Enter your remark"));
        }
        if (fromAccount.getBalance().compareTo(BigDecimal.ONE) == 1
                && fromAccount.getBalance().compareTo(amount) == 1) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountRepository.save(fromAccount);
            toAccount.setBalance(toAccount.getBalance().add(amount));
            accountRepository.save(toAccount);
            Transaction transaction = transactionRepository
                    .save(new Transaction(fromAccount.getUser_id(), fromAccount.getAccountNumber(), transferBalanceRequest.getToAccountNumber(), amount.negate(), Date.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)),"Transfer"));
            return ResponseEntity.ok(new TransferResponse(transaction.getaccountNumber(), transaction.getTransactionAmount(),
                    transaction.getTransactionDateTime(),
                    "Transfer successful! " + amount + "$ has been sent to " + toAccountNumber));
        }
        return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
    }

    public ResponseEntity<?> withdraw(WithdrawRequest withdraw,String name) {
        
        Account agAccount = accountRepository.findByAccountNumberEquals(name).get();
        Account frAccount = accountRepository.findByAccountNumberEquals(withdraw.getFromAccountNumber()).get();
        if (frAccount == null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }
        if(withdraw.getAmount()==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
        }
        BigDecimal balance = frAccount.getBalance();
        if (balance.compareTo(withdraw.getAmount()) == 1) {
            frAccount.setBalance(balance.subtract(withdraw.getAmount()));
            accountRepository.save(frAccount);
            agAccount.setBalance(agAccount.getBalance().add(withdraw.getAmount()));
            accountRepository.save(agAccount);
            Transaction transaction = transactionRepository
                    .save(new Transaction(agAccount.getUser_id(), withdraw.getFromAccountNumber(),name, withdraw.getAmount().negate(), Date.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)),"withdraw"));
            return ResponseEntity.ok(new WithdrawResponse(transaction.getTransactionAmount(),
                    transaction.getTransactionDateTime(),  "Amount " + withdraw.getAmount() + "$ has been debited from your account. Your current balance is "+balance.subtract(withdraw.getAmount())));
        }

        return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
    }
    
public ResponseEntity<?> Deposit(DepositRequest depositRequest,String name) {
        
        Account agAccount = accountRepository.findByAccountNumberEquals(name).get();
        Account toAccount = accountRepository.findByAccountNumberEquals(depositRequest.getToAccountNumber()).get();
        if (toAccount == null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }
        if(depositRequest.getAmount()==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Enter Valid Amount"));
        }
        BigDecimal balance = agAccount.getBalance();
        if (balance.compareTo(depositRequest.getAmount()) == 1) {
            agAccount.setBalance(balance.subtract(depositRequest.getAmount()));
            accountRepository.save(agAccount);
            toAccount.setBalance(toAccount.getBalance().add(depositRequest.getAmount()));
            accountRepository.save(toAccount);
            Transaction transaction = transactionRepository
                    .save(new Transaction(agAccount.getUser_id(),name, depositRequest.getToAccountNumber(), depositRequest.getAmount(), Date.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)),"Deposit"));
            return ResponseEntity.ok(new DepositResponse(transaction.getTransactionAmount(),
                    transaction.getTransactionDateTime(), "Amount " + depositRequest.getAmount() + "$ has been credited to your account. Your current balance is "+toAccount.getBalance().add(depositRequest.getAmount())));
        }

        return ResponseEntity.badRequest().body(new ResponseError("Insufficient balance"));
    }
//    
//    public AccountStatement getStatement(String accountnumber) {
//        Account account = accountRepository.findByAccountNumberEquals(accountnumber);
//        return new AccountStatement(account.getBalance(),transactionRepository.findByAccountNumberEquals(accountnumber));
//    }

}
