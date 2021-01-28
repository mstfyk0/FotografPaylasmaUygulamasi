package com.mustafayigitkarakoca.fotografpaylasmafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View


class KullaniciActivity : AppCompatActivity() {
    //
    private lateinit var auth: FirebaseAuth
    ////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        auth= FirebaseAuth.getInstance()
        ////
        //
        val guncelKullanici =auth.currentUser
        ////
        //
        if(guncelKullanici!=null){
            //
            val intent = Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
            ////
        }
        ////


    }
    //
    fun girisYap(view: View){
        //
        auth.signInWithEmailAndPassword(editTextEmail.text.toString(),editTextPassword.text.toString()).addOnCompleteListener { task->
            //
            if(task.isSuccessful){
                //
                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldiniz: ${guncelKullanici}",Toast.LENGTH_LONG).show()
                ////
                //
                val intent= Intent(this,HaberlerActivity::class.java)
                startActivity(intent)
                finish()
                ////
            }
            ////
        }.addOnFailureListener{
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
        ////

    }
    ////
    //
    fun kayitOl(view: View){
        //
        val password= editTextPassword.text.toString()
        ////
        //
        val email = editTextEmail.text.toString()
        ////
        //
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            //asenkron çalışıyor
            //
            if(task.isSuccessful){
                //diğer aktiviteye gidelim
                val intent= Intent(this,HaberlerActivity::class.java)
                startActivity(intent)
                finish()
                ////
            }
            ////
        }.addOnFailureListener { exception->
            //
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            ////
        }
        ////

    }
    ////

}