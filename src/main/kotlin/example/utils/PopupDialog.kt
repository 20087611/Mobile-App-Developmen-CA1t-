package com.example.utils

import com.example.Styles
import tornadofx.*

/**
 * 一个TornadoFX Fragment，用于显示弹出对话框。
 *
 * 该对话框包含一个带有传入消息的标签。
 *
 * @property message 通过参数传递给此Fragment的消息文本。
 */
class PopupDialog : Fragment() {

    // 从外部接收的消息参数
    val message: String by param()

    // 对话框的主要UI布局
    override val root = vbox {

        // 在此垂直布局盒子(vbox)内添加一个标签，其文本设置为传入的消息。
        label(message) {
            // 为标签添加样式
            addClass(Styles.heading)
        }
    }
}
