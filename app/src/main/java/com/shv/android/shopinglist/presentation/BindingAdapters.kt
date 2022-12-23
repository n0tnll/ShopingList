package com.shv.android.shopinglist.presentation

import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shv.android.shopinglist.R

@BindingAdapter("numberAsString")
fun bindNumberAsString(textInputEditText: TextInputEditText, number: Int) {
    textInputEditText.setText(number.toString())
}
@BindingAdapter("setErrorName")
fun bindSetErrorName(textInputLayout: TextInputLayout, viewModel: ShopItemViewModel) {
    val editText = textInputLayout.editText as TextInputEditText
    editText.doOnTextChanged { text, _, _, _ ->
        text?.let {
            if (text.isBlank()) {
                textInputLayout.error = textInputLayout.context.getString(R.string.error_input_name)
            } else {
                viewModel.resetErrorInputName()
                textInputLayout.error = null
            }
        }
    }
}

@BindingAdapter("setErrorCount")
fun bindSetErrorCount(textInputLayout: TextInputLayout, viewModel: ShopItemViewModel) {
    val editText = textInputLayout.editText as TextInputEditText
    editText.doOnTextChanged { text, _, _, count ->
        text?.let {
            if (text.isNotBlank()) {
                val txtCount = text.toString().toInt()
                if (txtCount <= 0)
                    textInputLayout.error =
                        textInputLayout.context.getString(R.string.error_input_count_zero)
                else if (count == 0)
                    textInputLayout.error =
                        textInputLayout.context.getString(R.string.error_input_count)
                else {
                    viewModel.resetErrorInputCount()
                    textInputLayout.error = null
                }
            } else {
                textInputLayout.error =
                    textInputLayout.context.getString(R.string.error_input_count)
            }
        }
    }
}