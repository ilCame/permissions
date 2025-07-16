package org.example.permission.permissions.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yahoo.elide.annotation.CreatePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.ReadPermission;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
@Schema(extensions = {
        @Extension(properties = {
                @ExtensionProperty(name = "path", value = "Store"),
                @ExtensionProperty(name = "entityName", value = "Store")
        })
})
@ToString(exclude = {"books"})
@CreatePermission(expression = "permission Create")
@ReadPermission(expression = "permission Read")
@Include(name = "store")
public class Store extends AbstractEntity implements CodeAwareEntity {

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @JsonBackReference(value = "books-store")
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
    private List<Books> books = new ArrayList<>();
}
