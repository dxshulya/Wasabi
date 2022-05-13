package com.dxshulya.wasabi.ui.login

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.LoginFragmentBinding
import com.dxshulya.wasabi.core.util.Util.Companion.toObservable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: LoginFragmentBinding
    private lateinit var tilEmail: TextInputLayout
    private lateinit var edtEmail: TextInputEditText

    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText

    private lateinit var nextButton: Button

    private lateinit var linkToRegistrationFragment: TextView

    private lateinit var progressBar: CircularProgressIndicator

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun initUIs() {
        tilEmail = binding.tilEmail
        edtEmail = binding.edtEmail
        tilPassword = binding.tilPassword
        edtPassword = binding.edtPassword
        nextButton = binding.nextButton
        linkToRegistrationFragment = binding.linkToLoginFragment
        progressBar = binding.loginBar
    }

    private fun initButton() {
        nextButton.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        tilEmail.error = validEmail()
        tilPassword.error = validPassword()

        val validEmail = tilEmail.error == null
        val validPassword = tilPassword.error == null

        if(validEmail && validPassword) {
            viewModel.postLogin()
            Handler().postDelayed({
                viewModel.getTotalCount()
            }, 2000)
            progressBar.visibility = View.VISIBLE
        }

        else invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(tilEmail.error != null) message += tilEmail.error
        if(tilPassword.error!= null) message += tilPassword.error
    }

    private fun emailFocusListener() {
        edtEmail.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                tilEmail.error = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val email = edtEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.error_email)
        }
        if (email.length > 50) {
            return getString(R.string.error_max)
        }
        if (email == "") {
            return getString(R.string.error_empty)
        }
        return null
    }

    private fun passwordFocusListener() {
        edtPassword.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                tilPassword.error = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val password = edtPassword.text.toString()
        if(password.length > 50) {
            return getString(R.string.error_max)
        }
        if(password.length < 7) {
            return getString(R.string.error_min)
        }
        if (password == "") {
            return getString(R.string.error_empty)
        }
        return null
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun showErrorWindow(message: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.error_window_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.error_window_btn)) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun subscribeFields() {
        val subscribeEdtEmail = toObservable(edtEmail)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updateEmail(it)
            }, { Log.e("email", it.message.toString()) })

        compositeDisposable.add(subscribeEdtEmail)

        val subscribeEdtPassword = toObservable(edtPassword)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updatePassword(it)
            }, { Log.e("password", it.message.toString()) })

        compositeDisposable.add(subscribeEdtPassword)
    }

    private fun checkFields() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            if (it.statusCode == 401 || it.statusCode == 400 || it.token == "") {
                showErrorWindow(it.message.toString())
                progressBar.visibility = View.GONE
            } else {
                toTaskFragment()
            }
        }
    }

    private fun toTaskFragment() {
        this.findNavController()
            .navigate(R.id.action_loginFragment_to_taskFragment)
    }

    private fun toRegistrationFragment() {
        linkToRegistrationFragment.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIs()
        emailFocusListener()
        passwordFocusListener()
        subscribeFields()
        checkFields()
        toRegistrationFragment()
        initButton()
        progressBar.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

}