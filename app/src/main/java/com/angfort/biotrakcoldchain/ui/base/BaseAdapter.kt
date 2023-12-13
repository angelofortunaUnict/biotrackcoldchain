package com.angfort.biotrakcoldchain.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.angfort.biotrakcoldchain.manager.BaseFunction
import com.angfort.biotrakcoldchain.manager.logPrint

/**
 * A generic RecyclerView adapter with view binding support.
 * @author TW - Angelo Fortuna
 */
abstract class BaseAdapter<ITEM, BINDING : ViewBinding>(
    val context: Context,
    private val itemList: ArrayList<ITEM> = ArrayList()
) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    lateinit var binding: BINDING

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * returns the list of items in the adapter.
     */
    fun getItemList() = itemList

    /**
     * Returns the view binding instance for the item view.
     * ```
     * E.g.
     * override fun setBindingClass(inflater: LayoutInflater, p0: ViewGroup) = BINDING.inflate(inflater, p0, false)
     * ```
     */
    abstract fun setBindingClass(inflater: LayoutInflater, p0: ViewGroup): BINDING

    /**
     * Binds the item data to the view.
     *
     * E.g.
     * override fun bindItem(item: Model) = binding.init(item)
     */
    abstract fun bindItem(item: ITEM, position: Int)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        binding = setBindingClass(inflater, p0)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item: ITEM = itemList[p1]
        bindItem(item, p1)
        changeSelected(item, binding)
    }

    open fun changeSelected(item: ITEM, binding: BINDING) {}

    /**
     * Removes the specified [ITEM] from the adapter.
     * @param item [ITEM]
     */
    fun removeItem(item: ITEM, then: BaseFunction? = null) {
        item.logPrint("item_print")
        val index = itemList.indexOf(item)
        itemList.remove(item)
        notifyItemRemoved(index)
        then?.invoke()
    }

    /**
     * Removes items that meet the specified condition from the adapter.
     * @param condition
     */
    fun removeItemWithCondition(condition: (ITEM) -> Boolean, then: BaseFunction? = null) {
        var index = 0
        itemList.removeIf {
            index = itemList.lastIndexOf(it)
            condition(it)
        }
        index.logPrint("g5_index")
        notifyItemRemoved(index)
        then?.invoke()
    }

    /**
     * Adds an [ITEM] to the adapter.
     * @param item [ITEM]
     */
    fun addItem(item: ITEM, then: BaseFunction? = null) {
        itemList.add(item)
        notifyItemInserted(itemCount - 1)
        then?.invoke()
    }

    /**
     * Adds an item at the specified position to the adapter.
     */
    open fun addItemAtPosition(item: ITEM, position: Int) {
        itemList.add(position, item)
        notifyItemInserted(itemCount - 2)
    }

    /**
     * Add list into the itemList
     * @param list ArrayList<[ITEM]>
     * @param retain if true list will be added to the existed [itemList] else [itemList] will be cleared first
     */
    fun addItemList(list: ArrayList<ITEM>?, retain: Boolean = true, then: BaseFunction? = null) {
        if (!retain) itemList.clear()
        itemList.addAll(list ?: arrayListOf())
        val count = list?.size
        notifyItemRangeChanged(itemCount.minus(count ?: 0) - 1, itemCount - 1)
        then?.invoke()
    }

    /**
     * Remove all from the itemList
     */
    fun removeAll() {
        itemList.clear()
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view)
}


abstract class GenericRecyclerViewAdapter<T, VB : ViewBinding>(
    private val data: MutableList<T> = mutableListOf(),
    private val layoutInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val onBind: (VB, T) -> Unit
) : RecyclerView.Adapter<GenericRecyclerViewAdapter<T, VB>.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaterClass = LayoutInflater.from(parent.context)
        val binding = layoutInflater.invoke(layoutInflaterClass, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        onBind(holder.binding, item)
        holder.binding.root.setOnClickListener {
            onItemClick(item, position)
        }
        holder.binding.root.setOnLongClickListener {
            onItemLongClick(item, position)
        }
    }

    override fun getItemCount(): Int = data.size

    fun add(item: T) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun addAll(items: List<T>) {
        data.addAll(items)
        notifyItemRangeInserted(data.size - items.size, items.size)
    }

    fun remove(item: T) {
        val index = data.indexOf(item)
        if (index != -1) {
            data.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    private var onItemClick: (T, Int) -> Unit = { _, _ -> }
    private var onItemLongClick: (T, Int) -> Boolean = { _, _ -> false }

    fun setOnItemClickListener(listener: (T, Int) -> Unit) {
        onItemClick = listener
    }

    fun setOnItemLongClickListener(listener: (T, Int) -> Boolean) {
        onItemLongClick = listener
    }
}