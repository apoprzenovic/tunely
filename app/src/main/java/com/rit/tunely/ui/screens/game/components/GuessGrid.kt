package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.rit.tunely.ui.theme.BackgroundGray
import com.rit.tunely.ui.theme.BorderGray
import com.rit.tunely.ui.theme.PastelGray
import com.rit.tunely.ui.theme.PastelGreen
import com.rit.tunely.ui.theme.PastelYellow
import com.rit.tunely.util.Constants

enum class LetterStatus {
    CORRECT,
    PRESENT,
    ABSENT,
    EMPTY,
    TYPING
}

private fun String.titleWithoutSpaces() = this.replace(" ", "")

@Composable
fun GuessGrid(
    state: GameUiState,
    modifier: Modifier = Modifier
) {
    val trackTitle = state.track?.title?.uppercase() ?: return
    val titleWithoutSpaces = remember(trackTitle) { trackTitle.titleWithoutSpaces() }
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
                originalTitle = trackTitle,
                guess = guess,
                currentInput = currentInput,
                targetWordWithoutSpaces = titleWithoutSpaces,
                isSubmitted = guess != null
            )
        }
    }
}

@Composable
fun GuessRow(
    originalTitle: String,
    guess: String?,
    currentInput: String?,
    targetWordWithoutSpaces: String,
    isSubmitted: Boolean
) {
    val letterStatuses = remember(guess, targetWordWithoutSpaces, isSubmitted) {
        if (isSubmitted && guess != null) {
            evaluateGuess(guess, targetWordWithoutSpaces)
        } else {
            List(targetWordWithoutSpaces.length) { LetterStatus.EMPTY }
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var letterIndex = 0
        for (colIndex in originalTitle.indices) {
            val originalChar = originalTitle[colIndex]

            if (originalChar == ' ') {
                Spacer(Modifier.width(20.dp))
            } else {
                val char = currentInput?.getOrNull(letterIndex)
                    ?: guess?.getOrNull(letterIndex)

                val status = if (isSubmitted) {
                    letterStatuses.getOrElse(letterIndex) { LetterStatus.EMPTY }
                } else if (currentInput != null && letterIndex < currentInput.length) {
                    LetterStatus.TYPING
                } else {
                    LetterStatus.EMPTY
                }

                LetterBox(char = char, status = status)

                letterIndex++
            }
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
    val textColor = Color.Black

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

fun evaluateGuess(guess: String, targetWordWithoutSpaces: String): List<LetterStatus> {
    val statuses = MutableList(targetWordWithoutSpaces.length) { LetterStatus.ABSENT }
    val targetLetterCounts = targetWordWithoutSpaces.groupingBy { it }.eachCount().toMutableMap()

    for (i in targetWordWithoutSpaces.indices) {
        if (i < guess.length && guess[i] == targetWordWithoutSpaces[i]) {
            statuses[i] = LetterStatus.CORRECT
            targetLetterCounts[targetWordWithoutSpaces[i]] = targetLetterCounts.getOrDefault(targetWordWithoutSpaces[i], 0) - 1
        }
    }

    for (i in targetWordWithoutSpaces.indices) {
        if (statuses[i] != LetterStatus.CORRECT && i < guess.length) {
            val char = guess[i]
            if (targetLetterCounts.getOrDefault(char, 0) > 0) {
                statuses[i] = LetterStatus.PRESENT
                targetLetterCounts[char] = targetLetterCounts.getOrDefault(char, 0) - 1
            }
        }
    }

    return statuses
}