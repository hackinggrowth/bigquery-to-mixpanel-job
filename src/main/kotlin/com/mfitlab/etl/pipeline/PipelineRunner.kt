package com.mfitlab.etl.pipeline

import com.google.api.services.bigquery.model.TableFieldSchema
import com.google.api.services.bigquery.model.TableSchema
import com.mfitlab.etl.option.BigQueryToMixpanelJobOptions
import com.mfitlab.etl.pipeline.fn.UserPropertiesTransformDoFn
import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.TypedRead.Method
import org.apache.beam.sdk.transforms.ParDo

object PipelineRunner {
    fun run(options: BigQueryToMixpanelJobOptions) {
        val pipeline = Pipeline.create(options)

        pipeline
            .apply(
                BigQueryIO.readTableRows()
                    .from(options.inputTable)
                    .withMethod(Method.EXPORT)
            )
            .apply(ParDo.of(UserPropertiesTransformDoFn()))
            .apply(
                BigQueryIO.writeTableRows()
                    .to(options.outputTable)
                    .withSchema(
                        TableSchema().setFields(
                            arrayListOf(
                                TableFieldSchema().setName("user_id").setType("STRING"),
                                TableFieldSchema().setName("occurrence_date").setType("STRING"),
                                TableFieldSchema().setName("last_updated_date").setType("STRING"),
                                TableFieldSchema().setName("insert_time").setType("STRING"),
                                TableFieldSchema().setName("user_properties").setType("JSON"),
                            )
                        )
                    )
                    .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                    .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE)
            )
        pipeline.run().waitUntilFinish()
    }
}