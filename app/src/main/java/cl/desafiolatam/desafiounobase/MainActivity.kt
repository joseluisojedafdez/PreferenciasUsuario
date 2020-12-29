package cl.desafiolatam.desafiounobase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var nameInput: TextInputEditText
    lateinit var advance: Button
    lateinit var container: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filename="cl.desafiolatam.desafiounobase"
        sharedPreferences=getSharedPreferences(filename, Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)
        nameInput = findViewById(R.id.name_input)
        advance = findViewById(R.id.login_button)
        container = findViewById(R.id.container)
        setUpListeners()
    }

    private fun setUpListeners() {
        advance.setOnClickListener {
            if (nameInput.text!!.isNotEmpty()) {

                val intent: Intent
                if (hasSeenWelcome()) {
                    intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("Username",nameInput.text.toString())
                } else {
                    saveUser(nameInput.text.toString())
                    intent = Intent(this, WelcomeActivity::class.java)
                    intent.putExtra("Username",nameInput.text.toString())
                }

                startActivity(intent)
            } else {
                Snackbar.make(container, "El nombre no puede estar vacío", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUser(username: String) {

        sharedPreferences.edit().putString("Usuario $username",username).apply()

    }

    private fun hasSeenWelcome(): Boolean {
        var returnValue = false

        val name=nameInput.text.toString()
        sharedPreferences.getString("Usuario $name",name)

        if(sharedPreferences.contains("Usuario $name")){
            returnValue=true
        }
        //implementar este método para saber si el usuario ya ha entrado a la aplicación y ha visto
        //la pantalla de bienvenida. Este método permite decidir que pantalla se muestra después de presionar Ingresar
        //recorra la lista de usuarios
        return returnValue
    }
}
