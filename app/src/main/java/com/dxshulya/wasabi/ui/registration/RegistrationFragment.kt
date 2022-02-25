package com.dxshulya.wasabi.ui.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.RegistrationFragmentBinding
import com.dxshulya.wasabi.util.Util.Companion.toObservable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

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
        subscribeFields()
        checkFields()
        linkToLoginFragment()
        initButton()
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
    }

    private fun showErrorWindow(message: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Упс!")
                .setMessage(message)
                .setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
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
            if (it.statusCode == 400 || it.token == "") {
                showErrorWindow(it.message.toString())
                clearStore()
            } else {
                this.findNavController().navigate(R.id.action_registrationFragment_to_taskFragment)
            }
        }
    }

    private fun clearStore() {
        viewModel.apply {
            sharedPreference.email = ""
            sharedPreference.name = ""
            sharedPreference.password = ""
            sharedPreference.token = ""
        }
    }

    private fun linkToLoginFragment() {
        linkToLoginFragment.setOnClickListener {
            this.findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    private fun initButton() {
        nextButton.setOnClickListener {
            if (!validateEmail() || !validateName() || !validatePassword()) {
                Log.e("VALIDATE_ERROR", "")
            } else viewModel.postRegistration()
        }
    }

    private fun validateEmail(): Boolean {
        return (edtEmail.text.toString().validator()
            .validEmail().addErrorCallback {
                edtEmail.error = "Некорректный адрес!"
            }
            .maxLength(50).addErrorCallback {
                edtEmail.error = "Максимум 50 символов!"
            }
            .nonEmpty().addErrorCallback {
                edtEmail.error = "Пустое поле!"
            }.check())
    }

    private fun validateName(): Boolean {
        return (edtName.text.toString().validator()
            .maxLength(50).addErrorCallback {
                edtName.error = "Максимум 50 символов!"
            }
            .nonEmpty().addErrorCallback {
                edtName.error = "Пустое поле!"
            }.check())
    }

    private fun validatePassword(): Boolean {
        return (edtPassword.text.toString().validator()
            .maxLength(50).addErrorCallback {
                edtPassword.error = "Максимум 50 символов!"
            }
            .nonEmpty().addErrorCallback {
                edtPassword.error = "Пустое поле!"
            }
            .minLength(7).addErrorCallback {
                edtPassword.error = "Минимум 7 символов!"
            }.check())
    }
}