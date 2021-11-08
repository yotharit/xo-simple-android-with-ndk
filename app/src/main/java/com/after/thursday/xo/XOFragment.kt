package com.after.thursday.xo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.after.thursday.xo.databinding.XoFragmentLayoutBinding
import java.util.*

class XOFragment : Fragment() {

    companion object {
        fun getInstance(): XOFragment {
            return XOFragment()
        }
    }

    private lateinit var viewModel: XOViewModel

    private lateinit var binding: XoFragmentLayoutBinding

    private val buttonList: ArrayList<Button> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(XOViewModel::class.java)
        viewModel.setup()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = XoFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.resultString.observe(viewLifecycleOwner, {
            binding.statusTitle.text = it
        })
        viewModel.xoBoard.observe(viewLifecycleOwner, {
            updateBoard(it)
        })
        viewModel.gameStatus.observe(viewLifecycleOwner, {
            when (it) {
                XOViewModel.GameStatus.WIN -> disableBoard()
                XOViewModel.GameStatus.DRAW -> disableBoard()
                XOViewModel.GameStatus.ONGOING -> {
                    // do nothing
                }
            }
        })
    }

    private fun updateBoard(board: CharArray) {
        board.forEachIndexed { index, c ->
            val button = buttonList.get(index)
            if (c != ' ') {
                button.isClickable = false
                button.isEnabled = false
            } else {
                button.isClickable = true
                button.isEnabled = true
            }
            button.text = c.toString()
        }
    }

    private fun disableBoard() {
        buttonList.forEach {
            it.isClickable = false
            it.isEnabled = false
        }
    }

    private fun setupView() {
        buttonList.addAll(
            listOf(
                binding.point00, binding.point01, binding.point02,
                binding.point10, binding.point11, binding.point12,
                binding.point20, binding.point21, binding.point22
            )
        )
        buttonList.forEachIndexed { index, button ->
            val pair = viewModel.getRowColumn(index)
            button.setOnClickListener {
                viewModel.placeXO(pair.first, pair.second)
            }
        }
        binding.resetButton.setOnClickListener {
            viewModel.resetGame()
        }
    }
}