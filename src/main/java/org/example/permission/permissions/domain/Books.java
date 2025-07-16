package org.example.permission.permissions.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yahoo.elide.annotation.CreatePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.annotation.LifeCycleHookBindings;
import com.yahoo.elide.annotation.ReadPermission;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
@Schema(extensions = {
        @Extension(properties = {
                @ExtensionProperty(name = "path", value = "books"),
                @ExtensionProperty(name = "entityName", value = "books")
        })
})
@Table(indexes = {
        @Index(name = "book_author_store_index", columnList = "store_id, author_id", unique = true)
})
@Include(name = "books")
@CreatePermission(expression = "permission Create")
@ReadPermission(expression = "permission Read")
public class Books extends AbstractEntity implements CodeAwareEntity {

    @Column(nullable = false)
    private String code;

    @JsonBackReference(value = "author-books")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
    private Author author;

    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
    private Store store;
}
