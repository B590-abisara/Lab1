package com.example.lab1

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import Question
import android.util.Log
import androidx.core.view.ViewCompat
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import com.example.lab1.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
        )

    private var answeredQuestions = mutableSetOf<Int>()  // Track answered questions
    private var correctAnswers = 0
    private var currentIndex = 0
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.trueButton.setOnClickListener {
                view: View ->
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
                view: View ->
            checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {

            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)

        // Enable or disable buttons based on whether the question has been answered
        val alreadyAnswered = currentIndex in answeredQuestions
        binding.trueButton.isEnabled = !alreadyAnswered
        binding.falseButton.isEnabled = !alreadyAnswered
    }

    // Check user's answer and disable buttons to prevent multiple submissions
    private fun checkAnswer(userAnswer: Boolean) {
        // Prevent re-answering the same question
        if (currentIndex in answeredQuestions) return

        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            correctAnswers++ // Increment correct answers count
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        // Show toast message
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        // Mark question as answered
        answeredQuestions.add(currentIndex)

        // Disable buttons after answering
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false

        // Check if all questions have been answered
        if (answeredQuestions.size == questionBank.size) {
            showScore()
        }
    }

    // final quiz score
    private fun showScore() {
        val scorePercentage = (correctAnswers.toDouble() / questionBank.size) * 100
        Toast.makeText(this, "Quiz Completed! Your score: $scorePercentage%", Toast.LENGTH_LONG).show()
    }

}