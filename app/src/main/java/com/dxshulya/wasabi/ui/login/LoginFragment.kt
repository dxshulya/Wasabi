package com.dxshulya.wasabi.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.LoginFragmentBinding
import com.dxshulya.wasabi.util.Util.Companion.toObservable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: LoginFragmentBinding
    private lateinit var tilEmail: TextInputLayout
    private lateinit var edtEmail: TextInputEditText

    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText

    private lateinit var nextButton: Button

    private lateinit var linkToRegistrationFragment: TextView

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun initUIs() {
        tilEmail = binding.tilEmail
        edtEmail = binding.edtEmail
        tilPassword = binding.tilPassword
        edtPassword = binding.edtPassword
        nextButton = binding.nextButton
        linkToRegistrationFragment = binding.linkToLoginFragment
    }

    private fun initButton() {
        nextButton.setOnClickListener {
            if (!validateEmail() || !validatePassword()) {
                //clearStore()
            }
            else viewModel.postLogin()

            Log.e("EMAIL", viewModel.sharedPreference.email)
            Log.e("NAME", viewModel.sharedPreference.name)
            Log.e("PASS", viewModel.sharedPreference.password)
            Log.e("TOKEN", viewModel.sharedPreference.token)

        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun validateEmail(): Boolean {
        return (edtEmail.text.toString().validator()
            .validEmail().addErrorCallback {
                tilEmail.error = getString(R.string.error_email)
            }
            .maxLength(50).addErrorCallback {
                tilEmail.error = getString(R.string.error_max)
            }
            .nonEmpty().addErrorCallback {
                tilEmail.error = getString(R.string.error_empty)
            }.check())
    }

    private fun validatePassword(): Boolean {
        return (edtPassword.text.toString().validator()
            .maxLength(50).addErrorCallback {
                tilPassword.error = getString(R.string.error_max)
            }
            .minLength(7).addErrorCallback {
                tilPassword.error = getString(R.string.error_min)
            }
            .nonEmpty().addErrorCallback {
                tilPassword.error = getString(R.string.error_empty)
            }.check())
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
                //clearStore()
            }
            else {
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
        subscribeFields()
        checkFields()
        toRegistrationFragment()
        initButton()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

}