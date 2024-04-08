package com.dshagapps.tfi_software.data.service

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dshagapps.tfi_software.data.service.ServiceProvider.Companion.PREF_COOKIES
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ServiceProvider(context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Set the connection timeout
        .readTimeout(30, TimeUnit.SECONDS) // Set the read timeout
        .writeTimeout(30, TimeUnit.SECONDS) // Set the write timeout
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )
        .addInterceptor(
            AddCookiesInterceptor(context)
        )
        .addInterceptor(
            ReceivedCookiesInterceptor(context)
        )

    private val builder = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.client(client.build()).build()
        return retrofit.create(serviceClass)
    }

    companion object {
        const val PREF_COOKIES = "PREF_COOKIES"
    }
}

class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(PREF_COOKIES, HashSet()) as HashSet<String>?
            for (header in originalResponse.headers("Set-Cookie")) {
                if (header.startsWith("connect.sid")) {
                    cookies?.add(header)
                }
            }
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(PREF_COOKIES, cookies)
                .apply()
        }
        return originalResponse
    }
}

class AddCookiesInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            .getStringSet(PREF_COOKIES, HashSet()) as HashSet<String>?
        preferences?.forEach { cookie ->
            if (cookie.startsWith("connect.sid")) {
                builder.addHeader("Cookie", cookie)
            }
        }
        return chain.proceed(builder.build())
    }
}