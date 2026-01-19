package com.sethvanstaden.dintest.ui.features.results

import com.sethvanstaden.dintest.data.local.entities.TestResultEntity
import com.sethvanstaden.dintest.domain.model.TestResultUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TestResultUiMapper {

    fun map(entity: TestResultEntity): TestResultUiModel {
        return TestResultUiModel(
            id = entity.id,
            score = entity.score,
            correctLabel = "${entity.correctCount}/${entity.totalRounds}",
            dateLabel = formatEpoch(entity.createdAt),
            difficultyPathLabel = entity.difficultyPath
        )
    }

    private val dateFormatter =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    private fun formatEpoch(ms: Long): String {
        return dateFormatter.format(Date(ms))
    }
}