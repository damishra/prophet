package com.example.prophetai

/**
 * @author Dishant Mishra
 */

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.prophetai.utils.GraphAdapter
import com.example.prophetai.utils.PickerAdapter
import com.robinhood.spark.SparkView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import kotlinx.android.synthetic.main.fragment_data.*
import kotlinx.android.synthetic.main.fragment_graph.*
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

    private var to = "USD"
    private var from = "EUR"
    private val key = "QJKZI2R7JR0M1W8P"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pickerLayoutManagerFromCurr = PickerLayoutManager(
            this, PickerLayoutManager.HORIZONTAL, false)
        pickerLayoutManagerFromCurr.isChangeAlpha = true;
        pickerLayoutManagerFromCurr.scaleDownBy = 0.99f;
        pickerLayoutManagerFromCurr.scaleDownDistance = 0.8f;

        val snapHelperFromCurr: SnapHelper = LinearSnapHelper()
        snapHelperFromCurr.attachToRecyclerView(fromCurrency)

        fromCurrency.apply {
            setHasFixedSize(true)
            layoutManager = pickerLayoutManagerFromCurr
        }

        val adapter = PickerAdapter(resources.getStringArray(R.array.currencies))
        fromCurrency.adapter = adapter

        val pickerLayoutManagerToCurr = PickerLayoutManager(
            this, PickerLayoutManager.HORIZONTAL, false)

        pickerLayoutManagerToCurr.isChangeAlpha = true;
        pickerLayoutManagerToCurr.scaleDownBy = 0.99f;
        pickerLayoutManagerToCurr.scaleDownDistance = 0.8f;

        val snapHelperToCurr: SnapHelper = LinearSnapHelper()
        snapHelperToCurr.attachToRecyclerView(toCurrency)

        toCurrency.apply {
            setHasFixedSize(true)
            layoutManager = pickerLayoutManagerToCurr
        }

        toCurrency.adapter = adapter

        tickTo.setCharacterLists(TickerUtils.provideAlphabeticalList())

        tickTo.animationDuration = 500
        tickTo.animationInterpolator = OvershootInterpolator()
        tickTo.setPreferredScrollingDirection(TickerView.ScrollingDirection.ANY)
        tickTo.textColor = resources.getColor(R.color.colorPrimary)
        tickTo.typeface = resources.getFont(R.font.circe_rounded)

        pickerLayoutManagerFromCurr.setOnScrollStopListener { view: View? ->
            val p = view as TextView?
            from = p!!.text.toString()
        }

        pickerLayoutManagerToCurr.setOnScrollStopListener { view: View? ->
            val p = view as TextView?
            to = p!!.text.toString()
        }

        sparkview.lineColor = resources.getColor(R.color.colorPrimary)
        sparkview.isScrubEnabled = true
        sparkview.adapter = GraphAdapter(from, to)
        sparkview.scrubLineColor = resources.getColor(R.color.colorPrimaryDark)

        tickVal.setCharacterLists(TickerUtils.provideNumberList())
        tickVal.animationDuration = 500
        tickVal.animationInterpolator = OvershootInterpolator()
        tickVal.setPreferredScrollingDirection(TickerView.ScrollingDirection.ANY)
        tickVal.textColor = resources.getColor(R.color.colorPrimary)
        tickVal.typeface = resources.getFont(R.font.circe_rounded)
        sparkview.scrubListener =
            SparkView.OnScrubListener { value -> try {
                    tickVal.text = value?.toString()!!
                } catch (e: KotlinNullPointerException) {

                } catch (e: NullPointerException) {

                }
            }

        /**
        var graphAdapter = GraphAdapter(getData()!!)
        sparkview.adapter = graphAdapter
        **/

        submit.setOnClickListener {
            /**
            graphAdapter = GraphAdapter(getData()!!)
            sparkview.adapter = graphAdapter
            **/
            tickTo.text = "$from v $to"
            sparkview.adapter = GraphAdapter(from, to)
        }

    }

}
