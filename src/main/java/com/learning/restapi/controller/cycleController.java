package com.learning.restapi.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.learning.restapi.business.LoginBody;
import com.learning.restapi.entity.Cart;
import com.learning.restapi.entity.Cycle;
import com.learning.restapi.entity.User;
import com.learning.restapi.entity.addNewCycle;
import com.learning.restapi.service.CartService;
import com.learning.restapi.service.CycleService;
import com.learning.restapi.service.DomainUserService;
import com.learning.restapi.service.RegistrationForm;
import com.learning.restapi.service.TransactionService;
import com.learning.restapi.service.UserService;


@RestController
@RequestMapping("/api/cycle")
@CrossOrigin(origins = "http://localhost:4200")
public class cycleController {

    @Autowired
    private LoginBody loginBody;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private CartService cartService;

    @Autowired
    private DomainUserService domainUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthenticationManager authentication;


    @GetMapping("/health")
    public String checkhealth() {
        return "healthy";
    }


     @GetMapping("/list")
    public ResponseEntity<List<Cycle>> listAvailableCycles() {

        var allCycles = cycleService.listCycles();
        return ResponseEntity.status(HttpStatus.OK).body(allCycles);
    }


    @PostMapping("/{id}/borrow")
    public ResponseEntity<String> borrowCyclePostMapping(@PathVariable long id, @RequestBody Map<String, Integer> requestData) {
        int count = requestData.getOrDefault("count", 1);
        cycleService.borrowCycle(id, count);
        return ResponseEntity.status(HttpStatus.OK).body("Cycle borrowed successfully");
    }

    @PostMapping("/{id}/cart")
    public ResponseEntity<String> cartCyclePostMapping(@PathVariable long id, @RequestBody Map<String, Integer> requestData) {
        int count = requestData.getOrDefault("count", 1);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        cartService.addToCart(count, id, username);

        return ResponseEntity.status(HttpStatus.OK).body("Cycle added to cart successfully");
    }

    @GetMapping("/cart")
    public ResponseEntity<List<Cart>> listCyclesInCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long userId = userService.getByName(username).get().getId();
        System.out.println(userId);
        var allCyclesInCart = cartService.getCyclesByUserId(userId);
        System.out.println(allCyclesInCart);
        return ResponseEntity.status(HttpStatus.OK).body(allCyclesInCart);
    }

    @PostMapping("/checkOut")
    public ResponseEntity<String> rentCycles(@RequestBody Map<String, Integer> requestData){
        double totalAmount = requestData.getOrDefault("totalAmount", 0);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        transactionService.rentCycles(username,totalAmount);
        return ResponseEntity.status(HttpStatus.OK).body("Cycle rented successfully");
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnRentedCycle(@RequestBody Map<String, Integer> requestData) {
        long cartId = requestData.getOrDefault("cartId", 1);
        cartService.returnCycle(cartId);
        return ResponseEntity.status(HttpStatus.OK).body("Cycle returned successfully");
    }


    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnCycle(@PathVariable long id, @RequestBody Map<String, Integer> requestData) {
        int count = requestData.getOrDefault("count", 1); // Default value is 1 if "count" is not provided
        cycleService.returnCycle(id, count);
        return ResponseEntity.status(HttpStatus.OK).body("Cycle returned successfully");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCycle(@RequestBody addNewCycle addCycle){
        System.out.println(addCycle.getBrand());
        System.out.println(addCycle.getStock());
       if((addCycle.getBrand() != null) && addCycle.getStock() != 0)
            cycleService.addCycle(addCycle);

        return ResponseEntity.status(HttpStatus.OK).body("Cycle added successfully");
    }


    @PostMapping("/{id}/restock")
    public ResponseEntity<String> restockCycle(@PathVariable long id, @RequestBody Map<String, Integer> requestData) {
        int count = requestData.getOrDefault("count", 1);
        cycleService.restockBy(id, count);
        return ResponseEntity.status(HttpStatus.OK).body("Cycle restocked successfully");
    }

    @GetMapping("/")
    @ResponseBody
    public List<Cycle> listUsers() {
        return cycleService.listCycles();
    }

 

 

    @PostMapping("/post/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationForm registrationForm) {
        try {
            registrationForm.getUsername();
            if (domainUserService.getByName(registrationForm.getUsername()) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }
            if(!registrationForm.getPassword().equals(registrationForm.getRepeatPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password doesnot match");
            }

            System.out.println(domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword(), registrationForm.getRole()));
           
            return ResponseEntity.ok("User registered successfully");
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }

    }

 
    
}
