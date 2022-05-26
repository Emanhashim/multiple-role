package com.bazra.usermanagement.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountService;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.TransactionRepository;
import com.bazra.usermanagement.request.BalanceRequest;
import com.bazra.usermanagement.request.DepositRequest;
import com.bazra.usermanagement.request.TransactionRequest;
import com.bazra.usermanagement.request.TransferRequest;
import com.bazra.usermanagement.request.WithdrawRequest;
import com.bazra.usermanagement.response.BalanceResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.TransactionResponse;



@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounts")
@Api(value = "Wallet  User's Activity  Endpoint", description = "This EndPoint Activities For Bazra  Wallet User's Activity")
@ApiResponses(value ={
        @ApiResponse(code = 404, message = "web user that a requested page is not available "),
        @ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
        @ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
        @ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
        @ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class UsersTransaction {

    @Autowired
    private AccountService accountService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    public UserInfo getCurrentUser(@AuthenticationPrincipal UserInfo user) {
        return user;
    }
    @GetMapping("/all")
    @ApiOperation(value ="This EndPoint To Get All Users Who User The Bazra Wallet")
    public List<Account> all() {
        return accountService.findAll();
    }

    @PostMapping("/sendmoney")
    @ApiOperation(value ="This Allows User To Transfer Money From One Account To Other. Post Method")
    public ResponseEntity<?> sendMoney(@RequestBody TransferRequest transferBalanceRequest, Authentication authentication) {
     
        return accountService.sendMoney(transferBalanceRequest,authentication.getName());
    }

    @PostMapping("/withdraw")
    @ApiOperation(value ="This EndPoint To WithDrawl Money From an Account. Post Method")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest,Authentication authentication) {
        
        return accountService.withdraw(withdrawRequest,authentication.getName());
    }
    
    @PostMapping("/deposit")
    @ApiOperation(value ="This EndPoint To Deposit Money From Account. Post Method")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest,Authentication authentication) {
        
        return accountService.Deposit(depositRequest,authentication.getName());
    }
    
    @GetMapping("/transaction")
    @ApiOperation(value ="This EndPoint To Get All Transaction History. Get Method" )
    public ResponseEntity<?> transaction(Authentication authentication) {
          Account account = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
          if (account == null) {
              return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
          }
          
           return ResponseEntity.ok(new TransactionResponse(transactionRepository.findByfromAccountNumberEquals(authentication.getName())));
//        return accountService.findall(transactionRequest.getAccountNumber());
    }

    @GetMapping("/balance")
    @ApiOperation(value ="This EndPoint Bring The Current Balance Get Method")
    public ResponseEntity<?> balance(Authentication authentication) {
    	Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();

        if (account==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }
        BigDecimal balance = account.getBalance();
        return ResponseEntity.ok(new BalanceResponse(balance,"Your current balance equals "+balance));
    }


}