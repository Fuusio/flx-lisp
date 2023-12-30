/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package com.flx.features.repl.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import org.fuusio.api.extension.getColor
import org.fuusio.api.view.ToolBarFragment
import org.fuusio.flx.Flx
import org.fuusio.flx.Program
import org.fuusio.flx.core.error.FlxException
import org.fuusio.flx.core.parser.ParserError
import org.fuusio.flx.core.parser.ParserObserver
import org.fuusio.flx.core.toLiteral
import com.flx.lisp.app.R
import com.flx.lisp.app.databinding.ReplFragmentBinding
import com.flx.features.repl.logic.ExecutionManager
import org.fuusio.flx.core.vm.*
import org.koin.core.inject

class ReplFragment : ToolBarFragment<ReplFragmentBinding>(), VmObserver, ExceptionObserver {

    private val executionManager: ExecutionManager by inject()

    private val inputCache = mutableListOf<String>()
    private var inputIndex = 0
    private lateinit var inputEditText: EditText
    private lateinit var consoleTextView: TextView
    private lateinit var vm: FlxVM

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): ReplFragmentBinding =
        ReplFragmentBinding.inflate(inflater, container, false)

    override fun getMenuRes() = R.menu.repl_fragment_menu

    override fun getToolBar() = binding.toolbar

    override fun onResume() {
        super.onResume()
        showKeyboard(inputEditText, true)

        val activity = requireActivity()
        val androidCtx = executionManager.setupExecutionContext(activity, this, this)
        val flxCtx = androidCtx.parent!!
        flxCtx.program = Program()
        vm = FlxScreenVM(androidCtx)
        vm.start()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        val flxCtx = Flx.ctx as FlxCtx
        flxCtx.exceptionObserver = null
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_reset_vm -> doResetVm()
        R.id.action_clear_repl_console -> doClearConsole()
        R.id.action_previous_input -> doPreviousInput()
        R.id.action_next_input -> doNextInput()
        R.id.action_copy_console_content -> doCopyConsoleContentToClipboard()
        else -> super.onOptionsItemSelected(item)
    }

    private fun doPreviousInput(): Boolean {
        inputEditText.setText(inputCache[--inputIndex])
        inputEditText.setSelection(inputEditText.text.length)
        invalidateOptionsMenu()
        return true
    }

    private fun doNextInput(): Boolean {
        inputEditText.setText(inputCache[++inputIndex])
        inputEditText.setSelection(inputEditText.text.length)
        invalidateOptionsMenu()
        return true
    }

    private fun doClearConsole(): Boolean {
        consoleTextView.visibility = View.GONE
        consoleTextView.text = ""
        return true
    }

    private fun doClearInput() {
        inputEditText.setText("")
    }

    private fun doResetVm(): Boolean {
        doClearConsole()
        vm.reset()
        inputCache.clear()
        inputIndex = 0
        invalidateOptionsMenu()
        return true
    }

    private fun doCopyConsoleContentToClipboard(): Boolean {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Execution result", consoleTextView.text)
        clipboard.setPrimaryClip(clip)
        showSnackbar("Console content copied to clipboard.")
        return true
    }

    private fun doHandleInput(input: String) {
        doClearInput()
        cacheInput(input)
        displayText(input, R.color.repl_input)

        val parserErrors = mutableListOf<ParserError>()
        val parseObserver = object : ParserObserver {
            override fun onNext(parsingResult: Any) {
                // Ignore
            }

            override fun onError(error: ParserError) {
                parserErrors.add(error)
            }
        }
        val forms = vm.repl.parse(input, parseObserver)

        if (parserErrors.isNotEmpty()) {
            parserErrors.forEach { error -> displayErrorText(error.description) }
        } else {
            val result = vm.repl.eval(forms)
            displayText("\u21e8 ${result.toLiteral()}", R.color.repl_output)
        }
    }

    override fun onSetupToolBar(toolBar: Toolbar) {
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

    override fun onSetupOptionsMenu(menu: Menu) {
        setMenuItemEnabled(R.id.action_next_input, inputIndex < inputCache.size - 1)
        setMenuItemEnabled(R.id.action_previous_input, inputIndex > 0)
    }

    override fun onSetupViewBindings(binding: ReplFragmentBinding) {
        inputEditText = binding.editTextReplInput
        consoleTextView = binding.textViewReplConsole

        doClearConsole()
        doClearInput()

        binding.imageButtonClearInput.setOnClickListener{  doClearInput()  }

        inputEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed) {
                    doHandleInput(inputEditText.text.toString())
                }
                true
            } else false
        }
    }

    private fun appendText(string: String, @ColorRes color: Int = R.color.repl_input) {
        if (consoleTextView.visibility == View.GONE) consoleTextView.visibility = View.VISIBLE
        val builder = SpannableStringBuilder()
        val spanned = SpannableString(string)
        spanned.setSpan(ForegroundColorSpan(getColor(color)), 0, spanned.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        consoleTextView.append(builder.append(spanned))
    }

    private fun displayErrorText(errorMessage: String) {
        displayText("\u21e8 Error: $errorMessage", R.color.repl_error)
    }

    private fun displayText(string: String, @ColorRes color: Int = R.color.repl_input) {
        if (consoleTextView.length() > 0) appendText("\n")
        appendText(string, color)
    }

    private fun cacheInput(input: String) {
        if (inputCache.size >= MAX_INPUT_CACHE_SIZE) {
            inputCache.removeAt(0)
        }
        inputCache.add(input)
        inputIndex = inputCache.size
        invalidateOptionsMenu()
    }

    override fun onException(exception: FlxException) {
        displayErrorText(exception.message ?: "Runtime exception stopped execution: ${exception::class.qualifiedName}")
    }

    companion object {

        const val MAX_INPUT_CACHE_SIZE = 64
    }

    override fun onStarted(vm: FlxVM) {}

    override fun onPaused(vm: FlxVM) {}

    override fun onResumed(vm: FlxVM) {}

    override fun onStopped(vm: FlxVM) {}
}
