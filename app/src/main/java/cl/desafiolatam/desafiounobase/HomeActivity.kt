package cl.desafiolatam.desafiounobase

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var userName: String
    private lateinit var nickname:TextView
    private lateinit var homeTitle: TextView
    private lateinit var nicknameTitle: TextView
    private lateinit var spanishCheckBox: CheckBox
    private lateinit var englishCheckBox: CheckBox
    private lateinit var germanCheckBox: CheckBox
    private lateinit var otherCheckBox: CheckBox
    private lateinit var otherTextInput: TextInputEditText
    private lateinit var nickNameInput: TextInputEditText
    private lateinit var ageInput: TextInputEditText
    private lateinit var save: Button
    private lateinit var container: ConstraintLayout
// para almacenar los datos en shared preferences utilice claves que contengan el nombre del usuario y el nombre de el campo guardado.
    //esta recomendación no aplica para todos los valores, pero ayuda con varios
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filename="cl.desafiolatam.desafiounobase"
        sharedPreferences=getSharedPreferences(filename, Context.MODE_PRIVATE)
        setContentView(R.layout.activity_home)
        setUpViews()
        loadData()
        save.setOnClickListener {
            //Agregar los pasos necesario para guardar los datos
            saveLanguages()
            saveNickNameAndAge()
            Snackbar.make(container, "Datos guardados", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setUpViews() {
        homeTitle = findViewById(R.id.home_title)
        nickname=findViewById(R.id.nickname_title)
        nicknameTitle = findViewById(R.id.nickname_input)
        spanishCheckBox = findViewById(R.id.language_one)
        englishCheckBox = findViewById(R.id.language_two)
        germanCheckBox = findViewById(R.id.language_three)
        otherCheckBox = findViewById(R.id.language_other)
        otherTextInput = findViewById(R.id.other_language_input)
        nickNameInput = findViewById(R.id.nickname_input)
        ageInput = findViewById(R.id.age_input)
        save = findViewById(R.id.save_button)
        container = findViewById(R.id.container)
    }
    private fun loadData() {
        userName = intent.getStringExtra("Username")
        val title = "BienvenidoEsta es la pantalla inicial, aquí $userName podrá ver todas sus preferencias"
        homeTitle.text = title
        nickname.text=sharedPreferences.getString("nicknameKey $userName","")
        handleLanguages()
        loadProfile()

    }

    private fun loadProfile() {
        //crear las claves para buscar y almacenar los datos, recuerde asociar las mismas al usuario de alguna manera
        val nicknameKey = "nicknameKey $userName"
        val ageKey = "ageKey $userName"
        val nickNameString = sharedPreferences.getString(nicknameKey,"Sin Nickname")
        nickNameInput.setText(nickNameString)
        nicknameTitle.text = nickNameString
        val ageString =sharedPreferences.getInt(ageKey,0).toString()
        ageInput.setText(ageString)
    }

    private fun handleLanguages() {
        val languagesKey = "languagesKey $userName"
        val languages = sharedPreferences.getStringSet(languagesKey,mutableSetOf<String>())

        for (language:String in languages){
            resolveLanguage(language)
        }


        //ocupar resolveLanguage para cargar los datos iniciales en los checkboxs
    }

    private fun resolveLanguage(language: String) {
        if (language.isNotEmpty() && language == "Spanish") {
            spanishCheckBox.isChecked = true
        }
        if (language.isNotEmpty() && language == "English") {
            englishCheckBox.isChecked = true
        }
        if (language.isNotEmpty() && language == "German") {
            germanCheckBox.isChecked = true
        }
        if (language.isNotEmpty() && language.contains("Other")) {
            otherCheckBox.isChecked = true
            otherTextInput.setText(language.split("*")[1]) // ocupar el mismo delimitador para almacenar el valor de este campo
        }
    }

    private fun saveLanguages() {
        //crear lo necesario para guardar los idiomas seleccionados por el usuario
        //en sharedpreferences
        val languagesKey = "languagesKey $userName"
        val languages= mutableSetOf<String>()
        if (spanishCheckBox.isChecked) {
            languages.add("Spanish")
        }
        if (englishCheckBox.isChecked) {
            languages.add("English")
        }
        if (germanCheckBox.isChecked) {
            languages.add("German")
        }
        if (otherCheckBox.isChecked) {
            languages.add("Other*${otherTextInput.text.toString()}")
        }

        sharedPreferences.edit().putStringSet(languagesKey,languages).apply()

    }

    private fun saveNickNameAndAge() {
        val nicknameKey = "nicknameKey $userName"
        val ageKey = "ageKey $userName"
        if (nickNameInput.text!!.isNotEmpty()) {
           sharedPreferences.edit().putString(nicknameKey,nickNameInput.text.toString()).apply()
            nickname.text=nickNameInput.text.toString()
        }
        if (ageInput.text!!.isNotEmpty()) {
            val ageInt = ageInput.text.toString().toInt()
            sharedPreferences.edit().putInt(ageKey,ageInt).apply()

        }
    }
}
