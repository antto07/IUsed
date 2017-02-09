package com.iused.bean;

import java.util.ArrayList;

/**
 * Created by yash on 19-10-2016.
 */
public class DonorResponsesBean {

    public String ProductId;
    public String ProductName;
    public String ProductImage;
    public String count;
    public String Status;
    public String Type;

    public String RequestId;
    public String Username;
    public String UserPhone;
    public String EmotionalMessage;
    public String UsedFor;
    public String Currency;
    public String Condition;
    public String Photo;
    public String Qty;
    public String Amount;
    public String OriginalPrice;
    public String OfferApplied;
    public String OfferTill;
    public String Status_request;
    public String ProductId_request;
    public String ProductName_request;

    private ArrayList<String> arrayList_request_id = null;
    private ArrayList<String> arrayList_user_name = null;
    private ArrayList<String> arrayList_amount = null;
    private ArrayList<String> arrayList_photo = null;
    private ArrayList<String> arrayList_product_name = null;
    private ArrayList<String> arrayList_offer_till = null;
    private ArrayList<String> arrayList_emotional_image = null;
    private ArrayList<String> arrayList_user_phone = null;


    public int int_arr_count=0;


    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getOriginalPrice() {
        return OriginalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        OriginalPrice = originalPrice;
    }

    public String getOfferApplied() {
        return OfferApplied;
    }

    public void setOfferApplied(String offerApplied) {
        OfferApplied = offerApplied;
    }

    public String getOfferTill() {
        return OfferTill;
    }

    public void setOfferTill(String offerTill) {
        OfferTill = offerTill;
    }

    public String getStatus_request() {
        return Status_request;
    }

    public void setStatus_request(String status_request) {
        Status_request = status_request;
    }

    public String getProductId_request() {
        return ProductId_request;
    }

    public void setProductId_request(String productId_request) {
        ProductId_request = productId_request;
    }

    public String getProductName_request() {
        return ProductName_request;
    }

    public void setProductName_request(String productName_request) {
        ProductName_request = productName_request;
    }

    public ArrayList<String> getArrayList_request_id() {
        return arrayList_request_id;
    }

    public void setArrayList_request_id(ArrayList<String> arrayList_request_id) {
        this.arrayList_request_id = arrayList_request_id;
    }

    public ArrayList<String> getArrayList_user_name() {
        return arrayList_user_name;
    }

    public void setArrayList_user_name(ArrayList<String> arrayList_user_name) {
        this.arrayList_user_name = arrayList_user_name;
    }

    public ArrayList<String> getArrayList_amount() {
        return arrayList_amount;
    }

    public void setArrayList_amount(ArrayList<String> arrayList_amount) {
        this.arrayList_amount = arrayList_amount;
    }

    public ArrayList<String> getArrayList_photo() {
        return arrayList_photo;
    }

    public void setArrayList_photo(ArrayList<String> arrayList_photo) {
        this.arrayList_photo = arrayList_photo;
    }

    public ArrayList<String> getArrayList_product_name() {
        return arrayList_product_name;
    }

    public void setArrayList_product_name(ArrayList<String> arrayList_product_name) {
        this.arrayList_product_name = arrayList_product_name;
    }

    public ArrayList<String> getArrayList_offer_till() {
        return arrayList_offer_till;
    }

    public void setArrayList_offer_till(ArrayList<String> arrayList_offer_till) {
        this.arrayList_offer_till = arrayList_offer_till;
    }

    public int getInt_arr_count() {
        return int_arr_count;
    }

    public void setInt_arr_count(int int_arr_count) {
        this.int_arr_count = int_arr_count;
    }

    public ArrayList<String> getArrayList_user_phone() {
        return arrayList_user_phone;
    }

    public void setArrayList_user_phone(ArrayList<String> arrayList_user_phone) {
        this.arrayList_user_phone = arrayList_user_phone;
    }

    public ArrayList<String> getArrayList_emotional_image() {
        return arrayList_emotional_image;
    }

    public void setArrayList_emotional_image(ArrayList<String> arrayList_emotional_image) {
        this.arrayList_emotional_image = arrayList_emotional_image;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getEmotionalMessage() {
        return EmotionalMessage;
    }

    public void setEmotionalMessage(String emotionalMessage) {
        EmotionalMessage = emotionalMessage;
    }

    public String getUsedFor() {
        return UsedFor;
    }

    public void setUsedFor(String usedFor) {
        UsedFor = usedFor;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }
}
