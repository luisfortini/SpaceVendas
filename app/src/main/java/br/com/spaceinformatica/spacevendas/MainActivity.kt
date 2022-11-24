package br.com.spaceinformatica.spacevendas


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.FilialModel
import br.com.spaceinformatica.spacevendas.model.LoginBody
import br.com.spaceinformatica.spacevendas.utils.FILIAL
import br.com.spaceinformatica.spacevendas.utils.TOKEN
import br.com.spaceinformatica.spacevendas.utils.createUrlBase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var inputUser: EditText
    private lateinit var inputPass: EditText
    private lateinit var spinnerFilial: Spinner
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

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

        Thread{
            createUrlBase(this)
        }.start()


        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE

        inputUser = findViewById(R.id.login_user)
        inputUser.setSelectAllOnFocus(true)
        inputUser.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                getFiliais(inputUser.text.toString())
            }
        }

        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun openSettings() {
        val intent = Intent(this, ConfigActivity::class.java)
        startActivity(intent)
    }

    private fun getFiliais(user: String) {

        progressBar.visibility = View.VISIBLE
        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .getFiliais(user)
            .enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {

                        val data = response.body()?.string()
                        if (JSONObject(data!!).getBoolean("resposta")) {
                            val filialObject = JSONObject(data).getJSONArray("dados")

                            val type = object : TypeToken<List<FilialModel>>() {}.type
                            val filialList =
                                Gson().fromJson<List<FilialModel>>(filialObject.toString(), type)
                            spinnerFilial = findViewById(R.id.spinner_filial)
                            val adapterFilial =
                                ArrayAdapter(this@MainActivity,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    filialList)
                            spinnerFilial.adapter = adapterFilial


//                            val filialList = mutableListOf<FilialModel>()
//                            for (i in 0 until filialObject.length()) {
//                                val filial = filialObject.getJSONObject(i)
//                                val filialCodigo = filial.getInt("Filial")
//                                val filialFantasia = filial.getString("Fantasia")
//                                val filialModel = FilialModel(filialCodigo, filialFantasia)
//                                filialList.add(filialModel)
//                            }
//
//                            spinnerFilial.adapter = FilialAdapter(this@MainActivity, filialList)


                        } else {
                            val resposta = JSONObject(data).getString("dados")
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, resposta, Toast.LENGTH_LONG).show()
                        }

                    } else {
                        val data = response.errorBody()?.string()
                        Log.i("Teste", data.toString())
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "Falha na conexão! Verifique a internet ou as configurações!",
                        Toast.LENGTH_LONG
                    ).show()

                }

            })
    }

    private fun login() {
        progressBar.visibility = View.VISIBLE

        inputPass = findViewById(R.id.login_pass)
        val user = inputUser.text.toString()
        val pass = inputPass.text.toString()
        val filialModel: FilialModel =
            spinnerFilial.adapter.getItem(spinnerFilial.selectedItemPosition) as FilialModel
//        val filialId = spinnerFilial.selectedItemId.toInt()


        val loginBody = LoginBody(user, pass, filialModel.filCodigo)

        val bodyRequest = Gson().toJson(loginBody).toRequestBody("application/json".toMediaType())

        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .postLogin(bodyRequest)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val data = JSONObject(response.body()?.string()!!)
                        val usuario = data.getString("login")
                        val filial = data.getInt("filialCodigo")
                        TOKEN = data.getString("token")
                        FILIAL = filial.toString()

                        val intent = Intent(this@MainActivity, ClienteActivity::class.java)
                        intent.putExtra("usuario", usuario)
                        intent.putExtra("filial", filial)
                        startActivity(intent)

                    } else {
                        val data = response.errorBody()?.string()
                        val messageUser = JSONObject(data!!).getString("mensagemUsuario")
                        Toast.makeText(this@MainActivity, messageUser, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "Falha na conexão! Verifique a internet ou as configurações!",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

    }
}

