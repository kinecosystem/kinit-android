package org.kinecosystem.kinit

import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.Scheduler

interface CoreComponentsProvider : DataStoreProvider {

    fun userRepo(): UserRepository

    fun questionnaireRepo(): QuestionnaireRepository

    fun offersRepo(): OffersRepository

    fun services(): ServicesProvider

    fun analytics(): Analytics

    fun scheduler(): Scheduler

    fun notificationPublisher(): NotificationPublisher
}

