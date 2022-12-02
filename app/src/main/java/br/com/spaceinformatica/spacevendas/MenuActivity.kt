package br.com.spaceinformatica.spacevendas

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.adapter.MenuAdapter
import br.com.spaceinformatica.spacevendas.databinding.ActivityMenuBinding
import br.com.spaceinformatica.spacevendas.model.ItemMenu
import br.com.spaceinformatica.spacevendas.utils.COLABORADOR

class MenuActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_item_home ,R.id.nav_item_clientes, R.id.nav_item_produtos), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        createMenu()

        val navHeader = navView.getHeaderView(0)
        val textUser = navHeader.findViewById<TextView>(R.id.desc_user_menu)
        textUser.text = "Bem vindo, $COLABORADOR"

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun createMenu(){

        val itensMenu = mutableListOf<ItemMenu>()

        itensMenu.add(
            ItemMenu(1,
            R.drawable.ic_baseline_people,
            R.string.client)
        )

        itensMenu.add(
            ItemMenu(2,
                R.drawable.ic_baseline_ballot,
                R.string.product)
        )

        itensMenu.add(
            ItemMenu(3,
                R.drawable.ic_baseline_shopping_cart,
                R.string.pedidos)
        )
        itensMenu.add(
            ItemMenu(4,
                R.drawable.ic_baseline_help_center,
                R.string.help)
        )

        val adapter = MenuAdapter(this, itensMenu) {id ->
            when(id){
                1 -> startActivity(Intent(this, ClienteActivity::class.java))
                2 -> startActivity(Intent(this, ProdutoActivity::class.java))
                3 -> Toast.makeText(this, "Aqui deve abrir a lista de pedidos", Toast.LENGTH_SHORT).show()
                4 -> Toast.makeText(this, "Aqui deve abrir o manual de uso do app", Toast.LENGTH_SHORT).show()
            }
        }

        val rv = findViewById<RecyclerView>(R.id.rv_menu_home)
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(this, 2)


    }


}