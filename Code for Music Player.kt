import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.Desktop
import java.io.File

data class Song(
    val title: String,
    val artist: String,
    val duration: String,
    val durationInSeconds: Int // New property to store duration in seconds
)

@Composable
fun MusicPlayerApp() {
    var currentSongIndex by remember { mutableStateOf(0) }
    var playlist by remember { mutableStateOf(mutableStateListOf<Song>()) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }

    Window(
        title = "Music Player",
        size = WindowSize(400.dp, 250.dp),
        position = WindowPosition.Center
    ) {
        MaterialTheme {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = playlist.getOrNull(currentSongIndex)?.title ?: "No Song Selected",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = playlist.getOrNull(currentSongIndex)?.artist ?: "",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Duration: ${playlist.getOrNull(currentSongIndex)?.duration ?: ""}",
                    style = MaterialTheme.typography.body2
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (currentSongIndex > 0) {
                            currentSongIndex--
                            isPlaying = false
                        }
                    }) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                    }

                    IconButton(onClick = {
                        isPlaying = !isPlaying
                    }) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause"
                        )
                    }

                    IconButton(onClick = {
                        if (currentSongIndex < playlist.size - 1) {
                            currentSongIndex++
                            isPlaying = false
                        }
                    }) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next")
                    }

                    IconButton(onClick = {
                        openFilePicker { files ->
                            files.forEach { file ->
                                val title = file.nameWithoutExtension
                                val song = Song(title, "Unknown Artist", "Unknown Duration", 0) // You can implement getting artist and duration information from the file
                                playlist.add(song)
                            }
                        }
                    }) {
                        Icon(Icons.Default.LibraryMusic, contentDescription = "Add to Playlist")
                    }
                }
            }
        }
    }
}

fun main() = application {
    MusicPlayerApp()
}

fun openFilePicker(onFilesSelected: (List<File>) -> Unit) {
    val files = Desktop.getDesktop().run {
        fileDialog(
            null,
            "Choose Music Files",
            FileDialogMode.Open,
            FileDialogProperties(),
        )
    }.files

    onFilesSelected(files.toList())
}
