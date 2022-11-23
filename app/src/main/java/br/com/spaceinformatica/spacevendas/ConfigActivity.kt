package br.com.spaceinformatica.spacevendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import br.com.spaceinformatica.spacevendas.model.Config

class ConfigActivity : AppCompatActivity() {

    private lateinit var editHost: EditText
    private lateinit var editPort: EditText
    private lateinit var checkHttps: CheckBox
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        editHost = findViewById(R.id.host_config)
        editPort = findViewById(R.id.port_config)
        checkHttps = findViewById(R.id.https_config)

        btnSave = findViewById(R.id.btn_save_config)
        btnSave.setOnClickListener {
            val host = editHost.text.toString()
            val port = editPort.text.toString()
            val https = checkHttps.isChecked

            if (host.isNotEmpty() && port.isNotEmpty()) {

                saveSetting(host, port.toInt(), https)

            } else {
                Toast.makeText(this, R.string.message_invalid_config, Toast.LENGTH_SHORT).show()
            }
        }

        getValuesConfig()

    }

    fun setValuesConfig(config: Config) {
        editHost.setText(config.host)
        editPort.setText(config.porta.toString())
        checkHttps.setChecked(config.https)
    }

    fun saveSetting(host: String, port: Int, https: Boolean) {

        Thread {

            val app = application as App
            val dao = app.db.configDao()
            val respons: List<Config> = dao.getConfig()

            if (respons.size > 0) {
                dao.updateConfig((Config(1, host, port, https)))
            } else {
                dao.insertConfig(Config(1, host, port, https))
            }

            runOnUiThread{
                Toast.makeText(this, R.string.data_sucess_massege, Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()

    }

    fun getValuesConfig(){

        Thread {

            val app = application as App
            val dao = app.db.configDao()
            val response: List<Config> = dao.getConfig()


                if (response.size > 0) {
                    setValuesConfig(response.get(0))
                }

        }.start()

    }
}