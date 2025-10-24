package com.fund.group09.quanlyngansach.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fund.group09.quanlyngansach.Model.Order;
import com.fund.group09.quanlyngansach.Repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);
        if (order != null) {
            order.setOrderItems(orderDetails.getOrderItems());
            order.setTotalAmount(orderDetails.getTotalAmount());
            return orderRepository.save(order);
        }
        return null;
    }
}