package com.mfitlab.etl

import com.mfitlab.etl.option.BigQueryToMixpanelJobOptions
import com.mfitlab.etl.pipeline.PipelineRunner
import org.apache.beam.sdk.options.PipelineOptionsFactory

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val options =
            (PipelineOptionsFactory.fromArgs(*args).withValidation().`as`(BigQueryToMixpanelJobOptions::class.java))
        PipelineRunner.run(options)
    }
}