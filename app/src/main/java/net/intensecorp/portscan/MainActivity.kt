package net.intensecorp.portscan

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import net.intensecorp.portscan.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val numThreads: Int = 16
    private var host: String = "www.google.com"

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_startscan -> {
                host = hostx.text.toString()
                if (host.isEmpty()) {
                    this.scrollView2.portScanResults.setText("No host specified")
                    return@OnNavigationItemSelectedListener false
                }
                this.scrollView2.portScanResults.setText("Scan results for:\n$host\n============")

                val executor = Executors.newFixedThreadPool(numThreads)

                val commonlyUsedPorts = intArrayOf(22, 80, 443, 3306, 21, 25, 53, 8080, 8988, 9999)
                for (port in commonlyUsedPorts) {
                    val worker = PortScan(this.scrollView2.portScanResults, host, port)
                    executor.execute(worker)
                }
                executor.shutdown()
                return@OnNavigationItemSelectedListener false
            }
            R.id.navigation_about -> {
                this.scrollView2.portScanResults.setText("Created by Pushpendra Yadav")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

}
