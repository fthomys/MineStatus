


package me.fthomys.minestatus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.ui.about.AboutScreen
import me.fthomys.minestatus.ui.detail.ServerDetailScreen
import me.fthomys.minestatus.ui.main.MainScreen
import me.fthomys.minestatus.ui.main.MainViewModel
import me.fthomys.minestatus.ui.theme.MineStatusTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            MineStatusTheme {
                val navController = rememberNavController()
                var serverToEdit by remember { mutableStateOf<Server?>(null) }

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = viewModel,
                            onServerClick = { server ->
                                navController.navigate("server_detail/${server.id}")
                                viewModel.refreshServerStatus(server)
                            },
                            serverToEdit = serverToEdit,
                            onServerToEditChange = { server ->
                                serverToEdit = server
                            },
                            onNavigateToAbout = {
                                navController.navigate("about")
                            }
                        )
                    }

                    composable(
                        route = "server_detail/{serverId}",
                        arguments = listOf(
                            navArgument("serverId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val serverId = backStackEntry.arguments?.getLong("serverId") ?: 0L
                        ServerDetailScreen(
                            serverId = serverId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onEditServer = { server ->

                                serverToEdit = server
                                navController.popBackStack()
                            },
                            onDeleteServer = { server ->
                                viewModel.deleteServer(server)
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("about") {
                        AboutScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
