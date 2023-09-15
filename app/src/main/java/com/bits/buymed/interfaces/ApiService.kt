package com.bits.buymed.interfaces

import com.bits.buymed.model.CartItem
import com.bits.buymed.model.LoginRequest
import com.bits.buymed.model.SignUpRequest
import com.bits.buymed.model.StockItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/register/")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<Void>

    @POST("/api/login/")
    fun login(@Body loginRequest: LoginRequest): Call<Void>

    @GET("/api/stockitems/")
    fun getStockItemsByCategory(@Query("category") category: String): Call<List<StockItem>>

    @POST("/api/cartitem/add/")
    fun addToCart(@Body requestBody: CartItem): Call<Void>

    @GET("/api/cartitems/")
    fun getCartItems(): Call<List<CartItem>>
}