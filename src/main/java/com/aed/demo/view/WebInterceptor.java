package com.aed.demo.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aed.demo.entity.Payment;
import com.aed.demo.entity.User;
import com.aed.demo.repositories.PaymentRepository;
import com.aed.demo.repositories.UserRepository;
import com.aed.demo.stripe.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Service
public class WebInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    PaymentRepository paymentRepo;

    @Autowired
    UserRepository userRepo;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String API_PUBLIC_KEY;

    @Value("${STRIPE_SECRET_KEY}")
    private String API_SECRET_KEY;

    private StripeService stripeService;

    public WebInterceptor(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

//        // TODO Auto-generated method stub
//        System.out.println("MPIKE STON INTERCEPTOR");
//        System.out.println("user repo " + userRepo);
//        System.out.println("api key " + API_PUBLIC_KEY);
//
//        Stripe.apiKey = API_SECRET_KEY;
//
//        User loggedinUser = getLoggedInUser();
//
//        // Optional<PaymentRepository> paymentOptional =
//        // paymentRepo.findByUserUserId(loggedinUser.getUserId());
//
//        List<Payment> payments = paymentRepo.findAll();
//        Payment paymentFound = null;
//
//        // Payment payment = (Payment) paymentOptional.get();
//        for (Payment payment : payments) {
//            if (payment.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
//                paymentFound = payment;
//                break;
//            }
// 
//        }
//
//        Subscription subscription = null;
//        try {
//
//            subscription = Subscription.retrieve(paymentFound.getSubscriptionName());
//        } catch (StripeException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        String status = subscription.getStatus();
//
//        System.out.println("stauts: " + status);
//
//        if (status.equalsIgnoreCase("canceled") || status.equalsIgnoreCase("unpaid")) {
//            
//            //na kanei to isActive tou masterClinic 3 kai twn upoleipwn tou nosokomeio 0
//            
//            userRepo.save(loggedinUser);
//        }
//
//        System.out.println("redirect is active " + loggedinUser.getIsActive());
//        if (loggedinUser.getIsActive() == 2) {
//            System.out.println("REDIRECTING");
//            response.sendRedirect("/setUserCredentials");
//        } else if (loggedinUser.getIsActive() == 3) {
//            response.sendRedirect("/plans");
//        }

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }

    public User getLoggedInUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();

        } else {
            username = principal.toString();
        }

        List<User> users = userRepo.findAll();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }

        }
        return null;
    }
}