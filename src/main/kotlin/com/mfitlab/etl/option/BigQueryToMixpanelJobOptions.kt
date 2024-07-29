package com.mfitlab.etl.option

import org.apache.beam.sdk.options.Default
import org.apache.beam.sdk.options.Description
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.options.Validation

interface BigQueryToMixpanelJobOptions : PipelineOptions {
    @get:Description("Table to read from, specified as <project_id>:<dataset_id>.<table_id>")
    @get:Default.String("pj-da-recruit.analytics_404026502.users_20231113")
    var inputTable: String

    @get:Description("BigQuery table to write to, specified as <project_id>:<dataset_id>.<table_id>. The dataset must already exist.")
    @get:Validation.Required
    var outputTable: String
}