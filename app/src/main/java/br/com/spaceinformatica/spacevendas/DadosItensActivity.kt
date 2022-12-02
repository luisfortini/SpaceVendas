package br.com.spaceinformatica.spacevendas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.spaceinformatica.spacevendas.databinding.ActivityDadosItensBinding
import br.com.spaceinformatica.spacevendas.utils.CLIENTE_ATIVO
import br.com.spaceinformatica.spacevendas.utils.getBuscaTotalPedido


class DadosItensActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDadosItensBinding
    private val navController by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_dados_itens)!!.findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDadosItensBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        getTotalPedido()

        binding.descCliente.text = "${CLIENTE_ATIVO?.codigoCliente!!} - ${CLIENTE_ATIVO?.fantasiaCliente!!}"

        binding.floatButtonAddProduto.setOnClickListener {
            startActivity(Intent(this,ProdutoActivity::class.java))
            finish()
        }

    }

    private fun initNavigation() {
        with(binding.bottomNavMenu) { setupWithNavController(navController) }
    }

    fun getTotalPedido() {
        Thread {
            val totalPedido = getBuscaTotalPedido(this)

            runOnUiThread {
                binding.totalPedido.text = "Total do Pedido: R$ $totalPedido"
            }
        }.start()
    }


}
