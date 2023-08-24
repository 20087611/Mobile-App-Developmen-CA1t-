package com.example.controller

import com.example.dao.ProductDao
import com.example.model.Product
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.asObservable

class ManagementController : Controller() {
    // 实例化数据访问对象
    private val dao = ProductDao()

    // 使用SortedFilteredList存储所有产品，这允许排序和过滤功能
    val products = SortedFilteredList(items = listAllProducts().asObservable())

    // 用于存储名称搜索的结果
    var nameSearch = FXCollections.observableArrayList<Product>()

    // 添加新产品
    fun addProduct(name: String, category: String, number: Int, price: Int) {
        val product = Product(name, category, number, price)
        dao.addProduct(product) // 数据库中添加产品
        products.add(product)   // 当前产品列表中添加产品
    }

    // 获取所有产品
    private fun listAllProducts(): List<Product> {
        return dao.listProducts()
    }

    // 删除产品
    fun deleteProduct(item: Product) {
        dao.deleteProduct(item) // 数据库中删除产品
        products.removeAll(item) // 当前产品列表中删除产品
        nameSearch.removeAll(item) // 搜索结果中删除产品（如果存在）
    }

    // 更新产品信息
    fun updateProduct(oldItem: Product, number: Int, price: Int) {
        val product = Product(oldItem.name, oldItem.category, number, price)
        dao.updateProduct(oldItem.name, oldItem.category, product) // 数据库中更新产品
        with(products) {
            remove(oldItem) // 删除旧产品信息
            add(product)    // 添加新产品信息
        }
    }

    // 根据名称搜索产品
    fun searchProduct(productName: String) {
        nameSearch = dao.searchProduct(productName) as ObservableList<Product>?
    }

    // 购买产品
    fun purchaseProduct(product: Product, purchaseAmount: Int) {
        val newProduct = Product(product.name, product.category, product.number - purchaseAmount, product.price)

        if (newProduct.number == 0) {
            deleteProduct(product)
        } else {
            dao.purchaseProduct(product, purchaseAmount) // 更新数据库中的产品信息
            with(products) {
                remove(product)    // 从当前列表中删除旧产品信息
                add(newProduct)    // 添加新产品信息
            }
            with(nameSearch) {
                remove(product)    // 从搜索结果中删除旧产品信息
                add(newProduct)    // 添加新产品信息
            }
        }
    }
}
