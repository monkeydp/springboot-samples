package com.monkeydp.springboot.sample.hibernate.search

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.hibernate.search.mapper.orm.Search
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @author iPotato-Work
 * @date 2021/3/19
 */
@Entity
@Indexed
class Product(
        @FullTextField
        var name: String,
) {
    @Id
    @GeneratedValue
    val id: Long = -1
}

@Repository
interface ProductRepo : JpaRepository<Product, Long>

@Api(tags = ["产品"])
@RestController
@RequestMapping("products")
class ProductController(
        private val repo: ProductRepo,
        private val entityManager: EntityManager,
) {
    @Suppress("UNCHECKED_CAST")
    @ApiOperation("产品列表")
    @GetMapping
    fun list(form: ListForm) =
            Search.session(entityManager).search(Product::class.java)
                    .where {
                        if (form.name == null) return@where it.matchAll()
                        it.match()
                                .fields("name")
                                .matching(form.name)
                    }
                    .fetchAll()
                    .hits() as List<Product>

    class ListForm(val name: String? = null)

    @ApiOperation("产品创建")
    @PostMapping
    fun create(@RequestBody form: CreateForm) =
            repo.save(Product(form.name))

    class CreateForm(val name: String)

    @ApiOperation("产品编辑")
    @PutMapping
    fun edit(@RequestBody form: EditForm) =
            form.run {
                repo.findById(id).get()
                        .also { it.name = name }
                        .run(repo::save)
            }

    class EditForm(
            val id: Long,
            val name: String,
    )

    @ApiOperation("产品删除")
    @DeleteMapping
    fun delete(@RequestBody form: DeleteForm) =
            repo.deleteById(form.id)

    class DeleteForm(val id: Long)
}
