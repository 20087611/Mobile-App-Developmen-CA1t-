package example.view

import com.example.view.shop.CustomerFragment
import com.example.view.shop.ManagerFragment
import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tabpane

class Header : View(){
    override val root = tabpane{
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab<ManagerFragment>()
        tab<CustomerFragment>()
    }

}