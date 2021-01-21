package hui.ait.finalproject

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import hui.ait.finalproject.data.AppDatabase
import hui.ait.finalproject.data.Location
import kotlinx.android.synthetic.main.activity_budget.*
import java.util.ArrayList

class BudgetActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private var loc3 = ""

    private lateinit var locItem: Location

    private var food = 5000
    private var travel = 5000
    private var lodging = 5000
    private var entertainment = 5000

    private var budgetRem = 10000

    companion object {
        val LOCATION_NAME = "LOCATION_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        if (getIntent().getExtras() != null && intent.extras.containsKey(LOCATION_NAME)){
            loc3 = intent.getStringExtra(LOCATION_NAME)
        }


        chartBudget.setEntryLabelTextSize(12f)
        chartBudget.description.isEnabled = false
        chartBudget.setCenterTextSize(15f)
        chartBudget.setCenterText(getString(R.string.spendingbreakdown))

        btnPlus.setOnClickListener {
            if (etBudget.text.toString() != "") {

                locItem.budget += etBudget.text.toString().toInt()

                Thread {
                    AppDatabase.getInstance(this@BudgetActivity).LocationDao().updateLocation(locItem)
                }.start()

                budgetRem += etBudget.text.toString().toInt()
                tvBudget.text = getString(R.string.budgRem) + budgetRem.toString()
            }
        }

        btnMinus.setOnClickListener {
            if (etBudget.text.toString() != "") {
                if (etBudget.text.toString().toInt() <= budgetRem) {

                    locItem.budget -= etBudget.text.toString().toInt()

                    budgetRem -= etBudget.text.toString().toInt()
                    tvBudget.text =  getString(R.string.budgRem) + budgetRem.toString()

                    Thread {
                        AppDatabase.getInstance(this@BudgetActivity).LocationDao().updateLocation(locItem)
                    }.start()

                } else {
                    Toast.makeText(this@BudgetActivity,
                        getString(R.string.OverBudg), Toast.LENGTH_LONG).show()
                }
            }
        }

        Thread{
            if (AppDatabase.getInstance(this).LocationDao().findLocationWithName(loc3) == null) {
                AppDatabase.getInstance(this).LocationDao().insertLocation(Location(
                    loc3, 1000, 0, 0, 0, 0))
            }
            locItem = AppDatabase.getInstance(this@BudgetActivity).LocationDao().findLocationWithName(loc3)

            food = locItem.food
            travel = locItem.travel
            lodging = locItem.lodging
            entertainment = locItem.entertainment
            budgetRem = locItem.budget - food - travel - lodging - entertainment

            runOnUiThread {
                tvBudget.text =  getString(R.string.budgRem) + budgetRem.toString()
            }

            updateChart()

        }.start()



        btnUpdate.setOnClickListener {
            updateBudget()
        }
        chartBudget.setOnChartValueSelectedListener(this)
    }

    private fun updateBudget() {
        var total = 0

        var nFood  = 0
        var nTravel = 0
        var nFun = 0
        var nLodging = 0

        if (etFood.text.toString() != "") {

            total += etFood.text.toString().toInt()
            nFood = etFood.text.toString().toInt()
        }

        if (etTravel.text.toString() != "") {
            total += etTravel.text.toString().toInt()
            nTravel = etTravel.text.toString().toInt()

        }

        if (etLodging.text.toString() != "") {
            total += etLodging.text.toString().toInt()
            nLodging = etLodging.text.toString().toInt()

        }

        if (etFun.text.toString() != "") {
            total += etFun.text.toString().toInt()
            nFun = etFun.text.toString().toInt()
        }

        if (total <= budgetRem) {
            food += nFood
            locItem.food = food
            budgetRem -= nFood

            travel += nTravel
            locItem.travel = travel
            budgetRem -= nTravel

            lodging += nLodging
            locItem.lodging = lodging
            budgetRem -= nLodging

            entertainment += nFun
            locItem.entertainment = entertainment
            budgetRem -= nFun

            Thread {
                AppDatabase.getInstance(this).LocationDao().updateLocation(locItem)
            }.start()

            updateChart()
            tvBudget.text =  getString(R.string.budgRem) + budgetRem.toString()
        } else {
            Toast.makeText(this@BudgetActivity,
                getString(R.string.overBudg), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateChart() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(food.toFloat(), getString(R.string.food)))
        entries.add(PieEntry(travel.toFloat(), getString(R.string.travel)))
        entries.add(PieEntry(lodging.toFloat(), getString(R.string.lodge)))
        entries.add(PieEntry(entertainment.toFloat(), getString(R.string.entertainment)))

        val dataSet = PieDataSet(entries, getString(R.string.budg))

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f


        val colors = ArrayList<Int>()
        colors.add(0x77e555b6)
        colors.add(0x77d3d450)
        colors.add(0x77ac8af6)
        colors.add(0x775cc5da)

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(16f)
        data.setValueTextColor(Color.BLUE)


        chartBudget.data = data

        chartBudget.highlightValues(null)

        chartBudget.invalidate()
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

}
