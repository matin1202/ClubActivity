package com.matin.dongari

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_mole_hunt.*
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.concurrent.timer

class MoleHuntActivity : AppCompatActivity(), View.OnClickListener {
    private var time = 0
    private var timerTask: Timer? = null
    private var moleTask: Timer? = null
    private var isStarted = false
    private var score = 0
    private var numbers = mutableListOf<Int?>(null, null, null, null, null, null)
    private val tag = "MHA"
    private var bestScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mole_hunt)
        
        tv_mole_hunt_start.setOnClickListener(this)
        mole_hunt_mole_1.setOnClickListener(this)
        mole_hunt_mole_2.setOnClickListener(this)
        mole_hunt_mole_3.setOnClickListener(this)
        mole_hunt_mole_4.setOnClickListener(this)
        mole_hunt_mole_5.setOnClickListener(this)
        mole_hunt_mole_6.setOnClickListener(this)
        mole_hunt_mole_7.setOnClickListener(this)
        mole_hunt_mole_8.setOnClickListener(this)
        mole_hunt_mole_9.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun startGame(){
        score = 0
        Log.d(tag, "startGame: Start Game")
        isStarted = true
        tv_mole_hunt_start.text = "중지"
        timerTask = timer(period = 100){
            time ++
            runOnUiThread{
                tv_mole_hunt_remain.text = "${(500-time)/10}초 남음"
                tv_mole_hunt_score.text = "${score}점"
            }
            if(time >= 500) {
                stopGame()
            }
        }
        Log.d(tag, "startGame: timerTask end")
        moleTask = timer(period = 1000){
            try {
                numbers = mutableListOf(null, null, null, null, null, null)
                var times = 1
                for (i in 0 until time / 100) {
                    times++
                }
                val random = (0..8).toMutableSet()
                for (i in 0 until times) {
                    numbers[i] = random.random()
                    random.remove(numbers[i])
                }
                runOnUiThread {
                    val lists = mutableListOf<ImageView>(
                        findViewById(R.id.mole_hunt_mole_1),
                        findViewById(R.id.mole_hunt_mole_2),
                        findViewById(R.id.mole_hunt_mole_3),
                        findViewById(R.id.mole_hunt_mole_4),
                        findViewById(R.id.mole_hunt_mole_5),
                        findViewById(R.id.mole_hunt_mole_6),
                        findViewById(R.id.mole_hunt_mole_7),
                        findViewById(R.id.mole_hunt_mole_8),
                        findViewById(R.id.mole_hunt_mole_9)
                    )
                    for (i in 0..8) {
                        lists[i].setImageResource(R.drawable.mole_in)
                    }
                    for (i in 0 until numbers.size) {
                        if(numbers[i]!=null) {
                            lists[numbers[i]!!].setImageResource(R.drawable.mole_out)
                        }
                    }
                }
            }catch (e: IndexOutOfBoundsException){
                e.printStackTrace()
            }//finally{
               // stopGame()
            //}
            
        }
        Log.d(tag, "startGame: moleTask end")
    }

    @SuppressLint("SetTextI18n")
    private fun stopGame(){
        Log.d(tag, "stopGame: called")
        isStarted = false
        if(bestScore<score){
            bestScore = score
        }
        runOnUiThread {
            tv_mole_hunt_score.text = "최고 점수 : $bestScore"
            tv_mole_hunt_remain.text = "남은 시간"
            tv_mole_hunt_start.text = "시작"
            val ids = mutableListOf(R.id.mole_hunt_mole_1, R.id.mole_hunt_mole_2, R.id.mole_hunt_mole_3, R.id.mole_hunt_mole_4, R.id.mole_hunt_mole_5, R.id.mole_hunt_mole_6, R.id.mole_hunt_mole_7, R.id.mole_hunt_mole_8, R.id.mole_hunt_mole_9)
            for(i in ids.indices){
                findViewById<ImageView>(ids[i]).setImageResource(R.drawable.mole_in)
            }
        }
        timerTask?.cancel()
        moleTask?.cancel()
        timerTask = null
        moleTask = null
        time = 0
        numbers = mutableListOf(null, null,null, null, null, null)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.tv_mole_hunt_start){
            if(!isStarted){
                isStarted = true
                startGame()
            }
            else if(isStarted){
                isStarted = false
                stopGame()
            }
            return
        }
        val ids = mutableListOf(R.id.mole_hunt_mole_1, R.id.mole_hunt_mole_2, R.id.mole_hunt_mole_3, R.id.mole_hunt_mole_4, R.id.mole_hunt_mole_5, R.id.mole_hunt_mole_6, R.id.mole_hunt_mole_7, R.id.mole_hunt_mole_8, R.id.mole_hunt_mole_9)
        for(i in 0 until time / 100 + 1){
            try {
                if (numbers[i] != null) {
                    if (ids[numbers[i]!!] == v?.id) {
                        score++
                        val imageView = findViewById<ImageView>(ids[numbers[i]!!])
                        imageView.setImageResource(R.drawable.mole_in)
                        numbers.remove(numbers[i])
                        return
                    }
                }
            }catch(e: Exception){
                //no-ops
            }
        }
        score--
    }

}
