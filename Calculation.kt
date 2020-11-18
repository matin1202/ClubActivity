package com.matin.dongari

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_calculation.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.round
import kotlin.math.sqrt

class CalculationActivity : AppCompatActivity() {
    private var stage = 0
    private var isStart = false
    private var answer = 0.0f
    private var time = 300.0f
    private var isClear = true
    private var timer: Timer? = null
    private var game: Timer? = null
    private var maxStage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        bt_calculation_start.setOnClickListener {
            if(isStart){
                stopGame()
            }
            else{
                startGame()
            }
        }

        bt_calculation_post.setOnClickListener {
            if(et_calculation_post.text.toString().isEmpty()){
                return@setOnClickListener
            }
            if(et_calculation_post.text.toString().toFloat() == answer){
                time = 200.0f
                Toast.makeText(this, "정답입니다. 잠시후 다음 스테이지로 넘어갑니다.", Toast.LENGTH_SHORT).show()
                et_calculation_post.text.clear()
                isClear = true
            }
            else{
                Toast.makeText(this, "오답입니다.", Toast.LENGTH_SHORT).show()
                et_calculation_post.text.clear()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startGame() {
        isStart = true
        timer = timer(period = 10){
            time --
            runOnUiThread{
                tv_calculation_time.text = "${time / 100.0f} 초 남음"
            }
            if(time == 0.0f && !isClear){
                runOnUiThread {
                    Toast.makeText(applicationContext, "시간 초과...", Toast.LENGTH_SHORT).show()
                }
                stopGame()
            }
            if(time == 0.0f && isClear){
                stage ++
                if(stage == 6){
                    stopGame()
                    return@timer
                }
                time = 1500.0f
                val question = makeQuestion()
                runOnUiThread{
                    tv_calculation_question.text = question
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun stopGame(){
        if(maxStage < stage){
            maxStage = stage
        }
        isStart = false
        timer?.cancel()
        game?.cancel()
        timer = null
        game = null
        isClear = true
        time = 300.0f
        stage = 0
        runOnUiThread {
            et_calculation_post.text.clear()
            tv_calculation_question.text = "답은 $answer 입니다."
            tv_calculation_max_stage.text = "${getString(R.string.max_stage)} $maxStage"
        }
    }

    private fun makeQuestion(): String{
        var question = ""
        isClear = false
        when(stage){
            1 -> {
                val one = makeSecondRandom()
                val two = makeSecondRandom()
                question = "$one + $two = ???"
                answer = ((one + two).toFloat())
            }
            2 -> {
                var one = makeSecondRandom()
                var two = makeSecondRandom()
                if(one < two){
                    two -= one
                    one += two
                    two = one - two
                }
                question = "$one - $two = ???"
                answer = ((one - two).toFloat())
            }
            3 -> {
                val one = makeThirdRandom()
                val two = makeThirdRandom()
                question = "$one + $two = ???"
                answer = (one + two).toFloat()
            }
            4 -> {
                val randomTwo = (4..9).toMutableList()
                val one = makeThirdRandom()
                val two = randomTwo.random()
                question = "$one * $two = ???"
                answer = one.toFloat() * two.toFloat()
            }
            5 -> {
                val one = makeThirdRandom()
                val two = (2..9).toMutableList().random()
                question = "$one / $two = ???"
                answer = (round((one / two) * 10f) / 0.1f)
            }
            6 -> {
                val one = makeSecondRandom()
                val two = makeSecondRandom()
                val three = makeSecondRandom()
                question = "$one + $two + $three = ???"
                answer = one + two + three.toFloat()
            }
            7 -> {
                val one = makeFirstRandom()
                val two = makeThirdRandom()
                question = "$one x $two = ???"
                answer = one * two.toFloat()
            }
            8 -> {
                val one = makeSecondRandom()
                val two = makeFirstRandom()
                val three = makeThirdRandom()
                question = "$one x $two + $three"
                answer = one * two + three.toFloat()
            }
            9 -> {
                val one = makeFirstRandom()
                val two = makeFirstRandom()
                val three = makeFirstRandom()
                val four = makeFirstRandom()
                val five = makeFirstRandom()
                question = "$one - $two + $three * $four + $five = ???"
                answer = one - two + three * four + five.toFloat()
            }
            10 -> {
                val one = makeFourthRandom()
                val two = (5..9).toMutableList().random()
                question = "$one / $two"
                answer = round(one / two * 10f) / 10
            }
        }
        Log.d("CA", "makeQuestion: Answer is $answer")
        return question
    }

    private fun makeFirstRandom(): Int{
        return (2..9).toMutableList().random()
    }

    private fun makeSecondRandom(): Int{
        return (10..99).toMutableList().random()
    }

    private fun makeThirdRandom(): Int{
        return (100..999).toMutableList().random()
    }

    private fun makeFourthRandom(): Int {
        val one = (1000..9999).toMutableList().random()
        for(i in 2 until round(sqrt(one.toFloat())).toInt()){
            if(one % i == 0){
                return makeFourthRandom()
            }
        }
        return one
    }
}
