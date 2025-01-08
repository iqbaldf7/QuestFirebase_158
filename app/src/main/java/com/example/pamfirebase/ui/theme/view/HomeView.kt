package com.example.pamfirebase.ui.theme.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pamfirebase.R
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.ui.theme.viewmodel.HomeUiState
import com.example.pamfirebase.ui.theme.viewmodel.HomeViewModel
import com.example.pamfirebase.ui.theme.viewmodel.PenyediaViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry:() -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHome.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = viewModel::getMhs
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Mahasiswa")
            }
        },
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.mhsUiState,
            retryAction = {viewModel.getMhs()}, modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.getMhs()
            }
        )
    }
}
@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier,
    onDeleteClick: (Mahasiswa) -> Unit,
    onDetailClick: (String) -> Unit
){
    when(homeUiState){
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeUiState.Success ->
            if(homeUiState.mhs.isEmpty()){
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Tidak ada data Mahasiswa")
                }
            }else{
                MhsLayout(
                    mahasiswa = homeUiState.mhs,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.nim) },
                    onDeleteClick = {
                        onDeleteClick(it)
                    }
                )
            }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}



@Composable
fun OnLoading(modifier: Modifier = Modifier){
    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center

    )
}



@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            painter  = painterResource(id = R.drawable.error), contentDescription = null
        )
        Text(text = stringResource(R.string.loading), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}



@Composable
fun MhsLayout(
    mahasiswa: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onDetailClick: (Mahasiswa) -> Unit,
    onDeleteClick: (Mahasiswa) -> Unit = {}
){
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mahasiswa){kontak->
            MhsCard(
                mahasiswa = kontak,
                modifier = Modifier.fillMaxWidth()
                    .clickable{ onDetailClick(kontak) },
                onDeleteClick ={
                    onDeleteClick(kontak)
                }
            )
        }
    }
}


@Composable
fun MhsCard(
    mahasiswa: Mahasiswa,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {}
){
    var deleteConfirmationRequared by rememberSaveable { mutableStateOf(false) }
    Card (
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = mahasiswa.nama,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.width(1f))
                IconButton(onClick = { deleteConfirmationRequared = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
                if(deleteConfirmationRequared){
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            onDeleteClick(mahasiswa)
                            deleteConfirmationRequared = false
                        },
                        onDeleteCancel = { deleteConfirmationRequared = false }
                    )
                }
            }
            Text(
                text = mahasiswa.nim,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = mahasiswa.kelas,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = mahasiswa.alamat,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(onDismissRequest = {/*Do Nothing*/},
        title = { Text("Delete Data") },
        text = {Text("Apakah anda yakin ingin menghapus data ini?")},
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("Yes")
            }
        })
}