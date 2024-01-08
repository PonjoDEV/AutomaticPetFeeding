package com.example.automaticpetfeeding

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.automaticpetfeeding.control.FeedingTimeController
import com.example.automaticpetfeeding.databinding.ActivityTimepickerBinding

class TimepickerActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityTimepickerBinding
    var hour:Int = 0
    var minutes:Int = 0

    val feedingTimeController = FeedingTimeController(this)

    private val sharedPreference: SharedPreferences by lazy {
        this.getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
    }

    //Keys for the shared preference file
    private val HOUR_KEY = "hour_key"
    private val MINUTE_KEY = "minute_key"
    private lateinit var spinnerIdList: Spinner

    //private lateinit var recyclerView: RecyclerView
    //private lateinit var  adapter: FeedingTimeAdapter


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityTimepickerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        registerEvents()

        showFT()



        //feedTimeControl = FeedingTimeControlROOM(baseContext)

        //viewModel = ViewModelProvider(this).get(FeedingTimeListViewModel::class.java)

        //configRecycleView()
        //registerObservers()

    }

    private fun showFT() {
        val feedingTimes = feedingTimeController.getAllFeedingTimes()
        val stringBuilder = StringBuilder()

        for (feedingTime in feedingTimes) {
            stringBuilder.append("    ${feedingTime.hour} : ")
            stringBuilder.append("${feedingTime.minute}\n")
            stringBuilder.append("--------\n")
        }

        val dadosTextView = binding.feedingTimeList
        dadosTextView.text= stringBuilder.toString()
    }


    private fun registerEvents() {
        binding.newFt.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.btnDeleteAll.setOnClickListener(this)
    }

    override fun onClick(btn: View) {
        when(btn.id){
            binding.newFt.id -> {
                addNewFt(feedingTimeController)
                showFT()
            }
            binding.btnDelete.id ->{
                deleteSelectedId()
                showFT()
            }
            binding.btnDeleteAll.id->{
                deleteAllData()
                showFT()
            }
        }
    }

    private fun deleteAllData() {
        feedingTimeController.deleteAllData()
        showFT()
        Toast.makeText(this, "Todos os dados foram apagados", Toast.LENGTH_SHORT).show()
    }

    private fun deleteSelectedId() {
        val selectedIdString = binding.deleteID.text.toString()

        if (selectedIdString.isNotBlank()) {
            val selectedId = selectedIdString.toLongOrNull()

            if (selectedId != null) {
                // Verifique se o ID existe no banco de dados
                val idList = feedingTimeController.getAllFeedingTimes().map { it.id }

                if (idList.contains(selectedId)) {
                    feedingTimeController.deleteFeedingTime(selectedId)

                    // Atualize a lista de IDs após a exclusão
                    val updatedIdList = feedingTimeController.getAllFeedingTimes().map { it.id }

                    // Se desejar, você pode usar a lista de IDs atualizada para qualquer propósito
                    // por exemplo, exibindo-a em um Spinner ou ListView
                    // Atualize sua UI conforme necessário

                    // Limpe o campo de texto
                    binding.deleteID.text.clear()

                    Toast.makeText(this, "Item deletado com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "ID não encontrado no banco de dados", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor, insira um ID", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addNewFt(feedingTimeController: FeedingTimeController) {
        val hourString = binding.hour.text.toString()
        val minuteString = binding.minute.text.toString()

        if (hourString.isNotEmpty() && minuteString.isNotEmpty()) {
            hour = hourString.toInt()
            minutes = minuteString.toInt()

            if (hour in 0..24 && minutes in 0..60) {
                val allFeedingTimes = feedingTimeController.getAllFeedingTimes()

                if (allFeedingTimes.size < 5) {
                    val newId = feedingTimeController.addFeedingTime(hour, minutes)
                    Toast.makeText(this, "Novo tempo de alimentação adicionado com ID $newId", Toast.LENGTH_SHORT).show()

                    binding.hour.text.clear()
                    binding.minute.text.clear()
                } else {
                    Toast.makeText(this, "Não é possível adicionar mais tempo de alimentação. Limite de 5 atingido.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Valores fora do intervalo permitido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor, preencha ambos os campos", Toast.LENGTH_SHORT).show()
        }
    }



    /*
    private fun configRecycleView() {
        viewModel.loadFeedingTime(feedTimeControl.getAllFT())
        adapter = FeedingTimeListAdapter(viewModel.feedingTimeList(),viewModel.deleteFeedingTime())

        binding.feedingTimeList.layoutManager = LinearLayoutManager(baseContext)
        binding.feedingTimeList.adapter = adapter
    }

    private fun registerObservers() {
        viewModel.deleteFeedingTime().observe(this,{
            viewModel.excludeFeedingTime(it)
            adapter.notifyDataSetChanged()
        })
    }
    */
}