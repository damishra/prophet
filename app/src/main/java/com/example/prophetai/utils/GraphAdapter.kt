package com.example.prophetai.utils

/**
 * @author Dishant Mishra
 */

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.robinhood.spark.SparkAdapter
import org.json.JSONObject


class GraphAdapter(private val from: String, private val to: String) : SparkAdapter() {
    private var yData: FloatArray
    private val key = "QJKZI2R7JR0M1W8P"
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }

    init {
        yData = FloatArray(300)
        val url = "https://www.alphavantage.co/query" +
                "?function=FX_MONTHLY" +
                "&from_symbol=$from" +
                "&to_symbol=$to" +
                "&apikey=$key"
        url.httpGet().response { req, res, result ->
            val (bytes, err) = result
            if (err != null) {
                Log.d("error", err.message!!)
            }
            if (bytes != null) {
                val body = JSONObject(JSONObject(String(bytes))
                    .get("Time Series FX (Monthly)").toString())
                Log.d("body", body.length().toString())
                yData = FloatArray(body.length())
                var i = 0
                body.keys().forEachRemaining { key ->
                    yData[i] = JSONObject(body.get(key)
                        .toString())
                        .get("1. open").toString().toFloat()
                    i++
                }
                notifyDataSetChanged()
            } else {
                Log.d("fail", "request failed")
            }
        }
    }
}
