package com.flynnchow.zero.magician.base.helper

import android.graphics.Typeface
import android.widget.TextView
import com.flynnchow.zero.base.BaseApplication

object TitleFontHelper {
    fun updateTitleFont(view:TextView){
        val assets = BaseApplication.instance.assets
        val typeFace = Typeface.createFromAsset(assets, "fonts/zhanku.ttf");
        view.typeface = typeFace
    }
}