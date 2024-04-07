package com.example.rentapp.service;

import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.entity.ProductCategory;
import com.example.rentapp.model.entity.SearchedProductSubscription;
import com.example.rentapp.model.request.CreateSearchedProductSubscriptionParam;
import com.example.rentapp.repository.SearchedProductSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchedProductSubscriptionService {

    private final ProductCategoryService productCategoryService;
    private final SearchedProductSubscriptionRepository searchedProductSubscriptionRepository;
    private final EmailService emailService;

    public void create(CreateSearchedProductSubscriptionParam param){
        ProductCategory category = productCategoryService.findById(param.getCategoryId());
        SearchedProductSubscription subscription = new SearchedProductSubscription();
        subscription.setCategory(category);
        subscription.setMinPrice(param.getMinPrice());
        subscription.setMaxPrice(param.getMaxPrice());
        subscription.setAvailableFrom(param.getAvailableFrom());
        subscription.setAvailableUntil(param.getAvailableUntil());
        searchedProductSubscriptionRepository.save(subscription);
    }

    @Async
    public void sendNotifications(Product product){
        List<SearchedProductSubscription> subscriptions = searchedProductSubscriptionRepository.findProductMatches(
                product.getProductPrice().getBasicPrice(),
                product.getAvailableFrom(),
                product.getAvailableUntil(),
                product.getCategory().getId());

        String productLink = "http://localhost:8888/price?id=" + product.getId();
        String emailText = "Match for your product has appeared. \n Click link to see details: " + productLink;
        subscriptions.forEach((subscription)->emailService.sendMail(emailText,subscription.getNotificationTarget()));
    }

}
