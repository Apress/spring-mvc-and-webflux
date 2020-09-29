package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.Book;
import com.apress.prospringmvc.bookstore.domain.BookSearchCriteria;
import com.apress.prospringmvc.bookstore.domain.Cart;
import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.domain.Order;
import com.apress.prospringmvc.bookstore.domain.OrderDetail;
import com.apress.prospringmvc.bookstore.repository.BookRepository;
import com.apress.prospringmvc.bookstore.repository.CategoryRepository;
import com.apress.prospringmvc.bookstore.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author Marten Deinum
 *
 * @see BookstoreService
 */
@Service
@Transactional(readOnly = true)
class BookstoreServiceImpl implements BookstoreService {

	private static final int RANDOM_BOOKS = 2;

	private final BookRepository bookRepository;
	private final OrderRepository orderRepository;
	private final CategoryRepository categoryRepository;

	BookstoreServiceImpl(BookRepository bookRepository, OrderRepository orderRepository, CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.orderRepository = orderRepository;
		this.categoryRepository = categoryRepository;
	}

  @Override
	public List<Book> findBooksByCategory(Category category) {
		return this.bookRepository.findByCategory(category);
	}

	@Override
	public Category findCategory(long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public List<Book> findRandomBooks() {
		PageRequest request = PageRequest.of(0, RANDOM_BOOKS);
		return this.bookRepository.findRandom(request);
	}

	@Override
	@Transactional(readOnly = false)
	public Order store(Order order) {
		return this.orderRepository.save(order);
	}

	@Override
	public List<Book> findBooks(BookSearchCriteria bookSearchCriteria) {

		Specification<Book> withTitle = (Specification<Book>) (r, q, cb) -> cb.like(cb.upper(r.get("title")), "%" + bookSearchCriteria.getTitle() + "%");
		Specification<Book> withCategory = (Specification<Book>) (r, q, cb) -> cb.equal(r.<Category> get("category"), bookSearchCriteria.getCategory());

		Specification<Book> books = null;

		if (bookSearchCriteria.getTitle() != null) {
			books = Specification.where(withTitle);
		}

		if (bookSearchCriteria.getCategory() != null) {
			if (books == null) {
				books = Specification.where(withCategory);
			} else {
				books = books.and(withCategory);
			}
		}

		return books == null ? this.bookRepository.findAll() : this.bookRepository.findAll(books);
	}

	@Override
	public Book findBook(long id) {
		return this.bookRepository.findById(id).orElse(null);
	}

	@Override
	public List<Order> findOrdersForAccount(Account account) {
		return this.orderRepository.findByAccount(account);
	}

	@Override
	@Transactional(readOnly = false)
	public Order createOrder(Cart cart, Account customer) {
		var order = new Order(customer);
		for (Entry<Book, Integer> line : cart.getBooks().entrySet()) {
			order.addOrderDetail(new OrderDetail(line.getKey(), line.getValue()));
		}
		order.setOrderDate(new Date());
		return order;
	}

	@Override
	public Optional<Order> findOrder(long id) {
		return this.orderRepository.findById(id);
	}

	@Override
	public Iterable<Category> findAllCategories() {
		return this.categoryRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void addBook(Book book) {
		this.bookRepository.save(book);
	}
}
