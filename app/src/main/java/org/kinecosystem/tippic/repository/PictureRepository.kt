package org.kinecosystem.tippic.repository

import android.databinding.ObservableField
import org.kinecosystem.tippic.model.Picture
import org.kinecosystem.tippic.model.isValid

class PictureRepository {

    var picture: ObservableField<Picture> = ObservableField()
        private set

    fun updatePicture(picture: Picture?){
        if (picture != null && picture != this.picture.get() && picture.isValid())
            this.picture.set(picture)
    }
}