package com.flynnchow.zero.magician.store

import android.os.Bundle
import com.flynnchow.zero.common.fragment.BindingFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentStoreBinding
import com.google.android.material.transition.platform.Hold
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough

class StoreFragment : BindingFragment<FragmentStoreBinding>(R.layout.fragment_store) {
    override fun onInitView() {

    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }
}