package com.rit.tunely.util

import java.text.Normalizer

/**
 * Makes a track title guess-friendly.
 *
 * Steps:
 *   1. Cut everything inside parentheses.   e.g. "Love Song (Remix)" → "Love Song"
 *   2. Strip accents & diacritics.          e.g. "Águas" → "Aguas"
 *   3. Remove punctuation / emoji.          e.g. "Hit That!" → "Hit That"
 *   4. Collapse multiple spaces.
 */
fun sanitizeTitle(raw: String): String =
    Normalizer
        // remove (…)
        .normalize(raw.replace(Regex("\\s*\\(.*\\)"), ""), Normalizer.Form.NFD)
        // drop combining diacritical marks
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        // drop everything that isn't A-Z, a-z, 0-9 or space
        .replace(Regex("[^A-Za-z0-9 ]+"), "")
        // collapse & trim
        .replace(Regex("\\s+"), " ")
        .trim()
