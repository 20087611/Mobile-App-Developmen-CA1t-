package com.example.view.shop

import com.example.controller.ManagementController
import com.example.model.Product
import com.example.utils.PopupDialog
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import tornadofx.*
import kotlin.collections.ArrayList

// CustomerFragment类表示客户购物平台的用户界面。
class CustomerFragment: Fragment("Customer Shopping Platform") {

    // 定义界面上的各种控件及其属性
    var productField: TextField by singleAssign()
    var amountField: TextField by singleAssign()
    var searchField: TextField by singleAssign()
    private lateinit var filterCb: ComboBox<String>
    lateinit var table: TableView<Product>
    private var purchaseAmountString = SimpleStringProperty()
    private var purchaseNameString = SimpleStringProperty()
    private var productCategory = SimpleStringProperty()
    var itemPurchase :Product? = null

    // 定义一个下拉框，列出所有产品类别
    private val filterBox = FXCollections.observableArrayList(
        "All Category","Topping","Tea leaf","Syrup","Packaging","Equipment"
    )

    // 注入ManagementController以进行产品管理操作
    private val managementController: ManagementController by inject()

    // 定义此Fragment的主要布局和内容
    override val root = borderpane {

        left = vbox {
            form {
                setPrefSize(230.0, 600.0)
                fieldset {

                    // 用于搜索产品的输入框
                    field("SearchBox")
                    searchField = textfield(purchaseNameString) {
                        promptText = "Search Product Name or Category"
                    }

                    // “搜索”按钮，执行搜索操作
                    button("Search") {
                        spacing = 10.0
                        setOnAction {
                            if (searchField.text != null) {
                                managementController.searchProduct(searchField.text)
                                table.items = managementController.nameSearch
                            } else {
                                find<PopupDialog>(params = mapOf("message" to "You need to fill the box !!!")).openModal()
                            }
                        }
                    }

                    // 显示用户选择的产品的输入框，此框不可编辑
                    field("The Product you select:")
                    productField = textfield {
                        isEditable = false
                    }

                    // 用户输入要购买的产品数量的输入框
                    field("Amount Purchase")
                    amountField = textfield(purchaseAmountString) {
                        promptText = "Please Input purchase amount here"
                        filterInput { it.controlNewText.isInt() }
                    }

                    // “购买”按钮，执行购买操作
                    button("Purchase") {
                        spacing = 10.0
                        setOnAction {
                            if (productField.text != null && amountField.text != null) {
                                if (itemPurchase!!.number - amountField.text.toInt() >= 0) {
                                    managementController.purchaseProduct(itemPurchase!!, amountField.text.toInt())
                                    productField.text = null
                                    amountField.text = null
                                    find<PopupDialog>(params = mapOf("message" to "Purchase Success!!!")).openModal()
                                    itemPurchase = null
                                } else {
                                    find<PopupDialog>(params = mapOf("message" to "We don't have enough goods !!!")).openModal()
                                }
                            } else {
                                find<PopupDialog>(params = mapOf("message" to "You need to fill the box !!!")).openModal()
                            }

                        }
                    }
                }
            }
        }

        // 包含TableView的中心部分
        center = vbox {
            table = tableview<Product> {
                items = managementController.products
                columnResizePolicy = SmartResize.POLICY

                column("Name", String::class) {
                    value {
                        it.value.name
                    }
                    remainingWidth()
                }
                column("Category", String::class) {
                    value {
                        it.value.category
                    }
                    remainingWidth()
                }
                column("Amount", Int::class) {
                    value {
                        it.value.number
                    }
                    remainingWidth()
                }
                column("Price", Int::class) {
                    value {
                        it.value.price
                    }
                    remainingWidth()
                }

                // 当用户选择表中的一个产品时的操作
                onUserSelect(clickCount = 1) { product ->
                    itemPurchase = product
                    productField.text = itemPurchase?.name
                }
            }

            // 用于过滤产品类别的下拉框
            label("Category Filter")
            spacing = 10.0
            filterCb = combobox(productCategory,filterBox){
                setOnAction {
                    if(this.value == "All Category"){
                        table.items = managementController.products
                    }else{
                        managementController.searchProduct(this.value)
                        table.items = managementController.nameSearch
                    }
                }
            }

        }
    }
}
