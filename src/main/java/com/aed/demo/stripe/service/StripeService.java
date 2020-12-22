package com.aed.demo.stripe.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Product;
import com.stripe.model.Subscription;

@Service
public class StripeService {

	@Value("${STRIPE_SECRET_KEY}")
	private String API_SECRET_KEY;

	public StripeService() {

	}

	public String createCustomer(String email, String token) {

		String id = null;

		try {
			Stripe.apiKey = API_SECRET_KEY;
			Map<String, Object> customerParams = new HashMap<>();
			customerParams.put("description", "Customer for " + email);
			customerParams.put("email", email);
			// obtained with stripe.js
			customerParams.put("source", token);

			Customer customer = Customer.create(customerParams);
			id = customer.getId();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public String createPlan(String planAmount, String interval, String email, String ambNumber,
			String licensePerAmbPrice, String ambPrice, String userNumber, String licensePerUserPrice,
			String usersPrice, String adminNumber, String licensePerAdminPrice, String adminsPrice, String yearsNumber)
			throws StripeException {
		Stripe.apiKey = API_SECRET_KEY;

		String id = null;

		Map<String, Object> productParams = new HashMap<>();

		productParams.put("name", "LifeTime Subscription from: " + email);

		Product product = Product.create(productParams);

		Map<String, Object> planParams = new HashMap<>();

		planParams.put("amount", Integer.parseInt(planAmount) * 100);
		planParams.put("currency", "eur");
		planParams.put("interval", interval);
		planParams.put("active", true);

		Map<String, String> initialMetadata = new HashMap<>();

		initialMetadata.put("email", email);

		initialMetadata.put("ambulanceLicenses", ambNumber);
		initialMetadata.put("ambulancePricePerLicense", licensePerAmbPrice);
		initialMetadata.put("ambulanceLicensesTotalPrice", ambPrice);

		initialMetadata.put("userLicenses", userNumber);
		initialMetadata.put("userPricePerLicense", licensePerUserPrice);
		initialMetadata.put("userLicensesTotalPrice", usersPrice);

		initialMetadata.put("adminLicenses", adminNumber);
		initialMetadata.put("adminPricePerLicense", licensePerAdminPrice);
		initialMetadata.put("adminLicensesTotalPrice", adminsPrice);

		initialMetadata.put("yearsOfSubscription", yearsNumber);

		planParams.put("metadata", initialMetadata);

		planParams.put("product", product.getId());

		// Create plan

		Plan plan = Plan.create(planParams);
		id = plan.getId();

		return id;
	}

	public String createTrialPlan(String amount) throws StripeException {
		Stripe.apiKey = API_SECRET_KEY;

		Map<String, Object> productParams = new HashMap<>();

		productParams.put("name", "LifeTime Subscription");

		Product product = Product.create(productParams);

		Map<String, Object> planParams = new HashMap<>();
		planParams.put("amount", amount);
		planParams.put("currency", "eur");
		planParams.put("interval", "month");
		planParams.put("active", true);

		planParams.put("product", product.getId());

		// Plan plan =
		Plan.create(planParams);

		return "";
	}

	// public String createSubscription(String customerId, String plan, String
	// coupon, String taxRate, int trialDays) {

	// String subscriptionId = null;

	// try {
	// Stripe.apiKey = API_SECRET_KEY;

	// Map<String, Object> item = new HashMap<>();
	// item.put("plan", plan);

	// Map<String, Object> items = new HashMap<>();
	// items.put("0", item);

	// Map<String, Object> params = new HashMap<>();
	// params.put("customer", customerId);
	// params.put("items", items);
	// params.put("default_tax_rates", taxRate);
	// params.put("trial_period_days", trialDays);

	// if (!coupon.isEmpty()) {
	// params.put("coupon", coupon);
	// }

	// Subscription subscription = Subscription.create(params);

	// subscriptionId = subscription.getId();
	// System.out.println(subscriptionId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return subscriptionId;
	// }

	public String createSubscription(String customerId, String plan, String coupon, int trialDays) {

		String subscriptionId = null;

		try {
			Stripe.apiKey = API_SECRET_KEY;

			Map<String, Object> item = new HashMap<>();
			item.put("plan", plan);

			Map<String, Object> items = new HashMap<>();
			items.put("0", item);

			Map<String, Object> params = new HashMap<>();
			params.put("customer", customerId);
			params.put("items", items);

			if (!coupon.isEmpty()) {
				params.put("coupon", coupon);
			}

			// params.put("default_tax_rates", taxRate);
			params.put("trial_period_days", trialDays);

			Subscription subscription = Subscription.create(params);

			subscriptionId = subscription.getId();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return subscriptionId;
	}

	public boolean cancelSubscription(String subscriptionId) {

		boolean subscriptionStatus;

		try {
			Subscription subscription = Subscription.retrieve(subscriptionId);
			subscription.cancel();
			subscriptionStatus = true;
		} catch (Exception e) {
			e.printStackTrace();
			subscriptionStatus = false;
		}
		return subscriptionStatus;
	}

	public Coupon retriveCoupon(String code) {
		try {
			Stripe.apiKey = API_SECRET_KEY;
			return Coupon.retrieve(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// public String createCharge(String email, String token, int amount) {

	// String chargeId = null;

	// try {
	// Stripe.apiKey = API_SECRET_KEY;

	// Map<String, Object> chargeParams = new HashMap<>();
	// chargeParams.put("description", "Charge for " + email);
	// chargeParams.put("currency", "usd");
	// chargeParams.put("amount", amount);
	// chargeParams.put("source", token);

	// Charge charge = Charge.create(chargeParams);

	// chargeId = charge.getId();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return chargeId;
	// }

}
