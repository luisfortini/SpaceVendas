package br.com.spaceinformatica.spacevendas


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var inputUser: EditText
    private lateinit var inputPass: EditText
    private lateinit var spinnerFilial: Spinner

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.config_menu) {
            openSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputUser = findViewById(R.id.login_user)
        inputUser.setSelectAllOnFocus(true)
        inputUser.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                Toast.makeText(this@MainActivity, "depois", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun openSettings() {
        val intent = Intent(this, ConfigActivity::class.java)
        startActivity(intent)
    }

    private fun getFiliais(user: String){
        val retorfitClient = NetworkUtils.get
    }

}
