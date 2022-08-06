package com.anggitdev.core.di

import androidx.room.Room
import com.anggitdev.core.BuildConfig
import com.anggitdev.core.data.UserGithubRepository
import com.anggitdev.core.data.local.LocalDataSource
import com.anggitdev.core.data.local.room.UserGithubDatabase
import com.anggitdev.core.data.remote.RemoteDataSource
import com.anggitdev.core.data.remote.network.ApiService
import com.anggitdev.core.domain.repository.IUserGithubRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory {
        get<UserGithubDatabase>().userGithubDao()
    }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("anggitdev".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            UserGithubDatabase::class.java,
            "Github.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single{
        val hostname = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, BuildConfig.KEY1)
            .add(hostname, BuildConfig.KEY2)
            .build()
        val loggingInterceptor =if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor{ chain ->
                chain.proceed(
                    chain.request().newBuilder().addHeader(
                        "Authorization",
                        "token ${BuildConfig.TOKEN}"
                    ).build()
                )
            }
            .certificatePinner(certificatePinner)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IUserGithubRepository> {
        UserGithubRepository(
            get(),
            get()
        )
    }
}