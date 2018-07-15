package org.kinecosystem.kinit.repository

import org.kinecosystem.kinit.model.spend.Offer

class OffersRepository {
    var offerList: List<Offer> = ArrayList()
        private set

    fun offer(i: Int): Offer {
        return offerList[i]
    }

    fun numOfOffers(): Int {
        return offerList.size
    }

    fun replaceOfferList(newOfferList: List<Offer>) {
        offerList = newOfferList
    }

    fun hasValidOffer(index: Int): Boolean {
        return index >= 0 && index < numOfOffers()
    }
}