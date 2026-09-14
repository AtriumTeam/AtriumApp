package ir.atrium.core.common.platform

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlatformModule {

    @Provides
    @Singleton
    fun providePushProvider(): PushProvider = NoOpPushProvider()

    @Provides
    @Singleton
    fun provideCrashReporter(): CrashReporter = LogcatCrashReporter()

    @Provides
    @Singleton
    fun provideIntegrityChecker(): IntegrityChecker = AlwaysPassIntegrityChecker()
}
