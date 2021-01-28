package com.mustafayigitkarakoca.fotografpaylasmafirebase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.util.*

class FotografPaylasma : AppCompatActivity() {
    //
    var secilenGorsel : Uri?= null
    ////
    //
    var secilenBitMap :Bitmap?=null
    ////
    //
    private lateinit var storage : FirebaseStorage
    ////
    //
    private lateinit var auth: FirebaseAuth
    ////
    //
    private lateinit var database: FirebaseFirestore
    ////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
    }
    //
    fun gorselSec(view: View){
        //
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            //izin alınmamış
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            ////
        }
        ////
        //
        else{
            //izin alınmış
            val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
            ////
        }
        ////
    }
    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //
        if(requestCode==1){
            //
            if(grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
                ////
            }
            ////
        }
        ////


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    ////

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //
        if(requestCode==2 && resultCode== RESULT_OK && data != null){
            //
            secilenGorsel=data.data
            ////
            //
            if (secilenGorsel!=null){
                //
                if(Build.VERSION.SDK_INT>=28){
                    //
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitMap= ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitMap)
                    ////
                }
                ////
                //
                else{
                    //
                    secilenBitMap=MediaStore.Images.Media.getBitmap(this.contentResolver,
                    secilenGorsel)
                    imageView.setImageBitmap(secilenBitMap)
                    ////
                }
                ////
            }
            ////
        }
        ////

        super.onActivityResult(requestCode, resultCode, data)
    }
    ////
    //
    fun paylas(view: View){
        //depo işlemleri
        val reference = storage.reference
        ////
        //
        val uuid = UUID.randomUUID()
        ////
        //
        val gorselIsmi = "${uuid}.jpg"
        ////
        //UUID universal unique id
        val gorselReference = reference.child("images").child(gorselIsmi)
        //
        if (secilenGorsel!=null){
            //
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                //
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(
                        gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    //
                    val guncelKullaniciEmaili = auth.currentUser!!.email.toString()
                    ////
                    //
                    val kullaniciYorumu = editTextYorum.text.toString()
                    ////
                    //
                    val tarih = Timestamp.now()
                    ////
                    // veritabanı işlemleri
                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("gorselurl",downloadUrl)
                    postHashMap.put("kullaniciemail",guncelKullaniciEmaili)
                    postHashMap.put("kullaniciyorum",kullaniciYorumu)
                    postHashMap.put("tarih",tarih)
                    ////
                    //
                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        //
                        if(it.isSuccessful){
                            finish()
                        }
                        ////
                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                    ////


                }
                ////
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
            ////
        }
        ////

        ////
    }
    ////
}