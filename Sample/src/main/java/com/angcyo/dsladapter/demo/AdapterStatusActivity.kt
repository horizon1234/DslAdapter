package com.angcyo.dsladapter.demo

import android.util.Log
import com.angcyo.dsladapter.DslAdapterStatusItem
import com.angcyo.dsladapter.DslViewHolder
import com.angcyo.dsladapter.demo.R

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/10/16
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
const val TAG = "zyh"

class AdapterStatusActivity : BaseRecyclerActivity() {

    override fun getBaseLayoutId(): Int {
        return R.layout.activity_adatper_startus
    }

    override fun onInitBaseLayoutAfter() {
        super.onInitBaseLayoutAfter()

        dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_LOADING)

        dslViewHolder.postDelay(1000) {
            dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_NONE)
            dslAdapter.来点数据()
        }

        initAdapterStatus()

        dslAdapter.dslAdapterStatusItem.onRefresh = {
            Log.i(TAG, "onInitBaseLayoutAfter: ")
            dslViewHolder.postDelay(2000){
                dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_NONE)
                onRefresh()
            }
        }
        dslAdapter.dslAdapterStatusItem.onItemStateChange = {from: Int, to: Int ->  
            
        }
        dslAdapter.dslAdapterStatusItem.onBindStateLayout = { itemHolder: DslViewHolder, state: Int ->  
            
        }
    }

    private fun initAdapterStatus() {
        dslViewHolder.click(R.id.normal) {
            dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_NONE)
        }
        dslViewHolder.click(R.id.empty) {
            dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_EMPTY)
        }
        dslViewHolder.click(R.id.loading) {
            dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_LOADING)
        }
        dslViewHolder.click(R.id.error) {
            dslAdapter.setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_ERROR)
        }
    }


    override fun onRefresh() {
        super.onRefresh()
        dslAdapter.resetItem(listOf())
        dslAdapter.来点数据()
    }
}
