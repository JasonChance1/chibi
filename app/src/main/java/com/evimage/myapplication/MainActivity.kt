package com.evimage.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.evimage.myapplication.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var countDownTimer: CountDownTimer? = null
    private lateinit var adapter: Adapter
    private var time: Long = 30000
    private val resultList = mutableListOf("", "")
    private val selectList = listOf("魏", "属", "吴")

    private var currentSelected = ""
    private var wei = 0
    private var shu = 0
    private var wu = 0
    private var account = 5000
    private var isConfirm = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.account.text = account.toString()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(resultList)
        binding.recyclerView.adapter = adapter
        startTimer(time)
        setupListeners()

        val spAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            selectList
        )
        binding.spinner.adapter = spAdapter

        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currentSelected = selectList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.confirm.setOnClickListener {
            if (binding.inputValue.text.isNotBlank()) {
                if (account - binding.inputValue.text.toString().toInt() >= 0) {
                    when (currentSelected) {
                        "魏" -> {
                            wei += binding.inputValue.text.toString().toInt()
                            binding.wei.text = "已增援魏$wei"
                        }

                        "属" -> {
                            shu += binding.inputValue.text.toString().toInt()
                            binding.shu.text = "已增援属$shu"
                        }

                        "吴" -> {
                            wu += binding.inputValue.text.toString().toInt()
                            binding.wu.text = "已增援吴$wu"
                        }
                    }
                    account -= binding.inputValue.text.toString().toInt()
                    isConfirm = true
                    binding.account.text = account.toString()
                    ToastUtils.showLong("支援成功")
                } else {
                    ToastUtils.showLong("余额不足")
                }

            } else {
                ToastUtils.showLong("请输入猜测金额")
            }
        }
    }

    private fun setupListeners() {
        binding.hour.addTextChangedListener(getTextWatcher())
        binding.min.addTextChangedListener(getTextWatcher())
        binding.second.addTextChangedListener(getTextWatcher())
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                time = calculateTime()
                startTimer(time)
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
    }

    private fun calculateTime(): Long {
        var calculatedTime = 0L
        if (binding.hour.text.isNotBlank()) {
            calculatedTime += binding.hour.text.toString().toLong() * 60 * 60 * 1000
        }
        if (binding.min.text.isNotBlank()) {
            calculatedTime += binding.min.text.toString().toLong() * 60 * 1000
        }
        if (binding.second.text.isNotBlank()) {
            calculatedTime += binding.second.text.toString().toLong() * 1000
        }
        return calculatedTime
    }

    private fun startTimer(time: Long) {
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.timer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                val result = when (listOf(1, 2, 3).random()) {
                    1 -> "魏"
                    2 -> "属"
                    3 -> "吴"
                    else -> {
                        ""
                    }
                }
                if (isConfirm) {
                    if ((wei > 0 || shu > 0 || wu > 0)) {
                        if (result == "魏" && wei > 0) {
                            account += (wei * 2.9).toInt()
                            binding.account.text = account.toString()
                            ToastUtils.showLong(
                                "恭喜获得${
                                    (wei * 2.9).toInt()
                                }"
                            )
                        } else if (result == "属" && shu > 0) {
                            account += (shu * 2.9).toInt()
                            binding.account.text = account.toString()
                            ToastUtils.showLong(
                                "恭喜获得${
                                    (shu * 2.9).toInt()
                                }"
                            )
                        } else if (result == "吴" && wu > 0) {
                            account += (wu * 2.9).toInt()
                            binding.account.text = account.toString()
                            ToastUtils.showLong(
                                "恭喜获得${
                                    (wu * 2.9).toInt()
                                }"
                            )
                        }
//                        if (result == currentSelected) {
//                            account += (binding.inputValue.text.toString().toInt() * 2.9).toInt()
//                            binding.account.text = account.toString()
//                            ToastUtils.showLong(
//                                "恭喜获得${
//                                    (binding.inputValue.text.toString().toInt() * 2.9).toInt()
//                                }"
//                            )
//                        }
                        else {
                            ToastUtils.showLong(
                                "遗憾战败，损失${
                                    (binding.inputValue.text.toString().toInt()).toInt()
                                }"
                            )
                        }
                    }
                    isConfirm = false
                    binding.inputValue.setText("")
                    binding.wei.text = ""
                    binding.shu.text = ""
                    binding.wu.text = ""
                    wei = 0
                    shu = 0
                    wu = 0
                }
                resultList.add(0, result)
                adapter.notifyItemInserted(0)
                binding.result.text = result
                binding.timer.text = "00:00:00"
                startTimer(time)
            }
        }.start()
    }
}