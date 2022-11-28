package br.com.spaceinformatica.spacevendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import br.com.spaceinformatica.spacevendas.databinding.ActivityDadosItensBinding
import br.com.spaceinformatica.spacevendas.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

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

    }

    private fun initNavigation(){
        with(binding.bottomNavMenu) {setupWithNavController(navController)}
    }


//   private fun replaceFragment(fragment: Fragment){
//val fragmentManager = supportFragmentManager
//       val fragmentTrans
//
//   }

}
