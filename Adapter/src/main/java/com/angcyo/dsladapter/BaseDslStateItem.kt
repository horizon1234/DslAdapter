package com.angcyo.dsladapter

import android.view.View

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/10/21
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
abstract class BaseDslStateItem : DslAdapterItem() {

    /**
     * [key] 是状态
     * [value] 是对应的布局文件id
     * */
    val itemStateLayoutMap = hashMapOf<Int, Int>()

    /**当前的布局状态*/
    var itemState = -1
        set(value) {
            val old = field
            field = value
            _onItemStateChange(old, value)
        }

    /**是否将激活状态item*/
    open var itemStateEnable: Boolean = true

    //DSL写法

    /**当状态改变时回调*/
    var onItemStateChange: (from: Int, to: Int) -> Unit = { _, _ -> }

    /**绑定不同状态的布局*/
    var onBindStateLayout: (itemHolder: DslViewHolder, state: Int) -> Unit = { _, _ -> }

    init {
        itemLayoutId = R.layout.item_base_state
        itemSpanCount = -1
    }

    /**
     * recyclerView的onBindViewHolder时回调
     * DslViewHolder就是为了拿到这个view
     * itemPosition是pos
     * adapterItem这个其实数据
     * */
    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem)
        //这个有必要清空，因为这个item就是一个空的viewGroup，然后把几种非正常状态的view
        //给加到这个里面去，然后切换时，会回调这个方法。
        itemHolder.clear()
        val stateLayout = itemStateLayoutMap[itemState]
        //其实一想，这个stateItem也是一个Item，所以就有点东西，需要特殊处理
        itemHolder.group(R.id.item_wrap_layout)?.apply {
            if (stateLayout == null) {
                //没有状态对应的布局文件
                removeAllViews()
            } else {
                var inflate = true
                if (childCount > 0) {
                    val tagLayout = getChildAt(0).getTag(R.id.tag)
                    if (tagLayout == stateLayout) {
                        //已经存在相同状态的布局
                        inflate = false
                    } else {
                        removeAllViews()
                    }
                }

                //填充布局, 如果需要
                if (inflate) {
                    inflate(stateLayout, true)
                    val view = getChildAt(0)
                    view.visibility = View.VISIBLE
                    view.setTag(R.id.tag, stateLayout)
                }

                _onBindStateLayout(itemHolder, itemState)
            }
        }
    }

    /**
     * 这里有个值得学习的点，就是利用高级函数，来简化回调函数的写法
     * 比如这里绑定状态变化和状态变化的回调，如果使用接口，还要定义接口
     * 这里直接使用高级函数回调，onBindStateLayout就是一个函数
     * */

    /**
     * 这个open方法只有这一处调用，下面的全是override
     * */
    open fun _onBindStateLayout(itemHolder: DslViewHolder, state: Int) {
        onBindStateLayout(itemHolder, state)
    }

    open fun _onItemStateChange(old: Int, value: Int) {
        if (old != value) {
            onItemStateChange(old, value)
        }
    }

    /**是否处于状态显示模式*/
    open fun isInStateLayout() = itemEnable && itemStateEnable && itemState > 0
}