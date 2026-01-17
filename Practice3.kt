// TP 8 EX - 3

// -------- | MainActivity | --------

package com.example.workmanager_tp8

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn = findViewById<MaterialButton>(R.id.btn)

        btn.setOnClickListener {

            val workRequest = OneTimeWorkRequestBuilder<CalculationWorker>().build()
            WorkManager.getInstance(this).enqueue(workRequest)

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
                .observe(this) { workInfo ->
                    if (workInfo != null && workInfo.state.isFinished) {

                        val calculResultat = workInfo.outputData.getInt("SOMME", 0)
                        Toast.makeText(this, "a somme est : $calculResultat", Toast.LENGTH_LONG).show()

                    }
                }


        }
    }
}

// -------- | CalculationWorker | --------

package com.example.workmanager_tp8

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class CalculationWorker (appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        return try {
            var somme = 0
            for (i in 1..100) {
                somme += i
            }

            val outputData = workDataOf("SOMME" to somme)
            Result.success(outputData)

        } catch (e: Exception) {
            Log.e("WorkerManager", "Error: ${e.message}")
            Result.failure()
        }

    }

}
