package com.apress.prospringmvc.bookstore.repository;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for working with {@link Order} domain objects
 *  
 * @author Marten Deinum
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find the orders for the given {@link Account}.
     * @param account the account
     * @return list of orders for the account, never <code>null</code>
     */
    List<Order> findByAccount(Account account);

}
