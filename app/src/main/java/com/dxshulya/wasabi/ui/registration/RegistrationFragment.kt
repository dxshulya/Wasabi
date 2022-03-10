package com.dxshulya.wasabi.ui.registration

import android.os.Bundle
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
import androidx.paging.ExperimentalPagingApi
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.RegistrationFragmentBinding
import com.dxshulya.wasabi.util.Util.Companion.toObservable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@ExperimentalPagingApi
class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel

    private lateinit var binding: RegistrationFragmentBinding
    private lateinit var tilEmail: TextInputLayout
    private lateinit var edtEmail: TextInputEditText

    private lateinit var tilName: TextInputLayout
    private lateinit var edtName: TextInputEditText

    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText

    private lateinit var nextButton: Button

    private lateinit var linkToLoginFragment: TextView

    private lateinit var progressBar: CircularProgressIndicator

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUis()
        emailFocusListener()
        nameFocusListener()
        passwordFocusListener()
        subscribeFields()
        checkFields()
        linkToLoginFragment()
        initButton()
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun initUis() {
        tilEmail = binding.tilEmail
        edtEmail = binding.edtEmail
        tilPassword = binding.tilPassword
        edtPassword = binding.edtPassword
        tilName = binding.tilName
        edtName = binding.edtName
        nextButton = binding.nextButton
        linkToLoginFragment = binding.linkToLoginFragment
        progressBar = binding.registrationBar
    }

    private fun showErrorWindow(message: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.error_window_title))
                .setMessage(message)
                .setPositiveButton(R.string.error_window_btn) { dialog, _ -> dialog.dismiss() }
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

        val subscribeEdtName = toObservable(edtName)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updateName(it)
            }, { Log.e("name", it.message.toString()) })

        compositeDisposable.add(subscribeEdtName)

        val subscribeEdtPassword = toObservable(edtPassword)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updatePassword(it)
            }, { Log.e("password", it.message.toString()) })

        compositeDisposable.add(subscribeEdtPassword)
    }

    private fun checkFields() {
        viewModel.registrationLiveData.observe(viewLifecycleOwner) {
            if (it.statusCode == 400 || it.statusCode == 401 || it.token == "") {
                showErrorWindow(it.message.toString())
                progressBar.visibility = View.GONE
            } else {
                this.findNavController().navigate(R.id.action_registrationFragment_to_taskFragment)
            }
        }
    }

    private fun linkToLoginFragment() {
        linkToLoginFragment.setOnClickListener {
            this.findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    private fun initButton() {
        nextButton.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        tilEmail.error = validEmail()
        tilName.error = validName()
        tilPassword.error = validPassword()

        val validEmail = tilEmail.error == null
        val validName = tilName.error == null
        val validPassword = tilPassword.error == null

        if(validEmail && validName && validPassword) {
            viewModel.postRegistration()
            progressBar.visibility = View.VISIBLE
        }

        else invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(tilEmail.error != null) message += tilEmail.error
        if(tilName.error != null) message += tilName.error
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

    private fun nameFocusListener() {
        edtName.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                tilName.error = validName()
            }
        }
    }

    private fun validName(): String? {
        val name = edtName.text.toString()
        if(name.length > 50) {
            return getString(R.string.error_max)
        }
        if(name == "") {
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

}