package com.vladimir_x.easyenglishlearn.di

import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.di.modules.AppModule
import com.vladimir_x.easyenglishlearn.di.modules.DataBaseModule
import com.vladimir_x.easyenglishlearn.di.modules.FragmentBuilder
import com.vladimir_x.easyenglishlearn.di.modules.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class,
        DataBaseModule::class,
        ViewModelModule::class,
        FragmentBuilder::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<App>
}