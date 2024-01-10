package com.bookrealm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bookrealm.model.Book;
import com.bookrealm.model.Cart;
import com.bookrealm.model.User;
import com.bookrealm.repository.BookRepository;
import com.bookrealm.repository.CartRepository;
import com.bookrealm.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;

	// User에게 장바구니 생성
	public void createCart(User user) {
		Cart cart = Cart.createCart(user);
		cartRepository.save(cart);
	}
	
	// 장바구니에 book 추가
	@Transactional
	public Cart addCart(User user, Book book, int purchase) {
		
		
		Cart cart = cartRepository.findByIdAndBookId(user.getId(), book.getId());
		
		// 상품이 장바구니에 존재하지 않는다면 카트상품 생성
		if (cart == null) {
			cart = Cart.createCartBook(user, book, purchase);
			cartRepository.save(cart);
		}
		
		// 상품이 장바구니에 이미 존재한다면 수량만 증가
		else {
			cart.addPurchase(purchase);
		}
		return cartRepository.save(cart);
	}
	
	// 장바구니 조회하기
	public List<Cart> userCartView(Cart cart) {
		List<Cart> cart_books = cartRepository.findAll();
		List<Cart> user_books = new ArrayList<>();
		
		for (Cart cart_book : cart_books) {
            if (cart_book.getId() == cart.getId()) {
            	cart_books.add(cart_book);
            }
        }

        return user_books;
	}

	public Cart deleteBookFromCart(User user, Book book){
		Cart cart = cartRepository.findByIdAndBookId(user.getId(),book.getId());

		if(cart != null){
			cartRepository.delete(cart);
		}

		return cart;
	}

}