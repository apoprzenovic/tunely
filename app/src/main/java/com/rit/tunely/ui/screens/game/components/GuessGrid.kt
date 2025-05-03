package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rit.tunely.data.state.GameUiState
import com.rit.tunely.ui.screens.game.BackgroundGray
import com.rit.tunely.ui.screens.game.BorderGray
import com.rit.tunely.ui.screens.game.PastelGray
import com.rit.tunely.ui.screens.game.PastelGreen
import com.rit.tunely.ui.screens.game.PastelYellow
import com.rit.tunely.util.Constants

enum class LetterStatus {
    CORRECT,
    PRESENT,
    ABSENT,
    EMPTY,
    TYPING
}

@Composable
fun GuessGrid(
    state: GameUiState,
    modifier: Modifier = Modifier
) {
    val trackTitle = state.track?.title?.uppercase() ?: return
    val titleLength = trackTitle.length
    val totalGuesses = Constants.GUESSES_TOTAL

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (rowIndex in 0 until totalGuesses) {
            val guess = state.guesses.getOrNull(rowIndex)
            val isCurrentAttemptRow = rowIndex == state.currentAttemptIndex && !state.gameFinished
            val currentInput = if (isCurrentAttemptRow) state.currentGuess.uppercase() else null

            GuessRow(
                wordLength = titleLength,
                guess = guess,
                currentInput = currentInput,
                targetWord = trackTitle,
                isSubmitted = guess != null
            )
        }
    }
}

@Composable
fun GuessRow(
    wordLength: Int,
    guess: String?,
    currentInput: String?,
    targetWord: String,
    isSubmitted: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val letterStatuses = remember(guess, targetWord, isSubmitted) {
            if (isSubmitted && guess != null) {
                evaluateGuess(guess, targetWord)
            } else {
                List(wordLength) { LetterStatus.EMPTY }
            }
        }

        for (colIndex in 0 until wordLength) {
            val char = currentInput?.getOrNull(colIndex)
                ?: guess?.getOrNull(colIndex)
            val status = if (isSubmitted) {
                letterStatuses.getOrElse(colIndex) { LetterStatus.EMPTY }
            } else if (currentInput != null && colIndex < currentInput.length) {
                LetterStatus.TYPING
            } else {
                LetterStatus.EMPTY
            }

            LetterBox(char = char, status = status)
        }
    }
}

@Composable
fun LetterBox(char: Char?, status: LetterStatus) {
    val backgroundColor = when (status) {
        LetterStatus.CORRECT -> PastelGreen
        LetterStatus.PRESENT -> PastelYellow
        LetterStatus.ABSENT -> PastelGray
        LetterStatus.TYPING -> BackgroundGray.copy(alpha = 0.5f)
        LetterStatus.EMPTY -> BackgroundGray
    }
    val border = BorderStroke(1.dp, if (status == LetterStatus.EMPTY) BorderGray else Color.Transparent)
    val textColor = if (status == LetterStatus.EMPTY || status == LetterStatus.TYPING) Color.Black else Color.Black

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .border(border, shape = MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString() ?: "",
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

fun evaluateGuess(guess: String, targetWord: String): List<LetterStatus> {
    val targetUpper = targetWord.uppercase()
    val guessUpper = guess.uppercase()
    val statuses = MutableList(targetUpper.length) { LetterStatus.ABSENT }
    val targetLetterCounts = targetUpper.groupingBy { it }.eachCount().toMutableMap()

    for (i in targetUpper.indices) {
        if (i < guessUpper.length && guessUpper[i] == targetUpper[i]) {
            statuses[i] = LetterStatus.CORRECT
            targetLetterCounts[targetUpper[i]] = targetLetterCounts.getOrDefault(targetUpper[i], 0) - 1
        }
    }

    for (i in targetUpper.indices) {
        if (statuses[i] != LetterStatus.CORRECT && i < guessUpper.length) {
            val char = guessUpper[i]
            if (targetLetterCounts.getOrDefault(char, 0) > 0) {
                statuses[i] = LetterStatus.PRESENT
                targetLetterCounts[char] = targetLetterCounts.getOrDefault(char, 0) - 1
            }
        }
    }

    return statuses
}