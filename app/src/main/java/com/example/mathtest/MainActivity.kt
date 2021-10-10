package com.example.mathtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.mathtest.databinding.*

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var bindingFirst: ActivityFirstTaskBinding
    private lateinit var bindingNext: ActivityNextTaskBinding
    private lateinit var bindingLast: ActivityLastTaskBinding
    private lateinit var bindingFinish: ActivityFinishBinding
    private val indexArray = mutableSetOf<Int>()
    private val answerArray = mutableListOf<Int>()
    private val solutionArray = mutableListOf<Int>()
    private lateinit var countDownTimer: CountDownTimer
    private var flagFinish = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        bindingFirst = ActivityFirstTaskBinding.inflate(layoutInflater)
        bindingNext = ActivityNextTaskBinding.inflate(layoutInflater)
        bindingLast = ActivityLastTaskBinding.inflate(layoutInflater)
        bindingFinish = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        bindingMain.start.setOnClickListener {
            while (indexArray.size < 10) {
                val max = ProblemsData.data.size - 1
                indexArray.add((0..max).random())
            }
            while (answerArray.size < 10) {
                answerArray.add(-1)
            }
            indexArray.forEachIndexed { index, _ ->
                solutionArray.add(ProblemsData.data[index][6].toInt())
            }
            startTimer()
            customOnClick(0)
        }
    }

    private fun customOnClick(index: Int) {
        val problem = ProblemsData.data[indexArray.elementAt(index)]

        if (index == 0) {
            setContentView(bindingFirst.root)
            bindingFirst.number.text = "Soal 1"
            bindingFirst.problem.text = problem[0]
            bindingFirst.A.text = problem[1]
            bindingFirst.B.text = problem[2]
            bindingFirst.C.text = problem[3]
            bindingFirst.D.text = problem[4]
            bindingFirst.E.text = problem[5]
            bindingFirst.submitButton.setOnClickListener {
                if (bindingFirst.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Please select your answer!", Toast.LENGTH_LONG).show()
                } else {
                    answerArray[index] = bindingFirst.radioGroup.checkedRadioButtonId % bindingFirst.A.id
                    customOnClick(1)
                }
            }
            bindingFirst.skipButton.setOnClickListener {
                customOnClick(1)
            }
            bindingFirst.clearButton.setOnClickListener {
                bindingFirst.radioGroup.clearCheck()
                answerArray[index] = 0
            }
        } else {
            if (index < 9) {
                setContentView(bindingNext.root)
                bindingNext.number.text = "Soal ${index + 1}"
                bindingNext.problem.text = problem[0]
                bindingNext.radioGroup.check(bindingNext.A.id + answerArray[index])
                bindingNext.A.text = problem[1]
                bindingNext.B.text = problem[2]
                bindingNext.C.text = problem[3]
                bindingNext.D.text = problem[4]
                bindingNext.E.text = problem[5]
                bindingNext.submitButton.setOnClickListener {
                    if (bindingNext.radioGroup.checkedRadioButtonId == -1) {
                        Toast.makeText(this, "Please select your answer!", Toast.LENGTH_LONG).show()
                    } else {
                        answerArray[index] = bindingNext.radioGroup.checkedRadioButtonId % bindingNext.A.id
                        bindingNext.radioGroup.clearCheck()
                        customOnClick(index + 1)
                    }
                }
                bindingNext.skipButton.setOnClickListener {
                    bindingNext.radioGroup.clearCheck()
                    customOnClick(index + 1)
                }
                bindingNext.previousButton.setOnClickListener {
                    customOnClick(index - 1)
                }
                bindingNext.clearButton.setOnClickListener {
                    bindingNext.radioGroup.clearCheck()
                    answerArray[index] = 0
                }
            } else {
                setContentView(bindingLast.root)
                bindingLast.number.text = "Soal 10"
                bindingLast.problem.text = problem[0]
                bindingLast.A.text = problem[1]
                bindingLast.B.text = problem[2]
                bindingLast.C.text = problem[3]
                bindingLast.D.text = problem[4]
                bindingLast.E.text = problem[5]
                bindingLast.previousButton.setOnClickListener {
                    customOnClick(index - 1)
                }
                bindingLast.skipButton.setOnClickListener {
                    flagFinish = 1
                    setContentView(bindingFinish.root)
                    bindingFinish.score.text = calculateScore().toString()
                }
                bindingLast.submitButton.setOnClickListener {
                    if (bindingLast.radioGroup.checkedRadioButtonId == -1) {
                        Toast.makeText(this, "Please select your answer!", Toast.LENGTH_LONG).show()
                    } else {
                        flagFinish = 1
                        answerArray[index] = bindingLast.radioGroup.checkedRadioButtonId % bindingLast.A.id
                        setContentView(bindingFinish.root)
                        val score = calculateScore()
                        bindingFinish.score.text = score.toString()
                    }
                }
            }
        }
    }

    private fun calculateScore(): Int {
        var score = 0
        answerArray.forEachIndexed { index, element ->
            if (element == solutionArray[index]) {
                score += 4
            } else if (element != -1) {
                score -= 1
            }
        }
        return score
    }

    private fun startTimer() {
        countDownTimer = object: CountDownTimer(1200000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minute = ((millisUntilFinished/1000) / 60).toInt()
                val seconds = ((millisUntilFinished/1000) % 60).toInt()
                if (minute != 0) {
                    bindingFirst.timer.text = "${minute}m ${seconds}s left"
                    bindingNext.timer.text = "${minute}m ${seconds}s left"
                    bindingLast.timer.text = "${minute}m ${seconds}s left"
                } else {
                    bindingFirst.timer.text = "${seconds}s left"
                    bindingNext.timer.text = "${seconds}s left"
                    bindingLast.timer.text = "${seconds}s left"
                }
            }
            override fun onFinish() {
                if (flagFinish == 0) {
                    setContentView(bindingFinish.root)
                    bindingFinish.alert.text = getString(R.string.alert)
                    bindingFinish.score.text = calculateScore().toString()
                }
            }
        }
        countDownTimer.start()
    }
}