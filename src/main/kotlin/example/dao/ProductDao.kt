package com.example.dao

import com.example.model.Product

class ProductDao {
    /**
     * 添加一个新的产品到数据库
     * @param product 要添加的产品对象
     */
    fun addProduct(product: Product) {
        // 获取数据库连接并使用 `use` 函数确保资源释放
        Database().conn.use { conn ->
            // 准备插入产品的 SQL 查询
            conn.prepareStatement("insert into product(name, category, number, price) values(?,?,?,?)").use { ps ->
                // 设置 SQL 查询中的参数
                ps.setString(1, product.name)
                ps.setString(2, product.category)
                ps.setInt(3, product.number)
                ps.setInt(4, product.price)
                // 执行插入操作
                ps.executeUpdate()
            }
        }
    }

    /**
     * 从数据库获取所有产品（除去数量为0的产品）
     * @return 产品列表
     */
    fun listProducts(): List<Product> {
        val products = ArrayList<Product>()
        Database().conn.use { conn ->
            // 执行查询所有产品的 SQL 查询
            conn.createStatement().executeQuery("select * from product where number != 0 order by id").use { resultSet ->
                // 遍历查询结果集并构建产品列表
                while (resultSet.next()) {
                    val name = resultSet.getString("name")
                    val category = resultSet.getString("category")
                    val number = resultSet.getInt("number")
                    val price = resultSet.getInt("price")
                    products.add(Product(name, category, number, price))
                }
            }
        }
        return products
    }

    /**
     * 从数据库删除指定的产品
     * @param item 要删除的产品对象
     */
    fun deleteProduct(item: Product) {
        // 获取数据库连接并使用 `use` 函数确保资源释放
        Database().conn.use { conn ->
            // 准备删除产品的 SQL 查询
            conn.prepareStatement("delete from product where name = ? and category = ? and number = ? and price = ?").use { ps ->
                // 设置 SQL 查询中的参数
                ps.setString(1, item.name)
                ps.setString(2, item.category)
                ps.setInt(3, item.number)
                ps.setInt(4, item.price)
                // 执行删除操作
                ps.executeUpdate()
            }
        }
    }

    /**
     * 更新数据库中的一个已有的产品
     * @param name 产品的名称
     * @param category 产品的类别
     * @param item 新的产品对象，用于更新
     */
    fun updateProduct(name: String, category: String, item: Product) {
        // 获取数据库连接并使用 `use` 函数确保资源释放
        Database().conn.use { conn ->
            // 准备更新产品的 SQL 查询
            conn.prepareStatement("update product set number = ?, price = ? where name = ? and category = ?").use { ps ->
                // 设置 SQL 查询中的参数
                ps.setInt(1, item.number)
                ps.setInt(2, item.price)
                ps.setString(3, name)
                ps.setString(4, category)
                // 执行更新操作
                ps.executeUpdate()
            }
        }
    }

    /**
     * 通过名称或类别搜索产品
     * @param name 搜索的关键词，可以是产品名称或类别
     * @return 匹配的产品列表
     */
    fun searchProduct(name: String): List<Product> {
        val searchProducts = ArrayList<Product>()
        Database().conn.use { conn ->
            // 准备搜索产品的 SQL 查询
            conn.prepareStatement("select * from product where name like ? or category like ?").use { ps ->
                ps.setString(1, "%$name%")
                ps.setString(2, "%$name%")
                val resultSet = ps.executeQuery()
                // 遍历查询结果集并构建搜索结果列表
                while (resultSet.next()) {
                    val prodName = resultSet.getString("name")
                    val category = resultSet.getString("category")
                    val number = resultSet.getInt("number")
                    val price = resultSet.getInt("price")
                    searchProducts.add(Product(prodName, category, number, price))
                }
            }
        }
        return searchProducts
    }

    /**
     * 购买指定数量的产品，更新数据库中的库存
     * @param product 要购买的产品对象
     * @param amount 购买的数量
     */
    fun purchaseProduct(product: Product, amount: Int) {
        Database().conn.use { conn ->
            // 准备更新产品库存的 SQL 查询
            conn.prepareStatement("update product set number = (number - ?) where name = ? and category = ?").use { ps ->
                ps.setInt(1, amount)
                ps.setString(2, product.name)
                ps.setString(3, product.category)
                ps.executeUpdate()
            }
        }
    }




}
