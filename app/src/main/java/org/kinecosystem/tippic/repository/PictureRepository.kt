package org.kinecosystem.tippic.repository

import android.databinding.ObservableField
import org.kinecosystem.tippic.model.Picture
import org.kinecosystem.tippic.model.isValid

class PictureRepository {

    var picture = ObservableField<Picture>()
        private set

    var picturesSummery = ObservableField<List<Picture>>(ArrayList())
        private set

    fun updatePicture(picture: Picture?){
        if (picture != null && picture != this.picture.get() && picture.isValid())
            this.picture.set(picture)
    }

    fun updatePicturesSummery(list: List<Picture>?){
        list?.let { this.picturesSummery.set(it) }
    }
}