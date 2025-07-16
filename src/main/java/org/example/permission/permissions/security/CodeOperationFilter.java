package org.example.permission.permissions.security;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.Path;
import com.yahoo.elide.core.filter.expression.FilterExpression;
import com.yahoo.elide.core.filter.predicates.FalsePredicate;
import com.yahoo.elide.core.filter.predicates.InPredicate;
import com.yahoo.elide.core.security.RequestScope;
import com.yahoo.elide.core.security.checks.FilterExpressionCheck;
import com.yahoo.elide.core.type.Type;
import org.apache.commons.lang3.StringUtils;
import org.example.permission.permissions.domain.CodeAwareEntity;
import org.flywaydb.core.internal.util.CollectionsUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SecurityCheck("permission Read")
public class CodeOperationFilter extends FilterExpressionCheck<CodeAwareEntity> {

    @Override
    public FilterExpression getFilterExpression(Type<?> entityClass, RequestScope requestScope) {
        Class<?> underlyingClass = entityClass.getUnderlyingClass()
                .orElseThrow(() -> new IllegalArgumentException("Entity class is empty"));
        Set<String> codeHeaders =
                Optional.ofNullable(requestScope.getRoute().getHeaders())
                        .map(headers -> headers.get("X-Code-Permission"))
                        .flatMap(headers -> headers.stream().findFirst())
                        .filter(StringUtils::isNotBlank)
                        .map(header -> Set.of(header.toLowerCase().split(",")))
                        .orElse(Set.of());

        Path.PathElement codePath = new Path.PathElement(
                underlyingClass, String.class, "code");
        Path path = new Path(List.of(codePath));

        if (CollectionsUtils.hasItems(codeHeaders)) {
            String[] codesArray = codeHeaders.toArray(new String[0]);
            return new InPredicate(path, codesArray);
        } else {
            return new FalsePredicate(path);
        }
    }
}