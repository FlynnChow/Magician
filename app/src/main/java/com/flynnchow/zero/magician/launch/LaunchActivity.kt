package com.flynnchow.zero.magician.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityLaunchBinding
import com.flynnchow.zero.magician.databinding.ActivityMainBinding
import com.flynnchow.zero.magician.gallery.view.GalleryActivity
import com.flynnchow.zero.magician.main.MainActivity

class LaunchActivity : BindingActivity<ActivityLaunchBinding>(R.layout.activity_launch) {
    override fun onInitView() {
        mBinding.gallery.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }
}