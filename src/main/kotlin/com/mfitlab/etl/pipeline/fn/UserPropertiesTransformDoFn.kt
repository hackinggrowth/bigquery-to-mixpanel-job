package com.mfitlab.etl.pipeline.fn

import com.google.api.services.bigquery.model.TableRow
import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.LoggerFactory

class UserPropertiesTransformDoFn : DoFn<TableRow, TableRow>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ProcessElement
    fun processElement(c: ProcessContext) {
        val row = c.element()

        val values = row.getValue("user_properties") as List<*>

        val geo = row.getValue("geo") as Map<*, *>?
        val device = row.getValue("device") as Map<*, *>?

        val result = mutableMapOf<String, Any>(
            // 기본 속성
            "\$country_code" to (geo?.get("country") as String? ?: ""),
            "\$city" to (geo?.get("city") as String? ?: ""),
            "\$region" to (geo?.get("region") as String? ?: ""),
            "\$device" to (device?.get("mobile_model_name") as String? ?: ""),
            "\$os" to (device?.get("operating_system") as String? ?: ""),
        )

        values.forEach {
            val item = it as Map<*, *>
            val key = item["key"] as String
            val value = item["value"] as Map<*, *>
            try {
                result[key] = when {
                    value["int_value"] != null -> (value["int_value"] as String).toLong()
                    value["string_value"] != null -> value["string_value"] as String
                    value["double_value"] != null -> value["double_value"] as Double
                    value["float_value"] != null -> (value["float_value"] as String).toFloat()
                    else -> value
                }
            } catch (e: Exception) {
                logger.error(e.message)
            }
        }

        val newRow = TableRow()
            .set("user_id", row.getValue("user_id"))
            .set("occurrence_date", row.getValue("occurrence_date"))
            .set("last_updated_date", row.getValue("last_updated_date"))
            .set("insert_time", row.getValue("occurrence_date"))
            .set("user_properties", result)

        c.output(newRow)
    }
}