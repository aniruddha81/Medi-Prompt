package com.aniruddha81.mediprompt

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.aniruddha81.mediprompt.alarm.AlarmSchedulerImpl
import com.aniruddha81.mediprompt.models.NavItem
import com.aniruddha81.mediprompt.pages.aiAsstPage.AiAsst
import com.aniruddha81.mediprompt.pages.alarmPage.HomeScreen
import com.aniruddha81.mediprompt.pages.profilePage.Profile
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Ai Assistant", Icons.Default.Notifications),
        NavItem("Profile", Icons.Default.Person),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    val viewModel: AlarmActivityViewModel = hiltViewModel()
    val alarmSchedulerImpl = AlarmSchedulerImpl(LocalContext.current)
    when (selectedIndex) {
        0 -> HomeScreen(modifier, viewModel, alarmSchedulerImpl)
        1 -> AiAsst(modifier)
        2 -> Profile(modifier)
    }
}


















