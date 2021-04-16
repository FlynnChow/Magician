package com.flynnchow.zero.magician.album.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flynnchow.zero.magician.album.view.ClassificationFragment
import com.flynnchow.zero.model.MLPhotoAlbum

class PhotoAlbumAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments:ArrayList<ClassificationFragment> = ArrayList()
    private val albums:ArrayList<MLPhotoAlbum> = ArrayList()
    private val albumMap:HashMap<String,Int> = HashMap()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getTabText(position: Int):String = albums[position].key

    fun insertAlbum(album:MLPhotoAlbum){
        removeAlbum(album)
        val addIndex = itemCount
        fragments.add(ClassificationFragment.createClassificationFragment(album.key))
        albums.add(album)
        albumMap[album.key] = addIndex
        notifyItemInserted(addIndex)
    }

    fun insertAlbum(albums:List<MLPhotoAlbum>){
        for (album in albums){
            insertAlbum(album)
        }
    }

    fun updateAlbum(albums:List<MLPhotoAlbum>,position:Int):Int{
        var newPosition = 0
        val newFragments = ArrayList<ClassificationFragment>()
        val newAlbums = ArrayList<MLPhotoAlbum>()
        val newAlbumMap = HashMap<String,Int>()
        for (index in albums.indices){
            val album = albums[index]
            val oldIndex = albumMap[album.key]
            if (oldIndex != null){
                val fragment = fragments[oldIndex]
                newFragments.add(fragment)
                newAlbums.add(album)
                newAlbumMap[album.key] = index
                if (position == oldIndex){
                    newPosition = index
                }
            }else{
                newFragments.add(ClassificationFragment.createClassificationFragment(album.key))
                newAlbums.add(album)
                newAlbumMap[album.key] = index
            }
        }
        this.fragments.clear()
        this.albums.clear()
        this.albumMap.clear()
        this.fragments.addAll(newFragments)
        this.albums.addAll(newAlbums)
        this.albumMap.putAll(newAlbumMap)
        return newPosition
    }

    fun removeAlbum(albums:List<MLPhotoAlbum>){
        for (album in albums){
            removeAlbum(albums)
        }
    }

    fun removeAlbum(album:MLPhotoAlbum){
        val index = albumMap[album.name]
        if (index != null){
            albums.removeAt(index)
            fragments.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        for (fragment in fragments){
            if (itemId == fragment.hashCode().toLong()){
                return true
            }
        }
        return false
    }
}