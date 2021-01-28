package com.mustafayigitkarakoca.fotografpaylasmafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {
    //
    private lateinit var auth : FirebaseAuth
    ////
    //
    private lateinit var database :FirebaseFirestore
    ////
    //
    var postListesi = ArrayList<Post>()
    ////
    //
    private lateinit var recyclerViewAdapter: RecyclerAdapter
    ////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)
        //
        auth= FirebaseAuth.getInstance()
        ////
        //
        database = FirebaseFirestore.getInstance()
        ////
        //
        verileriAl()
        ////
        //
        var layoutManager  = LinearLayoutManager(this,)
        recyclerViewHaberler.layoutManager = layoutManager
        ////
        //
        recyclerViewAdapter = RecyclerAdapter(postListesi)
        recyclerViewHaberler.adapter = recyclerViewAdapter
        ////
    }
    //
    fun verileriAl(){
        //
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
            //
            if(error!=null){
                //
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
                ////
            }
            ////
            //
            else{
                //
                if(value!=null){
                    //
                    if (value.isEmpty==false){
                        //
                        val documents = value.documents
                        ////
                        //
                        postListesi.clear()
                        ////
                        //
                        for(document in documents){
                            //
                            val kullaniciEmail = document.get("kullaniciemail") as String
                            ////
                            //
                            val kullaniciYorumu = document.get("kullaniciyorum") as String
                            ////
                            //
                            val kullaniciGorsel = document.get("gorselurl") as String
                            ////
                            //
                            val indirilenPost = Post(kullaniciEmail,kullaniciYorumu,kullaniciGorsel)
                            postListesi.add(indirilenPost)
                            ////
                        }
                        ////
                        //
                        recyclerViewAdapter.notifyDataSetChanged()
                        ////
                    }
                    ////
                }
                ////
            }
            ////
        }
        ////

    }
    ////

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_cikisyap_yeniekle,menu)
        ////
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //
        if(item.itemId==R.id.menuFotografPaylas){
            //fotoğraf paylaşma aktivitesine gidilecek
            val intent = Intent(this,FotografPaylasma::class.java)
            startActivity(intent)
            ////
        }
        ////
        //
        else if(item.itemId==R.id.menuCikisYap){
            //
            auth.signOut()
            ////
            //
            val intent = Intent(this,KullaniciActivity::class.java)
            startActivity(intent)
            finish()
            ////


        }
        ////



        return super.onOptionsItemSelected(item)
    }
    ////

}