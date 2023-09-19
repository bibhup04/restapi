package com.learning.restapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
        

        return cartRepository.save(cart);
    }

    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }

    public void removeCartItem(long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public List<Cycle> getCyclesByUserId(long userId) {
        return cartRepository.findByUser_Id(userId);
    }

    
}
