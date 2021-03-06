package com.vku.marshallelecom.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.vku.marshallelecom.AppConfig
import com.vku.marshallelecom.MainApplication
import com.vku.marshallelecom.R
import com.vku.marshallelecom.activities.MainActivity
import com.vku.marshallelecom.client.AuthClient
import com.vku.marshallelecom.model.User
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.layoutPassword
import kotlinx.android.synthetic.main.fragment_sign_up.view.layoutUsername
import kotlinx.android.synthetic.main.fragment_sign_up.view.txtPassword
import kotlinx.android.synthetic.main.fragment_sign_up.view.txtUsername
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {
    lateinit var rootView : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_sign_up, container,false)
        rootView.txtSignIn.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.frameAuthContainer, SignInFragment()).commit()
        }

        rootView.txtUsername.doOnTextChanged { text, start, before, count ->
            if(!rootView.txtUsername.text.toString().isEmpty()) {
                layoutUsername.isErrorEnabled = false
            }
        }
        rootView.txtEmail.doOnTextChanged { text, start, before, count ->
            if(isValidEmail(rootView.txtEmail.text.toString())) {
                layoutEmail.isErrorEnabled = false
            }
        }
        rootView.txtPassword.doOnTextChanged { text, start, before, count ->
            if(rootView.txtPassword.text.toString().length>=6) {
                layoutPassword.isErrorEnabled = false
            }
        }
        rootView.txtConfirm.doOnTextChanged { text, start, before, count ->
            if(!rootView.txtConfirm.text.toString().isEmpty()||rootView.txtConfirm.text.toString().equals(rootView.txtPassword.text.toString())) {
                layoutConfirm.isErrorEnabled = false
            }
        }
        rootView.btnSignUp.setOnClickListener{
            if(validate()) {
                register()
            }
        }
        return rootView
    }

    private fun validate() : Boolean {
        if(rootView.txtUsername.text.toString().isEmpty()) {
            rootView.layoutUsername.isErrorEnabled = true
            rootView.layoutUsername.error = "B???n c???n nh???p t??n ????ng nh???p!"
            return false
        }
        if(!isValidEmail(rootView.txtEmail.text.toString())) {
            rootView.layoutEmail.isErrorEnabled = true
            rootView.layoutEmail.error = "?????a ch??? email kh??ng h???p l???!"
            return false
        }
        if(rootView.txtPassword.text.toString().length<6) {
            rootView.layoutPassword.isErrorEnabled = true
            rootView.layoutPassword.error = "M???t kh???u ph???i c?? ??t nh???t 6 k?? t???!"
            return false
        }
        if(rootView.txtConfirm.text.toString().isEmpty()||!rootView.txtConfirm.text.toString().equals(rootView.txtPassword.text.toString())) {
            rootView.layoutConfirm.isErrorEnabled = true
            rootView.layoutConfirm.error = "Nh???p l???i m???t kh???u kh??ng kh???p!"
            return false
        }
        return true
    }
    private fun isValidEmail(target : String) : Boolean {
        if(target.isEmpty()) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private fun register() {
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()

        val username = rootView.txtUsername.text.toString()
        val email = rootView.txtEmail.text.toString()
        val password = rootView.txtPassword.text.toString()
        val password_confirmation = rootView.txtConfirm.text.toString()
        val authClient: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
        val registerService: Call<JsonObject> = authClient.register(username, email, password, password_confirmation)
        registerService.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.hide()
                pDialog = SweetAlertDialog(this@SignUpFragment.context, SweetAlertDialog.ERROR_TYPE)
                pDialog.setTitleText("L???i....")
                    .setContentText(t.localizedMessage)
                    .show();
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                pDialog.dismiss()
                val lObject = response.body()!!

                if(response.isSuccessful&&lObject.get("success").asBoolean) {
                    val token = lObject.get("token").asString
                    val user = Gson().fromJson(lObject.get("user").asJsonObject, User::class.java)

                    val editor = MainApplication.userSharedPreferences().edit()
                    editor.putString("token", token)
                    editor.putString("username", user.username)
                    editor.putString("email", user.email)
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()
                    pDialog = SweetAlertDialog(this@SignUpFragment.context, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Th??nh c??ng!")
                        .setContentText("T??i kho???n c???a b???n ???? ???????c kh???i t???o")
                        .show();
                    pDialog.setConfirmClickListener {
                        this@SignUpFragment.startActivity(Intent(this@SignUpFragment.context, MainActivity::class.java))
                        this@SignUpFragment.activity?.finish()
                    }

                } else {
                    pDialog = SweetAlertDialog(this@SignUpFragment.context, SweetAlertDialog.ERROR_TYPE)
                    pDialog.setTitleText("L???i...")
                        .setContentText(lObject.get("message").asString)
                        .show();
                }
            }

        })
    }
}