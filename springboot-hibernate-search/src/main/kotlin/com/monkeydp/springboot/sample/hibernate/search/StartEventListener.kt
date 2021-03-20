package com.monkeydp.springboot.sample.hibernate.search

import org.hibernate.search.mapper.orm.Search
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * @author iPotato-Work
 * @date 2021/3/20
 */
@Component
class StartEventListener(
        private val entityManager: EntityManager,
) : ApplicationListener<ApplicationStartedEvent> {

    @Transactional
    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        // 同步 Database 已有数据到 Elasticsearch
        Search.session(entityManager)
                .massIndexer(Product::class.java)
                .startAndWait()
    }
}
