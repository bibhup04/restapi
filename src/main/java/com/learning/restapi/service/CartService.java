package com.learning.restapi.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.learning.restapi.entity.Cart;
import com.learning.restapi.entity.Cycle;
import com.learning.restapi.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CycleService cycleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;


    public Cart addToCart(int count, long brandId, String username) {

        Cart cart = new Cart();
        cart.setCycle(cycleService.findByIdOrThrow404(brandId));
        cart.setQuantity(count);
        cart.setUser(userService.getByName(username).get());
        cart.setBooked(false);

        return cartRepository.save(cart);
    }

    public List<Cart> getAllCartItems() {
        List<Cart> cycleInCart = new ArrayList<>();
        cycleInCart = cartRepository.findAll();
        System.out.println(cycleInCart);
        return cartRepository.findAll();
    }

    public void removeCartItem(long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public List<Cart> getCyclesByUserId(long userId) {
        var listFromDB = cartRepository.findByUser_Id(userId);
         var cycleList = new ArrayList<Cart>();
         listFromDB.forEach(cycleList::add);
         return cycleList;
    }

    public void markCartItemsAsBooked(long userId) {
        List<Cart> cartItemsToRent = cartRepository.findByUser_IdAndBookedFalse(userId);

        for (Cart cartItem : cartItemsToRent) {
            cartItem.setBooked(true);
            cycleService.borrowCycle(cartItem.getCycle().getId(), cartItem.getQuantity());
            cartRepository.save(cartItem);
        }
    }

    public void returnCycle(long cartId) {
   
        Cart cartItem = cartRepository.findById(cartId);
                
        cartItem.setReturned(true);

        cycleService.returnCycle(cartItem.getCycle().getId(), cartItem.getQuantity());
        cartRepository.save(cartItem);
        
        if(!checkRemainingCycle(cartItem.getUser().getId())){
           // transactionService.endTransaction()
        }

    }

    public boolean checkRemainingCycle(long userId) {
        List<Cart> cartItems = cartRepository.findByUser_Id(userId);
    
        for (Cart cartItem : cartItems) {
            if (!cartItem.isReturned()) {
                return true;
            }
        }

        return false;
    }
    
    
}
