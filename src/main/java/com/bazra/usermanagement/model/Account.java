package com.bazra.usermanagement.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

//import com.bazra.usermanagement.model.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@SequenceGenerator(name = "seq", initialValue = 1000, allocationSize = 100)
@Entity
@Table(name = "accountinfo")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")

    @ApiModelProperty(value= "This Variable Holds Account Number Or Phone Number Of The User ")
    private String accountNumber;


    @ApiModelProperty(value= "This Variable Holds User's Balance")
    private BigDecimal balance;

    @ApiModelProperty(value= "This Variable Holds User's ID")
    private long user_id;

    @ApiModelProperty(value= "This Variable Holds Account Number Or Phone Number Of The User ")
    private String username;
    private String type;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    // private AccountTypes accounttype;
//  private Date createdat;
//  private Date updatedat;
//  private Date lastAccessed;
//  private Status status;
//  private AccountDetail accountDetail;
//    public Account() {
//
//    }
    
public Account(String accountNumber, BigDecimal balance, long user_id, String username,String type) {
		this.type=type;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.user_id = user_id;
		this.username = username;
	}


//	public Account() {
//	super();
//	// TODO Auto-generated constructor stub
//}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Account(String balance) {
        this.balance=new BigDecimal(balance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String id) {
        this.accountNumber = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

//  public Date getLastAccessed() {
//        return lastAccessed;
//    }
//    public void setLastAccessed(Date lastAccessed) {
//        this.lastAccessed = lastAccessed;
//    }
//    public Date getCreatedat() {
//      return createdat;
//  }
//  public void setCreatedat(Date createdat) {
//      this.createdat = createdat;
//  }
//  public Date getUpdatedat() {
//      return updatedat;
//  }
//  public void setUpdatedat(Date updatedat) {
//      this.updatedat = updatedat;
//  }
//  
//  public AccountTypes getAccounttype() {
//      return accounttype;
//  }
//  public AccountTypes setAccounttype(AccountTypes accounttype) {
//      return  accounttype;
//  }
//  
//    public Status getStatus() {
//        return status;
//    }
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//    public AccountDetail getAccountDetail() {
//        return accountDetail;
//    }
//    public void setAccountDetail(AccountDetail accountDetail) {
//        this.accountDetail = accountDetail;
//    }
//  

}
