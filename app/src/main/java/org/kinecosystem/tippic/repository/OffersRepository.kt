package org.kinecosystem.tippic.repository

import android.databinding.ObservableField
import org.kinecosystem.tippic.model.spend.Offer

class OffersRepository {
    var offers: ObservableField<List<Offer>> = ObservableField(ArrayList())
        private set

    fun offer(index: Int): Offer {
        return offers.get()[index]
    }

    fun offersCount(): Int {
        return offers.get().size
    }

    fun updateOffers(newOffers: List<Offer>?) {
        if (isDifferent(newOffers.orEmpty())) {
            offers.set(newOffers.orEmpty())
        }
    }

    private fun isDifferent(newOffers: List<Offer>): Boolean {
        if (newOffers.size != offersCount()) return true

        for ((index, newOffer) in newOffers.withIndex()) {
            if (newOffer != offer(index))
                return true
        }
        return false
    }

    fun hasOffer(offer: Offer): Boolean {
        return offers.get().contains(offer)
    }

    fun isEmpty() = offersCount() == 0
}