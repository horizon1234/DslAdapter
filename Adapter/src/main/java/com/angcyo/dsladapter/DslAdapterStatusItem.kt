package com.angcyo.dsladapter

import android.view.ViewGroup

/**
 * [DslAdapter] 中, 控制情感图显示状态的 [Item]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/08/09
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */

/**
 * 具体Status即StateItem，在上一级中处理的是view如何切换，以及一些变量等
 * */
open class DslAdapterStatusItem : BaseDslStateItem() {

    companion object {
        /**正常状态, 切换到内容*/
        const val ADAPTER_STATUS_NONE = 0

        /**空数据*/
        const val ADAPTER_STATUS_EMPTY = 1

        /**加载中*/
        const val ADAPTER_STATUS_LOADING = 2

        /**错误*/
        const val ADAPTER_STATUS_ERROR = 3

        /**预加载状态, 不会触发加载中回调*/
        const val ADAPTER_STATUS_PRE_LOADING = 4
    }

    /**刷新回调*/
    var onRefresh: (itemHolder: DslViewHolder) -> Unit = {
        L.i("[DslAdapterStatusItem] 触发刷新")
    }

    //是否已经在刷新, 防止重复触发回调
    var _isRefresh = false

    init {
        itemStateLayoutMap[ADAPTER_STATUS_PRE_LOADING] = R.layout.base_loading_layout
        itemStateLayoutMap[ADAPTER_STATUS_LOADING] = R.layout.base_loading_layout
        itemStateLayoutMap[ADAPTER_STATUS_ERROR] = R.layout.base_error_layout
        itemStateLayoutMap[ADAPTER_STATUS_EMPTY] = R.layout.base_empty_layout
        //默认是数据状态
        itemState = ADAPTER_STATUS_NONE

        itemWidth = ViewGroup.LayoutParams.MATCH_PARENT
        itemHeight = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem)
    }

    //这个就是绑定发生改变时回调
    override fun _onBindStateLayout(itemHolder: DslViewHolder, state: Int) {
        super._onBindStateLayout(itemHolder, state)
        if (itemState == ADAPTER_STATUS_ERROR) {
            //出现错误后, 触击刷新
            itemHolder.clickItem {
                if (itemState == ADAPTER_STATUS_ERROR) {
                    _notifyRefresh(itemHolder)
                    itemDslAdapter?.setAdapterStatus(ADAPTER_STATUS_LOADING)
                }
            }
            itemHolder.click(R.id.base_retry_button) {
                itemHolder.clickView(itemHolder.itemView)
            }
        } else if (itemState == ADAPTER_STATUS_LOADING) {
            //当切换到Loading时直接触发刷新
            _notifyRefresh(itemHolder)
        } else {
            itemHolder.itemView.isClickable = false
        }
    }

    //刷新
    open fun _notifyRefresh(itemHolder: DslViewHolder) {
        if (!_isRefresh) {
            _isRefresh = true
            //高级函数，为了DSL效果
            itemHolder.post { onRefresh(itemHolder) }
        }
    }

    //状态改变回调
    override fun _onItemStateChange(old: Int, value: Int) {
        if (old != value && value != ADAPTER_STATUS_LOADING) {
            _isRefresh = false
        }
        super._onItemStateChange(old, value)
    }
}