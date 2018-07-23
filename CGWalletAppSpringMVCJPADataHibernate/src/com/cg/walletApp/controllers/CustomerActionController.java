package com.cg.walletApp.controllers;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cg.walletApp.beans.Customer;
import com.cg.walletApp.beans.Transactions;
import com.cg.walletApp.exception.InsufficientBalanceException;
import com.cg.walletApp.exception.InvalidInputException;
import com.cg.walletApp.service.WalletService;
@Controller
public class CustomerActionController {
	@Autowired
	WalletService walletService;
	@RequestMapping(value="/registerCustomer")
	public ModelAndView registerCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result) {
		try {
					if(result.hasErrors()) return new ModelAndView("registrationPage");
			customer = walletService.createAccount(customer);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("registrationSuccessPage", "customer", customer);		
	}
	@RequestMapping(value="/findCustomer")
	public ModelAndView findCustomer(@RequestParam("mobileNo") String mobileNo) {

		Customer customer = new Customer();
		try {
			customer = walletService.showBalance(mobileNo);
		} catch (InvalidInputException e) {
			e.printStackTrace();
			
		}
		return new ModelAndView("showBalanceSuccess", "customer", customer);	
	}

	@RequestMapping(value="/depositAmt")
	public ModelAndView depositAmt(@RequestParam("mobileNo") String mobileNo, @RequestParam("wallet.balance") BigDecimal amount) {

		Customer customer = new Customer();
		try {
			customer = walletService.depositAmount(mobileNo, amount);
		} catch (InvalidInputException e) {
			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("depositAmountSuccess", "customer", customer);	
	}

	@RequestMapping(value="/withdrawAmount")
	public ModelAndView withdrawAmount(@RequestParam("mobileNo") String mobileNo, @RequestParam("wallet.balance") BigDecimal amount) {

		Customer customer = new Customer();

		try {
			customer = walletService.withdrawAmount(mobileNo, amount);
		} catch (InvalidInputException | InsufficientBalanceException e) {
			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("withdrawAmountSuccess", "customer", customer);	
	}

	@RequestMapping(value="/fundTsf")
	public ModelAndView fundTsf(@RequestParam("sourceMobile") String sourceMobile, @RequestParam("targetMobile") String targetMobile, @RequestParam("wallet.balance") BigDecimal amount) {

		Customer customer = new Customer();

		try {
			customer = walletService.fundTransfer(sourceMobile, targetMobile, amount);
		} catch (InvalidInputException | InsufficientBalanceException e) {

			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("fundTransferSuccess", "customer", customer);	
	}

	@RequestMapping(value="/printAllTransactions")
	public ModelAndView printAllTransactions(@RequestParam("mobileNo") String mobileNo) {

		List<Transactions> transaction;
		try {	
			transaction = walletService.getAllTransactions(mobileNo);
		} catch (InvalidInputException e) {
			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("transactionSuccessPage", "transactions1", transaction);
	}
	
	
	public String getMobileNo(@RequestParam(value="mobileNo",required=true)String mobileNo) {
		return mobileNo;
	}
}
