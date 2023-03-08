package com.ladecentro.dagger

import com.google.gson.GsonBuilder
import com.ladecentro.api.AddressRequest
import com.ladecentro.api.AuthRequest
import com.ladecentro.api.ProfileRequest
import com.ladecentro.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthAPI(retrofit: Retrofit) : AuthRequest {
        return retrofit.create(AuthRequest::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileAPI(retrofit: Retrofit) : ProfileRequest {
        return retrofit.create(ProfileRequest::class.java)
    }

    @Provides
    @Singleton
    fun provideAddressAPI(retrofit: Retrofit) : AddressRequest {
        return retrofit.create(AddressRequest::class.java)
    }
}